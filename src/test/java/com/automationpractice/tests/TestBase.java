package com.automationpractice.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.automationpractice.operation.ReadObject;
import com.automationpractice.operation.UIOperation;

public class TestBase implements ITest {
    private WebDriver driver;
    private static String testName = "";
    final private Logger logger = LoggerFactory.getLogger(TestBase.class);

    public String getTestName() {
	return testName;
    }

    public static void setTestName(String param) {
	testName = param;
    }

    // Runs everytime when takes new params from the XLS
    @BeforeMethod(alwaysRun = true)
    private void beforeMethod(Method method, Object[] parameters) {
	logger.debug("Start test " + method.getName() + " with params " + Arrays.asList(parameters));

    }

    // @BeforeTest

    protected void init(String testcaseName) {

	if (!(testName.regionMatches(true, 0, testcaseName, 0, 10))) {
	    if (testcaseName != null && testcaseName.length() != 0) {
		// this.testName = testcaseName;
		launchBrowser();
		setTestName(testcaseName);
	    }
	} else
	    return;
    }

    private void launchBrowser() {

	if (System.getProperty("browser").toUpperCase().contains("HEADLESS")) {
	    ChromeOptions option = new ChromeOptions();
	    String driverPath = "";
	    if (System.getProperty("os.name").toUpperCase().contains("MAC")) {
		driverPath = "./src/test/resources/webdrivers/mac/chromedriver";
		option.addArguments("-headless");
		option.addArguments("-disable-gpu");
		option.addArguments("-window-size=1200x600");
	    } else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
		driverPath = "./src/test/resources/webdrivers/pc/chromedriver.exe";
		option.addArguments("--headless");
		option.addArguments("--disable-gpu");
		option.addArguments("--window-size=1200x600");
	    } else
		throw new IllegalArgumentException("Unknown OS");
	    System.setProperty("webdriver.chrome.driver", driverPath);
	    System.setProperty("webdriver.chrome.silentOutput", "true");
	    option.addArguments("headless");
	    option.addArguments("--disable-notifications");
	    driver = new ChromeDriver(option);
	    
	} else if (System.getProperty("browser").toUpperCase().contains("CHROME")) {
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

	} else if (System.getProperty("browser").toUpperCase().contains("FIREFOX")) {
	    // TODO fix Firefox options
	    FirefoxOptions option = new FirefoxOptions();
	    String driverPath = "";
	    if (System.getProperty("os.name").toUpperCase().contains("MAC")) {
		driverPath = "./src/test/resources/webdrivers/mac/geckodriver.sh";
		option.addArguments("-start-fullscreen");
	    } else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
		driverPath = "./src/test/resources/webdrivers/pc/geckodriver.exe";
		option.addArguments("--start-maximized");
	    } else
		throw new IllegalArgumentException("Unknown OS");
	    System.setProperty("webdriver.gecko.driver", driverPath);
	    driver = new FirefoxDriver(option);

	} else {
	    // -------------------
	}
    }

    // --------------------------
    protected void app(String keyword, String objectName, String objectType, String value)
	    throws IOException, Exception {
	ReadObject object = new ReadObject();
	Properties allObjects = object.getUIObjectRepository();
	UIOperation operation = new UIOperation(driver);

	// Call perform function to perform operation on UI
	// Verify
	assertThat(operation.perform(allObjects, keyword, objectName, objectType, value), equalTo(value));

    }

    @AfterMethod(alwaysRun = true)
    private void logTestStop(Method method, Object[] parameters) {
	logger.debug("Stop test " + method.getName());

    }

    // @BeforeSuite

    // @AfterTest

}
