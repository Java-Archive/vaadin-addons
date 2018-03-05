package junit.org.rapidpm.vaadin.addons.remote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.TestTemplate;
import org.rapidpm.vaadin.addons.testbench.junit5.extensions.seleniumhub.VaadinCompatTest;

@VaadinCompatTest
public class BasicRemoteCompatTest {
  @TestTemplate
  void test001(GooglePageObject pageObject) {
    pageObject.loadPage();
    assertEquals("Google", pageObject.getTitle());
  }
}
