package org.rapidpm.vaadin.addons.testbench;

import com.google.gson.stream.JsonReader;
import com.vaadin.testbench.TestBench;
import org.openqa.selenium.Platform;
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
import org.rapidpm.frp.Transformations;
import org.rapidpm.frp.functions.CheckedExecutor;
import org.rapidpm.frp.functions.CheckedFunction;
import org.rapidpm.frp.functions.CheckedPredicate;
import org.rapidpm.frp.functions.CheckedSupplier;
import org.rapidpm.frp.matcher.Case;
import org.rapidpm.frp.model.Result;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.System.setProperty;
import static java.util.stream.Collectors.toList;
import static org.rapidpm.frp.StringFunctions.notEmpty;
import static org.rapidpm.frp.StringFunctions.notStartsWith;
import static org.rapidpm.frp.Transformations.not;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.failure;
import static org.rapidpm.frp.model.Result.success;

/**
 *
 *
 */
public interface BrowserDriverFunctions {

  public static Supplier<String> localeIP() {
    return () -> {
      final CheckedSupplier<Enumeration<NetworkInterface>> checkedSupplier = NetworkInterface::getNetworkInterfaces;

      return Transformations.<NetworkInterface>enumToStream()
          .apply(checkedSupplier.getOrElse(Collections::emptyEnumeration))
          .filter((CheckedPredicate<NetworkInterface>) NetworkInterface::isUp)
          .map(NetworkInterface::getInetAddresses)
          .flatMap(iaEnum -> Transformations.<InetAddress>enumToStream().apply(iaEnum))
          .filter(inetAddress -> inetAddress instanceof Inet4Address)
          .filter(not(InetAddress::isMulticastAddress))
          .filter(not(InetAddress::isLoopbackAddress))
          .map(InetAddress::getHostAddress)
          .filter(notEmpty())
          .filter(adr -> notStartsWith().apply(adr, "127"))
          .filter(adr -> notStartsWith().apply(adr, "169.254"))
          .filter(adr -> notStartsWith().apply(adr, "255.255.255.255"))
          .filter(adr -> notStartsWith().apply(adr, "255.255.255.255"))
          .filter(adr -> notStartsWith().apply(adr, "0.0.0.0"))
          //            .filter(adr -> range(224, 240).noneMatch(nr -> adr.startsWith(valueOf(nr))))
          .findFirst().orElse("localhost");
    };
  }

  public static CheckedFunction<String, Properties> propertyReader() {
    return (filename) -> {
      try (
          final FileInputStream fis = new FileInputStream(new File(filename));
          final BufferedInputStream bis = new BufferedInputStream(fis)) {
        final Properties properties = new Properties();
        properties.load(bis);

        return properties;
      } catch (IOException e) {
        e.printStackTrace();
        throw e;
      }
    };
  }

  public static CheckedExecutor readTestbenchProperties() {
    return () -> propertyReader()
        .apply("config/testbench.properties")
        .ifPresent(p -> p.forEach((key, value) -> setProperty((String) key, (String) value))
        );

  }

  public static Function<String, Result<WebDriver>> localWebDriverInstance() {
    return browserType -> match(
        matchCase(() -> success(new PhantomJSDriver())),
        matchCase(browserType::isEmpty, () -> Result.failure("browserTape should not be empty")),
        matchCase(() -> browserType.equals(BrowserType.PHANTOMJS), () -> success(new PhantomJSDriver())),
        matchCase(() -> browserType.equals(BrowserType.FIREFOX), () -> success(new FirefoxDriver())),
        matchCase(() -> browserType.equals(BrowserType.CHROME), () -> success(new ChromeDriver())),
        matchCase(() -> browserType.equals(BrowserType.SAFARI), () -> success(new SafariDriver())),
        matchCase(() -> browserType.equals(BrowserType.OPERA), () -> success(new OperaDriver())),
        matchCase(() -> browserType.equals(BrowserType.OPERA_BLINK), () -> success(new OperaDriver())),
        matchCase(() -> browserType.equals(BrowserType.IE), () -> success(new InternetExplorerDriver()))
    );
  }

