/*
 * Created on Mar 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.members.presentation;

import is.idega.idegaweb.member.isi.block.clubs.presentation.ClubInfoBar;
import is.idega.idegaweb.member.isi.block.clubs.presentation.ClubPageIncluder;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserStatusBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.Status;
import com.idega.user.data.User;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GroupMemberList extends Block {
	
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	public static final String PARAM_NAME_GROUP_ID = "group_id";
	public static final String PARAM_NAME_SHOW_CLUB_COMMITEE_MAIN = "show_main_commitee";
	
	private IWResourceBundle _iwrb = null;
	
	public void main(IWContext iwc) {
		_iwrb = getResourceBundle(iwc);
		Group group = getGroupToShowMembersFor(iwc);
		Group division = getDivision(iwc);
		boolean showClubMainCommitee = "true".equals(iwc.getParameter(PARAM_NAME_SHOW_CLUB_COMMITEE_MAIN));
		if(!showClubMainCommitee) {
			String name = group==null?"":group.getName();
			Text title = new Text(name + ": ");
			title.setBold();
			add(title);
			addBreak();
		}
		if(group!=null) {
			add(getPlayerList(iwc, group, division));
		}
	}

	private PresentationObject getPlayerList(IWContext iwc, Group group, Group division) {
		boolean showStatus = false;//"true".equals(iwc.getParameter(PARAM_NAME_SHOW_STATUS));
		boolean showGroup = false;//"true".equals(iwc.getParameter(PARAM_NAME_SHOW_GROUP));
		
		String type = group.getGroupType();
		if(IWMemberConstants.GROUP_TYPE_CLUB_COMMITTEE_MAIN.equals(type)) {
			showStatus = true;
		} 
		if(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_TRAINER.equals(type)) {
			showGroup = true;
		}
		
		Table table = new Table();
		table.setCellspacing(0);
		Iterator userIter; //group.getChildren();
		try {
			userIter = getGroupBusiness(iwc).getUsers(group).iterator();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		int row = 1;
		int group_id = Integer.parseInt(group.getPrimaryKey().toString());
		while(userIter.hasNext()) {
			User user = (User) userIter.next();
			boolean nameAdded = false;
			try {
				int user_id = Integer.parseInt(user.getPrimaryKey().toString());
				int column = 1;
				
				String name = user.getName();
				table.add(name, column++, row);
				nameAdded = true;
				if(showStatus) {
					String statusKey = null;
					try {
						int status_id = getUserStatusBusiness(iwc).getUserGroupStatus(user_id,group_id);
						if(status_id != -1) {
							Status st = (Status) IDOLookup.findByPrimaryKey(Status.class, status_id);
							statusKey = st.getStatusKey();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(statusKey!=null) {
						String key = "usr_stat_" + statusKey;
						String status = _iwrb.getLocalizedString(key, statusKey);
						table.setCellpaddingLeft(column, row, 3);
						table.add(status, column++, row);
					}
				}
				if(showGroup) {
					String groupNames = getGroupNamesForTrainer(user, division);
					table.setCellpaddingLeft(column, row, 3);
					table.add(groupNames, column++, row);
				}
			} catch(Exception e) {
				System.out.println("Exception lising user " + user.getName());
				e.printStackTrace(); 
			}
			if(nameAdded) {
				row++;
			}
		}
		table.setHorizontalZebraColored(_color1, _color2);
		
		return table;
	}
	
	private String getGroupNamesForTrainer(User trainer, Group division) {
		Iterator parentIter = trainer.getParentGroups().iterator();
		Collection divisionChildren = division.getChildGroups();
		StringBuffer buf = new StringBuffer();
		while(parentIter.hasNext()) {
			Group group = (Group) parentIter.next();
			boolean isFlock = IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(group.getGroupType());
			boolean isInDivision = divisionChildren.contains(group);
			if(isFlock && isInDivision) {
				if(buf.length()>0) {
					buf.append(", ");
				}
				buf.append(group.getName());
			}
		}
		return buf.toString();
	}
	
	private Group getGroupToShowMembersFor(IWContext iwc) {
		boolean showClubMainCommitee = "true".equals(iwc.getParameter(PARAM_NAME_SHOW_CLUB_COMMITEE_MAIN));
		
		if(showClubMainCommitee) {
			System.out.println("showing members of club's main commitee");
			String clubId = iwc.getParameter(ClubPageIncluder.PARAM_ROOT_CLUB_ID);
			Group group = null;
			try {
				Group club = getGroup(iwc, Integer.parseInt(clubId));
				Group commiteeGroup = ((Group) club.getChildGroups(new String[] {IWMemberConstants.GROUP_TYPE_CLUB_COMMITTEE}, true).iterator().next());
				group = ((Group) commiteeGroup.getChildGroups(new String[] {IWMemberConstants.GROUP_TYPE_CLUB_COMMITTEE_MAIN}, true).iterator().next());
				System.out.println("Got group to show members for; " + group.getName());
			} catch(Exception e) {
				System.out.println("Exception getting club's main commitee");
				e.printStackTrace();
			}
			return group;
		} else {
			String groupId = iwc.getParameter(PARAM_NAME_GROUP_ID);
			if(groupId == null || groupId.length()==0) {
				System.out.println("no group to display players for");
			}
			Group group = null;
			try {
				group = getGroup(iwc, Integer.parseInt(groupId));
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			return group;
		}
	}
	
	private Group getDivision(IWContext iwc) {
		String groupId = iwc.getParameter(ClubInfoBar.PARAM_NAME_DIVISION_ID);
		if(groupId == null || groupId.length()==0) {
			System.out.println("no division found from request");
			return null;
		}
		Group group = null;
		try {
			group = getGroup(iwc, Integer.parseInt(groupId));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return group;
	}

	private Group getGroup(IWContext iwc, int groupId) {
		Group group = null;
		try {
			group = getGroupBusiness(iwc).getGroupByGroupID(groupId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return group;
	}
	
	private GroupBusiness getGroupBusiness(IWContext iwc) {
		if(_groupBiz == null) {
			try {
				_groupBiz = (GroupBusiness) IBOLookup.getServiceInstance(iwc.getApplicationContext(), GroupBusiness.class);
			} catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		
		return _groupBiz;
	}
	
	private UserStatusBusiness getUserStatusBusiness(IWApplicationContext iwc){
		UserStatusBusiness business = null;
		if(business == null){
			try{
				business = (UserStatusBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,UserStatusBusiness.class);
			}
			catch(java.rmi.RemoteException rme){
				throw new RuntimeException(rme.getMessage());
			}
		}
		return business;
	}
		
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	private String _currentColor = null;
	private String _color1 = "lightgray";
	private String _color2 = "white";
	
	private GroupBusiness _groupBiz = null;
}
