/*
 * Created on Nov 24, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropDownMenuInputHandler;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class YesNoDropDownMenu extends DropDownMenuInputHandler {

	private final static String LOCALIZED_YES = "YesNoDropDownMenu.yes";
	private final static String LOCALIZED_NO = "YesNoDropDownMenu.no";

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	public final static String YES = "y";
	public final static String NO = "n";

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle rb = this.getResourceBundle(iwc);

		addMenuElement(YES, rb.getLocalizedString(LOCALIZED_YES, "Yes"));
		addMenuElement(NO, rb.getLocalizedString(LOCALIZED_NO, "No"));
		//setSelectedElement(NO);
	}
	
	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.Object, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		String displayName = "";
		if (value != null) {
			IWResourceBundle iwrb = getResourceBundle(iwc);
			if(YES.equals(value)) {
				return iwrb.getLocalizedString(LOCALIZED_YES, "Yes");
			} else if(NO.equals(value)) {
				return iwrb.getLocalizedString(LOCALIZED_NO, "No");
			}
		}
		return displayName;
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

}
