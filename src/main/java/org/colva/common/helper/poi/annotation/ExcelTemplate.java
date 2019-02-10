package org.colva.common.helper.poi.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Excel模板
 * 
 * <pre>
 * 添加该注解的实体, 可作为ExcelExporter解析的模板
 * </pre>
 * 
 * @see {@link org.colva.common.helper.poi.parser.ParserGenerator}
 * 
 * @description
 * @author piaoruiqing
 * @date: 2019/02/07 10:22
 *
 * @since JDK 1.8
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface ExcelTemplate {

    /**
     * 标题<br/>
     * 默认无标题
     * 
     * @author piaoruiqing
     * @date: 2019/02/07 13:16
     * 
     * @return
     */
    String title() default "";

}
