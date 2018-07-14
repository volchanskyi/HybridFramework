package com.automationpractice.tests;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.automationpractice.interfaceBridge.TCreader;

public class DataProviders {

    // GUI tests Data Provider that reads params from an excel file
    @DataProvider(name = "UIData")
    protected Object[][] getDataFromUiDataProvider() throws IOException {
	Object[][] object = null;
	TCreader file = new TCreader();

	// Read keyword sheet
	if (isTestCase("TESTCASE.XLSX")) {
	    if (isType("POSITIVE")) {
		Sheet ivSheet = file.readExcel(System.getProperty("user.dir") + "//", "TestCase.xlsx", "Positive");
		object = readExcel(ivSheet);
	    } else if (isType("ERRORHANDLING")) {
		Sheet ivSheet = file.readExcel(System.getProperty("user.dir") + "//", "TestCase.xlsx", "ErrorHandling");
		object = readExcel(ivSheet);
	    }
	} else {
	    //source file for testing and developing new features purposes
	    Sheet ivSheet = file.readExcel(System.getProperty("user.dir") + "//", "test.xlsx", "ErrorHandling");
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
    //add source file for passing params
    private boolean isTestCase(String testCase) {
	return System.getProperty("testcase").toUpperCase().contains(testCase);
    }
    //choose a tab in the source excel file
    private boolean isType(String tcType) {
	return System.getProperty("tctype").toUpperCase().contains(tcType);
    }

}