package com.truthbean.excel4j.handler.transform.text;

/**
 * @author TruthBean
 * @since 2018-01-15 17:28
 */
public class DefaultTextTransformHandler extends AbstractTextTransformHandler<String> {

    @Override
    public String transform(String result) {
        return result;
    }
}
