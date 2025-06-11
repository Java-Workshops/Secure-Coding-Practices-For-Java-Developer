package com.svenruppert.demo.rest.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.svenruppert.demo.rest.server.SimpleFileRestServerV02;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ListHandlerV01
    implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange)
      throws IOException {
    if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1);
      return;
    }

    StringBuilder response = new StringBuilder();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(SimpleFileRestServerV02.DATA_DIR)) {
      for (Path file : stream) {
        if (Files.isRegularFile(file)) {
          response.append(file.getFileName())
              .append(" (")
              .append(Files.size(file))
              .append(" bytes)\n");
        }
      }
    }

    byte[] bytes = response.toString().getBytes();
    exchange.sendResponseHeaders(200, bytes.length);
    exchange.getResponseBody().write(bytes);
    exchange.close();
  }
}