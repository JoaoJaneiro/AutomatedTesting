package com.tandeminnovation.launcher;

import com.tandeminnovation.manager.FileManager;
import com.tandeminnovation.utilities.FileConstants;
import org.testng.ITestNGListener;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.*;
import java.util.*;

public class SuiteLauncher {

    private FileManager file;

    /**
     * Build the XML File
     * @param path
     * @return
     */
    private List<XmlSuite> buildSuite(String path) {


        try {
            file = new FileManager(FileConstants.SUITEDIR + path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XmlSuite suite = new XmlSuite();
        suite.setName(getSuiteName());

        try {

            addParametersToSuite(suite);

            List<XmlClass> classes = new ArrayList<>();

            classes.add(new XmlClass(this.getClass().getPackage().getName() + ".TestLauncher"));

            List<XmlTest> myTests = new ArrayList<>();
            buildTest(suite, classes, myTests);
            suite.setTests(myTests);

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<XmlSuite> mySuites = new ArrayList<>();
        mySuites.add(suite);

        return mySuites;
    }

    /**
     * Build the tests to add to the XML Suite File
     * @param suite
     * @param classes
     * @param myTests
     */
    private void buildTest(XmlSuite suite, List<XmlClass> classes, List<XmlTest> myTests) {

        int counter = 0;

        while (file.getData(counter + FileConstants.TEST) != null) {

            XmlTest test = new XmlTest(suite);
            Map<String, String> parameters = new HashMap<>();

            String testName = file.getData(counter + FileConstants.TEST_NAME);
            String beforePath = file.getData(counter + FileConstants.BEFORE);
            String testPath = file.getData(counter + FileConstants.TEST);

            if (testName != null) {
                test.setName(testName);
            }
            if (beforePath != null) {
                parameters.put("before", FileConstants.TESTDIR + beforePath);
            }
            if (testPath != null) {
                parameters.put("test", FileConstants.TESTDIR + testPath);
            }

            test.setParameters(parameters);
            test.setXmlClasses(classes);
            myTests.add(test);

            counter++;
        }
    }

    /**
     * Add parameters to the XML Suite File
     * @param suite
     */
    private void addParametersToSuite(XmlSuite suite) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(FileConstants.BROWSER, getBrowser());
        parameters.put(FileConstants.URL, getURL());
        suite.setParameters(parameters);
    }

    /**
     * List all the Files on the suites directory
     * @param name
     * @return
     */
    private List<String> listAllSuites(String name){

        List<String> list = new ArrayList<String>();

        File folder = new File(name);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                list.add(file.getName());
            } else if (file.isDirectory()) {
                list.add(file.getName());
            }
        }
        return list;
    }

    /**
     * Check a file existence
     * @param path
     * @return
     */
    private boolean checkFileExistence(String path) {
        File file = new File(path);
        return file.exists() && !file.isDirectory();
    }

    /**
     * Get the name of the suite
     * @return
     */
    private String getSuiteName() {
        String suiteName = file.getData(FileConstants.SUITE_NAME);
        if (suiteName != null && !suiteName.equals("")) {

            return suiteName;
        }

        return "Please configure the Suite Name!";
    }

    /**
     * Get the browser
     * @return
     */
    private String getBrowser() {
        String browser = file.getData(FileConstants.BROWSER);
        if (browser != null && !browser.equals("")) {

            return browser;
        }

        return "Please configure a browser!";
    }

    /**
     * Get the URL of the WebPage
     * @return
     */
    private String getURL() {
        String url = file.getData(FileConstants.URL);
        if (url != null && !url.equals("")) {

            return url;
        }

        return "Please configure the page url!";
    }

    /**
     * Launch the XML Suite File
     * @param mySuites
     */
    private void launchSuite(List<XmlSuite> mySuites) {

        TestNG testNG = new TestNG();

        testNG.setXmlSuites(mySuites);

        ITestNGListener tla = new TestListenerAdapter();
        testNG.addListener(tla);

        testNG.run();

    }

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        SuiteLauncher suiteLauncher = new SuiteLauncher();

        boolean bool = true;

        while (bool) {

            System.out.println("Would you like to run all Test? (Y/N)");
            String answer = scanner.next();

            if (!answer.equals("Y") && !answer.equals("N")) {

                System.out.println("Please write a valid answer.");
            } else if (answer.equals("Y")) {

                List<String> suites = suiteLauncher.listAllSuites(FileConstants.SUITEDIR);

                for (String suite : suites) {

                    List<XmlSuite> xmlSuite = suiteLauncher.buildSuite(suite);
                    suiteLauncher.launchSuite(xmlSuite);
                }
                bool = false;
            } else if (answer.equals("N")) {

                System.out.println("Available Suites:");
                System.out.println(suiteLauncher.listAllSuites(FileConstants.SUITEDIR));
                boolean bool2 = true;

                while (bool2) {
                    System.out.println("Please enter the name of the Suite File that you want to run? (ex: Login.properties)");
                    String name = scanner.next();

                    if (suiteLauncher.checkFileExistence(FileConstants.SUITEDIR + name)) {
                        List<XmlSuite> xmlSuite = suiteLauncher.buildSuite(name);
                        suiteLauncher.launchSuite(xmlSuite);
                        bool = false;
                        bool2 = false;

                    } else {

                        System.out.println("The file: " + "'" + name + "'" + " does not exist!");
                    }
                }
            }
        }
    }
}
