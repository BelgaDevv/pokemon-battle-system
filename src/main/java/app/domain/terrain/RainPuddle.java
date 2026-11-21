
package app.domain.terrain;

import app.domain.enums.WeatherCondition;

/**
 * Applies damage and accuracy bonuses to Water attacks
 * when the weather is rainy.
 */
public class RainPuddle implements Terrain {

  /**
   * Calculates terrain bonuses based on the weather.
   * Rain increases Water damage and accuracy by 10%.
   */
  @Override
  public TerrainEffect calculateEffect(TerrainContext context) {
    boolean rainy =
        context.getWeatherCondition() == WeatherCondition.RAINY;

    return new TerrainEffect(
        1.0,
        rainy ? 1.10 : 1.0,
        rainy ? 0.10 : 0.0,
        0.0
    );
  }


  @Override
  public String getName() {
    return "Rain Puddle";
  }
}
