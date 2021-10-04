package org.mgreis.properties;

public enum SSLServerPropertyKeys {
    SSL_KEYSTORE("ssl.keystore"),
    SSL_KEYSTORE_PASSWORD("ssl.keystore.password"),
    SSL_KEYSTORE_FILE("ssl.keystore.file"),
    SSL_KEYSTORE_MANAGER_ALGORITHM("ssl.keystore.manager.algorithm"),
    SSL_KEYSTORE_MANAGER_PROVIDER("ssl.keystore.manager.provider"),

    SSL_TRUSTSTORE("ssl.truststore"),
    SSL_TRUSTSTORE_PASSWORD("ssl.truststore.password"),
    SSL_TRUSTSTORE_FILE("ssl.truststore.file"),
    SSL_TRUSTSTORE_MANAGER_ALGORITHM("ssl.truststore.manager.algorithm"),
    SSL_TRUSTSTORE_MANAGER_PROVIDER("ssl.truststore.manager.provider"),

    SSL_PROTOCOL("ssl.protocol"),

    SSL_CONTEXT_PROTOCOL("ssl.context.protocol"),

    SSL_SERVER_PORT("ssl.server.port");

    private String szPropertyKey;

    private SSLServerPropertyKeys(String szPropertyKey) {
        this.szPropertyKey = szPropertyKey;
    }

    public String get() {
        return szPropertyKey;
    }
}
