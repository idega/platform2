package com.idega.block.school.business;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.idega.block.school.data.School;

/**
 * A class to compare a collection of <code>School</code> objects.
 * @author Laddi
 */
public class SchoolComparator implements Comparator {

	Locale _locale;
	
	/**
	 * Constructor for SchoolComparator.
	 */
	public SchoolComparator(Locale locale) {
		this._locale = locale;
	}

	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object o1, Object o2) {
		Collator collator = Collator.getInstance(this._locale);
		
		School school1;
		try {
			school1 = (School) o1;
		}
		catch (ClassCastException e) {
			return -1;
		}
		
		School school2;
		try {
			school2 = (School) o2;
		}
		catch (ClassCastException e) {
			return 1;
		}
		
		return collator.compare(school1.getSchoolName(), school2.getSchoolName());
	}
}
