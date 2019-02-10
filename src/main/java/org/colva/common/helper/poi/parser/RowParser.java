package org.colva.common.helper.poi.parser;

import org.apache.poi.ss.usermodel.Row;

/**
 * 行解析器
 * 
 * @see {@link org.colva.common.helper.poi.core.GenericSXSSFExcelExporter}
 * 
 * @description
 * @author piaoruiqing
 * @date: 2019/02/05 17:26
 *
 * @since JDK 1.8
 */
@FunctionalInterface
public interface RowParser<T> {

    /**
     * 解析行
     * 
     * @author piaoruiqing
     * @date: 2019/02/05 16:14
     * 
     * @param row
     * @param item
     */
    void apply(Row row, T item);

}
