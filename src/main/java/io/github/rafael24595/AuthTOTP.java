package io.github.rafael24595;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;

public class AuthTOTP {

    private static final String HMAC_ALGORITHM = "HmacSHA1";
    private static final int INTERVAL = 30;
    private static final int DIGITS = 6;
    private static final int TOLERANCE = 0;

    private AuthTOTP() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static String generate(String secret) throws GeneralSecurityException {
        long time = System.currentTimeMillis() / 1000 / INTERVAL;
        return generate(secret, time);
    }

    private static String generate(String secret, long time) throws GeneralSecurityException {
        byte[] keyBytes = new Base32().decode(secret);

        byte[] timeBytes = ByteBuffer.allocate(8)
                .putLong(time)
                .array();

        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec key = new SecretKeySpec(keyBytes, HMAC_ALGORITHM);
        mac.init(key);

        byte[] hash = mac.doFinal(timeBytes);
        int offset = hash[hash.length - 1] & 0xF;
        int binary = ((hash[offset] & 0x7F) << 24) |
                ((hash[offset + 1] & 0xFF) << 16) |
                ((hash[offset + 2] & 0xFF) << 8) |
                (hash[offset + 3] & 0xFF);

        int otp = binary % (int) Math.pow(10, DIGITS);
        return String.format("%06d", otp);
    }

    public static boolean validate(String secret, String userCode) throws GeneralSecurityException {
        long currentTime = System.currentTimeMillis() / 1000;

        for (int i = -TOLERANCE; i <= TOLERANCE; i++) {
            long timeCounter = (currentTime / INTERVAL) + i;
            String expectedCode = generate(secret, timeCounter);
            if (expectedCode.equals(userCode)) {
                return true;
            }
        }

        return false;
    }

}