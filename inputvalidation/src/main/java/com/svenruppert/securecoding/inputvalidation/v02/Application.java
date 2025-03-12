package com.svenruppert.securecoding.inputvalidation.v02;


public class Application {

  private Application() {
  }

  public static void main(String[] args) {
    RestService restService = new RestService();
    restService.startService(7070);
  }


}
