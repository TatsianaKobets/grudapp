package org.example.grudapp.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

  private static final String CONFIG_FILE = "config.properties";

  public static Properties loadConfig() {
    Properties properties = new Properties();
    try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
      if (input == null) {
        System.out.println("Sorry, unable to find " + CONFIG_FILE);
        return null;
      }

      properties.load(input);
      return properties;
    } catch (IOException e) {
      System.out.println("Error loading config: " + e.getMessage());
      return null;
    }
  }
}
