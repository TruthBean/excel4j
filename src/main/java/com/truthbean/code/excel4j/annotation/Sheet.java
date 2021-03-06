package com.truthbean.code.excel4j.annotation;

import java.lang.annotation.*;

/**
 * @author TruthBean
 * @since 0.0.1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sheet {
    String name() default "Sheet1";

    boolean noBigTitle() default false;

    String bigTitle() default "";
}
