/*
 * $Id: ProviderStatComparator.java,v 1.1 2004/09/09 13:25:20 aron Exp $
 * Created on 9.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * 
 *  Last modified: $Date: 2004/09/09 13:25:20 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class ProviderStatComparator implements Comparator{
    
    Locale _locale;
	
	/**
	 * Constructor for SchoolComparator.
	 */
	public ProviderStatComparator(Locale locale) {
		_locale = locale;
	}

	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object o1, Object o2) {
		Collator collator = Collator.getInstance(_locale);
		
		ProviderStat stat1;
		try {
			stat1 = (ProviderStat) o1;
		}
		catch (ClassCastException e) {
			return -1;
		}
		
		ProviderStat stat2;
		try {
			stat2 = (ProviderStat) o2;
		}
		catch (ClassCastException e) {
			return 1;
		}
		
		return collator.compare(stat1.getProviderName(), stat2.getProviderName());
	}

}
