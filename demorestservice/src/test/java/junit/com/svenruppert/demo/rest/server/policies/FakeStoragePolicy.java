package junit.com.svenruppert.demo.rest.server.policies;

import com.svenruppert.demo.rest.server.policies.StoragePolicy;

public class FakeStoragePolicy
    implements StoragePolicy {
  private final boolean result;

  public FakeStoragePolicy(boolean result) {
    this.result = result;
  }

  @Override
  public boolean hasEnoughSpace(long requiredBytes) {
    return result;
  }
}