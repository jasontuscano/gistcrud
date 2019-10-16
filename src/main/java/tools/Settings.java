package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Settings {

	public static String getProperty(String key) {
		String value = new String();
		try {
			File file = new File("src/main/resources/Settings.properties");
			InputStream in = new FileInputStream(file);
			Properties props = new Properties();
			props.load(in);
			value = props.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}
