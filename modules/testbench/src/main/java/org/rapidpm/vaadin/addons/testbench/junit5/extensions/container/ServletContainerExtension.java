package org.rapidpm.vaadin.addons.testbench.junit5.extensions.container;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.microservice.Main;

import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.localeIP;
import static org.rapidpm.vaadin.addons.testbench.junit5.pageobject.VaadinPageObject.KEY_VAADIN_SERVER_IP;

/**
 *
 */
public class ServletContainerExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, HasLogger {

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    //TODO hook in with Servletcontainer
    logger().info("ServletContainerExtension - beforeEach");
    Main.deploy();
  }


  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    //TODO hook in with Servletcontainer
    logger().info("ServletContainerExtension - afterEach");
    Main.stop();
  }

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    final String userVaadinServerIP = localeIP().get();
    logger().info(KEY_VAADIN_SERVER_IP + " ServletContainerExtension - will be -> " + userVaadinServerIP);
    System.setProperty(KEY_VAADIN_SERVER_IP, userVaadinServerIP);
  }
}
