/*
 * Created on Feb 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.user.presentation;

import java.sql.SQLException;
import java.util.Iterator;

import com.idega.block.user.business.UserInfoBusiness;
import com.idega.block.user.business.UserInfoBusinessBean;
import com.idega.block.user.data.UserExtraInfo;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Displays info about a user. Block properties can be user to select what to display.
 */
public class UserInfo extends Block {
	
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.user";
	
	public static final String PARAM_NAME_USER_ID = "user_id";
	
	private IWResourceBundle _iwrb = null;
	
	public void main(IWContext iwc) {
		_iwrb = getResourceBundle(iwc);
		_biz = UserInfoBusinessBean.getUserInfoBusiness(iwc);
		
		String userId = iwc.getParameter(PARAM_NAME_USER_ID);
		if(userId == null) {
			System.out.println("No userid found, no user info displayd");
			return;
		}
		
		User user = null;
		try {
			user =_biz.getUser(iwc, userId);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(user==null) {
			System.out.println("User with id " + userId + " not found, no user info displayd");
			return;
		}
		
		add(getMemberInfo(iwc, user));
	}
	
	private PresentationObject getMemberInfo(IWContext iwc, User user) {
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
				System.out.println("Could not display phones");
				e.printStackTrace();
				_showWorkPhone = _showHomePhone = _showMobilePhone = false;
			}
		}
		Table table = new Table();
		int row = 1;
		String name = user.getName();
		String nameLabel = _iwrb.getLocalizedString("name", "Name: ");
		table.add(nameLabel, 2, row);
		table.add(name, 3, row++);
		if(_showTitle) {
			String title = userExtraInfo.getTitle();
			String titleLabel = _iwrb.getLocalizedString("title", "Title: ");
			table.add(titleLabel, 2, row);
			table.add(title, 3, row++);
		}
		if(_showAge) {
			try {
				String age = _biz.getAge(user);
				String ageLabel = _iwrb.getLocalizedString("age", "Age: ");
				addTextToTable(table, row++, ageLabel, age);
			} catch(Exception e) {
				System.out.println("Could not display age");
				e.printStackTrace();
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
			Table emails = getEmailTable(user);
			Text emailsLabel = new Text(_iwrb.getLocalizedString("email", "Email: "));
			emailsLabel.setStyle(_textLabelStyle);
			table.add(emailsLabel, 2, row);
			table.add(emails, 3, row++);
		}
		/*if(_showStatus) {
			try {
				String status = _biz.getStatusKey(iwc, user, user.getPrimaryGroup());
				String statusLabel = _iwrb.getLocalizedString("status", "Status: ");
				addTextToTable(table, row++, statusLabel, status);
			} catch(Exception e) {
				System.out.println("Could not display user status");
				e.printStackTrace();
			}
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
		if(_showImage) {
			Image image = getImage(user);
			if (_imageWidth != null) {
				image.setWidth(_imageWidth);
			}
			if (_imageHeight != null) {
				image.setHeight(_imageHeight);
			}
			table.mergeCells(1, 1, 1, row);
			table.add(image, 1, 1);
		}
		
		//@TODO add a back link
		
		return table;
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
	 * Gets the image for a user
	 * @param user The user
	 * @return the image, null if no image for user
	 */
	private Image getImage(User user) {
		int imageId = user.getSystemImageID();
		Image image = null;
		if(imageId != -1) {
			try {
				image = new Image(imageId, _iwrb.getLocalizedString("member_overview_imag_text", "User picture"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No image found for user " + user.getName());
		}
		return image;
	}

	/**
	 * Gets a table containing user's emails
	 * @param user The user
	 * @return The user's emails in a Table
	 */
	private Table getEmailTable(User user) {
		Table table = new Table();
		int row = 1;
		try {
			Iterator addressIter = _biz.getEmailList(user).iterator();
			while(addressIter.hasNext()) {
				String address = (String) addressIter.next();
				Link link = new Link(address);
				link.setURL("mailto:" + address);
				link.setSessionId(false);
				table.add(link, 1, row++);
			}
		} catch(Exception e) {
			// don't give a pair of donkeys kiddneys, most likely means there are no emails for user
		}
		return table;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
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
	
	public void setShowImage(boolean value) {
		_showImage = value;
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
	
	public void setImageWidth(String width) {
		_imageWidth = width;
	}

	public void setImageHeight(String height) {
		_imageHeight = height;
	}
	
	public void setTextInfoStyle(String style) {
		_textInfoStyle = style;
	}

	public void setTextLabelStyle(String style) {
		_textLabelStyle = style;
	}
	
	private boolean _showImage = true;
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
	private String _imageWidth = null;
	private String _imageHeight = null;
	private String _textInfoStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;";
	private String _textLabelStyle = "font-family: Arial, Helvetica,sans-serif;font-weight:bold;font-size: 8pt;color: #000000;";
	
	// END setters/properties for block properties to select what info is shown, names of methods&properties should be self explanatory
	
	private UserInfoBusiness _biz = null;
}
