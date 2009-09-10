package com.idega.block.staff.presentation;

/**
 * Title: Description: Copyright: Copyright (c) 2000-2001 idega.is All Rights
 * Reserved Company: idega
 * 
 * @author <a href="mailto:laddi@idega.is">��rhallur "Laddi" Helgason </a>
 * @version 1.2
 */

import java.util.Collections;
import java.util.List;

import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.block.staff.business.StaffBusiness;
import com.idega.block.staff.business.StaffFinder;
import com.idega.block.staff.data.StaffInfo;
import com.idega.block.staff.data.StaffMetaData;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.data.GenericGroup;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.GenericUserComparator;
import com.idega.util.IWTimestamp;
import com.idega.util.text.StyleConstants;
import com.idega.util.text.TextStyler;

public class Staff extends Block implements Builderaware {

	private boolean _isAdmin = false;

	private int _userID = -1;

	public static final int ALL_STAFF = 1;

	public static final int DIVISION_STAFF = 2;

	public static final int USER = 3;

	private int _layout = ALL_STAFF;

	private boolean _addAlphabet = true;

	private boolean _hasAlphabetLetter;

	private String _alphabetLetter;

	private String _selectedLetterColor;

	private String _zebraColor1, _zebraColor2;

	private int row = -1;

	private String _name;

	private String _alphabetName;

	private boolean _styles = true;

	private boolean _showAge;

	private boolean _showGender;

	private boolean _showEducation;

	private boolean _showTitle;

	private boolean _showListTitle;

	private boolean _showSchool;

	private boolean _showBeganWork;

	private boolean _showArea;

	private boolean _showImage;

	private boolean _showMetaData;

	private boolean _showWorkPhone;

	private boolean _showListWorkPhone;

	private boolean _showMobilePhone;

	private String _imageWidth;

	private String _imageHeight;

	private String _width;

	private String _linkStyle;

	private String _visitedStyle;

	private String _activeStyle;

	private String _hoverStyle;

	private String _alphabetLinkStyle;

	private String _alphabetVisitedStyle;

	private String _alphabetActiveStyle;

	private String _alphabetHoverStyle;

	private String _textStyle;

	private String _headlineStyle;

	private IWBundle _iwb;

	private IWResourceBundle _iwrb;

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.staff";

	private Table _myTable;

	public Staff() {
		setDefaultValues();
	}

	public void main(IWContext iwc) throws Exception {
		this._iwb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		this._iwrb = getResourceBundle(iwc);
		this._isAdmin = iwc.hasEditPermission(this);

		this.row = 1;
		this._myTable = new Table();
		this._myTable.setWidth(this._width);

		if (this._isAdmin) {
			this._myTable.add(getAdminButtons(), 1, this.row);
			this.row++;
		}

		handleParameters(iwc);
		getStaff(iwc);

		add(this._myTable);
	}

	private void getStaff(IWContext iwc) {
		setStyles();

		if (this._addAlphabet && this._layout == ALL_STAFF) {
			this._myTable.add(getAlphabetTable(), 1, this.row);
			this.row++;
		}

		switch (this._layout) {
			case ALL_STAFF:
				getAllStaff(iwc);
				break;
			case DIVISION_STAFF:
				getDivisionStaff(iwc);
				break;
			case USER:
				getUser(iwc);
				break;
		}
	}

	private void getAllStaff(IWContext iwc) {
		List users = null;
		if (this._hasAlphabetLetter) {
			users = StaffFinder.getAllUsersByLetter(iwc, this._alphabetLetter);
		}
		else {
			users = StaffFinder.getAllUsers(iwc);
		}

		getStaffTable(iwc, users);
	}

