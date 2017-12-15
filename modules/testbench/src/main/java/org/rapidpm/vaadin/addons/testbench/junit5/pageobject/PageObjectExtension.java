package org.rapidpm.vaadin.addons.testbench.junit5.pageobject;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.openqa.selenium.WebDriver;
import org.rapidpm.dependencies.core.logger.HasLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.rapidpm.vaadin.addons.testbench.junit5.extensions.ExtensionFunctions.webdriver;


/**
 *
 */
public class PageObjectExtension implements ParameterResolver, HasLogger {

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().isAnnotationPresent(PageObject.class);
  }

  @Override
  public AbstractVaadinPageObject resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    logger().info("PageObjectExtension - resolveParameter");
    final WebDriver webDriver  = webdriver().apply(extensionContext);
    Class<?>        pageObject = parameterContext.getParameter().getType();
    try {
      Constructor<?> constructor =
          pageObject.getConstructor(WebDriver.class);
      return AbstractVaadinPageObject.class.cast(constructor.newInstance(webDriver));
    } catch (NoSuchMethodException
        | IllegalAccessException
        | InstantiationException
        | InvocationTargetException e) {
      e.printStackTrace();
      throw new ParameterResolutionException("was not able to create PageObjectInstance ", e);
    }
  }


}
