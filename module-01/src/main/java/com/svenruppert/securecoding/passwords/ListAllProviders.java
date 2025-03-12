package com.svenruppert.securecoding.passwords;

import java.security.Provider;
import java.security.Security;

public class ListAllProviders {
  public static void main(String[] args) {
    for (Provider provider : Security.getProviders()) {
      System.out.println("Provider: " + provider.getName());
      for (Provider.Service service : provider.getServices()) {
//        System.out.println("  Algorithm: " + service.getAlgorithm());
        if ("SecureRandom".equals(service.getType())) {
          System.out.println("  Algorithmus: " + service.getAlgorithm());
        } else {
        }
      }
    }
  }
}
