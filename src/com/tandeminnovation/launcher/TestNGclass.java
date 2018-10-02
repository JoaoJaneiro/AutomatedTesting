package com.tandeminnovation.launcher;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class TestNGclass {

    public WebDriver driver;

    @Test
    public void main() {

        System.out.println("Este é o teste");

    }

    @BeforeMethod
    public void beforeMethod() {

        // Create a new instance of the Firefox driver
        System.setProperty("webdriver.chrome.driver", "C:/Users/Asus/Desktop/ISEL/PRJ/Jars/drivers/chromedriver_win32/chromedriver.exe");
        driver = new ChromeDriver();

        //Put a Implicit wait, this means that any search for elements on the page could take the time the implicit wait is set for before throwing exception

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //Launch the Online Store Website

        driver.get("http://www.facebook.com");

    }

    @AfterMethod
    public void afterMethod() {
        // Close the driver
        driver.quit();
    }

}
