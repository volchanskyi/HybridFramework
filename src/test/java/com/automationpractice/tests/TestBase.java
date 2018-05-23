package com.automationpractice.tests;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.automationpractice.operation.ApplicationManager;

public class TestBase implements ITest {

    private final Logger logger = LoggerFactory.getLogger(TestBase.class);
    private static WebDriver driver;
    private static final ApplicationManager APP = new ApplicationManager(driver);

    // Initialization of AppManager and passing TC name or Step
    protected void init(String testcaseName) throws IOException {
	APP.init(testcaseName);
    }

    // Initialization of AppManager and passing Step details (TC actions)
    protected void verify(String keyword, String objectName, String objectType, String value)
	    throws IOException, Exception {
	APP.verify(keyword, objectName, objectType, value);
    }

    // Runs everytime when takes new params from the XLS
    @BeforeMethod(alwaysRun = true)
    protected void setUp(Method method, Object[] parameters) {
	// Enable steps debugging, prints parameters out
	logger.error("Start test " + method.getName() + " with params " + Arrays.asList(parameters));
	// Initialization of AppManager and passing String as a test step(For reporting)
	APP.setTestName(
		Arrays.asList(parameters).subList(0, 3).toString().replace(",", "").replace("[", "").replace("]", ""));
    }

    @AfterMethod(alwaysRun = true)
    protected void tearDown(Method method, Object[] parameters, ITestResult result) throws IOException {
	// Marks when a step is finished
	logger.error("Stop test " + method.getName());
	// take a screenshot when an exception occurs
	APP.onException(result);
    }

    // For reporting
    @Override
    public String getTestName() {
	// Initialization of AppManager and return TC step from AppManager
	return APP.getTestName();
    }

}
