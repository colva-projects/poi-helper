# poi-helper


> * poi-helper, 基于Apache poi
> * 支持大批量导出excel, 支持模板映射, 支持Map



## 目录

[TOC]

## 更新日志

| 版本  | 修订人   | 修订时间   | 修订内容                         |
| ----- | ------ | ---------- | ------------------------------ |
| 1.0.0 |   | 2019/02/10 | 支持大批量导出excel <br/>支持模板映射 <br/>支持自定义解析器<br/>支持Map |



## 快速开始


### 1. 示例

> 可参考项目中的单元测试

```java
// 模板导出(Entity)
ExcelExporter<SomeEntityTemplate> exporter = null;
try {
    exporter = GenericSXSSFExcelExporter.<SomeEntityTemplate>builder()
        .path(path)
        .template(SomeEntityTemplate.class)
        .build();
    exporter.write(data);				// 写入数据, 建议分页查询多次写入, 以防查询操作出现OOM
} finally {
    IOUtils.closeQuietly(exporter);		// 输出Excel文件到path
}

// 模板导出(Map)
ExcelExporter<Map<String, Object>> exporter = null;
try {
    exporter = GenericSXSSFExcelExporter.<Map<String, Object>>builder()
        .path(path)
        .template(SomeEntityTemplate.class)
        .build();
    exporter.write(data);
} finally {
    IOUtils.closeQuietly(exporter);
}

// 自定义导出
ExcelExporter<AnyObject> exporter = null;
try {
    exporter = GenericSXSSFExcelExporter.<AnyObject>builder()
        .path(path)
        .title("AnyObject")
        .header(new String[] {"序号", "A", "B", "时间"})
        .parser((row, item) -> {	// 自定义导出不定义template, 但必须自定义parser
            row.createCell(0).setCellValue(row.getRowNum() - 1);
            row.createCell(1).setCellValue(item.getA());
            row.createCell(2).setCellValue(item.getB());
            row.createCell(3).setCellValue(
                    DateFormatUtils.format(item.getGmt(), "yyyy-MM-dd HH:mm:ss")
                );
        }).build();
    exporter.write(data);
} finally {
    IOUtils.closeQuietly(exporter);
}

// 模板
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ExcelTemplate(title = "列表")
public class SomeEntityTemplate {
    @ExcelConvert(using = Converter.SequenceGenerator.class)
    @ExcelField(index = 0, value = "序号")
    private Long index;
    @ExcelField(index = 1, value = "ID", mapKey = "id")
    private Long id;
    @ExcelConvert(using = SomeTypeConverter.class)
    @ExcelField(index = 2, value = "类型", mapKey = "some_type")
    private Integer someType;
    @ExcelConvert(using = DateConverter.Milliseconds.class)
    @ExcelField(index = 3, value = "时间", mapKey = "gmt")
    private Long gmt;
}
```



### 2. 快速入门

#### `GenericSXSSFExcelExporter`

> 通用Excel导出

* 支持大批量分页写入数据(不会发生OOM)
* 支持模板
* 支持Map
* 支持自定义解析器

#### `Converter`

> 转换器接口
> 可实现`Converter`接口自定义转换器

- `Converter.SequenceGenerator`: 序列生成器
  - 使用此转换器会将该列填充为对应行编号(不包括标题和表头)
  - 从1开始
- `DateConverter.Milliseconds`: 日期转换器(毫秒转日期)
- `DateConverter.Seconds`: 日期转换器(秒转日期)

#### `@ExcelTemplate`

> 模板类

* `title`: 指定导出Excel的标题

#### `@ExcelField`

> 列

* `value`: 列名(表头文本)
* `index`: 位置
* `mapKey`: MapKey, 若使用Map作为导出数据, 则须指定字段对应的MapKey

#### `@ExcelConvert`

> 转换器

* `using`: 指定转换器, 默认`GenericConverter`




## TODO

* 优化缓存

