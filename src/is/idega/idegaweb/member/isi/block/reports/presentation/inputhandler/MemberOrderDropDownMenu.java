package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.util.IWMemberConstants;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropDownMenuInputHandler;
/**
 * A presentation object for dynamic reports to genders. Both,male or female. both is default.
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class MemberOrderDropDownMenu extends DropDownMenuInputHandler  {

	private static final String NAME_ORDER = IWMemberConstants.ORDER_BY_NAME; //same as in workreportmember
	private static final String ADDRESS_ORDER = IWMemberConstants.ORDER_BY_ADDRESS; //same as in workreportmember
	private static final String POSTAL_CODE_ORDER = IWMemberConstants.ORDER_BY_POSTAL_CODE;

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	public MemberOrderDropDownMenu() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.addMenuElement(NAME_ORDER, iwrb.getLocalizedString("MemberOrderDropDownMenu.name_order", "Name"));
		this.addMenuElement(ADDRESS_ORDER, iwrb.getLocalizedString("MemberOrderDropDownMenu.address_order", "Address"));
		this.addMenuElement(POSTAL_CODE_ORDER, iwrb.getLocalizedString("MemberOrderDropDownMenu.postal_code_order", "Postal code"));
		String selectedElement = getSelectedElementValue();
		if (selectedElement == null || selectedElement.length() == 0) {
			this.setSelectedElement(POSTAL_CODE_ORDER);
		}
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
			if (NAME_ORDER.equals(value)) {
				displayName = iwrb.getLocalizedString("MemberOrderDropDownMenu.name_order", "Name");
			}
			else if (ADDRESS_ORDER.equals(value)) {
					displayName = iwrb.getLocalizedString("MemberOrderDropDownMenu.address_order", "Address");
			}
			else {
				displayName = iwrb.getLocalizedString("MemberOrderDropDownMenu.postal_code_order", "Postal code");
			}

			return displayName;
		}
		else
			return "";
	}
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}


}
