package io.github.rafael24595.utils;

import io.github.rafael24595.utils.Base32;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Base32Test {

    @ParameterizedTest
    @CsvSource({
            "'JBSWY3DPFQQEEYLTMUZTEII=', 'Hello, Base32!'",
            "'JBUQ====', 'Hi'",
            "'J5FSC===', 'OK!'",
            "'JV4VK3DUOJQVG5LQMVZFGZLDOJSXIS3FPE======', 'MyUltraSuperSecretKey'",
    })
    void testEncoding(String base32, String text) throws EncoderException {
        assertEquals(base32, Base32.encode(text.getBytes()));
    }

    @ParameterizedTest
    @CsvSource({
            "'Hello, Base32!', 'JBSWY3DPFQQEEYLTMUZTEII'",
            "'Hi', 'JBUQ===='",
            "'OK!', 'J5FSC==='",
            "'MyUltraSuperSecretKey', 'JV4VK3DUOJQVG5LQMVZFGZLDOJSXIS3FPE======'",
    })
    void testDecoding(String text, String base32) {
        assertEquals(text, new String(Base32.decode(base32)));
    }

    @Test
    void testEmptyString() {
        assertEquals("", new String(Base32.decode(Base32.encode("".getBytes()))));
    }

    @Test
    public void testSymmetry() {
        String input = "Java Encoding Test!";
        String encoded = Base32.encode(input.getBytes());
        String decoded = new String(Base32.decode(encoded));
        assertEquals(input, decoded);
    }

    @Test
    void testInvalidBase32Input() {
        assertThrows(IllegalArgumentException.class, () -> {
            Base32.decode("INVALID%%STRING");
        });
    }

}
