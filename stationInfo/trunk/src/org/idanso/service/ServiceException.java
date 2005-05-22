package org.idanso.service;

public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3257848775023473207L;
	private String message;
	
	public ServiceException(String message) {
		this.message=message;
	}
	
	public ServiceException() {
		// TODO Auto-generated constructor stub
	}

	public String getMessage()
	{
		return message;
	}


}
