package com.svenruppert.securecoding.jca;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.stream.IntStream;

public class SecureSymmetricEncryption {

  public static void main(String[] args) throws Exception {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(256);
    SecretKey secretKey = keyGenerator.generateKey();

    SecureRandom secureRandom = new SecureRandom();
    byte[] iv = new byte[16];
    secureRandom.nextBytes(iv);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);

    char[] originalText = {'H', 'e', 'l', 'l', 'o', ' ', 'J', 'C', 'A'};
    byte[] encryptedText = encrypt(originalText, secretKey, ivSpec);
    char[] decryptedText = decrypt(encryptedText, secretKey, ivSpec);

    System.out.println(decryptedText);
    Arrays.fill(decryptedText, '\0');
    Arrays.fill(originalText, '\0');
  }

  private static byte[] encrypt(char[] input, SecretKey key, IvParameterSpec ivSpec) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

    byte[] inputBytes = new byte[input.length];
    IntStream.range(0, input.length).forEachOrdered(i -> inputBytes[i] = (byte) input[i]);

    byte[] encryptedBytes = cipher.doFinal(inputBytes);
    Arrays.fill(inputBytes, (byte) 0);
    return encryptedBytes;
  }

  private static char[] decrypt(byte[] input, SecretKey key, IvParameterSpec ivSpec) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

    byte[] decryptedBytes = cipher.doFinal(input);
    char[] decryptedChars = new char[decryptedBytes.length];

    IntStream.range(0, decryptedBytes.length).forEachOrdered(i -> decryptedChars[i] = (char) decryptedBytes[i]);

    Arrays.fill(decryptedBytes, (byte) 0);
    return decryptedChars;
  }
}

