package org.colva.common.helper.poi.converter;

/**
 * 类型转换器
 * 
 * @description 
 * @author piaoruiqing
 * @date: 2019/02/17 18:14
 *
 * @since JDK 1.8
 */
public class SomeTypeConverter implements Converter<Integer> {

    @Override
    public String convert(Integer value) {
        return "类型" + value;
    }

}
