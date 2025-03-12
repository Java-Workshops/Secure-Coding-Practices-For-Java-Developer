package com.svenruppert.securecoding.jca.demo01;


public class CommunicationDemo {


  public static void main(String[] args) throws Exception {

    SecureServer secureServer = new SecureServer();
    secureServer.start();

    SecureClient client = new SecureClient();
    client.initConn();

    client.sendEncryptedMessage("### Hello World ###");
    client.sendEncryptedMessage("### and more messages ###");

    secureServer.stop();
  }

}
