package io.github.rafael24595;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class CodeQR {

    private static final int SIZE = 40;

    private CodeQR() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void print(String text) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, SIZE, SIZE);

            for (int y = 0; y < SIZE; y++) {
                for (int x = 0; x < SIZE; x++) {
                    System.out.print(bitMatrix.get(x, y) ? "██" : "  ");
                }
                System.out.println();
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}
