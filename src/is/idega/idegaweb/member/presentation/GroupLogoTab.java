/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.presentation;

import com.idega.block.media.presentation.ImageInserter;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.presentation.UserGroupTab;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GroupLogoTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "usr_imag_tab_name";
	private static final String DEFAULT_TAB_NAME = "Image";
	
	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String HELP_TEXT_KEY = "group_logo_tab";
	
	/*	this is the order methods are executed
			initializeFieldNames();
			initializeFields();
			initializeTexts();
			initializeFieldValues();
			lineUpFields();
	 */
	private ImageInserter imageField;
	private String imageFieldName;
	private Text imageText;
  
	private CheckBox removeImageField;
	private String removeImageFieldName;
	private Text removeImageText;
  
	private int systemImageId = -1;

	public GroupLogoTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	public GroupLogoTab(Group group) {
		this();
		setGroupId(((Integer) group.getPrimaryKey()).intValue());
	}

	public void initializeFieldNames() {
		imageFieldName = "grp_imag_userSystemImageId";
		removeImageFieldName = "grp_imag_removeImageFieldName";
	}

	public void initializeFields() {
		imageField = new ImageInserter(imageFieldName + getGroupId());
		imageField.setHasUseBox(false);
		removeImageField = new CheckBox(removeImageFieldName);
		removeImageField.setWidth("10");
		removeImageField.setHeight("10");
	}

	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		imageText = new Text(iwrb.getLocalizedString(imageFieldName, "Image"));
		imageText.setBold();
    
		removeImageText = new Text(iwrb.getLocalizedString(removeImageFieldName, "do not show an image"));
		removeImageText.setBold();
	}

	public void initializeFieldValues() {
		systemImageId = -1;
		fieldValues.put(removeImageFieldName, new Boolean(false));
	}

	public void lineUpFields() {
		this.resize(1, 1);

		Table imageTable = new Table(1, 2);
		imageTable.setWidth(Table.HUNDRED_PERCENT);
		imageTable.setCellpadding(5);
		imageTable.setCellspacing(0);

		imageTable.add(imageText, 1, 1);
		imageTable.add(Text.getBreak(), 1, 1);
		imageTable.add(imageField, 1, 1);

		imageTable.add(removeImageField, 1, 2);
		imageTable.add(removeImageText, 1, 2);

		this.add(imageTable, 1, 1);
	}

	public void main(IWContext iwc) {
		getPanel().addHelpButton(getHelpButton());		
	}

	public void updateFieldsDisplayStatus() {
		imageField.setImageId(systemImageId);
		removeImageField.setChecked(((Boolean)fieldValues.get(removeImageFieldName)).booleanValue());
	}

	public boolean collect(IWContext iwc) {
		String imageID = iwc.getParameter(imageFieldName + getGroupId());
		if (imageID != null) {
			fieldValues.put(imageFieldName, imageID);
		}
    
		fieldValues.put(removeImageFieldName, new Boolean(iwc.isParameterSet(removeImageFieldName)));

		return true;
	}

	public boolean store(IWContext iwc) {
		try {
			if (getGroupId() > -1) {
				Group group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));

				String image = (String)fieldValues.get(imageFieldName);

				if ((image != null) && (!image.equals("-1")) && (!image.equals(""))) {
					int tempId;
					if (((Boolean) fieldValues.get(removeImageFieldName)).booleanValue())  {
						group.setMetaData("group_image","-1");
						// set variables to default values
						systemImageId = -1;
						fieldValues.put(imageFieldName, "-1");
						group.store();
						updateFieldsDisplayStatus();
					}
					else if ((tempId = Integer.parseInt(image)) != systemImageId) {
						systemImageId = tempId;
						group.setMetaData("group_image",Integer.toString(systemImageId));
						group.store();
						updateFieldsDisplayStatus();
					}

					iwc.removeSessionAttribute(imageFieldName + getGroupId());

				}

			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("update group exception");
		}
		return true;
	}

	public void initFieldContents() {
		try {
			imageField.setImSessionImageName(imageFieldName + getGroupId());

			Group group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));

			systemImageId = Integer.parseInt(group.getMetaData("group_image"));

			if (systemImageId != -1) {
				fieldValues.put(imageFieldName, Integer.toString(systemImageId));
			}
      
			fieldValues.put(removeImageFieldName, new Boolean(false));
    
			updateFieldsDisplayStatus();
		}
		catch (Exception e) {
			e.printStackTrace();
			systemImageId = -1;
			System.err.println(
				"GroupLogoTab error initFieldContents, groupId : " + getGroupId());
		}
	}
	public Help getHelpButton() {
		IWContext iwc = IWContext.getInstance();
		IWBundle iwb = getBundle(iwc);
		Help help = new Help();
		Image helpImage = iwb.getImage("help.gif");
		help.setHelpTextBundle( MEMBER_HELP_BUNDLE_IDENTIFIER);
		help.setHelpTextKey(HELP_TEXT_KEY);
		help.setImage(helpImage);
		return help;
		
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}	
}