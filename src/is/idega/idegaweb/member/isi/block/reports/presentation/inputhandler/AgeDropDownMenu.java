package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;

import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;
/**
 * A presentation object for dynamic reports. Select an age (1-123).
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class AgeDropDownMenu extends DropdownMenu implements InputHandler {

	private static final int youngest = 1;

	private static final int oldest = 123;

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	public AgeDropDownMenu() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		addMenuElement(" ",iwrb.getLocalizedString("AgeDropdownmenu.all_ages", "All ages"));
		for (int i = youngest; i <= oldest; i++) {
			addMenuElement(i, Integer.toString(i));
		}
		
		setSelectedElement(" ");
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
		if (values != null && values.length > 0) {
			String age = values[0];
			if (" ".equals(age)) {
				return null;
			}
			else
				return new Integer(age);
		}
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.String, com.idega.presentation.IWContext)
	 */
	public String getDisplayNameOfValue(Object value, IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (value != null) {
			return value.toString();
		}
		else
			return iwrb.getLocalizedString("AgeDropdownmenu.all_ages", "All ages");
	}
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}
