package org.rapidpm.vaadin.addons.testbench.junit5.extensions.testcontainers;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.testcontainers.containers.GenericContainer;

/**
 *
 */
public class ZaleniumExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, HasLogger {
  @Override
  public void afterEach(ExtensionContext context) throws Exception {
//    zaleniumContainer().apply(context).stop();
//    removeZaleniumContainer().accept(context);
    //removeWebDriver().accept(context);
  }

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {

  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {

    final GenericContainer container = new GenericContainer("dosel/zalenium:latest");
    container.addExposedPorts(4444, 5555);
    container.setPrivilegedMode(true);
    container.withEnv("chromeContainers", "3");
    container.withEnv("firefoxContainers", "3");
    container.withWorkingDirectory("target/zalenium");

    container.start();
//    kills with to long String on surefire version > 2.19.1
//    Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger());
//    webDriverContainer.followOutput(logConsumer);

//    storeZaleniumContainer().accept(context, container);
//    storeWebDriver().accept(context, webDriverContainer::getWebDriver);
  }
}
