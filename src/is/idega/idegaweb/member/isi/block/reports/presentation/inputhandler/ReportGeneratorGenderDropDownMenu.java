package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import java.util.ArrayList;
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

	public void main(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.addMenuElement(MALE, iwrb.getLocalizedString("GenderDropdownmenu.male", "Male"));
		this.addMenuElement(FEMALE, iwrb.getLocalizedString("GenderDropdownmenu.female", "Female"));
		this.addMenuElement(BOTH, iwrb.getLocalizedString("GenderDropdownmenu.both", "Both"));
		this.setSelectedElement(BOTH);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String, java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
		this.setName(name);

		if (value != null) {
			this.setSelectedElement(value);
		}

		return this;
	}

	/**
	 * @return the year, Integer
	 *  
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		Collection genders = null;
		if (values != null && values.length > 0) {
			if(values.length==1 && values[0].equals(BOTH)) {
				values = ALL;
			}
			genders = new ArrayList();
			
			for(int i=0; i<values.length; i++) {
				int age = Integer.parseInt(values[i]);
				
				IWTimestamp stamp = IWTimestamp.RightNow();
				
				stamp.addYears(-age);
				
				genders.add(stamp.toString());
			}
		} else {
			values = ALL;
		}
		return genders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.String, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (value != null) {
			
			String displayName = "";
			if (BOTH.equals(value)) {
				displayName = iwrb.getLocalizedString("GenderDropdownmenu.both", "Both");
			}
			else if (MALE.equals(value)) {
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
		String value = (String) Collections.min(values);
		return getHandlerObject(name, value, iwc);
	}


	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#convertResultingObjectToType(java.lang.Object, java.lang.String)
	 */
	public Object convertSingleResultingObjectToType(Object value, String className) {
		return value;
	}
}
