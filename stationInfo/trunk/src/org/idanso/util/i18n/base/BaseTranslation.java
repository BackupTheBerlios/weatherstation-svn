package org.idanso.util.i18n.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the translation table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="translation"
 */

public abstract class BaseTranslation  implements Serializable {

	public static String REF = "Translation";
	public static String PROP_LANG = "Lang";
	public static String PROP_SRC_STRING = "SrcString";
	public static String PROP_DST_STRING = "DstString";


	// constructors
	public BaseTranslation () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTranslation (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseTranslation (
		java.lang.Long id,
		java.lang.String lang,
		java.lang.String srcString) {

		this.setId(id);
		this.setLang(lang);
		this.setSrcString(srcString);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String lang;
	private java.lang.String srcString;
	private java.lang.String dstString;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="vm"
     *  column="id"
     */
	public java.lang.Long getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Long id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: lang
	 */
	public java.lang.String getLang () {
		return lang;
	}

	/**
	 * Set the value related to the column: lang
	 * @param lang the lang value
	 */
	public void setLang (java.lang.String lang) {
		this.lang = lang;
	}



	/**
	 * Return the value associated with the column: src_string
	 */
	public java.lang.String getSrcString () {
		return srcString;
	}

	/**
	 * Set the value related to the column: src_string
	 * @param srcString the src_string value
	 */
	public void setSrcString (java.lang.String srcString) {
		this.srcString = srcString;
	}



	/**
	 * Return the value associated with the column: dst_string
	 */
	public java.lang.String getDstString () {
		return dstString;
	}

	/**
	 * Set the value related to the column: dst_string
	 * @param dstString the dst_string value
	 */
	public void setDstString (java.lang.String dstString) {
		this.dstString = dstString;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.idanso.util.i18n.Translation)) return false;
		else {
			org.idanso.util.i18n.Translation translation = (org.idanso.util.i18n.Translation) obj;
			if (null == this.getId() || null == translation.getId()) return false;
			else return (this.getId().equals(translation.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		return super.toString();
	}


}