package app.domain.terrain;


import app.domain.enums.PeriodOfDay;

/**
 * Represents hot asphalt terrain.
 *
 * Fire attacks receive a 15% damage bonus during daytime.
 */
public class HotAsphalt implements Terrain {

  @Override
  public TerrainEffect calculateEffect(TerrainContext context) {
    double fireMultiplier =
        context.getPeriodOfDay() == PeriodOfDay.DAY
            ? 1.15
            : 1.0;

    return new TerrainEffect(
        fireMultiplier,
        1.0,
        0.0,
        0.0
    );
  }

  @Override
  public String getName() {
    return "Hot Asphalt";
  }
}