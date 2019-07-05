package com.truthbean.excel4j.entity;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public class ExcelModel {
    /**
     * workbook
     */
    private Workbook workbook;

    /**
     * excel sheet name
     */
    private String sheetName;

    /**
     * excel big title, locate in first row
     */
    private CellModel bigTitle;

    /**
     * excel no big title
     */
    private boolean noBigTitle;

    /**
     * excel title
     */
    private RowModel titles;

    private short contentRowHeight;

    /**
     * excel content
     */
    private List<RowModel> content;

    /**
     * if content data is big, use it
     */
    private List<List<RowModel>> bigDataContent;

    /**
     * is excel 2003
     */
    private boolean excel2003;

    /**
     * excel sheet
     */
    private Sheet sheet;

    /**
     * excel read or write begin
     */
    private int begin;

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public CellModel getBigTitle() {
        return bigTitle;
    }

    public void setBigTitle(CellModel bigTitle) {
        this.bigTitle = bigTitle;
    }

    public RowModel getTitles() {
        return titles;
    }

    public void setTitles(RowModel titles) {
        this.titles = titles;
    }

    public List<RowModel> getContent() {
        return content;
    }

    public void setContent(List<RowModel> content) {
        this.content = content;
    }

    public boolean isExcel2003() {
        return excel2003;
    }

    public void setExcel2003(boolean excel2003) {
        this.excel2003 = excel2003;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public List<List<RowModel>> getBigDataContent() {
        return bigDataContent;
    }

    public void setBigDataContent(List<List<RowModel>> bigDataContent) {
        this.bigDataContent = bigDataContent;
    }

    public boolean isNoBigTitle() {
        return noBigTitle;
    }

    public void setNoBigTitle(boolean noBigTitle) {
        this.noBigTitle = noBigTitle;
    }

    public short getContentRowHeight() {
        return contentRowHeight;
    }

    public void setContentRowHeight(short contentRowHeight) {
        this.contentRowHeight = contentRowHeight;
    }
}
