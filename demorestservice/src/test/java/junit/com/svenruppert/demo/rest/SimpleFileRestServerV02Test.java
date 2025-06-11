package junit.com.svenruppert.demo.rest;

import com.svenruppert.demo.rest.client.SimpleFileRestClient;
import com.svenruppert.demo.rest.client.SimpleFileRestClientV01;
import com.svenruppert.demo.rest.server.SimpleFileRestServer;
import com.svenruppert.demo.rest.server.SimpleFileRestServerV01;
import junit.com.svenruppert.demo.rest.tools.TestOutputCapture;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SimpleFileRestServerV02Test {

  protected static final String TEST_FILE_FOLDER = "data";
  private static final String TEST_FILE_NAME = "test_upload.txt";
  private static final String TEST_FILE_CONTENT = "Dies ist ein Test.";
  private static final Path TEST_FILE_PATH = Paths.get(TEST_FILE_NAME);
  private static final Path DOWNLOADED_FILE = Paths.get("downloaded_" + TEST_FILE_NAME);
  private SimpleFileRestServer server;
  private final SimpleFileRestClient client = new SimpleFileRestClientV01();

  @BeforeEach
  void startServer()
      throws Exception {
    server = new SimpleFileRestServerV01();
    server.init();
    Thread.sleep(500);
  }

  @BeforeEach
  void setupTestFile()
      throws IOException {
    Files.writeString(TEST_FILE_PATH, TEST_FILE_CONTENT);
  }

  @AfterEach
  void tearDown() {
    server.shutdown();
  }

  @AfterEach
  void cleanupLocalFiles()
      throws IOException {
    Files.deleteIfExists(TEST_FILE_PATH);
    Files.deleteIfExists(DOWNLOADED_FILE);
  }

  @Test
  @Order(1)
  void testUploadFile()
      throws IOException {
    client.uploadFile(TEST_FILE_NAME);
    Path uploadedFile = Paths.get(TEST_FILE_FOLDER, TEST_FILE_NAME);
    assertTrue(Files.exists(uploadedFile), "Uploaded file should exist on server.");
  }

  @Test
  @Order(2)
  void testListFiles() {
    // capture output stream
    TestOutputCapture capture = new TestOutputCapture();
    capture.capture(() -> {
      try {
        client.listFiles();
      } catch (IOException e) {
        Assertions.fail(e);
      }
    });

    String output = capture.getOutput();
    assertTrue(output.contains(TEST_FILE_NAME), "List should contain uploaded file.");
  }

  @Test
  @Order(3)
  void testDownloadFile()
      throws IOException {
    client.downloadFile(TEST_FILE_NAME);
    assertTrue(Files.exists(DOWNLOADED_FILE), "Downloaded file should exist.");
    String content = Files.readString(DOWNLOADED_FILE);
    assertEquals(TEST_FILE_CONTENT, content);
  }

  @Test
  @Order(4)
  void testDeleteFile()
      throws IOException {
    client.deleteFile(TEST_FILE_NAME);
    Path uploadedFile = Paths.get(TEST_FILE_FOLDER, TEST_FILE_NAME);
    assertFalse(Files.exists(uploadedFile), "File should be deleted from server.");
  }


}