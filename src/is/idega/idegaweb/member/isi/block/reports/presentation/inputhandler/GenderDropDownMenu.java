package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropDownMenuInputHandler;
/**
 * A presentation object for dynamic reports to genders. Both,male or female. both is default.
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class GenderDropDownMenu extends DropDownMenuInputHandler {

	private static final String MALE = "m"; //same as in workreportmember
	private static final String FEMALE = "f"; //same as in workreportmember
	private static final String BOTH = "b";

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	public GenderDropDownMenu() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.addMenuElement(MALE, iwrb.getLocalizedString("GenderDropdownmenu.male", "Male"));
		this.addMenuElement(FEMALE, iwrb.getLocalizedString("GenderDropdownmenu.female", "Female"));
		this.addMenuElement(BOTH, iwrb.getLocalizedString("GenderDropdownmenu.both", "Both"));
		this.setSelectedElement(BOTH);
	}
	
	/**
	 * @return the year, Integer
	 *  
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		if (values != null && values.length > 0) {
			String gender = values[0];
			if (BOTH.equals(gender)) {
				return null;
			}
			else
				return gender;
		}
		else
			return null;
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

}
