package com.svenruppert.demo.rest.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.svenruppert.demo.rest.server.SimpleFileRestServerV02.DATA_DIR;

public class UploadHandlerV03 implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1);
      return;
    }

    var requestHeaders = exchange.getRequestHeaders();
    var xFileName = requestHeaders.getFirst("X-Filename");
    String fileName = Optional
        .ofNullable(xFileName)
        .orElse("unknown_" + System.currentTimeMillis());

    fileName = Paths.get(fileName).getFileName().toString(); // sanitize
    Path filePath = DATA_DIR.resolve(fileName);

    long maxBytes = 500_000_000L; // 500 MB
    long total = 0;
    byte[] buffer = new byte[16 * 1024];

    try (InputStream is = exchange.getRequestBody();
         OutputStream os = Files.newOutputStream(filePath)) {

      int bytesRead;
      while ((bytesRead = is.read(buffer)) != -1) {
        total += bytesRead;

        if (total > maxBytes) {
          os.close();
          Files.deleteIfExists(filePath);
          String response = "Upload rejected: file too large.";
          exchange.sendResponseHeaders(413, response.length());
          exchange.getResponseBody().write(response.getBytes());
          exchange.close();
          return;
        }

        os.write(buffer, 0, bytesRead);
      }
    }

    String response = "Uploaded: " + fileName;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.close();
  }
}