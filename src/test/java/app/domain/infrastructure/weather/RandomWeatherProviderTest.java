
package app.domain.infrastructure.weather;

import app.domain.enums.WeatherCondition;
import app.domain.port.RandomGenerator;
import app.infrastructure.weather.RandomWeatherProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RandomWeatherProviderTest {

  // Maps random values to the correct weather conditions.
  @ParameterizedTest
  @CsvSource({
      "0, SUNNY",
      "1, RAINY",
      "2, CLOUDY"
  })
  void shouldReturnWeatherForRandomValue(
      int randomValue,
      WeatherCondition expectedWeather
  ) {
    RandomGenerator randomGenerator = mock(RandomGenerator.class);

    when(randomGenerator.nextInt(3)).thenReturn(randomValue);

    RandomWeatherProvider provider =
        new RandomWeatherProvider(randomGenerator);

    assertEquals(expectedWeather, provider.getCurrentWeather());

    verify(randomGenerator).nextInt(3);
  }

  // Rejects a null random generator.
  @Test
  void shouldRejectNullRandomGenerator() {
    assertThrows(
        NullPointerException.class,
        () -> new RandomWeatherProvider(null)
    );
  }
}
