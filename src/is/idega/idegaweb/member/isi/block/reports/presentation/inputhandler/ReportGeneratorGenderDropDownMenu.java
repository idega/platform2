package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.SelectionBox;
import com.idega.util.IWTimestamp;
/**
 * A presentation object for dynamic reports to genders. Both,male or female. both is default.
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class ReportGeneratorGenderDropDownMenu extends SelectionBox implements InputHandler {

	private static final String MALE = "1"; //same as in workreportmember
	private static final String FEMALE = "2"; //same as in workreportmember
	private static final String BOTH = "b";
	private static final String[] ALL = new String[] { MALE, FEMALE };
	
	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	public ReportGeneratorGenderDropDownMenu() {
		super();
	}

	private void initialize(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.addMenuElement(MALE, iwrb.getLocalizedString("GenderDropdownmenu.male", "Male"));
		this.addMenuElement(FEMALE, iwrb.getLocalizedString("GenderDropdownmenu.female", "Female"));
		this.addMenuElement(BOTH, iwrb.getLocalizedString("GenderDropdownmenu.both", "Both"));
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String, java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
		initialize(iwc);
		this.setName(name);
		if (value != null) {
			setSelectedElement(value);
		}
		else {
			setSelectedElement(BOTH);
		}
		return this;
	}

	/**
	 * @return genders as Collection of Integer objects
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		Collection genders =	new ArrayList();
		if(values == null || (values.length==1 && values[0].equals(BOTH))) {
			values = ALL;
		}
		for(int i=0; i<values.length; i++) {
			Integer gender = new Integer(values[i]);
			genders.add(gender);
		}
		return genders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.String, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		Collection values = (Collection) value;
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (values != null) {
			String displayName = "";
			if (values.size() != 1) {
				displayName = iwrb.getLocalizedString("GenderDropdownmenu.both", "Both");
			}
			else if (values.contains(new Integer(MALE))) {
					displayName = iwrb.getLocalizedString("GenderDropdownmenu.male", "Male");
			}
			else {
				displayName = iwrb.getLocalizedString("GenderDropdownmenu.female", "Female");
			}
			return displayName;
		}
		else
			return iwrb.getLocalizedString("GenderDropdownmenu.both", "Both");
	}
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public PresentationObject getHandlerObject(String name, Collection values, IWContext iwc) {
		String value = null;
		// if there are more than one value set value to null, then all values are set
		if (values != null && values.size() == 1) {
			value = (String) values.iterator().next();
		}			
		return getHandlerObject(name, value, iwc);
	}


	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#convertResultingObjectToType(java.lang.Object, java.lang.String)
	 */
	public Object convertSingleResultingObjectToType(Object value, String className) {
		return value;
	}
}
