package com.svenruppert.securecoding.jca.demo01;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.svenruppert.dependencies.core.logger.HasLogger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class SecureServer implements HasLogger {
  private static final int PORT = 8080;
  private static KeyPair rsaKeyPair;
  private static SecretKey aesKey;

  private HttpServer server;

  public void stop() {
    server.stop(2);
  }

  public void start() throws Exception {
    rsaKeyPair = generateRSAKeyPair();
    server = HttpServer.create(new InetSocketAddress(PORT), 0);
    createContextPublicKey();
    createContextExchangeKey();
    createContextSecureMessage();
    server.setExecutor(null);
    server.start();
    logger().info("Server gestartet auf Port " + PORT);
  }

  private void createContextSecureMessage() {
    server.createContext("/secureMessage", (HttpExchange exchange) -> {
      byte[] encryptedMessage = exchange.getRequestBody().readAllBytes();
      String decryptedMessage = decryptAES(encryptedMessage, aesKey);
      logger().info("Entschlüsselte Nachricht: {}", decryptedMessage);
      exchange.sendResponseHeaders(200, 0);
      exchange.close();
    });
  }

  private void createContextExchangeKey() {
    server.createContext("/exchangeKey", (HttpExchange exchange) -> {
      byte[] encryptedKey = exchange.getRequestBody().readAllBytes();
      aesKey = decryptAESKey(encryptedKey);
      logger().info("Sitzungsschlüssel empfangen und entschlüsselt.");
      exchange.sendResponseHeaders(200, 0);
      exchange.close();
    });
  }

  private void createContextPublicKey() {
    server.createContext("/publicKey", (HttpExchange exchange) -> {
      logger().info("Öffentlichen Schlüssel gesendet.");
      byte[] publicKeyBytes = rsaKeyPair.getPublic().getEncoded();
      exchange.sendResponseHeaders(200, publicKeyBytes.length);
      exchange.getResponseBody().write(publicKeyBytes);
      exchange.close();
    });
  }

  private KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(2048);
    return keyGen.generateKeyPair();
  }

  private SecretKey decryptAESKey(byte[] encryptedKey) {
    try {
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, rsaKeyPair.getPrivate());
      byte[] decryptedKeyBytes = cipher.doFinal(encryptedKey);
      return new SecretKeySpec(decryptedKeyBytes, "AES");
    } catch (Exception e) {
      throw new RuntimeException("Fehler beim Entschlüsseln des Sitzungsschlüssels", e);
    }
  }

  private String decryptAES(byte[] encryptedData, SecretKey key) {
    try {
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.DECRYPT_MODE, key);
      byte[] decryptedBytes = cipher.doFinal(encryptedData);
      return new String(decryptedBytes, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("Fehler beim Entschlüsseln der Nachricht", e);
    }
  }
}