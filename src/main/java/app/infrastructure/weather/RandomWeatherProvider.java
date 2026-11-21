package app.infrastructure.weather;

import app.domain.enums.WeatherCondition;
import app.domain.port.RandomGenerator;
import app.domain.port.WeatherProvider;

import java.util.Objects;

/**
 * Selects random weather conditions for battle environments.
 */
public class RandomWeatherProvider implements WeatherProvider {

  private final RandomGenerator randomGenerator;

  public RandomWeatherProvider(RandomGenerator randomGenerator) {
    this.randomGenerator = Objects.requireNonNull(randomGenerator);
  }

  @Override
  public WeatherCondition getCurrentWeather() {
    WeatherCondition[] conditions = WeatherCondition.values();

    return conditions[randomGenerator.nextInt(conditions.length)];
  }
}