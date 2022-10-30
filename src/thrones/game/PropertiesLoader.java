package thrones.game;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * fabricated class that loads all the properties from the properties file
 */
public class PropertiesLoader {

    private static Properties properties = new Properties();

    public PropertiesLoader() {
    }

    /**
     * method that contains all the logic to read the properties files
     * @param filename the properties file name
     * @return the properties to begin the game
     * @throws FileNotFoundException
     */
    public static Properties loadPropertiesFile(String filename) throws FileNotFoundException {
        try (InputStream input = new FileInputStream(filename)) {
            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return properties;
    }
}
