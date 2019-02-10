package org.colva.common.helper.poi.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.colva.common.helper.poi.converter.Converter;
import org.colva.common.helper.poi.converter.GenericConverter;

/**
 * Excel转换器
 * 
 * @description
 * @author piaoruiqing
 * @date: 2019/02/06 10:35
 *
 * @since JDK 1.8
 */
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelConvert {

    /**
     * 转换器
     * 
     * @see {@link org.colva.common.helper.poi.converter.Converter}
     * 
     * @author piaoruiqing
     * @date: 2019/02/06 13:44
     * 
     * @return
     */
    public Class<? extends Converter<?>> using() default GenericConverter.class;
}
