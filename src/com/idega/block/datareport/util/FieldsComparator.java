/*
 * Created on Nov 12, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.idega.block.datareport.util;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/*
 * A Comparator for ReportableFields, used to sort ReportableData by one or more ReportableFields.
 * The default comparisons works as follows.
 * If a field value is a String containing a number, the numbers are used for the comparison. if the field is a String starting with a number and a whitespace,
 * first the String after the whitespace is compared, then the number before the whitespace. In other cases the comparison is alphanumerical using 
 * icelandic locale.
 * @author jonas
 */
public class FieldsComparator implements Comparator {
	
	/**
	 * Constructs a FieldsComparator that compares ReportableData objects using the given fields,
	 * comparing each of the fields using one of the given comparators
	 * @param reportableFields The fields to use for comparing 
	 * @param comparators The comparators to use, array must be same length as <code>reportableFields</code> array.
	 *        The i-th Comparator is used to compare the i-th ReportableField. If the i-th is <code>null</code>, then 
	 *        default comparison is used (same as if other constructor was used).
	 */
	public FieldsComparator(ReportableField[] reportableFields, Comparator[] comparators) {
		_reportableFields = reportableFields;
		_comparators = comparators;
	}
	
	/**
	 * Constructs a FieldsComparator that compares ReportableData objects using the given fields.
	 * @param reportableFields The fields to use for comparing 
	 */
	public FieldsComparator(ReportableField[] reportableFields) {
		_reportableFields = reportableFields;
		_comparators = null;
	}
	
	public int compare(Object o1, Object o2) {
		if(o1==o2) {
			return 0;		
		}
		ReportableData data1 = (ReportableData) o1;
		ReportableData data2 = (ReportableData) o2;
		if(data1==null) {
			return -1;
		} else if (data2==null) {
			return 1;
		}
		int count = _reportableFields.length;
		for(int i=0; i<count; i++) {
			ReportableField field = _reportableFields[i];
			Object fieldValue1 = data1.getFieldValue(field);
			Object fieldValue2 = data2.getFieldValue(field);
			if(fieldValue1==fieldValue2) {
				continue;
			}
			if(fieldValue1==null) {
				return -1;
			} else if(fieldValue2==null) {
				return 1;
			}
			int comp;
			if(_comparators == null || _comparators[i]==null) {
				if(fieldValue1 instanceof String) {
					// the two fieldvalues are always of the same type
					comp = defaultStringCompare((String) fieldValue1, (String) fieldValue2);
				} else {
					comp = _collator.compare(fieldValue1, fieldValue2);
				}
			} else {
				comp = _comparators[i].compare(fieldValue1, fieldValue2);
			}
			if(comp!=0) {
				return comp;
			}
		}
		return 0;
	}
	
	private int defaultStringCompare(String str0, String str1) {
		int comp = 0;
		
		int i1 = getInt(str0);
		int i2 = getInt(str1);
		if(i1!=-1 && i2!=-1) {
			// found numbers to compare, use them
			int dstr = _collator.compare(getStringAfterInt(str0), getStringAfterInt(str1));
			comp = dstr==0?(i1-i2):dstr;
		} else {
			comp = _collator.compare(str0, str1);
		}
		return comp;
	}
	
	private int getInt(String str) {
		try {
			int val = Integer.parseInt(str);
			return val;
		} catch(NumberFormatException e) {
			// don't care, continue searching for int
		}
		int i = str.indexOf(" ");
		String c1;
		if(i==-1) {
			c1 = str; 
		} else {
			c1 = str.substring(0, i);
		}
		try {
			return Integer.parseInt(c1);
		} catch(Exception e) {
			//e.printStackTrace();
			return -1;
		}
	}
	
	private String getStringAfterInt(String str) {
		int i = str.indexOf(" ");
		return str.substring(i+1);
	}
	
	private ReportableField[] _reportableFields;
	private Comparator[] _comparators;
	private Collator _collator = Collator.getInstance(new Locale("is","IS"));
}