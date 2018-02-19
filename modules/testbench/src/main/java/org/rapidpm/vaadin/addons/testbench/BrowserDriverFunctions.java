package org.rapidpm.vaadin.addons.testbench;

import com.vaadin.testbench.TestBench;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.dependencies.core.logger.Logger;
import org.rapidpm.dependencies.core.properties.PropertiesResolver;
import org.rapidpm.frp.functions.CheckedExecutor;
import org.rapidpm.frp.functions.CheckedFunction;
import org.rapidpm.frp.functions.CheckedSupplier;
import org.rapidpm.frp.model.Result;
import org.rapidpm.frp.model.Tripel;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.System.setProperty;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.memoizer.Memoizer.memoize;
import static org.rapidpm.frp.model.Result.failure;
import static org.rapidpm.frp.model.Result.success;

/**
 *
 *
 */
public interface BrowserDriverFunctions extends HasLogger {

  String BROWSER_NAME = "browserName";
  String PLATFORM     = "platform";
  String UNITTESTING  = "unittesting";
  String ENABLE_VNC   = "enableVNC";
  String VERSION      = "version";
  String ENABLE_VIDEO = "enableVideo";


  String SELENIUM_GRID_PROPERTIES_LOCALE_IP      = "locale-ip";
  String SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER = "locale";
  String SELENIUM_GRID_PROPERTIES_NO_GRID        = "nogrid";

  String CONFIG_FOLDER = ".testbenchextensions/";

  static CheckedFunction<String, Properties> propertyReaderMemoized() {
    return (CheckedFunction<String, Properties>) memoize(propertyReader());
  }

  static CheckedFunction<String, Properties> propertyReader() {
    return (filename) -> {
      final PropertiesResolver resolver = new PropertiesResolver();
      return resolver.get(filename);
    };
  }

  static CheckedExecutor readTestbenchProperties() {
    return () -> propertyReader()
        .apply(CONFIG_FOLDER + "testbench")
        .ifPresent(p -> p.forEach((key, value) -> setProperty((String) key, (String) value))
        );
  }

  static Supplier<Properties> readSeleniumGridProperties() {
    return () -> propertyReader()
        .apply(CONFIG_FOLDER + "selenium-grids")
        .getOrElse(Properties::new);
  }


  static Function<String, Result<WebDriver>> localWebDriverInstance() {
    return browserType -> {
      readTestbenchProperties().execute();
      return match(
          matchCase(() -> success(new PhantomJSDriver())),
          matchCase(browserType::isEmpty, () -> failure("browserType should not be empty")),
          matchCase(() -> browserType.equals(BrowserType.PHANTOMJS), () -> success(new PhantomJSDriver())),
          matchCase(() -> browserType.equals(BrowserType.FIREFOX), () -> success(new FirefoxDriver())),
          matchCase(() -> browserType.equals(BrowserType.CHROME), () -> success(new ChromeDriver())),
          matchCase(() -> browserType.equals(BrowserType.SAFARI), () -> success(new SafariDriver())),
          matchCase(() -> browserType.equals(BrowserType.OPERA), () -> success(new OperaDriver())),
          matchCase(() -> browserType.equals(BrowserType.OPERA_BLINK), () -> success(new OperaDriver())),
          matchCase(() -> browserType.equals(BrowserType.IE), () -> success(new InternetExplorerDriver()))
      );
    };
  }

  static Function<String, Result<DesiredCapabilities>> type2Capabilities() {
    return (browsertype) ->
        match(
            matchCase(() -> failure("browsertype unknown : " + browsertype)),
            matchCase(browsertype::isEmpty, () -> failure("browsertype should not be empty")),
            matchCase(() -> browsertype.equals(BrowserType.PHANTOMJS), () -> success(DesiredCapabilities.phantomjs())),
            matchCase(() -> browsertype.equals(BrowserType.FIREFOX), () -> success(DesiredCapabilities.firefox())),
            matchCase(() -> browsertype.equals(BrowserType.CHROME), () -> success(DesiredCapabilities.chrome())),
            matchCase(() -> browsertype.equals(BrowserType.EDGE), () -> success(DesiredCapabilities.edge())),
            matchCase(() -> browsertype.equals(BrowserType.SAFARI), () -> success(DesiredCapabilities.safari())),
            matchCase(() -> browsertype.equals(BrowserType.OPERA_BLINK), () -> success(DesiredCapabilities.operaBlink())),
            matchCase(() -> browsertype.equals(BrowserType.OPERA), () -> success(DesiredCapabilities.opera())),
            matchCase(() -> browsertype.equals(BrowserType.IE), () -> success(DesiredCapabilities.internetExplorer()))
        );
  }

//  static Supplier<Result<DesiredCapabilities>> readDefaultDesiredCapability() {
//    return () -> ofNullable(readDesiredCapabilities()
//                                .get()
//                                .stream()
//                                .map(desiredCapabilitiesList -> desiredCapabilitiesList
//                                    .stream()
//                                    .filter(dc -> (dc.getCapability(UNITTESTING) != null)
//                                                  ? Boolean.valueOf(dc.getCapability(UNITTESTING).toString())
//                                                  : FALSE)
//                                    .collect(toList()))
//                                .filter(l -> l.size() == 1)
//                                .findFirst()
//                                .orElse(emptyList())
//                                .get(0), "too many or no default Browser specified..");
//  }

