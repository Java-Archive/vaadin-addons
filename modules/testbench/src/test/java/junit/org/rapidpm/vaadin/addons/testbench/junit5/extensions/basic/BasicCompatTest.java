package junit.org.rapidpm.vaadin.addons.testbench.junit5.extensions.basic;

import junit.org.rapidpm.vaadin.addons.testbench.junit5.extensions.basic.demo.BasicTestPageObject;
import org.junit.jupiter.api.TestTemplate;
import org.rapidpm.vaadin.addons.testbench.junit5.extensions.seleniumhub.VaadinCompatTest;

/**
 *
 */
@VaadinCompatTest
class BasicCompatTest {

  @TestTemplate
  void testTemplate(BasicTestPageObject pageObject) {
    pageObject.loadPage();
    pageObject.button.get().click();
    pageObject.screenshot();
  }
}
