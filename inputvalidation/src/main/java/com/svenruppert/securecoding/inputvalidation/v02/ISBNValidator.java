package com.svenruppert.securecoding.inputvalidation.v02;

public class ISBNValidator {


  public static boolean isValidISBN(String isbn) {
    if (isbn.length() == 10) {
      return isValidISBN10(isbn);
    }
    if (isbn.length() == 13) {
      return isValidISBN13(isbn);
    }
    return false;
  }

  public static boolean isValidISBN10(String isbn) {
    if (!isbn.matches("\\d{9}[\\dX]")) return false;
    int sum = 0;
    for (int i = 0; i < 9; i++) {
      sum += (isbn.charAt(i) - '0') * (10 - i);
    }
    char lastChar = isbn.charAt(9);
    sum += (lastChar == 'X') ? 10 : (lastChar - '0');
    return sum % 11 == 0;
  }

  public static boolean isValidISBN13(String isbn) {
    if (!isbn.matches("\\d{13}")) return false;
    int sum = 0;
    for (int i = 0; i < 13; i++) {
      int digit = isbn.charAt(i) - '0';
      sum += (i % 2 == 0) ? digit : digit * 3;
    }
    return sum % 10 == 0;
  }
}
