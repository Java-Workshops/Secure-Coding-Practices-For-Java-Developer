package junit.com.svenruppert.demo.rest;

import com.svenruppert.demo.rest.client.SimpleFileRestClient;
import com.svenruppert.demo.rest.client.SimpleFileRestClientV03;
import com.svenruppert.demo.rest.server.SimpleFileRestServer;
import com.svenruppert.demo.rest.server.SimpleFileRestServerV03;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static junit.com.svenruppert.demo.rest.tools.LargeTestFileGenerator.generateLargeFile;
import static org.junit.jupiter.api.Assertions.*;

public class LargeFileUploadV03Test {

  private static final Path LARGE_FILE = Paths.get("large_test_file.bin");
//  private static final long FILE_SIZE_BYTES = 1_000_000_000L; // 10 GB
    private static final long FILE_SIZE_BYTES = 500_000_000L; // 500 MB - OK
  private static final String SERVER_FILE_NAME = "large_test_file.bin";

  private static final SimpleFileRestServer server = new SimpleFileRestServerV03();
  private final SimpleFileRestClient simpleFileRestClient = new SimpleFileRestClientV03();

  @BeforeAll
  static void beforeAll()
      throws IOException, InterruptedException {
    generateLargeFile(LARGE_FILE, FILE_SIZE_BYTES);
    server.init();
    // Kleiner Delay, um Server starten zu lassen
    Thread.sleep(500);
  }

  @AfterAll
  static void cleanup()
      throws IOException {
    Files.deleteIfExists(LARGE_FILE);
    Files.deleteIfExists(Paths.get("data", SERVER_FILE_NAME));
    server.shutdown();
  }

  @Test
  void testLargeFileUpload() {
    long start = System.currentTimeMillis();
    try {

      simpleFileRestClient.uploadFile(LARGE_FILE.toString());



      Path uploaded = Paths.get("data", SERVER_FILE_NAME);
      assertTrue(Files.exists(uploaded), "Uploaded large file must exist");

      long uploadedSize = Files.size(uploaded);
      assertEquals(FILE_SIZE_BYTES, uploadedSize, "Uploaded file size mismatch - expected: " + FILE_SIZE_BYTES + ", actual: " + uploadedSize);

    } catch (IOException e) {
      fail("Upload failed with exception: " + e.getMessage());
    } finally {
      long duration = System.currentTimeMillis() - start;
      System.out.println("Upload duration: " + (duration / 1000.0) + "s");
    }
  }
}