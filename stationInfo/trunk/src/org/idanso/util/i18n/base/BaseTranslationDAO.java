package org.idanso.util.i18n.base;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import org.idanso.util.i18n.dao.TranslationDAO;
import net.sf.hibernate.expression.Order;

/**
 * This is an automatically generated DAO class which should not be edited.
 */
public abstract class BaseTranslationDAO extends org.idanso.util.i18n.dao._RootDAO {

	// query name references


	public static TranslationDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static TranslationDAO getInstance () {
		if (null == instance) instance = new TranslationDAO();
		return instance;
	}

	public Class getReferenceClass () {
		return org.idanso.util.i18n.Translation.class;
	}

    public Order getDefaultOrder () {
		return null;
    }

	/**
	 * Cast the object as a org.idanso.util.i18n.Translation
	 */
	public org.idanso.util.i18n.Translation cast (Object object) {
		return (org.idanso.util.i18n.Translation) object;
	}

	public org.idanso.util.i18n.Translation get(java.lang.Long key)
		throws net.sf.hibernate.HibernateException {
		return (org.idanso.util.i18n.Translation) get(getReferenceClass(), key);
	}

	public org.idanso.util.i18n.Translation get(java.lang.Long key, Session s)
		throws net.sf.hibernate.HibernateException {
		return (org.idanso.util.i18n.Translation) get(getReferenceClass(), key, s);
	}

	public org.idanso.util.i18n.Translation load(java.lang.Long key)
		throws net.sf.hibernate.HibernateException {
		return (org.idanso.util.i18n.Translation) load(getReferenceClass(), key);
	}

	public org.idanso.util.i18n.Translation load(java.lang.Long key, Session s)
		throws net.sf.hibernate.HibernateException {
		return (org.idanso.util.i18n.Translation) load(getReferenceClass(), key, s);
	}

	public org.idanso.util.i18n.Translation loadInitialize(java.lang.Long key, Session s) 
			throws net.sf.hibernate.HibernateException { 
		org.idanso.util.i18n.Translation obj = load(key, s); 
		if (!Hibernate.isInitialized(obj)) {
			Hibernate.initialize(obj);
		} 
		return obj; 
	}


	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param translation a transient instance of a persistent class 
	 * @return the class identifier
	 */
	public java.lang.Long save(org.idanso.util.i18n.Translation translation)
		throws net.sf.hibernate.HibernateException {
		return (java.lang.Long) super.save(translation);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * Use the Session given.
	 * @param translation a transient instance of a persistent class
	 * @param s the Session
	 * @return the class identifier
	 */
	public java.lang.Long save(org.idanso.util.i18n.Translation translation, Session s)
		throws net.sf.hibernate.HibernateException {
		return (java.lang.Long) save((Object) translation, s);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param translation a transient instance containing new or updated state 
	 */
	public void saveOrUpdate(org.idanso.util.i18n.Translation translation)
		throws net.sf.hibernate.HibernateException {
		saveOrUpdate((Object) translation);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default the
	 * instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the identifier
	 * property mapping. 
	 * Use the Session given.
	 * @param translation a transient instance containing new or updated state.
	 * @param s the Session.
	 */
	public void saveOrUpdate(org.idanso.util.i18n.Translation translation, Session s)
		throws net.sf.hibernate.HibernateException {
		saveOrUpdate((Object) translation, s);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param translation a transient instance containing updated state
	 */
	public void update(org.idanso.util.i18n.Translation translation) 
		throws net.sf.hibernate.HibernateException {
		update((Object) translation);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * Use the Session given.
	 * @param translation a transient instance containing updated state
	 * @param the Session
	 */
	public void update(org.idanso.util.i18n.Translation translation, Session s)
		throws net.sf.hibernate.HibernateException {
		update((Object) translation, s);
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
	 * @param translation the instance to be removed
	 */
	public void delete(org.idanso.util.i18n.Translation translation)
		throws net.sf.hibernate.HibernateException {
		delete((Object) translation);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param translation the instance to be removed
	 * @param s the Session
	 */
	public void delete(org.idanso.util.i18n.Translation translation, Session s)
		throws net.sf.hibernate.HibernateException {
		delete((Object) translation, s);
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
	public void refresh (org.idanso.util.i18n.Translation translation, Session s)
		throws net.sf.hibernate.HibernateException {
		refresh((Object) translation, s);
	}


}