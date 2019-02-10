package org.colva.common.helper.poi.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Excel字段
 * 
 * @description
 * @author piaoruiqing
 * @date: 2019/02/06 10:29
 *
 * @since JDK 1.8
 */
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelField {

    /**
     * 表头名称
     * 
     * @author piaoruiqing
     * @date: 2019/02/15 09:39
     * 
     * @return
     */
    String value() default "";

    /**
     * 位置
     * 
     * @author piaoruiqing
     * @date: 2019/02/15 09:39
     * 
     * @return
     */
    int index();
    
    /**
     * Map key
     * <pre>
     * 若使用Map作为导出数据, 则须指定字段对应的MapKey
     * </pre>
     * 
     * @author piaoruiqing
     * @date: 2019/02/15 11:41
     * 
     * @return
     */
    String mapKey() default "";

}
