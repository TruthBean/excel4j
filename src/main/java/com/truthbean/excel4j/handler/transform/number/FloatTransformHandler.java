package com.truthbean.excel4j.handler.transform.number;

import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;

/**
 * @author TruthBean
 * @since 0.0.1
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
