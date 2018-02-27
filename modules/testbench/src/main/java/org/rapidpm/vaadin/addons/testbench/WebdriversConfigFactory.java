package org.rapidpm.vaadin.addons.testbench;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.addons.testbench.GridConfig.Type;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;
import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER;
import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.type2Capabilities;
import static org.rapidpm.vaadin.addons.testbench.WebdriversConfig.*;

public class WebdriversConfigFactory implements HasLogger {

  public WebdriversConfig createFromProperies(Properties configProperties) {

    DesiredCapabilities unittestingBrowser = type2Capabilities()
        .apply(configProperties.getProperty(UNITTESTING_BROWSER, "chrome")).get();

    final String unittestingTarget =
        configProperties.getProperty(UNITTESTING_TARGET, SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER);

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
      String          target = getGridTarget(configProperties, gridName);
      GridConfig.Type type   = getGridType(configProperties, gridName);
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
    String target = configProperties.getProperty(getGridNameKey(gridName) + ".target");
    return Validate.notBlank(target, "The target for the grid {} may not be blank", gridName);
  }

  private String getGridNameKey(String gridName) {
    return COMPATTESTING_GRID + "." + gridName;
  }

}
