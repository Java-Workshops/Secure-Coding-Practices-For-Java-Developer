package com.svenruppert.demo.rest.server;

import com.sun.net.httpserver.HttpServer;
import com.svenruppert.demo.rest.server.handler.DeleteHandlerV01;
import com.svenruppert.demo.rest.server.handler.DownloadHandlerV01;
import com.svenruppert.demo.rest.server.handler.ListHandlerV01;
import com.svenruppert.demo.rest.server.handler.UploadHandlerV03;

public class SimpleFileRestServerV03
    extends SimpleFileRestServer {


  protected void createContext(HttpServer server) {
    server.createContext("/upload", new UploadHandlerV03());
    server.createContext("/download", new DownloadHandlerV01());
    server.createContext("/list", new ListHandlerV01());
    server.createContext("/delete", new DeleteHandlerV01());
  }
}