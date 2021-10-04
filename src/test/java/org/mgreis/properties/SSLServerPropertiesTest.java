package org.mgreis.properties;

import org.junit.jupiter.api.Test;

public class SSLServerPropertiesTest {

    String szFilename = "ssl_server.properties";

    @Test
    public void testStuff() {
        SSLServerProperties myProperties= new SSLServerProperties(this.szFilename);

        System.out.println(myProperties.get("ssl.key_store"));
    }
}
