package com.truthbean.code.excel4j.handler.transform.number;

import com.truthbean.code.excel4j.handler.transform.CellEntityValueHandler;

/**
 * @author TruthBean
 * @since 2018-01-16 19:16
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
