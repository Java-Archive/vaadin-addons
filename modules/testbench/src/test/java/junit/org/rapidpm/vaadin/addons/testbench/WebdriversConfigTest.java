package junit.org.rapidpm.vaadin.addons.testbench;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.*;
import org.rapidpm.frp.model.Result;
import org.rapidpm.vaadin.addons.testbench.WebdriversConfig;

public class WebdriversConfigTest {
  @Test
  @DisplayName("build default config")
  void test001() {
    WebdriversConfig config = new WebdriversConfig(new Properties());

    assertEquals(DesiredCapabilities.chrome(), config.getUnittestingBrowser());
    assertEquals(SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER, config.getUnittestingTarget());
  }

  @Test
  @DisplayName("build from properties")
  void test002() {
    Result<Properties> apply = propertyReader().apply(CONFIG_FOLDER + "config-002");
    Properties configProperties = apply.get();

    WebdriversConfig config = new WebdriversConfig(configProperties);

    assertEquals(DesiredCapabilities.firefox(), config.getUnittestingBrowser());
    assertEquals("localhost", config.getUnittestingTarget());
  }
}
