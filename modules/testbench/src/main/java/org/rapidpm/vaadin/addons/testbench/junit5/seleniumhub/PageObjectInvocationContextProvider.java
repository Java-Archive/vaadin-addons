package org.rapidpm.vaadin.addons.testbench.junit5.seleniumhub;

import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.WebDriver;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.frp.functions.CheckedFunction;
import org.rapidpm.frp.model.Result;
import org.rapidpm.vaadin.addons.testbench.junit5.VaadinPageObject;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.webDriverInstances;
import static org.rapidpm.vaadin.addons.testbench.junit5.TestbenchFunctions.webdrivername;
import static org.rapidpm.vaadin.addons.testbench.junit5.extensions.ExtensionFunctions.storeWebDriver;

/**
 *
 */
public class PageObjectInvocationContextProvider implements TestTemplateInvocationContextProvider, HasLogger {
  @Override
  public boolean supportsTestTemplate(ExtensionContext context) {
    return true;
  }

  @Override
  public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {

    logger().info("provideTestTemplateInvocationContexts");

    return webDriverInstances()
        .get()
        .stream()
        .map(this::invocationContext)
        .peek(po -> {
          logger().info("peek - page object -> setting as webDriver into Store ");
          storeWebDriver().accept(context, ((MyTestTemplateInvocationContext) po)::webdriver);
        });
  }


  public interface MyTestTemplateInvocationContext extends TestTemplateInvocationContext {

    WebDriver webdriver();

  }


  private TestTemplateInvocationContext invocationContext(WebDriver parameter) {
    return new MyTestTemplateInvocationContext() {

      @Override
      public WebDriver webdriver() {
        return webdriver;
      }

      public WebDriver webdriver = parameter;

      @Override
      public String getDisplayName(int invocationIndex) {
        return webdrivername().apply(parameter);
      }

      @Override
      public List<Extension> getAdditionalExtensions() {
        return singletonList(new ParameterResolver() {
          @Override
          public boolean supportsParameter(ParameterContext parameterContext,
                                           ExtensionContext extensionContext) {
            final Class<?> type = parameterContext.getParameter().getType();
            return VaadinPageObject.class.isAssignableFrom(type);
          }

          @Override
          public Object resolveParameter(ParameterContext parameterContext,
                                         ExtensionContext extensionContext) {

            Class<?> pageObject = parameterContext
                .getParameter()
                .getType();

            final Result<VaadinPageObject> po = ((CheckedFunction<Class<?>, VaadinPageObject>) aClass -> {
              final Constructor<?> constructor = pageObject.getConstructor(WebDriver.class);
              return VaadinPageObject.class.cast(constructor.newInstance(parameter));
            })
                .apply(pageObject);

            po.ifPresentOrElse(
                success -> logger().fine("pageobject of type " + pageObject.getSimpleName() + " was created with " + webdrivername().apply(parameter)),
                failed -> logger().warning("was not able to create PageObjectInstance " + failed)
            );
            po.ifAbsent(() -> {
              throw new ParameterResolutionException("was not able to create PageObjectInstance of type " + pageObject);
            });
            return po.get();
          }
        });
      }
    };
  }
}
