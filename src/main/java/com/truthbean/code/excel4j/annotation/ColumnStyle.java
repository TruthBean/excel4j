package com.truthbean.code.excel4j.annotation;

import java.lang.annotation.*;

/**
 * @author TruthBean
 * @since 0.0.1
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ColumnStyle {

    //TODO handle excel bigTitle
    CellColumnStyle[] bigTitleStyle();

    //TODO handle excel title style
    CellColumnStyle[] titleStyle();

    //TODO handle excel cell style
    CellColumnStyle[] contentStyle();
}
