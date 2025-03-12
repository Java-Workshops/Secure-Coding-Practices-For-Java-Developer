package com.svenruppert.securecoding.core;

import java.io.CharArrayWriter;
import java.util.Arrays;

public class CharArrayString {
  private final CharArrayWriter writer;

  public CharArrayString() {
    this.writer = new CharArrayWriter();
  }

  public CharArrayString(char[] chars) {
    this.writer = new CharArrayWriter();
    this.writer.write(chars, 0, chars.length);
  }

  public CharArrayString(String str) {
    this.writer = new CharArrayWriter();
    this.writer.write(str, 0, str.length());
  }

  // Gibt die Länge der Zeichenkette zurück
  public int length() {
    return writer.size();
  }

  // Gibt ein Zeichen an einer bestimmten Position zurück
  public char charAt(int index) {
    if (index < 0 || index >= writer.size()) {
      throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
    }
    return writer.toCharArray()[index];
  }

  // Gibt ein Teilstück des CharArrayString zurück
  public CharArrayString substring(int start, int end) {
    if (start < 0 || end > writer.size() || start > end) {
      throw new IndexOutOfBoundsException("Invalid substring range");
    }
    return new CharArrayString(Arrays.copyOfRange(writer.toCharArray(), start, end));
  }

  // Fügt Zeichen oder Strings hinzu
  public void append(char c) {
    writer.write(c);
  }

  public void append(char[] chars) {
    writer.write(chars, 0, chars.length);
  }

  public void append(String str) {
    writer.write(str, 0, str.length());
  }

  // Konvertiert in ein char-Array
  public char[] toCharArray() {
    return writer.toCharArray();
  }

  // Löscht den Speicher (z. B. für sensible Daten)
  public void clear() {
//    char[] array = writer.toCharArray();


    Arrays.fill(array, '\0');  // Überschreibt den Speicherbereich
    writer.reset();  // Leert den Writer-Puffer
  }

  // Vergleicht zwei `CharArrayString`-Objekte
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    CharArrayString other = (CharArrayString) obj;
    return Arrays.equals(this.toCharArray(), other.toCharArray());
  }

  // Hashcode-Berechnung auf Basis des char-Arrays
  @Override
  public int hashCode() {
    return Arrays.hashCode(writer.toCharArray());
  }

  // Debug-Ausgabe, aber ohne String-Pool-Effekte
  public void print() {
    System.out.println(writer.toCharArray());
  }
}

