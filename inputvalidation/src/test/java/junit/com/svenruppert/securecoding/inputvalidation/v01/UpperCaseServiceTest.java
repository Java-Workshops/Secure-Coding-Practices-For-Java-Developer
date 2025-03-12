package junit.com.svenruppert.securecoding.inputvalidation.v01;

import com.svenruppert.securecoding.inputvalidation.v01.UpperCaseService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpperCaseServiceTest {

  @Test
  void test001() {
    String hello = new UpperCaseService()
        .toUpperCase("hello");
    assertEquals("HELLO", hello);
  }

}
