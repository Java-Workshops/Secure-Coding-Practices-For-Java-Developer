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

public class SimpleFileRestClientV03
    implements SimpleFileRestClient {

  private static final String SERVER = "http://localhost:8080";


  public void uploadFile(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    if (!Files.exists(path)) {
      System.out.println("File not found: " + filePath);
      return;
    }

    long fileSize = Files.size(path);
    long maxHeap = Runtime.getRuntime().maxMemory();
    long bufferSize = Math.max(1024 * 1024, maxHeap / 20); // mindestens 1 MB
    int chunkSize = (int) Math.min(bufferSize, Integer.MAX_VALUE); // maximal 2 GB Puffer

    System.out.printf("Heap max: %.2f MB → Upload buffer: %.2f MB%n",
                      maxHeap / (1024.0 * 1024.0), chunkSize / (1024.0 * 1024.0));

    HttpURLConnection conn = (HttpURLConnection) URI.create(SERVER + "/upload").toURL().openConnection();
    conn.setDoOutput(true);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("X-Filename", path.getFileName().toString());
    conn.setFixedLengthStreamingMode(fileSize);

    try (OutputStream os = conn.getOutputStream();
         InputStream is = Files.newInputStream(path)) {

      byte[] buffer = new byte[chunkSize];
      int bytesRead;
      long total = 0;

      while ((bytesRead = is.read(buffer)) != -1) {
        os.write(buffer, 0, bytesRead);
        total += bytesRead;
        System.out.printf("Sent %.2f MB...\n", total / (1024.0 * 1024.0));
      }
    }

    int responseCode = conn.getResponseCode();

    if (responseCode == 413) {
      System.out.println("Upload failed: file too large (server responded with 413)");
      try (InputStream err = conn.getErrorStream()) {
        if (err != null) {
          System.out.println(new String(err.readAllBytes()));
        }
      }
      return;
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