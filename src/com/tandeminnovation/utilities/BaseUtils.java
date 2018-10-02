package com.tandeminnovation.utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


class BaseUtils {

    WebDriver driver;
    private WebDriverWait wait;

    BaseUtils(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Get an element
     * @param element
     * @return
     */
    WebElement getElement(By element) {
        return driver.findElement(element);
    }

    /**
     * Move mouse and click on element
     * @param element
     */
    void moveAndClick(By element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(getElement(element)).click().perform();
    }

    /**
     * Wait and write text on element
     * @param element
     * @param text
     */
    void waitAndWriteText(By element, String text) {
        waitToBeClickable(element);
        driver.findElement(element).sendKeys(text);
    }

    /**
     * Wait and clear text on element
     * @param element
     */
    void waitAndClearText(By element) {
        waitToBeClickable(element);
        driver.findElement(element).clear();
    }

    /**
     * Wait and read text on element
     * @param element
     * @return
     */
    String waitAndReadText(By element) {
        waitToBeClickable(element);
        return driver.findElement(element).getText();
    }

    /**
     * Wait and read attribute on element
     * @param element
     * @return
     */
    String waitAndReadAttribute(By element) {
        waitToBeClickable(element);
        return driver.findElement(element).getAttribute("value");
    }

    /**
     * Wait for element to be clickable
     * @param element
     */
    void waitToBeClickable(By element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait for element to be visable
     * @param element
     */
    void waitToBeVisable(By element) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }
}
