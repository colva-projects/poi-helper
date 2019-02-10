package org.colva.common.helper.poi.converter;

/**
 * 通用转换器
 * 
 * @description
 * @author piaoruiqing
 * @date: 2019/02/06 11:16
 *
 * @since JDK 1.8
 */
public class GenericConverter implements Converter<Object> {

    public static final Converter<?> INSTANCE = new GenericConverter();

    /*
     * (non-Javadoc)
     * @see org.colva.common.helper.poi.converter.Converter#convert(java.lang.Object)
     */
    @Override
    public String convert(Object value) {
        return null == value ? null : value.toString();
    }
}
