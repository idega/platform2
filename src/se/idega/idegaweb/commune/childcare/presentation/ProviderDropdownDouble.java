/*
 * Created on 8.5.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.school.data.School;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
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
	protected String getValue(IWContext iwc, Object value) {
		if (value instanceof School) {
			School school = (School) value;
			return school.getSchoolName();
		}
		else {
			IWResourceBundle iwrb = iwc.getApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
			return iwrb.getLocalizedString("child_care.select_provider","Select provider...");
		}
	}

	/**
	 * @see com.idega.presentation.ui.SelectDropdownDouble#getKey(java.lang.Object)
	 */
	protected String getKey(IWContext iwc, Object key) {
		if (key instanceof School) {
			School school = (School) key;
			return school.getPrimaryKey().toString();
		}
		else {
			return (String) key;
		}
	}
}