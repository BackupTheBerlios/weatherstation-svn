package org.idanso.weather.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the station table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="station"
 */

public abstract class BaseStation  implements Serializable {

	public static String REF = "Station";
	public static String PROP_TITLE = "Title";
	public static String PROP_LOCATION = "Location";
	public static String PROP_LATITUDE = "Latitude";
	public static String PROP_LONGITUDE = "Longitude";
	public static String PROP_ALIAS = "Alias";
	public static String PROP_DRIVER = "Driver";


	// constructors
	public BaseStation () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseStation (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseStation (
		java.lang.Long id,
		java.lang.String driver) {

		this.setId(id);
		this.setDriver(driver);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String title;
	private java.lang.String location;
	private float latitude;
	private float longitude;
	private java.lang.String alias;
	private java.lang.String driver;

	// collections
	private java.util.Map parameters;



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
	 * Return the value associated with the column: title
	 */
	public java.lang.String getTitle () {
		return title;
	}

	/**
	 * Set the value related to the column: title
	 * @param title the title value
	 */
	public void setTitle (java.lang.String title) {
		this.title = title;
	}



	/**
	 * Return the value associated with the column: location
	 */
	public java.lang.String getLocation () {
		return location;
	}

	/**
	 * Set the value related to the column: location
	 * @param location the location value
	 */
	public void setLocation (java.lang.String location) {
		this.location = location;
	}



	/**
	 * Return the value associated with the column: latitude
	 */
	public float getLatitude () {
		return latitude;
	}

	/**
	 * Set the value related to the column: latitude
	 * @param latitude the latitude value
	 */
	public void setLatitude (float latitude) {
		this.latitude = latitude;
	}



	/**
	 * Return the value associated with the column: longitude
	 */
	public float getLongitude () {
		return longitude;
	}

	/**
	 * Set the value related to the column: longitude
	 * @param longitude the longitude value
	 */
	public void setLongitude (float longitude) {
		this.longitude = longitude;
	}



	/**
	 * Return the value associated with the column: alias
	 */
	public java.lang.String getAlias () {
		return alias;
	}

	/**
	 * Set the value related to the column: alias
	 * @param alias the alias value
	 */
	public void setAlias (java.lang.String alias) {
		this.alias = alias;
	}



	/**
	 * Return the value associated with the column: driver
	 */
	public java.lang.String getDriver () {
		return driver;
	}

	/**
	 * Set the value related to the column: driver
	 * @param driver the driver value
	 */
	public void setDriver (java.lang.String driver) {
		this.driver = driver;
	}



	/**
	 * Return the value associated with the column: parameters
	 */
	public java.util.Map getParameters () {
		return parameters;
	}

	/**
	 * Set the value related to the column: parameters
	 * @param parameters the parameters value
	 */
	public void setParameters (java.util.Map parameters) {
		this.parameters = parameters;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.idanso.weather.Station)) return false;
		else {
			org.idanso.weather.Station station = (org.idanso.weather.Station) obj;
			if (null == this.getId() || null == station.getId()) return false;
			else return (this.getId().equals(station.getId()));
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