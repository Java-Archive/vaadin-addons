package org.rapidpm.vaadin.addons.testbench.junit5.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 */
public interface ExtensionFunctions {

  ExtensionContext.Namespace NAMESPACE_GLOBAL = ExtensionContext.Namespace.create("global");



  static Function<ExtensionContext, ExtensionContext.Namespace> namespaceFor() {
    return (ctx) -> {
      String name       = ctx.getTestClass().get().getName();
      String methodName = ctx.getTestMethod().get().getName();
      ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(ExtensionFunctions.class,
                                                                               name,
                                                                               methodName
      );
      return namespace;
    };
  }

  static Function<ExtensionContext, ExtensionContext.Store> storeGlobal() {
    return (context) -> context.getStore(NAMESPACE_GLOBAL);
  }

  static Function<ExtensionContext, ExtensionContext.Store> store() {
    return (context) -> context.getStore(namespaceFor().apply(context));
  }

  static Function<ExtensionContext, BiConsumer<String, Object>> storeObjectIn() {
    return (context) -> (key, value) -> store().apply(context).put(key, value);
  }

  static Function<ExtensionContext, Consumer<String>> removeObjectIn() {
    return (context) -> (key) -> store().apply(context).remove(key);
  }


}
