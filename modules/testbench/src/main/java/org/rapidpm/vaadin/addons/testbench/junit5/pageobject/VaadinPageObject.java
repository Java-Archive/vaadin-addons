package org.rapidpm.vaadin.addons.testbench.junit5.pageobject;

import com.vaadin.testbench.HasDriver;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.frp.functions.CheckedExecutor;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.lang.System.getProperties;
import static org.rapidpm.microservice.MainUndertow.DEFAULT_SERVLET_PORT;
import static org.rapidpm.microservice.MainUndertow.MYAPP;
import static org.rapidpm.vaadin.addons.testbench.WebDriverFunctions.takeScreenShot;

/**
 *
 */
public interface VaadinPageObject extends HasDriver, HasLogger {

  String DEFAULT_PROTOCOL = "http";
  String DEFAULT_IP       = "127.0.0.1";

  String KEY_VAADIN_SERVER_PROTOCOL = "vaadin.server.protocol";
  String KEY_VAADIN_SERVER_IP       = "vaadin.server.ip";
  String KEY_VAADIN_SERVER_PORT     = "vaadin.server.port";
  String KEY_VAADIN_SERVER_WEBAPP   = "vaadin.server.webapp";


  default BiFunction<String, String, String> property() {
    return (key, defaultValue) -> (String) getProperties().getOrDefault(key, defaultValue);
  }

  default Supplier<String> protocol() {
    return () -> property().apply(KEY_VAADIN_SERVER_PROTOCOL, DEFAULT_PROTOCOL);
  }

  default Supplier<String> ip() {
    return () -> property().apply(KEY_VAADIN_SERVER_IP, DEFAULT_IP);
  }

  default Supplier<String> port() {
    //TODO per properties
    return () -> property().apply(KEY_VAADIN_SERVER_PORT, DEFAULT_SERVLET_PORT + "");
  }

  //TODO per properties
  default Supplier<String> webapp() {
    return () -> property().apply(KEY_VAADIN_SERVER_WEBAPP, MYAPP);
  }


  default Supplier<String> baseURL() {
    return () -> protocol().get() + "://" + ip().get() + ":" + port().get();
  }

  default Supplier<String> url() {
    return () -> baseURL().get() + webapp().get() + "/";
  }

  default Supplier<String> urlRestartApp() {
    return () -> url().get() + "?restartApplication";
  }

  default void destroy() {
    ((CheckedExecutor) getDriver()::quit)
        .apply(null)
        .ifPresentOrElse(
            ok -> logger().info("webdriver quit -> OK"),
            failed -> logger().warning("webdriver quit failed -> " + failed)
        );

    ((CheckedExecutor) getDriver()::close)
        .apply(null)
        .ifPresentOrElse(
            ok -> logger().info("webdriver close -> OK"),
            failed -> logger().warning("webdriver close failed -> " + failed)
        );
  }

  default void screenshot(){
    takeScreenShot().accept(getDriver());
  }

}
