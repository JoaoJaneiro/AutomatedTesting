package com.tandeminnovation.launcher;

import com.tandeminnovation.manager.FileManager;
import com.tandeminnovation.report.Report;
import com.tandeminnovation.report.ReportPdf;
import com.tandeminnovation.utilities.FileConstants;
import com.tandeminnovation.utilities.FileReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.IOException;

@Listeners(Report.class)
public class TestLauncher {

    private static final String NOFILE = "noFile";
    private static final String BEFORE = "before";
    private static final String TEST = "test";

    private FileReader fileReader;
    private WebDriver driver;
    private WebDriverWait wait;

    /**
     * Configure drivers and navigate to the page
     * @param browser
     * @param URL
     * @throws IOException
     */
    @Parameters({FileConstants.BROWSER, FileConstants.URL})
    @BeforeClass
    public void beforeClass(String browser, String URL) throws IOException {
        FileManager drivers = new FileManager(FileConstants.CONFIGURATION);
        switch (browser) {
            case FileConstants.CHROME:
                System.setProperty(drivers.getData(FileConstants.CHROMEDRIVER), drivers.getData(FileConstants.CHROMEPATH));
                driver = new ChromeDriver();
                break;
            case FileConstants.FIREFOX:
                System.setProperty(drivers.getData(FileConstants.FFDRIVER), drivers.getData(FileConstants.FFPATH));
                driver = new FirefoxDriver();
                break;
            default:
                System.out.println("Browser configuration failed!");
                break;
        }
        driver.get(URL);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 20);
    }

    /**
     * Automate an action before the test
     * @param file
     * @throws Exception
     */
    @Parameters({BEFORE})
    @BeforeMethod
    public void beforeMethod(@Optional(NOFILE) String file) throws Exception {
        fileReader = new FileReader(driver, wait);
        if (!file.equals(NOFILE)) {

            FileManager beforeTest = new FileManager(file);
            fileReader.parseFile(beforeTest);
        }
    }

    /**
     * Launch the test
     * @param file
     * @throws Exception
     */
    @Parameters({TEST})
    @Test
    public void generalTest(String file) throws Exception {
        FileManager test = new FileManager(file);
        fileReader.parseFile(test);
    }

    /**
     * Close the driver
     */
    @AfterClass
    public void afterClass() {
        driver.quit();
    }

    public WebDriver getDriver() {
        return driver;
    }

}
