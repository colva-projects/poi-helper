package org.colva.common.helper.poi.parser;

import lombok.Builder;
import lombok.Getter;

/**
 * Excel解析器
 * 
 * @description 
 * @author piaoruiqing
 * @date: 2019/02/07 11:02
 *
 * @since JDK 1.8
 */
@Getter
@Builder
public class ExcelParser<T> {
    
    /** 标题 */
    private String title;
    /** 表头 */
    private String[] header;
    /** 行解析器 */
    private RowParser<T> rowParser;

}