	private void getStaffTable(IWContext iwc, List users) {
		Table table = new Table();
		table.setWidth("100%");
		table.setCellspacing(0);
		table.setCellpadding(3);

		int staffRow = 1;

		User user = null;
		Email email = null;
		Link userLink = null;
		Link emailLink = null;
		Text titleText = null;
		Text phoneText = null;
		StaffInfo staffInfo = null;
		Phone phone = null;
		int column = 1;

		if (users != null) {
			GenericUserComparator comparator = new GenericUserComparator(GenericUserComparator.NAME);
			Collections.sort(users, comparator);
			for (int a = 0; a < users.size(); a++) {
				column = 1;
				user = (User) users.get(a);
				email = StaffFinder.getUserEmail(user);
				if (this._showListTitle) {
					staffInfo = StaffFinder.getStaffInfo(user.getID());
				}
				if (this._showListWorkPhone) {
					phone = UserBusiness.getUserPhone(user.getID(), com.idega.core.contact.data.PhoneTypeBMPBean.WORK_PHONE_ID);
				}

				userLink = getStaffLink(user);
				emailLink = getEmailLink(email);

				if (staffInfo != null && staffInfo.getTitle() != null) {
					titleText = new Text(staffInfo.getTitle());
				}
				else {
					titleText = new Text("");
				}
				titleText.setFontStyle(this._textStyle);

				if (phone != null) {
					phoneText = new Text(phone.getNumber());
				}
				else {
					phoneText = new Text("");
				}
				phoneText.setFontStyle(this._textStyle);

				table.add(userLink, column++, staffRow);
				if (this._showListTitle) {
					table.add(titleText, column++, staffRow);
				}
				if (this._showListWorkPhone) {
					table.add(phoneText, column++, staffRow);
				}
				if (emailLink != null) {
					table.add(emailLink, column++, staffRow);
				}

				if (this._isAdmin) {
					table.add(getEditLink(user), column++, staffRow);
				}

				staffRow++;
			}
		}

		if (this._zebraColor1 != null && this._zebraColor2 != null) {
			table.setHorizontalZebraColored(this._zebraColor1, this._zebraColor2);
		}
		for (int a = 2; a <= table.getColumns(); a++) {
			table.setColumnAlignment(a, "center");
		}
		this._myTable.add(table, 1, this.row);
	}

	private void getDivisionStaff(IWContext iwc) {
		List groups = StaffFinder.getAllGroups(iwc);
		if (groups != null) {
			for (int a = 0; a < groups.size(); a++) {
				List users = StaffFinder.getUsersInPrimaryGroup((GenericGroup) groups.get(a));
				if (users != null && users.size() > 0) {
					this._myTable.add(((GenericGroup) groups.get(a)).getName(), 1, this.row);
					this.row++;
					getStaffTable(iwc, users);
					this.row++;
					this._myTable.setHeight(1, this.row, "6");
					this.row++;
				}
			}
		}
	}

