package org.rapidpm.vaadin.addons.testbench.junit5.extensions.container;

import com.google.auto.service.AutoService;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.dependencies.core.net.PortUtils;
import org.rapidpm.microservice.Main;

import java.lang.reflect.Method;

import static org.rapidpm.microservice.MainUndertow.REST_PORT_PROPERTY;
import static org.rapidpm.microservice.MainUndertow.SERVLET_PORT_PROPERTY;
import static org.rapidpm.vaadin.addons.framework.NetworkFunctions.localeIP;

@AutoService(ContainerInitializer.class)
public class RapidPMContainerInitializer implements ContainerInitializer, HasLogger {

  // TODO copy and pasted move to better place
  public static final String KEY_VAADIN_SERVER_IP   = "vaadin.server.ip";
  public static final String KEY_VAADIN_SERVER_PORT = "vaadin.server.port";

  @Override
  public void beforeEach(Method testMethod) throws Exception {
    final PortUtils portUtils = new PortUtils();
    System.setProperty(SERVLET_PORT_PROPERTY, portUtils.nextFreePortForTest() + "");
    System.setProperty(KEY_VAADIN_SERVER_PORT, System.getProperty(SERVLET_PORT_PROPERTY));
    System.setProperty(REST_PORT_PROPERTY, portUtils.nextFreePortForTest() + "");
    Main.deploy();
  }

  @Override
  public void afterEach(Method testMethod) throws Exception {
    Main.stop();
  }

  @Override
  public void afterAll(Class<?> testClass) throws Exception {
    //nothing
  }

  @Override
  public void beforeAll(Class<?> testClass) throws Exception {
    final String userVaadinServerIP = localeIP().get();
    logger().info(
        KEY_VAADIN_SERVER_IP + " ServletContainerExtension - will be -> " + userVaadinServerIP);
    System.setProperty(KEY_VAADIN_SERVER_IP, userVaadinServerIP);
  }
}
