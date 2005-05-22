package org.idanso.stationinfo.fetch;

import java.util.Properties;

import net.sf.hibernate.HibernateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.soap.server.ServiceManager;
import org.idanso.service.Service;
import org.idanso.service.ServiceException;
import org.idanso.stationinfo.servlet.ServletService;

public class StationUpdateService implements Service {

	private StationUpdater updater=null;
	private static String title="Station fetch&update service";
	private static Log log=LogFactory.getLog(StationUpdateService.class);
	
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
				updater=new StationUpdater();
				org.idanso.service.ServiceManager.getInstance().getTimer().schedule(updater,0,60000);		
			} catch (HibernateException e) {
				log.error("Error in stationUpdateService",e);
				throw new ServiceException();
			}
		}
	}

	public synchronized void stop() throws ServiceException {
		if (isRunning())
		{
			updater.cancel();
			updater=null;
		}
	}

	public synchronized boolean isRunning() {
		return updater!=null;
	}

}
