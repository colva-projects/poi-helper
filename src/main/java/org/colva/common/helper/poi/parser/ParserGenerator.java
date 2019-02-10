package org.colva.common.helper.poi.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.colva.common.helper.poi.annotation.ExcelConvert;
import org.colva.common.helper.poi.annotation.ExcelField;
import org.colva.common.helper.poi.annotation.ExcelTemplate;
import org.colva.common.helper.poi.converter.Converter;
import org.colva.common.helper.poi.converter.ConverterPool;
import org.colva.common.helper.poi.converter.GenericConverter;
import org.colva.common.helper.poi.parser.ExcelParser.ExcelParserBuilder;
import org.colva.common.helper.poi.util.ArrayUtils;
import org.colva.common.helper.poi.util.StringUtils;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * 模板解析器生成器
 * 
 * @description 
 * @author piaoruiqing
 * @date: 2019/02/07 10:43
 *
 * @since JDK 1.8
 */
public abstract class ParserGenerator {
    
    private static final ClassPool classPool = ClassPool.getDefault();
    static {
        classPool.insertClassPath(new ClassClassPath(ParserGenerator.class));
        classPool.importPackage(RowParser.class.getName());
        classPool.importPackage(Converter.class.getName());
        classPool.importPackage(ConverterPool.class.getName());
        classPool.importPackage(Row.class.getName());
        classPool.importPackage(Cell.class.getName());
        classPool.importPackage(StringUtils.class.getName());
        classPool.importPackage(Map.class.getName());
    }

    /**
     * 根据模板生成解析器
     * 
     * @author piaoruiqing
     * @date: 2019/02/07 16:11
     * 
     * @param template
     * @return
     * @throws CannotCompileException
     * @throws NotFoundException
     * @throws ReflectiveOperationException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ExcelParser<?> generate(Class<?> template) throws CannotCompileException, NotFoundException, ReflectiveOperationException {
        
        ExcelParserBuilder builder = ExcelParser.builder();
        ExcelTemplate excelTemplate = template.getAnnotation(ExcelTemplate.class);
        if (null == excelTemplate) {
            throw new IllegalArgumentException("param is not an ExcelTemplate");
        }
        // 标题
        builder.title(excelTemplate.title());
        String simpleName = template.getSimpleName();
        CtClass parser = classPool.makeClass(template.getPackage().getName() + "." + simpleName + "Parser");
        // 依赖
        classPool.importPackage(template.getName());
        // 接口
        parser.setInterfaces(new CtClass[] {classPool.get(RowParser.class.getName())});
        // 属性
        parser.addField(new CtField(classPool.get("double"), "offset", parser));
        String[] header = getHeader(template);
        parser.addMethod(CtNewMethod.make(getMetnodContent(header, template), parser));
        Class<?> clazz = parser.toClass();
        RowParser instance = (RowParser)clazz.newInstance();
        setOffset(header, template, instance);
        return builder.rowParser(instance).header(header).build();
    }
    
    /**
     * 设置offset
     * 
     * @author piaoruiqing
     * @date: 2019/02/07 12:59
     * 
     * @param header
     * @param template
     * @param instance
     * @throws ReflectiveOperationException
     */
    @SuppressWarnings("rawtypes")
    private static void setOffset(String[] header, Class<?> template, RowParser instance) throws ReflectiveOperationException {
        
        ExcelTemplate excelTemplate = template.getAnnotation(ExcelTemplate.class);
        double offset = 1D;
        if (StringUtils.isNotBlank(excelTemplate.title())) {
            offset--;
        }
        if (!ArrayUtils.isBlank(header)) {
            offset--;
        }
        Field field = instance.getClass().getDeclaredField("offset");
        field.setAccessible(true);
        field.set(instance, offset);
        field.setAccessible(false);
    }
    
