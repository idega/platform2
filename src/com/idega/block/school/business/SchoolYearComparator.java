package com.idega.block.school.business;

import java.util.Comparator;

import com.idega.block.school.data.SchoolYear;

/**
 * A class to compare a collection of <code>SchoolYear</code> objects.
 * @author Laddi
 */
public class SchoolYearComparator implements Comparator {
	
	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object o1, Object o2) {
		SchoolYear schoolYear1 = (SchoolYear) o1;
		SchoolYear schoolYear2 = (SchoolYear) o2;
		
		int yearAge1 = schoolYear1.getSchoolYearAge();
		int yearAge2 = schoolYear2.getSchoolYearAge();
		
		if (yearAge1 < yearAge2)
			return -1;
		else if (yearAge1 > yearAge2)
			return 1;
		else
			return 0;
	}
}