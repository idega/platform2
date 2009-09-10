package com.idega.block.staff.presentation;

/**
 * Title: Old version of StaffBrowser using the old legacy system.
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:laddi@idega.is">��rhallur "Laddi" Helgason</a>
 * @version 1.2
 */

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import com.idega.block.staff.business.StaffBusiness;
import com.idega.block.staff.business.StaffFinder;
import com.idega.block.staff.business.StaffHolder;
import com.idega.core.builder.data.ICPage;
import com.idega.core.data.GenericGroup;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.app.UserApplication;
import com.idega.user.data.Group;
import com.idega.util.GenericGroupComparator;
import com.idega.util.GenericUserComparator;
import com.idega.util.text.StyleConstants;
import com.idega.util.text.TextStyler;

public class StaffBrowser extends Block implements Builderaware {

	private String _emailAlignment;
	private boolean _isAdmin = false;
	private int _userID = -1;
	private int _localeID;

	public static final int ALL_STAFF = 1;
	public static final int DIVISION_STAFF = 2;
	public static final int USER = 3;
	private int _layout = ALL_STAFF;

	private boolean _addAlphabet = false;
	private boolean _hasAlphabetLetter;
	private String _alphabetLetter;
	private String _selectedLetterColor;
	private String _zebraColor1, _zebraColor2;

	private int row = -1;
	private String _name;
	private String _alphabetName;
	private boolean _styles = true;

	private boolean _showAge;
	//private boolean _showGender;
	private boolean _showEducation;
	private boolean _showTitle;
	private boolean _showListTitle;
	private boolean _showBeganWork;
	private boolean _showArea;
	private boolean _showImage;
	private boolean _showMetaData;
	private boolean _showWorkPhone;
	private boolean _showListWorkPhone;
	private boolean _showMobilePhone;
	private boolean _showEmail;
	private boolean _allowUserEdit= false;
	private boolean _showPictureInList = false;

	private String _imageWidth;
	private String _imageHeight;
	private String _nameWidth;
	private String _attributesWidth;

	private String _width;
	private String _linkStyle;
	private String _hoverStyle;
	private String _alphabetLinkStyle;
	private String _alphabetHoverStyle;

	private String _textStyle;
	private String _headlineStyle;
	private String _divisionStyle;

	private ICPage _page;
	private ICPage _backPage;

	private IWBundle _iwb;
	private IWResourceBundle _iwrb;
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.staff";

	private Table _myTable;

	private String _emailWidth;
	private String _workPhoneWidth;
	private String _titleWidth;

	private boolean _showDetails = true;

	private Group _group;
	private int _sortNamesBy = GenericUserComparator.FIRSTLASTMIDDLE;

	private boolean _showDivisionHeader = true;

	private String extraAlignment = Table.HORIZONTAL_ALIGN_CENTER;
	private boolean _sortAlphabetically = true;
	private boolean _sortGroupsAlphabetically = true;
	private boolean _showNavigationLinks = true;

	public StaffBrowser() {
		setDefaultValues();
	}

	public void main(IWContext iwc) throws Exception {
		this._iwb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		this._iwrb = getResourceBundle(iwc);
		this._isAdmin = iwc.hasEditPermission(this);
		this._localeID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());

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
			case ALL_STAFF :
				getAllStaff(iwc);
				break;
			case DIVISION_STAFF :
				getDivisionStaff(iwc);
				break;
			case USER :
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

		Link userLink = null;
		Text userName = null;
		Link emailLink = null;
		Text titleText = null;
		Text phoneText = null;
		StaffHolder holder = null;
		Image image = null;
		int column = 1;
		int emailColumn = -1;

