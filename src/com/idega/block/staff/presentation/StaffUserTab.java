package com.idega.block.staff.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Locale;

import com.idega.block.staff.business.StaffUserBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.user.presentation.UserConstants;
import com.idega.user.presentation.UserTab;
import com.idega.util.IWTimestamp;

/**
 * A tab for displaying and editing a user's staff information, such as title, area and education.
 * @author <a href="mailto:laddi@idega.is">Thorhallur Helgason</a>
 * @version 1.0
 */

public class StaffUserTab extends UserTab {

	protected static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.staff";
	protected static final String TAB_NAME = "staff_user_tab";
	protected static final String DEFAULT_TAB_NAME = "Staff info";
	protected static final String HELP_TEXT_KEY = "staff_user_tab";
	
	private String educationFieldName;
	private String titleFieldName;
	private String areaFieldName;
	private String beganWorkFieldName;
	
	private TextArea educationArea;
	private TextInput titleInput;
	private TextArea areaArea;
	private DateInput beganWorkInput;

	private Text educationText;
	private Text titleText;
	private Text areaText;
	private Text beganWorkText;

	public StaffUserTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	public void initializeFieldNames() {
		educationFieldName = "st_education";
		titleFieldName = "st_title";
		areaFieldName = "st_area";
		beganWorkFieldName = "st_began_work";
	}

	public void initializeFieldValues() {
		fieldValues.put(educationFieldName, "");
		fieldValues.put(titleFieldName, "");
		fieldValues.put(areaFieldName, "");
		fieldValues.put(beganWorkFieldName, null);
	}

	public void updateFieldsDisplayStatus() {
		educationArea.setContent((String) fieldValues.get(educationFieldName));
		titleInput.setContent((String) fieldValues.get(titleFieldName));
		areaArea.setContent((String) fieldValues.get(areaFieldName));
		
		Date beganWork = (Date) fieldValues.get(beganWorkFieldName);
		if (beganWork != null) {
			beganWorkInput.setDate(beganWork);
		}
	}

	public void initializeFields() {
		educationArea = new TextArea(educationFieldName);
		educationArea.setWidth(Table.HUNDRED_PERCENT);
		educationArea.setHeight("100");

		areaArea = new TextArea(areaFieldName);
		areaArea.setWidth(Table.HUNDRED_PERCENT);
		areaArea.setHeight("100");
		
		titleInput = new TextInput(titleFieldName);
		titleInput.setLength(24);
		
		IWTimestamp stamp = new IWTimestamp();
		beganWorkInput = new DateInput(beganWorkFieldName);
		beganWorkInput.setYearRange(stamp.getYear() + 1, stamp.getYear() - 75);
	}

	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		educationText = new Text(iwrb.getLocalizedString("staff_tab.education", "Education") + ":");
		titleText = new Text(iwrb.getLocalizedString("staff_tab.title", "Title") + ":");
		areaText = new Text(iwrb.getLocalizedString("staff_tab.area", "Area") + ":");
		beganWorkText = new Text(iwrb.getLocalizedString("staff_tab.began_work", "Began work") + ":");
	}

	public Help getHelpButton() {
		IWContext iwc = IWContext.getInstance();
		IWBundle iwb = getBundle(iwc);
		Help help = new Help();
		Image helpImage = iwb.getImage("help.gif");
		help.setHelpTextBundle(UserConstants.HELP_BUNDLE_IDENTFIER);
		help.setHelpTextKey(HELP_TEXT_KEY);
		help.setImage(helpImage);
		return help;
	}

	public void lineUpFields() {
		this.resize(1, 1);
		
		Table table = new Table(2, 3);
		table.setWidth("100%");
		table.setCellpadding(5);
		table.setCellspacing(0);
		table.setBorder(0);
		table.mergeCells(1, 2, 2, 2);
		table.mergeCells(1, 3, 2, 3);
		int row = 1;
		
		table.add(titleText, 1, row);
		table.add(new Break(), 1, row);
		table.add(titleInput, 1, row);
		
		table.add(beganWorkText, 2, row);
		table.add(new Break(), 2, row);
		table.add(beganWorkInput, 2, row++);
		
		table.add(educationText, 1, row);
		table.add(new Break(), 1, row);
		table.add(educationArea, 1, row++);
		
		table.add(areaText, 1, row);
		table.add(new Break(), 1, row);
		table.add(areaArea, 1, row++);
		
		add(table);
	}

	public void main(IWContext iwc) {
		getPanel().addHelpButton(getHelpButton());
	}

	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			String education = iwc.getParameter(educationFieldName);
			String title = iwc.getParameter(titleFieldName);
			String area = iwc.getParameter(areaFieldName);
			String beganWork = iwc.getParameter(beganWorkFieldName);
			
			if (education != null) {
				fieldValues.put(educationFieldName, education);
			}
			if (title != null) {
				fieldValues.put(titleFieldName, title);
			}
			if (area != null) {
				fieldValues.put(areaFieldName, area);
			}
			if (beganWork != null) {
				IWTimestamp stamp = new IWTimestamp(beganWork);
				fieldValues.put(beganWorkFieldName, stamp.getDate());
			}

			this.updateFieldsDisplayStatus();
			return true;
		}
		return false;
	}

	
	public boolean store(IWContext iwc) {
		try {
			if (getUserId() > 0) {
				User user = getUser();
				getBusiness(iwc).storeStaffUser(user, (String) fieldValues.get(educationFieldName), (String) fieldValues.get(titleFieldName), (String) fieldValues.get(areaFieldName), (Date) fieldValues.get(beganWorkFieldName), iwc.getCurrentLocale());
				return true;
			}
			return false;
		}
		catch (RemoteException re) {
			re.printStackTrace();
			return false;
		}
	}

	public void initFieldContents() {
		try {
			User user = getUser();
			IWContext iwc = IWContext.getInstance();
			Locale locale = iwc.getCurrentLocale();
			
			String education = getBusiness(iwc).getUserEducation(user, locale);
			String title = getBusiness(iwc).getUserTitle(user, locale);
			String area = getBusiness(iwc).getUserArea(user, locale);
			Date beganWork = getBusiness(iwc).getBeganWork(user);

			fieldValues.put(educationFieldName, (education != null) ? education : "");
			fieldValues.put(titleFieldName, (title != null) ? title : "");
			fieldValues.put(areaFieldName, (area != null) ? area : "");
			fieldValues.put(beganWorkFieldName, beganWork);
			updateFieldsDisplayStatus();
		}
		catch (Exception e) {
			System.err.println("GeneralUserInfoTab error initFieldContents, userId : " + getUserId());
		}
	}

	private StaffUserBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (StaffUserBusiness) IBOLookup.getServiceInstance(iwac, StaffUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}