package com.svenruppert.securecoding.authentication.rest.demo01.v02;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.svenruppert.dependencies.core.logger.HasLogger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.IntStream;

public class LoginHandlerV2
    implements HttpHandler, HasLogger {

  private static final char[] USERNAME = {'a', 'd', 'm', 'i', 'n'};
  private static final char[] PASSWORD = {'s', 'e', 'c', 'r', 'e', 't'};

  private char[] extractValue(byte[] bodyBytes, String key) {
    int start = findIndex(bodyBytes, key + "=");
    if (start == -1) return new char[0];
    start += key.length() + 1;

    int end = findIndex(bodyBytes, "&", start);
    if (end == -1) end = bodyBytes.length;

    char[] value = new char[end - start];
    for (int i = start, j = 0; i < end; i++, j++) {
      value[j] = (char) bodyBytes[i];
    }
    return value;
  }

  private int findIndex(byte[] data, String pattern) {
    return findIndex(data, pattern, 0);
  }

  private int findIndex(byte[] data, String pattern, int start) {
    byte[] patternBytes = pattern.getBytes(StandardCharsets.UTF_8);
    return IntStream
        .rangeClosed(start, data.length - patternBytes.length)
        .filter(i -> matchesPattern(data, patternBytes, i))
        .findFirst()
        .orElse(-1);
  }

  private boolean matchesPattern(byte[] data, byte[] pattern, int start) {
    return IntStream
        .range(0, pattern.length)
        .noneMatch(j -> data[start + j] != pattern[j]);
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if ("POST".equals(exchange.getRequestMethod())) {
      logger().info("Received POST request");
      byte[] requestBody = exchange.getRequestBody().readAllBytes();

      char[] receivedUsername = extractValue(requestBody, "username");
      char[] receivedPassword = extractValue(requestBody, "password");

      Arrays.fill(requestBody, (byte) 0); // Löschen des Byte-Arrays nach Verarbeitung


      logger().info("Received username {}", Arrays.toString(receivedUsername));
      logger().info("Received password {}", Arrays.toString(receivedPassword));

      boolean authenticated = Arrays.equals(receivedUsername, USERNAME) &&
          Arrays.equals(receivedPassword, PASSWORD);

      Arrays.fill(receivedUsername, '\0'); // Löschen der sensiblen Daten
      Arrays.fill(receivedPassword, '\0');
      Arrays.fill(requestBody, (byte) 0);

      String response = authenticated ? "Login erfolgreich" : "Login fehlgeschlagen";
      logger().info(response);

      exchange.sendResponseHeaders(authenticated ? 200 : 401, response.length());
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(response.getBytes(StandardCharsets.UTF_8));
      }
    } else {
      exchange.sendResponseHeaders(405, -1);
    }
  }

}

