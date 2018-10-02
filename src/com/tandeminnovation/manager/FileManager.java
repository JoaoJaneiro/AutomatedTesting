package com.tandeminnovation.manager;

import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FileManager {

    private Properties properties;
    private String FilePath;

    public FileManager(String FilePath) throws IOException {
        properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(FilePath);
        properties.load(fileInputStream);
        this.FilePath = FilePath;
    }

    public String fileName() {
        return FilePath;
    }

    /**
     * Get Data from a property
     * @param ElementName
     * @return
     */
    public String getData(String ElementName) {

        return properties.getProperty(ElementName);
    }

    /**
     * Get a locator value
     * @param ElementName
     * @return
     */
    public By getLocator(String ElementName) {

        String locator = properties.getProperty(ElementName);

        if (locator != null) {

            String locatorType = locator.split(":")[0];
            String locatorValue = locator.split(":")[1];

            switch (locatorType.toLowerCase()) {
                case "id":
                    return By.id(locatorValue);
                case "name":
                    return By.name(locatorValue);
                case "classname":
                case "class":
                    return By.className(locatorValue);
                case "tagname":
                case "tag":
                    return By.className(locatorValue);
                case "linktext":
                case "link":
                    return By.linkText(locatorValue);
                case "partiallinktext":
                    return By.partialLinkText(locatorValue);
                case "cssselector":
                case "css":
                    return By.cssSelector(locatorValue);
                case "xpath":
                    return By.xpath(locatorValue);
                default:
            }
        }
        return null;
    }
}
