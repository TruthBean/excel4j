package com.truthbean.code.excel4j.annotation;

import java.lang.annotation.*;

/**
 * @author TruthBean
 * @since 0.0.1
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    /**
     * default width
     */
    int WIDTH = 8000;

    /**
     * @return cell order in one column
     */
    int order() default 1;

    /**
     * @return column name
     */
    String name() default "name";

    /**
     * @return is cell single, if merge return false
     */
    boolean single() default true;

    /**
     * @return cell width
     */
    int width() default WIDTH;

    /**
     * @return column cell value type, format and so on
     */
    ColumnValue columnValue() default @ColumnValue;

    /**
     * @return column cell style
     */
    ColumnStyle[] columnStyle() default {};
}
