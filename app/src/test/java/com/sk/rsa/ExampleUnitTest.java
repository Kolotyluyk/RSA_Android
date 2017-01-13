package com.sk.rsa;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    RSA rsa;


    @Test
    public void testEncrypt() throws Exception {
        rsa = new RSA(8);
        String a = rsa.encrypt("test", rsa.getN().intValue(), rsa.getE().intValue());

        assertEquals("test", rsa.decrypt(a, rsa.getN().intValue(), rsa.getE().intValue()));
    }

    @Test
    public void testN() throws Exception {
        int p = rsa.getP().intValue();
        int q = rsa.getQ().intValue();

        assertEquals(p * q, rsa.getN().intValue());
    }


}