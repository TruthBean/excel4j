package com.truthbean.excel4j.entity;

import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * @author TruthBean
 * @since 2018-01-14 20:09
 */
public class CellEntity {

    /**
     * cell name
     */
    private String name;

    /**
     * cell value
     */
    private Object value;

    /**
     * value class
     */
    private CellEntityValueClass valueClass;

    /**
     * value transform
     */
    private CellEntityValueHandler valueHandler;

    /**
     * order
     */
    private int order;

    private int rowStart;

    private int rowEnd;

    private int columnStart;

    private int columnEnd;

    private boolean single;

    private CellStyle cellStyle;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
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

    public CellEntityValueClass getValueClass() {
        return valueClass;
    }

    public void setValueClass(CellEntityValueClass valueClass) {
        this.valueClass = valueClass;
    }

    public CellEntityValueHandler getValueHandler() {
        return valueHandler;
    }

    public void setValueHandler(CellEntityValueHandler valueHandler) {
        this.valueHandler = valueHandler;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
