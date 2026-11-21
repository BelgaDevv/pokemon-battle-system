
package app.domain.infrastructure.random;

import app.infrastructure.random.RandomGeneratorImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomGeneratorImplTest {

  // Generates a long value without errors.
  @Test
  void shouldGenerateLongValue() {
    RandomGeneratorImpl generator = new RandomGeneratorImpl();

    assertDoesNotThrow(generator::nextLong);
  }

  // Generates an integer within the specified bound.
  @Test
  void shouldGenerateIntegerWithinBound() {
    RandomGeneratorImpl generator = new RandomGeneratorImpl();

    int result = generator.nextInt(10);

    assertTrue(result >= 0);
    assertTrue(result < 10);
  }

  // Generates a double within the valid range.
  @Test
  void shouldGenerateDoubleWithinRange() {
    RandomGeneratorImpl generator = new RandomGeneratorImpl();

    double result = generator.nextDouble();

    assertTrue(result >= 0.0);
    assertTrue(result < 1.0);
  }

  // Rejects non-positive integer bounds.
  @Test
  void shouldRejectNonPositiveBound() {
    RandomGeneratorImpl generator = new RandomGeneratorImpl();

    assertThrows(
        IllegalArgumentException.class,
        () -> generator.nextInt(0)
    );
  }
}
