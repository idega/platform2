/*
 * Created on Feb 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.members.data;

import is.idega.idegaweb.member.util.IWMemberConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.GroupRelationHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class MemberGroupData {
	
	public static final String LOCALIZE_KEY_PREFIX_GROUP_CATEGORY = "group_category_";
	
	public MemberGroupData(User user, IWResourceBundle iwrb) {
		_user = user;
		Collection history = null;
		try {
			history = (Collection) ((GroupRelationHome) com.idega.data.IDOLookup.getHome(GroupRelation.class)).findAllGroupsRelationshipsByRelatedGroup(user.getID(),"GROUP_PARENT");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Collection parentGroups = user.getParentGroups();
		//Iterator iter = parentGroups.iterator();
		Iterator iter = history.iterator();
		StringBuffer buf = new StringBuffer();
		while(iter.hasNext()) {
			GroupRelation groupRel = (GroupRelation) iter.next();
			/*Group group = (Group) iter.next();
			if(group==null) {
				System.out.println("a parent group was null!!");
				continue;
			}
			processGroup(group);*/
			processGroupRelation(groupRel, iwrb);
		}
	}
	
	/**
	 * Returns a String with info on membership based on a group. Searches all ascendant groups and includes clubs, 
	 * leagues, regional unions, unions, federations and divisions
	 * @param group an imediate parent group of the member
	 */
	private void processGroupRelation(GroupRelation groupRel, IWResourceBundle iwrb) {
		Group group = groupRel.getGroup();
		String groupTypeName = iwrb.getLocalizedString(LOCALIZE_KEY_PREFIX_GROUP_CATEGORY + group.getGroupType(), "Unknown");
		if(groupTypeName.equals("Unknown")) {
			groupTypeName="";
			System.out.println("Name for group type not defined, skipping group (key=\"" + LOCALIZE_KEY_PREFIX_GROUP_CATEGORY + group.getGroupType() + "\")");
			return;
		}
		_buf.setLength(0);
		processGroup(group, _buf, true);
		if(_buf.length()>0) {
			String[] result = new String[4];
			result[0] = _buf.toString();
			result[1] = groupTypeName;
			result[2] = (new IWTimestamp(groupRel.getInitiationDate())).getDateString("dd.MM.yyyy");
			if(groupRel.getTerminationDate()!=null) {
				result[3] = (new IWTimestamp(groupRel.getTerminationDate())).getDateString("dd.MM.yyyy");
			} else {
				result[3] = null;
			}
			_groupInfoList.add(result);
		}
	}
	
	/**
	 * Returns a String with infor on membership based on a group. Searches all ascendant groups and includes clubs, 
	 * leagues, regional unions, unions, federations and divisions
	 * @param group the group to categorize
	 * @param buf a StringBuffer to collect the member group info into
	 * @param isFirstGroup if the group is an imediate parent of the member
	 * @returns true if a club, league, regional union, union or federation was found among groups ancestors (including
	 * group itself)
	 */
	private void processGroup(Group group, StringBuffer buf, boolean isFirstGroup) {
		
		String type = group.getGroupType();
		String name = group.getName();
		
		boolean isFlock = IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(type);
		boolean isClub = IWMemberConstants.GROUP_TYPE_CLUB.equals(type);
		if(isClub && !_clubList.contains(group)) {
			_clubList.add(group);
		}
		boolean isFinalGroup = IWMemberConstants.GROUP_TYPE_CLUB.equals(type) ||
		                       IWMemberConstants.GROUP_TYPE_LEAGUE.equals(type) ||
		                       IWMemberConstants.GROUP_TYPE_REGIONAL_UNION.equals(type) ||
		                       IWMemberConstants.GROUP_TYPE_UNION.equals(type) ||
		                       IWMemberConstants.GROUP_TYPE_FEDERATION.equals(type);
		boolean isDivision = IWMemberConstants.GROUP_TYPE_CLUB_DIVISION.equals(type);
		
		boolean ok = true; // assume final group will be found
		
		if(isFirstGroup) {
			buf.append(name);
		} else if(isDivision) {
			buf.append(" - ").append(name);
		}
		
		if(!isFinalGroup) {
			Collection groups = group.getParentGroups();
			if(groups!=null && groups.size()>0) {
				Iterator iter = groups.iterator();
				while(iter.hasNext()) {
					processGroup((Group) iter.next(), buf, false);
				}
			}
		} else {
			buf.append(" - ").append(name);
		}
	}
	
	/**
	 * gets the user that the groups in this ISIMemberGroups instance apply to
	 * @return the user
	 */
	public User getUser() {
		return _user;
	}
	
	/**
	 * A list of String arrays. Each array contains info on a membership, the Strings in the array are as following
	 * <ul>
	 *   <li>[0]: The name of the group and it's relevant ancestors</li>
	 *   <li>[1]: The name of the group type, empty if not known</li>
	 *   <li>[2]: The time the user was registered in the group</li>
	 *   <li>[3]: The time the user war unregistered from the group, null if user is still in the group</li>
	 * </ul>
	 * @return list of membeship info entries
	 */
	public List getGroupInfoList() {
		return _groupInfoList;
	}
	
	/**
	 * Gets a list of the clubs (Group instances)
	 * @return list of the clubs the user is inb
	 */
	public List getClubList() {
		return _clubList;
	}
	
	private List _groupInfoList = new ArrayList();
	private List _clubList = new ArrayList();
	private User _user;
	private StringBuffer _buf = new StringBuffer();
}
