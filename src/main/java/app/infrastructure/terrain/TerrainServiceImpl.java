
package app.infrastructure.terrain;

import app.domain.application.port.TerrainService;
import app.domain.enums.PeriodOfDay;
import app.domain.enums.WeatherCondition;
import app.domain.port.WeatherProvider;
import app.domain.terrain.Arena;
import app.domain.terrain.TerrainContext;
import app.domain.terrain.TerrainEffect;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Creates battle environments based on the time of day
 * and weather conditions, and calculates terrain effects.
 */
public class TerrainServiceImpl implements TerrainService {

  private static final ZoneId TIME_ZONE =
      ZoneId.of("America/Sao_Paulo");

  private static final LocalTime DAY_START =
      LocalTime.of(6, 0);

  private static final LocalTime NIGHT_START =
      LocalTime.of(18, 0);

  private final Clock clock;
  private final WeatherProvider weatherProvider;

  public TerrainServiceImpl(
      Clock clock,
      WeatherProvider weatherProvider
  ) {
    if (clock == null || weatherProvider == null) {
      throw new IllegalArgumentException(
          "Clock and weather provider cannot be null."
      );
    }

    this.clock = clock;
    this.weatherProvider = weatherProvider;
  }

  /**
   * Creates a battle context using the current time in São Paulo
   * and the weather provided for the battle.
   */
  @Override
  public TerrainContext createBattleContext() {
    LocalTime currentTime = LocalTime.now(
        clock.withZone(TIME_ZONE)
    );

    PeriodOfDay period = isDaytime(currentTime)
        ? PeriodOfDay.DAY
        : PeriodOfDay.NIGHT;

    WeatherCondition weather =
        weatherProvider.getCurrentWeather();

    if (weather == null) {
      throw new IllegalStateException(
          "Weather provider returned null."
      );
    }

    return new TerrainContext(period, weather);
  }

  @Override
  public TerrainEffect calculateEffect(
      Arena arena,
      TerrainContext context
  ) {
    if (arena == null || context == null) {
      throw new IllegalArgumentException(
          "Arena and context cannot be null."
      );
    }

    return arena.getTerrain().calculateEffect(context);
  }

  private boolean isDaytime(LocalTime time) {
    return !time.isBefore(DAY_START)
        && time.isBefore(NIGHT_START);
  }
}
