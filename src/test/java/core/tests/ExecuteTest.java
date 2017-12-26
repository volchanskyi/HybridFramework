package core.tests;

import org.testng.annotations.Test;
import core.tests.DataProviders;

public class ExecuteTest extends TestBase {
    // WebDriver driver;
    //TODO add Test Case names
    @Test(dataProvider = "UIData", dataProviderClass = DataProviders.class)
    public void uiTest(String testcaseName, String keyword, String objectName, String objectType, String value)
	    throws Exception {
	ivRun(testcaseName);
	ivRead(keyword, objectName, objectType, value);
	
    }
}
