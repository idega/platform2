/*
 * $Id: RegulationComparator.java,v 1.2 2003/10/14 10:33:15 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.presentation;

import java.util.Comparator;
import com.idega.data.GenericEntity;
import se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulation;

/**
 * RegulationComparator compares objects for the dropdowns
 * <p>
 * $Id: RegulationComparator.java,v 1.2 2003/10/14 10:33:15 kjell Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.2 $
 */
public class RegulationComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		if (o1 instanceof GenericEntity) {
			String s1 = o1.toString();
			String s2 = o2.toString();
			if (o1.getClass().getName().indexOf("AgeRegulationBMPBean") != -1) {
				// Nasty hack to get the AgeRegulation entity with special methods to order properly
				// Gotta find a better way to do this /Kelly
				s1 = ""+((AgeRegulation) o1).getAgeInterval();
				s2 = ""+((AgeRegulation) o2).getAgeInterval();
			}
			return s1.compareTo(s2);
		} else if (o1 instanceof Object []){
			Object [] co1 = (Object []) o1;
			Object [] co2 = (Object []) o2;
			int cint1 = ((Integer) co1[0]).intValue();
			int cint2 = ((Integer) co2[0]).intValue();
			return cint1 - cint2;
		} else {
			String s1 = o1.toString();
			String s2 = o2.toString();
			return s1.compareTo(s2);
		}
	}
}