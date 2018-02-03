package com.automationpractice.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.automationpractice.operation.ReadObject;
import com.automationpractice.operation.UIOperation;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

public class TestBase implements ITest {
    WebDriver driver;
    private String testName = "";
    final Logger logger = LoggerFactory.getLogger(TestBase.class);

    public String getTestName() {
	return testName;
    }

    private void setTestName(String param) {
	testName = param;
    }

    // Before test
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method, Object[] parameters) {
	logger.debug("Start test " + method.getName() + " with params" + Arrays.asList(parameters));
	setTestName(method.getName());
	Override a = method.getAnnotation(Override.class);
	String testCaseId = (String) parameters[a.id()];
	if (testCaseId != null && testCaseId.length() != 0) {
	    setTestName(testCaseId);
	} else {
	    setTestName((String) parameters[a.id()]);
	}
    }

    protected void ivRun(String testcaseName) {
	if (testcaseName != null && testcaseName.length() != 0) {
	    ChromeOptions option = new ChromeOptions();
	    String driverPath = "";
	    if (System.getProperty("os.name").toUpperCase().contains("MAC")) {
		driverPath = "./src/test/resources/webdrivers/mac/chromedriver";
		option.addArguments("-start-fullscreen");
	    } else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
		driverPath = "./src/test/resources/webdrivers/pc/chromedriver.exe";
		option.addArguments("--start-maximized");
	    } else
		throw new IllegalArgumentException("Unknown OS");
	    System.setProperty("webdriver.chrome.driver", driverPath);
	    System.setProperty("webdriver.chrome.silentOutput", "true");
	    option.addArguments("disable-infobars");
	    option.addArguments("--disable-notifications");
	    driver = new ChromeDriver(option);

	}
    }

    // --------------------------
    protected void ivRead(String keyword, String objectName, String objectType, String value)
	    throws IOException, Exception {
	ReadObject object = new ReadObject();
	Properties allObjects = object.getUIObjectRepository();
	UIOperation operation = new UIOperation(driver);

	// Call perform function to perform operation on UI
	// Verify
	assertThat(operation.perform(allObjects, keyword, objectName, objectType, value), equalTo(value));

    }

    @AfterMethod(alwaysRun = true)
    public void logTestStop(Method method) {
	logger.debug("Stop test " + method.getName());

//	LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//	// print logback's internal status
//	StatusPrinter.print(lc);
	// TODO add close browser method
    }

    // @BeforeClass

    // @AfterTest

}
