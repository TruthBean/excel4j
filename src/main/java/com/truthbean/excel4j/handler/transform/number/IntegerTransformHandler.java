package com.truthbean.excel4j.handler.transform.number;

import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public class IntegerTransformHandler implements CellEntityValueHandler<Integer, String> {
    @Override
    public Integer transform(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public String format(Integer result) {
        return String.valueOf(result);
    }
}
