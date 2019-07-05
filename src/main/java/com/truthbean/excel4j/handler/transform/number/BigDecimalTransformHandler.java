package com.truthbean.excel4j.handler.transform.number;

import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;

import java.math.BigDecimal;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public class BigDecimalTransformHandler implements CellEntityValueHandler<BigDecimal, String> {
    @Override
    public BigDecimal transform(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        return new BigDecimal(value);
    }

    @Override
    public String format(BigDecimal result) {
        // If the specified value is null, return a zero-length String
        if (result == null) {
            return "";
        }

        return result.toPlainString();
    }

}
