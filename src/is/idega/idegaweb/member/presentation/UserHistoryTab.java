package is.idega.idegaweb.member.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.idega.event.IWLinkEvent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.IFrame;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.GroupRelationHome;
import com.idega.user.data.User;
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
	
	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String HELP_TEXT_KEY = "user_history_tab";

	
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
		memberofFrame.setWidth("100%");
		memberofFrame.setStyleAttribute("border", "1px #bbbbbb solid;");
		memberofFrame.setScrolling(IFrame.SCROLLING_YES);
	}

	public void actionPerformed(IWLinkEvent e) {
		this.collect(e.getIWContext());
	}

	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

//		memberof = this.getTextObject();
		memberof = new Text(iwrb.getLocalizedString("usr_history","History"));
		memberof.setBold();
	}

	public boolean store(IWContext iwc) {
		return true;
	}

	public void lineUpFields() {
		this.resize(1, 1);
		setCellpadding(5);
		setCellspacing(0);

		this.add(memberof, 1, 1);
		add(Text.getBreak(), 1, 1);
		this.add(memberofFrame, 1, 1);
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
		if (getPanel() != null) {
			getPanel().addHelpButton(getHelpButton());		
		}
		User viewedUser = getUser();
		User viewingUser = iwc.getCurrentUser();
		boolean isAdmin = iwc.isSuperAdmin();
		boolean isSameUser = viewedUser.getPrimaryKey().equals(viewingUser.getPrimaryKey());
		boolean checkNeeded = !(isAdmin || isSameUser);
		System.out.println("User " + viewingUser.getName() + " is viewing user " + viewedUser.getName() + ", checkNeede=" + checkNeeded);
		
		Collection groupRelations = ((GroupRelationHome) com.idega.data.IDOLookup.getHome(GroupRelation.class)).findAllGroupsRelationshipsByRelatedGroupOrderedByInitiationDate(getUserId(),"GROUP_PARENT");
		if(checkNeeded) {
			groupRelations = getFilteredGroupRelations(iwc, Collections.unmodifiableCollection(groupRelations), viewingUser);
		}
		if (groupRelations != null) {
			iwc.setSessionAttribute(
				UserHistoryTab.SESSIONADDRESS_USERGROUPS_HISTORY,
				groupRelations);
		}
		else {
			iwc.removeSessionAttribute(
				UserHistoryTab.SESSIONADDRESS_USERGROUPS_HISTORY);
		}
		
		Collection statuses = ((UserStatusHome) com.idega.data.IDOLookup.getHome(UserStatus.class)).findAllByUserId(getUserId());
		if(checkNeeded) {
			statuses = getFilteredStatuses(iwc, Collections.unmodifiableCollection(statuses), viewingUser);
		}
		if (statuses != null) {
			iwc.setSessionAttribute(
				UserHistoryTab.SESSIONADDRESS_USERGROUPS_STATUS,
				statuses);
		}
		else {
			iwc.removeSessionAttribute(
				UserHistoryTab.SESSIONADDRESS_USERGROUPS_STATUS);
		}
		
	}
	
	/**
	 * Filters Statuses by users persmission to see groups. Only statuses pertaining to a groups that are descendants of one of users top group
	 * nodes are returned
	 * @param iwc IWContext
	 * @param statuses The Statuses to filter
	 * @param user The user whos top groups nodes are used for the filtering
	 * @return All the Statuses in <code>statuses</code> that are for a group that is a descendant of one of <code>user</code> top groups
	 */
	private Collection getFilteredStatuses(IWContext iwc, Collection statuses, User user) {
		Collection result = new ArrayList();
		UserBusiness userBusiness = this.getUserBusiness(iwc);
		Iterator statusIter = statuses.iterator();
		while(statusIter.hasNext()) {
			UserStatus status = (UserStatus) statusIter.next();
			boolean ok = false;
			try {
				ok = userBusiness.isGroupUnderUsersTopGroupNode(iwc, status.getGroup(), user);
			} catch (RemoteException e) {
				System.out.println("Could not check if group in user status is a descendant of a users top group, status for group not shown");
				e.printStackTrace();
			}
			if(ok) {
				result.add(status);
			} else {
				System.out.println("User status in group " + status.getGroup().getName() + " not shown");
			}
		}
		
		return result;
	}
	
	/**
	 * Filters GroupRelations by users persmission to see groups. Only GroupRelations pertaining to groups that are descendants of
	 * one of users top group nodes are returned
	 * @param iwc IWContext
	 * @param groupRelations The GroupRelations to filter
	 * @param user The user whos top groups nodes are used for the filtering
	 * @return All the GroupRelations in <code>groupRelations</code> that are for groups that are both descendants of one of <code>user</code> top groups
	 */
	private Collection getFilteredGroupRelations(IWContext iwc, Collection groupRelations, User user) {
		Collection result = new ArrayList();
		UserBusiness userBusiness = this.getUserBusiness(iwc);
		Iterator groupRelationIter = groupRelations.iterator();
		while(groupRelationIter.hasNext()) {
			GroupRelation rel = (GroupRelation) groupRelationIter.next();
			boolean ok = false;
			try {
				ok = userBusiness.isGroupUnderUsersTopGroupNode(iwc, rel.getGroup(), user) &&
				     userBusiness.isGroupUnderUsersTopGroupNode(iwc, rel.getRelatedGroup(), user);
			} catch (RemoteException e) {
				System.out.println("Could not check if groups in relation were descendants of a users top group, group relation not shown");
				e.printStackTrace();
			}
			if(ok) {
				result.add(rel);
			} else {
				System.out.println("Group relation between " + rel.getGroup().getName() + " and " + rel.getRelatedGroup().getName() + " not shown");
			}
		}
		
		return result;
	}
	
	public Help getHelpButton() {
		IWContext iwc = IWContext.getInstance();
		IWBundle iwb = getBundle(iwc);
		Help help = new Help();
		Image helpImage = iwb.getImage("help.gif");
		help.setHelpTextBundle( MEMBER_HELP_BUNDLE_IDENTIFIER);
		help.setHelpTextKey(HELP_TEXT_KEY);
		help.setImage(helpImage);
		return help;
		
	}


	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}