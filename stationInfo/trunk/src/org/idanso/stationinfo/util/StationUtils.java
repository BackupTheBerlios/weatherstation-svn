package org.idanso.stationinfo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.idanso.weather.Station;
import org.idanso.weather.StationRecord;

public class StationUtils {

	private static Properties conf=null;
	private static Log log=LogFactory.getLog(StationUtils.class);
	
	public static List getAllStations(boolean extended) throws HibernateException {		
		Session session=HibernateUtil.getSession();
		List stations;
		if (extended)
		{
			stations=new ArrayList();
			Iterator it=session.iterate("select distinct station from Station as station");
			Query q=session.createQuery("from StationRecord as record where record.Stamp = (select max(rec.Stamp) from StationRecord rec where rec.station=?)");
			q.setCacheable(true);
			while(it.hasNext())			
			{
				Station station=(Station) it.next();
				try
				{
					q.setParameter(0,station);
					StationRecord record=(StationRecord) q.iterate().next();
					station.setLatestRecord(record);
				}
				catch(NoSuchElementException e)
				{
					station.setLatestRecord(StationRecord.getEmptyRecord());					
				}
				stations.add(station);
			}
		}
		else
		{
			stations=session.find("from Station as station");
		}
		return stations;
	}
	
	public static Properties getConfiguration() {
		if (conf==null)
		{
			InputStream res=StationUtils.class.getResourceAsStream("/weather.properties");
			conf=new Properties();
			try {
				conf.load(res);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		// TODO Auto-generated method stub
		return conf;
	}
	
	public static List getExtendedAllStations() throws HibernateException {
		Session session=HibernateUtil.getSession();
		List stations=session.find("from Station as station");
		return stations;
	}


	public static Integer getMaxPagedStationRecords(Station station,int maxInPage) throws HibernateException
	{
		Session session=HibernateUtil.getSession();
		Query q = session.createQuery("select count(*) from StationRecord record where record.station=?");
		q.setParameter(0,station);
		Iterator it=q.iterate();
		Integer count=(Integer) it.next();
		return new Integer((int) Math.ceil(count.floatValue()/maxInPage));
	}
	
	
	public static List getPagedStationRecords(Station station,int page,int maxInPage) throws HibernateException {
		Session session=HibernateUtil.getSession();
		Query q=session.createQuery("from StationRecord record where record.station=? order by record.Stamp DESC");
		q.setParameter(0,station);
		q.setMaxResults(maxInPage);
		q.setFirstResult(maxInPage*(page-1));		
		return q.list();
	}
	
	public static List getTimeRangeStationRecords(Station station, Date start, Date end) throws HibernateException {
		log.debug("Fetching records for" + station+", using range of "+start+end);
		Session session=HibernateUtil.getSession();
		Query q=session.createQuery("from StationRecord record where record.station=? and record.Stamp between ? and ? order by record.Stamp ASC");
		q.setParameter(0,station);
		q.setParameter(1,start);
		q.setParameter(2,end);
		return q.list();
	}

	public static StationRecord getMaxTimeRangeStationRecord(Station station, Date start, Date end) throws HibernateException {
		Session session=HibernateUtil.getSession();
		Query q=session.createQuery("select max(record.Temperature),max(record.Humadity),max(record.Pressure),max(record.windSpeed) from StationRecord record where record.station=? and record.Stamp between ? and ?");
		q.setParameter(0,station);
		q.setParameter(1,start);
		q.setParameter(2,end);
		Object[] result = (Object[]) q.list().get(0);
		StationRecord record=new StationRecord();
		if(result[0]!=null)
		{
			record.setTemperature(((Float) result[0]).floatValue());
			record.setHumadity(((Float) result[1]).floatValue());
			record.setPressure(((Float) result[2]).floatValue());
			record.setWindSpeed(((Float) result[3]).floatValue());
		}
		return record;				
	}
	
	public static StationRecord getMinTimeRangeStationRecord(Station station, Date start, Date end) throws HibernateException {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Query q=session.createQuery("select min(record.Temperature),min(record.Humadity),min(record.Pressure),min(record.windSpeed) from StationRecord record where record.station=? and record.Stamp between ? and ?");
		q.setParameter(0,station);
		q.setParameter(1,start);
		q.setParameter(2,end);
		Object[] result = (Object[]) q.list().get(0);
		StationRecord record=new StationRecord();
		if(result[0]!=null)
		{
			record.setTemperature(((Float) result[0]).floatValue());
			record.setHumadity(((Float) result[1]).floatValue());
			record.setPressure(((Float) result[2]).floatValue());
			record.setWindSpeed(((Float) result[3]).floatValue());
		}
		return record;				
	}
	
	public static StationRecord getLatestRecord(Station station) throws HibernateException {
		Session session=HibernateUtil.getSession();
		Query q=session.createQuery("from StationRecord record where record.station=? order by record.Stamp DESC");
		q.setParameter(0,station);
		q.setMaxResults(1);
		if (q.list().size()==0)
		{
			return null;
		}
		else
		{
			return (StationRecord) q.list().get(0);
		}
	}



	protected StationUtils()
	{
		
	}

}
