package junit.com.svenruppert.securecoding.core;

import com.svenruppert.securecoding.core.CharArrayString;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;

import static org.junit.jupiter.api.Assertions.*;

class CharArrayStringTest {

  @Test
  void length() {
    CharArrayString cas = new CharArrayString();
    cas.append('a');
    assertEquals(1, cas.length());
    cas.append('b');
    assertEquals(2, cas.length());
  }

  @Test
  void charAt() {
    CharArrayString cas = new CharArrayString();
    cas.append(new char[]{'a','b','c','d',});
    assertEquals('c',cas.charAt(2));
  }

  @Test
  void substring() {
    CharArrayString cas = new CharArrayString();
    cas.append(new char[]{'a','b','c','d',});
    char[] expected = {'b', 'c'};
    char[] actual = cas.substring(1, 3).toCharArray();
    assertArrayEquals(expected, actual);
  }

  @Test
  void toCharArray() {
    CharArrayString cas = new CharArrayString();
    char[] input = {'a', 'b', 'c', 'd',};
    cas.append(input);
    char[] actual = cas.toCharArray();
    assertArrayEquals(input, actual);
  }

  @Test
  void clear() {
    CharArrayString cas = new CharArrayString();
    char[] input = {'a', 'b', 'c', 'd',};
    cas.append(input);
    assertEquals(4,cas.length());

    cas.clear();
    assertEquals(0, cas.length());
  }

  @Test
  void testEquals() {
    char[] input = {'a', 'b', 'c', 'd',};
    CharArrayString casA = new CharArrayString(input);
    CharArrayString casB = new CharArrayString(input);
    assertEquals(casA, casB);
  }
}