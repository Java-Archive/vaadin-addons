package org.rapidpm.vaadin.addons.testbench.junit5.extensions.testcontainers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.rapidpm.vaadin.addons.junit5.extensions.ExtensionFunctions.store;

/**
 *
 */
public interface TestcontainersFunctions {

  String BROWSERCONTAINER_CHROME   = "browsercontainerChrome";
  String BROWSERCONTAINER_FIREFOX = "browsercontainerFirefox";

  String WEBDRIVER_CHROME = "webdriverChrome";
  String WEBDRIVER_FIREFOX = "webdriverFirefox";

  //TODO refactore : to repetitive


  static Function<ExtensionContext, BrowserWebDriverContainer> browserContainerChrome() {
    return (context) -> store().apply(context).get(BROWSERCONTAINER_CHROME, BrowserWebDriverContainer.class);
  }

  static BiConsumer<ExtensionContext, BrowserWebDriverContainer> storeBrowserContainerChrome() {
    return (context, webDriver) -> store().apply(context).put(BROWSERCONTAINER_CHROME, webDriver);
  }

  static Consumer<ExtensionContext> removeBrowserContainerChrome() {
    return (context) -> store().apply(context).remove(BROWSERCONTAINER_CHROME);
  }

  static Function<ExtensionContext, BrowserWebDriverContainer> browserContainerFirefox() {
    return (context) -> store().apply(context).get(BROWSERCONTAINER_FIREFOX, BrowserWebDriverContainer.class);
  }

  static BiConsumer<ExtensionContext, BrowserWebDriverContainer> storeBrowserContainerFireFox() {
    return (context, webDriver) -> store().apply(context).put(BROWSERCONTAINER_FIREFOX, webDriver);
  }

  static Consumer<ExtensionContext> removeBrowserContainerFirefox() {
    return (context) -> store().apply(context).remove(BROWSERCONTAINER_FIREFOX);
  }

  static Function<ExtensionContext, Supplier<WebDriver>> webdriverChrome() {
    return (context) -> store().apply(context).get(WEBDRIVER_CHROME, Supplier.class);
  }

  static BiConsumer<ExtensionContext, Supplier<WebDriver>> storeWebDriverChrome() {
    return (context, webDriver) -> store().apply(context).put(WEBDRIVER_CHROME, webDriver);
  }

  static Consumer<ExtensionContext> removeWebDriverChrome() {
    return (context) -> store().apply(context).remove(WEBDRIVER_CHROME);
  }

  static Function<ExtensionContext, Supplier<WebDriver>> webdriverFirefox() {
    return (context) -> store().apply(context).get(WEBDRIVER_FIREFOX, Supplier.class);
  }

  static BiConsumer<ExtensionContext, Supplier<WebDriver>> storeWebDriverFirefox() {
    return (context, webDriver) -> store().apply(context).put(WEBDRIVER_FIREFOX, webDriver);
  }

  static Consumer<ExtensionContext> removeWebDriverFirefox() {
    return (context) -> store().apply(context).remove(WEBDRIVER_FIREFOX);
  }
}
