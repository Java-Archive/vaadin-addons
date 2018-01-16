package org.rapidpm.vaadin.addons.testbench.junit5.extensions.container;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface SpringBootConf {
  public Class<?> source();

  public String[] args() default {};
}
