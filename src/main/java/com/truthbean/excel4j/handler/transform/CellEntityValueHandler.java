package com.truthbean.excel4j.handler.transform;

/**
 * @author TruthBean
 * @since 2018-01-15 16:36
 */
public interface CellEntityValueHandler<O, R> {

    /**
     * format to excel cell
     * @param value value
     * @return result
     */
    R format(O value);

    /**
     * transform excel cell to bean
     * @param result result
     * @return value
     */
    O transform(R result);
}
