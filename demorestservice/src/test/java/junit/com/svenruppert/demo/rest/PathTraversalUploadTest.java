package junit.com.svenruppert.demo.rest;

import com.svenruppert.demo.rest.server.SimpleFileRestServerV04;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PathTraversalUploadTest {
  private static final SimpleFileRestServerV04 server = new SimpleFileRestServerV04();
  private static final Path DANGER_FILE = Paths.get("outside_target.txt"); // außerhalb von /data
  private static final String SERVER = "http://localhost:8080";

  @BeforeAll
  static void beforeAll()
      throws IOException, InterruptedException {
    server.init();
    // Kleiner Delay, um Server starten zu lassen
    Thread.sleep(500);
  }

  @AfterAll
  static void afterAll() {
    server.shutdown();
  }

  @Test
  void testPathTraversalBlocked(TestReporter reporter)
      throws IOException {
    // Testdaten
    String[] maliciousNames = {
        "../outside_target.txt",
        "..\\outside_target.txt",
        "../../etc/passwd",
        "..%2F..%2Foutside_target.txt",  // encoded traversal
        "..%5C..%5Coutside_target.txt"   // encoded backslash
    };

    for (String attackName : maliciousNames) {
      reporter.publishEntry("try to upload.. " + attackName);
      // Upload mit manipuliertem Dateinamen
      HttpURLConnection conn = (HttpURLConnection) URI.create(SERVER + "/upload").toURL().openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("X-Filename", attackName);
      byte[] content = "Malicious content".getBytes();
      conn.setFixedLengthStreamingMode(content.length);

      try (OutputStream os = conn.getOutputStream()) {
        os.write(content);
      }

      int responseCode = conn.getResponseCode();
      assertEquals(403, responseCode, "Server must reject path traversal with 403 Forbidden for: " + attackName);
    }

    // Prüfen, ob die Datei NICHT außerhalb geschrieben wurde
    assertFalse(Files.exists(DANGER_FILE), "Traversal must not write outside file");
  }

  @AfterEach
  void cleanup()
      throws IOException {
    Files.deleteIfExists(DANGER_FILE); // falls doch geschrieben wurde
  }
}