package com.truthbean.excel4j.entity;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * @author TruthBean
 * @since 2018-01-14 20:32
 */
public class ExcelInfo {
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
    private CellEntity bigTitle;

    /**
     * excel title
     */
    private List<CellEntity> titles;

    /**
     * excel content
     */
    private List<List<CellEntity>> content;

    /**
     * if content data is big, use it
     */
    private List<List<List<CellEntity>>> bigDataContent;

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

    public CellEntity getBigTitle() {
        return bigTitle;
    }

    public void setBigTitle(CellEntity bigTitle) {
        this.bigTitle = bigTitle;
    }

    public List<CellEntity> getTitles() {
        return titles;
    }

    public void setTitles(List<CellEntity> titles) {
        this.titles = titles;
    }

    public List<List<CellEntity>> getContent() {
        return content;
    }

    public void setContent(List<List<CellEntity>> content) {
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

    public List<List<List<CellEntity>>> getBigDataContent() {
        return bigDataContent;
    }

    public void setBigDataContent(List<List<List<CellEntity>>> bigDataContent) {
        this.bigDataContent = bigDataContent;
    }
}
