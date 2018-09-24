package game.infrpg.server.util;

import game.infrpg.Globals;
import lib.logger.Logger;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ServerProperties {

	private static final String PROPERTY_FILE_COMMENT = "Infrpg server properties";

	private final Properties prop;
	private final File file;
	private final ILogger logger;

	public ServerProperties(File file) throws IOException {
		this.logger = Globals.logger();
		
		if (file.isDirectory()) {
			throw new IOException("The specified server properties file is denoting a directory.");
		}

		Properties defaults = createDefaults();
		this.prop = new Properties(defaults);
		this.file = file;

		if (file.exists()) {
			logger.debug("Reading properties file " + file.getAbsolutePath());
			readFromFile(file, prop);
		}
		else {
			logger.debug("Writing default properties to " + file.getAbsolutePath());
			writeToFile(file, defaults, PROPERTY_FILE_COMMENT);
		}
	}

	public String getProperty(ServerProperty property) {
		return prop.getProperty(property.key());
	}

	@Override
	public String toString() {
		return "Server properties " + prop.toString();
	}
	
	
	public static void readFromFile(File file, Properties properties) throws IOException {
		FileReader fr = new FileReader(file);
		try (BufferedReader br = new BufferedReader(fr)) {
			properties.load(br);
		}
	}

	public static void writeToFile(File file, Properties properties, String comments) throws IOException {
		FileWriter fwr = new FileWriter(file);
		try (BufferedWriter bw = new BufferedWriter(fwr)) {
			properties.store(bw, comments);
		}
	}

	private static Properties createDefaults() {
		Properties defaults = new Properties();
		Arrays.stream(ServerProperty.values()).forEach(P
				-> defaults.setProperty(P.key(), P.defaultValue));
		return defaults;
	}

}
