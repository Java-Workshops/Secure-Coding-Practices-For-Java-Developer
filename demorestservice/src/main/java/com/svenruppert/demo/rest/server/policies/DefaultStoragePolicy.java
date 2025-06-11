package com.svenruppert.demo.rest.server.policies;

import com.svenruppert.dependencies.core.logger.HasLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultStoragePolicy
    implements StoragePolicy , HasLogger {

  private final Path path;
  private final long safetyMarginBytes;

  public DefaultStoragePolicy(Path path, long safetyMarginBytes) {
    this.path = path;
    this.safetyMarginBytes = safetyMarginBytes;
  }

  @Override
  public boolean hasEnoughSpace(long requiredBytes) {
    try {
      long available = Files.getFileStore(path).getUsableSpace();
      return available >= (requiredBytes + safetyMarginBytes);
    } catch (IOException e) {
      return false;
    }
  }
}