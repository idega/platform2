package is.idega.idegaweb.member.presentation;

import com.idega.event.IWLinkEvent;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.IFrame;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.GroupRelationHome;
import com.idega.user.data.UserStatus;
import com.idega.user.data.UserStatusHome;
import com.idega.user.presentation.UserGroupList;
import com.idega.user.presentation.UserTab;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class UserHistoryTab extends UserTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "usr_his_tab_name";
	private static final String DEFAULT_TAB_NAME = "History";
	
	private IFrame memberofFrame;

	public static final String PARAMETER_USER_ID = "ic_user_id";
	public static final String SESSIONADDRESS_USERGROUPS_HISTORY =
		"ic_user_ic_group_history";
	public static final String SESSIONADDRESS_USERGROUPS_STATUS =
		"ic_user_ic_group_status";

	protected Text memberof;

	public UserHistoryTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	public void initFieldContents() {
		updateFieldsDisplayStatus();
	}

	public void updateFieldsDisplayStatus() {
	}

	public void initializeFields() {
		memberofFrame = new IFrame("ic_user_history", UserHistoryList.class);
		memberofFrame.setHeight(280);
		memberofFrame.setWidth(370);
		memberofFrame.setScrolling(IFrame.SCROLLING_YES);
	}

	public void actionPerformed(IWLinkEvent e) {
		this.collect(e.getIWContext());
	}

	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		memberof = this.getTextObject();
		memberof.setText(iwrb.getLocalizedString("usr_history","History") + ":");
	}

	public boolean store(IWContext iwc) {
		return true;
	}

	public void lineUpFields() {
		this.resize(1, 2);

		this.add(memberof, 1, 1);
		this.add(memberofFrame, 1, 2);

	}

	public boolean collect(IWContext iwc) {
		return true;
	}

	public void initializeFieldNames() {
	}

	public void initializeFieldValues() {
		updateFieldsDisplayStatus();
	}

	public void dispose(IWContext iwc) {
		iwc.removeSessionAttribute(
			UserGroupList.SESSIONADDRESS_USERGROUPS_DIRECTLY_RELATED);
	}

	public void main(IWContext iwc) throws Exception {
		Object obj = ((GroupRelationHome) com.idega.data.IDOLookup.getHome(GroupRelation.class)).findAllGroupsRelationshipsByRelatedGroup(getUserId(),"GROUP_PARENT");
		Object obj2 = ((UserStatusHome) com.idega.data.IDOLookup.getHome(UserStatus.class)).findAllByUserId(getUserId());
		if (obj != null) {
			iwc.setSessionAttribute(
				UserHistoryTab.SESSIONADDRESS_USERGROUPS_HISTORY,
				obj);
		}
		else {
			iwc.removeSessionAttribute(
				UserHistoryTab.SESSIONADDRESS_USERGROUPS_HISTORY);
		}
		if (obj2 != null) {
			iwc.setSessionAttribute(
				UserHistoryTab.SESSIONADDRESS_USERGROUPS_STATUS,
				obj2);
		}
		else {
			iwc.removeSessionAttribute(
				UserHistoryTab.SESSIONADDRESS_USERGROUPS_STATUS);
		}
		
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}