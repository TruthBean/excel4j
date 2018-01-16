package com.truthbean.excel4j.util;

import org.apache.poi.ss.usermodel.*;

/**
 * @author TruthBean
 * @since 2018-01-15 11:38
 */
public final class CellStyleHelper {
    private CellStyleHelper() {
    }

    private static volatile CellStyle BIG_TITLE_STYLE;

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

        if (BIG_TITLE_STYLE == null) {
            BIG_TITLE_STYLE = workbook.createCellStyle();
            // 设置边框
            BIG_TITLE_STYLE.setBorderTop(BorderStyle.THIN);
            BIG_TITLE_STYLE.setBorderBottom(BorderStyle.THIN);
            BIG_TITLE_STYLE.setBorderLeft(BorderStyle.THIN);
            BIG_TITLE_STYLE.setBorderRight(BorderStyle.THIN);
            BIG_TITLE_STYLE.setVerticalAlignment(VerticalAlignment.CENTER);
            BIG_TITLE_STYLE.setAlignment(HorizontalAlignment.CENTER);
            BIG_TITLE_STYLE.setFont(bigTitleFont);
        }

        return BIG_TITLE_STYLE;
    }

    private static volatile CellStyle TITLE_STYLE;

    public static CellStyle setTitleDefaultStyle(Workbook workbook) {
        if (TITLE_STYLE == null) {
            // 标题列样式
            TITLE_STYLE = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
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
        }

        return TITLE_STYLE;
    }

    private static volatile CellStyle CONTENT_STYLE;

    public static CellStyle setContentDefaultStyle(Workbook workbook) {
        if (CONTENT_STYLE == null) {
            // 内容列样式
            CONTENT_STYLE = workbook.createCellStyle();
            CONTENT_STYLE.setBorderTop(BorderStyle.THIN);
            CONTENT_STYLE.setBorderBottom(BorderStyle.THIN);
            CONTENT_STYLE.setBorderLeft(BorderStyle.THIN);
            CONTENT_STYLE.setBorderRight(BorderStyle.THIN);
            CONTENT_STYLE.setVerticalAlignment(VerticalAlignment.CENTER);
            CONTENT_STYLE.setAlignment(HorizontalAlignment.CENTER);
        }

        return CONTENT_STYLE;
    }
}
