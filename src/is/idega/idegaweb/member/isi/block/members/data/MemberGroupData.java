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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class MemberGroupData {
	
	/* A list of Strings with membership info */
	private List _memberInfo = new ArrayList();
	
	public MemberGroupData(User user) {
		_user = user;
		Collection parentGroups = user.getParentGroups();
		Iterator iter = parentGroups.iterator();
		StringBuffer buf = new StringBuffer();
		while(iter.hasNext()) {
			Group group = (Group) iter.next();
			if(group==null) {
				System.out.println("a parent group was null!!");
				continue;
			}
			String memberInfo = processGroup(group);
			if(memberInfo!=null && memberInfo.length()>0) {
				_memberInfo.add(memberInfo);
			}
			buf.setLength(0);
		}
	}
	
	/**
	 * Returns a String with infor on membership based on a group. Searches all ascendant groups and includes clubs, 
	 * leagues, regional unions, unions, federations and divisions
	 * @param group an imediate parent group of the member
	 */
	private String processGroup(Group group) {
		boolean ok = processGroup(group, _buf, true);
		String result = "";
		if(ok) {
			result = _buf.toString();
			_stringByFinalGroup.put(group, result);
		}
		_buf.setLength(0);
		return result;
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
	private boolean processGroup(Group group, StringBuffer buf, boolean isFirstGroup) {
		if(group == null) return false;
		String type = group.getGroupType();
		
		System.out.println("Checking group " + group.getName() + "-" + group.getPrimaryKey());
		
		boolean isFinalGroup = IWMemberConstants.GROUP_TYPE_CLUB.equals(type) ||
		                       IWMemberConstants.GROUP_TYPE_LEAGUE.equals(type) ||
		                       IWMemberConstants.GROUP_TYPE_REGIONAL_UNION.equals(type) ||
		                       IWMemberConstants.GROUP_TYPE_UNION.equals(type) ||
		                       IWMemberConstants.GROUP_TYPE_FEDERATION.equals(type);
		boolean isDivision = IWMemberConstants.GROUP_TYPE_CLUB_DIVISION.equals(type);
		
		boolean ok = true; // assume final group will be found
		
		if(!isFinalGroup) {
			Collection groups = group.getParentGroups();
			if(groups==null || groups.size()==0) {
				// final group not found
				ok = false;
			} else {
				Iterator iter = groups.iterator();
				while(iter.hasNext()) {
					ok = processGroup((Group) iter.next(), buf, false);
				}
			}
		} else {
			String name = group.getName();
			buf.append(name);
		}
		
		if(isDivision || isFirstGroup) {
			buf.append(" - ").append(group.getName());
		}
		
		return ok;
	}
	
	/*
	 * Get a string from the list in getMemberInfo base on the group displayd in the last part of the String
	 * @param group the group at the end of the string
	 * @return the string
	 */
	public String getStringByGroup(Group group) {
		return (String) _stringByFinalGroup.get(group);
	}
	
	/**
	 * gets the user that the groups in this ISIMemberGroups instance apply to
	 * @return the user
	 */
	public User getUser() {
		return _user;
	}
	
	public List getMemberInfo() {
		return _memberInfo;
	}
	
	private Map _stringByFinalGroup = new HashMap();
	private User _user;
	
	private StringBuffer _buf = new StringBuffer();
}
