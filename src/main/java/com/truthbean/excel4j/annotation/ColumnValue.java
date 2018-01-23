package com.truthbean.excel4j.annotation;

import com.truthbean.excel4j.entity.CellValueType;
import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;
import com.truthbean.excel4j.handler.transform.text.DefaultTextTransformHandler;

import java.lang.annotation.*;

/**
 * @author TruthBean
 * @since 2018-01-20 18:08
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