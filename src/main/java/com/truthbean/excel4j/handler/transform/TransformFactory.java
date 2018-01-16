package com.truthbean.excel4j.handler.transform;

import com.truthbean.excel4j.handler.transform.date.DefaultTimeTransformHandler;
import com.truthbean.excel4j.handler.transform.number.*;
import com.truthbean.excel4j.handler.transform.text.DefaultTextTransformHandler;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author TruthBean
 * @since 2018-01-16 20:06
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
        if ("long".equals(typeName) || "java.lang.Long".equals(typeName)) {
            return new LongTransformHandler();
        }

        if ("short".equals(typeName) || "java.lang.Short".equals(typeName)) {
            return new ShortTransformHandler();
        }

        if ("int".equals(typeName) || "java.lang.Integer".equals(typeName)) {
            return new IntegerTransformHandler();
        }

        if ("double".equals(typeName) || "java.lang.Double".equals(typeName)) {
            return new DoubleTransformHandler();
        }

        if ("flout".equals(typeName) || "java.lang.Flout".equals(typeName)) {
            return new FloatTransformHandler();
        }

        if ("String".equals(typeName) || "java.lang.String".equals(typeName)) {
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
