package org.colva.common.helper.poi.converter;

/**
 * 转换器
 * 
 * @description
 * @author piaoruiqing
 * @date: 2019/02/06 10:37
 *
 * @since JDK 1.8
 */
public interface Converter<IN> {

    /**
     * 序列生成
     * 
     * <pre>
     * 使用此转换器会将该列填充为对应行编号(不包括标题和表头)
     * 从1开始
     * </pre>
     * 
     * @description
     * @author piaoruiqing
     * @date: 2019/02/14 15:07
     *
     * @since JDK 1.8
     */
    public static abstract class SequenceGenerator implements Converter<Long> {}

    /**
     * 转换
     * 
     * @author piaoruiqing
     * @date: 2019/02/13 10:44
     * 
     * @param value
     * @return
     */
    public abstract String convert(IN value);

}
