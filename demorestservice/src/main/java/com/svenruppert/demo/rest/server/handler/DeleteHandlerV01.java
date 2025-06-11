package com.svenruppert.demo.rest.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.svenruppert.demo.rest.QueryParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.svenruppert.demo.rest.server.SimpleFileRestServerV02.DATA_DIR;

public class DeleteHandlerV01
    implements HttpHandler {


  @Override
  public void handle(HttpExchange exchange)
      throws IOException {
    if (!"DELETE".equalsIgnoreCase(exchange.getRequestMethod())) {
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

    Files.delete(filePath);
    String response = "Deleted: " + name;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.close();
  }


}