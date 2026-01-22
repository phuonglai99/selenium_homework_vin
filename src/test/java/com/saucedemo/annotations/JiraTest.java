package com.saucedemo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to link test methods with Jira issues
 * Usage: @JiraTest(key = "PROJ-123")
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JiraTest {
    /**
     * Jira issue key (e.g., "PROJ-123", "SAUCE-456")
     */
    String key();

    /**
     * Optional description
     */
    String description() default "";

    /**
     * Optional priority (e.g., "High", "Medium", "Low")
     */
    String priority() default "Medium";
}
