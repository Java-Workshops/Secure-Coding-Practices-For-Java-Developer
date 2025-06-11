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

public class UploadHandlerV02
    implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange)
      throws IOException {
    if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1); // Method Not Allowed
      return;
    }

    var requestHeaders = exchange.getRequestHeaders();
    var xFileName = requestHeaders.getFirst("X-Filename");
    String fileName = Optional
        .ofNullable(xFileName)
        .orElse("unknown_" + System.currentTimeMillis());

    fileName = Paths.get(fileName).getFileName().toString(); // sanitize
    Path filePath = SimpleFileRestServerV02.DATA_DIR.resolve(fileName);

    try (InputStream is = exchange.getRequestBody();
         OutputStream os = Files.newOutputStream(filePath)) {

      byte[] buffer = new byte[8192]; // oder 16KB
      int bytesRead;
      long total = 0;
      while ((bytesRead = is.read(buffer)) != -1) {
        os.write(buffer, 0, bytesRead);
        total += bytesRead;
        if (total % (10 * 1024 * 1024) == 0) { // alle 10 MB
          System.out.println("ServerSide Written " + (total / (1024 * 1024)) + " MB...");
        }
      }

    }

    String response = "Uploaded: " + fileName;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.close();
  }
}