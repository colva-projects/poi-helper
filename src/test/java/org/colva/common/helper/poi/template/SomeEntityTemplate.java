package org.colva.common.helper.poi.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.colva.common.helper.poi.annotation.ExcelConvert;
import org.colva.common.helper.poi.annotation.ExcelField;
import org.colva.common.helper.poi.annotation.ExcelTemplate;
import org.colva.common.helper.poi.converter.Converter;
import org.colva.common.helper.poi.converter.DateConverter;
import org.colva.common.helper.poi.converter.SomeTypeConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SomeEntity导出模板
 * 
 * @description
 * @author piaoruiqing
 * @date: 2019/02/13 09:25
 *
 * @since JDK 1.8
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ExcelTemplate(title = "列表")
public class SomeEntityTemplate {

    @ExcelConvert(using = Converter.SequenceGenerator.class)
    @ExcelField(index = 0, value = "序号")
    private Long index;
    @ExcelField(index = 1, value = "ID", mapKey = "id")
    private Long id;
    @ExcelConvert(using = SomeTypeConverter.class)
    @ExcelField(index = 2, value = "类型", mapKey = "some_type")
    private Integer someType;
    @ExcelConvert(using = DateConverter.Milliseconds.class)
    @ExcelField(index = 3, value = "时间", mapKey = "gmt")
    private Long gmt;
    
    /**
     * 模拟分页接口
     * 
     * @author piaoruiqing
     * @date: 2019/02/12 18:08
     * 
     * @param limit
     * @param offset
     * @return
     */
    public static List<SomeEntityTemplate> page(int limit, long offset) {

        if (offset > 65535L) {
            return Collections.emptyList();
        }
        List<SomeEntityTemplate> list = new ArrayList<>(limit);
        for (int index = 0; index < limit; index++) {
            SomeEntityTemplate item = new SomeEntityTemplate(null, (long)index, ThreadLocalRandom.current().nextInt(2), System.currentTimeMillis());
            list.add(item);
        }
        return list;
    }
    
    /**
     * 模拟分页接口
     * 
     * @author piaoruiqing
     * @date: 2019/02/12 18:08
     * 
     * @param limit
     * @param offset
     * @return
     */
    public static List<Map<String, Object>> pageMap(int limit, long offset) {
        
        if (offset > 65535L) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> list = new ArrayList<>(limit);
        for (int index = 0; index < limit; index++) {
            Map<String , Object> item = new HashMap<>();
            item.put("id", index);
            item.put("some_type", ThreadLocalRandom.current().nextInt(2));
            item.put("gmt", System.currentTimeMillis());
            list.add(item);
        }
        return list;
    }
    
}