	private void getUser(IWContext iwc) {
		User user = StaffFinder.getUser(this._userID);
		StaffInfo staffInfo = StaffFinder.getStaffInfo(this._userID);
		StaffMetaData[] staffMeta = StaffFinder.getMetaData(this._userID);
		Phone workphone = UserBusiness.getUserPhone(this._userID, com.idega.core.contact.data.PhoneTypeBMPBean.WORK_PHONE_ID);
		Phone mobilephone = UserBusiness.getUserPhone(this._userID, com.idega.core.contact.data.PhoneTypeBMPBean.MOBILE_PHONE_ID);

		Table userTable = new Table();
		userTable.setWidth("100%");
		userTable.setCellpadding(0);
		userTable.setCellspacing(0);

		Table textTable = new Table();
		textTable.setWidth("100%");
		//textTable.setWidth(1,"110");

		int tableRow = 1;
		int column = 1;

		Image image = null;
		if (staffInfo != null && staffInfo.getImageID() != -1) {
			try {
				image = new Image(staffInfo.getImageID());
				if (this._imageWidth != null) {
					image.setWidth(this._imageWidth);
				}
				if (this._imageHeight != null) {
					image.setHeight(this._imageHeight);
				}
				image.setBorder(1);
				image.setVerticalSpacing(3);
				image.setHorizontalSpacing(10);
			} catch (Exception e) {
				image = null;
			}

			if (image != null) {
				userTable.add(image, 1, 1);
			}
		}

		if (user != null) {
			Text name = new Text(this._iwrb.getLocalizedString("user_name", "Name") + ":");
			name.setFontStyle(this._headlineStyle);
			Text nameText = new Text(user.getName());
			nameText.setFontStyle(this._textStyle);

			textTable.add(name, column, tableRow);
			textTable.add(nameText, column + 1, tableRow);
			tableRow++;

			IWTimestamp dateOfBirth = null;
			if (user.getDateOfBirth() != null) {
				dateOfBirth = new IWTimestamp(user.getDateOfBirth());
			}
			IWTimestamp dateToday = new IWTimestamp();

			int userAge = 0;
			if (dateOfBirth != null) {
				userAge = (new IWTimestamp().getDaysBetween(dateOfBirth, dateToday)) / 365;
			}

			Text age = new Text(this._iwrb.getLocalizedString("user_age", "Age") + ":");
			age.setFontStyle(this._headlineStyle);
			Text ageText = new Text(Integer.toString(userAge));
			ageText.setFontStyle(this._textStyle);

			if (this._showAge && userAge > 0) {
				textTable.add(age, column, tableRow);
				textTable.add(ageText, column + 1, tableRow);
				tableRow++;
			}

			/*
			 * Text gender = new
			 * Text(_iwrb.getLocalizedString("user_gender","Gender")+":");
			 * gender.setFontStyle(_headlineStyle); Text genderText = new
			 * Text(""); genderText.setFontStyle(_textStyle);
			 * 
			 * if ( _showGender ) { textTable.add(gender,column,tableRow);
			 * textTable.add(genderText,column+1,tableRow); tableRow++; }
			 */

			Text title = new Text(this._iwrb.getLocalizedString("user_title", "Title") + ":");
			title.setFontStyle(this._headlineStyle);
			Text titleText = new Text("");
			if (staffInfo != null) {
				titleText.setText(staffInfo.getTitle());
			}
			titleText.setFontStyle(this._textStyle);

			if (this._showTitle) {
				textTable.add(title, column, tableRow);
				textTable.add(titleText, column + 1, tableRow);
				tableRow++;
			}

			Text workPhone = new Text(this._iwrb.getLocalizedString("work_phone", "Work phone") + ":");
			workPhone.setFontStyle(this._headlineStyle);
			Text workPhoneText = new Text("");
			if (workphone != null) {
				workPhoneText.setText(workphone.getNumber());
			}
			workPhoneText.setFontStyle(this._textStyle);

			if (this._showWorkPhone) {
				textTable.add(workPhone, column, tableRow);
				textTable.add(workPhoneText, column + 1, tableRow);
				tableRow++;
			}

			Text mobilePhone = new Text(this._iwrb.getLocalizedString("Mobile_phone", "Mobile phone") + ":");
			mobilePhone.setFontStyle(this._headlineStyle);
			Text mobilePhoneText = new Text("");
			if (mobilephone != null) {
				mobilePhoneText.setText(mobilephone.getNumber());
			}
			mobilePhoneText.setFontStyle(this._textStyle);

			if (this._showMobilePhone) {
				textTable.add(mobilePhone, column, tableRow);
				textTable.add(mobilePhoneText, column + 1, tableRow);
				tableRow++;
			}

			Text area = new Text(this._iwrb.getLocalizedString("user_area", "Area") + ":");
			area.setFontStyle(this._headlineStyle);
			Text areaText = new Text("");
			if (staffInfo != null) {
				areaText.setText(staffInfo.getArea());
			}
			areaText.setFontStyle(this._textStyle);

			if (this._showArea) {
				textTable.add(area, column, tableRow);
				textTable.add(areaText, column + 1, tableRow);
				tableRow++;
			}

			Text beganWork = new Text(this._iwrb.getLocalizedString("user_began_work", "Began work") + ":");
			beganWork.setFontStyle(this._headlineStyle);
			Text beganWorkText = new Text("");
			if (staffInfo != null && staffInfo.getBeganWork() != null) {
				beganWorkText.setText(new IWTimestamp(staffInfo.getBeganWork()).getLocaleDate(iwc));
			}
			beganWorkText.setFontStyle(this._textStyle);

			if (this._showBeganWork) {
				textTable.add(beganWork, column, tableRow);
				textTable.add(beganWorkText, column + 1, tableRow);
				tableRow++;
			}

			Text education = new Text(this._iwrb.getLocalizedString("user_education", "Education") + ":");
			education.setFontStyle(this._headlineStyle);
			Text educationText = new Text("");
			if (staffInfo != null) {
				educationText.setText(staffInfo.getEducation());
			}
			educationText.setFontStyle(this._textStyle);

			if (this._showEducation) {
				textTable.add(education, column, tableRow);
				textTable.add(educationText, column + 1, tableRow);
				tableRow++;
			}

			Text school = new Text(this._iwrb.getLocalizedString("user_school", "School") + ":");
			school.setFontStyle(this._headlineStyle);
			Text schoolText = new Text("");
			if (staffInfo != null) {
				schoolText.setText(staffInfo.getSchool());
			}
			schoolText.setFontStyle(this._textStyle);

			if (this._showSchool) {
				textTable.add(school, column, tableRow);
				textTable.add(schoolText, column + 1, tableRow);
				tableRow++;
			}

			if (staffMeta != null && staffMeta.length > 0 && this._showMetaData) {
				for (int a = 0; a < staffMeta.length; a++) {
					Text meta = new Text(staffMeta[a].getAttribute() + ":");
					meta.setFontStyle(this._headlineStyle);
					Text metaText = new Text(staffMeta[a].getValue());
					metaText.setFontStyle(this._textStyle);

					textTable.add(meta, column, tableRow);
					textTable.add(metaText, column + 1, tableRow);
					tableRow++;
				}
			}
		}

		int index = -1;

		List users = StaffFinder.getAllUsers(iwc);
		if (users != null) {
			GenericUserComparator comparator = new GenericUserComparator(GenericUserComparator.NAME);
			Collections.sort(users, comparator);
			index = users.indexOf(user);
		}

		Table linkTable = new Table(3, 1);
		linkTable.setWidth(1, "33%");
		linkTable.setWidth(2, "33%");
		linkTable.setWidth(3, "33%");
		linkTable.setWidth("100%");
		linkTable.setAlignment(2, 1, "center");

		Link nextLink = getNextUserLink(users, index);
		Link previousLink = getPreviousUserLink(users, index);
		Link backLink = new Link("< " + this._iwrb.getLocalizedString("back", "Back") + " >");
		backLink.setStyle(this._name);

		if (previousLink != null) {
			linkTable.add(previousLink, 1, 1);
		}
		linkTable.add(backLink, 2, 1);
		if (nextLink != null) {
			linkTable.add(nextLink, 3, 1);
			linkTable.setAlignment(3, 1, "right");
		}

		if (image != null) {
			userTable.add(textTable, 2, 1);
			userTable.setWidth(2, "100%");
			userTable.mergeCells(1, 5, 2, 5);
			userTable.add(linkTable, 1, 5);
		} else {
			userTable.add(textTable, 1, 1);
			userTable.add(linkTable, 1, 5);
		}

		textTable.setWidth(1, "90");
		textTable.setColumnVerticalAlignment(1, "top");
		textTable.setColumnVerticalAlignment(2, "top");
		userTable.setColumnVerticalAlignment(1, "top");
		userTable.setColumnVerticalAlignment(2, "top");

		this._myTable.add(userTable, 1, this.row);

		if (this._isAdmin) {
			this._myTable.add(getEditLink(user), 1, this.row + 1);
		}
	}

