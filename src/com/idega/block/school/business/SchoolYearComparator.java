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
		int returner = 0;
		SchoolYear schoolYear1 = (SchoolYear) o1;
		SchoolYear schoolYear2 = (SchoolYear) o2;
		
		returner = compareTypes(schoolYear1.getSchoolTypeId(), schoolYear2.getSchoolTypeId());
		if (returner == 0)
			returner = compareAge(schoolYear1.getSchoolYearAge(), schoolYear2.getSchoolYearAge());
		return returner;
	}
	
	public int compareTypes(int schoolType1, int schoolType2) {
		if (schoolType1 < schoolType2)
			return -1;
		else if (schoolType1 > schoolType2)
			return 1;
		else
			return 0;
	}

	public int compareAge(int age1, int age2) {
		if (age1 < age2)
			return -1;
		else if (age1 > age2)
			return 1;
		else
			return 0;
	}
}