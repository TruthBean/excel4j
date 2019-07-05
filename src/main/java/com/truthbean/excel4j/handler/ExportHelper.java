package com.truthbean.excel4j.handler;

import com.truthbean.excel4j.annotation.ExcelEntityHandler;
import com.truthbean.excel4j.common.Constants;
import com.truthbean.excel4j.entity.*;
import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;
import com.truthbean.excel4j.util.CellStyleHelper;
import com.truthbean.excel4j.util.ExcelUtils;
import com.truthbean.excel4j.util.FileUtils;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public final class ExportHelper {

    private ExportHelper() {
    }

    /**
     * slf4j logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportHelper.class);


    public static synchronized <T> String toExcelFile(List<T> list, Class<T> cellModelClass, String fileName,
                                                      String distFileDir) {
        ExcelModel excelModel = ExportHelper.handleData(list, cellModelClass);
        return ExportHelper.writeToFile(excelModel, fileName, distFileDir);
    }

    public static synchronized <T> String toExcelFile(List<T> list, Class<T> cellModelClass, String bigTitle,
                                                      String fileName, String distFileDir) {
        ExcelModel excelModel = ExportHelper.handleData(list, cellModelClass, bigTitle);
        return ExportHelper.writeToFile(excelModel, fileName, distFileDir);
    }


    /**
     * handle data to excel
     *
     * @param data   data
     * @param tClass class
     * @param <T>    T
     * @return excel info
     */
    public static synchronized <T> ExcelModel handleData(List<T> data, Class<T> tClass) {
        final ExcelEntityHandler<T> entityHandler = new ExcelEntityHandler<>(tClass);
        ExcelModel excelModel = entityHandler.handleExcelTitle();
        entityHandler.handleExcelContent(excelModel, data);
        return excelModel;
    }

    /**
     * handle data to excel
     *
     * @param data   data
     * @param tClass class
     * @param <T>    T
     * @return excel info
     */
    public static synchronized <T> ExcelModel handleData(List<T> data, Class<T> tClass, String bigTitle) {
        final ExcelEntityHandler<T> entityHandler = new ExcelEntityHandler<>(tClass);
        ExcelModel excelModel = entityHandler.handleExcelTitle();

        //big title
        CellModel cellModel = new CellModel();
        cellModel.setValue(bigTitle);

        CellValueModel model = new CellValueModel();
        model.setValueType(CellValueType.TEXT);
        cellModel.setValueModel(model);

        excelModel.setBigTitle(cellModel);

        //should have big title
        excelModel.setNoBigTitle(false);

        entityHandler.handleExcelContent(excelModel, data);
        return excelModel;
    }

    /**
     * 导出数据到本地excel中
     *
     * @param excelModel  excel info
     * @param filename    文件名
     * @param distFileDir 目标路径
     * @return filePath
     */
    public static synchronized String writeToFile(ExcelModel excelModel, String filename, String distFileDir) {
        //是否是2003格式的excel
        boolean isExcel2003 = ExcelUtils.isExcel2003File(filename);
        excelModel.setExcel2003(isExcel2003);

        String filePath = distFileDir + filename;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        List<List<RowModel>> contentList = excelModel.getBigDataContent();
        if (contentList == null || contentList.isEmpty()) {
            if (excelModel.getContent() != null && !excelModel.getContent().isEmpty()) {
                contentList = new ArrayList<>();
                contentList.add(excelModel.getContent());
            }
        }

        Workbook workbook;

        try {
            if (contentList != null && !contentList.isEmpty()) {
                int contentListSize = contentList.size();
                int begin = 0;

                int i = 0;
                do {
                    excelModel.setContent(contentList.get(i));
                    workbook = writeExcel(excelModel, isExcel2003, begin);
                    excelModel.setWorkbook(workbook);
                    i++;
                    begin += contentList.get(i - 1).size();
                } while (i < contentListSize);

            } else {
                //写一个内容为空的excel
                excelModel.setContent(new ArrayList<>());
                workbook = writeExcel(excelModel, isExcel2003, 0);
                excelModel.setWorkbook(workbook);
            }

            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            workbook.close();
            fileOut.flush();
            fileOut.close();

        } catch (IOException e) {
            LOGGER.error("", e);
        }
        LOGGER.info("filePath is " + filePath);

        return filename;
    }

    private static Workbook writeExcel(ExcelModel excelModel, boolean isExcel2003, int begin) {
        //处理excel表头
        writeExcelSheetTitle(excelModel);

        Sheet sheet = excelModel.getSheet();
        Workbook workbook = excelModel.getWorkbook();
        //列数
        int titleCount = excelModel.getTitles().getCellModels().size();

        //总的记录数
        int contentCount = excelModel.getContent().size();
        Row row;
        Cell cell;
        CellStyle cellStyle = CellStyleHelper.setContentDefaultStyle(workbook);
        // 写内容
        for (int i = 0; i < contentCount; i++) {
            RowModel contents = excelModel.getContent().get(i);
            // 新建一行
            row = sheet.createRow(i + 2 + begin);

            for (int j = 0; j < titleCount; j++) {
                // 新建一个单元格
                cell = row.createCell(j);

                //设置内容样式
                cell.setCellStyle(cellStyle);

                CellModel cellModel = contents.getCellModels().get(j);
                if (cellModel.getValue() == null || "null".equals(cellModel.getValue())) {
                    cellModel.setValue("");
                }
                handleValue(cellModel, excelModel.getContentRowHeight(), isExcel2003, cell, excelModel.getWorkbook(), sheet);
            }
            if (excelModel.getContentRowHeight() <= 0) {
                row.setHeight((short) -1);
            } else {
                row.setHeight((short) (20 * excelModel.getContentRowHeight()));
            }
        }
        return workbook;
    }

    /**
     * handle file
     *
     * @param value       File or file name or byteArray data or inputStream
     * @param cell        new cell
     * @param workbook    excel workbook
     * @param pictureInfo picture info
     */
    private static void handlePictureValue(Object value, Cell cell, Workbook workbook, Sheet sheet,
                                           PictureInfo pictureInfo, int rowHeight) {
        byte[] data = FileUtils.rawValueToByteArray(value);
        if (data != null && pictureInfo.getType() != null) {
            int pictureIdx = workbook.addPicture(data, pictureInfo.getType().getType());

            CreationHelper helper = workbook.getCreationHelper();
            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();

            // 图片插入坐标
            anchor.setCol1(cell.getColumnIndex());
            anchor.setRow1(cell.getRowIndex());
            anchor.setDx1(6);
            anchor.setDy1(6);
            // 插入图片
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            double scale = (rowHeight - 12.0) * 20 / 9 / pictureInfo.getHeight();
            System.out.println(scale);
            pict.resize(scale, scale);
        }
    }

    public static int getAnchorX(int px, int colWidth) {
        return (int) Math.round(((double) 701 * 16000.0 / 301) * ((double) 1 / colWidth) * px);
    }

    public static int getAnchorY(int px, int rowHeight) {
        return (int) Math.round(((double) 144 * 8000 / 301) * ((double) 1 / rowHeight) * px);
    }

    public static int getRowHeight(int px) {
        return (int) Math.round(((double) 4480 / 300) * px);
    }

    public static int getColWidth(int px) {
        return (int) Math.round(((double) 10971 / 300) * px);
    }

    private static void handleDoubleValue(Object value, Cell cell) {
        if (value instanceof BigDecimal) {
            value = ((BigDecimal) value).doubleValue();
            cell.setCellValue((double) value);
        }
        if (value instanceof BigInteger) {
            value = ((BigInteger) value).longValue();
            cell.setCellValue((long) value);
        }
        if (value.getClass().equals(int.class) || value instanceof Integer) {
            cell.setCellValue((int) value);
        }
        if (value.getClass().equals(short.class) || value instanceof Short) {
            cell.setCellValue((short) value);
        }
        if (value.getClass().equals(long.class) || value instanceof Long) {
            cell.setCellValue((long) value);
        }
    }

    private static void handleValue(CellModel cellModel, int rowHeight, boolean isExcel2003, Cell cell, Workbook workbook, Sheet sheet) {
        Object value = cellModel.getValue();
        CellValueModel valueModel = cellModel.getValueModel();

        CellValueType valueType = valueModel.getValueType();
        PictureInfo pictureInfo = valueModel.getPictureInfo();

        CellEntityValueHandler valueHandler = valueModel.getValueHandler();
        if (isExcel2003) {
            switch (valueType) {
                case NUMBER:
                    handleDoubleValue(value, cell);
                    break;
                case DATE:
                    if (value instanceof Date) {
                        cell.setCellValue(new HSSFRichTextString((String) valueHandler.format(value)));
                    }
                    break;
                case PICTURE:
                    handlePictureValue(value, cell, workbook, sheet, pictureInfo, rowHeight);
                    break;
                case TEXT:
                    cell.setCellValue(new HSSFRichTextString((String) valueHandler.format(value)));
                default:
                    break;
            }
        } else {
            switch (valueType) {
                case NUMBER:
                    handleDoubleValue(value, cell);
                    break;
                case DATE:
                    if (value instanceof Date) {
                        cell.setCellValue(new XSSFRichTextString((String) valueHandler.format(value)));
                    }
                    break;
                case PICTURE:
                    handlePictureValue(value, cell, workbook, sheet, pictureInfo, rowHeight);
                    break;
                case TEXT:
                    cell.setCellValue(new XSSFRichTextString((String) valueHandler.format(value)));
                default:
                    break;
            }
        }

    }

    /**
     * 处理excel表头
     *
     * @param excelModel excel info
     */
    private static void writeExcelSheetTitle(ExcelModel excelModel) {
        Workbook workbook = excelModel.getWorkbook();

        Sheet sheet;
        int begin;

        // 创建新HSSFWorkbook对象
        if (workbook == null) {
            if (excelModel.isExcel2003()) {
                workbook = new HSSFWorkbook();
                //EXCEL2003格式最大行数是65535
                if (excelModel.getContent().size() >= Constants.EXCEL2003_MAX_ROW) {
                    throw new IllegalStateException();
                }
            } else {
                workbook = new SXSSFWorkbook();
            }

            //workbook
            excelModel.setWorkbook(workbook);

            //设置样式
            //大标题样式
            CellStyle bigTitleStyle = CellStyleHelper.setBigTitleDefaultStyle(workbook);
            CellModel bigTitle = excelModel.getBigTitle();

            if (!excelModel.isNoBigTitle()) {
                CellStyleModel bigTitleStyleModel = bigTitle.getStyleModel();
                //if style is null, do this
                if (bigTitleStyleModel == null) {
                    bigTitleStyleModel = new CellStyleModel();
                }

                bigTitleStyleModel.setCellStyle(bigTitleStyle);
                bigTitle.setStyleModel(bigTitleStyleModel);
            }

            //标题样式
            CellStyle titleStyle = CellStyleHelper.setTitleDefaultStyle(workbook);
            CellStyleModel styleModel;
            for (CellModel title : excelModel.getTitles().getCellModels()) {
                styleModel = title.getStyleModel();
                //if style is null, do this
                if (styleModel == null) {
                    styleModel = new CellStyleModel();
                }

                styleModel.setCellStyle(titleStyle);
                title.setStyleModel(styleModel);
            }

            // 创建新的sheet对象
            sheet = workbook.createSheet(excelModel.getSheetName());
            excelModel.setSheet(sheet);
            begin = setExcelTitle(excelModel);

            excelModel.setBegin(begin);
        } else {
            sheet = workbook.getSheet(excelModel.getSheetName());
            excelModel.setSheet(sheet);
        }
    }

    /**
     * 设置excel标题
     *
     * @param excelModel bigTitle    第一行标题
     *                   titleStrs   数据标题
     *                   sheet       excel里面的sheet
     *                   isExcel2003 是否是2003格式的excel
     *                   begin       开始插入excel的行数
     * @return 最好插入excel的行数
     */
    private static int setExcelTitle(ExcelModel excelModel) {
        List<CellModel> titleCellEntities = excelModel.getTitles().getCellModels();
        int titleCount = titleCellEntities.size();

        Sheet sheet = excelModel.getSheet();

        //创建第一行
        if (!excelModel.isNoBigTitle()) {
            //单元格范围 参数（int firstRow, int lastRow, int firstCol, int lastCol)
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, titleCount - 1);
            //添加合并单元格
            sheet.addMergedRegion(cellRangeAddress);

            //创建单元格并接设置值为富文本
            Row bigTitleRow = sheet.createRow(0);
            Cell first = bigTitleRow.createCell(0);
            RichTextString str;
            CellModel bigTitle = excelModel.getBigTitle();
            CellStyleModel styleModel = bigTitle.getStyleModel();

            if (excelModel.isExcel2003()) {
                str = new HSSFRichTextString((String) bigTitle.getValue());
            } else {
                str = new XSSFRichTextString((String) bigTitle.getValue());
            }

            first.setCellValue(str);
            first.setCellStyle(styleModel.getCellStyle());
            bigTitleRow.setHeightInPoints(22);
        }

        //第二行，标题行
        int titleRowNum = excelModel.isNoBigTitle() ? 0 : 1;
        Row titleRow = sheet.createRow(titleRowNum);

        //20像素
        titleRow.setHeightInPoints(20);

        // 写标题
        Cell cell;
        int begin = excelModel.getBegin();

        CellModel cellModel;
        CellStyleModel styleModel;

        for (int k = 0; k < titleCount; k++) {
            // 新建一个单元格
            cell = titleRow.createCell(k);
            cellModel = titleCellEntities.get(k);
            styleModel = cellModel.getStyleModel();

            //设置标题样式
            cell.setCellStyle(styleModel.getCellStyle());
            cell.setCellType(CellType.STRING);
            cell.setCellValue((String) titleCellEntities.get(k).getValue());
            //设置列宽
            sheet.setColumnWidth(k, styleModel.getColumnWidth());
            begin = sheet.getLastRowNum() - titleRowNum;
        }

        //冻结第一、二行
        sheet.createFreezePane(0, 2);
        return begin;
    }


}
