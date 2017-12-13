package org.rapidpm.vaadin.addons.testbench.junit5.extensions.seleniumhub;

import com.vaadin.testbench.TestBenchDriverProxy;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.rapidpm.dependencies.core.logger.HasLogger;

import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.failure;
import static org.rapidpm.frp.model.Result.success;
import static org.rapidpm.vaadin.addons.testbench.TestbenchFunctions.webdrivername;
import static org.rapidpm.vaadin.addons.testbench.junit5.extensions.ExtensionFunctions.*;

/**
 *
 */
public class PageObjectWebDriverCleanerExtension implements AfterEachCallback, HasLogger {

  public static final String SESSION_ID = "SESSION_ID";

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    logger().info("PageObjectWebDriverCleanerExtension -> remove Webdriver");
    final WebDriver webDriver = webdriver().apply(context);
    logger().info("close webdriver of type " + webdrivername().apply(webDriver));
    match(
        matchCase(() -> failure("could not map driver to impl class " + webdrivername().apply(webDriver))),
        matchCase(() -> webDriver instanceof RemoteWebDriver, () -> success(((RemoteWebDriver) webDriver).getSessionId().toString())),
        matchCase(() -> webDriver instanceof TestBenchDriverProxy,
                  () -> success(
                      ((RemoteWebDriver)
                          ((TestBenchDriverProxy) webDriver).getActualDriver())
                          .getSessionId().toString())
        )
    ).ifPresentOrElse(
        success -> store().apply(context).put(SESSION_ID, success),
        message -> {
          logger().warning(message);
          store().apply(context).remove(SESSION_ID);
        }
    );
    webDriver.quit();
    removeWebDriver().accept(context);
  }
}
