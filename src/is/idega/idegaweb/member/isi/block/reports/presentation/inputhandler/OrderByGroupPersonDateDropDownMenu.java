package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.util.IWMemberConstants;

import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author Sigtryggur
 */
public class OrderByGroupPersonDateDropDownMenu extends DropdownMenu implements InputHandler {

	private static final String NAME_ORDER = IWMemberConstants.ORDER_BY_NAME; //same as in workreportmember
	private static final String GROUP_NAME_ORDER = IWMemberConstants.ORDER_BY_GROUP_NAME; //same as in workreportmember
	private static final String ENTRY_DATE_ORDER = IWMemberConstants.ORDER_BY_ENTRY_DATE;

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private WorkReportBusiness workBiz = null;

	public OrderByGroupPersonDateDropDownMenu() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.addMenuElement(NAME_ORDER, iwrb.getLocalizedString("OrderByGroupPersonDateDropDownMenu.name_order", "Name"));
		this.addMenuElement(GROUP_NAME_ORDER, iwrb.getLocalizedString("OrderByGroupPersonDateDropDownMenu.group_name_order", "Group name"));
		this.addMenuElement(ENTRY_DATE_ORDER, iwrb.getLocalizedString("OrderByGroupPersonDateDropDownMenu.entry_date_order", "Entry date"));
		this.setSelectedElement(NAME_ORDER);
	}

	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc) {
		this.setName(name);
		if (stringValue != null) {
			this.setSelectedElement(stringValue);
		}
		return this;
	}

	/**
	 * @return the orderByString, String
	 *  
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		if (values != null && values.length > 0) {
			return values[0];
		}
		else
			return null;
	}

	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (value != null) {
			
			String displayName = "";
			if (NAME_ORDER.equals(value)) {
				displayName = iwrb.getLocalizedString("OrderByGroupPersonDateDropDownMenu.name_order", "Name");
			}
			else if (GROUP_NAME_ORDER.equals(value)) {
				displayName = iwrb.getLocalizedString("OrderByGroupPersonDateDropDownMenu.group_name_order", "Group name");
			}
			else {
				displayName = iwrb.getLocalizedString("OrderByGroupPersonDateDropDownMenu.entry_date_order", "Entry date");
			}
			return displayName;
		}
		else
			return "";
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}