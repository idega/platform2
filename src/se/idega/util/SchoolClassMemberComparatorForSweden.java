/*
 * $Id: SchoolClassMemberComparatorForSweden.java,v 1.2 2005/03/31 07:00:58 laddi Exp $
 * Created on Oct 21, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.util;

import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import com.idega.block.school.business.SchoolClassMemberComparator;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;


/**
 * Example:
 * This Comparator is used to change the behaviour of the 
 * SchoolClassMemberComparator.
 * 
 *  Last modified: $Date: 2005/03/31 07:00:58 $ by $Author: laddi $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.2 $
 */
public class SchoolClassMemberComparatorForSweden{
	
	  public static final int NAME_SORT = SchoolClassMemberComparator.NAME_SORT;
	  public static final int GENDER_SORT = SchoolClassMemberComparator.GENDER_SORT;
	  public static final int ADDRESS_SORT = SchoolClassMemberComparator.ADDRESS_SORT;
	  public static final int PERSONAL_ID_SORT = SchoolClassMemberComparator.PERSONAL_ID_SORT;
	  public static final int LANGUAGE_SORT = SchoolClassMemberComparator.LANGUAGE_SORT;
	
	 static public Comparator getComparatorSortBy(int sortBy, Locale locale, UserBusiness business, Map students) {
	  	return getComparator(sortBy, locale, business, students);
	  }
	  
	 static public Comparator getComparatorSortByName(Locale locale, UserBusiness business, Map students) {
	  	return getComparator(SchoolClassMemberComparator.NAME_SORT, locale, business, students);
	  }
	
	 
	 private static Comparator getComparator(int sortBy, Locale locale, UserBusiness business, Map students) {
	 	Comparator comparator = new Comparator() {
	 		public int compare(Object o1, Object o2) {
	 			return ((User) o1).getGenderID() - ((User) o2).getGenderID();
	 		}
	 	};
	 	return SchoolClassMemberComparator.getComparatorSortByUseGenderComparator(sortBy, comparator, locale, business, students);
	 }

	  
	 
	private SchoolClassMemberComparatorForSweden() {
		// use static  methods
	}

}
