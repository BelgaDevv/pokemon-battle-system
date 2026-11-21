
package app.domain.infrastructure.terrain;

import app.domain.enums.PeriodOfDay;
import app.domain.enums.WeatherCondition;
import app.domain.port.WeatherProvider;
import app.domain.terrain.*;
import app.infrastructure.terrain.TerrainServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TerrainServiceImplTest {

  private TerrainServiceImpl createService(
      String dateTime,
      WeatherCondition weather
  ) {
    Clock clock = Clock.fixed(
        Instant.parse(dateTime),
        ZoneId.of("UTC")
    );

    WeatherProvider weatherProvider = mock(WeatherProvider.class);

    when(weatherProvider.getCurrentWeather()).thenReturn(weather);

    return new TerrainServiceImpl(clock, weatherProvider);
  }

  // Identifies daytime at 6 AM.
  @Test
  void shouldCreateDayContextAtSixAm() {
    TerrainServiceImpl service = createService(
        "2026-11-15T09:00:00Z",
        WeatherCondition.SUNNY
    );

    TerrainContext context = service.createBattleContext();

    assertEquals(PeriodOfDay.DAY, context.getPeriodOfDay());
    assertEquals(
        WeatherCondition.SUNNY,
        context.getWeatherCondition()
    );
  }

  // Identifies daytime immediately before 6 PM.
  @Test
  void shouldCreateDayContextBeforeSixPm() {
    TerrainServiceImpl service = createService(
        "2026-11-15T20:59:00Z",
        WeatherCondition.CLOUDY
    );

    TerrainContext context = service.createBattleContext();

    assertEquals(PeriodOfDay.DAY, context.getPeriodOfDay());
  }

  // Identifies nighttime at 6 PM.
  @Test
  void shouldCreateNightContextAtSixPm() {
    TerrainServiceImpl service = createService(
        "2026-11-15T21:00:00Z",
        WeatherCondition.RAINY
    );

    TerrainContext context = service.createBattleContext();

    assertEquals(PeriodOfDay.NIGHT, context.getPeriodOfDay());
  }

  // Identifies nighttime immediately before 6 AM.
  @Test
  void shouldCreateNightContextBeforeSixAm() {
    TerrainServiceImpl service = createService(
        "2026-11-15T08:59:00Z",
        WeatherCondition.SUNNY
    );

    TerrainContext context = service.createBattleContext();

    assertEquals(PeriodOfDay.NIGHT, context.getPeriodOfDay());
  }

  // Uses the weather provider and rejects missing weather.
  @Test
  void shouldUseValidWeatherProviderResponse() {
    TerrainServiceImpl service = createService(
        "2026-11-15T12:00:00Z",
        WeatherCondition.RAINY
    );

    TerrainContext context = service.createBattleContext();

    assertEquals(
        WeatherCondition.RAINY,
        context.getWeatherCondition()
    );

    TerrainServiceImpl invalidService = createService(
        "2026-11-15T12:00:00Z",
        null
    );

    assertThrows(
        IllegalStateException.class,
        invalidService::createBattleContext
    );
  }

  // Calculates terrain effects and rejects null arguments.
  @Test
  void shouldCalculateTerrainEffectWithValidArguments() {
    TerrainServiceImpl service = createService(
        "2026-11-15T12:00:00Z",
        WeatherCondition.SUNNY
    );

    Arena arena = new Arena(
        "Test Arena",
        new HotAsphalt()
    );

    TerrainContext context = new TerrainContext(
        PeriodOfDay.DAY,
        WeatherCondition.SUNNY
    );

    TerrainEffect effect = service.calculateEffect(arena, context);

    assertNotNull(effect);
    assertEquals(
        1.15,
        effect.getFireDamageMultiplier(),
        0.001
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> service.calculateEffect(null, context)
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> service.calculateEffect(arena, null)
    );
  }

  // Applies the hot asphalt bonus only during daytime.
  @Test
  void shouldApplyHotAsphaltBonusOnlyDuringDay() {
    TerrainServiceImpl dayService = createService(
        "2026-11-15T09:00:00Z",
        WeatherCondition.SUNNY
    );

    TerrainServiceImpl nightService = createService(
        "2026-11-15T21:00:00Z",
        WeatherCondition.SUNNY
    );

    Arena arena = new Arena(
        "Test Arena",
        new HotAsphalt()
    );

    TerrainContext dayContext =
        dayService.createBattleContext();

    TerrainContext nightContext =
        nightService.createBattleContext();

    TerrainEffect dayEffect =
        dayService.calculateEffect(arena, dayContext);

    TerrainEffect nightEffect =
        nightService.calculateEffect(arena, nightContext);

    assertEquals(
        PeriodOfDay.DAY,
        dayContext.getPeriodOfDay()
    );

    assertEquals(
        PeriodOfDay.NIGHT,
        nightContext.getPeriodOfDay()
    );

    assertEquals(
        1.15,
        dayEffect.getFireDamageMultiplier(),
        0.001
    );

    assertEquals(
        1.0,
        nightEffect.getFireDamageMultiplier(),
        0.001
    );
  }
}
