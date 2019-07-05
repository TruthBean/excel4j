package com.truthbean.excel4j.handler.transform.number;

import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;

import java.math.BigInteger;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public class BigIntegerTransformHandler implements CellEntityValueHandler<BigInteger, String> {
    @Override
    public String format(BigInteger value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return value.toString();
    }

    @Override
    public BigInteger transform(String result) {
        // If the specified value is null or zero-length, return null
        if (result == null) {
            return null;
        }

        result = result.trim();

        if (result.length() < 1) {
            return null;
        }

        return new BigInteger(result);
    }
}
