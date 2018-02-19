package junit.org.rapidpm.vaadin.addons.testbench;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.rapidpm.vaadin.addons.testbench.BrowserDriverFunctions;

public class BrowserDriverFunctionsTest {
  @Test
  @DisplayName("test reading properties")
  void test001() {
    Properties properties = BrowserDriverFunctions.propertyReader()
        .apply(BrowserDriverFunctions.CONFIG_FOLDER + "config").get();

    boolean isLocaleOrRapidSelnoid = "locale".equals(properties.get("unittesting.target"))
        || "selenoid.rapidpm.org".equals(properties.get("unittesting.target"));
    assertTrue(isLocaleOrRapidSelnoid);
  }

}
