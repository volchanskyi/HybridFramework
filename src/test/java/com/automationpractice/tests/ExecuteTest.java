package com.automationpractice.tests;

import org.testng.annotations.Test;

public class ExecuteTest extends TestBase {


    @Test(dataProvider = "UIData", dataProviderClass = DataProviders.class)
    public void uiTest(String testcaseName, String keyword, String objectName, String objectType, String value)
	    throws Exception {
	init(testcaseName);
	verify(keyword, objectName, objectType, value);
    }

}
