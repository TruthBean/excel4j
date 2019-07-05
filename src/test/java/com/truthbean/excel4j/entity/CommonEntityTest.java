package com.truthbean.excel4j.entity;

import com.truthbean.excel4j.annotation.Column;
import com.truthbean.excel4j.annotation.ColumnValue;
import com.truthbean.excel4j.handler.transform.number.DoubleTransformHandler;

/**
 * @author TruthBean
 * @since 2018-01-26 16:52
 */
public class CommonEntityTest {

    @Column(order = 2, name = "ID", columnValue = @ColumnValue(
            type = CellValueType.NUMBER, transformHandler = DoubleTransformHandler.class
    ), width = 2864)
    private int id;

    private int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" + "id:" + id + "}";
    }
}
