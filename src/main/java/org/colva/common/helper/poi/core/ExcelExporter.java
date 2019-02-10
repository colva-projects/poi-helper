package org.colva.common.helper.poi.core;

import java.io.Closeable;
import java.util.List;

/**
 * Excel导出
 * 
 * @description
 * @author piaoruiqing
 * @date: 2019/02/05 17:29
 *
 * @since JDK 1.8
 */
public interface ExcelExporter<T> extends Closeable {

    /**
     * 写入数据
     * 
     * @author piaoruiqing
     * @date: 2019/02/05 17:31
     * 
     * @param data
     */
    void write(List<T> data);

}
