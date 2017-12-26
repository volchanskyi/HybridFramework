package core.operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadObject {

    Properties p = new Properties();

    public Properties getUIObjectRepository() throws IOException {
	// Read object repository file
	InputStream stream = new FileInputStream(
		new File(System.getProperty("user.dir") + "\\src\\test\\java\\core\\objects\\object.properties"));
	// load all objects
	p.load(stream);
	return p;
    }

    public String getUIObjectProperty(String value) {
	
	return p.getProperty(value);
    }

}
