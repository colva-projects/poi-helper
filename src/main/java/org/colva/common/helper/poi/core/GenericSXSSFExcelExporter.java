package org.colva.common.helper.poi.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.colva.common.helper.poi.annotation.ExcelTemplate;
import org.colva.common.helper.poi.parser.ExcelParser;
import org.colva.common.helper.poi.parser.ParserPool;
import org.colva.common.helper.poi.parser.RowParser;
import org.colva.common.helper.poi.util.ArrayUtils;
import org.colva.common.helper.poi.util.StringUtils;

/**
 * 通用大批量Excel导出
 * 
 * <pre>
 * * 支持大批量分页写入数据(不会发生OOM)
 * * 数据会先写入到缓存(xml文件), 调用{@link #close()}后会输出到excel文件中
 * </pre>
 * 
 * @see {@link org.colva.common.helper.poi.parser.RowParser}
 * @description
 * @author piaoruiqing
 * @date: 2018/11/08 16:12
 *
 * @since JDK 1.8
 */
public class GenericSXSSFExcelExporter<T> implements ExcelExporter<T> {

    private String title;
    private String[] header;
    private RowParser<T> parser;
    private String path;
    
    private SXSSFWorkbook workbook;
    private Sheet sheet;
    private int index = 0;

    /**
     * @param builder
     * @throws FileNotFoundException
     */
    private GenericSXSSFExcelExporter(Builder<T> builder) throws FileNotFoundException {
        this(builder.path, builder.title, builder.header, builder.rowAccessWindowSize, builder.template, builder.parser);
    }

    /**
     * @param path
     * @param title
     * @param header
     * @param rowAccessWindowSize
     * @param parser
     * @throws FileNotFoundException
     */
    @SuppressWarnings("unchecked")
    private GenericSXSSFExcelExporter(String path, String title, String[] header, int rowAccessWindowSize, Class<?> template, RowParser<T> parser)
        throws FileNotFoundException {

        if (StringUtils.isBlank(path)) {
            throw new IllegalArgumentException("path must not be blank");
        }
        this.path = path;
        if (null != template) {
            ExcelParser<?> excelParser = ParserPool.getOrInit(template);
            this.title = excelParser.getTitle();
            this.header = excelParser.getHeader();
            this.parser = (RowParser<T>)excelParser.getRowParser();
        } else if (null != parser){
            this.title = title;
            this.header = header;
            this.parser = parser;
        } else {
            throw new IllegalArgumentException("template and parser cannot both be empty");
        }
        this.workbook = new SXSSFWorkbook(rowAccessWindowSize);
        this.sheet = workbook.createSheet();
        this.title();
        this.header();
    }

    /*
     * (non-Javadoc)
     * @see org.colva.common.helper.poi.core.ExcelExporter#write(java.util.List)
     */
    public void write(List<T> data) {
        if (null == data || data.isEmpty()) {
            return;
        }
        for (T item : data) {
            Row row = sheet.createRow(index++);
            parser.apply(row, item);
        }
    }

    /*
     * (non-Javadoc)
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            workbook.write(fileOutputStream);
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
            workbook.dispose();
        }
    }

    /**
     * 标题
     * 
     * @author piaoruiqing
     * @date: 2018/11/08 14:07
     *
     */
    private void title() {

        if (StringUtils.isBlank(title)) {
            return;
        }
        Row titleRow = sheet.createRow(index++);
        int length = (null == header || header.length <= 0) ? 1 : header.length;
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, length - 1));
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        Font font = workbook.createFont();
        font.setBold(true);
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);
        titleCell.setCellStyle(style);
    }

    /**
     * 表头
     * 
     * @author piaoruiqing
     * @date: 2019/02/08 12:47
     *
     */
    private void header() {

        if (ArrayUtils.isBlank(header)) {
            return;
        }
        Font font = workbook.createFont();
        font.setBold(true);
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);
        style.setBorderTop(BorderStyle.DASH_DOT);
        style.setBorderBottom(BorderStyle.DASH_DOT);
        Row row = sheet.createRow(index++);
        for (int i = 0; i < header.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(StringUtils.isBlank(header[i]) ? StringUtils.EMPTY : header[i]);
            cell.setCellStyle(style);
        }
    }

    /**
     * Creates builder to build {@link GenericSXSSFExcelExporter}.
     * 
     * @return created builder
     */
    public static <T> Builder<T> builder() {
        return new Builder<T>();
    }

    /**
     * Builder to build {@link GenericSXSSFExcelExporter}.
     */
    public static final class Builder<T> {
        private String title;
        private String[] header;
        private String path;
        private int rowAccessWindowSize = SXSSFWorkbook.DEFAULT_WINDOW_SIZE;
        private Class<?> template;
        private RowParser<T> parser;

        private Builder() {}

        public Builder<T> title(String title) {
            this.title = title;
            return this;
        }

        public Builder<T> header(String[] header) {
            this.header = header;
            return this;
        }

        public Builder<T> path(String path) {
            this.path = path;
            return this;
        }

        public Builder<T> rowAccessWindowSize(int rowAccessWindowSize) {
            this.rowAccessWindowSize = rowAccessWindowSize;
            return this;
        }

        public Builder<T> template(Class<?> template) {
            if (!template.isAnnotationPresent(ExcelTemplate.class)) {
                throw new IllegalArgumentException("param is not an ExcelTemplate");
            }
            this.template = template;
            return this;
        }

        public Builder<T> parser(RowParser<T> parser) {
            this.parser = parser;
            return this;
        }

        public GenericSXSSFExcelExporter<T> build() throws FileNotFoundException {
            return new GenericSXSSFExcelExporter<T>(this);
        }
    }

}
