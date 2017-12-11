package org.rapidpm.vaadin.addons.testbench.junit5.seleniumhub;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.rapidpm.dependencies.core.logger.HasLogger;

import static org.rapidpm.vaadin.addons.testbench.TestbenchFunctions.webdrivername;
import static org.rapidpm.vaadin.addons.testbench.junit5.extensions.ExtensionFunctions.removeWebDriver;
import static org.rapidpm.vaadin.addons.testbench.junit5.extensions.ExtensionFunctions.webdriver;

/**
 *
 */
public class PageObjectWebDriverCleanerExtension implements AfterEachCallback, HasLogger {
  @Override
  public void afterEach(ExtensionContext context) throws Exception {

    logger().warning("PageObjectWebDriverCleanerExtension -> remove Webdriver");

    final WebDriver webDriver = webdriver().apply(context).get();
    logger().info("close webdriver of type " + webdrivername().apply(webDriver));
    webDriver.quit();

    removeWebDriver().accept(context);
  }
}
