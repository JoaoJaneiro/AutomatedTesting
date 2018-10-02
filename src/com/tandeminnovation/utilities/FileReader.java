package com.tandeminnovation.utilities;

import com.tandeminnovation.manager.FileManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

public class FileReader extends BaseUtils {

    public FileReader(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /**
     * Parse a TestCase File and read values
     * @param fileManager
     */
    public void parseFile(FileManager fileManager) {

        int step = 0;
        int asserts = 0;

        while (fileManager.getData(step + FileConstants.ELEMENT) != null) {

            By element;
            String action = fileManager.getData(step + FileConstants.ELEMENT + FileConstants.ACTION);

            switch (action) {
                case "click":
                    element = fileManager.getLocator(step + FileConstants.ELEMENT);
                    waitToBeClickable(element);
                    moveAndClick(element);
                    break;
                case "write":
                    element = fileManager.getLocator(step + FileConstants.ELEMENT);
                    waitAndClearText(element);
                    waitAndWriteText(element, fileManager.getData(step + FileConstants.ELEMENT + FileConstants.VALUE));
                    break;
                case "dropdown":
                    element = fileManager.getLocator(step + FileConstants.ELEMENT);
                    waitToBeClickable(element);
                    Select state = new Select(getElement(fileManager.getLocator(step + FileConstants.ELEMENT)));
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    state.selectByVisibleText(fileManager.getData(step + FileConstants.ELEMENT + FileConstants.VALUE));
                    break;
                default:
                    System.out.println("That action '" + action + "' is not valid!");
                    break;
            }
            step++;
        }

        while (fileManager.getData(asserts + FileConstants.ASSERT) != null) {

            By element;
            String type = fileManager.getData(asserts + FileConstants.ASSERT_TYPE);
            String value = fileManager.getData(asserts + FileConstants.ASSERT_VALUE);

            switch (type) {
                case "text":
                    element = fileManager.getLocator(asserts + FileConstants.ASSERT);
                    waitToBeVisable(element);
                    Assert.assertEquals(waitAndReadText(element), value);
                    break;
                case "attribute":
                    element = fileManager.getLocator(asserts + FileConstants.ASSERT);
                    waitToBeVisable(element);
                    Assert.assertEquals(waitAndReadAttribute(element), value);
                    break;
                default:
                    System.out.println("That assert type '" + type + "' is not valid!");
                    break;
            }
            asserts++;
        }
    }
}
