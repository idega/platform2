package com.idega.block.school.business;

import java.text.Collator;
import java.util.Comparator;

import com.idega.block.school.data.SchoolType;
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
		
		if (schoolYear1.getSchoolTypeId() != schoolYear2.getSchoolTypeId()) {
			SchoolType type1 = null;
			try {
				type1 = schoolYear1.getSchoolType();
			}
			catch (Exception e) {
				type1 = null;
			}
			SchoolType type2 = null;
			try {
				type2 = schoolYear2.getSchoolType();
			}
			catch (Exception e) {
				type2 = null;
			}
			
			returner = compareTypes(type1, type2);
		}
		
		if (returner == 0) {
			returner = compareAge(schoolYear1.getSchoolYearAge(), schoolYear2.getSchoolYearAge());
		}
		if (returner == 0) {
			returner = Collator.getInstance().compare(schoolYear1.getSchoolYearName(), schoolYear2.getSchoolYearName());
		}

		return returner;
	}
	
	public int compareOrder(int order1, int order2) {
		if (order1 < order2) {
			return -1;
		}
		else if (order1 > order2) {
			return 1;
		}
		else {
			return 0;
		}
	}

	public int compareTypes(SchoolType schoolType1, SchoolType schoolType2) {
		int returner = 0;
		if (schoolType1 == null || schoolType2 == null) {
			if (schoolType1 == null && schoolType2 != null) {
				returner = 1;
			}
			else if (schoolType1 != null && schoolType2 == null) {
				returner = -1;
			}
			else {
				returner = 0;
			}
		}
		else {
			returner = compareOrder(schoolType1.getOrder(), schoolType2.getOrder());
			if (returner == 0) {
				int typeID1 = ((Integer) schoolType1.getPrimaryKey()).intValue();
				int typeID2 = ((Integer) schoolType2.getPrimaryKey()).intValue();
	
				if (typeID1 < typeID2) {
					returner = -1;
				}
				else if (typeID1 > typeID2) {
					returner = 1;
				}
				else {
					returner = 0;
				}
			}
		}
		
		return returner;
	}

	public int compareAge(int age1, int age2) {
		if (age1 < age2) {
			return -1;
		}
		else if (age1 > age2) {
			return 1;
		}
		else {
			return 0;
		}
	}
}