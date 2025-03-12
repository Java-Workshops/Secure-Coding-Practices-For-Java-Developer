package com.svenruppert.securecoding.mutationtesting;

public class Demo {
  public int add(int a, int b) {
    if (a < 2) {
      return (a + b) * -1;
    } else {
      return a + b;
    }
  }
}

