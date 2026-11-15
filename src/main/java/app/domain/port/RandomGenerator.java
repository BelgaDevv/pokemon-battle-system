package app.domain.port;

/**
 * Defines the contract for generating random values
 * used by the battle system.
 */

public interface RandomGenerator {

  int nextInt(int bound);

  double nextDouble();
}