package org.rapidpm.vaadin.addons.testbench.junit5.pageobject;

import org.openqa.selenium.WebDriver;
import org.rapidpm.vaadin.addons.testbench.WithID;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.CssLayoutElement;
import com.vaadin.testbench.elements.DateFieldElement;
import com.vaadin.testbench.elements.FormLayoutElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;

/**
 *
 */
public abstract class AbstractVaadinPageObject
    extends TestBenchTestCase
    implements VaadinPageObject {


  public AbstractVaadinPageObject(WebDriver webDriver) {
    setDriver(webDriver);
  }

  public void switchToDebugMode() {
    getDriver().get(url().get() + "?debug&restartApplication");
  }

  public void restartApplication() {
    getDriver().get(urlRestartApp().get());
  }

  public void loadPage() {
    final String url = url().get();
    getDriver().get(url);
  }

  public String getTitle() {
    return getDriver().getTitle();
  }
  
  public WithID<TextFieldElement> textField() {
    return id -> $(TextFieldElement.class).id(id);
  }

  public WithID<PasswordFieldElement> passwordField() {
    return id -> $(PasswordFieldElement.class).id(id);
  }

  public WithID<ButtonElement> btn() {
    return id -> $(ButtonElement.class).id(id);
  }

  public WithID<LabelElement> label() {
    return id -> $(LabelElement.class).id(id);
  }

  public WithID<GridElement> grid() {
    return id -> $(GridElement.class).id(id);
  }

  public WithID<ComboBoxElement> comboBox() {
    return id -> $(ComboBoxElement.class).id(id);
  }

  public WithID<DateFieldElement> dateField() {
    return id -> $(DateFieldElement.class).id(id);
  }

  public WithID<FormLayoutElement> formLayout() {
    return id -> $(FormLayoutElement.class).id(id);
  }

  public WithID<CssLayoutElement> cssLayout() {
    return id -> $(CssLayoutElement.class).id(id);
  }

  public WithID<HorizontalLayoutElement> horizontalLayout() {
    return id -> $(HorizontalLayoutElement.class).id(id);
  }

  public WithID<VerticalLayoutElement> verticalLayout() {
    return id -> $(VerticalLayoutElement.class).id(id);
  }


}