	private Link getNextUserLink(List users, int index) {
		Link link = new Link(this._iwrb.getLocalizedString("next_user", "Next") + " >>");
		link.setStyle(this._name);

		if (users.size() > index + 1) {
			link.addParameter(StaffBusiness.PARAMETER_USER_ID, ((User) users.get(index + 1)).getID());
		}
		else {
			link = null;
		}

		return link;
	}

	private Link getPreviousUserLink(List users, int index) {
		Link link = new Link("<< " + this._iwrb.getLocalizedString("previous_user", "Prev"));
		link.setStyle(this._name);

		if (index > 0) {
			link.addParameter(StaffBusiness.PARAMETER_USER_ID, ((User) users.get(index - 1)).getID());
		}
		else {
			link = null;
		}

		return link;
	}

	private Link getStaffLink(User user) {
		String name = user.getName();

		Link link = new Link(name);
		if (this._styles) {
			link.setStyle(this._name);
		}
		link.addParameter(StaffBusiness.PARAMETER_USER_ID, user.getID());

		return link;
	}

	private Link getEmailLink(Email email) {
		Link link = null;

		if (email != null) {
			link = new Link(email.getEmailAddress());
			if (this._styles) {
				link.setStyle(this._name);
			}
			link.setURL("mailto:" + email.getEmailAddress());
		}

		return link;
	}

