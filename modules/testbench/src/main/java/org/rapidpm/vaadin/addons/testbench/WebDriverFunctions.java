package org.rapidpm.vaadin.addons.testbench;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static java.util.Optional.ofNullable;
import static org.openqa.selenium.By.id;

/**
 *
 */
public interface WebDriverFunctions {

    static BiFunction<WebDriver, String, Optional<WebElement>> elementFor() {
        return (driver, id) -> ofNullable(driver.findElement(id(id)));
    }

    static Consumer<WebDriver> takeScreenShot() {
        return (webDriver) -> {
            //take Screenshot
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                outputStream.write(((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES));
                //write to target/screenshot-[timestamp].jpg
                final FileOutputStream out = new FileOutputStream("target/screenshot-" + LocalDateTime.now() + ".png");
                out.write(outputStream.toByteArray());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

}
