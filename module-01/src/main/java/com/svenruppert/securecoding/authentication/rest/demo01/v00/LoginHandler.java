package com.svenruppert.securecoding.authentication.rest.demo01.v00;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.svenruppert.dependencies.core.logger.HasLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginHandler
    implements HttpHandler, HasLogger {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if ("POST".equals(exchange.getRequestMethod())) {
      InputStream requestBody = exchange.getRequestBody();
      String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
      Map<String, String> params = parseParams(body);

      String username = params.get("username");
      String password = params.get("password");

      boolean authenticated = isAuthenticated(username, password);

      String response = authenticated ? "Login erfolgreich" : "Login fehlgeschlagen";
      exchange.sendResponseHeaders(authenticated ? 200 : 401, response.length());
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(response.getBytes(StandardCharsets.UTF_8));
      }
    } else {
      exchange.sendResponseHeaders(405, -1);
    }
  }

  private boolean isAuthenticated(String username, String password) {
    return "admin".equals(username) && "secret".equals(password);
  }

  private Map<String, String> parseParams(String body) {
    Map<String, String> params = new HashMap<>();
    for (String pair : body.split("&")) {
      String[] keyValue = pair.split("=");
      if (keyValue.length == 2) params.put(keyValue[0], keyValue[1]);
    }
    return params;
  }
}
