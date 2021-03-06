/*
 * Created on May 12, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.user.presentation;

import java.rmi.RemoteException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.idega.block.user.business.UserInfoBusiness;
import com.idega.block.user.business.UserInfoBusinessBean;
import com.idega.block.user.data.UserExtraInfo;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Displays a list of members for groups. A selection of things can be displayd for a member (phone numbers, email, title, etc.) by setting block properties.
 * The groups to display can be specified with either a request paremter or a block property.
 */
public class GroupMemberList extends Block {
	
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.user";
	public static final String PARAM_NAME_GROUP_IDS = "group_ids";
	
	private IWResourceBundle _iwrb = null;
	
	public void main(IWContext iwc) {
		this._iwrb = getResourceBundle(iwc);
		this._biz = UserInfoBusinessBean.getUserInfoBusiness(iwc);
		this._collator = Collator.getInstance(iwc.getLocale());
		
		List groups = getGroups(iwc);
		Iterator groupIter = groups.iterator();
		boolean showColumnNames = true;
		while(groupIter.hasNext()) {
			Group group = (Group) groupIter.next();
			add(getTableForGroup(iwc, group, showColumnNames));
			showColumnNames = false;
		}
	}
	
	/**
	 * Gets a Table with list of group memebers, first row in table is group name and (optionaly) column names
	 * @param group The group
	 * @param showColumnNames if true first row shows group name and column names, if false first row only shows group name
	 * @return The Table with group members
	 */
	private Table getTableForGroup(IWContext iwc, Group group, boolean showColumnNames) {
		Table table = new Table();
		if(this._showGroupName) {
			Text header = new Text(group.getName());
			header.setStyle(this._headlineStyle);
			table.add(header, 1, 1);
		}
		if(showColumnNames) {
			int column = 1;
			if(this._showTitle) {
				String titleLabel = this._iwrb.getLocalizedString("title", "Title: ");
				table.add(titleLabel, column++, 1);
			}
			if(this._showAge) {
				String ageLabel = this._iwrb.getLocalizedString("age", "Age: ");
				table.add(ageLabel, column++, 1);
			}
			if(this._showWorkPhone) {
				String workPhoneLabel = this._iwrb.getLocalizedString("workphone", "Workphone: ");
				table.add(workPhoneLabel, column++, 1);
			}
			if(this._showHomePhone) {
				String homePhoneLabel = this._iwrb.getLocalizedString("homephone", "Homephone: ");
				table.add(homePhoneLabel, column++, 1);
			}
			if(this._showMobilePhone) {
				String mobilePhoneLabel = this._iwrb.getLocalizedString("mobilePhone", "Mobilephone: ");
				table.add(mobilePhoneLabel, column++, 1);
			}
			if(this._showEmails) {
				String emailsLabel = this._iwrb.getLocalizedString("email", "Email: ");
				table.add(emailsLabel, column++, 1);
			}
			/*if(_showStatus) {
				String statusLabel = _iwrb.getLocalizedString("status", "Status: ");
				table.add(statusLabel, column++, 1);
			}*/
			if(this._showEducation) {
				String educationLabel = this._iwrb.getLocalizedString("education", "Education: ");
				table.add(educationLabel, column++, 1);
			}
			if(this._showSchool) {
				String schoolLabel = this._iwrb.getLocalizedString("school", "School: ");
				table.add(schoolLabel, column++, 1);
			}
			if(this._showArea) {
				String areaLabel = this._iwrb.getLocalizedString("area", "Area: ");
				table.add(areaLabel, column++, 1);
			}
			if(this._showBeganWork) {
				String begunWorkLabel = this._iwrb.getLocalizedString("begun_work", "Begun work: ");
				table.add(begunWorkLabel, column++, 1);
			}
		}
		
		try {
			List users = this._biz.getUsersByGroup(iwc, group, this._collator);
			Iterator userIter = users.iterator();
			int row = (this._showGroupName || showColumnNames)?2:1;
			while(userIter.hasNext()) {
				User user = (User) userIter.next();
				insertUserIntoRow(user, table, row++);
			}
		} catch(RemoteException e) {
			System.out.println("Exception getting users in group \"" + group.getName() + "\"");
			e.printStackTrace();
		}
		
		return table;
	}
	
