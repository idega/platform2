package com.idega.block.school.business;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.idega.block.school.data.SchoolArea;

/**
 * A class to compare a collection of <code>SchoolArea</code> objects.
 * @author Laddi
 */
public class SchoolAreaComparator implements Comparator {

	Locale _locale;
	
	/**
	 * Constructor for SchoolComparator.
	 */
	public SchoolAreaComparator(Locale locale) {
		_locale = locale;
	}

	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object o1, Object o2) {
		Collator collator = Collator.getInstance(_locale);
		
		SchoolArea schoolArea1 = (SchoolArea) o1;
		SchoolArea schoolArea2 = (SchoolArea) o2;
		
		return collator.compare(schoolArea1.getName(), schoolArea2.getName());
	}
}
