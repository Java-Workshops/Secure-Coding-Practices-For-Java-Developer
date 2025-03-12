package fuzz.com.svenruppert.securecoding.fuzzing;

import net.jqwik.api.*;
import net.jqwik.api.constraints.Size;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MonkeyTestingJqwik {
  @Property
  void testWithRandomData(@ForAll String input) {
    assertDoesNotThrow(() -> processInput(input));
  }

  void processInput(String input) {
    if (input.length() > 1000) {
      throw new IllegalArgumentException("String too long");
    }
    if (input.contains("crash")) {
      throw new RuntimeException("Detected crash keyword!");
    }
  }

  @Property
  void testWithRandomLimitedStrings(@ForAll @Size(max = 50) String input) {
    assertDoesNotThrow(() -> processInputV02(input));
  }

  @Property
  void testWithOnlyLetters(@ForAll("alphaStrings") String input) {
    assertDoesNotThrow(() -> processInputV02(input));
  }

  @Provide
  Arbitrary<String> alphaStrings() {
    return Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(20);
  }

  void processInputV02(String input) {
    if (input.length() > 50) {
      throw new IllegalArgumentException("String too long");
    }
    if (input.contains("crash")) {
      throw new RuntimeException("Detected crash keyword!");
    }
  }
}
