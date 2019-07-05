package com.truthbean.excel4j.util;

import com.truthbean.excel4j.common.Constants;
import com.truthbean.excel4j.entity.PictureInfo;
import com.truthbean.excel4j.exception.OverExcel2003MaxRowException;
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

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ExcelHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    private ExcelHandler() {
    }

    /**
     * 标题样式
     */
    private static CellStyle BIG_TITLE_STYLE = null;

    /**
     * 标题行样式
     */
    private static CellStyle TITLE_STYLE = null;

    /**
     * 行信息内容样式
     */
    private static CellStyle CONTENT_STYLE = null;

    /**
     * 是否是2003格式的excel
     *
     * @param filePath 文件路径
     * @return boolean
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * 是否是2007格式的excel
     *
     * @param filePath 文件路径
     * @return boolean
     */
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 读取excel sheet1 中的数据
     *
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
     * @return String[][]
     */
    public static String[][] readExcelSheet1(String path) {
        Workbook workbook = getExcelSheet1FromFile(path);
        assert workbook != null;
        return readExcelSheet1(workbook);
    }

    /**
     * 写excel文件
     *
     * @param workbook    workbook
     * @param bigTitle    标题
     * @param titles      标题行
     * @param contentList 内容列
     * @param sheetName   sheet名称
     * @param isExcel2003 是否是2003格式
     * @return workbook
     */
    public static Workbook writeExcel(Workbook workbook, String bigTitle, String[] titles,
                                      List<Object[]> contentList, String sheetName, boolean isExcel2003, int begin,
                                      int rowHeight) {
        Sheet sheet;
        //列数
        int titleCount = titles.length;
        // 创建新HSSFWorkbook对象
        if (workbook == null) {
            if (isExcel2003) {
                workbook = new HSSFWorkbook();
                //EXCEL2003格式最大行数是65535
                if (contentList.size() >= Constants.EXCEL2003_MAX_ROW) {
                    throw new OverExcel2003MaxRowException();
                }
            } else {
                workbook = new SXSSFWorkbook();
            }
            //执行样式初始化
            setExcelStyle(workbook);

            // 创建新的sheet对象
            sheet = workbook.createSheet(sheetName);
            begin = setExcelTitle(bigTitle, titles, isExcel2003, sheet, titleCount, begin);

        } else {
            sheet = workbook.getSheet(sheetName);
        }

        //总的记录数
        int contentCount = contentList.size();
        Row row;
        Cell cell;

        Object object;

        // 写内容
        for (int i = 0; i < contentCount; i++) {
            Object[] contents = contentList.get(i);
            // 新建一行
            row = sheet.createRow(i + 2 + begin);

            for (int j = 0; j < titleCount; j++) {
                // 新建一个单元格
                cell = row.createCell(j);

                //设置内容样式
                cell.setCellStyle(CONTENT_STYLE);

                object = contents[j];

                if (object == null || "null".equals(object)) {
                    object = "";
                }
                if (isExcel2003) {
                    if (objectIsNotString(object)) {
                        if (object instanceof BigDecimal) {
                            if (((BigDecimal) object).compareTo(new BigDecimal(Double.MAX_VALUE)) < 0
                                    && ((BigDecimal) object).compareTo(new BigDecimal(Double.MIN_VALUE)) > 0) {
                                object = ((BigDecimal) object).doubleValue();
                            } else {
                                cell.setCellValue(new HSSFRichTextString(((BigDecimal) object).toPlainString()));
                            }
                        }
                        if (object instanceof BigInteger) {
                            object = ((BigInteger) object).doubleValue();
                        }
                        if (object instanceof Integer) {
                            object = ((Integer) object).doubleValue();
                        }
                        if (object instanceof PictureInfo) {
                            handlePictureValue(cell, workbook, sheet, (PictureInfo) object, rowHeight);
                            continue;
                        }
                        cell.setCellValue((double) object);
                    } else {
                        cell.setCellValue(new HSSFRichTextString(String.valueOf(object)));
                    }
                } else {
                    if (objectIsNotString(object)) {
                        if (object instanceof BigDecimal) {
                            object = ((BigDecimal) object).doubleValue();
                        }
                        if (object instanceof BigInteger) {
                            object = ((BigInteger) object).doubleValue();
                        }
                        if (object instanceof Integer) {
                            object = ((Integer) object).doubleValue();
                        }
                        cell.setCellValue((double) object);
                    } else {
                        cell.setCellValue(new XSSFRichTextString(String.valueOf(object)));
                    }
                }
            }
        }
        return workbook;
    }

    /**
     * handle file
     *
     * @param cell        new cell
     * @param workbook    excel workbook
     * @param pictureInfo picture info
     */
    private static void handlePictureValue(Cell cell, Workbook workbook, Sheet sheet, PictureInfo pictureInfo,
                                           int rowHeight) {
        int pictureIdx = workbook.addPicture(pictureInfo.getContent(), pictureInfo.getType().getType());

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

    private static boolean objectIsNotString(Object object) {
        return object.getClass().equals(int.class) || object instanceof Integer
                || object.getClass().equals(short.class) || object instanceof Short
                || object.getClass().equals(long.class) || object instanceof Long
                || object.getClass().equals(double.class) || object instanceof Double
                || object.getClass().equals(float.class) || object instanceof Float
                || object instanceof BigDecimal || object instanceof BigInteger;
    }

    /**
     * 设置excel标题
     *
     * @param bigTitle    第一行标题
     * @param titleStrs   数据标题
     * @param isExcel2003 是否是2003格式的excel
     * @param sheet       excel里面的sheet
     * @param titleCount  标题个数
     * @param begin       开始插入excel的行数
     * @return 最好插入excel的行数
     */
    private static int setExcelTitle(String bigTitle, String[] titleStrs, boolean isExcel2003,
                                     Sheet sheet, int titleCount, int begin) {
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
        first.setCellStyle(BIG_TITLE_STYLE);
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
            cell.setCellStyle(TITLE_STYLE);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(titleStrs[k]);
            //设置列宽
            sheet.setColumnWidth(k, 9000);
            begin = sheet.getLastRowNum() - 1;
        }

        //冻结第一、二行
        sheet.createFreezePane(0, 2);
        return begin;
    }

    /**
     * 导出数据到本地excel中
     *
     * @param wb          workbook
     * @param titles      标题
     * @param contentList 内容
     * @param filename    文件名
     * @param distFileDir 目标路径
     * @return filePath
     */
    public static String writeToFile(Workbook wb, String bigTitle, String[] titles, List<List<Object[]>> contentList,
                                     String filename, String sheetName, String distFileDir, int rowHeight) {
        boolean isExcel2003;
        if (isExcel2003(filename)) {
            isExcel2003 = true;
        } else if (isExcel2007(filename)) {
            isExcel2003 = false;
        } else {
            throw new RuntimeException("excel 文件格式错误");
        }
        String filePath = distFileDir + filename;
        try {
            if (contentList != null && !contentList.isEmpty()) {
                int contentListSize = contentList.size();
                int begin = 0;

                Workbook workbook;
                int i = 0;
                do {
                    workbook = writeExcel(wb, bigTitle, titles, contentList.get(i), sheetName, isExcel2003, begin, rowHeight);
                    i++;
                    begin += contentList.get(i - 1).size();
                } while (i < contentListSize);

                FileOutputStream fileOut = new FileOutputStream(filePath);
                workbook.write(fileOut);
                workbook.close();
                fileOut.flush();
                fileOut.close();
            } else {
                //写一个内容为空的excel
                Workbook workbook = writeExcel(wb, bigTitle, titles, new ArrayList<>(), sheetName, isExcel2003, 0, rowHeight);
                FileOutputStream fileOut = new FileOutputStream(filePath);
                workbook.write(fileOut);
                workbook.close();
                fileOut.flush();
                fileOut.close();
            }

        } catch (IOException e) {
            LOGGER.error("", e);
        }
        LOGGER.info("filePath is " + filePath);

        return filename;
    }

    /**
     * 导出数据到本地excel中
     *
     * @param titleStrs   标题
     * @param contentList 内容
     * @param filename    文件名
     * @param distFileDir 目标路径
     * @return filePath
     */
    public static String writeToFile(String bigTitle, String[] titleStrs, List<Object[]> contentList,
                                     String filename, String sheetName, String distFileDir, int rowHeight) {
        List<List<Object[]>> contents = new ArrayList<>();
        contents.add(contentList);
        return writeToFile(null, bigTitle, titleStrs, contents, filename, sheetName, distFileDir, rowHeight);
    }

    /**
     * 通过HttpServletResponse返回给请求方
     *
     * @param response HttpServletResponse
     * @param filename 文件名称
     * @param workbook workbook
     */
    public static void writeToServletResponse(HttpServletResponse response, String filename, Workbook workbook) {
        //severlet 响应生成excel文件
        try {
            String head = new String(filename.getBytes("GB2312"), StandardCharsets.ISO_8859_1);
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment; filename=" + head);
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            workbook.close();
            os.close();
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    /**
     * 样式初始化
     *
     * @param workBook workBook
     */
    private static void setExcelStyle(Workbook workBook) {
        // 设置列标题字体，样式
        Font bigTitleFont = workBook.createFont();
        bigTitleFont.setBold(true);
        bigTitleFont.setBold(true);
        short color = 0x000;
        bigTitleFont.setColor(color);
        bigTitleFont.setItalic(false);
        bigTitleFont.setFontName("微软雅黑");
        short heightInPoints = 16;
        bigTitleFont.setFontHeightInPoints(heightInPoints);

        BIG_TITLE_STYLE = workBook.createCellStyle();
        // 设置边框
        BIG_TITLE_STYLE.setBorderTop(BorderStyle.THIN);
        BIG_TITLE_STYLE.setBorderBottom(BorderStyle.THIN);
        BIG_TITLE_STYLE.setBorderLeft(BorderStyle.THIN);
        BIG_TITLE_STYLE.setBorderRight(BorderStyle.THIN);
        BIG_TITLE_STYLE.setVerticalAlignment(VerticalAlignment.CENTER);
        BIG_TITLE_STYLE.setAlignment(HorizontalAlignment.CENTER);
        BIG_TITLE_STYLE.setFont(bigTitleFont);
        // 标题列样式
        TITLE_STYLE = workBook.createCellStyle();
        Font titleFont = workBook.createFont();
        titleFont.setBold(true);
        // 设置边框
        TITLE_STYLE.setBorderTop(BorderStyle.THIN);
        TITLE_STYLE.setBorderBottom(BorderStyle.THIN);
        TITLE_STYLE.setBorderLeft(BorderStyle.THIN);
        TITLE_STYLE.setBorderRight(BorderStyle.THIN);
        TITLE_STYLE.setVerticalAlignment(VerticalAlignment.CENTER);
        TITLE_STYLE.setAlignment(HorizontalAlignment.CENTER);
        titleFont.setColor(IndexedColors.WHITE.getIndex());
        TITLE_STYLE.setFont(titleFont);
        TITLE_STYLE.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        TITLE_STYLE.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 内容列样式
        CONTENT_STYLE = workBook.createCellStyle();
        CONTENT_STYLE.setBorderTop(BorderStyle.THIN);
        CONTENT_STYLE.setBorderBottom(BorderStyle.THIN);
        CONTENT_STYLE.setBorderLeft(BorderStyle.THIN);
        CONTENT_STYLE.setBorderRight(BorderStyle.THIN);
        CONTENT_STYLE.setVerticalAlignment(VerticalAlignment.CENTER);
        CONTENT_STYLE.setAlignment(HorizontalAlignment.CENTER);
    }

    public static void main(String[] args) {
        String bigTitle = "人员信息";
        String[] titles = new String[]{"序列", "库", "姓名", "证件号", "二维码", "库照"};

        // 内容分页查询
        List<List<Object[]>> contentList = new ArrayList<>();
        String filename = UUID.randomUUID().toString() + ".xls";
        String sheetName = "设备号";
        String distFileDir = "/tmp/";

        int rowHeight = 400;

        /*List<Object[]> page1 = new ArrayList<>();
        String idStr = "430222199504032354";
        PictureInfo qrcode = new PictureInfo();
        qrcode.setContent(ZxingUtils.encodeQrCodeToByteArray(idStr, 200, 200));
        qrcode.setWidth(200);
        qrcode.setHeight(200);
        qrcode.setType(WorkbookPictureType.PNG);

        PictureInfo picture = new PictureInfo();
        File file = new File(OceanEyeServerConstants.PICTURE_DIR + "/1111.jpg");
        Bitmap bitmap = ImageUtils.from(file);
        picture.setContent(ImageUtils.readDataFromFile(file));
        picture.setWidth(bitmap.getWidth());
        picture.setHeight(bitmap.getHeight());
        picture.setType(WorkbookPictureType.JPEG);

        Object[] row1 = new Object[]{1, "财务部-会计", "李斯", idStr, qrcode, picture};
        page1.add(row1);
        contentList.add(page1);

        writeToFile(null, bigTitle, titles, contentList, filename, sheetName, distFileDir, 400);*/
    }

}