	private Link getEditLink(User user) {
		Image adminImage = this._iwb.getImage("shared/edit.gif");
		Link adminLink = new Link(adminImage);
		adminLink.setWindowToOpen(StaffPropertyWindow.class);
		adminLink.addParameter(StaffPropertyWindow.PARAMETERSTRING_USER_ID, user.getID());

		return adminLink;
	}

	private Table getAlphabetTable() {
		String[] alphabet = { "A", "�", "B", "C", "D", "E", "�", "F", "G", "H", "I", "�", "J", "K", "L", "M", "N", "O", "�", "P", "Q", "R", "S", "T",
				"U", "�", "V", "W", "X", "Y", "�", "Z", "�", "�", "�", this._iwrb.getLocalizedString("all", "Allir") };
		Table table = new Table();
		int column = 1;

		Link link = null;
		Text divider = new Text(" - ");
		divider.setFontStyle(this._alphabetLinkStyle);

		for (int a = 0; a < alphabet.length; a++) {
			if (this._alphabetLetter != null && this._alphabetLetter.equalsIgnoreCase(alphabet[a])) {
				Text text = new Text(alphabet[a]);
				if (this._styles) {
					TextStyler styler = new TextStyler(this._alphabetLinkStyle);
					styler.setStyleValue(StyleConstants.ATTRIBUTE_COLOR, this._selectedLetterColor);
					styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_WEIGHT, StyleConstants.FONT_WEIGHT_BOLD);
					text.setFontStyle(styler.getStyleString());
				}
				table.add(text, column, 1);
			} else {
				link = new Link(alphabet[a]);
				if (this._styles) {
					link.setStyle(this._alphabetName);
				}
				link.addParameter(StaffBusiness.PARAMETER_LETTER, alphabet[a]);
				table.add(link, column, 1);
			}
			column++;
		}

