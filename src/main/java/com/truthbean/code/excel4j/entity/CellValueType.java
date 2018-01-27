package com.truthbean.code.excel4j.entity;

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
     * numeric
     * Deprecated since 0.0.2
     * replace by #NUMBER
     */
    @Deprecated
    DOUBLE,

    /**
     * numeric value
     * @since 0.0.2
     */
    NUMBER,

    /**
     * date time
     */
    DATE
}
