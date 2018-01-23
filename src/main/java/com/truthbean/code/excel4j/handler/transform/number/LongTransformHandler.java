package com.truthbean.code.excel4j.handler.transform.number;

import com.truthbean.code.excel4j.handler.transform.CellEntityValueHandler;

/**
 * @author TruthBean
 * @since 2018-01-16 19:23
 */
public class LongTransformHandler implements CellEntityValueHandler<Long, String> {
    @Override
    public Long transform(String value) {
        return Long.parseLong(value);
    }

    @Override
    public String format(Long result) {
        return String.valueOf(result);
    }
}
