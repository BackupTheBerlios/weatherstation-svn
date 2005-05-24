package org.idanso.stationinfo.servlet;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.idanso.service.Service;
import org.idanso.service.ServiceException;
import org.mortbay.http.HttpServer;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.jetty.servlet.ServletHttpContext;
import org.mortbay.util.InetAddrPort;

/**
 * Provides embeded interface for station info using Jetty
 * 
 * @author idan
 *
 */
public class ServletService implements Service {
	
	private static String title="Web front-end";
	private static Log log=LogFactory.getLog(ServletService.class);
	protected HttpServer httpServer;
	protected Properties config;
	private String hostAddress="0.0.0.0:9000";
	private boolean isRunning=false;
	
	public ServletService()
	{
		config=new Properties();
	}
	

	public String getTitle() {
		return title;
	}

	public void configure(Properties properties) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public synchronized void start() throws ServiceException {
		if (!isRunning())
		{
			try {
				httpServer=new HttpServer();
				httpServer.addListener(new InetAddrPort(hostAddress));
				// Add servlet handler
				ServletHttpContext context=new ServletHttpContext();
				context.setContextPath("/");
				context.addServlet("/weather/*",WeatherServlet.class.getCanonicalName());
				httpServer.addContext(context);			
				// Add static content handler
				context=new ServletHttpContext();
				context.setContextPath("/static/*");
				context.setResourceBase("static/");
				ResourceHandler handler=new ResourceHandler();
				handler.setDirAllowed(true);
				context.addHandler(handler);
				httpServer.addContext(context);			
				httpServer.start();
				isRunning=true;
			} catch (Exception e) {
				log.fatal("Jetter init error",e);
				throw new ServiceException(e.getMessage());
			} 
		}
	}

	public synchronized void stop() throws ServiceException {
		if (isRunning())
		{
			try
			{
				httpServer.stop();
				isRunning=false;
			}
			catch(Exception e)
			{
				log.fatal("Jetter shutdown error",e);
				throw new ServiceException(e.getMessage());				
			}
			
			
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

}
