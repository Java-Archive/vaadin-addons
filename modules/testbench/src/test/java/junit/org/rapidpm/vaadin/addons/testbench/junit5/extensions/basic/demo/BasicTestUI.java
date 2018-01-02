package junit.org.rapidpm.vaadin.addons.testbench.junit5.extensions.basic.demo;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

import static org.rapidpm.vaadin.addons.framework.ComponentIDGenerator.buttonID;

/**
 *
 */
public class BasicTestUI extends UI {

  public static final String BUTTON_ID = buttonID().apply(BasicTestUI.class, "buttonID");

  @Override
  protected void init(VaadinRequest request) {
    final Button button = new Button();
    button.setId(BasicTestUI.BUTTON_ID);
    button.setCaption(BUTTON_ID);
    button.addClickListener(e -> System.out.println("e = " + e));
    setContent(button);
  }
}
