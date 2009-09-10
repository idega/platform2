/*
 * $Id: BoxComparator.java,v 1.1.2.1 2007/01/12 19:32:11 idegaweb Exp $
 * Created on 19.1.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.boxoffice.business;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.idega.block.boxoffice.data.BoxLink;
import com.idega.core.localisation.business.ICLocaleBusiness;


/**
 * Last modified: $Date: 2007/01/12 19:32:11 $ by $Author: idegaweb $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1.2.1 $
 */
public class BoxComparator implements Comparator {

	private Locale iLocale;
	
	public BoxComparator(Locale locale) {
		this.iLocale = locale;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		BoxLink box1 = (BoxLink) arg0;
		BoxLink box2 = (BoxLink) arg1;
		int localeID = ICLocaleBusiness.getLocaleId(this.iLocale);

		String text1 = BoxBusiness.getLocalizedString(box1, localeID);
		String text2 = BoxBusiness.getLocalizedString(box2, localeID);
		
		Collator collator = Collator.getInstance(this.iLocale);
		
		if (text1 != null && text2 != null) {
			return collator.compare(text1, text2);
		}
		else if (text1 != null && text2 == null) {
			return -1;
		}
		else if (text1 == null && text2 != null) {
			return 1;
		}
		return 0;
	}
}