		if (users != null) {
			if (this._sortAlphabetically) {
				Collections.sort(users, new GenericUserComparator(iwc.getCurrentLocale(), this._sortNamesBy));
			}
			
			Iterator iter = users.iterator();
			while (iter.hasNext()) {
				column = 1;
				holder = StaffFinder.getStaffHolder((User) iter.next(), this._localeID);

				userLink = getStaffLink(holder.getName(), holder.getUserID());
				userName = getStaffText(holder.getName());
				emailLink = getEmailLink(holder.getEmail());

				if (holder.getTitle() != null) {
					titleText = new Text(holder.getTitle());
				}
				else {
					titleText = new Text("");
				}
				titleText.setFontStyle(this._textStyle);

				if (holder.getWorkPhone() != null) {
					phoneText = new Text(holder.getWorkPhone());
				}
				else {
					phoneText = new Text("");
				}
				phoneText.setFontStyle(this._textStyle);
				
				if(!this._showPictureInList){
					if (isShowDetails()) {
						table.add(userLink, column++, staffRow);
					}
					else {
						table.add(userName, column++, staffRow);
					}
				
					if (this._showListTitle) {
						if (this._titleWidth != null) {
							table.setWidth(column, this._titleWidth);
						}
						table.add(titleText, column++, staffRow);
					}
					if (this._showListWorkPhone) {
						if (this._workPhoneWidth != null) {
							table.setWidth(column, this._workPhoneWidth);
						}
						table.add(phoneText, column++, staffRow);
					}
					if (emailLink != null) {
						if (this._emailWidth != null) {
							table.setWidth(column, this._emailWidth);
						}
						if (this._emailAlignment != null) {
							emailColumn = column;
							table.setAlignment(column, staffRow, this._emailAlignment);
						}
						table.add(emailLink, column++, staffRow);
					}
				}
				else{
					column = 2;
					int imageStartRow = staffRow;
					if (holder.getImageID() != -1) {
						try {
							image = new Image(holder.getImageID());
							if (this._imageWidth != null) {
								image.setWidth(this._imageWidth);
							}
							if (this._imageHeight != null) {
								image.setHeight(this._imageHeight);
							}
							image.setBorder(1);
							image.setVerticalSpacing(3);
							image.setHorizontalSpacing(10);
						}
						catch (Exception e) {
							image = null;
						}

						if (image != null) {
							table.add(image, 1, staffRow);
						}
					}
					staffRow++;
					if (isShowDetails()) {
						table.add(userLink, column, ++staffRow);
					}
					else {
						table.add(userName, column, ++staffRow);
					}
					
					if (this._showListTitle) {
						table.add(titleText, column, ++staffRow);
					}
					if (this._showListWorkPhone) {
						table.add(phoneText, column, ++staffRow);
					}
					if (emailLink != null) {
						table.add(emailLink, column, ++staffRow);
					}
					table.mergeCells(1,imageStartRow,1,staffRow);
					column++;
				}

				if (this._isAdmin || (this._allowUserEdit && iwc.getUserId() == holder.getUserID())) {
					table.setAlignment(column, staffRow, Table.HORIZONTAL_ALIGN_RIGHT);
					table.add(getEditLink(holder.getUserID()), column, staffRow);
					if(this._isAdmin) {
						table.add(getDeleteLink(holder.getUserID()), column, staffRow);
					}
				}

				staffRow++;
			}
		}

