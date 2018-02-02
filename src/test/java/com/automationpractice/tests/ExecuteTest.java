package com.automationpractice.tests;

import org.testng.annotations.Test;

public class ExecuteTest extends TestBase {

    // TODO add Test Case names
    @Override
    @Test(dataProvider = "UIData", dataProviderClass = DataProviders.class)
    public void uiTest(String testcaseName, String keyword, String objectName, String objectType, String value)
	    throws Exception {
	ivRun(testcaseName);
	ivRead(keyword, objectName, objectType, value);

    }

}
