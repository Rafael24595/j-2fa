package io.github.rafael24595;

import io.github.rafael24595.auth.AuthTOTP;
import io.github.rafael24595.output.CodeQR;
import org.apache.commons.codec.binary.Base32;

import java.security.GeneralSecurityException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws GeneralSecurityException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nProvide a secret: ");
        String secret = scanner.nextLine();

        String secret32 = new Base32().encodeToString(secret.getBytes());

        System.out.print(String.format("\nYour secret is \"%s\" which corresponds to the base32 \"%s\".", secret, secret32));
        CodeQR.print(secret32);

        AuthTOTP totp = new AuthTOTP();

        while (true) {
            String generatedCode = totp.generate(secret32);
            System.out.print("\nSystem suggested: " + generatedCode);
            System.out.print("\nProvide the key: ");

            String code = scanner.nextLine();

            if(code.equalsIgnoreCase("exit")) {
                System.out.print("\nExiting...");
                return;
            }

            System.out.print("--------------------------");

            if(totp.validate(secret32, code)) {
                System.out.print("\nCorrect key.");
            } else {
                System.out.print("\nIncorrect key.");
            }

            System.out.println();
        }
    }

}