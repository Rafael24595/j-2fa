package io.github.rafael24595.utils;

import java.util.Arrays;

public class Base32 {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private static final char PADDING_CHAR = '=';

    private Base32() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static String encode(String data) {
        return encode(data.getBytes());
    }

    public static String encode(byte[] data) {
        if (data == null || data.length == 0) return "";

        StringBuilder encoded = new StringBuilder();
        int index = 0;
        int buffer = 0;
        int bitsLeft = 0;

        while (index < data.length) {
            buffer = (buffer << 8) | (data[index++] & 0xFF);
            bitsLeft += 8;

            while (bitsLeft >= 5) {
                int charIndex = (buffer >> (bitsLeft - 5)) & 0x1F;
                encoded.append(ALPHABET.charAt(charIndex));
                bitsLeft -= 5;
            }
        }

        if (bitsLeft > 0) {
            buffer = buffer << (5 - bitsLeft);
            encoded.append(ALPHABET.charAt(buffer & 0x1F));
        }

        while (encoded.length() % 8 != 0) {
            encoded.append(PADDING_CHAR);
        }

        return encoded.toString();
    }

    public static byte[] decode(String encoded) {
        encoded = encoded.replaceAll(Character.toString(PADDING_CHAR), ""); // Remove padding
        byte[] decoded = new byte[(encoded.length() * 5) / 8];
        int buffer = 0;
        int bitsLeft = 0;
        int outputIndex = 0;

        for (char c : encoded.toCharArray()) {
            int value = ALPHABET.indexOf(c);
            if (value == -1) {
                throw new IllegalArgumentException("Invalid Base32 character: " + c);
            }

            buffer = (buffer << 5) | value;
            bitsLeft += 5;

            if (bitsLeft >= 8) {
                decoded[outputIndex++] = (byte) ((buffer >> (bitsLeft - 8)) & 0xFF);
                bitsLeft -= 8;
            }
        }

        return Arrays.copyOf(decoded, outputIndex);
    }

}