  @Deprecated
  // Uses old config format
//  static Supplier<Result<List<DesiredCapabilities>>> readDesiredCapabilities() {
//    return () -> {
//      final List<DesiredCapabilities> result = new ArrayList<>();
//      final File                      file   = new File(CONFIG_FOLDER + "browser_combinations.json");
//      try (
//          final FileReader fr = new FileReader(file);
//          final JsonReader reader = new JsonReader(fr)) {
//
//        reader.beginObject();
//        while (reader.hasNext()) {
//          String name = reader.nextName();
//          if (name.equals("browsers")) {
//            reader.beginArray();
//            while (reader.hasNext()) {
//              reader.beginObject();
//              String                    browser     = "";
//              String                    version     = "";
//              Platform                  platform    = Platform.ANY;
//              final Map<String, Object> noNameProps = new HashMap<>();
//              while (reader.hasNext()) {
//                String property = reader.nextName();
//                switch (property) {
//                  case BROWSER_NAME:
//                    browser = reader.nextString();
//                    break;
//                  case PLATFORM:
//                    platform = Platform.fromString(reader.nextString());
//                    break;
//                  case VERSION:
//                    version = reader.nextString();
//                    break;
//                  case ENABLE_VIDEO:
//                    noNameProps.put(property, reader.nextBoolean());
//                    break;
//                  case UNITTESTING:
//                    noNameProps.put(property, reader.nextBoolean());
//                    break;
//                  case ENABLE_VNC:
//                    noNameProps.put(property, reader.nextBoolean());
//                    break;
//                  default:
//                    noNameProps.put(property, reader.nextString());
//                    break;
//                }
//              }
//
//              final Platform platformFinal = platform;
//              final String   versionFinal  = version;
//
//              type2Capabilities()
//                  .apply(browser)
//                  .ifPresentOrElse(
//                      success -> {
//                        success.setPlatform(platformFinal);
//                        success.setVersion(versionFinal);
//                        noNameProps.forEach(success::setCapability);
//                        result.add(success);
//                      },
//                      failed -> {
//                      }
//                  );
//              ((CheckedExecutor) reader::endObject).execute();
//            }
//            reader.endArray();
//          }
//        }
//        reader.endObject();
//        reader.close();
//
//      } catch (IOException e) {
//        e.printStackTrace();
//        return Result.failure(e.getMessage());
//      }
//
//      return Result.success(result);
//    };
//  }

  static Result<WebDriver> unittestingWebDriverInstance() {
    WebdriversConfig          config            = readConfig();
    final String              unittestingTarget = config.getUnittestingTarget();
    final DesiredCapabilities unittestingDC     = config.getUnittestingBrowser();
    return (unittestingTarget != null)
           ? match(
        matchCase(() -> remoteWebDriverInstance(unittestingDC, unittestingTarget).get()),
        matchCase(unittestingTarget::isEmpty, () -> failure(UNITTESTING + " should not be empty")),
        matchCase(() -> unittestingTarget.equals(SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER),
                  () -> localWebDriverInstance().apply(unittestingDC.getBrowserName())
        )
    )
           : failure("no target for " + UNITTESTING + " could be found.");
  }

  static CheckedSupplier<WebDriver> remoteWebDriverInstance(DesiredCapabilities desiredCapability,
                                                            final String ip) {
    return () -> {
      final URL             url             = new URL("http://" + ip + ":4444/wd/hub");
      final RemoteWebDriver remoteWebDriver = new RemoteWebDriver(url, desiredCapability);
      return TestBench.createDriver(remoteWebDriver); // remove TB dependency (proxy)
    };
  }

  static Supplier<List<WebDriver>> webDriverInstances() {
    return () -> {

      final Map<Boolean, List<Result<WebDriver>>> resultMap = readConfig()
          .getGridConfigs()
          .stream()
          .flatMap(gridConfig -> gridConfig
              .getDesiredCapabilities()
              .stream()
              .map(dc -> new Tripel<>(gridConfig.getTarget().equals(SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER),
                                      dc,
                                      gridConfig.getTarget()
              ))
          )
          .map(t -> (t.getT1())
                    ? localWebDriverInstance().apply(t.getT2().getBrowserName())
                    : remoteWebDriverInstance(t.getT2(), t.getT3()).get()
          )
          .peek(r -> r.ifFailed((failed) -> Logger.getLogger(BrowserDriverFunctions.class).warning(failed)))
//        .filter(Result::isPresent)
//        .map(Result::get)
          .collect(groupingBy(Result::isPresent));

      if (resultMap.containsKey(FALSE)) {
        throw new RuntimeException(resultMap.get(FALSE).toString());
      }
      return resultMap.get(TRUE).stream().map(Result::get).collect(toList());


    };
  }

  static WebdriversConfig readConfig() {
    final Properties configProperties =
        propertyReader()
            .apply(CONFIG_FOLDER + "config")
            .getOrElse(Properties::new);
    return new WebdriversConfigFactory().createFromProperies(configProperties);
  }
}
