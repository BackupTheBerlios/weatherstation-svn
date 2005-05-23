package org.idanso.weatherstation;

import java.util.Date;
import java.util.Properties;

/**
 * Defines an interface for a weather station
 * 
 * @author idan
 *
 */
public interface WeatherStation {

	
	/**
	 * Configures the weather station using the given properties
	 * 
	 * @param conf
	 */
	public void configure(Properties conf);
	/**
	 * Pull fresh data from the weather station and ensure they can be fetched using
	 * the interface.
	 * 
	 * 
	 * @return true if fresh data is available, otherwise false
	 * @throws WeatherStationException
	 */
	public boolean update() throws WeatherStationException;
	
	/**
	 * Returns the current station record
	 * 
	 * @return
	 */
	public WeatherStationRecord getRecord();
	
	/**
	 * Return the timestamp for the current data in the station
	 * 
	 * @return
	 */
	public Date getLastUpdateTime();
	
	
	/**
	 * Does the station record the air temperature?
	 * 
	 * @return
	 */
	public boolean hasTemperature();
	/**
	 * Does the station record the air relative humadity?
	 * 
	 * @return
	 */
	public boolean hasHumadity();
	/**
	 * Does the station record the air pressure
	 * 
	 * @return
	 */
	public boolean hasPressure();
	/**
	 * Does the station record the wind direction
	 * 
	 * @return
	 */
	public boolean hasWindDirection();
	/**
	 * Does the station record the wind speed
	 * 
	 * @return
	 */
	public boolean hasWindSpeed();
	
}
