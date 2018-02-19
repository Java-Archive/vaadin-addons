package org.rapidpm.vaadin.addons.testbench;

import java.util.List;
import org.openqa.selenium.remote.DesiredCapabilities;
import net.vergien.beanautoutils.annotation.Bean;

@Bean
public class WebdriversConfig {
  public static final String CHROME_BINARY_PATH = "chrome.binary.path";
  public static final String UNITTESTING_BROWSER = "unittesting.browser";
  public static final String UNITTESTING_TARGET = "unittesting.target";
  public static final String COMPATTESTING = "compattesting";
  public static final String COMPATTESTING_GRID = COMPATTESTING + ".grid";
  private final String unittestingTarget;
  private final DesiredCapabilities unittestingBrowser;
  private final List<GridConfig> gridConfigs;


  public WebdriversConfig(String unittestingTarget, DesiredCapabilities unittestingBrowser,
      List<GridConfig> gridConfigs) {
    this.unittestingTarget = unittestingTarget;
    this.unittestingBrowser = unittestingBrowser;
    this.gridConfigs = gridConfigs;
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
