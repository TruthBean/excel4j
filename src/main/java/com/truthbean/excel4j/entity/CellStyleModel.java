package com.truthbean.excel4j.entity;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * @author TruthBean
 * @since 2018-01-23 13:44
 */
public class CellStyleModel {
    /**
     * column width
     */
    private int columnWidth;

    /**
     * row start
     */
    private int rowStart;

    /**
     * row end
     */
    private int rowEnd;

    /**
     * column start
     */
    private int columnStart;

    /**
     * column end
     */
    private int columnEnd;

    /**
     * is cell single
     */
    private boolean single;

    /**
     * cell style, one workbook cannot has as many as max
     */
    private CellStyle cellStyle;

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public int getRowStart() {
        return rowStart;
    }

    public void setRowStart(int rowStart) {
        this.rowStart = rowStart;
    }

    public int getRowEnd() {
        return rowEnd;
    }

    public void setRowEnd(int rowEnd) {
        this.rowEnd = rowEnd;
    }

    public int getColumnStart() {
        return columnStart;
    }

    public void setColumnStart(int columnStart) {
        this.columnStart = columnStart;
    }

    public int getColumnEnd() {
        return columnEnd;
    }

    public void setColumnEnd(int columnEnd) {
        this.columnEnd = columnEnd;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }
}
