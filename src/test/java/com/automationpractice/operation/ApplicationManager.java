package com.automationpractice.operation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

import com.automationpractice.tests.TestBase;

public class ApplicationManager {

    private final Logger logger = LoggerFactory.getLogger(TestBase.class);
    private WebDriver driver;
    private String testName = "";
    private String driverPath = "";

    // App Manager implements main logic of the app
    public ApplicationManager(WebDriver driver) {
	this.driver = driver;
    }

    // reads step in a TC
    public String getTestName() {
	return this.testName;
    }

    // rewrites step in a TC
    public void setTestName(String param) {
	this.testName = param;

    }

    // Takes first param as a TC name or Step in the TC
    public void init(String testcaseName) throws IOException {
	// Launch a browser when reads a new TC
	if ((testcaseName != null && testcaseName.length() != 0) && !testcaseName.contains("Step")) {
	    // NEW TC NAME WILL LAUNCH NEW BROWSER INSTANCE
	    launchBrowser();
	}
	// If there`s word - Step, then skip launching browser
	else
	    return;
    }

    private void launchBrowser() {
	// Check for browser property and assign browser options
	if (isBrowser("HEADLESS")) {
	    ChromeOptions option = new ChromeOptions();
	    if (isPlatform("MAC")) {
		this.driverPath = "./src/test/resources/webdrivers/mac/chromedriver";
		option.addArguments("-headless");
		// option.addArguments("-disable-gpu");
		option.addArguments("-window-size=1920x1080");
	    } else if (isPlatform("WINDOWS")) {
		this.driverPath = "./src/test/resources/webdrivers/pc/chromedriver.exe";
		option.addArguments("--headless");
		// option.addArguments("--disable-gpu");
		option.addArguments("--window-size=1920x1080");
	    } else
		throw new IllegalArgumentException("Unknown OS");
	    System.setProperty("webdriver.chrome.driver", this.driverPath);
	    System.setProperty("webdriver.chrome.silentOutput", "true");
	    option.addArguments("headless");
	    option.addArguments("--disable-notifications");
	    this.driver = new ChromeDriver(option);

	} else if (isBrowser("CHROME")) {
	    ChromeOptions option = new ChromeOptions();
	    if (isPlatform("MAC")) {
		this.driverPath = "./src/test/resources/webdrivers/mac/chromedriver";
		option.addArguments("-start-fullscreen");
	    } else if (isPlatform("WINDOWS")) {
		this.driverPath = "./src/test/resources/webdrivers/pc/chromedriver.exe";
		option.addArguments("--start-maximized");
	    } else
		throw new IllegalArgumentException("Unknown OS");
	    System.setProperty("webdriver.chrome.driver", this.driverPath);
	    System.setProperty("webdriver.chrome.silentOutput", "true");
	    option.addArguments("disable-infobars");
	    option.addArguments("--disable-notifications");
	    this.driver = new ChromeDriver(option);

	} else if (isBrowser("FIREFOX")) {
	    FirefoxOptions option = new FirefoxOptions();
	    option.setLegacy(true);
	    if (isPlatform("MAC")) {
		this.driverPath = "./src/test/resources/webdrivers/mac/geckodriver.sh";
	    } else if (isPlatform("WINDOWS")) {
		this.driverPath = "./src/test/resources/webdrivers/pc/geckodriver.exe";
	    } else
		throw new IllegalArgumentException("Unknown OS");
	    System.setProperty("webdriver.gecko.driver", this.driverPath);
	    this.driver = new FirefoxDriver(option);
	    this.driver.manage().window().maximize();
	} else {
	}
    }

    // Distinguish OS
    private boolean isPlatform(String platform) {
	return System.getProperty("os.name").toUpperCase().contains(platform);
    }

    // Distinguish Browser
    private boolean isBrowser(String browser) {
	return System.getProperty("browser").toUpperCase().contains(browser);
    }

    // Pass step details(Action, Locator, Access By, Expected Result)
    public void verify(String keyword, String objectName, String objectType, String value)
	    throws IOException, Exception {
	// Create Object Reader instance to read data from property file (or.properties)
	ReadObject object = new ReadObject();
	// Loads data from or.properties file
	Properties allObjects = object.getUIObjectRepository();
	// Create instance of driver logic
	UIOperation operation = new UIOperation(driver);

	// Call perform function to perform operation on interface
	// Verify Expected result against Actual using Hamcrest assertion method
	assertThat(operation.perform(allObjects, keyword, objectName, objectType, value), equalTo(value));

    }

    public void runLogger(Method method, Object[] parameters) {
	// Enable steps debugging, prints parameters out
	if (System.getProperty("logger").toUpperCase().contains("ENABLED")) {
	    logger.debug("Start test " + method.getName() + " with params " + Arrays.asList(parameters));
	}
    }

    public void runLogger(Method method) {
	// Enable steps debugging, prints parameters out
	if (System.getProperty("logger").toUpperCase().contains("ENABLED")) {
	    logger.debug("Stop test " + method.getName());
	}
    }

    // Take screenshots on exceptions
    public void onException(ITestResult result) throws IOException {
	// check if the taking screenshot functionality was enabled
	if (System.getProperty("screenshot").toUpperCase().contains("ENABLED")
		// and check if and exception has occurred
		&& ITestResult.FAILURE == result.getStatus()) {
	    // generate a file with a screenshot
	    File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    // format the file and save it in the project dir
	    FileUtils.copyFile(scrFile,
		    new File(result.getName() + "--" + Arrays.toString(result.getParameters()) + ".jpg"));
	}

    }

}
