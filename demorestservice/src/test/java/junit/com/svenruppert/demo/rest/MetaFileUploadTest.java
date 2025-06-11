package junit.com.svenruppert.demo.rest;

import com.svenruppert.demo.rest.client.SimpleFileRestClientV04;
import com.svenruppert.demo.rest.server.SimpleFileRestServerV04;
import junit.com.svenruppert.demo.rest.tools.TestOutputCapture;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MetaFileUploadTest {

  private static final Path TEST_FILE = Paths.get("upload_meta_test.txt");
  private static final String TEST_CONTENT = "Dies ist eine Testdatei für Metadaten.";
  private static final Path DATA_DIR = Paths.get("data");

  private static final SimpleFileRestClientV04 client = new SimpleFileRestClientV04();
  private static final SimpleFileRestServerV04 server = new SimpleFileRestServerV04();
  private String uploadedServerFileName;

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

  @BeforeEach
  void beforeEach()
      throws IOException {
    Files.writeString(TEST_FILE, TEST_CONTENT, UTF_8);
  }

  @AfterEach
  void cleanupFiles()
      throws IOException {
    Files.deleteIfExists(TEST_FILE);
    if (uploadedServerFileName != null) {
      Files.deleteIfExists(DATA_DIR.resolve(uploadedServerFileName));
      Files.deleteIfExists(DATA_DIR.resolve(uploadedServerFileName + ".meta"));
    }
  }

  @Test
  void testMetaFileCreatedAndValid()
      throws IOException {
    // Upload durchführen
    TestOutputCapture capture = new TestOutputCapture();
    capture.capture(() -> {
      try {
        client.uploadFile(TEST_FILE.toString());
      } catch (IOException e) {
        fail("Upload failed: " + e.getMessage());
      }
    });

    // Extrahiere vom Server zugewiesenen Dateinamen
    String output = capture.getOutput();
    Pattern pattern = Pattern.compile("File was stored on server as: (.+)");
    Matcher matcher = pattern.matcher(output);
    assertTrue(matcher.find(), "Response must contain uploaded filename");
    uploadedServerFileName = matcher.group(1).trim();

    Path uploadedFile = DATA_DIR.resolve(uploadedServerFileName);
    Path metaFile = DATA_DIR.resolve(uploadedServerFileName + ".meta");

    // Check: Datei und .meta-Datei existieren
    assertTrue(Files.exists(uploadedFile), "Uploaded file must exist");
    assertTrue(Files.exists(metaFile), "Meta file must exist");

    // Inhalt prüfen
    String metaJson = Files.readString(metaFile);
    assertTrue(metaJson.contains("\"originalName\": \"" + TEST_FILE.getFileName()), "Meta must contain original name");
    assertTrue(metaJson.contains("\"storedName\": \"" + uploadedServerFileName + "\""), "Meta must contain stored name");
    assertTrue(metaJson.contains("\"uploadTime\": "), "Meta must contain upload time");
    assertTrue(metaJson.contains("\"sizeBytes\": " + TEST_CONTENT.getBytes(UTF_8).length), "Meta must contain file size");
  }



}