package org.colva.common.helper.poi;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.colva.common.helper.poi.core.ExcelExporter;
import org.colva.common.helper.poi.core.GenericSXSSFExcelExporter;
import org.colva.common.helper.poi.core.GenericSXSSFExcelExporter.Builder;
import org.colva.common.helper.poi.template.SomeEntityTemplate;
import org.junit.Test;

/**
 * ExcelExporter单元测试
 * 
 * @description
 * @author piaoruiqing
 * @date: 2019/02/14 10:58
 *
 * @since JDK 1.8
 */
public class ExcelExporterTest {
    
    /**
     * 模板导出(Entity)
     * 
     * @author piaoruiqing
     * @date: 2019/02/15 13:28
     * 
     * @throws IOException
     */
    @Test
    public void templateEntityTest() throws IOException {
        
        String path = "/tmp/" + UUID.randomUUID().toString() + ".xlsx";
        
        Builder<SomeEntityTemplate> builder = 
            GenericSXSSFExcelExporter.<SomeEntityTemplate>builder().path(path).template(SomeEntityTemplate.class);
        
        try (ExcelExporter<SomeEntityTemplate> exporter = builder.build();) {
            int limit = 500;
            long offset = 0L;
            List<SomeEntityTemplate> list;
            do {
                list = SomeEntityTemplate.page(limit, offset);
                offset += limit;
                exporter.write(list);
            } while (!CollectionUtils.isEmpty(list));
        }
    }
    

    /**
     * 模板导出(Map)
     * 
     * @author piaoruiqing
     * @date: 2019/02/15 13:28
     * 
     * @throws IOException
     */
    @Test
    public void templateMapTest() throws IOException {
        
        String path = "/tmp/" + UUID.randomUUID().toString() + ".xlsx";
        
        Builder<Map<String, Object>> builder = 
            GenericSXSSFExcelExporter.<Map<String, Object>>builder().path(path).template(SomeEntityTemplate.class);
        
        try (ExcelExporter<Map<String, Object>> exporter = builder.build();) {
            int limit = 500;
            long offset = 0L;
            List<Map<String, Object>> list;
            do {
                list = SomeEntityTemplate.pageMap(limit, offset);
                offset += limit;
                exporter.write(list);
            } while (!CollectionUtils.isEmpty(list));
        }
    }

    /**
     * 自定义导出
     * 
     * @author piaoruiqing
     * @date: 2019/02/15 13:28
     * 
     * @throws IOException
     */
    @Test
    public void customizeEntityTest() throws IOException {
        
        String path = "/tmp/" + UUID.randomUUID().toString() + ".xlsx";
        
        Builder<SomeEntityTemplate> builder = 
            GenericSXSSFExcelExporter.<SomeEntityTemplate>builder()
            .path(path).title("列表").header(new String[] {"序号", "ID", "类型", "时间"})
            .parser((row, item) -> {
                row.createCell(0).setCellValue(row.getRowNum() - 1);
                row.createCell(1).setCellValue(item.getId());
                row.createCell(2).setCellValue("类型" + item.getSomeType());
                row.createCell(3).setCellValue(DateFormatUtils.format(item.getGmt(), "yyyy-MM-dd HH:mm:ss"));
            });
        
        try (ExcelExporter<SomeEntityTemplate> exporter = builder.build();) {
            int limit = 500;
            long offset = 0L;
            List<SomeEntityTemplate> list;
            do {
                list = SomeEntityTemplate.page(limit, offset);
                offset += limit;
                exporter.write(list);
            } while (!CollectionUtils.isEmpty(list));
        }
    }
    
}
