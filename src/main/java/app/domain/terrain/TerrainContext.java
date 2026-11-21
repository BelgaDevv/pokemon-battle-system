
package app.domain.terrain;

import app.domain.enums.PeriodOfDay;
import app.domain.enums.WeatherCondition;

/**
 * Stores the time of day and weather conditions
 * used to determine terrain effects during a battle.
 */
public class TerrainContext {

  private final PeriodOfDay periodOfDay;
  private final WeatherCondition weatherCondition;

  /**
   * Creates a terrain context with the specified
   * time of day and weather condition.
   */
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

  /**
   * Returns the time of day used in the battle.
   */
  public PeriodOfDay getPeriodOfDay() {
    return periodOfDay;
  }

  /**
   * Returns the weather condition used in the battle.
   */
  public WeatherCondition getWeatherCondition() {
    return weatherCondition;
  }
}
