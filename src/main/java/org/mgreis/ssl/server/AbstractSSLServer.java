package org.mgreis.ssl.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mgreis.properties.SSLServerProperties;
import org.mgreis.properties.SSLServerPropertyKeys;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.*;
import java.security.cert.CertificateException;

//import org.mgreis.model.ReturnCode;


public abstract class AbstractSSLServer {

    private static final Logger logger = LogManager.getLogger(AbstractSSLServer.class);

    protected SSLServerProperties mySSSLServerProperties;


    public AbstractSSLServer(String szConfigurationFile) {
        System.setProperty("javax.net.debug", "all");
        this.mySSSLServerProperties = new SSLServerProperties(szConfigurationFile);
    }


    public int initialize() {
        try {
            KeyStore myKeyStore = this.getKeyStore(this.mySSSLServerProperties.get(SSLServerPropertyKeys.SSL_KEYSTORE.get()),
                    this.mySSSLServerProperties.get(SSLServerPropertyKeys.SSL_KEYSTORE_PASSWORD.get()),
                    this.mySSSLServerProperties.get(SSLServerPropertyKeys.SSL_KEYSTORE_FILE.get()));

            KeyStore myTrustStore = this.getKeyStore(this.mySSSLServerProperties.get(SSLServerPropertyKeys.SSL_TRUSTSTORE.get()),
                    this.mySSSLServerProperties.get(SSLServerPropertyKeys.SSL_TRUSTSTORE_PASSWORD.get()),
                    this.mySSSLServerProperties.get(SSLServerPropertyKeys.SSL_TRUSTSTORE_FILE.get()));

            KeyManagerFactory myKeyManagerFactory = this.getKeyManagerFactory(myKeyStore,
                    this.mySSSLServerProperties.get(SSLServerPropertyKeys.SSL_KEYSTORE_MANAGER_ALGORITHM.get()),
                    this.mySSSLServerProperties.get(SSLServerPropertyKeys.SSL_KEYSTORE_PASSWORD.get()),
                    this.mySSSLServerProperties.get(SSLServerPropertyKeys.SSL_KEYSTORE_MANAGER_PROVIDER.get()));

            TrustManagerFactory myTrustManagerFactory = this.getTrustManagerFactory(myTrustStore,
                    this.mySSSLServerProperties.get(SSLServerPropertyKeys.SSL_TRUSTSTORE_MANAGER_ALGORITHM.get()),
                    this.mySSSLServerProperties.get(SSLServerPropertyKeys.SSL_TRUSTSTORE_MANAGER_PROVIDER.get()));

            SSLContext mySSLContext = this.getSSLContext(this.mySSSLServerProperties.get(SSLServerPropertyKeys.SSL_CONTEXT_PROTOCOL.get()),
                    myKeyManagerFactory.getKeyManagers(),
                    myTrustManagerFactory.getTrustManagers(),
                    new SecureRandom());


            SSLServerSocketFactory serverSocketFactory = mySSLContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(1234);
            serverSocket.setNeedClientAuth(true);
            serverSocket.setEnabledProtocols(new String[]{this.mySSSLServerProperties.get(SSLServerPropertyKeys.SSL_PROTOCOL.get())});
            SSLSocket socket = (SSLSocket) serverSocket.accept();


            // InputStream and OutputStream Stuff
            PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;

            /* Initiate conversation with client
            KnockKnockProtocol kkp = new KnockKnockProtocol();
            outputLine = kkp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye."))
                    break;
            }*/
        } catch (Exception exc) {
            logger.warn("method=getKeystore message='An exception has occurred'", exc);
        }
        return 0;
    }

    abstract KeyStore getKeyStore(String szKeyStore, String szPassword, String szKeyStoreFile) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException;

    abstract KeyManagerFactory getKeyManagerFactory(KeyStore myKeyStore, String szPassword, String szAlgorithm, String szProvider) throws NoSuchProviderException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException;

    abstract TrustManagerFactory getTrustManagerFactory(KeyStore myTrustStore, String szAlgorithm, String szProvider) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException;

    abstract SSLContext getSSLContext(String szProtocol, KeyManager[] MyKeyManagerArray, TrustManager[] MyTrustManagerArray, SecureRandom mySecureRandom) throws NoSuchAlgorithmException, KeyManagementException;


}
