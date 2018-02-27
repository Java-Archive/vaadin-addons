package org.rapidpm.vaadin.addons.testbench.junit5.extensions.testcontainers;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.util.List;

import static org.rapidpm.vaadin.addons.testbench.junit5.extensions.testcontainers.TestcontainersFunctions.*;


/**
 *
 */
public class TestcontainersExtension
    implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, HasLogger {

  @Override
  public void beforeEach(ExtensionContext context) {
    logger().info("TestcontainersExtension - beforeEach");
    if (testChrome) {
      final BrowserWebDriverContainer webDriverContainerChrome
          = new BrowserWebDriverContainer()
          .withDesiredCapabilities(DesiredCapabilities.chrome()); // only one per container
      webDriverContainerChrome.start();
      storeBrowserContainerChrome().accept(context, webDriverContainerChrome);
      storeWebDriverChrome().accept(context, webDriverContainerChrome::getWebDriver);
    }

    if (testFirefox) {
      final BrowserWebDriverContainer webDriverContainerFirefox
          = new BrowserWebDriverContainer()
          .withDesiredCapabilities(DesiredCapabilities.firefox()); // only one per container

      webDriverContainerFirefox.start();
      storeBrowserContainerFireFox().accept(context, webDriverContainerFirefox);
      storeWebDriverFirefox().accept(context, webDriverContainerFirefox::getWebDriver);
    }
//    kills with to long String on surefire version > 2.19.1
//    Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger());
//    webDriverContainer.followOutput(logConsumer);
  }

  @Override
  public void afterEach(ExtensionContext context) {
    logger().info("TestcontainersExtension - afterEach");
    if (testChrome) {
      browserContainerChrome().apply(context).stop();
      removeBrowserContainerChrome().accept(context);
      removeWebDriverChrome().accept(context);
    }

    if (testFirefox) {
      browserContainerFirefox().apply(context).stop();
      removeBrowserContainerFirefox().accept(context);
      removeWebDriverFirefox().accept(context);
    }
  }

  private List<DesiredCapabilities> capabilities;
  private boolean testChrome  = true;
  private boolean testFirefox = true;

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    logger().info("Testcontainer only can handle chrome and firefox ");
//    readDesiredCapabilities()
//        .get()
//        .ifPresentOrElse(
//            success -> capabilities = success
//                .stream()
//                .filter(dc ->
//                            dc.getBrowserName().equals("chrome") ||
//                            dc.getBrowserName().equals("firefox"))
//                .collect(toList()),
//            failed -> {
//              capabilities = new ArrayList<>();
//              capabilities.add(DesiredCapabilities.chrome());
//            }
//        );

  }
}
