package com.svenruppert.securecoding.passwords;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PlainRandomCollisionComparison {
  public static void main(String[] args) {
    int count = 10_000_000; // Anzahl der zu generierenden Werte

    Random random = new Random();
    Set<Integer> randomSet = new HashSet<>();
    int randomCollisions = 0;

    SecureRandom secureRandom = new SecureRandom();
    Set<Integer> secureRandomSet = new HashSet<>();
    int secureRandomCollisions = 0;

    System.out.printf("%-20s %-20s %-20s%n",
        "Random Collisions",
        "SecureRandom Collisions",
        "Collision Difference");
    System.out.println("----------------------" +
        "----------------------------" +
        "----------------");

    for (int x = 0; x < 10; x++) {
      for (int i = 0; i < count; i++) {
        int randValue = random.nextInt();
        int secRandValue = secureRandom.nextInt();

        if (randomSet.contains(randValue)) randomCollisions++;
        else randomSet.add(randValue);

        if (secureRandomSet.contains(secRandValue)) secureRandomCollisions++;
        else secureRandomSet.add(secRandValue);
      }
      if (randomCollisions > secureRandomCollisions) {
        System.out.printf("(x) %-20d ( ) %-20d %-20d%n",
            randomCollisions,
            secureRandomCollisions,
            randomCollisions - secureRandomCollisions);
      } else {
        System.out.printf("( ) %-20d (x) %-20d %-20d%n",
            randomCollisions,
            secureRandomCollisions,
            secureRandomCollisions - randomCollisions);
      }
      randomSet.clear();
      secureRandomSet.clear();
      randomCollisions = 0;
      secureRandomCollisions = 0;
    }
  }
}
