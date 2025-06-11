package com.svenruppert.demo.rest.server.policies;

public interface StoragePolicy {
  boolean hasEnoughSpace(long requiredBytes);
}