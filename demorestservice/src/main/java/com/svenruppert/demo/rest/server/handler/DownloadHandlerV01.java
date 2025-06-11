package com.svenruppert.demo.rest.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.svenruppert.demo.rest.QueryParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.svenruppert.demo.rest.server.SimpleFileRestServerV02.DATA_DIR;

public class DownloadHandlerV01
    implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange)
      throws IOException {
    if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1);
      return;
    }

    Map<String, String> query = new QueryParser().parseQuery(exchange.getRequestURI().getRawQuery());
    String name = query.get("name");
    if (name == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

    Path filePath = DATA_DIR.resolve(name).normalize();
    if (!Files.exists(filePath) || !filePath.startsWith(DATA_DIR)) {
      exchange.sendResponseHeaders(404, -1);
      return;
    }

    exchange.getResponseHeaders().add("Content-Type", "application/octet-stream");
    exchange.sendResponseHeaders(200, Files.size(filePath));
    try (InputStream is = Files.newInputStream(filePath)) {
      is.transferTo(exchange.getResponseBody());
    }
    exchange.close();
  }
}