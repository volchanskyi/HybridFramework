package core.testCases;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import core.excelExportAndFileIO.ReadGuru99ExcelFile;
import core.operation.ReadObject;
import core.operation.UIOperation;

public class HybridExecuteTest extends TestBase{
   


    @Test(dataProvider = "hybridData")
    public void uiTest(String testcaseName, String keyword, String objectName, String objectType, String value)
	    throws Exception {
	// TODO Auto-generated method stub

	if (testcaseName != null && testcaseName.length() != 0) {
	    // webdriver = new ChromeDriver();
	    // ------------------
	    Logger logger = Logger.getLogger("");
	    logger.setLevel(Level.OFF);
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
	    webdriver = new ChromeDriver(option);
	    // PageFactory.initElements(driver, ObjectStorage.class);
	    webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    // ------------------
	}
	ReadObject object = new ReadObject();
	Properties allObjects = object.getObjectRepository();
	UIOperation operation = new UIOperation(webdriver);
	// Call perform function to perform operation on UI
	operation.perform(allObjects, keyword, objectName, objectType, value);

    }


    @DataProvider(name = "hybridData")
    public Object[][] getDataFromDataprovider() throws IOException {
	Object[][] object = null;
	ReadGuru99ExcelFile file = new ReadGuru99ExcelFile();

	// Read keyword sheet
	Sheet guru99Sheet = file.readExcel(System.getProperty("user.dir") + "\\", "TestCase.xlsx", "KeywordFramework");
	// Find number of rows in excel file
	int rowCount = guru99Sheet.getLastRowNum() - guru99Sheet.getFirstRowNum();
	object = new Object[rowCount][5];
	for (int i = 0; i < rowCount; i++) {
	    // Loop over all the rows
	    Row row = guru99Sheet.getRow(i + 1);
	    // Create a loop to print cell values in a row
	    for (int j = 0; j < row.getLastCellNum(); j++) {
		// Print excel data in console
		object[i][j] = row.getCell(j).toString();
	    }

	}
	System.out.println("");
	return object;
    }

}
