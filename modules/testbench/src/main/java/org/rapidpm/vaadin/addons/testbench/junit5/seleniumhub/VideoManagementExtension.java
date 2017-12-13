package org.rapidpm.vaadin.addons.testbench.junit5.seleniumhub;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.frp.model.Result;
import org.rapidpm.frp.model.Tripel;
import org.rapidpm.frp.model.serial.Pair;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static org.rapidpm.vaadin.addons.testbench.junit5.extensions.ExtensionFunctions.store;
import static org.rapidpm.vaadin.addons.testbench.junit5.extensions.ExtensionFunctions.storeGlobal;
import static org.rapidpm.vaadin.addons.testbench.junit5.seleniumhub.PageObjectWebDriverCleanerExtension.SESSION_ID;

/**
 *
 */
public class VideoManagementExtension implements AfterEachCallback, AfterAllCallback, HasLogger {


  public static final String SESSION_ID_COLLECTION = "SESSION_ID_COLLECTION";


  @Override
  public void afterEach(ExtensionContext context) throws Exception {

//    final Map<String, String> sessionIdCollection = (Map<String, String>) storeGlobal()
//        .apply(context)
//        .getOrComputeIfAbsent(SESSION_ID_COLLECTION,
//                              (Function<String, Map<String, String>>) s -> new ConcurrentHashMap<>());


    Result
        .ofNullable(
            store()
                .apply(context)
                .get(SESSION_ID))
        .ifPresentOrElse(
            success -> {
              logger().info("Found Session ID " + success);
              final String             displayName = context.getDisplayName();
              final Optional<Class<?>> testClass   = context.getTestClass();
              final Optional<Method>   testMethod  = context.getTestMethod();

              //TODO remove hard coded path...
              final File video = new File("video", success + ".mp4");
              logger().info("video file to rename" + video.getAbsoluteFile());
              logger().info("video file exists " + video.exists());
              final String filename = (testClass.get().getSimpleName() + "-"
                                       + testMethod.get().getName() + "-"
                                       + displayName + ".mp4")
                  .replace(" ", "_")
                  .replace(":", "_")
                  .replace("(", "_")
                  .replace(")", "_");
              final File videoFinalName = new File("video", filename);

//              sessionIdCollection.putIfAbsent(video.getAbsoluteFile().toString(),
//                                              videoFinalName.getAbsoluteFile().toString());

              logger().info("video will be renamed into " + videoFinalName.getAbsoluteFile());
              logger().info("renaming was successfull " + video.renameTo(videoFinalName));
            },
            failed -> logger().warning("no session id available..  no video will be renamed")
        );

  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
//    final Map<String, String> sessionIdCollection = (Map<String, String>) storeGlobal()
//        .apply(context)
//        .getOrComputeIfAbsent(SESSION_ID_COLLECTION,
//                              (Function<String, Map<String, String>>) s -> new ConcurrentHashMap<>());
//
//    sessionIdCollection
//        .entrySet()
//        .stream()
//        .map(entry -> {
//          final String key = entry.getKey();
//          final String value = entry.getValue();
//          return new Pair<>(new File(key), new File(value));
//        })
//        .map(p -> new Tripel<>(p.getT1().getAbsoluteFile(),
//                               p.getT2().getAbsoluteFile(),
//                               p.getT1().renameTo(p.getT2())))
//        .forEach(t -> logger().info(t.toString()));
  }

}
