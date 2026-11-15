package app.domain.terrain;


import app.domain.enums.PeriodOfDay;
import app.domain.enums.WeatherCondition;

/**
 * Represents the environmental conditions affecting a battle terrain.
 */
public class TerrainContext {

  private final PeriodOfDay periodOfDay;
  private final WeatherCondition weatherCondition;

  public TerrainContext(
      PeriodOfDay periodOfDay,
      WeatherCondition weatherCondition
  ) {
    if (periodOfDay == null || weatherCondition == null) {
      throw new IllegalArgumentException(
          "Period of day and weather condition cannot be null."
      );
    }

    this.periodOfDay = periodOfDay;
    this.weatherCondition = weatherCondition;
  }

  public PeriodOfDay getPeriodOfDay() {
    return periodOfDay;
  }

  public WeatherCondition getWeatherCondition() {
    return weatherCondition;
  }
}