/*
 * Created on Nov 22, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropDownMenuInputHandler;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WorkReportStatusDropDownMenu extends DropDownMenuInputHandler {

	public static final String LOCALIZED_STATUS_ALL = "WorkReportStatusDropDownMenu.status_all";
	public static final String LOCALIZED_STATUS_DONE = "WorkReportStatusDropDownMenu.status_done";
	public static final String LOCALIZED_STATUS_NOT_DONE = "WorkReportStatusDropDownMenu.status_not_done";
	public static final String LOCALIZED_STATUS_NO_REPORT = "WorkReportStatusDropDownMenu.status_no_report";
	public static final String LOCALIZED_STATUS_SOME_DONE = "WorkReportStatusDropDownMenu.status_some_done";
	
	public static final String STATUS_ALL = "1";
	public static final String STATUS_DONE = "2";
	public static final String STATUS_NOT_DONE = "3";
	public static final String STATUS_NO_REPORT = "4";
	public static final String STATUS_SOME_DONE = "5";

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle rb = this.getResourceBundle(iwc);
	
		addMenuElement(STATUS_ALL, rb.getLocalizedString(LOCALIZED_STATUS_ALL, "Any Status"));
		addMenuElement(STATUS_DONE, rb.getLocalizedString(LOCALIZED_STATUS_DONE, "Done"));
		addMenuElement(STATUS_NOT_DONE, rb.getLocalizedString(LOCALIZED_STATUS_NOT_DONE, "Not Done"));
		addMenuElement(STATUS_NO_REPORT, rb.getLocalizedString(LOCALIZED_STATUS_NO_REPORT, "No Report"));
		addMenuElement(STATUS_SOME_DONE, rb.getLocalizedString(LOCALIZED_STATUS_SOME_DONE, "Some Done"));
	}


	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.Object, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		String displayName = "";
		if (value != null) {
			IWResourceBundle iwrb = getResourceBundle(iwc);
			if(STATUS_ALL.equals(value)) {
				return iwrb.getLocalizedString(LOCALIZED_STATUS_ALL, "Any status");
			} else if(STATUS_DONE.equals(value)) {
				return iwrb.getLocalizedString(LOCALIZED_STATUS_DONE, "Done");
			} else if(STATUS_NOT_DONE.equals(value)) {
				return iwrb.getLocalizedString(LOCALIZED_STATUS_NOT_DONE, "Not Done");
			} else if(STATUS_NO_REPORT.equals(value)) {
				return iwrb.getLocalizedString(LOCALIZED_STATUS_NO_REPORT, "No Report");
			} else if(STATUS_SOME_DONE.equals(value)) {
				return iwrb.getLocalizedString(LOCALIZED_STATUS_SOME_DONE, "Some Done");
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
