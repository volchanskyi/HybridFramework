package com.automationpractice.tests;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.automationpractice.interfaceBridge.TCreader;

public class DataProviders {

    // public DataProviders() {
    // super();
    // }
//    TODO print only NOT empty strings
    // TODO create API DataProvvider
    @DataProvider(name = "UIData")
    public Object[][] getDataFromDataprovider() throws IOException {
	Object[][] object = null;
	TCreader file = new TCreader();

	// Read keyword sheet
	Sheet ivSheet = file.readExcel(System.getProperty("user.dir") + "\\", "TestCase.xlsx", "UItests");
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
//		System.out.println(row.getCell(j).toString());
	    }

	}
	// System.out.println("");
	return object;
    }

}