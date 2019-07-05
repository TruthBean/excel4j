package com.truthbean.excel4j.handler.transform.text;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public class DefaultTextTransformHandler extends AbstractTextTransformHandler<String> {

    @Override
    public String transform(String result) {
        return result;
    }
}
