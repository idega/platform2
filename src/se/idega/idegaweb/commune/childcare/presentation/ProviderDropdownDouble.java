/*
 * Created on 8.5.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import com.idega.block.school.data.School;
import com.idega.presentation.ui.SelectDropdownDouble;

/**
 * @author laddi
 */
public class ProviderDropdownDouble extends SelectDropdownDouble {

	/**
	 * 
	 */
	public ProviderDropdownDouble() {
		super();
	}

	/**
	 * @param primaryName
	 * @param secondaryName
	 */
	public ProviderDropdownDouble(String primaryName, String secondaryName) {
		super(primaryName, secondaryName);
	}

	/**
	 * @see com.idega.presentation.ui.SelectDropdownDouble#getValue(java.lang.Object)
	 */
	protected String getValue(Object value) {
		try {
			if (value instanceof School) {
				School school = (School) value;
				return school.getSchoolName();
			}
			return (String) value;
		}
		catch (ClassCastException e) {
			return (String) value;
		}
	}

	/**
	 * @see com.idega.presentation.ui.SelectDropdownDouble#getKey(java.lang.Object)
	 */
	protected String getKey(Object key) {
		try {
			School school = (School) key;
			return school.getPrimaryKey().toString();
		}
		catch (ClassCastException e) {
			return (String) key;
		}
	}
}