package com.svenruppert.securecoding.authentication.rest.demo01.v01;

import com.sun.net.httpserver.HttpServer;
import com.svenruppert.dependencies.core.logger.HasLogger;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RestServiceV1
implements HasLogger {

  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.createContext("/login", new LoginHandlerV1());
    server.setExecutor(null);
    System.out.println("Server started on port 8080");
    server.start();
  }
}
