package com.truthbean.code.excel4j.handler.transform.date;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public class DefaultTimeTransformHandler extends AbstractTimeTransformHandler {

    /**
     * 时间格式
     */
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public DefaultTimeTransformHandler() {
        super.setFormat(DATE_TIME_FORMAT);
    }
}
