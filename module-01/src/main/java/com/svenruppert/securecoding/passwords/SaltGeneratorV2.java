package com.svenruppert.securecoding.passwords;

import java.security.*;

import static java.security.DrbgParameters.instantiation;

public class SaltGeneratorV2 {

  private static final String DRBG = "DRBG";
  private static final String NATIVE_PRNG_BLOCKING = "NativePRNGBlocking";
  private static SecureRandom secureRandom;

  static {
    boolean drgb = false;
    boolean nativePrng = false;
    for (Provider provider : Security.getProviders()) {
      for (Provider.Service service : provider.getServices()) {
        if ("SecureRandom".equals(service.getType())) {
          String algorithm = service.getAlgorithm();
          if (algorithm.equals(DRBG)) drgb = true;
          if (algorithm.equals(NATIVE_PRNG_BLOCKING)) nativePrng = true;
          try {
            if (drgb) initDRGB();
            if (nativePrng && !drgb) initPrngBlocking();
          } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }

  private static void initPrngBlocking() throws NoSuchAlgorithmException {
    if (secureRandom == null) {
      synchronized (SaltGeneratorV2.class) {
        if (secureRandom == null) {
          secureRandom = SecureRandom.getInstance(NATIVE_PRNG_BLOCKING);
        }
      }
    }
  }

  private static void initDRGB() throws NoSuchAlgorithmException {
    if (secureRandom == null) {
      synchronized (SaltGeneratorV2.class) {
        if (secureRandom == null) {
          secureRandom = SecureRandom.getInstance(DRBG, instantiation(
              256, // Security Strength (128, 192, 256 Bit)
              DrbgParameters.Capability.PR_AND_RESEED, // Reseed erlaubt
              "CustomPersonalizationString".getBytes() // Optionaler Personalization String
          ));
        }
      }
    }
  }

  public byte[] generateSalt(int length) throws NoSuchAlgorithmException {
    byte[] salt = new byte[length];
    secureRandom.nextBytes(salt);
    return salt;
  }
}

