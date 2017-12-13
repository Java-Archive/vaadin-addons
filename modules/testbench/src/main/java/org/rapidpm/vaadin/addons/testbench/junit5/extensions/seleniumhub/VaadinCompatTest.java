package org.rapidpm.vaadin.addons.testbench.junit5.extensions.seleniumhub;

import org.junit.jupiter.api.extension.ExtendWith;
import org.rapidpm.vaadin.addons.testbench.junit5.extensions.container.ServletContainerExtension;
import org.rapidpm.vaadin.addons.testbench.junit5.extensions.seleniumhub.PageObjectInvocationContextProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(ServletContainerExtension.class)
@ExtendWith(PageObjectInvocationContextProvider.class)
//@ExtendWith(VideoManagementExtension.class)
@ExtendWith(PageObjectWebDriverCleanerExtension.class)
public @interface VaadinCompatTest {
}
