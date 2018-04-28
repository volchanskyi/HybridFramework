package com.automationpractice.tests;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.automationpractice.interfaceBridge.TCreader;

public class DataProviders {

    // TODO print only NOT empty strings
    // GUI tests Data Provider
    @DataProvider(name = "UIData")
    public Object[][] getDataFromUiDataProvider() throws IOException {
	Object[][] object = null;
	TCreader file = new TCreader();

	// Read keyword sheet
	// Sheet ivSheet = file.readExcel(System.getProperty("user.dir") + "\\",
	// "test.xlsx", "UItests");
	if (isTestCase("TESTCASE.XLSX")) {
	    Sheet ivSheet = file.readExcel(System.getProperty("user.dir") + "\\", "TestCase.xlsx", "UItests");
	    object = readExcel(ivSheet);
	} else {
	    Sheet ivSheet = file.readExcel(System.getProperty("user.dir") + "\\", "test.xlsx", "UItests");
	    object = readExcel(ivSheet);
	}
	return object;
    }

    private Object[][] readExcel(Sheet ivSheet) {
	Object[][] object;
	// Find number of rows in excel file
	int rowCount = ivSheet.getLastRowNum() - ivSheet.getFirstRowNum();
	object = new Object[rowCount][5];
	for (int i = 0; i < rowCount; i++) {
	    // Loop over all the rows
	    Row row = ivSheet.getRow(i + 1);
	    // Create a loop to print cell values in a row
	    for (int j = 0; j < row.getLastCellNum(); j++) {
		// Print excel data in console
		object[i][j] = row.getCell(j).toString();
	    }
	}
	return object;
    }

    private boolean isTestCase(String testCase) {
	return System.getProperty("testcase").toUpperCase().contains(testCase);
    }

}