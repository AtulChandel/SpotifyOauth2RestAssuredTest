package spotify.oauth2.utils;

import java.io.*;
import java.util.Properties;

public class PropertyUtils {
    public static Properties propertyLoader(String filePath) {
        Properties properties = new Properties();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
            return properties;
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
