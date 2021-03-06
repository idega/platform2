package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.util.IWMemberConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropDownMenuInputHandler;

/**
 * <p>
 * TODO sigtryggur Describe Type OrderByUserDetailsDropDownMenu
 * </p>
 *  Last modified: $Date: 2007/01/26 05:47:17 $ by $Author: idegaweb $
 * 
 * @author <a href="mailto:sigtryggur@idega.com">sigtryggur</a>
 * @version $Revision: 1.1.2.4 $
 * @deprecated Replaced with com.idega.presentation.ui.OrderByUserDetailsDropDownMenu
 */
public class OrderByUserDetailsDropDownMenu extends DropDownMenuInputHandler {

	private static final String ORDER_BY_GROUP_PATH = IWMemberConstants.ORDER_BY_GROUP_PATH;
	private static final String ORDER_BY_NAME = IWMemberConstants.ORDER_BY_NAME;
	private static final String ORDER_BY_USER_STATUS = IWMemberConstants.ORDER_BY_USER_STATUS;
	private static final String ORDER_BY_ADDRESS = IWMemberConstants.ORDER_BY_ADDRESS;
	private static final String ORDER_BY_POSTAL_ADDRESS = IWMemberConstants.ORDER_BY_POSTAL_ADDRESS;
	
	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String CLASS_NAME_PREFIX = "OrderByUserDetailsDropDownMenu.";

	public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.setName(name);
		this.addMenuElement(ORDER_BY_GROUP_PATH, iwrb.getLocalizedString(CLASS_NAME_PREFIX + ORDER_BY_GROUP_PATH, "Group path"));
		this.addMenuElement(ORDER_BY_NAME, iwrb.getLocalizedString(CLASS_NAME_PREFIX + ORDER_BY_NAME, "Name"));
		this.addMenuElement(ORDER_BY_USER_STATUS, iwrb.getLocalizedString(CLASS_NAME_PREFIX + ORDER_BY_USER_STATUS, "User status"));
		this.addMenuElement(ORDER_BY_ADDRESS, iwrb.getLocalizedString(CLASS_NAME_PREFIX + ORDER_BY_ADDRESS, "Address"));
		this.addMenuElement(ORDER_BY_POSTAL_ADDRESS, iwrb.getLocalizedString(CLASS_NAME_PREFIX + ORDER_BY_POSTAL_ADDRESS, "Postal address"));
		return this;
	}

	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		if (value != null) {
			IWResourceBundle iwrb = getResourceBundle(iwc);
			return iwrb.getLocalizedString(CLASS_NAME_PREFIX + value);
		}
		else {
			return "";
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}