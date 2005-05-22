package org.idanso.service;

import java.util.Properties;

/**
 * Defines the most basic operations of a service
 * 
 * @author idan
 *
 */
public interface Service {	
	public String getTitle();
	public void configure(Properties properties) throws ServiceException;
	public void start() throws ServiceException;
	public void stop() throws ServiceException;
	public boolean isRunning();	

}
