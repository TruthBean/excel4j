package com.truthbean.code.excel4j.handler.transform;

import com.truthbean.code.excel4j.common.TransformConstants;
import com.truthbean.code.excel4j.handler.transform.date.DefaultTimeTransformHandler;
import com.truthbean.code.excel4j.handler.transform.number.*;
import com.truthbean.code.excel4j.handler.transform.text.DefaultTextTransformHandler;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author TruthBean
 * @since 0.0.1
 */
public final class TransformFactory {
    private TransformFactory() {
    }

    /**
     * get CellEntityValueHandler by class
     * @param clazz class
     * @return CellEntityValueHandler
     */
    public static CellEntityValueHandler factory(Class<?> clazz) {
        if (short.class.equals(clazz) || Short.class.equals(clazz)) {
            return new ShortTransformHandler();
        }

        if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
            return new IntegerTransformHandler();
        }

        if (long.class.equals(clazz) || Long.class.equals(clazz)) {
            return new LongTransformHandler();
        }

        if (float.class.equals(clazz) || Float.class.equals(clazz)) {
            return new FloatTransformHandler();
        }

        if (double.class.equals(clazz) || Double.class.equals(clazz)) {
            return new DoubleTransformHandler();
        }

        if (String.class.equals(clazz)) {
            return new DefaultTextTransformHandler();
        }

        if (BigDecimal.class.equals(clazz)) {
            return new BigDecimalTransformHandler();
        }

        if (BigInteger.class.equals(clazz)) {
            return new BigIntegerTransformHandler();
        }

        if (Date.class.equals(clazz)) {
            return new DefaultTimeTransformHandler();
        }

        return new DefaultTransformHandler();

    }

    /**
     * get CellEntityValueHandler by class
     * @param type class type
     * @return CellEntityValueHandler
     */
    public static CellEntityValueHandler factory(Type type) {
        String typeName = type.getTypeName();
        if (TransformConstants.LONG.equals(typeName) || TransformConstants.LONG_CLASS.equals(typeName)) {
            return new LongTransformHandler();
        }

        if (TransformConstants.SHORT.equals(typeName) || TransformConstants.SHORT_CLASS.equals(typeName)) {
            return new ShortTransformHandler();
        }

        if (TransformConstants.INT.equals(typeName) || TransformConstants.INTEGER.equals(typeName)) {
            return new IntegerTransformHandler();
        }

        if (TransformConstants.DOUBLE.equals(typeName) || TransformConstants.DOUBLE_CLASS.equals(typeName)) {
            return new DoubleTransformHandler();
        }

        if (TransformConstants.FLOUT.equals(typeName) || TransformConstants.FLOUT_CLASS.equals(typeName)) {
            return new FloatTransformHandler();
        }

        if (TransformConstants.STRING.equals(typeName)) {
            return new DefaultTextTransformHandler();
        }

        return new DefaultTransformHandler();
    }

    private static class DefaultTransformHandler implements CellEntityValueHandler<Object, String> {
        @Override
        public String format(Object value) {
            return value.toString();
        }

        @Override
        public Object transform(String result) {
            return result;
        }
    }
}
