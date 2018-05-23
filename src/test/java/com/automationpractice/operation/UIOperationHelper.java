package com.automationpractice.operation;

import java.util.List;
import java.util.Properties;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class UIOperationHelper extends LocatorReader {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    private long currentTime = System.currentTimeMillis();

    // returns a list of all web elements on the page
    protected List<WebElement> findWebElements(Properties p, String objectName, String objectType) {
	return driver.findElements(this.getObject(p, objectName, objectType));
    }

    // returns the first element from the list of web elements
    protected WebElement findTheFirstWebElement(Properties p, String objectName, String objectType) {
	return findWebElements(p, objectName, objectType).get(0);
    }

    // takes the String from the value param and adds current time to it.
    // sends mutable text
    // returns mutable text
    protected String sendUniqueKeys(Properties p, String objectName, String objectType, String value) {
	findTheFirstWebElement(p, objectName, objectType).sendKeys(this.currentTime + value);
	return this.currentTime + value;
    }

    // An expectation for checking that an element is present on the DOM of a page
    protected WebElement waitForPresenceOfElement(Properties p, String objectName, String objectType) {
	return wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p, objectName, objectType)));
    }

    // An expectation for checking that an element is present on the DOM of a page
    // and visible.
    // Visibility means that the element is not only displayed but also has a height
    // and width that is greater than 0.
    protected WebElement waitForVisabilityOfElement(Properties p, String objectName, String objectType) {
	return wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p, objectName, objectType)));
    }

    //An expectation for checking that an element is either invisible or not present on the DOM.
    protected Boolean waitForDisappearanceOfElement(Properties p, String objectName, String objectType) {
	return wait.until(ExpectedConditions.invisibilityOfElementLocated(this.getObject(p, objectName, objectType)));
    }

    // returns the first element from the list of web elements
    //simulates typing into an element with value as a String
    protected void sendKeys(Properties p, String objectName, String objectType, String value) {
	findTheFirstWebElement(p, objectName, objectType).sendKeys(value);
    }

    //returns the first element from the list of web elements
    //Get the value of the given attribute of the element. 
    //Will return the current value, even if this has been modified 
    //after the page has been loaded. 
    protected String getJavaScriptObjectValue(Properties p, String objectName, String objectType) {
	return findTheFirstWebElement(p, objectName, objectType).getAttribute("value");
    }

    
    //modifies css of a web element (for debugging purposes)
    protected void visualDebug(WebElement element) {
	// Read system variable property from the POM
	if (System.getProperty("debug").toUpperCase().contains("ON")) {
	    // Create instance of Javascript executor
	    this.js = (JavascriptExecutor) driver;
	    // Style element with a red border
	    this.js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style",
		    "border: 2px solid red; border-style: dashed;");
	}
    }

    //Quits this driver, closing every associated window.
    protected void quit() {
	driver.quit();
    }

}
