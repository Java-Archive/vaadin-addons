package org.rapidpm.vaadin.addons.testbench;

import static java.util.Collections.unmodifiableList;
import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER;
import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.type2Capabilities;
import static org.rapidpm.vaadin.addons.testbench.WebdriversConfig.CHROME_BINARY_PATH;
import static org.rapidpm.vaadin.addons.testbench.WebdriversConfig.COMPATTESTING_GRID;
import static org.rapidpm.vaadin.addons.testbench.WebdriversConfig.UNITTESTING_BROWSER;
import static org.rapidpm.vaadin.addons.testbench.WebdriversConfig.UNITTESTING_HOST;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.addons.testbench.GridConfig.Type;

public class WebdriversConfigFactory implements HasLogger {

  public WebdriversConfig createFromProperies(Properties configProperties) {

    DesiredCapabilities unittestingBrowser = type2Capabilities()
        .apply(configProperties.getProperty(UNITTESTING_BROWSER, "chrome")).get();

    final String unittestingTarget =
        getUnitTestingTarget(configProperties);

    final String chromeBinaryPath =
        configProperties.getProperty(CHROME_BINARY_PATH, null);

    if (unittestingBrowser.getBrowserName().equals("chrome") && StringUtils.isNotBlank(chromeBinaryPath)) {
      ChromeOptions chromeOptions = new ChromeOptions();
      chromeOptions.setBinary(chromeBinaryPath);
      DesiredCapabilities capabilitiesToAdd = new DesiredCapabilities();
      capabilitiesToAdd.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
      unittestingBrowser = addCapabilities(unittestingBrowser, capabilitiesToAdd.asMap());
    }

    final List<GridConfig> gridConfigs = unmodifiableList(createGridConfigs(configProperties));

    logger().info("Browser for unittests is: " + unittestingBrowser.getBrowserName() + " on "
                  + unittestingTarget);

    logger().info("Loaded " + gridConfigs.size() + " grid configuration(s)");
    return new WebdriversConfig(unittestingTarget, unittestingBrowser, gridConfigs);
  }

  private String getUnitTestingTarget(Properties configProperties) {
    final String host =
        configProperties.getProperty(UNITTESTING_HOST, SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER);
    if (SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER.equals(host)) {
      return host;
    } else {
      return "http://" + host + ":4444/wd/hub";
    }
  }

  private DesiredCapabilities addCapabilities(DesiredCapabilities capabilities, Map<String, ?> capabilitiesToAdd) {
    if (capabilities == null && capabilitiesToAdd == null) {
      return new DesiredCapabilities();
    }

    if (capabilities == null) {
      return new DesiredCapabilities(capabilitiesToAdd);
    }

    if (capabilitiesToAdd == null) {
      return capabilities;
    }

    return new DesiredCapabilities(capabilities, new DesiredCapabilities(capabilitiesToAdd));
  }

  private List<GridConfig> createGridConfigs(Properties configProperties) {
    ArrayList<GridConfig> grids = new ArrayList<>();
    Set<String> gridNames = configProperties.stringPropertyNames().stream()
                                            .filter(key -> key.startsWith(COMPATTESTING_GRID))
                                            .map(key -> key.substring(COMPATTESTING_GRID.length() + 1))
                                            .map(key -> key.substring(0, key.indexOf('.'))).collect(Collectors.toSet());

    for (String gridName : gridNames) {
      GridConfig.Type type   = getGridType(configProperties, gridName);
      String          target;
      if(type==Type.BROWSERSTACK) {
        target = getGridTargetBrowserStack(configProperties, gridName);
      } else {        
        target = getGridTarget(configProperties, gridName);
      }
      grids.add(new GridConfig(type, gridName, target,
                               getDesiredCapapilites(configProperties, gridName, type)
      ));
    }

    return grids;
  }

  private Type getGridType(Properties configProperties, String gridName) {
    String stringType = configProperties.getProperty(getGridNameKey(gridName) + ".type", "generic");
    return Type.valueOf(stringType.toUpperCase());
  }

  private List<DesiredCapabilities> getDesiredCapapilites(Properties configProperties,
                                                          String gridName, Type type) {
    Set<String> oses = getOses(configProperties, gridName);

    Set<String>                    browsers           = getBrowsers(configProperties, gridName);
    ArrayList<DesiredCapabilities> desiredCapabilites = new ArrayList<>();

    for (String os : oses) {
      for (String browser : browsers) {
        for (String version : getVersions(configProperties, gridName, browser)) {
          DesiredCapabilities desiredCapability =
              new DesiredCapabilities(browser, version, Platform.fromString(os));
          if (type == Type.SELENOID) {
            desiredCapability.setCapability(BrowserDriverFunctions.ENABLE_VIDEO,
                                            getBoolean(configProperties, gridName, BrowserDriverFunctions.ENABLE_VIDEO)
            );
            desiredCapability.setCapability(BrowserDriverFunctions.ENABLE_VNC,
                                            getBoolean(configProperties, gridName, BrowserDriverFunctions.ENABLE_VNC)
            );
          }
          desiredCapabilites.add(desiredCapability);
        }
      }
    }
    return desiredCapabilites;
  }

  private boolean getBoolean(Properties configProperties, String gridName, String propertieName) {
    String stringValue =
        configProperties.getProperty(getGridNameKey(gridName) + "." + propertieName).trim();
    return StringUtils.isNotBlank(stringValue) ? Boolean.valueOf(stringValue) : false;
  }

  private Set<String> getBrowsers(Properties configProperties, String gridName) {
    return Arrays.stream(configProperties.getProperty(getGridNameKey(gridName) + ".browser").split(",")).map(String::trim).collect(Collectors.toSet());
  }

  private Set<String> getOses(Properties configProperties, String gridName) {
    return Arrays.stream(configProperties.getProperty(getGridNameKey(gridName) + ".os").split(",")).map(String::trim).collect(Collectors.toSet());
  }

  private Set<String> getVersions(Properties configProperties, String gridName, String browser) {
    return Arrays.stream(configProperties
                .getProperty(getGridNameKey(gridName) + ".browser." + browser + ".version").split(",")).map(String::trim).collect(Collectors.toSet());
  }

  private String getGridTarget(Properties configProperties, String gridName) {
    final String host = getProperty(configProperties, gridName, "target");
    Validate.notBlank(host, "The target for the grid {} may not be blank", gridName);

    final String proto = getProperty(configProperties, gridName, "proto", "http");
    final String port = getProperty(configProperties, gridName, "port", "4444");
    final String path = getProperty(configProperties, gridName, "path", "wd/hub");

    return proto + "://" + host + ":" + port + "/" + path;
  }

  private String getProperty(Properties configProperties, String gridName, String property) {
    return configProperties.getProperty(getGridNameKey(gridName) + "." + property);
  }

  private String getProperty(Properties configProperties, String gridName, String property, String defaultVaule) {
    return configProperties.getProperty(getGridNameKey(gridName) + "." + property, defaultVaule);
  }
  
  private String getGridTargetBrowserStack(Properties configProperties, String gridName) {
    final String userName =
        Validate.notBlank(configProperties.getProperty(getGridNameKey(gridName) + ".username"));
    final String key =
        Validate.notBlank(configProperties.getProperty(getGridNameKey(gridName) + ".key"));
    return "https://" + userName + ":" + key + "@hub-cloud.browserstack.com/wd/hub";
  }
  
  private String getGridNameKey(String gridName) {
    return COMPATTESTING_GRID + "." + gridName;
  }

}
