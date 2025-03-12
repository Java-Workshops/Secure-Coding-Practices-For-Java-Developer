package com.svenruppert.securecoding.authentication.rest.demo01.v00;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RestClient {
  public static void main(String[] args) throws IOException {
    String data = "username=admin&password=secret";
    byte[] postData = data.getBytes(StandardCharsets.UTF_8);

    URI uri = URI.create("http://localhost:8080/login");
    URL url = uri.toURL();
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    conn.setDoOutput(true);

    try (OutputStream os = conn.getOutputStream()) {
      os.write(postData);
    }

    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(
            conn.getInputStream(),
            StandardCharsets.UTF_8))) {
      System.out.println("Response: " + br.readLine());
    }
  }
}
