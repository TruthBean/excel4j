package com.truthbean.code.excel4j.entity;

import java.util.List;

/**
 * @author truthbean
 * @since 0.0.3
 */
public class RowModel implements Comparable<RowModel> {
    private int order;

    private List<CellModel> cellModels;

    private short rowHeight;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<CellModel> getCellModels() {
        return cellModels;
    }

    public void setCellModels(List<CellModel> cellModels) {
        this.cellModels = cellModels;
    }

    public short getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(short rowHeight) {
        this.rowHeight = rowHeight;
    }

    @Override
    public int compareTo(RowModel o) {
        return order - o.order;
    }
}
