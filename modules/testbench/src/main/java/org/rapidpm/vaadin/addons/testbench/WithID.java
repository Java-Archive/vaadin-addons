package org.rapidpm.vaadin.addons.testbench;

import com.vaadin.testbench.elementsbase.AbstractElement;

/**
 *
 */
@FunctionalInterface
public interface WithID<T extends AbstractElement> {
  T id(String id);
}
