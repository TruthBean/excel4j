package com.truthbean.excel4j.util;

import org.apache.poi.ss.usermodel.*;

/**
 * @author TruthBean
 * @since 2018-01-15 11:38
 */
public final class CellStyleHelper {
    private CellStyleHelper() {
    }

    public static CellStyle setBigTitleDefaultStyle(Workbook workbook) {
        // 设置列标题字体，样式
        Font bigTitleFont = workbook.createFont();
        bigTitleFont.setBold(true);
        bigTitleFont.setBold(true);
        short color = 0x000;
        bigTitleFont.setColor(color);
        bigTitleFont.setItalic(false);
        bigTitleFont.setFontName("微软雅黑");
        short heightInPoints = 16;
        bigTitleFont.setFontHeightInPoints(heightInPoints);

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