		if (this._zebraColor1 != null && this._zebraColor2 != null) {
			table.setHorizontalZebraColored(this._zebraColor1, this._zebraColor2);
		}
		int centeredColumn = 2;
		if (this._showListTitle) {
			centeredColumn = 3;
		}
		for (int a = centeredColumn; a <= table.getColumns(); a++) {
			if (a != emailColumn) {
				table.setColumnAlignment(a, this.extraAlignment);
			}
		}
		if (this._nameWidth != null) {
			table.setWidth(1, this._nameWidth);
		}
		this._myTable.add(table, 1, this.row);
	}

	private void getDivisionStaff(IWContext iwc) {
		List groups = StaffFinder.getAllGroups(iwc);
		if (this._sortGroupsAlphabetically) {
			Collections.sort(groups, new GenericGroupComparator(iwc));
		}
		boolean showDivision = true;
		
		Text divisionText = null;
		if (groups != null) {
			Iterator iterator = groups.iterator();
			while (iterator.hasNext()) {
				GenericGroup group = (GenericGroup) iterator.next();
				
				if (this._group != null) {
					showDivision = false;
					if (((Integer)this._group.getPrimaryKey()).intValue() == ((Integer)group.getPrimaryKey()).intValue()) {
						showDivision = true;
					}
				}
				
				if (showDivision) {
					List users = StaffFinder.getUsersInPrimaryGroup(group);
					if (users != null && users.size() > 0) {
						if (this._showDivisionHeader) {
							divisionText = new Text(group.getName());
							divisionText.setFontStyle(this._divisionStyle);
							this._myTable.add(divisionText, 1, this.row);
							this.row++;
						}
						getStaffTable(iwc, users);
						this.row++;
						this._myTable.setHeight(1, this.row, "6");
						this.row++;
					}
				}
			}
		}
	}

	private void getUser(IWContext iwc) {
		StaffHolder holder = StaffFinder.getStaffHolder(this._userID, iwc);

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
		if (holder != null && holder.getImageID() != -1) {
			try {
				image = new Image(holder.getImageID());
				if (this._imageWidth != null) {
					image.setWidth(this._imageWidth);
				}
				if (this._imageHeight != null) {
					image.setHeight(this._imageHeight);
				}
				image.setBorder(1);
				image.setVerticalSpacing(3);
				image.setHorizontalSpacing(10);
			}
			catch (Exception e) {
				image = null;
			}

			if (image != null) {
				userTable.add(image, 1, 1);
			}
		}
		int userid = -1000;
		if (holder != null) {
			userid = holder.getUserID();
			Text name = new Text(this._iwrb.getLocalizedString("user_name", "Name") + ":");
			name.setFontStyle(this._headlineStyle);
			Text nameText = new Text(holder.getName());
			nameText.setFontStyle(this._textStyle);

			textTable.add(name, column, tableRow);
			textTable.add(nameText, column + 1, tableRow);
			tableRow++;

			int userAge = holder.getAge();
			Text age = new Text(this._iwrb.getLocalizedString("user_age", "Age") + ":");
			age.setFontStyle(this._headlineStyle);
			Text ageText = new Text(Integer.toString(userAge));
			ageText.setFontStyle(this._textStyle);

			if (this._showAge && userAge > 0) {
				textTable.add(age, column, tableRow);
				textTable.add(ageText, column + 1, tableRow);
				tableRow++;
			}

			/*Text gender = new Text(_iwrb.getLocalizedString("user_gender","Gender")+":");
			gender.setFontStyle(_headlineStyle);
			Text genderText = new Text("");
			genderText.setFontStyle(_textStyle);
			
			if ( _showGender ) {
			textTable.add(gender,column,tableRow);
			textTable.add(genderText,column+1,tableRow);
			tableRow++;
			}*/

			Text title = new Text(this._iwrb.getLocalizedString("user_title", "Title") + ":");
			title.setFontStyle(this._headlineStyle);
			Text titleText = new Text("");
			if (holder.getTitle() != null) {
				titleText.setText(holder.getTitle());
			}
			titleText.setFontStyle(this._textStyle);

			if (this._showTitle && holder.getTitle() != null) {
				textTable.add(title, column, tableRow);
				textTable.add(titleText, column + 1, tableRow);
				tableRow++;
			}

			Text workPhone = new Text(this._iwrb.getLocalizedString("work_phone", "Work phone") + ":");
			workPhone.setFontStyle(this._headlineStyle);
			Text workPhoneText = new Text("");
			if (holder.getWorkPhone() != null) {
				workPhoneText.setText(holder.getWorkPhone());
			}
			workPhoneText.setFontStyle(this._textStyle);

			if (this._showWorkPhone && holder.getWorkPhone() != null) {
				textTable.add(workPhone, column, tableRow);
				textTable.add(workPhoneText, column + 1, tableRow);
				tableRow++;
			}

			Text mobilePhone = new Text(this._iwrb.getLocalizedString("Mobile_phone", "Mobile phone") + ":");
			mobilePhone.setFontStyle(this._headlineStyle);
			Text mobilePhoneText = new Text("");
			if (holder.getMobilePhone() != null) {
				mobilePhoneText.setText(holder.getMobilePhone());
			}
			mobilePhoneText.setFontStyle(this._textStyle);

			if (this._showMobilePhone && holder.getMobilePhone() != null) {
				textTable.add(mobilePhone, column, tableRow);
				textTable.add(mobilePhoneText, column + 1, tableRow);
				tableRow++;
			}

			Text mail = new Text(this._iwrb.getLocalizedString("email", "E-mail") + ":");
			mail.setFontStyle(this._headlineStyle);
			Text mailText = new Text("");
			if (holder.getEmail() != null) {
				mailText.setText(holder.getEmail());
			}
			mailText.setFontStyle(this._textStyle);
			Link mailLink = new Link(mailText);
			if (holder.getEmail() != null) {
				mailLink.setURL("mailto:" + holder.getEmail());
			}

			if (this._showEmail && holder.getEmail() != null) {
				textTable.add(mail, column, tableRow);
				textTable.add(mailLink, column + 1, tableRow);
				tableRow++;
			}

			Text area = new Text(this._iwrb.getLocalizedString("user_area", "Area") + ":");
			area.setFontStyle(this._headlineStyle);
			Text areaText = new Text("");
			if (holder.getArea() != null) {
				areaText.setText(holder.getArea());
			}
			areaText.setFontStyle(this._textStyle);

			if (this._showArea && holder.getArea() != null) {
				textTable.add(area, column, tableRow);
				textTable.add(areaText, column + 1, tableRow);
				tableRow++;
			}

			Text beganWork = new Text(this._iwrb.getLocalizedString("user_began_work", "Began work") + ":");
			beganWork.setFontStyle(this._headlineStyle);
			Text beganWorkText = new Text("");
			if (holder.getBeganWork() != null) {
				beganWorkText.setText(holder.getBeganWork().getLocaleDate(iwc));
			}
			beganWorkText.setFontStyle(this._textStyle);

			if (this._showBeganWork && holder.getBeganWork() != null) {
				textTable.add(beganWork, column, tableRow);
				textTable.add(beganWorkText, column + 1, tableRow);
				tableRow++;
			}

			Text education = new Text(this._iwrb.getLocalizedString("user_education", "Education") + ":");
			education.setFontStyle(this._headlineStyle);
			Text educationText = new Text("");
			if (holder.getEducation() != null) {
				educationText.setText(holder.getEducation());
			}
			educationText.setFontStyle(this._textStyle);

			if (this._showEducation && holder.getEducation() != null) {
				textTable.add(education, column, tableRow);
				textTable.add(educationText, column + 1, tableRow);
				tableRow++;
			}

			if (holder.getMetaAttributes() != null && this._showMetaData) {
				String[] attributes = holder.getMetaAttributes();
				String[] values = holder.getMetaValues();
				for (int a = 0; a < attributes.length; a++) {
					Text meta = new Text(attributes[a] + ":");
					meta.setFontStyle(this._headlineStyle);
					Text metaText = new Text(values[a]);
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
			index = users.indexOf(StaffFinder.getUser(this._userID));
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
		if (this._backPage != null) {
			backLink.setPage(this._backPage);
		}
		backLink.setStyle(this._name);

		if (previousLink != null && this._showNavigationLinks) {
			linkTable.add(previousLink, 1, 1);
		}
		linkTable.add(backLink, 2, 1);
		if (nextLink != null && this._showNavigationLinks) {
			linkTable.add(nextLink, 3, 1);
			linkTable.setAlignment(3, 1, "right");
		}

		if (image != null) {
			userTable.add(textTable, 2, 1);
			userTable.setWidth(2, "100%");
			userTable.mergeCells(1, 5, 2, 5);
			userTable.add(linkTable, 1, 5);
		}
		else {
			userTable.add(textTable, 1, 1);
			userTable.add(linkTable, 1, 5);
		}

		textTable.setWidth(1, "100");
		if (this._attributesWidth != null) {
			textTable.setWidth(1, this._attributesWidth);
		}
		textTable.setColumnVerticalAlignment(1, "top");
		textTable.setColumnVerticalAlignment(2, "top");
		userTable.setColumnVerticalAlignment(1, "top");
		userTable.setColumnVerticalAlignment(2, "top");

		this._myTable.add(userTable, 1, this.row);

		if (this._isAdmin || (this._allowUserEdit && iwc.getUserId()==userid)) {
			this._myTable.add(getEditLink(this._userID), 1, this.row + 1);
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

	private Text getStaffText(String name) {
		Text text = new Text(name);
		if (this._textStyle != null) {
			text.setStyleAttribute(this._textStyle);
		}

		return text;
	}

	private Link getStaffLink(String name, int userID) {
		Link link = new Link(name);
		if (this._styles) {
			link.setStyle(this._name);
		}
		link.addParameter(StaffBusiness.PARAMETER_USER_ID, userID);
		if (this._page != null) {
			link.setPage(this._page);
		}

		return link;
	}

	private Link getEmailLink(String email) {
		Link link = null;

		if (email != null) {
			link = new Link(email);
			if (this._styles) {
				link.setStyle(this._name);
			}
			link.setURL("mailto:" + email);
		}

		return link;
	}

	private Link getEditLink(int userID) {
		Image adminImage = this._iwb.getImage("shared/edit.gif");
		Link adminLink = new Link(adminImage);
		adminLink.setWindowToOpen(StaffEditor.class);
		adminLink.addParameter(StaffBusiness.PARAMETER_USER_ID, userID);

		return adminLink;
	}

	private Link getDeleteLink(int userID) {
		Image adminImage = this._iwb.getImage("shared/delete.gif");
		Link adminLink = new Link(adminImage);
		adminLink.setWindowToOpen(StaffEditor.class);
		adminLink.addParameter(StaffBusiness.PARAMETER_USER_ID, userID);
		adminLink.addParameter(StaffBusiness.PARAMETER_MODE, StaffBusiness.PARAMETER_DELETE);

		return adminLink;
	}

	private Table getAlphabetTable() {
		String[] alphabet = { "A", "�", "B", "C", "D", "E", "�", "F", "G", "H", "I", "�", "J", "K", "L", "M", "N", "O", "�", "P", "Q", "R", "S", "T", "U", "�", "V", "W", "X", "Y", "�", "Z", "�", "�", "�", this._iwrb.getLocalizedString("all", "Allir")};
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
			}
			else {
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
		adminLink.setWindowToOpen(UserApplication.class);

		return adminLink;
	}

	private void handleParameters(IWContext iwc) {
		if (iwc.getParameter(StaffBusiness.PARAMETER_LETTER) != null) {
			this._alphabetLetter = iwc.getParameter(StaffBusiness.PARAMETER_LETTER);
			this._hasAlphabetLetter = true;
			if (this._alphabetLetter != null && this._alphabetLetter.equalsIgnoreCase(this._iwrb.getLocalizedString("all", "Allir"))) {
				this._hasAlphabetLetter = false;
			}
		}
		else {
			this._alphabetLetter = this._iwrb.getLocalizedString("all", "Allir");
			this._hasAlphabetLetter = false;
		}

		if (iwc.getParameter(StaffBusiness.PARAMETER_USER_ID) != null) {
			try {
				this._userID = Integer.parseInt(iwc.getParameter(StaffBusiness.PARAMETER_USER_ID));
				this._layout = USER;
			}
			catch (NumberFormatException e) {
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
			getParentPage().setStyleDefinition("A." + this._name, this._linkStyle);
			getParentPage().setStyleDefinition("A." + this._name + ":hover", this._hoverStyle);
			getParentPage().setStyleDefinition("A." + this._alphabetName, this._alphabetLinkStyle);
			getParentPage().setStyleDefinition("A." + this._alphabetName + ":hover", this._alphabetHoverStyle);
		}
		else {
			this._styles = false;
		}
	}

	private void setDefaultValues() {
		this._width = "100%";
		this._selectedLetterColor = "#0000CC";
		this._linkStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;text-decoration: none;";
		this._hoverStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;text-decoration: underline;";
		this._alphabetLinkStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;text-decoration: none;";
		this._alphabetHoverStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;text-decoration: underline;";
		this._textStyle = "font-family: Arial, Helvetica,sans-serif;font-size: 8pt;color: #000000;";
		this._headlineStyle = "font-family: Arial, Helvetica,sans-serif;font-weight:bold;font-size: 10pt;color: #000000;";
		this._divisionStyle = "font-family: Arial, Helvetica,sans-serif;font-weight:bold;font-size: 10pt;color: #000000;";

		this._showAge = true;
		//_showGender = true;
		this._showEducation = true;
		this._showTitle = true;
		this._showBeganWork = true;
		this._showArea = true;
		this._showImage = true;
		this._showMetaData = true;
		this._showListTitle = false;
		this._showWorkPhone = true;
		this._showListWorkPhone = false;
		this._showMobilePhone = true;
		this._showEmail = true;
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

	/*public void setShowGender(boolean showGender) {
	  _showGender = showGender;
	}*/

	public void setShowEducation(boolean showEducation) {
		this._showEducation = showEducation;
	}

	public void setShowTitle(boolean showTitle) {
		this._showTitle = showTitle;
	}

	public void setShowListTitle(boolean showTitle) {
		this._showListTitle = showTitle;
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

	public void setShowEmail(boolean showEmail) {
		this._showEmail = showEmail;
	}

	public void setImageWidth(String width) {
		this._imageWidth = width;
	}

	public void setImageHeight(String height) {
		this._imageHeight = height;
	}

	public void setNameWidth(String width) {
		this._nameWidth = width;
	}

	public void setAttributesWidth(String width) {
		this._attributesWidth = width;
	}

	public void setTextStyle(String style) {
		this._textStyle = style;
	}

	public void setHeadlineStyle(String style) {
		this._headlineStyle = style;
	}

	public void setDivisionStyle(String style) {
		this._divisionStyle = style;
	}

	public void setLinkStyle(String style, String style2, String style3, String style4) {
		this._linkStyle = style;
		this._hoverStyle = style4;
	}

	public void setLinkStyle(String style, String hoverStyle) {
		this._linkStyle = style;
		this._hoverStyle = hoverStyle;
	}

	public void setAlphabetLinkStyle(String style, String style2, String style3, String style4) {
		this._alphabetLinkStyle = style;
		this._alphabetHoverStyle = style4;
	}

	public void setAlphabetLinkStyle(String style, String hoverStyle) {
		this._alphabetLinkStyle = style;
		this._alphabetHoverStyle = hoverStyle;
	}

	public void setWidth(String width) {
		this._width = width;
	}

	public void setSelectedLetterColor(String color) {
		this._selectedLetterColor = color;
	}

	public void setStaffPage(ICPage page) {
		this._page = page;
	}

	public void setBackPage(ICPage page) {
		this._backPage = page;
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
		StaffBrowser obj = null;
		try {
			obj = (StaffBrowser) super.clone();

			if (this._myTable != null) {
				obj._myTable = (Table) this._myTable.clone();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}

	protected String getCacheState(IWContext iwc, String cacheStatePrefix) {
		String returnString = iwc.getParameter(StaffBusiness.PARAMETER_USER_ID);
		if (returnString == null) {
			returnString = "";
		}
		return cacheStatePrefix + returnString;
	}
	/**
	 * Sets the _emailWidth.
	 * @param _emailWidth The _emailWidth to set
	 */
	public void setEmailWidth(String emailWidth) {
		this._emailWidth = emailWidth;
	}

	/**
	 * Sets the _titleWidth.
	 * @param _titleWidth The _titleWidth to set
	 */
	public void setTitleWidth(String titleWidth) {
		this._titleWidth = titleWidth;
	}

	/**
	 * Sets the _workPhoneWidth.
	 * @param _workPhoneWidth The _workPhoneWidth to set
	 */
	public void setWorkPhoneWidth(String workPhoneWidth) {
		this._workPhoneWidth = workPhoneWidth;
	}

	/**
	 * Returns the showDetails.
	 * @return boolean
	 */
	public boolean isShowDetails() {
		return this._showDetails;
	}

	/**
	 * Sets the showDetails.
	 * @param showDetails The showDetails to set
	 */
	public void setShowDetails(boolean showDetails) {
		this._showDetails = showDetails;
	}
	
	public void setGroup(Group group) {
		this._group = group;
	}
	
	public void setShowDivisionHeader(boolean showHeader) {
		this._showDivisionHeader = showHeader;
	}
	
	public void setExtraColumnsAlignment(String alignment) {
		this.extraAlignment = alignment;
	}
	/**
	 * Sets the _sortAlphabetically.
	 * @param sortAlphabetically The _sortAlphabetically to set
	 */
	public void setSortAlphabetically(boolean sortAlphabetically) {
		this._sortAlphabetically = sortAlphabetically;
	}

	/**
	 * Sets the _sortGroupsAlphabetically.
	 * @param sortGroupsAlphabetically The _sortGroupsAlphabetically to set
	 */
	public void setSortGroupsAlphabetically(boolean sortGroupsAlphabetically) {
		this._sortGroupsAlphabetically = sortGroupsAlphabetically;
	}

	/**
	 * @param string
	 */
	public void setEmailAlignment(String string) {
		this._emailAlignment = string;
	}

	/**
	 * @param navigationLinks
	 */
	public void setShowNavigationLinks(boolean navigationLinks) {
		this._showNavigationLinks = navigationLinks;
	}
	/**
	 * @param sortNamesBy The sortNamesBy to set.
	 */
	public void setSortNamesBy(int sortNamesBy) {
		this._sortNamesBy = sortNamesBy;
	}
	/**
	 * @param userEdit The _allowUserEdit to set.
	 */
	public void setAllowUserEdit(boolean userEdit) {
		this._allowUserEdit = userEdit;
	}
	
	public void setShowPictureInList(boolean picture){
		this._showPictureInList = picture;
	}
}