package org.idanso.weather.base;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import org.idanso.weather.dao.StationDAO;
import net.sf.hibernate.expression.Order;

/**
 * This is an automatically generated DAO class which should not be edited.
 */
public abstract class BaseStationDAO extends org.idanso.weather.dao._RootDAO {

	// query name references


	public static StationDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static StationDAO getInstance () {
		if (null == instance) instance = new StationDAO();
		return instance;
	}

	public Class getReferenceClass () {
		return org.idanso.weather.Station.class;
	}

    public Order getDefaultOrder () {
		return Order.asc("Title");
    }

	/**
	 * Cast the object as a org.idanso.weather.Station
	 */
	public org.idanso.weather.Station cast (Object object) {
		return (org.idanso.weather.Station) object;
	}

	public org.idanso.weather.Station get(java.lang.Long key)
		throws net.sf.hibernate.HibernateException {
		return (org.idanso.weather.Station) get(getReferenceClass(), key);
	}

	public org.idanso.weather.Station get(java.lang.Long key, Session s)
		throws net.sf.hibernate.HibernateException {
		return (org.idanso.weather.Station) get(getReferenceClass(), key, s);
	}

	public org.idanso.weather.Station load(java.lang.Long key)
		throws net.sf.hibernate.HibernateException {
		return (org.idanso.weather.Station) load(getReferenceClass(), key);
	}

	public org.idanso.weather.Station load(java.lang.Long key, Session s)
		throws net.sf.hibernate.HibernateException {
		return (org.idanso.weather.Station) load(getReferenceClass(), key, s);
	}

	public org.idanso.weather.Station loadInitialize(java.lang.Long key, Session s) 
			throws net.sf.hibernate.HibernateException { 
		org.idanso.weather.Station obj = load(key, s); 
		if (!Hibernate.isInitialized(obj)) {
			Hibernate.initialize(obj);
		} 
		return obj; 
	}


	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param station a transient instance of a persistent class 
	 * @return the class identifier
	 */
	public java.lang.Long save(org.idanso.weather.Station station)
		throws net.sf.hibernate.HibernateException {
		return (java.lang.Long) super.save(station);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * Use the Session given.
	 * @param station a transient instance of a persistent class
	 * @param s the Session
	 * @return the class identifier
	 */
	public java.lang.Long save(org.idanso.weather.Station station, Session s)
		throws net.sf.hibernate.HibernateException {
		return (java.lang.Long) save((Object) station, s);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param station a transient instance containing new or updated state 
	 */
	public void saveOrUpdate(org.idanso.weather.Station station)
		throws net.sf.hibernate.HibernateException {
		saveOrUpdate((Object) station);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default the
	 * instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the identifier
	 * property mapping. 
	 * Use the Session given.
	 * @param station a transient instance containing new or updated state.
	 * @param s the Session.
	 */
	public void saveOrUpdate(org.idanso.weather.Station station, Session s)
		throws net.sf.hibernate.HibernateException {
		saveOrUpdate((Object) station, s);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param station a transient instance containing updated state
	 */
	public void update(org.idanso.weather.Station station) 
		throws net.sf.hibernate.HibernateException {
		update((Object) station);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * Use the Session given.
	 * @param station a transient instance containing updated state
	 * @param the Session
	 */
	public void update(org.idanso.weather.Station station, Session s)
		throws net.sf.hibernate.HibernateException {
		update((Object) station, s);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param id the instance ID to be removed
	 */
	public void delete(java.lang.Long id)
		throws net.sf.hibernate.HibernateException {
		delete((Object) load(id));
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param id the instance ID to be removed
	 * @param s the Session
	 */
	public void delete(java.lang.Long id, Session s)
		throws net.sf.hibernate.HibernateException {
		delete((Object) load(id, s), s);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param station the instance to be removed
	 */
	public void delete(org.idanso.weather.Station station)
		throws net.sf.hibernate.HibernateException {
		delete((Object) station);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param station the instance to be removed
	 * @param s the Session
	 */
	public void delete(org.idanso.weather.Station station, Session s)
		throws net.sf.hibernate.HibernateException {
		delete((Object) station, s);
	}
	
	/**
	 * Re-read the state of the given instance from the underlying database. It is inadvisable to use this to implement
	 * long-running sessions that span many business tasks. This method is, however, useful in certain special circumstances.
	 * For example 
	 * <ul> 
	 * <li>where a database trigger alters the object state upon insert or update</li>
	 * <li>after executing direct SQL (eg. a mass update) in the same session</li>
	 * <li>after inserting a Blob or Clob</li>
	 * </ul>
	 */
	public void refresh (org.idanso.weather.Station station, Session s)
		throws net.sf.hibernate.HibernateException {
		refresh((Object) station, s);
	}


}