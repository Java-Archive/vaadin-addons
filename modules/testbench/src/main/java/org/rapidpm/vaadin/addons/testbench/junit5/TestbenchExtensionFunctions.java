package org.rapidpm.vaadin.addons.testbench.junit5;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.rapidpm.vaadin.addons.junit5.extensions.ExtensionFunctions;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.rapidpm.vaadin.addons.junit5.extensions.ExtensionFunctions.store;


/**
 *
 */
public interface TestbenchExtensionFunctions extends ExtensionFunctions {


  String WEBDRIVER = "webdriver";

  static Function<ExtensionContext, WebDriver> webdriver() {
    return (context) -> store().apply(context).get(WEBDRIVER, WebDriver.class);
  }

  static BiConsumer<ExtensionContext, WebDriver> storeWebDriver() {
    return (context, webDriver) -> store().apply(context).put(WEBDRIVER, webDriver);
  }

  static Consumer<ExtensionContext> removeWebDriver() {
    return (context) -> store().apply(context).remove(WEBDRIVER);
  }

}
