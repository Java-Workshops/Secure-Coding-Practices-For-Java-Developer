package com.svenruppert.securecoding.inputvalidation.v02;

import com.svenruppert.dependencies.core.logger.HasLogger;
import io.javalin.Javalin;

public class RestService
implements HasLogger {

  public static final int DEFAULT_PORT = 7070;
  private final Javalin service;
  private final int port;


  public RestService() {
    this.port = DEFAULT_PORT;
    service = initService();
  }

  public RestService(int port) {
    this.port = port;
    service = initService();
  }

  public Javalin getService() {
    return service;
  }

  public Javalin startService() {
    return service.start(port);
  }

  public Javalin startService(int port) {
    return service.start(port);
  }

  private Javalin initService() {
    logger().info("Initializing REST service");
    return Javalin.create(/*config*/)
        .get("/", ctx -> ctx.result("Hello World"))
        .get("/validate/isbn/{value}", ctx -> {
          ctx.status(400).json("Ungültige ISBN-Nummer");
        })
        .get("/booking/{isbn}/{amount}", ctx -> {
          ctx.status(400).json("Ungültige ISBN-Nummer");
        })
        .get("/upper/{value}/{name}", ctx -> {
          String value = ctx.pathParam("value");
          String name = ctx.pathParam("name");
          ctx.result(new UpperCaseService()
              .toUpperCase(value + "-" + name));
        });
  }


}
