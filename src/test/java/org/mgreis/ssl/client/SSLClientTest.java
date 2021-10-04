package org.mgreis.ssl.client;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class SSLClientTest {

    @Test
    public void testStuff() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        SSLClient myClient = new SSLClient();

        myClient.initialize();
    }
}
