package org.idanso.weather;

import java.util.Date;

import org.idanso.weather.base.BaseStationRecord;



public class StationRecord extends BaseStationRecord {
	private static final long serialVersionUID = 1L;
	private static StationRecord emptyRecord;
/*[CONSTRUCTOR MARKER BEGIN]*/
	public StationRecord () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public StationRecord (java.lang.Long id) {
		super(id);
	}
/*[CONSTRUCTOR MARKER END]*/

	
	public static StationRecord getEmptyRecord() {
		
		if (emptyRecord==null)
		{
			emptyRecord=new StationRecord();
			emptyRecord.setStamp(new Date());
		}
		return emptyRecord;
	}



}