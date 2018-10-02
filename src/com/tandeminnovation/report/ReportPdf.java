package com.tandeminnovation.report;

import com.tandeminnovation.builder.PdfBuilder;
import com.tandeminnovation.launcher.TestLauncher;
import com.tandeminnovation.manager.FileManager;
import com.tandeminnovation.utilities.FileConstants;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Random;

public class ReportPdf implements ITestListener, ISuiteListener {

//    public FileManager configuration;
    private static String className = "";
    private static String methodName = "";

    private static int number = 1;
    private static int nPass, nFail, nSkipped;

    private static long startTime;
    private static long endTime;

    @Override
    public void onStart(ISuite suite) {

//        try {
//            configuration = new FileManager(FileConstants.CONFIGURATION);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String name = suite.getName();
        String browser = suite.getParameter(FileConstants.BROWSER);
        String url = suite.getParameter(FileConstants.URL);

        Reporter.log("Executing: " + name, true);
        Reporter.log("Browser: " + browser, true);
        Reporter.log("Page URL: " + url, true);

        FileOutputStream file = null;

        try {
            file = new FileOutputStream(new File("C:/reports/" + name + ".pdf"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        try {
//            file = new FileOutputStream(new File(configuration.getData(FileConstants.REPORT) + name + ".pdf"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        PdfBuilder.buildDocument(file);
        PdfBuilder.buildHeader(name, browser, url);
        PdfBuilder.buildResultTable();

    }

    @Override
    public void onStart(ITestContext context) {
        Reporter.log("", true);
        Reporter.log("Executing: " + context.getName(), true);
        Reporter.log("Start Date: " + context.getStartDate(), true);

        PdfBuilder.addElementsToTable(Integer.toString(number++));
        PdfBuilder.addElementsToTable(context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {

        startTime = result.getStartMillis();

        if (className.equals("")) {

            className = result.getInstanceName();
        }
        if (methodName.equals("")) {

            methodName = result.getName();
        }

        PdfBuilder.buildGeneralTable(className, methodName);
        PdfBuilder.addElementsToTable(getArguments(result));

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        endTime = result.getEndMillis();

        Reporter.log("Test status: Passed", true);

        PdfBuilder.addElementsToTable("Passed");
        PdfBuilder.addElementsToTable("");
        PdfBuilder.addElementsToTable(Long.toString(endTime - startTime));

        nPass++;
    }

    @Override
    public void onTestFailure(ITestResult result) {
        endTime = result.getEndMillis();
        Reporter.log("Test status: Failed", true);
        String screenShot = screenShot(result);
        Reporter.log("Screenshot captured", true);
        PdfBuilder.addElementsToTable("Failed");
        PdfBuilder.addElementsToTable(screenShot);
        PdfBuilder.addElementsToTable(Long.toString(endTime - startTime));
        nFail++;
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        endTime = result.getEndMillis();

        Reporter.log("Test status: Skipped", true);

        PdfBuilder.addElementsToTable("Skipped");
        PdfBuilder.addElementsToTable("Skipped");
        PdfBuilder.addElementsToTable(Long.toString(endTime - startTime));

        nSkipped++;
    }

    @Override
    public void onFinish(ITestContext context) {
        Reporter.log("Finishing: " + context.getName(), true);
        Reporter.log("Finish Date: " + context.getEndDate(), true);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onFinish(ISuite suite) {
        Reporter.log("", true);
        Reporter.log("Finishing: " + suite.getName(), true);

        PdfBuilder.createPieChart(nPass, nFail, nSkipped);
        PdfBuilder.addToDocument();
        PdfBuilder.closeDocument();

        number = 1;
    }

    private String getArguments(ITestResult result) {

        StringBuilder inputArguments = new StringBuilder();

        Object[] inputArgs = result.getParameters();

        if (inputArgs != null && inputArgs.length > 0) {
            for (Object inputArg : inputArgs) {
                if (inputArg == null) {
                    inputArguments.append("null");
                } else {
                    inputArguments.append(inputArg.toString());
                }
                inputArguments.append(", ");
            }
            inputArguments.delete(inputArguments.length() - 2, inputArguments.length() - 1);
        }
        return inputArguments.toString();
    }

    private String screenShot(ITestResult result) {

        String path = "C:/reports/screenshot/";
        String name = (new Random().nextInt()) + ".png";
        try {
            Object base = result.getInstance();
            WebDriver driver = ((TestLauncher) base).getDriver();

            TakesScreenshot takesScreenshot = ((TakesScreenshot) driver);
            File image = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File destinationFile = new File(path + name);
            FileUtils.copyFile(image, destinationFile);

        } catch (IOException e) {
            System.out.println("Error taking screenshot: " + e.getMessage());
            e.printStackTrace();
        }
        return name;
    }

}
