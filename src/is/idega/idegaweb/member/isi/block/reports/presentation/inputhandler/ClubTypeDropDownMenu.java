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
public class ClubTypeDropDownMenu extends DropDownMenuInputHandler {

	private static final String LOCALIZED_EVERY_CLUB = "ClubTypeDropDownMenu.every_club";
	private static final String LOCALIZED_MULTI_DIVISION_CLUB = "ClubTypeDropDownMenu.multi_division_club";
	private static final String LOCALIZED_SINGLE_DIVISION_CLUB = "ClubTypeDropDownMenu.single_division_club";
	private static final String LOCALIZED_NO_MEMBERS_CLUB = "ClubTypeDropDownMenu.no_members_club";
	private static final String LOCALIZED_UMFI_CLUB = "ClubTypeDropDownMenu.umfi_club";
	private static final String LOCALIZED_INACTIVE_CLUB = "ClubTypeDropDownMenu.inactive_club";

	public static final String TYPE_MULTI_DIVISION_CLUB = IWMemberConstants.META_DATA_CLUB_STATUS_MULTI_DIVISION_CLUB;
	public static final String TYPE_SINGLE_DIVISION_CLUB = IWMemberConstants.META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB;
	public static final String TYPE_NO_MEMBERS_CLUB = IWMemberConstants.META_DATA_CLUB_STATUS_NO_MEMBERS_CLUB;
	public static final String TYPE_UMFI_CLUB = "umfi";
	public static final String TYPE_INACTIVE_CLUB = "inactive";

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";


	public ClubTypeDropDownMenu() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle rb = this.getResourceBundle(iwc);
		
		addMenuElement("", rb.getLocalizedString(LOCALIZED_EVERY_CLUB, "All Clubs"));
		addMenuElement(TYPE_MULTI_DIVISION_CLUB, rb.getLocalizedString(LOCALIZED_MULTI_DIVISION_CLUB, "Multi Division Clubs"));
		addMenuElement(TYPE_SINGLE_DIVISION_CLUB, rb.getLocalizedString(LOCALIZED_SINGLE_DIVISION_CLUB, "Single Division Clubs"));
		addMenuElement(TYPE_NO_MEMBERS_CLUB, rb.getLocalizedString(LOCALIZED_NO_MEMBERS_CLUB, "No Members Clubs"));
		addMenuElement(TYPE_UMFI_CLUB, rb.getLocalizedString(LOCALIZED_UMFI_CLUB, "UMFI Clubs"));
		addMenuElement(TYPE_INACTIVE_CLUB, rb.getLocalizedString(LOCALIZED_INACTIVE_CLUB, "Inactive Clubs"));
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.String, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		String displayName = "";
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (value != null) {
			
			if(TYPE_MULTI_DIVISION_CLUB.equals(value)) {
				return iwrb.getLocalizedString(LOCALIZED_MULTI_DIVISION_CLUB, "Multi Division Club");
			} else if(TYPE_SINGLE_DIVISION_CLUB.equals(value)) {
				return iwrb.getLocalizedString(LOCALIZED_SINGLE_DIVISION_CLUB, "Single Division Club");
			} else if(TYPE_NO_MEMBERS_CLUB.equals(value)) {
				return iwrb.getLocalizedString(LOCALIZED_NO_MEMBERS_CLUB, "No Members Club");
			} else if(TYPE_UMFI_CLUB.equals(value)) {
				return iwrb.getLocalizedString(LOCALIZED_UMFI_CLUB, "UMFI Club");
			} else if(TYPE_INACTIVE_CLUB.equals(value)) {
				return iwrb.getLocalizedString(LOCALIZED_INACTIVE_CLUB, "Inactive Club");
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
