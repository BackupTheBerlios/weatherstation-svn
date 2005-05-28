/**
 * 
 */
package org.idanso.stationinfo;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import org.idanso.service.Service;
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

	private static String updateServiceClassName="org.idanso.stationinfo.fetch.StationUpdateService";
	private static String servletServiceClassName="org.idanso.stationinfo.servlet.ServletService";
	
	/**
	 * @param args
	 * @throws ServiceException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws ServiceException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		LongOpt[] options=new LongOpt[3];
		options[0]=new LongOpt("help",LongOpt.NO_ARGUMENT,null,'h');
		options[1]=new LongOpt("enable-jetty",LongOpt.NO_ARGUMENT,null,'s');
		options[2]=new LongOpt("disable-update",LongOpt.NO_ARGUMENT,null,'u');
		Getopt opt=new Getopt(StationInfoLauncher.class.getName(),args,"",options);
		int c;
		boolean useServlet=false;
		boolean useUpdate=true;
		while( (c=opt.getopt())!=-1)
		{
			switch(c)
			{
			case 'h':
				printHelp();
				System.exit(0);
				break;
			case 's':
				useServlet=true;
				break;
			case 'u':
				useUpdate=false;
				break;
			}
			
		}		
		ServiceManager manager;
		manager=ServiceManager.getInstance();
		if(useUpdate)
		{
			// Launch update service;
			manager.addService((Service) Class.forName(updateServiceClassName).newInstance());
		}
		if(useServlet)
		{
			// Launch servlet service;
			manager.addService((Service) Class.forName(servletServiceClassName).newInstance());
		}		
		manager.startAll();		
	}

	private static void printHelp() {
		System.out.println("Options for launcher:");
		System.out.println("--enable-jetty Starts weather servlet using jetty");
		System.out.println("--disable-update Do not launch update service");
		
	}

}
