package com.truthbean.excel4j.annotation;

import com.truthbean.excel4j.entity.CellEntityValueClass;
import com.truthbean.excel4j.handler.transform.CellEntityValueHandler;
import com.truthbean.excel4j.handler.transform.text.DefaultTextTransformHandler;

import java.lang.annotation.*;

/**
 * @author TruthBean
 * @since 2018-01-14 20:05
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    int order() default 1;

    String column() default "columnName";

    boolean single() default true;

    CellEntityValueClass valueClass() default CellEntityValueClass.TEXT;

    Class<? extends CellEntityValueHandler> transformHandler() default DefaultTextTransformHandler.class;

    int columnWidth() default 8000;
}
