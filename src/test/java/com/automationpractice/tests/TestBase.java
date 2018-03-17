package com.automationpractice.tests;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.automationpractice.operation.ApplicationManager;

public class TestBase implements ITest {

    private final Logger logger = LoggerFactory.getLogger(TestBase.class);
    private static WebDriver driver;
    private static final ApplicationManager APP = new ApplicationManager(driver);
    
    //Initialization of AppManager and passing TC name
    protected void init(String testcaseName) throws IOException {
	APP.init(testcaseName);
    }
    
    ////Initialization of AppManager and passing TC details
    protected void verify(String keyword, String objectName, String objectType, String value)
	    throws IOException, Exception {
	APP.verify(keyword, objectName, objectType, value);
    }

    // Runs everytime when takes new params from the XLS
    @BeforeMethod(alwaysRun = true)
    private void setUp(Method method, Object[] parameters) {
	    //Enable steps debugging, prints  parameters out
	logger.debug("Start test " + method.getName() + " with params " + Arrays.asList(parameters));

    }


    @AfterMethod(alwaysRun = true)
    private void tearDown(Method method, Object[] parameters) {
	    //Marks when a step is finished
	logger.debug("Stop test " + method.getName());

    }

    @Override
    public String getTestName() {
	return APP.getTestName();
    }

    // @BeforeSuite

    // @AfterTest

}
