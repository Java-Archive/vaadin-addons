package org.rapidpm.vaadin.addons.testbench.junit5.pageobject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Target({ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PageObject {
}
