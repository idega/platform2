/*
 * Created on Nov 24, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package se.idega.idegaweb.commune.childcare.presentation.inputhandler;

import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropDownMenuInputHandler;

/**
 * @author laddi
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ChoicesDropDownMenu extends DropDownMenuInputHandler  {

	private final static String LOCALIZED_YES = "child_care.first_choice_only";
	private final static String LOCALIZED_NO = "child_care.all_choices";

	protected static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	public final static String YES = Boolean.TRUE.toString();
	public final static String NO = Boolean.FALSE.toString();

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle rb = this.getResourceBundle(iwc);

		addMenuElement(NO, rb.getLocalizedString(LOCALIZED_NO, "All choices"));
		addMenuElement(YES, rb.getLocalizedString(LOCALIZED_YES, "First hand choices only"));
	}
	
	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getResultingObject(java.lang.String[], com.idega.presentation.IWContext)
	 */
	public Object getResultingObject(String[] value, IWContext iwc) throws Exception {
		return new Boolean(value[0]);
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.Object, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if(YES.equals(value.toString())) {
			return iwrb.getLocalizedString(LOCALIZED_YES, "Yes");
		}
		else {
			return iwrb.getLocalizedString(LOCALIZED_NO, "No");
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}


}
