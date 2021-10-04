package org.mgreis.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SSLServerProperties {

    String szFilename;

    Properties prop = new Properties();

    public SSLServerProperties(String szFilename) {
        this.szFilename = szFilename;
        this.init();
    }


    private void init() {

        try (InputStream input = SSLServerProperties.class.getClassLoader().getResourceAsStream(this.szFilename)) {

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String get(String key) {
        return prop.getProperty(key);
    }
}
