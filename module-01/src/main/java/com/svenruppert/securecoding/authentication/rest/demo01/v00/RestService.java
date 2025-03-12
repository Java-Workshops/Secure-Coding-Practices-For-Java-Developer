package com.svenruppert.securecoding.authentication.rest.demo01.v00;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RestService {
  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    HttpHandler httpHandler = new LoginHandler();
    server.createContext("/login", httpHandler);
    server.start();
    System.out.println("Server started on port 8080");
  }

}