  public static Function<String, Result<DesiredCapabilities>> type2Capabilities() {
    return (browsertype) ->
        match(
            matchCase(() -> failure("browsertype unknown : " + browsertype)),
            matchCase(browsertype::isEmpty, () -> Result.failure("browsertype should not be empty")),
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

  public static Supplier<Result<List<DesiredCapabilities>>> readDesiredCapabilities() {
    return () -> {
      final List<DesiredCapabilities> result = new ArrayList<>();
      final File                      file   = new File("config/browser_combinations.json");
      try (
          final FileReader fr = new FileReader(file);
          final JsonReader reader = new JsonReader(fr)) {

        reader.beginObject();
        while (reader.hasNext()) {
          String name = reader.nextName();
          if (name.equals("browsers")) {
            reader.beginArray();
            while (reader.hasNext()) {
              reader.beginObject();
              String                    browser     = "";
              String                    version     = "";
              String                    platform    = "";
              final Map<String, Object> noNameProps = new HashMap<>();
              while (reader.hasNext()) {
                String property = reader.nextName();
                switch (property) {
                  case "browserName":
                    browser = reader.nextString();
                    break;
                  case "platform":
                    platform = reader.nextString();
                    break;
                  case "version":
                    version = reader.nextString();
                    break;
                  case "enableVideo":
                    noNameProps.put(property, reader.nextBoolean());
                    break;
                  default:
                    noNameProps.put(property, reader.nextString());
                    break;
                }
              }

              final String platformFinal = platform;
              final String versionFinal  = version;

              type2Capabilities()
                  .apply(browser)
                  .ifPresentOrElse(
                      success -> {
                        success.setPlatform(Platform.fromString(platformFinal));
                        success.setVersion(versionFinal);
                        noNameProps.forEach(success::setCapability);
                        result.add(success);
                      },
                      failed -> {
                      }
                  );
              ((CheckedExecutor) reader::endObject).execute();
            }
            reader.endArray();
          }
        }
        reader.endObject();
        reader.close();

      } catch (IOException e) {
        e.printStackTrace();
        return Result.failure(e.getMessage());
      }

      return Result.success(result);
    };
  }


  /**
   * This will create all defined WebDriver instances for the full integration test.
   * If you are defining different Selenium Hubs, you will get Driver for all of them.
   * Normally this amount is not so big, means that memory consumption should not be a problem
   */


  public static Supplier<List<WebDriver>> webDriverInstances() {
    return () -> {

      readTestbenchProperties().execute(); // load properties for locale webdriver

      final Properties properties = propertyReader()
          .apply("config/selenium-grids.properties")
          .getOrElse(Properties::new);

      return readDesiredCapabilities()
          .get()
          .getOrElse(Collections::emptyList) //TODO check if needed
          .stream()
          .map(desiredCapability -> {
            final String browserName = desiredCapability.getBrowserName();
            //for all selenium ips
            return properties
                .entrySet()
                .stream()
                .map(e -> {
                  final String key           = (String) e.getKey();
                  final String targetAddress = (String) e.getValue();

                  final String ip = (targetAddress.endsWith("locale-ip"))
                                    ? localeIP().get()
                                    : targetAddress;

                  return Case
                      .match(
                          matchCase(() -> ((CheckedSupplier<WebDriver>) () -> {
                                      final URL             url             = new URL("http://" + ip + ":4444/wd/hub");
                                      final RemoteWebDriver remoteWebDriver = new RemoteWebDriver(url, desiredCapability);
                                      return TestBench.createDriver(remoteWebDriver);
                                    }).get()
                          ),
                          matchCase(() -> key.equals("nogrid"), () -> localWebDriverInstance().apply(browserName))
                      );
                })
                .peek(r -> {
                  r.ifPresentOrElse(
                      success -> {},
                      failed -> {
                        System.out.println("failed = " + failed);
                        System.out.println("desiredCapability = " + desiredCapability);
                      }
                  );
                })
                .filter(Result::isPresent)
                .collect(toList());
          })
          .flatMap(Collection::stream)
          .map(Result::get)
          .collect(toList());
    };
  }
}
