package app.infrastructure.random;

import app.domain.port.RandomGenerator;

/**
 * Provides random values for battle operations using
 * Java's default random generator.
 */
public class RandomGeneratorImpl implements RandomGenerator {

  private final java.util.random.RandomGenerator delegate;

  public RandomGeneratorImpl() {
    this.delegate = java.util.random.RandomGenerator.getDefault();
  }

  @Override
  public int nextInt(int bound) {
    return delegate.nextInt(bound);
  }

  @Override
  public double nextDouble() {
    return delegate.nextDouble();
  }

  public long nextLong() {
    return delegate.nextLong();
  }
}