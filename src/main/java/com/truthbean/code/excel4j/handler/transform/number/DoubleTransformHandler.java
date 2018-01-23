package com.truthbean.code.excel4j.handler.transform.number;

import com.truthbean.code.excel4j.handler.transform.CellEntityValueHandler;

/**
 * @author TruthBean
 * @since 2018-01-16 19:25
 */
public class DoubleTransformHandler implements CellEntityValueHandler<Double, String> {
    @Override
    public Double transform(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        return Double.valueOf(value);
    }

    @Override
    public String format(Double result) {
        // If the specified value is null, return a zero-length String
        if (result == null) {
            return "";
        }

        return Double.toString(result);
    }
}
