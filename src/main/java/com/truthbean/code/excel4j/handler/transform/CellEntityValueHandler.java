package com.truthbean.code.excel4j.handler.transform;

/**
 * @author TruthBean
 * @since 0.0.1
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
