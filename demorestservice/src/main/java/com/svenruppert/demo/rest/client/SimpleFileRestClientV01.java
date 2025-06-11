package com.svenruppert.demo.rest.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.Scanner;

public class SimpleFileRestClientV01
    implements SimpleFileRestClient {

  private static final String SERVER = "http://localhost:8080";

  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);
    SimpleFileRestClientV01 client = new SimpleFileRestClientV01();

    while (true) {
      System.out.println("""
                    \n--- FILE CLIENT MENU ---
                    1 = Upload file
                    2 = Download file
                    3 = List files
                    4 = Delete file
                    0 = Exit
                    -------------------------
                    """);
      System.out.print("Choice: ");
      String choice = scanner.nextLine();

      switch (choice) {
        case "1" -> {
          System.out.print("Path to file to upload: ");
          String path = scanner.nextLine();
          client.uploadFile(path);
        }
        case "2" -> {
          System.out.print("Name of file to download: ");
          String fileName = scanner.nextLine();
          client.downloadFile(fileName);
        }
        case "3" -> client.listFiles();
        case "4" -> {
          System.out.print("Name of file to delete: ");
          String fileName = scanner.nextLine();
          client.deleteFile(fileName);
        }
        case "0" -> {
          System.out.println("Bye!");
          return;
        }
        default -> System.out.println("Invalid choice.");
      }
    }
  }

  @Override
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

    try (OutputStream os = conn.getOutputStream();
         InputStream is = Files.newInputStream(path)) {
      is.transferTo(os);
    }

    printResponse(conn);
  }

  @Override
  public void downloadFile(String fileName) throws IOException {
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

  @Override
  public void listFiles() throws IOException {
    HttpURLConnection conn = (HttpURLConnection) URI.create(SERVER + "/list").toURL().openConnection();
    conn.setRequestMethod("GET");
    printResponse(conn);
  }

  @Override
  public void deleteFile(String fileName) throws IOException {
    URL url = URI.create(SERVER + "/delete?name=" + URI.create(fileName).toASCIIString()).toURL();
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("DELETE");
    printResponse(conn);
  }

}