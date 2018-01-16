package com.truthbean.excel4j.handler.transform.text;

import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;

/**
 * @author TruthBean
 * @since 2018-01-15 17:25
 */
public abstract class AbstractTextTransformHandler<T> implements CellEntityValueHandler<T, String> {
    @Override
    public String format(T value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    @Override
    public abstract T transform(String result);
}
