package com.automationpractice.operation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class ApplicationManager {

    private WebDriver driver;
    private String testName = "";
    private String driverPath = "";

    public ApplicationManager(WebDriver driver) {
	this.driver = driver;
    }

    public String getTestName() {
	return this.testName;
    }

    public void setTestName(String param) {
	// TODO add conditions
	this.testName = param;

    }

    // Initialization of AppManager
    public void init(String testcaseName) throws IOException {
	// Check for empty strings in TC
	if (!(testName.regionMatches(true, 0, testcaseName, 0, 10))) {
	    if (testcaseName != null && testcaseName.length() != 0) {
		// Pass browser property and TC name to the method
		launchBrowser();
		// Rewrite TC name
		setTestName(testcaseName);
	    }
	} else
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
		option.addArguments("-window-size=1200x600");
	    } else if (isPlatform("WINDOWS")) {
		this.driverPath = "./src/test/resources/webdrivers/pc/chromedriver.exe";
		option.addArguments("--headless");
		// option.addArguments("--disable-gpu");
		option.addArguments("--window-size=1200x600");
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
	    // -------------------
	}
    }

    private boolean isPlatform(String platform) {
	return System.getProperty("os.name").toUpperCase().contains(platform);
    }

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
	// Verify using Hamcrest
	assertThat(operation.perform(allObjects, keyword, objectName, objectType, value), equalTo(value));

    }
    
    
    /**

     * This function will allow us to take screenshots

     * @param webdriver

     * @param fileWithPath

     * @throws Exception

     */

    public static void takeSnapShot(WebDriver webdriver,String fileWithPath) throws Exception{

        //Convert web driver object to TakeScreenshot

        TakesScreenshot scrShot =((TakesScreenshot)webdriver);

        //Call getScreenshotAs method to create image file

                File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);

            //Move image file to new destination

                File DestFile=new File(fileWithPath);

                //Copy file at destination

                FileUtils.copyFile(SrcFile, DestFile);

            

    }
    

}
