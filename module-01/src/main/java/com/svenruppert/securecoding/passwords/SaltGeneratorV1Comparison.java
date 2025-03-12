package com.svenruppert.securecoding.passwords;

import java.util.HashSet;
import java.util.Set;

public class SaltGeneratorV1Comparison {

  protected static final int MAX_ROUND = 1;
  protected static final int MAX_VALUES = 100_000_000; // Anzahl der zu generierenden Werte
  protected static final int MAX_BYTE_COUNT = 10;

  public static void main(String[] args) {

    SaltGeneratorV1 saltGenerator = new SaltGeneratorV1();
    Set<String> secureRandomSet = new HashSet<>();
    int secureRandomCollisions = 0;

    System.out.printf("%-20s %-20s%n",
        "byte count",
        "SecureRandom Collisions");
    System.out.println("----------------------" +
        "----------------------------" +
        "----------------");

    for (int byteCount = 0; byteCount < MAX_BYTE_COUNT; byteCount++) {
      for (int round = 0; round < MAX_ROUND; round++) {
        for (int valueCount = 0; valueCount < MAX_VALUES; valueCount++) {
          String generatedSaltSec = saltGenerator.generateSalt(byteCount);
          if (secureRandomSet.contains(generatedSaltSec)) secureRandomCollisions++;
          else secureRandomSet.add(generatedSaltSec);
        }
          System.out.printf("%-20d %-20d%n",
              byteCount,
              secureRandomCollisions);
        secureRandomSet.clear();
        secureRandomCollisions = 0;
      }
    }
  }
}
