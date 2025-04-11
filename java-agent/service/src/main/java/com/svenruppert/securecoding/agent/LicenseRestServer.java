package com.svenruppert.securecoding.agent;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class LicenseRestServer {
  private LicenseRestServer() {
  }

  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.createContext("/license", new LicenseHandler());
    server.setExecutor(null);
    server.start();
    System.out.println("Server started on port 8080");
  }

  static class LicenseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      if ("GET".equals(exchange.getRequestMethod())) {
        String response = "Current license: DEMO-1234-5678";
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
          os.write(response.getBytes());
        }
      } else {
        exchange.sendResponseHeaders(405, -1); // Method Not Allowed
      }
    }
  }
}
