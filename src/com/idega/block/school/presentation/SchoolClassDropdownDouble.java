/*
 * Created on 8.5.2003
 */
package com.idega.block.school.presentation;

import com.idega.block.school.data.SchoolClass;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.SelectDropdownDouble;

/**
 * @author laddi
 */
public class SchoolClassDropdownDouble extends SelectDropdownDouble {

	public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";
	private String noClassListEntry = null;

	/**
	 * 
	 */
	public SchoolClassDropdownDouble() {
		super();
	}

	/**
	 * @param primaryName
	 * @param secondaryName
	 */
	public SchoolClassDropdownDouble(String primaryName, String secondaryName) {
		super(primaryName, secondaryName);
	}

	/**
	 * @see com.idega.presentation.ui.SelectDropdownDouble#getValue(java.lang.Object)
	 */
	protected String getValue(IWContext iwc, Object value) {
		if (value instanceof SchoolClass) {
			SchoolClass schoolClass = (SchoolClass) value;
			return schoolClass.getSchoolClassName();
		}
		else if(noClassListEntry!=null){
			return noClassListEntry;
		}
		else {
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
			return iwrb.getLocalizedString("school.select_class","Select class...");
		}
	}

	/**
	 * @see com.idega.presentation.ui.SelectDropdownDouble#getKey(java.lang.Object)
	 */
	protected String getKey(IWContext iwc, Object key) {
		if (key instanceof SchoolClass) {
			SchoolClass schoolClass = (SchoolClass) key;
			return schoolClass.getPrimaryKey().toString();
		}
		else {
			return key.toString();
		}
	}
	
	public void setNoDataListEntry(String entry){
		this.noClassListEntry = entry;
	}
}