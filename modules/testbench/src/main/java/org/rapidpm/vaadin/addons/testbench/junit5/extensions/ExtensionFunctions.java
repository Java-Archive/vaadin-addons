package org.rapidpm.vaadin.addons.testbench.junit5.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 */
public interface ExtensionFunctions {


  String WEBDRIVER         = "webdriver";

  String ZALENIUMCONTAINER = "zaleniumcontainer";

  static Function<ExtensionContext, ExtensionContext.Namespace> namespaceFor() {
    return (ctx) -> {
      String name       = ctx.getTestClass().get().getName();
      String methodName = ctx.getTestMethod().get().getName();
      ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(ExtensionFunctions.class,
                                                                               name,
                                                                               methodName
      );
      return namespace;
    };
  }

  static Function<ExtensionContext, ExtensionContext.Store> store() {
    return (context) -> context.getStore(namespaceFor().apply(context));
  }

  static Function<ExtensionContext, BiConsumer<String, Object>> storeObjectIn() {
    return (context) -> (key, value) -> store().apply(context).put(key, value);
  }

  static Function<ExtensionContext, Consumer<String>> removeObjectIn() {
    return (context) -> (key) -> store().apply(context).remove(key);
  }

  static Function<ExtensionContext, GenericContainer> zaleniumContainer() {
    return (context) -> store().apply(context).get(ZALENIUMCONTAINER, BrowserWebDriverContainer.class);
  }

  static BiConsumer<ExtensionContext, GenericContainer> storeZaleniumContainer() {
    return (context, webDriver) -> store().apply(context).put(ZALENIUMCONTAINER, webDriver);
  }

  static Consumer<ExtensionContext> removeZaleniumContainer() {
    return (context) -> store().apply(context).remove(ZALENIUMCONTAINER);
  }

  //TODO extract to generic base class -> Testcontainers / Zalenium / ..
  static Function<ExtensionContext, Supplier<WebDriver>> webdriver() {
    return (context) -> store().apply(context).get(WEBDRIVER, Supplier.class);
  }

  static BiConsumer<ExtensionContext, Supplier<WebDriver>> storeWebDriver() {
    return (context, webDriver) -> store().apply(context).put(WEBDRIVER, webDriver);
  }

  static Consumer<ExtensionContext> removeWebDriver() {
    return (context) -> store().apply(context).remove(WEBDRIVER);
  }

}