		return table;
	}

	private Link getAdminButtons() {
		Image adminImage = this._iwb.getImage("shared/edit.gif");
		Link adminLink = new Link(adminImage);
		adminLink.setWindowToOpen(com.idega.core.user.presentation.UserModule.class);

		return adminLink;
	}

	private void handleParameters(IWContext iwc) {
		if (iwc.getParameter(StaffBusiness.PARAMETER_LETTER) != null) {
			this._alphabetLetter = iwc.getParameter(StaffBusiness.PARAMETER_LETTER);
			this._hasAlphabetLetter = true;
			if (this._alphabetLetter != null && this._alphabetLetter.equalsIgnoreCase(this._iwrb.getLocalizedString("all", "Allir"))) {
				this._hasAlphabetLetter = false;
			}
		} else {
			this._alphabetLetter = this._iwrb.getLocalizedString("all", "Allir");
			this._hasAlphabetLetter = false;
		}

		if (iwc.getParameter(StaffBusiness.PARAMETER_USER_ID) != null) {
			try {
				this._userID = Integer.parseInt(iwc.getParameter(StaffBusiness.PARAMETER_USER_ID));
				this._layout = USER;
			} catch (NumberFormatException e) {
				this._userID = -1;
				this._layout = ALL_STAFF;
			}

		}
	}

	private void setStyles() {
		if (this._name == null) {
			this._name = this.getName();
		}
		if (this._name == null) {
			if (getICObjectInstanceID() != -1) {
				this._name = "staff_" + Integer.toString(getICObjectInstanceID());
			}
			else {
				this._name = "staff_" + Double.toString(Math.random());
			}
		}
		this._alphabetName = "alpha_" + this._name;

		if (getParentPage() != null) {
			getParentPage().setStyleDefinition("A." + this._name + ":link", this._linkStyle);
			getParentPage().setStyleDefinition("A." + this._name + ":visited", this._visitedStyle);
			getParentPage().setStyleDefinition("A." + this._name + ":active", this._activeStyle);
			getParentPage().setStyleDefinition("A." + this._name + ":hover", this._hoverStyle);
			getParentPage().setStyleDefinition("A." + this._alphabetName + ":link", this._alphabetLinkStyle);
			getParentPage().setStyleDefinition("A." + this._alphabetName + ":visited", this._alphabetVisitedStyle);
			getParentPage().setStyleDefinition("A." + this._alphabetName + ":active", this._alphabetActiveStyle);
			getParentPage().setStyleDefinition("A." + this._alphabetName + ":hover", this._alphabetHoverStyle);
		} else {
			this._styles = false;
		}
	}

	private void setDefaultValues() {
		this._width = "300";
		this._selectedLetterColor = "#0000CC";
		this._linkStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;text-decoration: none;";
		this._visitedStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;text-decoration: none;";
		this._activeStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;text-decoration: none;";
		this._hoverStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;text-decoration: underline;";
		this._alphabetLinkStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;text-decoration: none;";
		this._alphabetVisitedStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;text-decoration: none;";
		this._alphabetActiveStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;text-decoration: none;";
		this._alphabetHoverStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;text-decoration: underline;";
		this._textStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;";
		this._headlineStyle = "font-family: Arial, Helvetica,sans-serif;font-weight:bold;font-size: 8pt;color: #000000;";

		this._showAge = true;
		this._showGender = true;
		this._showEducation = true;
		this._showTitle = true;
		this._showSchool = true;
		this._showBeganWork = true;
		this._showArea = true;
		this._showImage = true;
		this._showMetaData = true;
	}

	public void setShowAlphabet(boolean showAlphabet) {
		this._addAlphabet = showAlphabet;
	}

	public void setShowAge(boolean showAge) {
		this._showAge = showAge;
	}

	public void setShowWorkPhone(boolean showWorkPhone) {
		this._showWorkPhone = showWorkPhone;
	}

	public void setShowListWorkPhone(boolean showWorkPhone) {
		this._showListWorkPhone = showWorkPhone;
	}

	public void setShowMobilePhone(boolean showMobilePhone) {
		this._showMobilePhone = showMobilePhone;
	}

	public void setShowGender(boolean showGender) {
		this._showGender = showGender;
	}

	public void setShowEducation(boolean showEducation) {
		this._showEducation = showEducation;
	}

	public void setShowTitle(boolean showTitle) {
		this._showTitle = showTitle;
	}

	public void setShowListTitle(boolean showTitle) {
		this._showListTitle = showTitle;
	}

	public void setShowSchool(boolean showSchool) {
		this._showSchool = showSchool;
	}

	public void setShowBeganWork(boolean showBeganWork) {
		this._showBeganWork = showBeganWork;
	}

	public void setShowArea(boolean showArea) {
		this._showArea = showArea;
	}

	public void setShowImage(boolean showImage) {
		this._showImage = showImage;
	}

	public void setShowExtraInfo(boolean showExtraInfo) {
		this._showMetaData = showExtraInfo;
	}

	public void setImageWidth(String width) {
		this._imageWidth = width;
	}

	public void setImageHeight(String height) {
		this._imageHeight = height;
	}

	public void setTextStyle(String style) {
		this._textStyle = style;
	}

	public void setHeadlineStyle(String style) {
		this._headlineStyle = style;
	}

	public void setLinkStyle(String style, String style2, String style3, String style4) {
		this._linkStyle = style;
		this._visitedStyle = style2;
		this._activeStyle = style3;
		this._hoverStyle = style4;
	}

	public void setAlphabetLinkStyle(String style, String style2, String style3, String style4) {
		this._alphabetLinkStyle = style;
		this._alphabetVisitedStyle = style2;
		this._alphabetActiveStyle = style3;
		this._alphabetHoverStyle = style4;
	}

	public void setWidth(String width) {
		this._width = width;
	}

	public void setSelectedLetterColor(String color) {
		this._selectedLetterColor = color;
	}

	public void setZebraColors(String color1, String color2) {
		this._zebraColor1 = color1;
		this._zebraColor2 = color2;
	}

	public void setLayout(int layout) {
		this._layout = layout;
	}

	public boolean deleteBlock(int ICObjectInstanceId) {
		return true;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public Object clone() {
		Staff obj = null;
		try {
			obj = (Staff) super.clone();

			if (this._myTable != null) {
				obj._myTable = (Table) this._myTable.clone();
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}

}