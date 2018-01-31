package org.rapidpm.vaadin.addons.testbench;

import java.util.Properties;
import org.openqa.selenium.remote.DesiredCapabilities;
import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.*;

public class WebdriversConfig {
  public static final String UNITTESTING_BROWSER = "unittesting.browser";
  public static final String UNITTESTING_TARGET = "unittesting.target";

  private final String unittestingTarget;
  private final DesiredCapabilities unittestingBrowser;

  public WebdriversConfig(Properties configProperties) {
    unittestingBrowser = type2Capabilities()
        .apply(configProperties.getProperty(UNITTESTING_BROWSER, "chrome")).get();
    unittestingTarget =
        configProperties.getProperty(UNITTESTING_TARGET, SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER);
  }

  public String getUnittestingTarget() {
    return unittestingTarget;
  }

  public DesiredCapabilities getUnittestingBrowser() {
    return unittestingBrowser;
  }
}
