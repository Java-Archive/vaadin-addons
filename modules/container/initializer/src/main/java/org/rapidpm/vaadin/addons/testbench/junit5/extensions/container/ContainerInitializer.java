package org.rapidpm.vaadin.addons.testbench.junit5.extensions.container;

import java.lang.reflect.Method;

public interface ContainerInitializer {


  default void beforeAll(Class<?> testClass) throws Exception {
    // NOOP
  }

  default void beforeEach(Method testMethod) throws Exception {
    // NOOP
  }


  default void afterEach(Method testMethod) throws Exception {
    // NOOP
  }


  default void afterAll(Class<?> testClass) throws Exception {
    // NOOP
  }
}
