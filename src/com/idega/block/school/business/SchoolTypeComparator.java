package com.idega.block.school.business;

import java.text.Collator;
import java.util.Comparator;

import com.idega.block.school.data.SchoolType;

/**
 * A class to compare a collection of <code>SchoolType</code> objects.
 * @author Aron
 */
public class SchoolTypeComparator implements Comparator {
	
	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object o1, Object o2) {
		int returner = 0;
		SchoolType schoolType1 = (SchoolType) o1;
		SchoolType schoolType2 = (SchoolType) o2;
		
		
		returner = Collator.getInstance().compare(schoolType1.getSchoolTypeName(), schoolType2.getSchoolTypeName());

		return returner;
	}

}