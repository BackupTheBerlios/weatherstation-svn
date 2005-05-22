/**
 * 
 */
package org.idanso.stationinfo;

import org.idanso.service.ServiceException;
import org.idanso.service.ServiceManager;
import org.idanso.stationinfo.fetch.StationUpdateService;
import org.idanso.stationinfo.servlet.ServletService;

/**
 * @author idan
 * 
 * Launches StationInfo system
 *
 */
public class StationInfoLauncher {

	
	/**
	 * @param args
	 * @throws ServiceException 
	 */
	public static void main(String[] args) throws ServiceException {
		ServiceManager manager;
		StationUpdateService updateService;
		ServletService servletService;
		manager=ServiceManager.getInstance();
		updateService=new StationUpdateService();
		servletService=new ServletService();
		manager.addService(updateService);
		manager.addService(servletService);
		manager.startAll();		
	}

}
