package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.util.IWMemberConstants;

import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropDownMenuInputHandler;
/**
 * A presentation object for dynamic reports to genders. Both,male or female. both is default.
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class PlayersOrMembersDropDownMenu extends DropDownMenuInputHandler  {

	private static final String PLAYERS = IWMemberConstants.GROUP_TYPE_CLUB_PLAYER;
	private static final String MEMBERS = IWMemberConstants.GROUP_TYPE_CLUB_MEMBER;

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	public PlayersOrMembersDropDownMenu() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.addMenuElement(MEMBERS, iwrb.getLocalizedString("PlayersOrMembersDropDownMenu.members", "Members"));
		this.addMenuElement(PLAYERS, iwrb.getLocalizedString("PlayersOrMembersDropDownMenu.players", "Players"));
		String selectedElement = getSelectedElementValue();
		if (selectedElement == null || selectedElement.length() == 0) {
			this.setSelectedElement(MEMBERS);
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
