package core.framework.api.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author neo
 */
public class EncodingsTest {
    @Test
    public void base64() {
        assertEquals("", Encodings.base64(""));
        // from http://en.wikipedia.org/wiki/Base64
        assertEquals("bGVhc3VyZS4=", Encodings.base64("leasure."));
    }

    @Test
    public void decodeBase64() {
        // from http://en.wikipedia.org/wiki/Base64
        assertEquals("leasure.", new String(Encodings.decodeBase64("bGVhc3VyZS4="), Charsets.UTF_8));
    }

    @Test
    public void base64URLSafe() {
        byte[] bytes = new byte[256];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) i;
        }
        String encodedMessage = Encodings.base64URLSafe(bytes);
        assertArrayEquals(bytes, Encodings.decodeBase64URLSafe(encodedMessage));
    }
}