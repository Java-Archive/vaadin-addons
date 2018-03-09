package junit.org.rapidpm.vaadin.addons.remote;

import java.util.function.Supplier;
import org.openqa.selenium.WebDriver;
import org.rapidpm.vaadin.addons.testbench.junit5.pageobject.AbstractVaadinPageObject;

public class GooglePageObject extends AbstractVaadinPageObject {

  public GooglePageObject(WebDriver webDriver) {
    super(webDriver);
  }

  @Override
  public Supplier<String> url() {
    return () -> "https://www.google.com";
  }
}
