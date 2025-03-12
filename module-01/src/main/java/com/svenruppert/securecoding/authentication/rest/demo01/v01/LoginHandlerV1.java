package com.svenruppert.securecoding.authentication.rest.demo01.v01;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.svenruppert.dependencies.core.logger.HasLogger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LoginHandlerV1
    implements HttpHandler, HasLogger {
  private static final char[] USERNAME = {'a', 'd', 'm', 'i', 'n'};
  private static final char[] PASSWORD = {'s', 'e', 'c', 'r', 'e', 't'};

  private static char[] extractValue(String body, String key) {
    String prefix = key + "=";
    int start = body.indexOf(prefix);
    if (start == -1) return new char[0];
    start += prefix.length();
    int end = body.indexOf('&', start);
    if (end == -1) end = body.length();
    return body.substring(start, end).toCharArray();
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if ("POST".equals(exchange.getRequestMethod())) {
      byte[] requestBody = exchange.getRequestBody().readAllBytes();
      String body = new String(requestBody, StandardCharsets.UTF_8);

      char[] receivedUsername = extractValue(body, "username");
      char[] receivedPassword = extractValue(body, "password");

      boolean authenticated = Arrays.equals(receivedUsername, USERNAME) &&
          Arrays.equals(receivedPassword, PASSWORD);

      Arrays.fill(receivedUsername, '\0'); // Löschen der sensiblen Daten
      Arrays.fill(receivedPassword, '\0');

      String response = authenticated ? "Login erfolgreich" : "Login fehlgeschlagen";
      exchange.sendResponseHeaders(authenticated ? 200 : 401, response.length());
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(response.getBytes(StandardCharsets.UTF_8));
      }
    } else {
      exchange.sendResponseHeaders(405, -1);
    }
  }
}
