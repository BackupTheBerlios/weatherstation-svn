package org.idanso.weather.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the station_parameter table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="station_parameter"
 */

public abstract class BaseStationParameter  implements Serializable {

	public static String REF = "StationParameter";
	public static String PROP_KEY = "Key";
	public static String PROP_VALUE = "Value";


	// constructors
	public BaseStationParameter () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseStationParameter (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseStationParameter (
		java.lang.Long id,
		java.lang.String key) {

		this.setId(id);
		this.setKey(key);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String key;
	private java.lang.String value;

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
	 * Return the value associated with the column: prop
	 */
	public java.lang.String getKey () {
		return key;
	}

	/**
	 * Set the value related to the column: prop
	 * @param key the prop value
	 */
	public void setKey (java.lang.String key) {
		this.key = key;
	}



	/**
	 * Return the value associated with the column: value
	 */
	public java.lang.String getValue () {
		return value;
	}

	/**
	 * Set the value related to the column: value
	 * @param value the value value
	 */
	public void setValue (java.lang.String value) {
		this.value = value;
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
		if (!(obj instanceof org.idanso.weather.StationParameter)) return false;
		else {
			org.idanso.weather.StationParameter stationParameter = (org.idanso.weather.StationParameter) obj;
			if (null == this.getId() || null == stationParameter.getId()) return false;
			else return (this.getId().equals(stationParameter.getId()));
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