package org.colva.common.helper.poi.converter;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期转换器
 * 
 * @description
 * @author piaoruiqing
 * @date: 2019/02/06 10:46
 *
 * @since JDK 1.8
 */
public abstract class DateConverter implements Converter<Long> {

    /** 毫秒转日期 */
    public static final class Milliseconds extends DateConverter {};

    /** 秒转日期 */
    public static final class Seconds extends DateConverter {
        @Override
        public String convert(Long value) {
            return null == value ? null : super.convert(value * 1000L);
        }
    };

    /*
     * (non-Javadoc)
     * @see org.colva.common.helper.poi.converter.Converter#convert(java.lang.Object)
     */
    @Override
    public String convert(Long value) {
        return null == value ? null : DateFormatUtils.format(value, "yyyy-MM-dd HH:mm:ss");
    }
}
