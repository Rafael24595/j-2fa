package io.github.rafael24595.totp;

import io.github.rafael24595.ITimeResolver;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;

public class ResolverTOTP {

    private static final String HMAC_ALGORITHM = "HmacSHA1";
    private static final int DEFAULT_INTERVAL = 30;
    private static final int DEFAULT_DIGITS = 6;
    private static final int DEFAULT_TOLERANCE = 0;

    private final int interval;
    private final int digits;
    private final int tolerance;
    private final ITimeResolver resolver;

    public ResolverTOTP() {
        this(DEFAULT_TOLERANCE, new TimeResolverSystem());
    }

    public ResolverTOTP(int tolerance) {
        this(tolerance, new TimeResolverSystem());
    }

    public ResolverTOTP(ITimeResolver resolver) {
        this(DEFAULT_TOLERANCE, resolver);
    }

    public ResolverTOTP(int tolerance, ITimeResolver resolver) {
        //TODO-FUTURE: Expose interval and digits parametrization.
        this.interval = DEFAULT_INTERVAL;
        this.digits = DEFAULT_DIGITS;
        this.tolerance = tolerance < 0 ? 0 : tolerance;
        this.resolver = resolver;
    }

    public int tolerance() {
        return this.tolerance;
    }

    public String generate(String secret) throws GeneralSecurityException {
        long time = this.resolver.nowMilliseconds() / 1000 / this.interval;
        return generate(secret, time);
    }

    private String generate(String secret, long time) throws GeneralSecurityException {
        byte[] keyBytes = SecretAdapter.decode(secret);

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

        int otp = binary % this.calculateDivisor(this.digits);

        String formatString = String.format("%%0%dd", this.digits);
        return String.format(formatString, otp);
    }

    public boolean validate(String secret, String code) throws GeneralSecurityException {
        long currentTime = this.resolver.nowMilliseconds() / 1000;

        for (int i = -this.tolerance; i <= this.tolerance; i++) {
            long timeCounter = (currentTime / this.interval) + i;
            String expectedCode = generate(secret, timeCounter);
            if (expectedCode.equals(code)) {
                return true;
            }
        }

        return false;
    }

    private int calculateDivisor(int digits) {
        int result = 1;
        for (int i = 0; i < digits; i++) {
            result *= 10;
        }
        return result;
    }

}