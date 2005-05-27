package org.idanso.util.i18n.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.SessionFactory;

import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Order;


public abstract class _BaseRootDAO {

	protected static Map sessionFactoryMap;
	protected static SessionFactory sessionFactory;
	protected static ThreadLocal mappedSessions;
	protected static ThreadLocal sessions;

	/**
	 * Configure the session factory by reading hibernate config file
	 */
	public static void initialize () throws HibernateException {
		org.idanso.util.i18n.dao._RootDAO.initialize(
			(String) null);
	}
	
	/**
	 * Configure the session factory by reading hibernate config file
	 * @param configFileName the name of the configuration file
	 */
	public static void initialize (String configFileName) throws HibernateException {
		org.idanso.util.i18n.dao._RootDAO.initialize(
			configFileName,
			org.idanso.util.i18n.dao._RootDAO.getNewConfiguration(
				null));
	}

	public static void initialize (String configFileName, Configuration configuration) throws HibernateException {
		if (null == configFileName && null != sessionFactory) return;
		else if (null != sessionFactoryMap && null != sessionFactoryMap.get(configFileName)) return;
		else {
			if (null == configFileName) {
				configuration.configure();
				org.idanso.util.i18n.dao._RootDAO.setSessionFactory(
					configuration.buildSessionFactory());
			}
			else {
				configuration.configure(
					configFileName);
				org.idanso.util.i18n.dao._RootDAO.setSessionFactory(
					configFileName,
					configuration.buildSessionFactory());
			}
		}
	}

	/**
	 * Set the session factory
	 */
	protected static void setSessionFactory (SessionFactory sessionFactory) {
		setSessionFactory(
			(String) null,
			sessionFactory);
	}

	/**
	 * Set the session factory
	 */
	protected static void setSessionFactory (String configFileName, SessionFactory sf) {
		if (null == configFileName) {
			sessionFactory = sf;
		}
		else {
			if (null == sessionFactoryMap) sessionFactoryMap = new HashMap();
			sessionFactoryMap.put(
				configFileName,
				sessionFactory);
		}
	}

	/**
	 * Return the SessionFactory that is to be used by these DAOs.  Change this
	 * and implement your own strategy if you, for example, want to pull the SessionFactory
	 * from the JNDI tree.
	 */
	protected SessionFactory getSessionFactory() {
		return getSessionFactory(
		getConfigurationFileName());
	}

	protected SessionFactory getSessionFactory(String configFile) {
		if (null == configFile) {
			if (null == sessionFactory)
				throw new RuntimeException("The session factory has not been initialized (or an error occured during initialization)");
			else
				return sessionFactory;
		}
		else {
			if (null == sessionFactoryMap)
				throw new RuntimeException("The session factory for '" + configFile + "' has not been initialized (or an error occured during initialization)");
			else {
				SessionFactory sf = (SessionFactory) sessionFactoryMap.get(configFile);
				if (null == sf)
					throw new RuntimeException("The session factory for '" + configFile + "' has not been initialized (or an error occured during initialization)");
				else
					return sf;
			}
		}
	}

	/**
	 * Return a new Session object that must be closed when the work has been completed.
	 * @return the active Session
	 */
	public Session getSession() throws HibernateException {
		return getSession(
			getConfigurationFileName(),
			false);
	}

	/**
	 * Return a new Session object that must be closed when the work has been completed.
	 * @return the active Session
	 */
	public Session createNewSession() throws HibernateException {
		return getSession(
			getConfigurationFileName(),
			true);
	}

