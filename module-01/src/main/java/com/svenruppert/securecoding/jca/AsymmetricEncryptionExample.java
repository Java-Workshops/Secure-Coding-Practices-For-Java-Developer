package com.svenruppert.securecoding.jca;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.stream.IntStream;

public class AsymmetricEncryptionExample {

  public static void main(String[] args) throws Exception {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    PublicKey publicKey = keyPair.getPublic();
    PrivateKey privateKey = keyPair.getPrivate();

    char[] originalText = {'H', 'e', 'l', 'l', 'o', ' ', 'J', 'C', 'A'};
    byte[] encryptedText = encrypt(originalText, publicKey);
    char[] decryptedText = decrypt(encryptedText, privateKey);

    System.out.println(decryptedText);
    Arrays.fill(decryptedText, '\0'); // Sicheres Löschen der entschlüsselten Daten
    Arrays.fill(originalText, '\0');  // Sicheres Löschen der ursprünglichen Daten
  }

  private static byte[] encrypt(char[] input, PublicKey key) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key);

    byte[] inputBytes = new byte[input.length];
    IntStream
        .range(0, input.length)
        .forEachOrdered(i -> inputBytes[i] = (byte) input[i]);

    byte[] encryptedBytes = cipher.doFinal(inputBytes);
    Arrays.fill(inputBytes, (byte) 0);
    return encryptedBytes;
  }

  private static char[] decrypt(byte[] input, PrivateKey key) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
    cipher.init(Cipher.DECRYPT_MODE, key);

    byte[] decryptedBytes = cipher.doFinal(input);
    char[] decryptedChars = new char[decryptedBytes.length];

    IntStream
        .range(0, decryptedBytes.length)
        .forEachOrdered(i -> decryptedChars[i] = (char) decryptedBytes[i]);

    Arrays.fill(decryptedBytes, (byte) 0);
    return decryptedChars;
  }
}

