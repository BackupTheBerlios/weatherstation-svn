/**
 * 
 */
package org.idanso.stationinfo.util;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.idanso.util.i18n.Translation;

/**
 * @author idan
 *
 */
public class TranslationUtils {
	
	private static TranslationUtils instance;
	protected TranslationUtils()
	{
		
	}
	
	public static String getString(String string,List locales) throws HibernateException
	{
		Iterator it=locales.iterator();
		while(it.hasNext())
		{
			Locale locale=(Locale) it.next();
			Translation trans=findTranslation(string,locale);
			if (trans!=null)
			{
				String dst=trans.getDstString();
				if (dst!=null)
				{
					return dst;
				}				
			}
		}
		return string;
	}
	
	public static String getString(String string,Locale locale) throws HibernateException
	{
		Translation trans=findTranslation(string,locale);
		if (trans==null)
		{
			return string;
		}
		else
		{
			String dst=trans.getDstString();
			if (dst==null)
			{
				return string;
			}
			else
			{
				return dst;
			}
		}
	}
	
	
	public static Translation findTranslation(String string,Locale locale) throws HibernateException
	{
		Session session=HibernateUtil.getSession();
		Query q=session.createQuery("from Translation as trans where trans.Lang=? and trans.SrcString=?");
		q.setParameter(0,locale.getISO3Language());
		q.setParameter(1,string);
		List res=q.list();
		if (res.size()==1)
		{
			return (Translation) res.get(0);
		}
		else
		{
			// Generate a new translation row:-)
			Transaction tx=session.beginTransaction();
			Translation t=new Translation();
			t.setLang(locale.getISO3Language());
			t.setSrcString(string);
			session.save(t);
			tx.commit();
			return t;
		}
				
	}


	public static TranslationUtils getInstance() {
		if (instance==null)
		{
			instance=new TranslationUtils();
		}
		return instance;
	}

}
