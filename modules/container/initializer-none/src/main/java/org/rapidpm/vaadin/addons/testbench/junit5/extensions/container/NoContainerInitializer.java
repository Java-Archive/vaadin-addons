package org.rapidpm.vaadin.addons.testbench.junit5.extensions.container;

import java.lang.reflect.Method;
import org.rapidpm.dependencies.core.logger.HasLogger;
import com.google.auto.service.AutoService;

@AutoService(ContainerInitializer.class)
public class NoContainerInitializer implements ContainerInitializer, HasLogger {

  @Override
  public void beforeAll(Class<?> testClass) throws Exception {
    logger()
        .info("Running tests from " + testClass.getName() + " against remote deployed application");
  }

  @Override
  public void beforeEach(Method testMethod) throws Exception {

  }

  @Override
  public void afterEach(Method testMethod) throws Exception {

  }

  @Override
  public void afterAll(Class<?> testClass) throws Exception {

  }

}
