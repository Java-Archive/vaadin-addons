package org.rapidpm.vaadin.addons.testbench;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.success;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.rapidpm.frp.model.Result;
import com.vaadin.testbench.TestBenchDriverProxy;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elementsbase.AbstractElement;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;

/**
 *
 */
public interface TestbenchFunctions {


  static Function<WebDriver, String> webdrivername() {
    return proxyedDriver -> doToString().apply(unproxy().apply(proxyedDriver));
  }

  static Function<WebDriver, WebDriver> unproxy() {
    return proxyedDriver -> (proxyedDriver instanceof TestBenchDriverProxy)
        ? ((TestBenchDriverProxy) proxyedDriver).getActualDriver()
        : proxyedDriver;
  }

  static Function<WebDriver, String> doToString() {
    return driver -> {
      Result<String> result = match(
          matchCase(() -> success(driver.toString())),
          matchCase(() -> driver instanceof RemoteWebDriver, () -> success(format((RemoteWebDriver) driver))));
      return result.get();
    };
  }

  static String format(RemoteWebDriver driver) {
    return driver.getCapabilities().getBrowserName() + " " + driver.getCapabilities().getVersion()
        + " / " + driver.getCapabilities().getPlatform();
  }

  static Function<Class<? extends AbstractComponent>, Optional<Class<? extends AbstractElement>>> conv() {
    return (componentClass) -> {
      final Predicate<Class<? extends AbstractComponent>> is = componentClass::isAssignableFrom;

      if (is.test(Button.class)) return Optional.of(ButtonElement.class);
      if (is.test(TextField.class)) return Optional.of(TextFieldElement.class);

      return Optional.empty();
    };
  }

}
