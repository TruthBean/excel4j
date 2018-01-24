package com.truthbean.code.excel4j.util;

import org.apache.poi.ss.usermodel.*;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public final class CellStyleHelper {
    private CellStyleHelper() {
    }

    /**
     * default heightInPoints
     */
    private static final short DEFAULT_HEIGHT_IN_POINTS = 16;

    /**
     * set excel cell big title default style
     * @param workbook style create in workbook
     * @return CellStyle
     */
    public static CellStyle setBigTitleDefaultStyle(Workbook workbook) {
        // 设置列标题字体，样式
        Font bigTitleFont = workbook.createFont();
        bigTitleFont.setBold(true);
        bigTitleFont.setBold(true);
        short color = 0x000;
        bigTitleFont.setColor(color);
        bigTitleFont.setItalic(false);
        bigTitleFont.setFontName("微软雅黑");
        bigTitleFont.setFontHeightInPoints(DEFAULT_HEIGHT_IN_POINTS);

        CellStyle bigTitleStyle = workbook.createCellStyle();
        // 设置边框
        bigTitleStyle.setBorderTop(BorderStyle.THIN);
        bigTitleStyle.setBorderBottom(BorderStyle.THIN);
        bigTitleStyle.setBorderLeft(BorderStyle.THIN);
        bigTitleStyle.setBorderRight(BorderStyle.THIN);
        bigTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        bigTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        bigTitleStyle.setFont(bigTitleFont);

        return bigTitleStyle;
    }

    public static CellStyle setTitleDefaultStyle(Workbook workbook) {
        // 标题列样式
        CellStyle titleStyle = workbook.createCellStyle();

        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        // 设置边框
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleFont.setColor(IndexedColors.WHITE.getIndex());
        titleStyle.setFont(titleFont);
        titleStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return titleStyle;
    }

    public static CellStyle setContentDefaultStyle(Workbook workbook) {
        // 内容列样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }
}
