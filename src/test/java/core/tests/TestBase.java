package core.tests;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import core.operation.ReadObject;
import core.operation.UIOperation;

public class TestBase {
    WebDriver driver;

    protected void ivRun(String testcaseName) {
	if (testcaseName != null && testcaseName.length() != 0) {
	    // driver = new ChromeDriver();
	    // ------------------
	    Logger logger = Logger.getLogger("");
	    logger.setLevel(Level.WARNING);
	    ChromeOptions option = new ChromeOptions();
	    String driverPath = "";
	    if (System.getProperty("os.name").toUpperCase().contains("MAC")) {
		driverPath = "./resources/webdrivers/mac/chromedriver";
		option.addArguments("-start-fullscreen");
	    } else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
		driverPath = "./resources/webdrivers/pc/chromedriver.exe";
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

    // WebDriver driver;
    // public UIOperation app;
    // WebDriver driver = null;

    // --------------------------
    protected void ivRead(String keyword, String objectName, String objectType, String value)
	    throws IOException, Exception {
	ReadObject object = new ReadObject();
	Properties allObjects = object.getUIObjectRepository();
	UIOperation operation = new UIOperation(driver);
	// Call perform function to perform operation on UI
//	operation.perform(allObjects, keyword, objectName, objectType, value);
	 
	//TODO use Hamcrest insted of TestNG assertion
	assertEquals(operation.perform(allObjects, keyword, objectName,
	 objectType, value), value);

    }

    //// @BeforeClass
    //// public void start() throws Exception {
    //// app = new UIOperation(driver);
    //// }
    //

    // @AfterTest
    // public void quit() {
    // webdriver.quit();
    // }

}