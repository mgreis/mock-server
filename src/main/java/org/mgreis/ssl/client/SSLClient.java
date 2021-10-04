package org.mgreis.ssl.client;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class SSLClient {


    public void initialize() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        SSLSocketFactory factory = this.buildSocketFactory();
        Socket s = factory.createSocket("127.0.0.1", 1234);
    }

    private SSLSocketFactory buildSocketFactory() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[] { new BlindTrustManager() }, null);
        return ctx.getSocketFactory();
    }
}
