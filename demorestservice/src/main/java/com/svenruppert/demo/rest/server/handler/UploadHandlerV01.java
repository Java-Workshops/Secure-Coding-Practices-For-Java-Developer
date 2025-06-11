package com.svenruppert.demo.rest.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.svenruppert.demo.rest.server.SimpleFileRestServerV02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class UploadHandlerV01
    implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange)
      throws IOException {
    if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1); // Method Not Allowed
      return;
    }

    String fileName = Optional.ofNullable(exchange.getRequestHeaders().getFirst("X-Filename"))
        .orElse("unknown_" + System.currentTimeMillis());

    fileName = Paths.get(fileName).getFileName().toString(); // sanitize
    Path filePath = SimpleFileRestServerV02.DATA_DIR.resolve(fileName);

    try (InputStream is = exchange.getRequestBody();
         OutputStream os = Files.newOutputStream(filePath)) {
      is.transferTo(os);
    }

    String response = "Uploaded: " + fileName;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.close();
  }
}