package com.svenruppert.demo.rest.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.svenruppert.demo.rest.server.SimpleFileRestServer.DATA_DIR;

public class UploadHandlerV04
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

    String originalName = Optional.ofNullable(xFileName)
        .orElse("unknown_" + System.currentTimeMillis());

    // Traversal Angriffe abfangen
    if (!originalName.matches("^[a-zA-Z0-9_.-]{1,100}$")) {
      String msg = "Rejected: suspicious filename.";
      exchange.sendResponseHeaders(403, msg.length());
      exchange.getResponseBody().write(msg.getBytes());
      exchange.close();
      return;
    }

    // Nur den Dateinamen extrahieren, keine Pfadangaben übernehmen
    String safeOriginalName = Paths.get(originalName).getFileName().toString();

    // Erweiterung extrahieren
    String extension = getFileExtension(safeOriginalName);
    String uuid = UUID.randomUUID().toString();

    // Neuer Dateiname
    String storedFileName = uuid + (extension.isEmpty() ? "" : "." + extension);
    Path filePath = DATA_DIR.resolve(storedFileName).normalize();

    // Verzeichnisgrenze absichern
    if (!filePath.startsWith(DATA_DIR)) {
      exchange.sendResponseHeaders(403, -1); // Forbidden
      return;
    }

    // Upload begrenzen
    long maxBytes = 500_000_000L; // 500 MB
    long total = 0;
    byte[] buffer = new byte[16 * 1024];

    // Verfügbaren Speicherplatz prüfen
    long usableSpace = Files.getFileStore(DATA_DIR).getUsableSpace();

    if (usableSpace < maxBytes + 10 * 1024 * 1024) { // +10MB Puffer für Meta etc.
      String response = "Upload rejected: insufficient disk space.";
      exchange.sendResponseHeaders(507, response.length()); // 507 Insufficient Storage
      exchange.getResponseBody().write(response.getBytes());
      exchange.close();
      return;
    }

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

    // Metadaten-Datei erstellen
    Path metaFile = filePath.resolveSibling(filePath.getFileName().toString() + ".meta");

    String metaJson = """
        {
          "originalName": "%s",
          "storedName": "%s",
          "uploadTime": "%s",
          "sizeBytes": %d
        }
        """.formatted(
        escapeJson(safeOriginalName),
        escapeJson(storedFileName),
        Instant.now().toString(),
        total
    );

    Files.writeString(metaFile, metaJson, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);

    // Antwort mit dem tatsächlichen Dateinamen auf dem Server
    String response = "Uploaded as: " + storedFileName;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.close();
  }

  private String getFileExtension(String fileName) {
    int dot = fileName.lastIndexOf('.');
    return (dot >= 0 && dot < fileName.length() - 1)
        ? fileName.substring(dot + 1)
        : "";
  }

  private String escapeJson(String input) {
    return input.replace("\\", "\\\\").replace("\"", "\\\"");
  }
}