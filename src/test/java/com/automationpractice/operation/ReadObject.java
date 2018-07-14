package com.automationpractice.operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadObject {

    Properties properties = new Properties();

    protected Properties getUIObjectRepository() throws IOException {
	// Read object repository file where we store locators
	InputStream stream = new FileInputStream(new File(
		System.getProperty("user.dir") + "//src//test//java//com//automationpractice//objects//or.properties"));
	// load all objects
	properties.load(stream);
	return properties;
    }

    protected String getUIObjectProperty(String value) {
	//return the value
	return properties.getProperty(value);
    }

}
