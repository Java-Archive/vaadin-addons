package org.rapidpm.vaadin.addons.testbench.junit5.extensions.container;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.rapidpm.dependencies.core.logger.HasLogger;

/**
 *
 */
public class ServletContainerExtension implements BeforeAllCallback, BeforeEachCallback,
    AfterEachCallback, AfterAllCallback, HasLogger {

  private final ContainerInitializer containerIntializer;

  public ServletContainerExtension() {
    ServiceLoader<ContainerInitializer> serviceLoader =
        ServiceLoader.load(ContainerInitializer.class);
    List<ContainerInitializer> initializers = new ArrayList<>();
    serviceLoader.forEach(initializers::add);

    if (initializers.isEmpty()) {
      throw new IllegalStateException("No implementation of ContainerInitializer found");
    }
    if (initializers.size() != 1) {
      logger().warning("More than one implementation of ContainerInitializer found!");
    }
    containerIntializer = initializers.get(0);
    logger().info("Using ContainerInitializer: " + containerIntializer.getClass().getName());
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    logger().info("ServletContainerExtension - beforeEach");
    containerIntializer.beforeEach(context.getTestMethod().get());
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    logger().info("ServletContainerExtension - afterEach");
    containerIntializer.afterEach(context.getTestMethod().get());
  }

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    containerIntializer.beforeAll(context.getTestClass().get());
  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    containerIntializer.afterAll(context.getTestClass().get());

  }
}
