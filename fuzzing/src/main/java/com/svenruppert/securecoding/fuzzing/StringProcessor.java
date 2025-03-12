package com.svenruppert.securecoding.fuzzing;

public class StringProcessor {
  public static String process(String input) {
    if (input == null || input.isBlank()) {
      throw new IllegalArgumentException("Input darf nicht leer sein!");
    }
    if (input.equals("crash")) {
      throw new RuntimeException("Test-Crash ausgelöst!");
    }
    return input.trim().toUpperCase();
  }
}
