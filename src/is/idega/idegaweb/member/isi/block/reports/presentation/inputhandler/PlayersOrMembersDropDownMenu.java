package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.util.IWMemberConstants;

import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;
/**
 * A presentation object for dynamic reports to genders. Both,male or female. both is default.
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class PlayersOrMembersDropDownMenu extends DropdownMenu implements InputHandler {

	private static final String PLAYERS = IWMemberConstants.GROUP_TYPE_CLUB_PLAYER;
	private static final String MEMBERS = IWMemberConstants.GROUP_TYPE_CLUB_MEMBER;

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private WorkReportBusiness workBiz = null;

	public PlayersOrMembersDropDownMenu() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.addMenuElement(MEMBERS, iwrb.getLocalizedString("PlayersOrMembersDropDownMenu.members", "Members"));
		this.addMenuElement(PLAYERS, iwrb.getLocalizedString("PlayersOrMembersDropDownMenu.players", "Players"));
		this.setSelectedElement(MEMBERS);
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
			return values[0];
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
			String displayName = "";
			if (PLAYERS.equals(value)) {
				displayName = iwrb.getLocalizedString("PlayersOrMembersDropDownMenu.players", "Players");
			}
			else if (MEMBERS.equals(value)) {
					displayName = iwrb.getLocalizedString("PlayersOrMembersDropDownMenu.members", "Members");
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
