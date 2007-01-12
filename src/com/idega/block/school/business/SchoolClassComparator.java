package com.idega.block.school.business;

import java.text.Collator;
import java.util.Comparator;

import com.idega.block.school.data.SchoolClass;

/**
 * A class to compare a collection of <code>SchoolClass</code> objects.
 * @author Laddi
 */
public class SchoolClassComparator implements Comparator {
	
	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object o1, Object o2) {
		int returner = 0;
		SchoolClass schoolClass1 = (SchoolClass) o1;
		SchoolClass schoolClass2 = (SchoolClass) o2;
		
		returner = compareSubGroups(schoolClass1.getIsSubGroup(), schoolClass2.getIsSubGroup());
		
		if (returner == 0) {
			returner = Collator.getInstance().compare(schoolClass1.getSchoolClassName(), schoolClass2.getSchoolClassName());
		}

		return returner;
	}
	
	public int compareSubGroups(boolean subGroup1, boolean subGroup2) {
		if (!subGroup1 && subGroup2) {
			return -1;
		}
		else if (subGroup1 && !subGroup2) {
			return 1;
		}
		else {
			return 0;
		}
	}

}