package com.truthbean.code.excel4j.handler.transform.number;

import com.truthbean.code.excel4j.handler.transform.CellEntityValueHandler;

/**
 * @author TruthBean
 * @since 2018-01-16 19:27
 */
public class FloatTransformHandler implements CellEntityValueHandler<Float, String> {
    @Override
    public Float transform(String value) {
        return Float.parseFloat(value);
    }

    @Override
    public String format(Float result) {
        return String.valueOf(result);
    }
}
