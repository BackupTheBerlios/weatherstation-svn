package org.idanso.weather.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the station_record table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="station_record"
 */

public abstract class BaseStationRecord  implements Serializable {

	public static String REF = "StationRecord";
	public static String PROP_STAMP = "Stamp";
	public static String PROP_TEMPERATURE = "Temperature";
	public static String PROP_HUMADITY = "Humadity";
	public static String PROP_PRESSURE = "Pressure";
	public static String PROP_WIND_DIRECTION = "WindDirection";
	public static String PROP_WIND_SPEED = "windSpeed";


	// constructors
	public BaseStationRecord () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseStationRecord (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.util.Date stamp;
	private float temperature;
	private float humadity;
	private float pressure;
	private float windDirection;
	private float windSpeed;

	// many to one
	private org.idanso.weather.Station station;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="vm"
     *  column="id"
     */
	public java.lang.Long getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Long id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: stamp
	 */
	public java.util.Date getStamp () {
		return stamp;
	}

	/**
	 * Set the value related to the column: stamp
	 * @param stamp the stamp value
	 */
	public void setStamp (java.util.Date stamp) {
		this.stamp = stamp;
	}



	/**
	 * Return the value associated with the column: temperature
	 */
	public float getTemperature () {
		return temperature;
	}

	/**
	 * Set the value related to the column: temperature
	 * @param temperature the temperature value
	 */
	public void setTemperature (float temperature) {
		this.temperature = temperature;
	}



	/**
	 * Return the value associated with the column: humadity
	 */
	public float getHumadity () {
		return humadity;
	}

	/**
	 * Set the value related to the column: humadity
	 * @param humadity the humadity value
	 */
	public void setHumadity (float humadity) {
		this.humadity = humadity;
	}



	/**
	 * Return the value associated with the column: pressure
	 */
	public float getPressure () {
		return pressure;
	}

	/**
	 * Set the value related to the column: pressure
	 * @param pressure the pressure value
	 */
	public void setPressure (float pressure) {
		this.pressure = pressure;
	}



	/**
	 * Return the value associated with the column: wind_direction
	 */
	public float getWindDirection () {
		return windDirection;
	}

	/**
	 * Set the value related to the column: wind_direction
	 * @param windDirection the wind_direction value
	 */
	public void setWindDirection (float windDirection) {
		this.windDirection = windDirection;
	}



	/**
	 * Return the value associated with the column: wind_speed
	 */
	public float getWindSpeed () {
		return windSpeed;
	}

	/**
	 * Set the value related to the column: wind_speed
	 * @param windSpeed the wind_speed value
	 */
	public void setWindSpeed (float windSpeed) {
		this.windSpeed = windSpeed;
	}



	/**
	 * Return the value associated with the column: station
	 */
	public org.idanso.weather.Station getStation () {
		return station;
	}

	/**
	 * Set the value related to the column: station
	 * @param station the station value
	 */
	public void setStation (org.idanso.weather.Station station) {
		this.station = station;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.idanso.weather.StationRecord)) return false;
		else {
			org.idanso.weather.StationRecord stationRecord = (org.idanso.weather.StationRecord) obj;
			if (null == this.getId() || null == stationRecord.getId()) return false;
			else return (this.getId().equals(stationRecord.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		return super.toString();
	}


}