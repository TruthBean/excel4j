package com.truthbean.excel4j.handler;

import com.truthbean.excel4j.annotation.ExcelEntityHandler;
import com.truthbean.excel4j.common.Constants;
import com.truthbean.excel4j.entity.CellEntity;
import com.truthbean.excel4j.entity.CellEntityValueClass;
import com.truthbean.excel4j.entity.ExcelInfo;
import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;
import com.truthbean.excel4j.util.CellStyleHelper;
import com.truthbean.excel4j.util.ExcelUtils;
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
 * @since 2018-01-14 20:29
 */
public final class ExportHelper {

    private ExportHelper() {
    }

    /**
     * slf4j logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportHelper.class);

    /**
     * handle data to excel
     * @param data data
     * @param tClass class
     * @param <T> T
     * @return excel info
     * @throws Exception
     *      throw exception when new instance
     */
    public static synchronized <T> ExcelInfo handleData(List<T> data, Class<T> tClass) throws Exception {
        ExcelEntityHandler<T> entityHandler = new ExcelEntityHandler<>(tClass);
        T emptyModel = tClass.getDeclaredConstructor().newInstance();
        ExcelInfo excelInfo = entityHandler.handleExcelTitle(emptyModel);
        entityHandler.handleExcelContent(excelInfo, data);
        return excelInfo;
    }

