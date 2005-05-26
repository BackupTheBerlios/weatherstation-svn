package org.idanso.weatherstation.http;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.idanso.weatherstation.AbstractWeatherStation;
import org.idanso.weatherstation.WeatherStationException;

/**
 * A weather station which has fetches it's data source using HTTP.
 * 
 * @author idan
 *
 */

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

	/**
	 * Returns the current record validity time in miliseconds. the http source
	 * will not be requested again before this time passes since last request
	 * 
	 * @return
	 */
	protected abstract long getCacheTime();
	
	/**
	 * Handle the fetched HTTP request
	 * 
	 * @param method
	 * @return true if data changed since the last request
	 */
	protected abstract boolean handleHttpResponse(HttpMethod method);
	
	/**
	 * Returns HttpMethod(from common-httpclient package) instance which defines
	 * the http source url, parameters, etc..
	 * @return
	 */
	protected abstract HttpMethod getHttpMethod();


}
