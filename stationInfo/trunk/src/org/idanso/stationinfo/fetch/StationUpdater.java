package org.idanso.stationinfo.fetch;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TimerTask;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;
import org.idanso.stationinfo.util.HibernateUtil;
import org.idanso.stationinfo.util.StationUtils;
import org.idanso.weather.Station;
import org.idanso.weather.StationRecord;
import org.idanso.weatherstation.UnsupportedDevice;
import org.idanso.weatherstation.WeatherStation;
import org.idanso.weatherstation.WeatherStationException;
import org.idanso.weatherstation.WeatherStationRecord;

public class StationUpdater extends TimerTask{
	
	private Map driverMap=new HashMap();
	private Map confMap=new HashMap();
	// TODO: Find out why we have to deal with Log4JLogger directly..
	private static Log log=new Log4JLogger(StationUpdater.class.getName());
	
	private List updateStations;
	
	public StationUpdater() throws HibernateException
	{
		log.info("Initializing weather stations updater...");
		initConfig();
		initUpdater();
	}
	
	private void initConfig() {
		Properties conf=StationUtils.getConfiguration();
		Iterator it=conf.keySet().iterator();
		while(it.hasNext())
		{
			String key=(String) it.next();
			String fullkey=key;
			if(key.startsWith("org.weather."))
			{
				key=key.substring(12);
				if(key.startsWith("station."))
				{
					key=key.substring(8);
					String stationName=key.substring(0,key.indexOf('.'));
					key=key.substring(key.indexOf('.')+1);
					Properties stationConf=(Properties) confMap.get(stationName);
					if (stationConf==null)
					{
						stationConf=new Properties();
						confMap.put(stationName,stationConf);
					}
					if (key.equals("class"))
					{
						try {
							Class driver=Class.forName(conf.getProperty(fullkey));
							driverMap.put(stationName,driver);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else
					{
						stationConf.put(key,conf.getProperty(fullkey));
					}
				}
			}
		}
	}
	
	
	private void initUpdater() throws HibernateException {
		Iterator it=StationUtils.getAllStations(false).iterator();
		updateStations=new ArrayList();
		while(it.hasNext())
		{
			Station station=(Station) it.next();
			String stationName=station.getAlias();			
			try {
				Properties stationConf=(Properties) confMap.get(stationName);
				Class klass=(Class) driverMap.get(stationName);
				if (klass==null)
				{
					log.error("Could not find driver for "+stationName);
				}
				else
				{
					WeatherStation weatherStation=(WeatherStation) klass.newInstance();
					weatherStation.configure((Properties) stationConf.clone());
					Object[] arr=new Object[]{weatherStation,station};
					updateStations.add(arr);
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
//		timer.schedule(new StationUpdater(),0,1000);		
	}
	
	public void run() {
		Session sess;
		Transaction tr;
		try {
			sess=HibernateUtil.getSession();
			tr=sess.beginTransaction();
		} catch (HibernateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		Iterator it=updateStations.iterator();
		while(it.hasNext())
		{
			Object arr[]=(Object[]) it.next();
			WeatherStation weatherStation=(WeatherStation) arr[0];
			Station station=(Station) arr[1];
			try {
				if (weatherStation.update())
				{
					WeatherStationRecord rec=weatherStation.getRecord();
					StationRecord lastRecord=station.getLatestRecord();
					StationRecord record=station.createStationRecord();
					if (lastRecord!=null)
					{
						long a,b;
						a=lastRecord.getStamp().getTime();
						b=rec.getStamp().getTime();
						if (lastRecord.getStamp().getTime()==rec.getStamp().getTime())
						{
							// Avoid duplicates
							continue;
						}
					}
					record.setTemperature(rec.getTemperature());
					record.setHumadity(rec.getHumadity());
					record.setPressure(rec.getPressure());
					record.setWindDirection(rec.getWindDirection());
					record.setWindSpeed(rec.getWindSpeed());
					record.setStamp(rec.getStamp());
					sess.save(record);						
				}
			} catch (WeatherStationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedDevice e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				log.debug("Error when updating station "+station.getAlias(),e);				
			}
			
		}
		try {
			tr.commit();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	
	
}
