package org.idanso.weatherstation;

import java.util.Date;
import java.util.Properties;

public interface WeatherStation {

	public void configure(Properties conf);
	public boolean update() throws WeatherStationException;
	
	
	public WeatherStationRecord getRecord();
	
	public Date getLastUpdateTime();
	
	
	public boolean hasTemperature();
	public boolean hasHumadity();
	public boolean hasPressure();
	public boolean hasWindDirection();
	public boolean hasWindSpeed();
	
}
