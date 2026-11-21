
package app.domain.terrain;

import app.domain.enums.PeriodOfDay;

/**
 * Increases Fire attack damage by 15%
 * when the battle takes place during daytime.
 */
public class HotAsphalt implements Terrain {

  /**
   * Calculates the terrain effect based on the time of day.
   * Fire attacks receive a damage bonus only during daytime.
   */
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
