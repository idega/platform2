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
		this._iwrb = getResourceBundle(iwc);
		this._biz = UserInfoBusinessBean.getUserInfoBusiness(iwc);
		
		String userId = iwc.getParameter(PARAM_NAME_USER_ID);
		if(userId == null) {
			System.out.println("No userid found, no user info displayd");
			return;
		}
		
		User user = null;
		try {
			user =this._biz.getUser(iwc, userId);
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
				this._showTitle = this._showEducation = this._showSchool = this._showArea = this._showBeganWork = false;
			}
		}
		boolean showPhone = this._showWorkPhone || this._showHomePhone || this._showMobilePhone;
		String [] phones = null;
		if(showPhone) {
			try {
				phones = this._biz.getPhones(user);
			} catch(Exception e) {
				System.out.println("Could not display phones");
				e.printStackTrace();
				this._showWorkPhone = this._showHomePhone = this._showMobilePhone = false;
			}
		}
		Table table = new Table();
		int row = 1;
		String name = user.getName();
		String nameLabel = this._iwrb.getLocalizedString("name", "Name: ");
		table.add(nameLabel, 2, row);
		table.add(name, 3, row++);
		if(this._showTitle) {
			String title = userExtraInfo.getTitle();
			if(this._showEmptyFields || (title!=null && title.length()>0)) {
				System.out.println("title is " + title);
				String titleLabel = this._iwrb.getLocalizedString("title", "Title: ");
				addTextToTable(table, row++, titleLabel, title);
			}
		}
		if(this._showAge) {
			try {
				String age = this._biz.getAge(user);
				if(this._showEmptyFields || (age!=null && age.length()>0)) {
					System.out.println("age is " + age);
					String ageLabel = this._iwrb.getLocalizedString("age", "Age: ");
					addTextToTable(table, row++, ageLabel, age);
				}
			} catch(Exception e) {
				System.out.println("Could not display age");
				e.printStackTrace();
			}
		}
		if(this._showWorkPhone) {
			String workPhone = phones[0];
			if(this._showEmptyFields || (workPhone!=null && workPhone.length()>0)) {
				System.out.println("workphone is " + workPhone);
				String workPhoneLabel = this._iwrb.getLocalizedString("workphone", "Workphone: ");
				addTextToTable(table, row++, workPhoneLabel, workPhone);
			}
		}
		if(this._showHomePhone) {
			String homePhone = phones[1];
			if(this._showEmptyFields || (homePhone!=null && homePhone.length()>0)) {
				System.out.println("homephone is " + homePhone);
				String homePhoneLabel = this._iwrb.getLocalizedString("homephone", "Homephone: ");
				addTextToTable(table, row++, homePhoneLabel, homePhone);
			}
		}
		if(this._showMobilePhone) {
			String mobilePhone = phones[2];
			if(this._showEmptyFields || (mobilePhone!=null && mobilePhone.length()>0)) {
				System.out.println("mobilephone is " + mobilePhone);
				String mobilePhoneLabel = this._iwrb.getLocalizedString("mobilePhone", "Mobilephone: ");
				addTextToTable(table, row++, mobilePhoneLabel, mobilePhone);
			}
		}
		if(this._showEmails) {
			Table emails = getEmailTable(user);
			if(this._showEmptyFields || (emails!=null && emails.getRows()>0)) {
				Text emailsLabel = new Text(this._iwrb.getLocalizedString("email", "Email: "));
				emailsLabel.setStyle(this._textLabelStyle);
				table.add(emailsLabel, 2, row);
				if(emails.getRows()>0) {
					table.add(emails, 3, row++);
				}
			}
		}
		/*if(_showStatus) {
			try {
				String status = _biz.getStatusKey(iwc, user, user.getPrimaryGroup());
				if(_showEmptyFields || (status!=null && status.length()>0)) {
					String statusLabel = _iwrb.getLocalizedString("status", "Status: ");
					addTextToTable(table, row++, statusLabel, status);
				}
			} catch(Exception e) {
				System.out.println("Could not display user status");
				e.printStackTrace();
			}
		}*/
		if(this._showEducation) {
			String education = userExtraInfo.getEducation();
			if(this._showEmptyFields || (education!=null && education.length()>0)) {
				System.out.println("education is " + education);
				String educationLabel = this._iwrb.getLocalizedString("education", "Education: ");
				addTextToTable(table, row++, educationLabel, education);
			}
		}
		if(this._showSchool) {
			String school = userExtraInfo.getSchool();
			if(this._showEmptyFields || (school!=null && school.length()>0)) {
				System.out.println("school is " + school);
				String schoolLabel = this._iwrb.getLocalizedString("school", "School: ");
				addTextToTable(table, row++, schoolLabel, school);
			}
		}
		if(this._showArea) {
			String area = userExtraInfo.getArea();
			if(this._showEmptyFields || (area!=null && area.length()>0)) {
				System.out.println("area is " + area);
				String areaLabel = this._iwrb.getLocalizedString("area", "Area: ");
				addTextToTable(table, row++, areaLabel, area);
			}
		}
		if(this._showBeganWork) {
			String begunWork = (new IWTimestamp(userExtraInfo.getBeganWork())).getDateString("dd-MM-yyyy");
			if(this._showEmptyFields || (begunWork!=null && begunWork.length()>0)) {
				System.out.println("begunWork is " + begunWork);
				String begunWorkLabel = this._iwrb.getLocalizedString("begun_work", "Begun work: ");
				addTextToTable(table, row++, begunWorkLabel, begunWork);
			}
		}
		if(this._showImage) {
			Image image = getImage(user);
			if (this._imageWidth != null) {
				image.setWidth(this._imageWidth);
			}
			if (this._imageHeight != null) {
				image.setHeight(this._imageHeight);
			}
			table.mergeCells(1, 1, 1, row);
			table.add(image, 1, 1);
		}
		
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
		text.setStyle(this._textInfoStyle);
		Text label = new Text(strLabel);
		label.setStyle(this._textLabelStyle);
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
				image = new Image(imageId, this._iwrb.getLocalizedString("member_overview_imag_text", "User picture"));
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
			Iterator addressIter = this._biz.getEmailList(user).iterator();
			while(addressIter.hasNext()) {
				String address = (String) addressIter.next();
				if(address==null || address.length()==0) {
					continue;
				}
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
		this._showWorkPhone = value;
	}
	
	public void setShowHomePhone(boolean value) {
		this._showHomePhone = value;
	}
	
	public void setShowMobilePhone(boolean value) {
		this._showMobilePhone = value;
	}
	
	public void setShowImage(boolean value) {
		this._showImage = value;
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
	
	public void setImageWidth(String width) {
		this._imageWidth = width;
	}

	public void setImageHeight(String height) {
		this._imageHeight = height;
	}
	
	public void setTextInfoStyle(String style) {
		this._textInfoStyle = style;
	}

	public void setTextLabelStyle(String style) {
		this._textLabelStyle = style;
	}
	
	public void setShowEmptyFields(boolean value) {
		this._showEmptyFields = value;
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
	private boolean _showEmptyFields = false;
	private String _imageWidth = null;
	private String _imageHeight = null;
	private String _textInfoStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;";
	private String _textLabelStyle = "font-family: Arial, Helvetica,sans-serif;font-weight:bold;font-size: 8pt;color: #000000;";
	
	// END setters/properties for block properties to select what info is shown, names of methods&properties should be self explanatory
	
	private UserInfoBusiness _biz = null;
}
