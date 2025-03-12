package fuzz.com.svenruppert.securecoding.fuzzing;

import com.svenruppert.securecoding.fuzzing.StringProcessor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;


class StringProcessorFuzzTest {

  // Methode zur Generierung zufälliger Strings
  static Stream<String> randomStrings() {
    Random random = new Random();
    return Stream.generate(() -> {
      int length = random.nextInt(20); // Zufällige Länge (0-19)
      return random
          .ints(32, 127) // ASCII-Zeichen
          .limit(length)
          .collect(StringBuilder::new,
              StringBuilder::appendCodePoint,
              StringBuilder::append)
          .toString();
    }).limit(100); // 100 zufällige Strings testen
  }

  // ClusterFuzz - infrastructure
  // https://github.com/google/oss-fuzz?tab=readme-ov-file

  @ParameterizedTest
  @MethodSource("randomStrings")
  void fuzzTest02(String input) {
    try {
      String result = StringProcessor.process(input);
      assertNotNull(result);
    } catch (Exception e) {
      System.err.println("Fehler bei Input: " + input);
      throw e;
    }
  }
}
