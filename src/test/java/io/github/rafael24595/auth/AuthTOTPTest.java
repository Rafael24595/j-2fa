package io.github.rafael24595.auth;

import io.github.rafael24595.utils.Base32;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;

class AuthTOTPTest {

    @ParameterizedTest
    @CsvSource({
            "'MyUltraSuperSecretKey'",
            "'HelloTOTPAuth'",
            "'MySecret'",
    })
    void testSymmetry(String secret) throws GeneralSecurityException {
        AuthTOTP totp = new AuthTOTP();

        secret = Base32.encode(secret);

        String code = totp.generate(secret);
        boolean intTime = totp.validate(secret, code);

        assertTrue(intTime);
    }

    @ParameterizedTest
    @CsvSource({
            "'MyUltraSuperSecretKey'",
            "'HelloTOTPAuth'",
            "'MySecret'",
    })
    void testResolverSymmetry(String secret) throws GeneralSecurityException {
        TimeResolverTraveler resolver = new TimeResolverTraveler();
        AuthTOTP totp = new AuthTOTP(resolver);

        secret = Base32.encode(secret);

        String code = totp.generate(secret);
        boolean intTime = totp.validate(secret, code);

        resolver.fastForward(30 * 1000);
        boolean fastForward = totp.validate(secret, code);

        assertTrue(intTime);
        assertFalse(fastForward);
    }

    @ParameterizedTest
    @CsvSource({
            "'MyUltraSuperSecretKey', -1",
            "'MyUltraSuperSecretKey', 0",
            "'MyUltraSuperSecretKey', 1",
    })
    void testToleranceSymmetry(String secret, int tolerance) throws GeneralSecurityException {
        TimeResolverTraveler resolver = new TimeResolverTraveler();
        AuthTOTP totp = new AuthTOTP(tolerance, resolver);

        secret = Base32.encode(secret);

        String code = totp.generate(secret);
        boolean intTime = totp.validate(secret, code);

        long increment = (30 * totp.tolerance()) * 1000;
        resolver.fastForward(increment);
        boolean fastForward = totp.validate(secret, code);

        assertTrue(intTime);
        assertTrue(fastForward);
    }

}
