package junit.org.rapidpm.vaadin.addons.testbench;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.CONFIG_FOLDER;
import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER;
import static org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions.propertyReader;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.rapidpm.frp.model.Result;
import org.rapidpm.vaadin.addons.testbench.GridConfig;
import org.rapidpm.vaadin.addons.testbench.WebdriversConfig;
import org.rapidpm.vaadin.addons.testbench.WebdriversConfigFactory;

public class WebdriversConfigTest {
  private WebdriversConfigFactory factory = new WebdriversConfigFactory();

  @Test
  @DisplayName("build default config")
  void test001() {
    WebdriversConfig config = factory.createFromProperies(new Properties());

    assertEquals(DesiredCapabilities.chrome(), config.getUnittestingBrowser());
    assertEquals(SELENIUM_GRID_PROPERTIES_LOCALE_BROWSER, config.getUnittestingTarget());

    assertEquals(0, config.getGridConfigs().size());
  }

  @Test
  @DisplayName("build from properties")
  void test002() {
    Result<Properties> apply = propertyReader().apply(CONFIG_FOLDER + "config-002");
    Properties configProperties = apply.get();

    WebdriversConfig config = factory.createFromProperies(configProperties);

    assertEquals(DesiredCapabilities.firefox(), config.getUnittestingBrowser());
    assertEquals("localhost", config.getUnittestingTarget());

    assertEquals(1, config.getGridConfigs().size());

    GridConfig gridConfig = config.getGridConfigs().get(0);

    assertEquals("selenoid", gridConfig.getName());
    assertEquals("localhost", gridConfig.getTarget());

    List<DesiredCapabilities> desiredCapabilities = gridConfig.getDesiredCapabilities();

    assertEquals(4, desiredCapabilities.size());
  }

}
