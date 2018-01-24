package com.truthbean.code.excel4j.entity;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public class CellModel {

    /**
     * cell name
     */
    private String name;

    /**
     * cell value
     */
    private Object value;

    /**
     * cell value info
     */
    private CellValueModel valueModel;

    /**
     * order
     */
    private int order;

    /**
     * cell style info
     */
    private CellStyleModel styleModel;

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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public CellValueModel getValueModel() {
        return valueModel;
    }

    public void setValueModel(CellValueModel valueModel) {
        this.valueModel = valueModel;
    }

    public CellStyleModel getStyleModel() {
        return styleModel;
    }

    public void setStyleModel(CellStyleModel styleModel) {
        this.styleModel = styleModel;
    }
}
