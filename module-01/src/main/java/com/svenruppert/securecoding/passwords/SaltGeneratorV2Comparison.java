package com.svenruppert.securecoding.passwords;

import java.security.NoSuchAlgorithmException;

public class SaltGeneratorV2Comparison {

  public static void main(String[] args) {
    SaltGeneratorV2 saltGenerator = new SaltGeneratorV2();
    try {
      byte[] salt = saltGenerator.generateSalt(16);
      System.out.println("Generierter Salt-Wert: " + bytesToHex(salt));
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Fehler: " + e.getMessage());
    }
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder hexString = new StringBuilder();
    for (byte b : bytes) {
      hexString.append(String.format("%02x", b));
    }
    return hexString.toString();
  }
}
