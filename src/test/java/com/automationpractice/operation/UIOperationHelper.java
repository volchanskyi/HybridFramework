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

    protected List<WebElement> findWebElements(Properties p, String objectName, String objectType) {
        return driver.findElements(this.getObject(p, objectName, objectType));
    }

    protected WebElement findTheFirstWebElement(Properties p, String objectName, String objectType) {
        return findWebElements(p, objectName, objectType).get(0);
    }

    protected String sendUniqueKeys(Properties p, String objectName, String objectType, String value) {
        findTheFirstWebElement(p, objectName, objectType).sendKeys(this.currentTime + value);
        return this.currentTime + value;
    }

    protected WebElement waitForPresenceOfElement(Properties p, String objectName, String objectType) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p, objectName, objectType)));
    }

    protected WebElement waitForVisabilityOfElement(Properties p, String objectName, String objectType) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p, objectName, objectType)));
    }

    private Boolean waitForDisappearanceOfElement(Properties p, String objectName, String objectType) {
    return wait.until(ExpectedConditions.invisibilityOfElementLocated(this.getObject(p, objectName, objectType)));
    }

    protected void sendKeys(Properties p, String objectName, String objectType, String value) {
        findTheFirstWebElement(p, objectName, objectType).sendKeys(value);
    }

    protected String getJavaScriptObjectValue(Properties p, String objectName, String objectType) {
        return findTheFirstWebElement(p, objectName, objectType).getAttribute("value");
    }

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

    protected void quit() {
        driver.quit();
    }

    
    
    
    
    
}
