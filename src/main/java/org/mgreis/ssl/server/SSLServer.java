package org.mgreis.ssl.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class SSLServer extends AbstractSSLServer {

    private static final Logger logger = LogManager.getLogger(SSLServer.class);

    public SSLServer(String szConfigurationFile) {
        super(szConfigurationFile);
    }

    /**
     * Creates a keyStore class
     *
     * @param szKeyStore     a {@link String} class instance containing the keyStore type
     * @param szPassword     a {@link String} class instance containing the password
     * @param szKeyStoreFile a {@link String} class instance containing the keyStore file location
     * @return a {@link KeyStore} class instance containing the resource
     * @author mgreis
     * @since 1.0.0
     */
    @Override
    public KeyStore getKeyStore(String szKeyStore, String szPassword, String szKeyStoreFile) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {

        KeyStore keyStore = KeyStore.getInstance(szKeyStore);

        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(szKeyStoreFile);

        keyStore.load(inputStream, szPassword.toCharArray());

        return keyStore;

    }

    @Override
    public KeyManagerFactory getKeyManagerFactory(KeyStore myKeyStore,
                                                  String szAlgorithm,
                                                  String szPassword,
                                                  String szProvider) throws NoSuchProviderException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(szAlgorithm);

        keyManagerFactory.init(myKeyStore, szPassword.toCharArray());

        return keyManagerFactory;
    }

    @Override
    public TrustManagerFactory getTrustManagerFactory(KeyStore myTrustStore, String szAlgorithm, String szProvider) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException {

        TrustManagerFactory myTrustManagerFactory = TrustManagerFactory.getInstance(szAlgorithm);

        myTrustManagerFactory.init(myTrustStore);

        return myTrustManagerFactory;
    }

    @Override
    public SSLContext getSSLContext(String szProtocol, KeyManager[] MyKeyManagerArray, TrustManager[] MyTrustManagerArray, SecureRandom mySecureRandom) throws NoSuchAlgorithmException, KeyManagementException {

        SSLContext mySSLContext = SSLContext.getInstance(szProtocol);

        mySSLContext.init(MyKeyManagerArray, MyTrustManagerArray, mySecureRandom);

        return mySSLContext;
    }
}
