package com.jobApplication.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Define the custom annotation
@Target(ElementType.METHOD) // This annotation can only be applied to methods
@Retention(RetentionPolicy.RUNTIME) // This annotation will be available at runtime
public @interface Auditable {
    // Optional value element that can be used to provide additional information
    String value() default "";
}