package org.rapidpm.vaadin.addons.testbench;

import com.vaadin.testbench.elements.TextFieldElement;
import org.rapidpm.frp.functions.CheckedFunction;

/**
 *
 */
public interface WebElementFunctions {

  static CheckedFunction<TextFieldElement, Float> floatOfTextField() {
    return (tf) -> Float.valueOf(tf.getValue());
  }

  static CheckedFunction<TextFieldElement, Integer> intOfTextField() {
    return (tf) -> Integer.valueOf(tf.getValue());
  }

  static CheckedFunction<TextFieldElement, Long> longOfTextField() {
    return (tf) -> Long.valueOf(tf.getValue());
  }


}
