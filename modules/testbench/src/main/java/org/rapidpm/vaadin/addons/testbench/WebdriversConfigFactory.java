package org.rapidpm.vaadin.addons.testbench;

import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER;
import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.type2Capabilities;
import static org.rapidpm.vaadin.addons.testbench.WebdriversConfig.COMPATTESTING_GRID;
import static org.rapidpm.vaadin.addons.testbench.WebdriversConfig.UNITTESTING_BROWSER;
import static org.rapidpm.vaadin.addons.testbench.WebdriversConfig.UNITTESTING_TARGET;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.addons.testbench.GridConfig.Type;

public class WebdriversConfigFactory implements HasLogger {

  public WebdriversConfig createFromProperies(Properties configProperties) {
    String unittestingTarget;
    DesiredCapabilities unittestingBrowser;

    List<GridConfig> gridConfigs;
    unittestingBrowser = type2Capabilities()
        .apply(configProperties.getProperty(UNITTESTING_BROWSER, "chrome")).get();
    unittestingTarget =
        configProperties.getProperty(UNITTESTING_TARGET, SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER);
    gridConfigs = Collections.unmodifiableList(createGridConfigs(configProperties));

    logger().info("Browser for unittests is: " + unittestingBrowser.getBrowserName() + " on "
        + unittestingTarget);
    logger().info("Loaded " + gridConfigs.size() + " grid configuration(s)");
    return new WebdriversConfig(unittestingTarget, unittestingBrowser, gridConfigs);
  }

  private List<GridConfig> createGridConfigs(Properties configProperties) {
    ArrayList<GridConfig> grids = new ArrayList<>();
    Set<String> gridNames = configProperties.stringPropertyNames().stream()
        .filter(key -> key.startsWith(COMPATTESTING_GRID))
        .map(key -> key.substring(COMPATTESTING_GRID.length() + 1))
        .map(key -> key.substring(0, key.indexOf('.'))).collect(Collectors.toSet());

    for (String gridName : gridNames) {
      String target = getGridTarget(configProperties, gridName);
      GridConfig.Type type = getGridType(configProperties, gridName);
      grids.add(new GridConfig(type, gridName, target,
          getDesiredCapapilites(configProperties, gridName, type)));
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

    Set<String> browsers = getBrowsers(configProperties, gridName);
    ArrayList<DesiredCapabilities> desiredCapabilites = new ArrayList<>();

    for (String os : oses) {
      for (String browser : browsers) {
        for (String version : getVersions(configProperties, gridName, browser)) {
          DesiredCapabilities desiredCapability =
              new DesiredCapabilities(browser, version, Platform.fromString(os));
          if (type == Type.SELENOID) {
            desiredCapability.setCapability(BrowserDriverFunctions.ENABLE_VIDEO,
                getBoolean(configProperties, gridName, BrowserDriverFunctions.ENABLE_VIDEO));
            desiredCapability.setCapability(BrowserDriverFunctions.ENABLE_VNC,
                getBoolean(configProperties, gridName, BrowserDriverFunctions.ENABLE_VNC));
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
    return Arrays
        .asList(configProperties.getProperty(getGridNameKey(gridName) + ".browser").split(","))
        .stream().map(String::trim).collect(Collectors.toSet());
  }

  private Set<String> getOses(Properties configProperties, String gridName) {
    return Arrays.asList(configProperties.getProperty(getGridNameKey(gridName) + ".os").split(","))
        .stream().map(String::trim).collect(Collectors.toSet());
  }

  private Set<String> getVersions(Properties configProperties, String gridName, String browser) {
    return Arrays
        .asList(configProperties
            .getProperty(getGridNameKey(gridName) + ".browser." + browser + ".version").split(","))
        .stream().map(String::trim).collect(Collectors.toSet());
  }

  private String getGridTarget(Properties configProperties, String gridName) {
    String target = configProperties.getProperty(getGridNameKey(gridName) + ".target");
    return Validate.notBlank(target, "The target for the grid {} may not be blank", gridName);
  }

  private String getGridNameKey(String gridName) {
    return COMPATTESTING_GRID + "." + gridName;
  }

}
