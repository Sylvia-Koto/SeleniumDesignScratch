package SylviaAcademy.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	
	 private static Properties properties;

	 static {
		    try {
		        properties = new Properties();
		        properties.load(ConfigReader.class.getClassLoader().getResourceAsStream("config.properties"));
		    } catch (IOException | NullPointerException e) {
		        e.printStackTrace();
		        throw new RuntimeException("❌ Erreur de chargement du fichier config.properties");
		    }
		}

	    public static String get(String key) {
	        return properties.getProperty(key);
	    }

}
