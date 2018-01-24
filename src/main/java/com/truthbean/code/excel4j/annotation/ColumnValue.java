package com.truthbean.code.excel4j.annotation;

import com.truthbean.code.excel4j.entity.CellValueType;
import com.truthbean.code.excel4j.handler.transform.CellEntityValueHandler;
import com.truthbean.code.excel4j.handler.transform.text.DefaultTextTransformHandler;

import java.lang.annotation.*;

/**
 * @author TruthBean
 * @since 0.0.1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ColumnValue {
    CellValueType type() default CellValueType.TEXT;

    Class<? extends CellEntityValueHandler> transformHandler() default DefaultTextTransformHandler.class;

    //TODO handle formula
    String formula() default "";
}
