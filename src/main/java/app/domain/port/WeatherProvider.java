package app.domain.port;


import app.domain.enums.WeatherCondition;

/**
 * Defines the contract for providing weather conditions
 * used by the battle terrain system.
 */

public interface WeatherProvider {

  WeatherCondition getCurrentWeather();
}