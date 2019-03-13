package com.truthbean.code.excel4j.util;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * 操作EXCEL文件
 *
 * @author TruthBean
 * @since 0.0.1
 */
public final class ExcelUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    private ExcelUtils() {
    }

    /**
     * 2003 excel file name regex
     */
    private static final String EXCEL2003_FILE_NAME_REGEX = "^.+\\.(?i)(xls)$";

    /**
     * 2007 excel file name regex
     */
    private static final String EXCEL2007_FILE_NAME_REGEX = "^.+\\.(?i)(xlsx)$";

    /**
     * 是否是2003格式的excel
     * @param filePath 文件路径
     * @return boolean
     */
    private static boolean isExcel2003(String filePath) {
        return filePath.matches(EXCEL2003_FILE_NAME_REGEX);
    }

    /**
     * 是否是2007格式的excel
     * @param filePath 文件路径
     * @return boolean
     */
    private static boolean isExcel2007(String filePath) {
        return filePath.matches(EXCEL2007_FILE_NAME_REGEX);
    }

    /**
     * is file 2003 excel
     * @param filename
     * @return
     */
    public static boolean isExcel2003File(String filename) {
        boolean isExcel2003;
        if (isExcel2003(filename)) {
            isExcel2003 = true;
        } else if (isExcel2007(filename)) {
            isExcel2003 = false;
        } else {
            throw new RuntimeException("excel 文件格式错误");
        }
        return isExcel2003;
    }

    /**
     * 读取excel sheet1 中的数据
     * @param workbook workbook
     * @return String[][]
     */
    public static String[][] readExcelSheet1(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);

        //Iterate through each rows one by one
        Row firstRow = sheet.getRow(0);
        int cellLength = 0;
        if (firstRow != null) {
            cellLength = firstRow.getLastCellNum();
        }

        int rowLength = cellLength > 0 ? sheet.getLastRowNum() + 1 : 0;
        String[][] data = new String[rowLength][cellLength];

        int i = 0;
        Iterator<Cell> cellIterator;
        Cell cell;

        for (Row row : sheet) {
            //For each row, iterate through all the columns
            cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                if (cell != null) {
                    int index = cell.getColumnIndex();
                    if (index < cellLength) {
                        row.getCell(index).setCellType(CellType.STRING);
                        //Check the cell type and format accordingly
                        switch (cell.getCellTypeEnum()) {
                            case NUMERIC:
                                data[i][index] = new BigDecimal(cell.getNumericCellValue()).toPlainString();
                                break;
                            case STRING:
                                data[i][index] = cell.getStringCellValue();
                                break;
                            case BLANK:
                                data[i][index] = "";
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    break;
                }

            }

            i++;
        }

        return data;
    }

    /**
     * 读取excel sheet1 中的数据，需要比文件更多的内存
     *
     * @param excel excel文件
     *
     * @return String[][]
     */
    public static String[][] readExcelSheet1(File excel) {
        try {
            Workbook workbook = WorkbookFactory.create(excel);
            return readExcelSheet1(workbook);
        } catch (IOException | InvalidFormatException e) {
            LOGGER.error("new workbook 时异常", e);
        }
        return null;
    }

    public static Workbook getExcelSheet1FromFile(String path) {
        try {
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            Workbook workbook = null;
            if (isExcel2003(path)) {
                workbook = new HSSFWorkbook(fileInputStream);
            } else if (isExcel2007(path)) {
                OPCPackage opcPackage = OPCPackage.open(file);
                workbook = new XSSFWorkbook(opcPackage);
                workbook = new SXSSFWorkbook((XSSFWorkbook) workbook, 100);
            }
            return workbook;
        } catch (IOException | InvalidFormatException e) {
            LOGGER.error("new workbook 时异常", e);
        }
        return null;
    }

    /**
     * 读取excel sheet1 中的数据
     *
     * @param path 文件路径
     *
     * @return String[][]
     */
    public static String[][] readExcelSheet1(String path) {
        Workbook workbook = getExcelSheet1FromFile(path);
        assert workbook != null;
        return readExcelSheet1(workbook);
    }

    /**
     * 设置excel标题
     *
     * @param workbook          workbook
     * @param bigTitle    第一行标题
     * @param titleStrs   数据标题
     * @param isExcel2003 是否是2003格式的excel
     * @param sheet       excel里面的sheet
     * @param titleCount  标题个数
     * @param begin       开始插入excel的行数
     *
     * @return 最好插入excel的行数
     */
    private static int setExcelTitle(Workbook workbook, String bigTitle, String[] titleStrs, boolean isExcel2003, Sheet sheet, int titleCount, int begin) {
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
            str = new HSSFRichTextString(bigTitle);
        } else {
            str = new XSSFRichTextString(bigTitle);
        }

        first.setCellValue(str);
        first.setCellStyle(CellStyleHelper.setBigTitleDefaultStyle(workbook));
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
            cell.setCellStyle(CellStyleHelper.setTitleDefaultStyle(workbook));
            cell.setCellType(CellType.STRING);
            cell.setCellValue(titleStrs[k]);
            //设置列宽
            sheet.setColumnWidth(k, 10000);
            begin = sheet.getLastRowNum() - 1;
        }

        //冻结第一、二行
        sheet.createFreezePane(0, 2);
        return begin;
    }
}
