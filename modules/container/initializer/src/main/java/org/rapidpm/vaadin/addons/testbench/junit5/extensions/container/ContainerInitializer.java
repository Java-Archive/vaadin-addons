package org.rapidpm.vaadin.addons.testbench.junit5.extensions.container;

import java.lang.reflect.Method;

public interface ContainerInitializer {


  void beforeAll(Class<?> testClass) throws Exception;

  void beforeEach(Method testMethod) throws Exception;

  void afterEach(Method testMethod) throws Exception;

  void afterAll(Class<?> testClass) throws Exception;
}
