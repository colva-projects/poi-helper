package org.colva.common.helper.poi.util;


/**
 * 数组工具类
 * 
 * @description 
 * @author piaoruiqing
 * @date: 2019/02/07 14:04
 *
 * @since JDK 1.8
 */
public abstract class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {

    /**
     * 是否空数组
     * <pre>
     * [null, null] => true
     * []           => true
     * [1]          => false
     * [1, null]    => false
     * </pre>
     * @author piaoruiqing
     * @date: 2019/02/07 14:06
     * 
     * @param array
     * @return
     */
    public static boolean isBlank(final Object[] array) {

        if (isEmpty(array)) {
            return true;
        }
        for (Object obj : array) {
            if (null != obj) {
                return false;
            }
        }
        return true;
    }
}
