package com.svenruppert.demo.rest.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class SimpleFileRestServer {

  public static final Path DATA_DIR = Paths.get("data");
  private static final int PORT = 8080;
  private HttpServer server;

  public void init()
      throws IOException {
    if (!Files.exists(DATA_DIR)) {
      Files.createDirectories(DATA_DIR);
    }
    server = HttpServer.create(new InetSocketAddress(PORT), 0);

    createContext(server);

    server.setExecutor(null); // default executor
    server.start();
    System.out.println("Server started on http://localhost:" + PORT);
  }

  public void shutdown() {
    server.stop(1);
  }

  protected abstract void createContext(HttpServer server);


}