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

import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;

/*
 * A Comparator for ReportableFields, used to sort ReportableData by one or more ReportableFields. Comparations
 * are done in the order of  the ReportableFields in the array.
 * If a field value is a number or a string starting with a number and a whitespace, the nubers are used for the
 * comparison, otherwise the comparison is alphanumerical using icelandic locale
 * @author jonas
 */
public class FieldsComparator implements Comparator {
	
	public FieldsComparator(ReportableField[] reportableFields) {
		this.reportableFields = reportableFields;
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
		int count = reportableFields.length;
		for(int i=0; i<count; i++) {
			ReportableField field = reportableFields[i];
			String fieldValue1 = (String) data1.getFieldValue(field);
			String fieldValue2 = (String) data2.getFieldValue(field);
			if(fieldValue1==fieldValue2) {
				continue;
			}
			if(fieldValue1==null) {
				return -1;
			} else if(fieldValue2==null) {
				return 1;
			}
			int i1 = getInt(fieldValue1);
			int i2 = getInt(fieldValue2);
			if(i1!=-1 && i2!=-1) {
				// found numbers to compare, use them
				if(i1!=i2) {
					return i1-i2;
				} else {
					continue;
				}
			}
			int comp = collator.compare(fieldValue1, fieldValue2);
			if(comp!=0) {
				return comp;
			}
		}
		return 0;
	}
	
	private int getInt(String str) {
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
	
	private ReportableField[] reportableFields;	
	private Collator collator = Collator.getInstance(new Locale("is","IS"));
}