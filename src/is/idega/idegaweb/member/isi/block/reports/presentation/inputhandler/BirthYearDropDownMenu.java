/*
 * Created on Dec 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropDownMenuInputHandler;
import com.idega.util.IWTimestamp;
/**
 * A presentation object for dynamic reports. Select an age (1-123).
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class BirthYearDropDownMenu extends DropDownMenuInputHandler {

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	public BirthYearDropDownMenu() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
	
		IWTimestamp stamp = IWTimestamp.RightNow();		
		int currentYear = stamp.getYear();
		int beginningYear = 1900;				

		addMenuElement(" ",iwrb.getLocalizedString("BirthYearsDropdownmenu.all_ages", "All birthYears"));
		for (int i = currentYear; i >= beginningYear; i--) {
			addMenuElement(i, Integer.toString(i));
		}
		
		setSelectedElement(" ");
	}
	
	/**
	 * @return the year, Integer
	 *  
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		if (values != null && values.length > 0) {
			String birthYear = values[0];
			if (" ".equals(birthYear)) {
				return null;
			}
			else
				return new Integer(birthYear);
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
			return value.toString();
		}
		else
			return iwrb.getLocalizedString("BirthYearDropdownmenu.all_ages", "All birthYears");
	}
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}


}

