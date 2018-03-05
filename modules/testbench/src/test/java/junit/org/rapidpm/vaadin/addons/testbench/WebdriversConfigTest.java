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
import org.rapidpm.vaadin.addons.testbench.GridConfig.Type;
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
    assertEquals("http://localhost:4444/wd/hub", config.getUnittestingTarget());

    assertEquals(2, config.getGridConfigs().size());

    GridConfig genericGridConfig = config.getGridConfigs().stream()
        .filter(grid -> grid.getName().equals("generic")).findFirst().get();

    assertEquals("http://localhost:4444/wd/hub", genericGridConfig.getTarget());
    assertEquals(Type.GENERIC, genericGridConfig.getType());
    
    List<DesiredCapabilities> desiredCapabilities = genericGridConfig.getDesiredCapabilities();

    assertEquals(4, desiredCapabilities.size());
    
    GridConfig selenoidGridConfig = config.getGridConfigs().stream()
        .filter(grid -> grid.getName().equals("selenoid")).findFirst().get();
    
    List<DesiredCapabilities> slenoidDesiredCapabilities = selenoidGridConfig.getDesiredCapabilities();

    assertEquals(2, slenoidDesiredCapabilities.size());
    assertEquals(Type.SELENOID, selenoidGridConfig.getType());
    
    for(DesiredCapabilities desiredCapability: slenoidDesiredCapabilities) {
      assertEquals(true, desiredCapability.asMap().get("enableVNC"));
      assertEquals(true, desiredCapability.asMap().get("enableVideo"));
    }
  }

  @Test
  @DisplayName("build browserstack config")
  void test003() {
    Result<Properties> apply = propertyReader().apply(CONFIG_FOLDER + "config-003");
    Properties configProperties = apply.get();

    WebdriversConfig config = factory.createFromProperies(configProperties);

    assertEquals(1, config.getGridConfigs().size());

    GridConfig genericGridConfig = config.getGridConfigs().stream()
        .filter(grid -> grid.getName().equals("browserstack")).findFirst().get();

    assertEquals("https://danielnordhoffve1:abc@hub-cloud.browserstack.com/wd/hub",
        genericGridConfig.getTarget());
    assertEquals(Type.BROWSERSTACK, genericGridConfig.getType());
  }
}
