package com.svenruppert.securecoding.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class SimpleLoggingAgent {

  private SimpleLoggingAgent() {
  }

  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("[Agent] Initializing agent with arguments: " + agentArgs);
//    inst.addTransformer(new LoggingTransformer(), true);
    inst.addTransformer(new LoggingTransformer());
  }

  static class LoggingTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(Module module, ClassLoader loader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {
      if (className != null && className.contains("LicenseRestServer$LicenseHandler")) {
        System.out.println("[Agent] Transforming class: " + className);
        // Hier könnte man echten Bytecode verändern (z.B. mit ASM)
        // Im Sinne dieses Beispiels geben wir den Original-Bytecode zurück.
      }
      return classfileBuffer;
    }
  }

}
