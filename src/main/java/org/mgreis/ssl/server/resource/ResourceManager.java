package org.mgreis.ssl.server.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Optional;

public class ResourceManager {

    private static final Logger logger = LogManager.getLogger(ResourceManager.class);

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
    public static Optional<KeyStore> getKeyStore(String szKeyStore, String szPassword, String szKeyStoreFile) {

        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(szKeyStore);

            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(szKeyStoreFile);

            keyStore.load(inputStream, szPassword.toCharArray());

            return Optional.of(keyStore);

        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException exc) {
            logger.warn("class=ResourceManager method=getKeystore message='An exception has occurred'", exc);

            return Optional.empty();
        }
    }

    public static Optional<TrustManagerFactory> getTrustStoreManager (KeyStore myTrustStore, String szAlgorithm, String szProvider) {
        TrustManagerFactory trustManagerFactory = null;
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(szAlgorithm, szProvider);

        } catch (NoSuchAlgorithmException|NoSuchProviderException exc) {
            logger.warn("class=ResourceManager method=getKeystore message='An Exception has occurred while initializing the TrustStoreManager'", exc);
            return Optional.empty();
        }

        try {
            trustManagerFactory.init(myTrustStore);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        X509TrustManager x509TrustManager = null;
        for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager) {
                x509TrustManager = (X509TrustManager) trustManager;
                break;
            }
        }

        if (x509TrustManager == null) {
            logger.warn("class=ResourceManager method=getKeystore message='No suitable trustManager was found'");
            return Optional.empty();
        }

        return  Optional.of(trustManagerFactory);
    }
}
