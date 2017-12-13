package junit.org.rapidpm.vaadin.addons.testbench.junit5.extensions.basic.demo;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

import javax.servlet.annotation.WebServlet;

/**
 *
 */
@WebServlet(
    urlPatterns = "/*",
    name = "JumpstartServlet",
    displayName = "JumpstartServlet",
    asyncSupported = true,
    loadOnStartup = 1)
@VaadinServletConfiguration(productionMode = false, ui = BasicTestUI.class)
public class BasicTestServlet extends VaadinServlet { }
