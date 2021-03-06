package org.idanso.stationinfo.fetch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.idanso.stationinfo.util.HibernateUtil;
import org.idanso.stationinfo.util.StationUtils;
import org.idanso.weather.Station;
import org.idanso.weather.StationRecord;
import org.idanso.weatherstation.UnsupportedDevice;
import org.idanso.weatherstation.WeatherStation;
import org.idanso.weatherstation.WeatherStationException;
import org.idanso.weatherstation.WeatherStationRecord;

public class StationUpdater extends TimerTask{
	
	// TODO: Find out why we have to deal with Log4JLogger directly..
	private static Log log=LogFactory.getLog(StationUpdater.class);
	
	private List updateStations;
	
	public StationUpdater() throws HibernateException
	{
		log.info("Initializing weather stations updater...");
		initUpdater();
	}
	
	
	private void initUpdater() throws HibernateException {
		Iterator it=StationUtils.getAllStations(false).iterator();
		updateStations=new ArrayList();
		while(it.hasNext())
		{
			Station station=(Station) it.next();
			String stationName=station.getAlias();			
			try {
				String className=station.getDriver();
				Map parameters=station.getParameters();
				Class klass=Class.forName(className);
				WeatherStation weatherStation=(WeatherStation) klass.newInstance();
				weatherStation.configure(parameters);
				Object[] arr=new Object[]{weatherStation,station};
				updateStations.add(arr);
			} 
			catch (ClassNotFoundException e)
			{
				log.error("Could not find driver for "+stationName);				
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	public void run() {
		try
		{
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
		catch(Exception e)
		{
			log.warn("Error when updating stations",e);							
		}
	}

	
	
	
	
}
