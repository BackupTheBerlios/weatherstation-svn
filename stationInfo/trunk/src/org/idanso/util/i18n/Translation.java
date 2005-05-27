package org.idanso.util.i18n;

import org.idanso.util.i18n.base.BaseTranslation;



public class Translation extends BaseTranslation {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Translation () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Translation (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Translation (
		java.lang.Long id,
		java.lang.String lang,
		java.lang.String srcString) {

		super (
			id,
			lang,
			srcString);
	}

/*[CONSTRUCTOR MARKER END]*/


}