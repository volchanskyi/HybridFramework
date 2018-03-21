package com.automationpractice.operation;

import java.util.List;
import java.util.Properties;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Objects;

public class UIOperation {

    private WebDriver driver;
    private WebDriverWait wait;
    protected String title;
    private JavascriptExecutor js;
    final ApplicationManager APP = new ApplicationManager(driver);

    UIOperation(WebDriver driver) {
	this.driver = driver;
	wait = new WebDriverWait(driver, 10);

	// ---------------------------------

    }

    // UI interaction logic implemantation
    String perform(Properties p, String keyword, String objectName, String objectType, String value)
	    throws IllegalArgumentException {
	switch (keyword.toUpperCase()) {
	// Perform click on element
	case "CLICK":
	    WebElement elem = wait
		    .until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p, objectName, objectType)));
	    // Enable/Disable visual debug options(Highlighting web elements, visual verbose
	    // mode, etc.)
	    visualDebug(elem);
	    Actions builderClick = new Actions(driver);
	    builderClick.moveToElement(driver.findElements(this.getObject(p, objectName, objectType)).get(0))
		    .click(driver.findElements(this.getObject(p, objectName, objectType)).get(0)).build().perform();
	    break;

	// Perform select of item
	case "SELECTITEM":
	    WebElement item = wait
		    .until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p, objectName, objectType)));
	    // Enable/Disable visual debug options(Highlighting web elements, visual verbose
	    // mode, etc.)
	    visualDebug(item);
	    // Load items, find the one we`re looking for and apply click on it
	    List<WebElement> allElements = driver.findElements(this.getObject(p, objectName, objectType));
	    for (WebElement element : allElements) {
		String it = element.getText();
		if (Objects.equal(it, value)) {
		    Actions builder = new Actions(driver);
		    builder.moveToElement(element).click(element).build().perform();
		    return it;
		}

	    }
	    return "No such item";

	case "SCROLLINTOVIEW":
	    // Wait for the modal to appear
	    wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p, objectName, objectType)));
	    // Create instance of Javascript executor
	    js = (JavascriptExecutor) driver;
	    // ** now execute query which actually will scroll until that element
	    // * is
	    // * not appeared on page.
	    js.executeScript("arguments[0].scrollIntoView(true);",
		    driver.findElements(this.getObject(p, objectName, objectType)).get(0));
	    Actions builderView = new Actions(driver);
	    builderView.moveToElement(driver.findElements(this.getObject(p, objectName, objectType)).get(0)).build()
		    .perform();
	    break;

	case "SETTEXT":
	    // Put text into textfield
	    WebElement field = wait
		    .until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p, objectName, objectType)));
	    // Enable/Disable visual debug options(Highlighting web elements, visual verbose
	    // mode, etc.)
	    visualDebug(field);
	    driver.findElements(this.getObject(p, objectName, objectType)).get(0).sendKeys(value);
	    // Return the text that was inserted for verification
	    return driver.findElements(this.getObject(p, objectName, objectType)).get(0).getAttribute("value");

	case "GOTOURL":
	    // Get url of application
	    driver.get(value);
	    // Handle "Page not found" and writing errors in Expected results
	    if (driver.getTitle().contains("404")
		    || driver.getPageSource().contains("404") & driver.getTitle().contains("found")
		    || driver.getPageSource().contains("not found")) {
		return "404 Not Found";
	    }
	    // Return actual URI for TestNG to assert
	    return driver.getCurrentUrl();

	// Match initial title to the current one
	case "VERIFYTITLE":
	    wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p, objectName, objectType)));
	    // Return actual title for TestNG to assert
	    return driver.getTitle();

	// Compare application item with the item from TC
	case "VERIFYITEMS":
	    // Wait for the webElement to appear
	    wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p, objectName, objectType)));
	    List<WebElement> allItemElements = driver.findElements(this.getObject(p, objectName, objectType));
	    for (WebElement element : allItemElements) {
		String it = element.getText();
		if (Objects.equal(it, value)) {
		    return it;
		}

	    }
	    return "No such item";

	// Check if the Confirmation Alert is present and close it
	case "CLOSEALERT":
	    // Wait for the webElement to appear
	    wait.until(ExpectedConditions.alertIsPresent());
	    // Navigate to alert
	    Alert alert = driver.switchTo().alert();
	    // Get alert message
	    String alertMessage = alert.getText();
	    // Accept alert message
	    alert.accept();
	    // Check if the alert window is gone
	    wait.until(ExpectedConditions.not(ExpectedConditions.alertIsPresent()));
	    // return alert message for assertion
	    return alertMessage;

	// Close modal windows
	case "CLOSEMODAL":
	    // Wait for the modal to appear
	    wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p, objectName, objectType)));
	    // Close modal
	    js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].click();",
		    driver.findElements(this.getObject(p, objectName, objectType)).get(0));
	    Actions builderModal = new Actions(driver);
	    builderModal.moveToElement(driver.findElements(this.getObject(p, objectName, objectType)).get(0)).click()
		    .build().perform();
	    break;

	// Quit Active Driver!
	case "CLOSEBROWSER":
	    // TODO Fix TC name assignment
	    APP.setTestName("");
	    // -------------------------
	    quit();
	    break;

	default:
	    break;
	}

	return value;
    }

    private void visualDebug(WebElement element) {
	//Read system variable property from the POM
	if (System.getProperty("debug").toUpperCase().contains("ON")) {
	    // Create instance of Javascript executor
	    this.js = (JavascriptExecutor) driver;
	    // Style element with a red border
	    this.js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style",
		    "border: 2px solid red; border-style: dashed;");
	}
    }

    // Click on the first element in the array of web elements
    protected void clickElem(By param) throws IllegalArgumentException {

    }

    // Send keys in the first element in the array of web elements
    protected void sendKeys(By param, String value) throws IllegalArgumentException {

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
    private void quit() {
	driver.quit();
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
	// find by attribute
	else if (objectType.equalsIgnoreCase("TAGNAME")) {

	    return By.tagName(p.getProperty(objectName));

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
