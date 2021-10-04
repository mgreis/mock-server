package org.mgreis.ssl.server;

import org.junit.jupiter.api.Test;

public class SSLServerTest {

    @Test
    public void testStuff() {
        SSLServer myServer = new SSLServer("ssl_server.properties");
        System.out.println(myServer.initialize());
    }
}
