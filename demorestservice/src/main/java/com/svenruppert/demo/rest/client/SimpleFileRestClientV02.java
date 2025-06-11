package com.svenruppert.demo.rest.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class SimpleFileRestClientV02
    implements SimpleFileRestClient {

  private static final String SERVER = "http://localhost:8080";


  public void uploadFile(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    if (!Files.exists(path)) {
      System.out.println("File not found: " + filePath);
      return;
    }

    HttpURLConnection conn = (HttpURLConnection) URI.create(SERVER + "/upload").toURL().openConnection();
    conn.setDoOutput(true);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("X-Filename", path.getFileName().toString());
    conn.setFixedLengthStreamingMode(Files.size(path)); // verhindert RAM-Puffern

    try (OutputStream os = conn.getOutputStream();
         InputStream is = Files.newInputStream(path)) {

      byte[] buffer = new byte[100 * 1024 * 1024]; // 🔥 100 MB Pakete
      int bytesRead;
      long total = 0;

      while ((bytesRead = is.read(buffer)) != -1) {
        os.write(buffer, 0, bytesRead);
        total += bytesRead;
        System.out.printf("Client Side Sent %.2f MB...\n", total / (1024.0 * 1024.0));
      }
    }

    printResponse(conn);
  }


  public void downloadFile(String fileName)
      throws IOException {
    URL url = URI.create(SERVER + "/download?name=" + URI.create(fileName).toASCIIString()).toURL();
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");

    if (conn.getResponseCode() != 200) {
      printResponse(conn);
      return;
    }

    Path outPath = Paths.get("downloaded_" + fileName);
    try (InputStream in = conn.getInputStream();
         OutputStream out = Files.newOutputStream(outPath)) {
      in.transferTo(out);
    }

    System.out.println("Downloaded to: " + outPath.toAbsolutePath());
  }

  public void listFiles()
      throws IOException {
    HttpURLConnection conn = (HttpURLConnection) URI.create(SERVER + "/list").toURL().openConnection();
    conn.setRequestMethod("GET");
    printResponse(conn);
  }

  public void deleteFile(String fileName)
      throws IOException {
    URL url = URI.create(SERVER + "/delete?name=" + URI.create(fileName).toASCIIString()).toURL();
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("DELETE");
    printResponse(conn);
  }

}