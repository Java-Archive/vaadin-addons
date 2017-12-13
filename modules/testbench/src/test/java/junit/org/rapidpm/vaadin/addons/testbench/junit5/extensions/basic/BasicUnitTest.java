package junit.org.rapidpm.vaadin.addons.testbench.junit5.extensions.basic;

import junit.org.rapidpm.vaadin.addons.testbench.junit5.extensions.basic.demo.BasicTestPageObject;
import org.junit.jupiter.api.Test;
import org.rapidpm.vaadin.addons.testbench.junit5.pageobject.PageObject;
import org.rapidpm.vaadin.addons.testbench.junit5.extensions.unittest.VaadinUnitTest;

import static org.rapidpm.vaadin.addons.testbench.WebDriverFunctions.takeScreenShot;

/**
 *
 */

@VaadinUnitTest
public class BasicUnitTest {

  @Test
  void test001(@PageObject BasicTestPageObject pageObject) {
    pageObject.loadPage();
    pageObject.button.get().click();
    takeScreenShot().accept(pageObject.getDriver());
  }
}