	/**
	 * Inserts info on user into a row
	 * @param user User to insert info on
	 * @param table Table to insert user info into
	 * @param row row in table to isert user info into
	 */
	private void insertUserIntoRow(User user, Table table, int row) {
		boolean mustGetExtraUserInfo = this._showTitle || this._showEducation || this._showSchool || this._showArea || this._showBeganWork;
		UserExtraInfo userExtraInfo = null;
		if(mustGetExtraUserInfo) {
			try {
				userExtraInfo = this._biz.getInfo(user);
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(userExtraInfo == null) {
				System.out.println("No extra user info found although some info from it is supposed to be displayd, info not displayd");
			}
		}
		
		boolean showPhone = this._showWorkPhone || this._showHomePhone || this._showMobilePhone;
		String [] phones = null;
		if(showPhone) {
			try {
				phones = this._biz.getPhones(user);
			} catch(Exception e) {
				System.out.println("Exception getting phone, no phone displayed");
				e.printStackTrace();
			}
		}
		
		int column = 1;
		table.add(user.getName(), column++, row);
		if(this._showTitle) {
			if(userExtraInfo!=null) {
				String title = userExtraInfo.getTitle();
				table.add(title, column, row);
			}
			column++;
		}
		if(this._showAge) {
			try {
				String age = this._biz.getAge(user);
				table.add(age, column, row);
			} catch(Exception e) {
				System.out.println("Exception getting age, age not displayed");
				e.printStackTrace();
				this._showWorkPhone = this._showHomePhone = this._showMobilePhone = false;
			}
			column++;
		}
		if(this._showWorkPhone) {
			if(phones!=null) {
				String workPhone = phones[0];
				table.add(workPhone, column, row);
			}
			column++;
		}
		if(this._showHomePhone) {
			if(phones!=null) {
				String homePhone = phones[1];
				table.add(homePhone, column, row);
			}
			column++;
		}
		if(this._showMobilePhone) {
			if(phones!=null) {
				String mobilePhone = phones[2];
				table.add(mobilePhone, column, row);
			}
			column++;
		}
		if(this._showEmails) {
			PresentationObject emails = getEmailLinkList(user);
			table.add(emails, column++, row);
		}
		/*if(_showStatus) {
			String status = getStatus(iwc, user);
			table.add(status, column++, row);
		}*/
		if(this._showEducation) {
			if(userExtraInfo!=null) {
				String education = userExtraInfo.getEducation();
				table.add(education, column, row);
			}
			column++;
		}
		if(this._showSchool) {
			if(userExtraInfo!=null) {
				String school = userExtraInfo.getSchool();
				table.add(school, column, row);
			}
			column++;
		}
		if(this._showArea) {
			if(userExtraInfo!=null) {
				String area = userExtraInfo.getArea();
				table.add(area, column++, row);
			}
			column++;
		}
		if(this._showBeganWork) {
			String begunWork = (new IWTimestamp(userExtraInfo.getBeganWork())).getDateString("dd-MM-yyyy");
			table.add(begunWork, column++, row);
		}
	}
	
	/**
	 * Gets a comma separate list of user's emails, as links
	 * @param user The user
	 * @return The user's emails in comma separated list of links
	 */
	private PresentationObject getEmailLinkList(User user) {
		PresentationObjectContainer container = new PresentationObjectContainer();
		//int row = 1;
		try {
			// @TODO use email list from business bean
			Iterator emailIter = this._biz.getEmailList(user).iterator();
			boolean isFirst = true;
			while(emailIter.hasNext()) {
				if(isFirst) {
					isFirst = false;
				} else {
					container.add(", ");
				}
				String address = (String) emailIter.next();
				Link link = new Link(address);
				link.setURL("mailto:" + address);
				link.setSessionId(false);
				container.add(link);
			}
		} catch(Exception e) {
			System.out.println("Exception getting emails for user " + user.getName() + ", no emails shown");
		}
		return container;
	}
	
	/**
	 * Gets the groups to display, checks the context and block prop and returns the union of groups specified in both
	 * @return A List of Group instances, sorted based on current locale if <code>_sortGroups</code> is true.
	 * @param The List of Groups
	 */
	private List getGroups(IWContext iwc) {
		String[] groupIds = iwc.getParameter(PARAM_NAME_GROUP_IDS).split(",");
		Set groupIdsSet = new HashSet(); // must use a Set first to prevent duplicate values
		if(groupIds!=null && groupIds.length>0) {
			groupIdsSet.addAll(Arrays.asList(groupIds));
		}
		if(this._groupIdList!=null && this._groupIdList.size()>0) {
			groupIdsSet.addAll(this._groupIdList);
		}
		List groups = new ArrayList();
		Iterator idsIter = groupIdsSet.iterator();
		System.out.println("fetching #" + groupIdsSet.size() + " groups");
		while(idsIter.hasNext()) {
			try {
				Object objId = idsIter.next();
				String id = objId.toString().trim();
				System.out.println("fetching group with id " + id + " id class is " + objId.getClass().getName());
				Group group = this._biz.getGroup(iwc, id);
				if(group!=null) {
					groups.add(group);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(this._sortGroups) {
			Collections.sort(groups, new Comparator() {
				public int compare(Object arg0, Object arg1) {
					String name0 = ((Group) arg0).getName();
					String name1 = ((Group) arg1).getName();
					return GroupMemberList.this._collator.compare(name0, name1);
				}
			});
		}
		
		return groups;
	}
		
	// BEGIN setters/properties for block properties to select what info is shown, names of methods&properties should be self explanatory
	public void setShowGroupName(boolean value) {
		this._showGroupName = value;
	}
	
	public void setShowWorkPhone(boolean value) {
		this._showWorkPhone = value;
	}
	
	public void setShowHomePhone(boolean value) {
		this._showHomePhone = value;
	}
	
	public void setShowMobilePhone(boolean value) {
		this._showMobilePhone = value;
	}
		
	public void setShowAge(boolean value) {
		this._showAge = value;
	}
	
	/*public void setShowStatus(boolean value) {
		_showStatus = value;
	}*/
	
	public void setEmails(boolean value) {
		this._showEmails = value;
	}
	
	public void setTitle(boolean value) {
		this._showTitle = value;
	}
	
	public void setEducation(boolean value) {
		this._showEducation = value;
	}
	
	public void setSchool(boolean value) {
		this._showSchool = value;
	}
	
	public void setArea(boolean value) {
		this._showArea = value;
	}
	
	public void setBeganWork(boolean value) {
		this._showBeganWork = value;
	}
	
	public void setTextInfoStyle(String style) {
		this._textInfoStyle = style;
	}

	public void setHeadlineStyle(String style) {
		this._headlineStyle = style;
	}
	
	public void addGroupId(String id) {
		this._groupIdList.add(id);
	}
	
	private boolean _showGroupName = true;
	private boolean _showTitle = true;
	private boolean _showAge = true;
	private boolean _showWorkPhone = true;
	private boolean _showHomePhone = true;
	private boolean _showMobilePhone = true;
	private boolean _showEmails = true;
	//private boolean _showStatus = true;
	private boolean _showEducation = true;
	private boolean _showSchool = true;
	private boolean _showArea = true;
	private boolean _showBeganWork = true;
	private String _textInfoStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;";
	private String _headlineStyle = "font-family: Arial, Helvetica,sans-serif;font-weight:bold;font-size: 10pt;color: #000000;";
	private List _groupIdList = new ArrayList(); // set with block properties
	
	// END setters/properties for block properties to select what info is shown, names of methods&properties should be self explanatory
	
	private boolean _sortGroups = true; // if true, the groups to display members for are sorted alphabeticly
	
	private UserInfoBusiness _biz = null;
	private Collator _collator = null;
}
