package com.automationpractice.operation;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Objects;

public class UIOperation extends LocatorReader {

    private WebDriver driver;
    private WebDriverWait wait;
    protected String title;
    private JavascriptExecutor js;
    protected boolean booleanValue;
    protected int index;
    private long currentTime = System.currentTimeMillis();
    final ApplicationManager APP = new ApplicationManager(driver);
    private final List<String> regexList = new ArrayList<>();

    UIOperation(WebDriver driver) {
	this.driver = driver;
	wait = new WebDriverWait(driver, 10);
	// Set timeout for Async Java Script
	driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
    }

    // UI interaction logic implemantation
    String perform(Properties p, String keyword, String objectName, String objectType, String value)
	    throws IllegalArgumentException, InterruptedException {
	switch (keyword.toUpperCase()) {
	// Perform click on element
	case "CLICK":
	    WebElement elem = waitForVisabilityOfElement(p, objectName, objectType);
	    // Enable/Disable visual debug options(Highlighting web elements, visual verbose
	    // mode, etc.)
	    visualDebug(elem);
	    Actions builderClick = new Actions(driver);
	    builderClick.moveToElement(findTheFirstWebElement(p, objectName, objectType))
		    .click(findTheFirstWebElement(p, objectName, objectType)).build().perform();
	    break;

	// Perform select of an item on the web page
	case "SELECPAGETITEMFROM":
	    WebElement item = waitForVisabilityOfElement(p, objectName, objectType);
	    // Enable/Disable visual debug options(Highlighting web elements, visual verbose
	    // mode, etc.)
	    visualDebug(item);
	    // Load page items, find the one we`re looking for and apply click on it
	    List<WebElement> allElements = findWebElements(p, objectName, objectType);
	    for (WebElement element : allElements) {
		String it = element.getText();
		if (Objects.equal(it, value)) {
		    Actions builder = new Actions(driver);
		    builder.moveToElement(element).click(element).build().perform();
		    return it;
		}

	    }
	    return "No such item";

	case "CHOOSEFROM":
	    WebElement ddMenu = waitForPresenceOfElement(p, objectName, objectType);
	    // Enable/Disable visual debug options(Highlighting web elements, visual verbose
	    // mode, etc.)
	    visualDebug(ddMenu);
	    Select choose = new Select(findTheFirstWebElement(p, objectName, objectType));
	    // Selecting value
	    choose.selectByVisibleText(value);
	    // Necessary SLEEP to give time for sorting script to finish
	    // if we don`t wait here DOM refreshes
	    Thread.sleep(2000);
	    String chosen = choose.getFirstSelectedOption().getText();
	    if (chosen.equals(value)) {
		// Return the text that was inserted for verification
		return chosen;
	    } else
		return "The value hasn`t been selected been selected";

	case "VERIFYLOWESTPRICEFIRST":
	    WebElement pageItem = waitForVisabilityOfElement(p, objectName, objectType);
	    // Enable/Disable visual debug options(Highlighting web elements, visual verbose
	    // mode, etc.)
	    visualDebug(pageItem);

	    // get the number of items that are required
	    int itemSize = findWebElements(p, objectName, objectType).size();
	    // DOM refreshes here
	    // Work with each WebElement individually, rather than with a list (Handling
	    // StaleElementException)
	    for (int i = 0; i <= itemSize - 1; i++) {
		WebElement element = findWebElements(p, objectName, objectType).get(i);
		// Init string to parse with regex
		String regexString = element.getAttribute("innerText");
		// Generate regex to catch the string
		String regex = "^(\\$)(\\d{1,}\\.\\d{1,2})(.+)?$";
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(regexString);
		// add result to the List of strings
		matcher.find();
		this.regexList.add(matcher.group(2));
	    }
	    // Use BigDecimal for better precision
	    BigDecimal firstValue = new BigDecimal(this.regexList.get(0));
	    BigDecimal secondValue = new BigDecimal(this.regexList.get(1));
	    BigDecimal lastValue = new BigDecimal(this.regexList.get(this.regexList.size() - 1));

	    // Verify if the first item has the best price in the List
	    if (firstValue.compareTo(secondValue) <= 0 && firstValue.compareTo(lastValue) <= 0) {
		break;
	    } else
		return "Sorting failure. First item doesn`t have the best price on the page";

	    // TEMPORARY NOT WORKING 4.7.2018
	case "SORTBY":
	    // wait for presense of the element in the DOM(passing option from the list)
	    waitForPresenceOfElement(p, objectName, objectType);
	    // Create instance of Javascript executor
	    js = (JavascriptExecutor) driver;
	    // Use
	    js.executeAsyncScript(
		    "var select = arguments[0]; for(var i = 0; i < select.options.length; i++) { if(select.options[i].text == arguments[1]) { select.options[i].selected = true; } }",
		    findTheFirstWebElement(p, objectName, objectType), value);
	    break;

	case "CLICKRADIOBUTTON":
	    waitForPresenceOfElement(p, objectName, objectType);
	    List<WebElement> oRadioButton = findWebElements(p, objectName, objectType);
	    // Create a boolean variable which will hold the value (True/False)
	    // This statement will return True, if first Radio button is selected
	    this.booleanValue = oRadioButton.get(0).isSelected();
	    // This will check that if the bValue is True means if the first radio button is
	    // selected
	    if (this.booleanValue = false) {
		// This will select second radio button, if the first radio button is selected
		// by default
		oRadioButton.get(1).click();
		// Check if the button was clicked
		if (oRadioButton.get(1).isSelected()) {
		    break;
		}
		return "The radio button hasn`t been clicked";
	    } else if (this.booleanValue = true) {
		// If the first radio button is not selected by default, it will be
		// selected
		oRadioButton.get(0).click();
		// Check if the button was clicked
		if (oRadioButton.get(0).isSelected()) {
		    break;
		}
		return "The radio button hasn`t been clicked";
	    } else
		return "Couldn`t read radiobutton`s value";

	case "SCROLLINTOVIEW":
	    // Wait for the element to appear
	    waitForVisabilityOfElement(p, objectName, objectType);
	    // Create instance of Javascript executor
	    js = (JavascriptExecutor) driver;
	    // ** now execute query which actually will scroll until that element
	    // * is
	    // * not appeared on page.
	    js.executeScript("arguments[0].scrollIntoView(true);", findTheFirstWebElement(p, objectName, objectType));
	    Actions builderView = new Actions(driver);
	    builderView.moveToElement(findTheFirstWebElement(p, objectName, objectType)).build().perform();
	    break;

	case "SETTEXTIN":
	    // Put text into textfield
	    WebElement field = waitForVisabilityOfElement(p, objectName, objectType);
	    // Enable/Disable visual debug options(Highlighting web elements, visual verbose
	    // mode, etc.)
	    visualDebug(field);
	    sendKeys(p, objectName, objectType, value);
	    // Return the text that was inserted for verification
	    return getJavaScriptObjectValue(p, objectName, objectType);

	case "SETUNIQUETEXTIN":
	    // Put text into textfield
	    WebElement uniqueField = waitForVisabilityOfElement(p, objectName, objectType);
	    // Enable/Disable visual debug options(Highlighting web elements, visual verbose
	    // mode, etc.)
	    visualDebug(uniqueField);
	    String uniqueValue = sendUniqueKeys(p, objectName, objectType, value);
	    // Return the text that was inserted for verification
	    if (!uniqueValue.equals(getJavaScriptObjectValue(p, objectName, objectType))) {
		return "The text hasn`t been entered";
	    } else
		break;

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
	case "VERIFYPAGETITLE":
	    waitForPresenceOfElement(p, objectName, objectType);
	    // Return actual title for TestNG to assert
	    return driver.getTitle();

	// Compare application item with the item from TC
	case "VERIFYITEMS":
	    // Wait for the webElement to appear
	    waitForPresenceOfElement(p, objectName, objectType);
	    List<WebElement> allItemElements = findWebElements(p, objectName, objectType);
	    for (WebElement element : allItemElements) {
		String it = element.getText();
		if (Objects.equal(it, value)) {
		    return it;
		}

	    }
	    return "No such item";

	// Check if the image size is > 0
	// Verify if slider navigation works (waits for every image in the list to
	// appear UNTIL wait time is out)
	case "VERIFYSLIDER":
	    // Wait for the webElement to appear
	    waitForVisabilityOfElement(p, objectName, objectType);
	    List<WebElement> listOfElems = findWebElements(p, objectName, objectType);
	    // initialize counter
	    this.index = 0;
	    // iterate over array of WebElements
	    for (WebElement nextElem : listOfElems) {
		// wait until the image appears (error otherwise)
		wait.until(ExpectedConditions.visibilityOf(nextElem));
		visualDebug(nextElem);
		// update counter
		this.index++;
		// close loop when the last image is shown
		if (index == listOfElems.size()) {
		    // stub
		    return "";
		}
	    }
	    break;

	// Find broken links on the page
	case "VERIFYBROKENLINKS":
	    // Wait for URI links in the DOM
	    waitForPresenceOfElement(p, objectName, objectType);
	    HttpURLConnection httpURLConnection = null;
	    int respCode = 200;
	    String url = driver.getCurrentUrl();
	    // Harvest web links on the page
	    List<WebElement> links = findWebElements(p, objectName, objectType);
	    // initialize iterator
	    Iterator<WebElement> it = links.iterator();

	    // check if URL belongs to Third party domain or whether URL is empty/null.
	    while (it.hasNext()) {
		// init URI
		url = it.next().getAttribute("href");
		// skip EMPTY or NULL
		if (url == null || url.isEmpty()) {
		    // URL is either not configured for anchor tag or it is empty
		    continue;
		}

		// Skip links to another domain
		if (!url.startsWith(url)) {
		    // URL belongs to another domain, skipping it.
		    continue;
		}

		try {
		    httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
		    // request HTTP HEADER (only headers are returned and not document body)
		    httpURLConnection.setRequestMethod("HEAD");
		    // init connection
		    httpURLConnection.connect();
		    // Validating Links
		    // retreive response code (200, 300, 404, 500, etc.)
		    respCode = httpURLConnection.getResponseCode();

		    if (respCode >= 400) {
			return url + " is a broken link";
		    } else {
			return "";
		    }

		} catch (MalformedURLException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    break;

	// Find broken images on the page
	case "VERIFYBROKENIMAGES":
	    // Wait for src of images in the DOM
	    waitForPresenceOfElement(p, objectName, objectType);
	    // Create a List for broken images entries
	    List<String> brokenImages = new ArrayList<String>();
	    // list for all image webelems on the page
	    List<WebElement> images = findWebElements(p, objectName, objectType);
	    // init java script executor
	    js = (JavascriptExecutor) driver;
	    // Iterate over list of images on the page
	    for (int image = 0; image < images.size(); image++) {
		// Use JavaScript
		// looking for src tags with no data(no links, broken links, etc.)
		Object result = js.executeScript("return arguments[0].complete &&"
			+ "typeof arguments[0].naturalWidth != \"undefined\" &&" + "arguments[0].naturalWidth > 0",
			images.get(image));
		// If "result" object returns FALSE get src link of the image
		// and add it to the List for broken images entries
		if (result == Boolean.FALSE) {
		    // Enable/Disable visual debug options(Highlighting web elements, visual verbose
		    // mode, etc.)
		    visualDebug(images.get(image));
		    brokenImages.add(images.get(image).getAttribute("src"));
		    // return the broken image src link as String
		    return brokenImages.get(0);
		}
	    }
	    break;

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
	    waitForPresenceOfElement(p, objectName, objectType);
	    // Close modal
	    js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].click();", findTheFirstWebElement(p, objectName, objectType));
	    Actions builderModal = new Actions(driver);
	    builderModal.moveToElement(findTheFirstWebElement(p, objectName, objectType)).click().build().perform();
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

    private List<WebElement> findWebElements(Properties p, String objectName, String objectType) {
	return driver.findElements(this.getObject(p, objectName, objectType));
    }

    private WebElement findTheFirstWebElement(Properties p, String objectName, String objectType) {
	return findWebElements(p, objectName, objectType).get(0);
    }

    private String sendUniqueKeys(Properties p, String objectName, String objectType, String value) {
	findTheFirstWebElement(p, objectName, objectType).sendKeys(this.currentTime + value);
	return this.currentTime + value;
    }

    private WebElement waitForPresenceOfElement(Properties p, String objectName, String objectType) {
	return wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p, objectName, objectType)));
    }

    private WebElement waitForVisabilityOfElement(Properties p, String objectName, String objectType) {
	return wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p, objectName, objectType)));
    }

    private void sendKeys(Properties p, String objectName, String objectType, String value) {
	findTheFirstWebElement(p, objectName, objectType).sendKeys(value);
    }

    private String getJavaScriptObjectValue(Properties p, String objectName, String objectType) {
	return findTheFirstWebElement(p, objectName, objectType).getAttribute("value");
    }

    private void visualDebug(WebElement element) {
	// Read system variable property from the POM
	if (System.getProperty("debug").toUpperCase().contains("ON")) {
	    // Create instance of Javascript executor
	    this.js = (JavascriptExecutor) driver;
	    // Style element with a red border
	    this.js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style",
		    "border: 2px solid red; border-style: dashed;");
	}
    }

    // Quit driver
    private void quit() {
	driver.quit();
    }

}
