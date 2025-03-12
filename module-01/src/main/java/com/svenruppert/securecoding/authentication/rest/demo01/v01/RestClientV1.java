package com.svenruppert.securecoding.authentication.rest.demo01.v01;

import com.svenruppert.dependencies.core.logger.HasLogger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class RestClientV1
    implements HasLogger {

  public static void main(String[] args) throws IOException {
    char[] username = {'a', 'd', 'm', 'i', 'n'};
    char[] password = {'s', 'e', 'c', 'r', 'e', 't'};

    String body = "username=" + new String(username) + "&password=" + new String(password);
    byte[] postData = body.getBytes(StandardCharsets.UTF_8);

    URL url = new URL("http://localhost:8080/login");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    conn.setDoOutput(true);

    try (OutputStream os = conn.getOutputStream()) {
      os.write(postData);
    }

    int responseCode = conn.getResponseCode();
    try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8)) {
      String response = scanner.useDelimiter("\\A").next();
      System.out.println("Response: " + response);
    }

    Arrays.fill(username, '\0'); // Löschen der sensiblen Daten
    Arrays.fill(password, '\0');
  }


  private static byte[] convert(char[] input) throws CharacterCodingException {
    CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
    CharBuffer charBuffer = CharBuffer.wrap(input);
    ByteBuffer byteBuffer = encoder.encode(charBuffer);
    return new byte[byteBuffer.remaining()];
  }

}
