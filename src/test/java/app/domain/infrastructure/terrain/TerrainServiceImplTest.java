
package app.domain.infrastructure.terrain;

import app.domain.enums.PeriodOfDay;
import app.domain.enums.WeatherCondition;
import app.domain.port.WeatherProvider;
import app.domain.terrain.Arena;
import app.domain.terrain.HotAsphalt;
import app.domain.terrain.NormalTerrain;
import app.domain.terrain.TerrainContext;
import app.domain.terrain.TerrainEffect;
import app.infrastructure.terrain.TerrainServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

  // Creates a daytime context at 6 AM.
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

  // Keeps daytime status before 6 PM.
  @Test
  void shouldCreateDayContextBeforeSixPm() {
    TerrainServiceImpl service = createService(
        "2026-11-15T20:59:00Z",
        WeatherCondition.CLOUDY
    );

    TerrainContext context = service.createBattleContext();

    assertEquals(PeriodOfDay.DAY, context.getPeriodOfDay());
  }

  // Switches to nighttime at 6 PM.
  @Test
  void shouldCreateNightContextAtSixPm() {
    TerrainServiceImpl service = createService(
        "2026-11-15T21:00:00Z",
        WeatherCondition.RAINY
    );

    TerrainContext context = service.createBattleContext();

    assertEquals(PeriodOfDay.NIGHT, context.getPeriodOfDay());
  }

  // Keeps nighttime status before 6 AM.
  @Test
  void shouldCreateNightContextBeforeSixAm() {
    TerrainServiceImpl service = createService(
        "2026-11-15T08:59:00Z",
        WeatherCondition.SUNNY
    );

    TerrainContext context = service.createBattleContext();

    assertEquals(PeriodOfDay.NIGHT, context.getPeriodOfDay());
  }

  // Uses the weather provider's returned condition.
  @Test
  void shouldUseWeatherProvider() {
    TerrainServiceImpl service = createService(
        "2026-11-15T12:00:00Z",
        WeatherCondition.RAINY
    );

    TerrainContext context = service.createBattleContext();

    assertEquals(
        WeatherCondition.RAINY,
        context.getWeatherCondition()
    );
  }

  // Rejects a null weather condition.
  @Test
  void shouldThrowExceptionWhenWeatherProviderReturnsNull() {
    TerrainServiceImpl service = createService(
        "2026-11-15T12:00:00Z",
        null
    );

    assertThrows(
        IllegalStateException.class,
        service::createBattleContext
    );
  }

  // Rejects a null clock dependency.
  @Test
  void shouldRejectNullClock() {
    WeatherProvider weatherProvider = mock(WeatherProvider.class);

    assertThrows(
        IllegalArgumentException.class,
        () -> new TerrainServiceImpl(null, weatherProvider)
    );
  }

  // Rejects a null weather provider.
  @Test
  void shouldRejectNullWeatherProvider() {
    Clock clock = Clock.systemUTC();

    assertThrows(
        IllegalArgumentException.class,
        () -> new TerrainServiceImpl(clock, null)
    );
  }

  // Rejects a null arena.
  @Test
  void shouldRejectNullArena() {
    TerrainServiceImpl service = createService(
        "2026-11-15T12:00:00Z",
        WeatherCondition.SUNNY
    );

    TerrainContext context = new TerrainContext(
        PeriodOfDay.DAY,
        WeatherCondition.SUNNY
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> service.calculateEffect(null, context)
    );
  }

  // Rejects a null battle context.
  @Test
  void shouldRejectNullContext() {
    TerrainServiceImpl service = createService(
        "2026-11-15T12:00:00Z",
        WeatherCondition.SUNNY
    );

    Arena arena = new Arena("Test Arena", new NormalTerrain());

    assertThrows(
        IllegalArgumentException.class,
        () -> service.calculateEffect(arena, null)
    );
  }

  // Delegates effect calculation to the arena terrain.
  @Test
  void shouldDelegateEffectCalculationToArenaTerrain() {
    TerrainServiceImpl service = createService(
        "2026-11-15T12:00:00Z",
        WeatherCondition.SUNNY
    );

    Arena arena = new Arena("Test Arena", new HotAsphalt());

    TerrainContext context = new TerrainContext(
        PeriodOfDay.DAY,
        WeatherCondition.SUNNY
    );

    TerrainEffect effect = service.calculateEffect(arena, context);

    assertNotNull(effect);
    assertEquals(1.15, effect.getFireDamageMultiplier(), 0.001);
  }
}
