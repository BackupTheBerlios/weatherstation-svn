package org.idanso.weatherstation;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class HttpWeatherStation extends AbstractWeatherStation {

	private HttpClient client;
	private static Log log=LogFactory.getLog(HttpWeatherStation.class);
	
	protected HttpWeatherStation()
	{
		log.info("Initializing HTTP based weather station");
		client=new HttpClient();
	}
	
	
	
	public boolean update() throws WeatherStationException {
		log.debug("Update issued");
		Date lastUpdate=getLastUpdateTime();
		long cacheTime=getCacheTime();
		if(lastUpdate!=null && cacheTime>0)
		{
			log.debug("Checking record validity");
			long now=new Date().getTime();
			if(lastUpdate.getTime()+getCacheTime()>now )
			{
				log.debug("Record is still valid");
				return false;
			}
		}
		log.debug("Issueing http requeest");
		HttpMethod method=getHttpMethod();
		try {
			client.executeMethod(method);
			return handleHttpResponse(method);
		} catch (IllegalStateException e) {
			log.error("Ilegal state error occured",e);
		} catch (IOException e) {
			log.error("HTTP error occured",e);
		}
		return false;
		
	}


	protected abstract long getCacheTime();
	protected abstract boolean handleHttpResponse(HttpMethod method); 
	protected abstract HttpMethod getHttpMethod();


}