	/**
	 * Return a new Session object that must be closed when the work has been completed.
	 * @param configFile the config file must match the meta attribute "config-file" in the hibernate mapping file
	 * @return the active Session
	 */
	private Session getSession(String configFile, boolean createNew) throws HibernateException {
		if (createNew) {
			return getSessionFactory(configFile).openSession();
		}
		else {
			if (null == configFile) {
				if (null == sessions) sessions = new ThreadLocal();
				Session session = (Session) sessions.get();
				if (null == session || !session.isOpen()) {
					session = getSessionFactory(null).openSession();
					sessions.set(session);
				}
				return session;
			}
			else {
				if (null == mappedSessions) mappedSessions = new ThreadLocal();
				java.util.HashMap map = (java.util.HashMap) mappedSessions.get();
				if (null == map) {
					map = new HashMap(1);
					mappedSessions.set(map);
				}
				Session session = (Session) map.get(configFile);
				if (null == session || !session.isOpen()) {
					session = getSessionFactory(configFile).openSession();
					map.put(configFile, session);
				}
				return session;
			}
		}
	}

	/**
	 * Close all sessions for the current thread
	 */
	public static void closeCurrentThreadSessions () throws HibernateException {
		if (null != sessions) {
			Session session = (Session) sessions.get();
			if (null != session && session.isOpen()) {
				session.close();
			}
		}
		if (null != mappedSessions) {
			java.util.HashMap map = (java.util.HashMap) mappedSessions.get();
			if (null != map) {
				HibernateException thrownException = null;
				Session session = null;
				for (Iterator i=map.values().iterator(); i.hasNext(); ) {
					session = (Session) i.next();
					try {
						if (null != session && session.isOpen()) {
							session.close();
						}
					}
					catch (HibernateException e) {
						thrownException = e;
					}
				}
				map.clear();
				if (null != thrownException) throw thrownException;
			}
		}
	}

	/**
	 * Close the session
	 */
	public void closeSession (Session session) throws HibernateException {
		if (null != session) session.close();
	}

	/**
	 * Begin the transaction related to the session
	 */
	public Transaction beginTransaction(Session s) throws HibernateException {
		return s.beginTransaction();
	}

	/**
	 * Commit the given transaction
	 */
	public void commitTransaction(Transaction t) throws HibernateException {
		t.commit();
	}

	/**
	 * Return a new Configuration to use
	 */
	 public static Configuration getNewConfiguration (String configFileName) {
	 	return new Configuration();
	 }
	
	/**
	 * Return the name of the configuration file to be used with this DAO or null if default
	 */
	public String getConfigurationFileName () {
		return null;
	}

	/**
	 * Return the specific Object class that will be used for class-specific
	 * implementation of this DAO.
	 * @return the reference Class
	 */
	protected abstract Class getReferenceClass();

