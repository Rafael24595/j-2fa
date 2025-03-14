package io.github.rafael24595.totp;

import io.github.rafael24595.utils.Base32;

public class SecretAdapter {

    private SecretAdapter() throws InstantiationException {
        throw new InstantiationException("Can not instantiate an utility class.");
    }

    public static String encode(String secret) {
        return Base32.encode(secret);
    }

    public static byte[] decode(String encoded) {
        return Base32.decode(encoded);
    }

}
