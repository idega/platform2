package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.util.IWMemberConstants;

import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropDownMenuInputHandler;

/**
 * @author Sigtryggur
 */
public class OrderByGroupPersonDateDropDownMenu extends DropDownMenuInputHandler {

	private static final String NAME_ORDER = IWMemberConstants.ORDER_BY_NAME; //same as in workreportmember
	private static final String GROUP_NAME_ORDER = IWMemberConstants.ORDER_BY_GROUP_NAME; //same as in workreportmember
	private static final String ENTRY_DATE_ORDER = IWMemberConstants.ORDER_BY_ENTRY_DATE;

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	public OrderByGroupPersonDateDropDownMenu() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.addMenuElement(NAME_ORDER, iwrb.getLocalizedString("OrderByGroupPersonDateDropDownMenu.name_order", "Name"));
		this.addMenuElement(GROUP_NAME_ORDER, iwrb.getLocalizedString("OrderByGroupPersonDateDropDownMenu.group_name_order", "Group name"));
		this.addMenuElement(ENTRY_DATE_ORDER, iwrb.getLocalizedString("OrderByGroupPersonDateDropDownMenu.entry_date_order", "Entry date"));
		String selectedElement = getSelectedElementValue();
		if (selectedElement == null || selectedElement.length() == 0) {
			this.setSelectedElement(NAME_ORDER);
		}
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