package org.colva.common.helper.poi.parser;

import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.NotFoundException;

/**
 * 解析器池
 * 
 * @description
 * @author piaoruiqing
 * @date: 2019/02/07 10:36
 *
 * @since JDK 1.8
 */
public abstract class ParserPool {

    private static Map<Class<?>, ExcelParser<?>> POOL = new HashMap<>(16);

    /**
     * 获取解析器<br/>
     * 若不存在则初始化
     * 
     * @author piaoruiqing
     * @date: 2019/02/07 10:39
     * 
     * @param clazz
     * @return
     */
    public static ExcelParser<?> getOrInit(Class<?> clazz) {

        ExcelParser<?> parser = POOL.get(clazz);
        if (null != parser) {
            return parser;
        }
        synchronized (clazz) {
            if (POOL.containsKey(clazz)) {
                return POOL.get(clazz);
            }
            try {
                ExcelParser<?> generate = ParserGenerator.generate(clazz);
                POOL.put(clazz, generate);
                return generate;
            } catch (CannotCompileException | NotFoundException | ReflectiveOperationException e) {
                throw new RuntimeException("init excel parser error", e);
            }
        }
    }

}
