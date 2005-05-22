package org.idanso.weatherstation;

import java.util.Date;

public class BaseWeatherStationRecord implements WeatherStationRecord {
	
	private float temperature;
	private float humadity;
	private float pressure;
	private float windDirection;
	private float windSpeed;
	private Date stamp;

	public BaseWeatherStationRecord(float temperature, float humadity, float pressure, float direction, float speed, Date stamp) {
		super();
		// TODO Auto-generated constructor stub
		this.temperature = temperature;
		this.humadity = humadity;
		this.pressure = pressure;
		windDirection = direction;
		windSpeed = speed;
		this.stamp = stamp;
	}

	public WeatherStation getStation() {
		// TODO Auto-generated method stub
		return null;
	}

	public float getTemperature() {
		return temperature;
	}

	public float getHumadity() {
		return humadity;
	}

	public float getPressure() {
		return pressure;
	}

	public float getWindDirection() {
		return windDirection;
	}

	public float getWindSpeed() {
		return windSpeed;
	}
	
	public Date getStamp()
	{
		return stamp;
	}
	

}
