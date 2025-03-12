package com.svenruppert.securecoding.sqlinjection.v01;

import java.util.ArrayList;
import java.util.List;

public class Database {
  private final List<User> users = new ArrayList<>();

  void addUser(String username, String password) {
    users.add(new User(username, password));
  }

  boolean insecureLogin(String username, String password) {
    String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
    System.out.println("SQL-Query (Simulation): " + query);
    for (User user : users) {
      if (
          (user.username() + "' --").equals(username) || (user.username().equals(username)
              && user.password().equals(password))) {
        return true;
      }
    }
    return false;
  }

  boolean secureLogin(String username, String password) {
    System.out.println("Prepared Statement wird genutzt (Simulation). ");
    return users.stream()
        .anyMatch(user -> user.username().equals(username)
            && user.password().equals(password));
  }
}
