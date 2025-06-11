package com.svenruppert.securecoding.sqlinjection.v01;

import java.util.Scanner;

public class SQLInjectionDemo {
  public static void main(String[] args) {
    Database db = new Database();
    db.addUser("admin", "adminadmin");

    Scanner scanner = new Scanner(System.in);

    System.out.print("Benutzername: ");
    String username = scanner.nextLine();

    System.out.print("Passwort: ");
    String password = scanner.nextLine();

    System.out.println("\n[Unsicherer Login-Versuch]");
    boolean insecureLogin = db.insecureLogin(username, password);
    System.out.println("Erfolg: " + insecureLogin);

    System.out.println("\n[Sicherer Login-Versuch]");
    boolean secureLogin = db.secureLogin(username, password);
    System.out.println("Erfolg: " + secureLogin);

    scanner.close();
  }
}