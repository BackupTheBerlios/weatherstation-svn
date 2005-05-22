package org.idanso.stationinfo.util;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static Configuration cfg;
	private static SessionFactory factory;
	static
	{
		cfg=new Configuration();
		try {
			cfg.configure();
			factory=cfg.buildSessionFactory();
		} catch (MappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static final ThreadLocal session = new ThreadLocal();
	
	public static Session getSession() throws HibernateException
	{
		Session s = (Session) session.get();
		// Open a new Session, if this Thread has none yet
		if (s == null) {
			s = factory.openSession();
			session.set(s);
		}
		return s;	
	}
	
	public static void closeSession() throws HibernateException {
		Session s = (Session) session.get();
		session.set(null);
		if (s != null)
			s.close();
	}	
	
}
