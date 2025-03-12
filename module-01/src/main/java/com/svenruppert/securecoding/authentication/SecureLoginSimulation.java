package com.svenruppert.securecoding.authentication;

import com.svenruppert.dependencies.core.logger.HasLogger;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SecureLoginSimulation
    implements HasLogger {

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  private static char[] concat(char[] a, char[] b) {
    char[] c = new char[a.length + b.length];
    System.arraycopy(a, 0, c, 0, a.length);
    System.arraycopy(b, 0, c, a.length, b.length);
    return c;
  }

  private static byte[] toByte(char[] input){
    ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(CharBuffer.wrap(input));
    byte[] output = new byte[byteBuffer.remaining()];
    byteBuffer.get(output);
    return output;
  }

  public static void main(String[] args) {
    Server server = new Server();
    Client client = new Client();

    SecureLoginSimulation simulation = new SecureLoginSimulation();
    simulation.simulateRegistration(server, client);
    simulation.simulateLoginProcess(server, client);
  }

  public void simulateRegistration(Server server, Client client) {
    char[] userName = "sven.ruppert".toCharArray();
    char[] staticSaltForRegistration = server.requestStaticSalt(userName);
    char[] passwordForRegistration = "secret".toCharArray();
    char[] hashedPasswordWithStaticSalt = client.hash(concat(passwordForRegistration , staticSaltForRegistration));
    server.registerUser(userName, hashedPasswordWithStaticSalt);
  }

  public void simulateLoginProcess(Server server, Client client) {
    char[] userName = "sven.ruppert".toCharArray();
    char[] passwordForLogin = "secrete".toCharArray();
    char[] staticSalt = server.requestStaticSalt(userName);
    char[] hashedPasswordWithStaticSalt = client.hash(concat(passwordForLogin , staticSalt));
    char[] dynamicSalt = server.requestDynamicSalt(userName);
    char[] hashed = client.hash(concat(hashedPasswordWithStaticSalt , dynamicSalt));
    boolean authenticated = server.authenticate(userName, hashed);
    if (authenticated) {
      logger().info("Login is ok");
    } else {
      logger().info("Login failed");
    }

  }

  public static class HashGenerator {
    public char[] hash(char[] input) {
      try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(toByte(input));
        return Base64.getEncoder().encodeToString(hashBytes).toCharArray();
      } catch (Exception e) {
        throw new RuntimeException("Fehler beim Hashing", e);
      }
    }
  }

  public static class Client implements HasLogger {

    public char[] hash(char[] input) {
      return new HashGenerator().hash(input);
    }
  }

  public static class Server implements HasLogger {

    private static final char[] SECRET_PEPPER = "top-secret-pepper".toCharArray();

    private static final Map<char[], Login> loginDatabase = new HashMap<>();
    private static final Map<char[], char[]> registrationSalts = new HashMap<>();
    private static final Map<char[], char[]> dynamicSalts = new HashMap<>();


    private char[] generateSalt() {
      byte[] salt = new byte[16];  // 16 Bytes (128 Bit)
      SECURE_RANDOM.nextBytes(salt);
      return Base64.getEncoder().encodeToString(salt).toCharArray();
    }

    private char[] hash(char[] input) {
      return new HashGenerator().hash(input);
    }

    public char[] requestDynamicSalt(char[] userName) {
      logger().info("Requesting dynamic salt for {}", userName);
      if (loginDatabase.containsKey(userName)) {
        logger().info("login found for {}", userName);
        char[] dynamicSaltForLogin = generateSalt();
        logger().info("Generated for user {} dynamic salt {}", userName, dynamicSaltForLogin);
        dynamicSalts.put(userName, dynamicSaltForLogin);
        return dynamicSaltForLogin;
      } else {
        throw new RuntimeException("User " + Arrays.toString(userName) + " not found");
      }
    }

    public char[] requestStaticSalt(char[] userName) {
      logger().info("Requesting static salt for {}", userName);
      if (loginDatabase.containsKey(userName)) {
        logger().info("userName2LoginMap.contains salt for {}", userName);
        char[] staticSalt = loginDatabase.get(userName).staticSalt();
        logger().info("Static salt for {} is {}", userName, staticSalt);
        return staticSalt;
      } else {
        logger().info("userName2LoginMap.contains NO salt for {}", userName);
        char[] generatedSalt = generateSalt();
        logger().info("Generated salt for {} is {}", userName, generatedSalt);
        registrationSalts.put(userName, generatedSalt);
        return generatedSalt;
      }
    }

    public void registerUser(char[] userName, char[] hashed) {
      if (loginDatabase.containsKey(userName)) {
        throw new IllegalStateException("User already registered: " + Arrays.toString(userName));
      } else if (registrationSalts.containsKey(userName)) {
        logger().info("registrationSalts : {}", userName);
        //client-hash   -> hash(hash(password + staticSalt))
        //server-hash   -> hash(hash(password + staticSalt)) - store without Pepper
        logger().info("hashedPasswordWithStaticSalt : {}", hashed);
        Login login = new Login(
            userName,
            hashed,
            registrationSalts.get(userName));
        logger().info("userName2LoginMap.put {}", login);
        loginDatabase.put(userName, login);
        logger().info("remove user {} from registrationSalts", userName);
        registrationSalts.remove(userName);
      } else {
        throw new IllegalStateException("User can not register: " + Arrays.toString(userName));
      }
    }

    public boolean authenticate(char[] userName, char[] hashedPassword) {
      logger().info("Authenticating user {} with passwordHash {} ", userName , hashedPassword);
      Login login = loginDatabase.get(userName);
      if (login == null) {
        logger().info("User {} not found", userName);
        return false;
      }

      char[] dynamicSaltForLogin = dynamicSalts.get(userName);
      logger().info("DynamicSalt for {} is {}", userName, dynamicSaltForLogin);
      if (dynamicSaltForLogin == null) {
        return false;
      }
      //client-hash - received  -> hash(hash(password + staticSalt) + dynamicSalt))
      //client-hash - toCompare -> hash(received + pepper)
      //server-hash             -> hash(hash(storedHash + dynamicSalt) + pepper)
      //server-side storedHash  ->
      //server-hash             -> hash(hash(storedHash + dynamicSalt) + pepper)

      char[] passwordHashStored = login.passwordHash();
      char[] hashedServer = hash(concat(hash(concat(passwordHashStored, dynamicSaltForLogin)), SECRET_PEPPER));
      char[] hashedClient = hash(concat(hashedPassword, SECRET_PEPPER));

      logger().info("serverside hashed {} is {}", userName, hashedServer);
      logger().info("clientside hashed {} is {}", userName, hashedClient);
      return Arrays.equals(hashedServer, hashedClient);
    }

    public record Login(char[] username, char[] passwordHash, char[] staticSalt) {
    }


  }
}





