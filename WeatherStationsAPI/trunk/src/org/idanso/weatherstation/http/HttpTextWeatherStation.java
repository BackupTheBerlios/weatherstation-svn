package org.idanso.weatherstation.http;

import java.util.Date;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.idanso.weatherstation.BaseWeatherStationRecord;
import org.idanso.weatherstation.WeatherStationRecord;

/**
 * Parses text content from HTTP, searching for formatted station data
 * 
 * Must be configured  using configure() with properties object which contains the 
 * "url" property - containing a legal http utl
 * 
 * @author idan
 *
 */

public class HttpTextWeatherStation extends HttpWeatherStation {
	
	private String url;
	private static Log log=LogFactory.getLog(HttpTextWeatherStation.class);
	private String[] MARKER_TEMPERATURE  = new String[] {"emperature: "};
	private String[] MARKER_HUMADITY  = new String[] {"umidity: "};
	private String[] MARKER_PRESSURE  = new String[] {"ressure :","arometer :","arometer:"};

	public HttpTextWeatherStation() throws ParserConfigurationException {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public void configure(Properties conf) {
		super.configure(conf);
		url=conf.getProperty("url");
	}


	protected HttpMethod getHttpMethod() {
		return new GetMethod(url);
	}

	public boolean hasTemperature() {
		return true;
	}

	public boolean hasHumadity() {
		return true;
	}

	public boolean hasPressure() {
		return true;
	}

	public boolean hasWindDirection() {
		return false;
	}

	public boolean hasWindSpeed() {
		return false;
	}

	protected long getCacheTime() {
		return 90000;
	}

	protected boolean handleHttpResponse(HttpMethod method) {
		String data=method.getResponseBodyAsString();
		float temperature=parseTemperature(data);
		float humadity=parseHumadity(data);
		float pressure=parsePressure(data);
		float windDirection;
		float windSpeed;
		WeatherStationRecord record= new BaseWeatherStationRecord(temperature,humadity,pressure,0,0,new Date());
		setRecord(record);
		return true;
	}

	
	private float parseFromText(String data,String[] markers,int length)
	{
		for(int i=0;i<markers.length;++i)
		{
			int index;
			String marker=markers[i];
			index=data.indexOf(marker)+marker.length();
			if (index>=marker.length())
			{
				try
				{
					return Float.parseFloat(data.substring(index,index+length));
				}
				catch(NumberFormatException e)
				{
				}
				
			}
			else
			{
			}
		}
		return 0;
	}

	private float parseTemperature(String data) {
		return parseFromText(data,MARKER_TEMPERATURE,4);
	}

	private float parseHumadity(String data) {
		return parseFromText(data,MARKER_HUMADITY,2);
	}
	
	private float parsePressure(String data) {
		return parseFromText(data,MARKER_PRESSURE,6);
	}






	
}
