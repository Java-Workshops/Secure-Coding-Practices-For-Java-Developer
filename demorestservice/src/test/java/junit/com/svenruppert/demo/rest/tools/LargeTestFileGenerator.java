package junit.com.svenruppert.demo.rest.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LargeTestFileGenerator {


  public static void generateLargeFile(Path largeFile, long fileSizeBytes)
      throws IOException {

    if (Files.exists(largeFile) && Files.size(largeFile) == fileSizeBytes) {
      return;
    }

    System.out.println("Generating test data  file...");
    try (var out = Files.newOutputStream(largeFile)) {
      byte[] buffer = new byte[1024 * 1024]; // 1 MB
      long written = 0;

      while (written < fileSizeBytes) {
        int toWrite = (int) Math.min(buffer.length, fileSizeBytes - written);
        out.write(buffer, 0, toWrite);
        written += toWrite;
      }
    }

    long finalSize = Files.size(largeFile);
    assertEquals(fileSizeBytes, finalSize, "Generated file must be exactly 1 GB");
  }
}