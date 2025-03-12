package com.svenruppert.securecoding.passwords;

import java.security.SecureRandom;
import java.util.Random;

public class RandomComparison {


  public static void main(String[] args) {
    int seed = 12345; // Fester Seed für deterministische Ausgabe
    byte[] seedBytes = {1, 2, 3, 4, 5};
    int bound = 100; // Obergrenze für Zufallszahlen
    int count = 5; // Anzahl der zu generierenden Werte

    Random random1 = new Random(seed);
    Random random2 = new Random(seed);

    SecureRandom secureRandom1 = new SecureRandom(seedBytes);
    SecureRandom secureRandom2 = new SecureRandom(seedBytes);

    System.out.printf("%-15s %-15s %-15s %-15s%n", "Random (1)", "Random (2)", "SecureRandom (1)", "SecureRandom (2)");
    System.out.println("----------------------------------------------------------------------------------");

    for (int i = 0; i < count; i++) {
      int rand1 = random1.nextInt(bound);
      int rand2 = random2.nextInt(bound);
      int secRand1 = secureRandom1.nextInt(bound);
      int secRand2 = secureRandom2.nextInt(bound);

      System.out.printf("%-15d %-15d %-15d %-15d%n", rand1, rand2, secRand1, secRand2);
    }
  }
}