    /**
     * 导出数据到本地excel中
     *
     * @param excelInfo excel info
     * @param filename    文件名
     * @param distFileDir 目标路径
     *
     * @return filePath
     */
    public static synchronized String writeToFile(ExcelInfo excelInfo, String filename, String distFileDir) {
        //是否是2003格式的excel
        boolean isExcel2003 = ExcelUtils.isExcel2003File(filename);
        excelInfo.setExcel2003(isExcel2003);

        String filePath = distFileDir + filename;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        List<List<List<CellEntity>>> contentList = excelInfo.getBigDataContent();
        if (contentList == null || contentList.isEmpty()) {
            if (excelInfo.getContent() != null && !excelInfo.getContent().isEmpty()) {
                contentList = new ArrayList<>();
                contentList.add(excelInfo.getContent());
            }
        }

        Workbook workbook;

        try {
            if (contentList != null && !contentList.isEmpty()) {
                int contentListSize = contentList.size();
                int begin = 0;

                int i = 0;
                do {
                    excelInfo.setContent(contentList.get(i));
                    workbook = writeExcel(excelInfo, isExcel2003, begin);
                    excelInfo.setWorkbook(workbook);
                    i++;
                    begin += contentList.get(i - 1).size();
                } while (i < contentListSize);

            } else {
                //写一个内容为空的excel
                excelInfo.setContent(new ArrayList<>());
                workbook = writeExcel(excelInfo, isExcel2003, 0);
                excelInfo.setWorkbook(workbook);
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

    private static Workbook writeExcel(ExcelInfo excelInfo, boolean isExcel2003, int begin) {

        //处理excel表头
        writeExcelSheetTitle(excelInfo);

        Sheet sheet = excelInfo.getSheet();
        Workbook workbook = excelInfo.getWorkbook();
        //列数
        int titleCount = excelInfo.getTitles().size();

        //总的记录数
        int contentCount = excelInfo.getContent().size();
        Row row;
        Cell cell;
        // 写内容
        for (int i = 0; i < contentCount; i++) {
            List<CellEntity> contents = excelInfo.getContent().get(i);
            // 新建一行
            row = sheet.createRow(i + 2 + begin);

            for (int j = 0; j < titleCount; j++) {
                // 新建一个单元格
                cell = row.createCell(j);

                //设置内容样式
                cell.setCellStyle(CellStyleHelper.setContentDefaultStyle(workbook));

                CellEntity cellEntity = contents.get(j);
                if (cellEntity.getValue() == null || "null".equals(cellEntity.getValue())) {
                    cellEntity.setValue("");
                }
                handleValue(cellEntity, isExcel2003, cell);
            }
        }
        return workbook;
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

    private static void handleValue(CellEntity cellEntity, boolean isExcel2003, Cell cell) {
        Object value = cellEntity.getValue();
        CellEntityValueClass valueClass = cellEntity.getValueClass();
        CellEntityValueHandler valueHandler = cellEntity.getValueHandler();
        if (isExcel2003) {
            switch (valueClass) {
                case DOUBLE:
                    handleDoubleValue(value, cell);
                    break;
                case DATE:
                    if (value instanceof Date) {
                        cell.setCellValue(new HSSFRichTextString((String) valueHandler.format(value)));
                    }
                    break;
                case TEXT:
                    cell.setCellValue(new HSSFRichTextString((String) valueHandler.format(value)));
                default:
                    break;
            }
        } else {
            switch (valueClass) {
                case DOUBLE:
                    handleDoubleValue(value, cell);
                    break;
                case DATE:
                    if (value instanceof Date) {
                        cell.setCellValue(new XSSFRichTextString((String) valueHandler.format(value)));
                    }
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
     * @param excelInfo excel info
     */
    private static void writeExcelSheetTitle(ExcelInfo excelInfo) {
        Workbook workbook = excelInfo.getWorkbook();

        Sheet sheet;
        int begin = excelInfo.getBegin();

        // 创建新HSSFWorkbook对象
        if (workbook == null) {
            if (excelInfo.isExcel2003()) {
                workbook = new HSSFWorkbook();
                //EXCEL2003格式最大行数是65535
                if (excelInfo.getContent().size() >= Constants.EXCEL2003_MAX_ROW) {
                    throw new IllegalStateException();
                }
            } else {
                workbook = new SXSSFWorkbook();
            }

            //workbook
            excelInfo.setWorkbook(workbook);

            //设置样式
            //大标题样式
            CellStyle bigTitleStyle = CellStyleHelper.setBigTitleDefaultStyle(workbook);
            CellEntity bigTitle = excelInfo.getBigTitle();
            bigTitle.setCellStyle(bigTitleStyle);

            //标题样式
            CellStyle titleStyle = CellStyleHelper.setTitleDefaultStyle(workbook);
            for (CellEntity title : excelInfo.getTitles()) {
                title.setCellStyle(titleStyle);
            }

            // 创建新的sheet对象
            sheet = workbook.createSheet(excelInfo.getSheetName());
            begin = setExcelTitle(excelInfo.getBigTitle(), excelInfo.getTitles(), excelInfo.isExcel2003(), sheet, begin);

            excelInfo.setSheet(sheet);
            excelInfo.setBegin(begin);
        } else {
            sheet = workbook.getSheet(excelInfo.getSheetName());
            excelInfo.setSheet(sheet);
        }
    }

    /**
     * 设置excel标题
     *
     * @param bigTitle    第一行标题
     * @param titleStrs   数据标题
     * @param isExcel2003 是否是2003格式的excel
     * @param sheet       excel里面的sheet
     * @param begin       开始插入excel的行数
     *
     * @return 最好插入excel的行数
     */
    private static int setExcelTitle(CellEntity bigTitle, List<CellEntity> titleStrs, boolean isExcel2003,
                                     Sheet sheet, int begin) {
        int titleCount = titleStrs.size();

        //创建第一行
        //单元格范围 参数（int firstRow, int lastRow, int firstCol, int lastCol)
        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, titleCount - 1);
        //添加合并单元格
        sheet.addMergedRegion(cellRangeAddress);

        //创建单元格并接设置值为富文本
        Row bigTitleRow = sheet.createRow(0);
        Cell first = bigTitleRow.createCell(0);
        RichTextString str;
        if (isExcel2003) {
            str = new HSSFRichTextString((String) bigTitle.getValue());
        } else {
            str = new XSSFRichTextString((String) bigTitle.getValue());
        }

        first.setCellValue(str);
        first.setCellStyle(bigTitle.getCellStyle());
        bigTitleRow.setHeightInPoints(22);

        //第二行，标题行
        Row titleRow = sheet.createRow(1);

        //20像素
        titleRow.setHeightInPoints(20);

        // 写标题
        Cell cell;

        for (int k = 0; k < titleCount; k++) {
            // 新建一个单元格
            cell = titleRow.createCell(k);

            //设置标题样式
            cell.setCellStyle(titleStrs.get(k).getCellStyle());
            cell.setCellType(CellType.STRING);
            cell.setCellValue((String) titleStrs.get(k).getValue());
            //设置列宽
            sheet.setColumnWidth(k, 10000);
            begin = sheet.getLastRowNum() - 1;
        }

        //冻结第一、二行
        sheet.createFreezePane(0, 2);
        return begin;
    }


}
