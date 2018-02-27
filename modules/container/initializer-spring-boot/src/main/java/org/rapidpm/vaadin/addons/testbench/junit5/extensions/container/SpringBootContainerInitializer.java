package org.rapidpm.vaadin.addons.testbench.junit5.extensions.container;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import com.google.auto.service.AutoService;

import java.lang.reflect.Method;


@AutoService(ContainerInitializer.class)
public class SpringBootContainerInitializer implements ContainerInitializer {

  private ApplicationContext applicationContext;

  @Override
  public void beforeAll(Class<?> testClass) throws Exception {
    SpringBootConf springBootConf = AnnotationUtils.getAnnotation(testClass, SpringBootConf.class);
    if (springBootConf == null) {
      throw new IllegalStateException("No @SpringBootConf annotation found");
    }
    Class<?> appClass = springBootConf.source();
    if (appClass == null) {
      throw new IllegalStateException("No app class defined");
    }
    applicationContext = SpringApplication.run(appClass, springBootConf.args());
  }

  @Override
  public void beforeEach(Method testMethod) throws Exception { }

  @Override
  public void afterEach(Method testMethod) throws Exception { }

  @Override
  public void afterAll(Class<?> testClass) throws Exception {
    SpringApplication.exit(applicationContext);
  }
}
