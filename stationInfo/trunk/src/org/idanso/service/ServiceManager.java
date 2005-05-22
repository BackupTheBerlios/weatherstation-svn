package org.idanso.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceManager {
	
	private static Log log=LogFactory.getLog(ServiceManager.class);
	
	
	private Timer timer=new Timer();
	public static String DEFAULT_MANAGER_ID="Default service manager";
	protected static Map managers=new HashMap();
	
	private List services;
	
	public static ServiceManager getInstance(String id)
	{
		synchronized (managers) {
			if (managers.containsKey(id))
			{
				return (ServiceManager) managers.get(id);
			}
			else
			{
				return new ServiceManager(id);
			}		
		}
	}
	
	public static ServiceManager getInstance()
	{
		return getInstance(DEFAULT_MANAGER_ID);
	}
	
	protected ServiceManager(String id)
	{
		synchronized (managers) {
			
			if (managers.containsKey(id))
			{
				throw new IllegalArgumentException("Service ID already exists");
			}
			else
			{
				services=new ArrayList();
				managers.put(id,this);
			}
		}
	}
	
	public int addService(Service service) throws ServiceException
	{
		int index=services.indexOf(service);
		if(index==-1)
		{
			synchronized(services)
			{
				services.add(service);
			}
			index=services.indexOf(service);
		}
		return index;
	}
	
	public void removeService(int index)
	{
		synchronized (services) {
			services.remove(index);
		}
		
	}
	
			
	public void removeService(Service service)
	{
		synchronized (services) {
			services.remove(service);
		}
		
	}
	
	public void startAll()  
	{
		log.info("Starting all services... ");
		synchronized (services) {
			for(int i=0;i<services.size();++i)
			{		
				start(i);
			}
		}
	}
	
	public void stopAll()
	{
		log.info("Stopping all services... ");
		synchronized (services) {
			for(int i=0;i<services.size();++i)
			{		
				stop(i);
			}
		}
	}
	
	public void start(int index)
	{
		synchronized (services) {			
			Service service=(Service) services.get(index);
			log.info("Starting service :"+service.getTitle());
			try {
				service.start();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				log.error("Count not start service "+service.getTitle(),e);
			}
		}		
	}
	
	public void stop(int index)
	{
		synchronized (services) {			
			Service service=(Service) services.get(index);
			log.info("Stopping service :"+service.getTitle());
			try {
				service.stop();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				log.error("Count not stop service "+service.getTitle(),e);
			}
		}		
	}

	public Timer getTimer() {
		return timer;
	}


}
