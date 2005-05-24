package org.idanso.weather;

import java.util.Date;

import net.sf.hibernate.HibernateException;

import org.idanso.stationinfo.util.StationUtils;
import org.idanso.weather.base.BaseStation;



public class Station extends BaseStation {
	private static final long serialVersionUID = 1L;
	private StationRecord latestRecord = null;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Station () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Station (java.lang.Long id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public StationRecord createStationRecord()
	{
		StationRecord rec=new StationRecord();
		rec.setStation(this);
		rec.setStamp(new Date());
		return rec;
	}

	public void setLatestRecord(StationRecord record) {
		latestRecord=record;		
	}
	
	public StationRecord getLatestRecord() throws HibernateException {
		if (latestRecord==null)
		{
			setLatestRecord(StationUtils.getLatestRecord(this));
		}
		return latestRecord;
	}
	
	

}