package org.idanso.weather;

import org.idanso.weather.base.BaseStationParameter;



public class StationParameter extends BaseStationParameter {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public StationParameter () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public StationParameter (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public StationParameter (
		java.lang.Long id,
		java.lang.String key) {

		super (
			id,
			key);
	}

/*[CONSTRUCTOR MARKER END]*/


}