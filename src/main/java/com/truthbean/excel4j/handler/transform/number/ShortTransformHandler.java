package com.truthbean.excel4j.handler.transform.number;

import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public class ShortTransformHandler implements CellEntityValueHandler<Short, String> {
    @Override
    public Short transform(String value) {
        return Short.parseShort(value);
    }

    @Override
    public String format(Short result) {
        return String.valueOf(result);
    }
}
