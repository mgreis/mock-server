package org.mgreis.properties;

import org.junit.jupiter.api.Test;

public class SSLServerPropertyKeyTest {

    @Test
    public void testEnum() {
        System.out.println(SSLServerPropertyKeys.SSL_CONTEXT_PROTOCOL.get());
    }
}
