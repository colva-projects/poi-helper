package org.colva.common.helper.poi.util;

/**
 * String工具类
 * 
 * @description 
 * @author piaoruiqing
 * @date: 2019/02/07 14:03
 *
 * @since JDK 1.8
 */
public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 首字母大写
     * 
     * @date 2018/04/01 21:20:03
     * @author Ruiqing.Piao
     * @param str
     * @return
     */
    public static String toUpperCaseFirstOne(String str) {
        
        if(isBlank(str)) {
            return str;
        } else if (Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return new StringBuilder().append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
        }
    }
}
