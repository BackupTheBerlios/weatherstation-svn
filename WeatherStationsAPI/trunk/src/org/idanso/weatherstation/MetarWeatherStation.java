
package org.idanso.weatherstation;

import java.util.Date;
import java.util.Properties;

import net.sf.jweather.metar.Metar;
import net.sf.jweather.metar.MetarParseException;
import net.sf.jweather.metar.MetarParser;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implements a weather station by parsing METAR reports from NOAA. uses the
 * jweather package for parsing.
 * 
 * Must be configured  using configure() with properties object which contains the 
 * "station" property - marking a legal station code(such as LLBG)
 * 
 * @author idan
 *
 */

public class MetarWeatherStation extends HttpWeatherStation {

	public class MetarStationRecord implements WeatherStationRecord {

		public WeatherStation getStation() {
			// TODO Auto-generated method stub
			return null;
		}

		public float getTemperature() throws UnsupportedDevice {
			Float val=metar.getTemperatureInCelsius();
			if (val==null)
				return 0;
			else
				return val.floatValue();
		}

		public float getHumadity() throws UnsupportedDevice {
			// TODO: Generate from dewpoint
			return 0;
		}

		public float getPressure() throws UnsupportedDevice {
			Float val=metar.getPressure();
			if (val==null)
				return 0;
			else
				return val.floatValue();
		}

		public float getWindDirection() throws UnsupportedDevice {
			Integer val=metar.getWindDirection();
			if (val==null)
				return 0;
			else
				return val.floatValue();
		}

		public float getWindSpeed() throws UnsupportedDevice {
			Float val=metar.getWindSpeedInMPS();
			if (val==null)
				return 0;
			else
				return val.floatValue()*3600/1000;
		}

		public Date getStamp() {
			if (metar!=null)
			{
				return metar.getDate();
			}
			else
			{
				return null;
			}
		}

	}



	private static Log log=LogFactory.getLog(MetarWeatherStation.class);
	protected static String MetarBaseUrl="http://weather.noaa.gov/pub/data/observations/metar/stations/";
	protected static long cacheTime=60000*60;
	private String stationId;
	private String stationUrl;
	private Metar metar;
	
	
	public void configure(Properties conf) {
		super.configure(conf);
		stationId=conf.getProperty("station").toUpperCase();
		stationUrl=MetarBaseUrl+stationId+".TXT";
		setRecord(new MetarStationRecord());
	}
	
	protected long getCacheTime() {
		return cacheTime;
	}

	protected boolean handleHttpResponse(HttpMethod method) {
		try {
			Metar newMetar;
			newMetar=MetarParser.parse(method.getResponseBodyAsString()+"\n");
			if (metar==null)
			{
				metar=newMetar;
				return true;				
			}
			else if (!newMetar.getDate().equals(metar.getDate()))
			{
				metar=newMetar;
				return true;
			}
			else
			{
				return false;
			}
		} catch (MetarParseException e) {
			log.error("Could not fetch METAR for "+stationId,e);
			return false;
		}

	}

	protected HttpMethod getHttpMethod() {
		return new GetMethod(stationUrl);
	}

	public boolean hasTemperature() {
		return metar!=null;
	}

	public boolean hasHumadity() {
		return metar!=null;
	}

	public boolean hasPressure() {
		return metar!=null;
	}

	public boolean hasWindDirection() {
		return metar!=null;
	}

	public boolean hasWindSpeed() {
		return metar!=null;
	}
	
	

	public String getStationId() {
		return stationId;
	}

}
