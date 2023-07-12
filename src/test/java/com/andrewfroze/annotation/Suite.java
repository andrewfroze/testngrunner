package com.andrewfroze.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Suite {
    SuiteValue value();
    String description();
}
