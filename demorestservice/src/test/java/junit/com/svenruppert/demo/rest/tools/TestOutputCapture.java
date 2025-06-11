package junit.com.svenruppert.demo.rest.tools;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

// Hilfsklasse für Konsolenausgabe
public class TestOutputCapture {
  private ByteArrayOutputStream out;

  public void capture(Runnable runnable) {
    PrintStream originalOut = System.out;
    out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    try {
      runnable.run();
    } finally {
      System.setOut(originalOut);
    }
  }

  public String getOutput() {
    return out.toString();
  }
}