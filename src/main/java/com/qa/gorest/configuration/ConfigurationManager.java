package com.qa.gorest.configuration;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class ConfigurationManager {
	
	
	private Properties prop;
	private FileInputStream ip;

	public Properties initProp() {
		prop = new Properties();
		try {
			ip = new FileInputStream("./src/test/resources/config/config.properties"); // ip will make connection established with config.properties
			prop.load(ip);// through ip load all the info to the prop and send it to the basetest
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;

	}

	 

}
