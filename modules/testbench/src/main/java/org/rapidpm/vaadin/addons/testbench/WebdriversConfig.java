package org.rapidpm.vaadin.addons.testbench;

import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER;
import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.type2Capabilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import net.vergien.beanautoutils.annotation.Bean;

@Bean
public class WebdriversConfig {
  public static final String UNITTESTING_BROWSER = "unittesting.browser";
  public static final String UNITTESTING_TARGET = "unittesting.target";
  public static final String COMPATTESTING = "compattesting";
  public static final String COMPATTESTING_GRID = COMPATTESTING + ".grid";
  private final String unittestingTarget;
  private final DesiredCapabilities unittestingBrowser;

  private final List<GridConfig> gridConfigs;

  public WebdriversConfig(Properties configProperties) {
    unittestingBrowser = type2Capabilities()
        .apply(configProperties.getProperty(UNITTESTING_BROWSER, "chrome")).get();
    unittestingTarget =
        configProperties.getProperty(UNITTESTING_TARGET, SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER);
    gridConfigs = Collections.unmodifiableList(createGridConfigs(configProperties));
  }

  private List<GridConfig> createGridConfigs(Properties configProperties) {
    ArrayList<GridConfig> grids = new ArrayList<>();
    Set<String> gridNames = configProperties.stringPropertyNames().stream()
        .filter(key -> key.startsWith(COMPATTESTING_GRID))
        .map(key -> key.substring(COMPATTESTING_GRID.length() + 1))
        .map(key -> key.substring(0, key.indexOf('.'))).collect(Collectors.toSet());

    for (String gridName : gridNames) {
      String target = getGridTarget(configProperties, gridName);
      grids
          .add(new GridConfig(gridName, target, getDesiredCapapilites(configProperties, gridName)));
    }

    return grids;
  }

  private List<DesiredCapabilities> getDesiredCapapilites(Properties configProperties,
      String gridName) {
    Set<String> oses = getOses(configProperties, gridName);

    Set<String> browsers = getBrowsers(configProperties, gridName);
    ArrayList<DesiredCapabilities> desiredCapabilites = new ArrayList<>();

    for (String os : oses) {
      for (String browser : browsers) {
        for (String version : getVersions(configProperties, gridName, browser)) {
          desiredCapabilites
              .add(new DesiredCapabilities(browser, version, Platform.fromString(os)));
        }
      }
    }
    return desiredCapabilites;
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

  public String getUnittestingTarget() {
    return unittestingTarget;
  }

  public DesiredCapabilities getUnittestingBrowser() {
    return unittestingBrowser;
  }

  public List<GridConfig> getGridConfigs() {
    return gridConfigs;
  }

  @Override
  public String toString() {
    return WebdriversConfigBeanUtil.doToString(this);
  }

  @Override
  public int hashCode() {
    return WebdriversConfigBeanUtil.doToHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return WebdriversConfigBeanUtil.doEquals(this, obj);
  }
}
