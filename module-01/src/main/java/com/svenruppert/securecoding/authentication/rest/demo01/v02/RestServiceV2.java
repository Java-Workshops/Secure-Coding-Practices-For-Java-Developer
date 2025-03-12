package com.svenruppert.securecoding.authentication.rest.demo01.v02;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RestServiceV2 {
  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.createContext("/login", new LoginHandlerV2());
    server.setExecutor(null);
    System.out.println("Server started on port 8080");
    server.start();
  }
}
