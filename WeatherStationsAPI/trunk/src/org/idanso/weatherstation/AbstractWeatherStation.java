package org.idanso.weatherstation;

import java.util.Date;
import java.util.Properties;

/**
 * Abstract implementation of a weather station
 * 
 * @author idan
 *
 */

public abstract class AbstractWeatherStation implements WeatherStation {
	
	private WeatherStationRecord record;
	private Properties conf;

	public abstract boolean update() throws WeatherStationException ;
	
	public Date getLastUpdateTime() {
		synchronized (this) {
			if (record!=null)
			{
				return record.getStamp();
			}
			else
			{
				return null;
			}
		}
	}
	
	public WeatherStationRecord getRecord()
	{
		synchronized (this) {
			return record;
		}
	}
	
	protected void setRecord(WeatherStationRecord rec)
	{
		synchronized (this) {
			record=rec;
		}
	}

	public abstract boolean hasTemperature();
	public abstract boolean hasHumadity();
	public abstract boolean hasPressure();
	public abstract boolean hasWindDirection();
	public abstract boolean hasWindSpeed();

	public void configure(Properties conf) {
		this.conf=conf;		
	}
	
	protected Properties getConfiguration()
	{
		if (this.conf==null)
			this.conf=new Properties();
		return conf;
	}


}
