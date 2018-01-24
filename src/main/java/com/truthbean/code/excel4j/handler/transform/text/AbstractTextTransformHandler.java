package com.truthbean.code.excel4j.handler.transform.text;

import com.truthbean.code.excel4j.handler.transform.CellEntityValueHandler;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public abstract class AbstractTextTransformHandler<T> implements CellEntityValueHandler<T, String> {
    @Override
    public String format(T value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    /**
     * transform
     * @param result result
     * @return value
     */
    @Override
    public abstract T transform(String result);
}