    /**
     * 方法内容
     * 
     * @author piaoruiqing
     * @date: 2019/02/07 14:54
     * 
     * @param header
     * @param template
     * @param script
     * @return
     * @throws ReflectiveOperationException 
     */
    private static String getMetnodContent(String[] header, Class<?> template) throws ReflectiveOperationException {
        
        String name = template.getSimpleName();
        return 
            "public void apply(Row row, Object item) {" +
                "if (item instanceof " + name + "){" + 
                    name + " tmpl = (" + name + ")item;" + 
                    getScriptForEntity(template) + 
                "} else if (item instanceof Map) {" + 
                    "Map tmpl = (Map)item;" + 
                    getScriptForMap(template) + 
                "}" +
            "}";
    }
    
    /**
     * Entity解析
     * 
     * @author piaoruiqing
     * @date: 2019/02/07 12:37
     * 
     * @param template
     * @return
     * @throws ReflectiveOperationException
     */
    private static StringBuilder getScriptForEntity(Class<?> template) throws ReflectiveOperationException {
        
        StringBuilder script = new StringBuilder();
        for (Field field : template.getDeclaredFields()) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            Method getMethod = template.getDeclaredMethod("get" + StringUtils.toUpperCaseFirstOne(field.getName()));
            if (null == excelField || null == getMethod) {
                continue ;
            }
            ExcelConvert excelConvert = field.getAnnotation(ExcelConvert.class);
            Class<? extends Converter<?>> converter = excelConvert == null ? GenericConverter.class : excelConvert.using();
            if (converter == Converter.SequenceGenerator.class) {
                script.append(String.format("row.createCell(%d).setCellValue(row.getRowNum() + offset);", excelField.index()));
            } else {
                ConverterPool.getOrInit(converter);
                script.append(String.format("row.createCell(%d).setCellValue(ConverterPool.get(\"%s\").convert(tmpl.%s()));", 
                    excelField.index(), converter.getName(), getMethod.getName()));
            }
        }
        
        return script;
    }
    
    /**
     * Map解析
     * 
     * @author piaoruiqing
     * @date: 2019/02/07 12:37
     * 
     * @param template
     * @return
     * @throws ReflectiveOperationException
     */
    private static StringBuilder getScriptForMap(Class<?> template) throws ReflectiveOperationException {
        
        StringBuilder script = new StringBuilder();
        for (Field field : template.getDeclaredFields()) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (null == excelField) {
                continue ;
            }
            ExcelConvert excelConvert = field.getAnnotation(ExcelConvert.class);
            Class<? extends Converter<?>> converter = excelConvert == null ? GenericConverter.class : excelConvert.using();
            if (converter == Converter.SequenceGenerator.class) {
                script.append(String.format("row.createCell(%d).setCellValue(row.getRowNum() + offset);", excelField.index()));
            } else {
                ConverterPool.getOrInit(converter);
                script.append(String.format("row.createCell(%d).setCellValue(ConverterPool.get(\"%s\").convert(tmpl.getOrDefault(\"%s\", StringUtils.EMPTY)));", 
                    excelField.index(), converter.getName(), excelField.mapKey()));
            }
        }
        
        return script;
    }
    
    /**
     * 表头
     * 
     * @author piaoruiqing
     * @date: 2019/02/07 14:29
     * 
     * @param excelFields
     * @return
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     */
    private static String[] getHeader(Class<?> template) throws NoSuchMethodException, SecurityException {
        
        Field[] fields = template.getDeclaredFields();
        List<ExcelField> list = new ArrayList<>(fields.length);
        for (Field field : fields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            Method getMethod = template.getDeclaredMethod("get" + StringUtils.toUpperCaseFirstOne(field.getName()));
            if (null == excelField || null == getMethod) {
                continue ;
            }
            list.add(excelField);
        }
        
        list.sort((a, b) -> a.index() - b.index());
        ExcelField lastExcelField = list.get(list.size() - 1);
        String[] header = new String[lastExcelField.index()+1];
        list.forEach(v -> header[v.index()] = StringUtils.isBlank(v.value()) ? null : v.value());
        
        return header;
    }
    
}
