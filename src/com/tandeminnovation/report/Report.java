package com.tandeminnovation.report;

import com.tandeminnovation.utilities.FileConstants;
import org.testng.*;

public class Report implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        Reporter.log("Executing: " + suite.getName(), true);
        Reporter.log("Browser: " + suite.getParameter(FileConstants.BROWSER), true);
        Reporter.log("Page URL: " + suite.getParameter(FileConstants.URL), true);
    }

    @Override
    public void onStart(ITestContext context) {
        Reporter.log("", true);
        Reporter.log("Executing: " + context.getName(), true);
        Reporter.log("Start Date: " + context.getStartDate(), true);
    }

    @Override
    public void onTestStart(ITestResult result) {
        Reporter.log("ClassName: " + result.getInstanceName(), true);
        Reporter.log("MethodName: " + result.getName(), true);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Reporter.log("Test status: Passed", true);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Reporter.log("Test status: Failed", true);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Reporter.log("Test status: Skipped", true);
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
    }
}