	/**
	 * Used by the base DAO classes but here for your modification
	 * Get object matching the given key and return it.
	 */
	protected Object get(Class refClass, Serializable key) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return get(refClass, key, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Get object matching the given key and return it.
	 */
	protected Object get(Class refClass, Serializable key, Session s) throws HibernateException {
		return s.get(refClass, key);
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Load object matching the given key and return it.
	 */
	protected Object load(Class refClass, Serializable key) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return load(refClass, key, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Load object matching the given key and return it.
	 */
	protected Object load(Class refClass, Serializable key, Session s) throws HibernateException {
		return s.load(refClass, key);
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List findAll () throws HibernateException {
		Session s = null;
		try {
			s = getSession();
    		return findAll(s);
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * Use the session given.
	 * @param s the Session
	 */
	public java.util.List findAll (Session s) throws HibernateException {
   		return findAll(s, getDefaultOrder());
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List findAll (Order defaultOrder) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return findAll(s, defaultOrder);
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * Use the session given.
	 * @param s the Session
	 */
	public java.util.List findAll (Session s, Order defaultOrder) throws HibernateException {
		Criteria crit = s.createCriteria(getReferenceClass());
		if (null != defaultOrder) crit.addOrder(defaultOrder);
		return crit.list();
	}

	/**
	 * Return all objects related to the implementation of this DAO with a filter.
	 * Use the session given.
	 * @param propName the name of the property to use for filtering
	 * @param filter the value of the filter
	 */
	protected java.util.List findFiltered (String propName, Object filter) throws HibernateException {
		return findFiltered(propName, filter, getDefaultOrder());
	}

	/**
	 * Return all objects related to the implementation of this DAO with a filter.
	 * Use the session given.
	 * @param propName the name of the property to use for filtering
	 * @param filter the value of the filter
	 * @param orderProperty the name of the property used for ordering
	 */
	protected java.util.List findFiltered (String propName, Object filter, Order order) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return findFiltered(s, propName, filter, getDefaultOrder());
		}
		finally {
			closeSession(s);
		}
	}
	
	/**
	 * Return all objects related to the implementation of this DAO with a filter.
	 * Use the session given.
	 * @param s the Session
	 * @param propName the name of the property to use for filtering
	 * @param filter the value of the filter
	 * @param orderProperty the name of the property used for ordering
	 */
	protected java.util.List findFiltered (Session s, String propName, Object filter, Order order) throws HibernateException {
		Criteria crit = s.createCriteria(getReferenceClass());
		crit.add(Expression.eq(propName, filter));
		if (null != order) crit.addOrder(order);
		return crit.list();
	}
	
	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * @param name the name of a query defined externally 
	 * @return Query
	 */
	protected Query getNamedQuery(String name) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getNamedQuery(name, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the session given.
	 * @param name the name of a query defined externally 
	 * @param s the Session
	 * @return Query
	 */
	protected Query getNamedQuery(String name, Session s) throws HibernateException {
		Query q = s.getNamedQuery(name);
		return q;
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * @param name the name of a query defined externally 
	 * @param param the first parameter to set
	 * @return Query
	 */
	protected Query getNamedQuery(String name, Serializable param) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getNamedQuery(name, param, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the session given.
	 * @param name the name of a query defined externally 
	 * @param param the first parameter to set
	 * @param s the Session
	 * @return Query
	 */
	protected Query getNamedQuery(String name, Serializable param, Session s) throws HibernateException {
		Query q = s.getNamedQuery(name);
		q.setParameter(0, param);
		return q;
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter array
	 * @return Query
	 */
	protected Query getNamedQuery(String name, Serializable[] params) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getNamedQuery(name, params, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given and the Session given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter array
	 * @s the Session
	 * @return Query
	 */
	protected Query getNamedQuery(String name, Serializable[] params, Session s) throws HibernateException {
		Query q = s.getNamedQuery(name);
		if (null != params) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return q;
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter Map
	 * @return Query
	 */
	protected Query getNamedQuery(String name, Map params) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getNamedQuery(name, params, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given and the Session given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter Map
	 * @s the Session
	 * @return Query
	 */
	protected Query getNamedQuery(String name, Map params, Session s) throws HibernateException {
		Query q = s.getNamedQuery(name);
		if (null != params) {
			for (Iterator i=params.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry entry = (Map.Entry) i.next();
				q.setParameter((String) entry.getKey(), entry.getValue());
			}
		}
		return q;
	}

	/**
	 * Execute a query. 
	 * @param queryStr a query expressed in Hibernate's query language
	 * @return a distinct list of instances (or arrays of instances)
	 */
	public Query getQuery(String queryStr) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getQuery(queryStr, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Execute a query but use the session given instead of creating a new one.
	 * @param queryStr a query expressed in Hibernate's query language
	 * @s the Session to use
	 */
	public Query getQuery(String queryStr, Session s) throws HibernateException {
		return s.createQuery(queryStr);
	}



	/**
	 * Execute a query. 
	 * @param query a query expressed in Hibernate's query language
	 * @param queryStr the name of a query defined externally 
	 * @param param the first parameter to set
	 * @return Query
	 */
	protected Query getQuery(String queryStr, Serializable param) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getQuery(queryStr, param, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Execute a query but use the session given instead of creating a new one.
	 * @param queryStr a query expressed in Hibernate's query language
	 * @param param the first parameter to set
	 * @s the Session to use
	 * @return Query
	 */
	protected Query getQuery(String queryStr, Serializable param, Session s) throws HibernateException {
		Query q = getQuery(queryStr, s);
		q.setParameter(0, param);
		return q;
	}

	/**
	 * Execute a query. 
	 * @param queryStr a query expressed in Hibernate's query language
	 * @param params the parameter array
	 * @return Query
	 */
	protected Query getQuery(String queryStr, Serializable[] params) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getQuery(queryStr, params, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Execute a query but use the session given instead of creating a new one.
	 * @param queryStr a query expressed in Hibernate's query language
	 * @param params the parameter array
	 * @s the Session
	 * @return Query
	 */
	protected Query getQuery(String queryStr, Serializable[] params, Session s) throws HibernateException {
		Query q = getQuery(queryStr, s);
		if (null != params) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return q;
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given.
	 * @param queryStr a query expressed in Hibernate's query language
	 * @param params the parameter Map
	 * @return Query
	 */
	protected Query getQuery(String queryStr, Map params) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getQuery(queryStr, params, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given and the Session given.
	 * @param queryStr a query expressed in Hibernate's query language
	 * @param params the parameter Map
	 * @s the Session
	 * @return Query
	 */
	protected Query getQuery(String queryStr, Map params, Session s) throws HibernateException {
		Query q = getQuery(queryStr, s);
		if (null != params) {
			for (Iterator i=params.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry entry = (Map.Entry) i.next();
				q.setParameter((String) entry.getKey(), entry.getValue());
			}
		}
		return q;
	}

	protected Order getDefaultOrder () {
		return null;
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Persist the given transient instance, first assigning a generated identifier. 
	 * (Or using the current value of the identifier property if the assigned generator is used.) 
	 */
	protected Serializable save(Object obj) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			Serializable rtn = save(obj, s);
			commitTransaction(t);
			return rtn;
		}
		catch (HibernateException e) {
			if (null != t) t.rollback();
            throw e;
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Persist the given transient instance, first assigning a generated identifier. 
	 * (Or using the current value of the identifier property if the assigned generator is used.) 
	 */
	protected Serializable save(Object obj, Session s) throws HibernateException {
		return s.save(obj);
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Either save() or update() the given instance, depending upon the value of its
	 * identifier property.
	 */
	protected void saveOrUpdate(Object obj) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			saveOrUpdate(obj, s);
			commitTransaction(t);
		}
		catch (HibernateException e) {
			if (null != t) t.rollback();
            throw e;
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Either save() or update() the given instance, depending upon the value of its
	 * identifier property.
	 */
	protected void saveOrUpdate(Object obj, Session s) throws HibernateException {
		s.saveOrUpdate(obj);
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param obj a transient instance containing updated state
	 */
	protected void update(Object obj) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			update(obj, s);
			commitTransaction(t);
		}
		catch (HibernateException e) {
			if (null != t) t.rollback();
            throw e;
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param obj a transient instance containing updated state
	 * @param s the Session
	 */
	protected void update(Object obj, Session s) throws HibernateException {
		s.update(obj);
	}

	/**
	 * Delete all objects returned by the query
	 */
	protected int delete (Query query) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			int rtn = delete (query, s);
			commitTransaction(t);
			return rtn;
		}
		catch (HibernateException e) {
			if (null != t) t.rollback();
            throw e;
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Delete all objects returned by the query
	 */
	protected int delete (Query query, Session s) throws HibernateException {
		List list = query.list();
		for (Iterator i=list.iterator(); i.hasNext(); ) {
			delete(i.next(), s);
		}
		return list.size();
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 */
	protected void delete(Object obj) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			delete(obj, s);
			commitTransaction(t);
		}
		catch (HibernateException e) {
			if (null != t) t.rollback();
            throw e;
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 */
	protected void delete(Object obj, Session s) throws HibernateException {
		s.delete(obj);
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Re-read the state of the given instance from the underlying database. It is inadvisable to use this to implement
	 * long-running sessions that span many business tasks. This method is, however, useful in certain special circumstances.
	 */
	protected void refresh(Object obj, Session s) throws HibernateException {
		s.refresh(obj);
	}


}