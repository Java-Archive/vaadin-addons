package org.rapidpm.vaadin.addons.testbench;

import com.vaadin.testbench.TestBenchDriverProxy;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elementsbase.AbstractElement;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import org.openqa.selenium.WebDriver;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 */
public interface TestbenchFunctions {


  static Function<WebDriver, String> webdrivername() {
    return (d) -> (d instanceof TestBenchDriverProxy)
                  ? ((TestBenchDriverProxy) d).getActualDriver().toString()
                  : d.toString();
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
