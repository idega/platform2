/*
 * Created on May 4, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.user.presentation;

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
public class GroupInfo extends Block {
	
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.user";
	public static final String PARAM_NAME_GROUP_IDS = "group_ids";
	
	private IWResourceBundle _iwrb = null;
	
	public void main(IWContext iwc) {
		_iwrb = getResourceBundle(iwc);
		_biz = UserInfoBusinessBean.getUserInfoBusiness(iwc);
		_collator = Collator.getInstance(iwc.getLocale());
		
		getGroups(iwc);
	}
	
	/**
	 * Inserts info on user into a row
	 * @param user User to insert info on
	 * @param table Table to insert user info into
	 * @param row row in table to isert user info into
	 */
	private void insertUserIntoRow(User user, Table table, int row) {
		boolean mustGetExtraUserInfo = _showTitle || _showEducation || _showSchool || _showArea || _showBeganWork;
		UserExtraInfo userExtraInfo = null;
		if(mustGetExtraUserInfo) {
			try {
				userExtraInfo = _biz.getInfo(user);
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(userExtraInfo == null) {
				System.out.println("No extra user info found although some info from it is supposed to be displayd, info not displayd");
				_showTitle = _showEducation = _showSchool = _showArea = _showBeganWork = false;
			}
		}
		
		boolean showPhone = _showWorkPhone || _showHomePhone || _showMobilePhone;
		String [] phones = null;
		if(showPhone) {
			try {
				phones = _biz.getPhones(user);
			} catch(Exception e) {
				System.out.println("Exception getting phone, no phone displayed");
				e.printStackTrace();
				_showWorkPhone = _showHomePhone = _showMobilePhone = false;
			}
		}
		
		//int column = 1;
		if(_showTitle) {
			String title = userExtraInfo.getTitle();
			String titleLabel = _iwrb.getLocalizedString("title", "Title: ");
			addTextToTable(table, row++, titleLabel, title);
		}
		if(_showAge) {
			try {
				String age = _biz.getAge(user);
				String ageLabel = _iwrb.getLocalizedString("age", "Age: ");
				addTextToTable(table, row++, ageLabel, age);
			} catch(Exception e) {
				System.out.println("Exception getting age, age not displayed");
				e.printStackTrace();
				_showWorkPhone = _showHomePhone = _showMobilePhone = false;
			}
		}
		if(_showWorkPhone) {
			String workPhone = phones[0];
			String workPhoneLabel = _iwrb.getLocalizedString("workphone", "Workphone: ");
			addTextToTable(table, row++, workPhoneLabel, workPhone);
		}
		if(_showHomePhone) {
			String homePhone = phones[1];
			String homePhoneLabel = _iwrb.getLocalizedString("homephone", "Homephone: ");
			addTextToTable(table, row++, homePhoneLabel, homePhone);
		}
		if(_showMobilePhone) {
			String mobilePhone = phones[2];
			String mobilePhoneLabel = _iwrb.getLocalizedString("mobilePhone", "Mobilephone: ");
			addTextToTable(table, row++, mobilePhoneLabel, mobilePhone);
		}
		if(_showEmails) {
			PresentationObject emails = getEmailLinkList(user);
			String emailsLabel = _iwrb.getLocalizedString("email", "Email: ");
			table.add(emailsLabel, 2, row);
			table.add(emails, 3, row++);
		}
		/*if(_showStatus) {
			String status = getStatus(iwc, user);
			String statusLabel = _iwrb.getLocalizedString("status", "Status: ");
			addTextToTable(table, row++, statusLabel, status);
		}*/
		if(_showEducation) {
			String education = userExtraInfo.getEducation();
			String educationLabel = _iwrb.getLocalizedString("education", "Education: ");
			addTextToTable(table, row++, educationLabel, education);
		}
		if(_showSchool) {
			String school = userExtraInfo.getSchool();
			String schoolLabel = _iwrb.getLocalizedString("school", "School: ");
			addTextToTable(table, row++, schoolLabel, school);
		}
		if(_showArea) {
			String area = userExtraInfo.getArea();
			String areaLabel = _iwrb.getLocalizedString("area", "Area: ");
			addTextToTable(table, row++, areaLabel, area);
		}
		if(_showBeganWork) {
			String begunWork = (new IWTimestamp(userExtraInfo.getBeganWork())).getDateString("dd-MM-yyyy");
			String begunWorkLabel = _iwrb.getLocalizedString("begun_work", "Begun work: ");
			addTextToTable(table, row++, begunWorkLabel, begunWork);
		}
	}
	
	/**
	 * Utility method for adding text to a table
	 * @param table
	 * @param row
	 * @param strLabel
	 * @param strText
	 */
	private void addTextToTable(Table table, int row, String strLabel, String strText) {
		Text text = new Text(strText);
		Text label = new Text(strText);
		table.add(label, 2, row);
		table.add(text, 3, row);
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
			Iterator emailIter = _biz.getEmailList(user).iterator();
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
		String[] groupIds = iwc.getParameterValues(PARAM_NAME_GROUP_IDS);
		Set groupIdsSet = new HashSet(); // must use a Set first to prevent duplicate values
		if(groupIds!=null && groupIds.length>0) {
			groupIdsSet.add(Arrays.asList(groupIds));
		}
		if(_groupIds!=null && _groupIds.length>0) {
			groupIdsSet.add(Arrays.asList(_groupIds));
		}
		List groupIdsList = new ArrayList(groupIdsSet);
		List groups = new ArrayList();
		Iterator idsIter = groupIdsList.iterator();
		while(idsIter.hasNext()) {
			String id = (String) idsIter.next();
			try {
				Group group = _biz.getGroup(iwc, id);
				if(group!=null) {
					groups.add(group);
				}
			} catch(Exception e) {
				System.out.println("Exception getting group with id " + id + ", group not shown");
				e.printStackTrace();
			}
		}
		
		if(_sortGroups) {
			Collections.sort(groups, new Comparator() {
				public int compare(Object arg0, Object arg1) {
					String name0 = ((Group) arg0).getName();
					String name1 = ((Group) arg1).getName();
					return _collator.compare(name0, name1);
				}
			});
		}
		
		return groups;
	}
	
	public void setGroupIds(String[] groupIds) {
		_groupIds = groupIds;
	}
	
	// BEGIN setters/properties for block properties to select what info is shown, names of methods&properties should be self explanatory
	public void setShowWorkPhone(boolean value) {
		_showWorkPhone = value;
	}
	
	public void setShowHomePhone(boolean value) {
		_showHomePhone = value;
	}
	
	public void setShowMobilePhone(boolean value) {
		_showMobilePhone = value;
	}
		
	public void setShowAge(boolean value) {
		_showAge = value;
	}
	
	/*public void setShowStatus(boolean value) {
		_showStatus = value;
	}*/
	
	public void setEmails(boolean value) {
		_showEmails = value;
	}
	
	public void setTitle(boolean value) {
		_showTitle = value;
	}
	
	public void setEducation(boolean value) {
		_showEducation = value;
	}
	
	public void setSchool(boolean value) {
		_showSchool = value;
	}
	
	public void setArea(boolean value) {
		_showArea = value;
	}
	
	public void setBeganWork(boolean value) {
		_showBeganWork = value;
	}
	
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
	
	// END setters/properties for block properties to select what info is shown, names of methods&properties should be self explanatory
	
	private String[] _groupIds = null;
	private boolean _sortGroups = true; // if true, the groups to display members for are sorted alphabeticly
	
	private UserInfoBusiness _biz = null;
	private Collator _collator = null;
}
