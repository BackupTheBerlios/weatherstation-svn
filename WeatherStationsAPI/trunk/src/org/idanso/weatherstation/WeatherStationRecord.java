package org.idanso.weatherstation;

import java.util.Date;

public interface WeatherStationRecord {

	
	public WeatherStation getStation();
	
	/**
	 * 
	 * @return Latest temperature in Kelvin degrees
	 */
	public float getTemperature()  throws UnsupportedDevice ;
	/**
	 * 
	 * @return Latest relative humadity in percentage(0-100)
	 */	
	public float getHumadity()  throws UnsupportedDevice ;
	/**
	 * 
	 * @return Latest air pressure in Milibar
	 */
	public float getPressure()  throws UnsupportedDevice ;
	/**
	 * 
	 * @return Latest wind direction in degrees
	 */
	public float getWindDirection()  throws UnsupportedDevice ;
	/**
	 * 
	 * @return Latest wind speed in meter per second
	 */
	public float getWindSpeed()  throws UnsupportedDevice ;

	public Date getStamp();
	
	
}
