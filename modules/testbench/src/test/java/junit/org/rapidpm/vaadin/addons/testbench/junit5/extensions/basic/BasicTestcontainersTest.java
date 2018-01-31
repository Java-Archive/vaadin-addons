package junit.org.rapidpm.vaadin.addons.testbench.junit5.extensions.basic;

import junit.org.rapidpm.vaadin.addons.testbench.junit5.extensions.basic.demo.BasicTestPageObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.rapidpm.vaadin.addons.testbench.junit5.extensions.testcontainers.VaadinTestcontainersTest;

/**
 *
 */

@VaadinTestcontainersTest
@Disabled
public class BasicTestcontainersTest {


  @Test
  void test001(BasicTestPageObject pageObject) {


//    pageObject.setDriver(pageObject.webDriverSupplier.get());
//    pageObject.loadPage();
//
//    pageObject.button.get().click();
//    takeScreenShot().accept(pageObject.getDriver());
  }
}
