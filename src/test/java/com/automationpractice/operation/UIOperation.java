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

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String title;

    public UIOperation(WebDriver driver) {
	this.driver = driver;
	wait = new WebDriverWait(driver, 10);
	
	// ---------------------------------

    }

    public String perform(Properties p, String keyword, String objectName, String objectType, String value)
	    throws IllegalArgumentException {
	switch (keyword.toUpperCase()) {
	case "CLICK":
	    // Perform click
	    wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p, objectName, objectType)));
	    Actions builderClick = new Actions(driver);
	    
//	    builderClick.moveToElement(driver.findElements(this.getObject(p, objectName, objectType)).get(0)).build().perform();
	    builderClick.moveToElement(driver.findElements(this.getObject(p, objectName, objectType)).get(0))
		    .click(driver.findElements(this.getObject(p, objectName, objectType)).get(0)).build().perform();

	    break;

	case "SELECTITEM":
	    // Perform select of item
	    wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p, objectName, objectType)));
	    List<WebElement> allElements = driver.findElements(this.getObject(p, objectName, objectType));
	    for (WebElement element : allElements) {
		String it = element.getText();
		if (Objects.equal(it, value)) {
		    Actions builder = new Actions(driver);
//		    builder.moveToElement(element).build()
//			    .perform();
		    builder.moveToElement(element).click(element).build().perform();
		    return it;
		}

	    }
	    return "No such item";

	case "NGCLICK":
	    // Wait for the modal to appear
	    wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p, objectName, objectType)));
	    // Click on the element
	    // Use JS to perform click on ng-elems
	    JavascriptExecutor executor = (JavascriptExecutor) driver;
	    executor.executeScript("arguments[0].click();",
		    driver.findElements(this.getObject(p, objectName, objectType)).get(0));
	    Actions builderNgClick = new Actions(driver);
	    builderNgClick.moveToElement(driver.findElements(this.getObject(p, objectName, objectType)).get(0))
		    .click(driver.findElements(this.getObject(p, objectName, objectType)).get(0)).build().perform();

	    break;

	case "SCROLLINTOVIEW":
	    // Wait for the modal to appear
	    wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p, objectName, objectType)));
	    // Create instance of Javascript executor
	    JavascriptExecutor siv = (JavascriptExecutor) driver;
	    // now execute query which actually will scroll until that element
	    // is
	    // not appeared on page.
	    siv.executeScript("arguments[0].scrollIntoView(true);",
		    driver.findElements(this.getObject(p, objectName, objectType)).get(0));
	    Actions builderView = new Actions(driver);
	    builderView.moveToElement(driver.findElements(this.getObject(p, objectName, objectType)).get(0)).build()
		    .perform();
	    break;

	case "SETTEXT":
	    // Set text on control
	    wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p, objectName, objectType)));
	    driver.findElements(this.getObject(p, objectName, objectType)).get(0).sendKeys(value);
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
	    return driver.getCurrentUrl();

	case "VERIFYTITLE":
	    // Match initial title to the current one
	    wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p, objectName, objectType)));

	    return driver.getTitle();

	case "VERIFYITEMS":
	    // Match items
	    wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p, objectName, objectType)));
	    // String item = null;
	    List<WebElement> allItemElements = driver.findElements(this.getObject(p, objectName, objectType));
	    for (WebElement element : allItemElements) {
		String it = element.getText();
		if (Objects.equal(it, value)) {
		    return it;
		}

	    }
	    return "No such item";

	case "FINDITEMS":
	    // Match items
	    wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p, objectName, objectType)));
	    List<WebElement> allElems = driver.findElements(this.getObject(p, objectName, objectType));
	    for (WebElement element : allElems) {
		String it = element.getText();
		if (Objects.equal(it, value)) {
		    return it;
		}

	    }
	    return "No such item";

	case "CLOSEALERT":
	    // Check if the Confirmation Alert is present and close it
	    wait.until(ExpectedConditions.alertIsPresent());
	    Alert alert = driver.switchTo().alert();
	    String alertMessage = alert.getText();
	    alert.accept();
	    wait.until(ExpectedConditions.not(ExpectedConditions.alertIsPresent()));
	    return alertMessage;

	case "CLOSEMODAL":
	    // Wait for the modal to appear
	    wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p, objectName, objectType)));
	    // Close modal
	    JavascriptExecutor closeModal = (JavascriptExecutor) driver;
	    closeModal.executeScript("arguments[0].click();",
		    driver.findElements(this.getObject(p, objectName, objectType)).get(0));
	    Actions builderModal = new Actions(driver);
	    builderModal.moveToElement(driver.findElements(this.getObject(p, objectName, objectType)).get(0)).click()
		    .build().perform();

	    break;

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
    protected void quit() {
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
