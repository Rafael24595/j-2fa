package io.github.rafael24595.output;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class CodeQR {

    private static final int DEFAULT_SIZE = 40;

    private CodeQR() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void print(String text) {
        print(text, DEFAULT_SIZE);
    }

    public static void print(String text, int size) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size);

            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    System.out.print(bitMatrix.get(x, y) ? "██" : "  ");
                }
                System.out.println();
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}
