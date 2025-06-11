package com.svenruppert.demo.rest.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public interface SimpleFileRestClient {
  void uploadFile(String filePath)
      throws IOException;

  void downloadFile(String fileName)
      throws IOException;

  void listFiles()
      throws IOException;

  void deleteFile(String fileName)
      throws IOException;

  default void printResponse(HttpURLConnection conn)
      throws IOException {
    int code = conn.getResponseCode();
    System.out.println("Response Code: " + code);

    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(code >= 400 ? conn.getErrorStream() : conn.getInputStream()))) {
      reader.lines().forEach(System.out::println);
    }
  }
}