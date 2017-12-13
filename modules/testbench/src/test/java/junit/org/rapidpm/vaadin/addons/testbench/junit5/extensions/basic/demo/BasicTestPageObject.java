package junit.org.rapidpm.vaadin.addons.testbench.junit5.extensions.basic.demo;

import com.vaadin.testbench.elements.ButtonElement;
import org.openqa.selenium.WebDriver;
import org.rapidpm.vaadin.addons.testbench.junit5.pageobject.AbstractVaadinPageObject;

import java.util.function.Supplier;

/**
 *
 */
public class BasicTestPageObject extends AbstractVaadinPageObject {

  public BasicTestPageObject(WebDriver webDriver) {
    super(webDriver);
  }

  public Supplier<ButtonElement> button = () -> btn().id(BasicTestUI.BUTTON_ID);
}
