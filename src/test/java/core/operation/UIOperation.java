package core.operation;

import static org.testng.Assert.assertEquals;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UIOperation {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String title;

    public UIOperation(WebDriver driver) {
	this.driver = driver;
	wait = new WebDriverWait(driver, 10);
	// ---------------------------------

    }

    public void assertTest(String expected, String actual) {

    }

    public String perform(Properties p, String operation, String objectName, String objectType, String value)
	    throws IllegalArgumentException {
	// System.out.println("");
	switch (operation.toUpperCase()) {
	case "CLICK":
	    // Perform click
	    clickElem(this.getObject(p, objectName, objectType));
	    break;

	case "SETTEXT":
	    // Set text on control
	    wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p, objectName, objectType)));
	    driver.findElements(this.getObject(p, objectName, objectType)).get(0).sendKeys(value);
	    return driver.findElements(this.getObject(p, objectName, objectType)).get(0).getAttribute("value");

	case "GOTOURL":
	    // Get url of application
	    // driver.get(p.getProperty(value));
	    driver.get(value);
	    return driver.getCurrentUrl();

	// case "GETTEXT":
	// // Get text of an element
	// wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p,
	// objectName, objectType)));
	// return getText(this.getObject(p, objectName, objectType));

	// case "GETTITLE":
	// // Get title text of a web page
	// getTitle(this.getObject(p, objectName, objectType));
	// break;

	case "VERIFYTITLE":
	    // Match initial title to the current one
	    wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p, objectName, objectType)));
	    System.out.println(driver.findElements(this.getObject(p, objectName, objectType)).get(0).getText());
	    return driver.getTitle();

	    //TODO add Alert window Verification case
	    
	case "CLOSEBROWSER":
	    // Quit Active Driver!
	    quit();
	    break;

	default:
	    break;
	}
	return value;
    }

    // Click on the first element in the array of web elements
    protected void clickElem(By param) throws IllegalArgumentException {
	wait.until(ExpectedConditions.visibilityOfElementLocated(param));
	driver.findElements(param).get(0).click();
    }

    // Send keys in the first element in the array of web elements
    protected void sendKeys(By param, String value) throws IllegalArgumentException {
	wait.until(ExpectedConditions.visibilityOfElementLocated(param));
	driver.findElements(param).get(0).sendKeys(value);

    }

    // Check if the element is present on the page
    protected boolean isPresent(By param) throws IllegalArgumentException {
	if (driver.findElements(param).size() > 0) {
	    return true;
	} else {
	    // System.out.println("elem is not present");
	    return false;
	}
    }

    // Quit driver
    protected void quit() {
	driver.quit();
    }

    private String getText(By param) {
	return driver.findElements(param).get(0).getText();
    }

    /**
     * Find element BY using object type and value
     * 
     * @param p
     * @param objectName
     * @param objectType
     * @return
     * @throws Exception
     */
    private By getObject(Properties p, String objectName, String objectType) throws IllegalArgumentException {
	// Find by xpath
	if (objectType.equalsIgnoreCase("XPATH")) {

	    return By.xpath(p.getProperty(objectName));
	}
	// find by class
	else if (objectType.equalsIgnoreCase("CLASSNAME")) {

	    return By.className(p.getProperty(objectName));

	}
	// find by name
	else if (objectType.equalsIgnoreCase("NAME")) {

	    return By.name(p.getProperty(objectName));

	}
	// Find by css
	else if (objectType.equalsIgnoreCase("CSS")) {

	    return By.cssSelector(p.getProperty(objectName));

	}
	// find by link
	else if (objectType.equalsIgnoreCase("LINK")) {

	    return By.linkText(p.getProperty(objectName));

	}
	// find by partial link
	else if (objectType.equalsIgnoreCase("PARTIALLINK")) {

	    return By.partialLinkText(p.getProperty(objectName));

	} else {
	    throw new IllegalArgumentException("Wrong object type");
	}
    }
}
