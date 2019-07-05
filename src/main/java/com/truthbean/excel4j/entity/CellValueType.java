package com.truthbean.excel4j.entity;

/**
 * excel cell unit format
 * @author TruthBean
 * @since 0.0.1
 */
public enum CellValueType {
    /**
     *  text
     */
    TEXT,

    /**
     * numeric value
     * @since 0.0.2
     */
    NUMBER,

    /**
     * date time
     */
    DATE,

    /**
     * picture
     * @since 0.0.3
     */
    PICTURE
}
