package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;

import java.util.ArrayList;
import java.util.Collection;

import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.IWTimestamp;
/**
 * A presentation object for dynamic reports to genders. Both,male or female. both is default.
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class ReportGeneratorGenderDropDownMenu extends DropdownMenu implements InputHandler {

	private static final String MALE = "1"; //same as in workreportmember
	private static final String FEMALE = "2"; //same as in workreportmember
	private static final String BOTH = "b";
	private static final String[] ALL = new String[] { MALE, FEMALE };
	
	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private WorkReportBusiness workBiz = null;

	public ReportGeneratorGenderDropDownMenu() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
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
	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc) {
		this.setName(name);

		if (stringValue != null) {
			this.setSelectedElement(stringValue);
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
		}
		return genders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.String, com.idega.presentation.IWContext)
	 */
	public String getDisplayNameOfValue(Object value, IWContext iwc) {
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
