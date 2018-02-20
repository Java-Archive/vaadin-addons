package org.rapidpm.vaadin.addons.testbench.junit5.extensions.container;

import static org.rapidpm.vaadin.addons.framework.NetworkFunctions.localeIP;
import org.eclipse.jetty.server.Server;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.vaadin.addonhelpers.TServer;
import com.google.auto.service.AutoService;

import java.lang.reflect.Method;

@AutoService(ContainerInitializer.class)
public class AddonTestHelperContainerInitializer implements ContainerInitializer, HasLogger {

  // TODO copy and pasted move to better place
  public static final String KEY_VAADIN_SERVER_IP = "vaadin.server.ip";
  public static final String KEY_VAADIN_SERVER_PORT = "vaadin.server.port";

  private Server server;

  @Override
  public void beforeAll(Class<?> testClass) throws Exception {
    final String userVaadinServerIP = localeIP().get();
    logger().info(
        KEY_VAADIN_SERVER_IP + " ServletContainerExtension - will be -> " + userVaadinServerIP);
    System.setProperty(KEY_VAADIN_SERVER_IP, userVaadinServerIP);

    System.setProperty(KEY_VAADIN_SERVER_PORT, "9998");
    TServer tserver = new TServer();
    server = tserver.startServer();
  }

  @Override
  public void beforeEach(Method testMethod) throws Exception { }

  @Override
  public void afterEach(Method testMethod) throws Exception { }

  @Override
  public void afterAll(Class<?> testClass) throws Exception {
    server.stop();
  }
}
