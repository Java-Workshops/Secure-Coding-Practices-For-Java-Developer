package com.svenruppert.securecoding.authentication.rest.demo01.v02;


import com.svenruppert.dependencies.core.logger.HasLogger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class RestClientV2
    implements HasLogger {

  protected static final byte[] PREFIX_USERNAME = "username=".getBytes(StandardCharsets.UTF_8);
  protected static final byte[] PREFIX_PASSWORD = "&password=".getBytes(StandardCharsets.UTF_8);

  public static void main(String[] args) throws IOException {
    char[] username = {'a', 'd', 'm', 'i', 'n'};
    char[] password = {'s', 'e', 'c', 'r', 'e', 't'};

    byte[] postData = PREFIX_USERNAME;
    postData = concatenate(postData, charToBytes(username));
    postData = concatenate(postData, PREFIX_PASSWORD);
    postData = concatenate(postData, charToBytes(password));

    URL url = URI.create("http://localhost:8080/login").toURL();
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

    Arrays.fill(username, '\0');
    Arrays.fill(password, '\0');
    Arrays.fill(postData, (byte) 0);
  }

  private static byte[] charToBytes(char[] chars) {
    byte[] bytes = new byte[chars.length];
    IntStream
        .range(0, chars.length)
        .forEachOrdered(i -> bytes[i] = (byte) chars[i]);
    return bytes;
  }

  private static byte[] concatenate(byte[] first, byte[] second) {
    byte[] result = new byte[first.length + second.length];
    System.arraycopy(first, 0, result, 0, first.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }
}
