package thrones.game;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;


public class PropertiesLoader {

    private static Properties properties = new Properties();

    public PropertiesLoader() {
    }

    public static Properties loadPropertiesFile(String filename) throws FileNotFoundException {
        try (InputStream input = new FileInputStream(filename)) {
            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return properties;
    }
}
