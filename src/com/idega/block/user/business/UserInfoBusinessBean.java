/*
 * Created on May 5, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.user.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.user.data.UserExtraInfo;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneType;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserStatusBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.Status;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class UserInfoBusinessBean extends IBOServiceBean implements UserInfoBusiness {
	
	public static UserInfoBusiness getUserInfoBusiness(IWContext iwc) {
		try {
			return (UserInfoBusiness) IBOLookup.getServiceInstance(iwc, UserInfoBusiness.class);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * gets user from id
	 * @param iwc IWContext
	 * @param userId user id as a String
	 * @return The user with the given id, or null if not found or an error occured
	 */
	public User getUser(IWContext iwc, String userId) {
		User user = null;
		try {
			user = getUserBusiness(iwc).getUser(Integer.parseInt(userId));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	/**
	 * Gets the phones for a user
	 * @param user The user
	 * @return an array of length three with workphone, homephone and mobilephone in that order
	 */
	public String[] getPhones(User user) {
		String[] phones = {"", "", ""};
		Collection phoneCol = user.getPhones();
		if(phoneCol!=null) {
			Iterator phoneIter = phoneCol.iterator();
			while(phoneIter.hasNext()) {
				Phone phone = (Phone) phoneIter.next();
				String p = phone.getNumber();
				if(phone.getPhoneTypeId()==PhoneType.WORK_PHONE_ID) {
					phones[0] = p;
				} else if(phone.getPhoneTypeId()==PhoneType.HOME_PHONE_ID) {
					phones[1] = p;
				} else if(phone.getPhoneTypeId()==PhoneType.MOBILE_PHONE_ID) {
					phones[2] = p;
				}
			}
		}
		return phones;
	}
	
	/**
	 * Gets a List of user's email addresses (instances of String)
	 * @param user The user
	 * @return The user's emails in a List of Strings
	 */
	public List getEmailList(User user) {
		List emailList = new ArrayList();
		//int row = 1;
		try {
			Iterator emailIter = user.getEmails().iterator();
			while(emailIter.hasNext()) {
				Email email = (Email) emailIter.next();
				String address = email.getEmailAddress();
				emailList.add(address);
			}
		} catch(Exception e) {
			// don't give a pair of donkeys kiddneys, most likely means there are no emails for user
		}
		return emailList;
	}
	
	/**
	 * Gets the users in a group, sorted with a given comparator
	 * @param group Group to get member of
	 * @param comparator Comparator to use to compare user names, if null then list is not sorted.
	 * @return List of users in group.
	 */
	public List getUsersByGroup(IWContext iwc, Group group, final Comparator comparator) {
		List users = new ArrayList();
		try {
			users.add(getGroupBusiness(iwc).getUsers(group));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(comparator!=null) {
			Collections.sort(users, new Comparator() {
				public int compare(Object arg0, Object arg1) {
					String name0 = ((User) arg0).getName();
					String name1 = ((User) arg1).getName();
					return comparator.compare(name0, name1);
				}
			});
		}
		
		return users;
	}
	
	/**
	 * Gets a list of groups, sorted using a given comparator. Takes two arrays of group ids, the returning list contains groups from both arrays with
	 * no duplicate values
	 * @param iwc The IWContext
	 * @param ids1 first array og group ids
	 * @param ids2 second array og group ids
	 * @param comparator The Comparator to use to compare group names, if null then list is not sorted.
	 * @return
	 */
	public List getGroups(IWContext iwc, String[] ids1, String[] ids2, final Comparator comparator) {
		Set groupIdsSet = new HashSet(); // must use a Set first to prevent duplicate values
		if(ids1!=null && ids1.length>0) {
			groupIdsSet.add(Arrays.asList(ids1));
		}
		if(ids2!=null && ids2.length>0) {
			groupIdsSet.add(Arrays.asList(ids2));
		}
		List groupIdsList = new ArrayList(groupIdsSet);
		List groups = new ArrayList();
		Iterator idsIter = groupIdsList.iterator();
		while(idsIter.hasNext()) {
			String id = (String) idsIter.next();
			Group group = getGroup(iwc, id);
			if(group!=null) {
				groups.add(group);
			}
		}
		
		if(comparator!=null) {
			Collections.sort(groups, new Comparator() {
				public int compare(Object arg0, Object arg1) {
					String name0 = ((Group) arg0).getName();
					String name1 = ((Group) arg1).getName();
					return comparator.compare(name0, name1);
				}
			});
		}
		
		return groups;
	}
	
	/**
	 * Gets user's age
	 * @param user The user
	 * @return The user's age
	 */
	public String getAge(User user) {
		String age = null;
		if (user.getDateOfBirth() != null) {
			IWTimestamp iwtDateOfBirth = new IWTimestamp(user.getDateOfBirth());
			IWTimestamp iwtDateToday = new IWTimestamp();
			age = Integer.toString((new IWTimestamp().getDaysBetween(iwtDateOfBirth, iwtDateToday)) / 365);
		}
		return age;
	}
	
	/**
	 * Gets user status key for given group
	 * @param iwc context
	 * @param user The user
	 * @return The user's status key for given group
	 */
	public String getStatusKey(IWContext iwc, User user, Group group) {
		String statusKey = null;
		try {
			int group_id = Integer.parseInt(group.getPrimaryKey().toString());
			int user_id = Integer.parseInt(user.getPrimaryKey().toString());
			int status_id = getUserStatusBusiness(iwc).getUserGroupStatus(user_id,group_id);
			if(status_id != -1) {
				Status st = (Status) IDOLookup.findByPrimaryKey(Status.class, status_id);
				statusKey = st.getStatusKey();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return statusKey;
	}
	
	/**
	 * Gets UserExtraInfo for a user
	 * @param user The user to get UserExtraInfo for
	 * @return UserExtraInfo for given user
	 */
	public UserExtraInfo getInfo(User user) {
		try {
			return (UserExtraInfo) com.idega.data.IDOLookup.findByPrimaryKey(UserExtraInfo.class, Integer.parseInt(user.getPrimaryKey().toString()));
		} catch(FinderException e) {
			// dont have to handle this, just return null
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets a Group by id
	 * @param iwc IWContext
	 * @param groupId the Group's id
	 * @return The Group
	 */
	public Group getGroup(IWContext iwc, String groupId) {
		Group group = null;
		try {
			group = getGroupBusiness(iwc).getGroupByGroupID(Integer.parseInt(groupId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return group;
	}
	
	/**
	 * Gets a groups address
	 * @param iwc IWContext
	 * @param group The group to get address for
	 * @return The groups address
	 */
	public Address getGroupAddress(IWContext iwc, Group group) {
		Address address = null;
		try {
			address = getGroupBusiness(iwc).getGroupMainAddress(group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return address;
	}
	
	private UserBusiness getUserBusiness(IWApplicationContext iwc){
		if(_userBiz == null) {
			try{
				_userBiz = (UserBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,UserBusiness.class);
			}
			catch(java.rmi.RemoteException rme){
				throw new RuntimeException(rme.getMessage());
			}
		}
		return _userBiz;
	}
	
	private UserStatusBusiness getUserStatusBusiness(IWApplicationContext iwc){
		if(_userStatBiz == null) {
			try{
				_userStatBiz = (UserStatusBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,UserStatusBusiness.class);
			}
			catch(java.rmi.RemoteException rme){
				throw new RuntimeException(rme.getMessage());
			}
		}
		return _userStatBiz;
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
	
	private UserBusiness _userBiz = null;
	private UserStatusBusiness _userStatBiz = null;
	private GroupBusiness _groupBiz = null;

}
