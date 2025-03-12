package com.svenruppert.securecoding.jca.demo01;

import com.svenruppert.dependencies.core.logger.HasLogger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class SecureClient implements HasLogger {

  private static final String SERVER_URL = "http://localhost:8080";
  private static PublicKey serverPublicKey;
  private static SecretKey aesKey;

  public void initConn() throws Exception {
    serverPublicKey = fetchServerPublicKey();
    aesKey = generateAESKey();
    sendEncryptedAESKey();
  }

  private PublicKey fetchServerPublicKey() throws Exception {
    logger().info("Öffentlichen Schlüssel vom Server abrufen...");
    URL url = URI.create(SERVER_URL + "/publicKey").toURL();
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    byte[] keyBytes = conn.getInputStream().readAllBytes();
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
  }

  private SecretKey generateAESKey() throws NoSuchAlgorithmException {
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(128);
    return keyGen.generateKey();
  }

  private void sendEncryptedAESKey() throws Exception {
    logger().info("Verschlüsselten AES-Schlüssel an den Server senden...");
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
    byte[] encryptedKey = cipher.doFinal(aesKey.getEncoded());
    sendPostRequest("/exchangeKey", encryptedKey);
  }

  public void sendEncryptedMessage(String message) throws Exception {
    logger().info("Nachricht an den Server senden: " + message);
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, aesKey);
    byte[] encryptedMessage = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
    logger().info("verschlüsselte Nachricht an den Server senden: " + new String(encryptedMessage));
    sendPostRequest("/secureMessage", encryptedMessage);
  }

  private void sendPostRequest(String endpoint, byte[] data) throws Exception {
    URL url = URI.create(SERVER_URL + endpoint).toURL();
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST");
    conn.setDoOutput(true);
    conn.getOutputStream().write(data);
    conn.getOutputStream().flush();
    conn.getOutputStream().close();
    conn.getResponseCode(); // Warten auf Antwort
    conn.disconnect();
  }

}
