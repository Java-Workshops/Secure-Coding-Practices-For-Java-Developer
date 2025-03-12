package junit.com.svenruppert.securecoding.inputvalidation.v02;

import com.svenruppert.securecoding.inputvalidation.v02.ISBNValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.*;

public class ISBNValidatorTestV02 {


  @ParameterizedTest
  @CsvSource({
      "0306406152, true",
      "0471958697, true",
      "359821507X, true",
      "9780134685992, false",
      "1234567890123, false"
  })
  void test001(String isbn, boolean expected) {
    boolean validISBN = ISBNValidator.isValidISBN(isbn);
    assertEquals(expected, validISBN);
  }

  @Test
  void testValidISBN10() {
    assertTrue(ISBNValidator.isValidISBN("0306406152"));
    assertTrue(ISBNValidator.isValidISBN("0471958697"));
    assertTrue(ISBNValidator.isValidISBN("359821507X"));
  }

  @Test
  void testInvalidISBN13() {
    assertFalse(ISBNValidator.isValidISBN("9780134685992"));
    assertFalse(ISBNValidator.isValidISBN("1234567890123"));
  }
}
