package com.svenruppert.securecoding.jca;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Arrays;
import java.util.stream.IntStream;

public class DigitalSignatureExample {

  public static void main(String[] args) throws Exception {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    PublicKey publicKey = keyPair.getPublic();
    PrivateKey privateKey = keyPair.getPrivate();

    char[] message = {'H', 'e', 'l', 'l', 'o', ' ', 'J', 'C', 'A'};
    byte[] signature = signData(message, privateKey);
    boolean isValid = verifyData(message, signature, publicKey);

    System.out.println("Signatur gültig: " + isValid);
    Arrays.fill(message, '\0');
  }

  private static byte[] signData(char[] input, PrivateKey key) throws Exception {
    Signature signature = Signature.getInstance("SHA256withRSA");
    signature.initSign(key);

    byte[] inputBytes = new byte[input.length];
    IntStream.range(0, input.length).forEachOrdered(i -> inputBytes[i] = (byte) input[i]);

    signature.update(inputBytes);
    byte[] signedBytes = signature.sign();
    Arrays.fill(inputBytes, (byte) 0);
    return signedBytes;
  }

  private static boolean verifyData(char[] input, byte[] signatureBytes, PublicKey key) throws Exception {
    Signature signature = Signature.getInstance("SHA256withRSA");
    signature.initVerify(key);

    byte[] inputBytes = new byte[input.length];
    IntStream.range(0, input.length).forEachOrdered(i -> inputBytes[i] = (byte) input[i]);

    signature.update(inputBytes);
    boolean isValid = signature.verify(signatureBytes);
    Arrays.fill(inputBytes, (byte) 0);
    return isValid;
  }
}

