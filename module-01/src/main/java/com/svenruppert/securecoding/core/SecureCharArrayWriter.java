package com.svenruppert.securecoding.core;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.Arrays;

public class SecureCharArrayWriter
    extends CharArrayWriter {

  public SecureCharArrayWriter() {
    super();
  }

  protected void ensureCapacity(int minCapacity) {
    if (minCapacity - buf.length > 0) {
      grow(minCapacity);
    }
  }

  private void grow(int minCapacity) {
    int oldCapacity = buf.length;
    int newCapacity = Math.max(oldCapacity * 2, minCapacity);

    // Neues Array anlegen
    char[] newBuf = new char[newCapacity];

    // Vorhandene Werte übernehmen
    System.arraycopy(buf, 0, newBuf, 0, count);

    // Altes Array mit 0 überschreiben
    Arrays.fill(buf, (char) 0);

    // Referenz auf das neue Array setzen
    buf = newBuf;
  }

  public char[] getBuffer() {
    return buf; // Direkter Zugriff auf das interne Array
  }

  public void clearBuffer() {
    Arrays.fill(buf, (char) 0);
    this.count = 0;
  }

  public static void main(String[] args) throws IOException {
    SecureCharArrayWriter writer = new SecureCharArrayWriter();
    writer.write("Hallo Welt!");
    // Direkter Zugriff auf den internen Puffer
    char[] buffer = writer.getBuffer();
    System.out.println(new String(buffer, 0, writer.size())); // Gibt "Hallo Welt!" aus
    writer.clearBuffer(); // Setzt den Puffer sicher zurück
  }
}
