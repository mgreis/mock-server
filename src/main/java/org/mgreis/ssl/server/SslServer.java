package org.mgreis.ssl.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mgreis.model.ReturnCode;
import org.mgreis.ssl.server.resource.ResourceManager;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Optional;


public class SSLServer {

    private static final Logger logger = LogManager.getLogger(SSLServer.class);

    private String szKeyStore = "PKCS12";

    private String szPassword = "";

    private String szKeyStoreFile = "";

    public SSLServer() {
        System.setProperty("javax.net.debug", "all");
    }


    public ReturnCode initialize(String szKeyStore,
                            String szPassword,
                            String szKeyStoreFile,
                            String szTrustStore,
                            String szTrustStorePassword,
                            String szTrustStoreFile) {
        try {
            Optional<KeyStore> myKeyStore = ResourceManager.getKeyStore(szKeyStore, szPassword, szKeyStoreFile);
            Optional<KeyStore> myTrustStore = ResourceManager.getKeyStore(szTrustStore, szTrustStorePassword, szTrustStoreFile);

            if (!myKeyStore.isPresent() ||
                    !myTrustStore.isPresent()) {

                logger.error("class=SslServer method=initialize message='Socket could not be initialized'");

                return ReturnCode.ERROR;
            } else {

            }


            // TrustManagerFactory
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX", "SunJSSE");


            trustManagerFactory.init(myTrustStore.get());
            X509TrustManager x509TrustManager = null;
            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    x509TrustManager = (X509TrustManager) trustManager;
                    break;
                }
            }

            if (x509TrustManager == null) throw new NullPointerException();


            // KeyManagerFactory ()
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
            keyManagerFactory.init(keyStore, password.toCharArray());
            X509KeyManager x509KeyManager = null;
            for (KeyManager keyManager : keyManagerFactory.getKeyManagers()) {
                if (keyManager instanceof X509KeyManager) {
                    x509KeyManager = (X509KeyManager) keyManager;
                    break;
                }
            }
            if (x509KeyManager == null) throw new NullPointerException();

            // set up the SSL Context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(new KeyManager[]{x509KeyManager}, new TrustManager[]{x509TrustManager}, null);

            SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(8333);
            serverSocket.setNeedClientAuth(true);
            serverSocket.setEnabledProtocols(new String[]{"TLSv1.2"});
            SSLSocket socket = (SSLSocket) serverSocket.accept();


            // InputStream and OutputStream Stuff
            PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;

            // Initiate conversation with client
            KnockKnockProtocol kkp = new KnockKnockProtocol();
            outputLine = kkp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye."))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }


}
