package spring.deserve.it.properties;

import lombok.Getter;

import java.util.Properties;

@Getter
public class PropertiesLoader {

    private static Properties contextProperties;

    private PropertiesLoader() {
    }

    public static void loadProperties(String resourceFileName) {
        Properties configuration = new Properties();
        try (var inputStream = PropertiesLoader.class
                .getClassLoader()
                .getResourceAsStream(resourceFileName)) {
            configuration.load(inputStream);
            contextProperties = configuration;
        } catch (Exception e) {
            System.out.println("Error loading properties file: " + resourceFileName);
        }
    }

    public static String getProperty(String key) {
        return contextProperties.getProperty(key);
    }

    public static int getIntProperty(String key) {
        return Integer.parseInt(contextProperties.getProperty(key));
    }

    public static long getLongProperty(String key) {
        return Long.parseLong(contextProperties.getProperty(key));
    }

    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(contextProperties.getProperty(key));
    }
}
