/*
 * Created on May 18, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package com.idega.block.user.presentation;

import java.util.Locale;
import com.idega.block.user.business.UserInfoBusiness;
import com.idega.block.user.business.UserInfoBusinessBean;
import com.idega.block.user.data.UserExtraInfo;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author jonas
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class UserExtraInfoEditor extends IWAdminWindow {

	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.user";
	
	private static final String PARAM_NAME_LOCALE_ID = "loc_id";
	private static final String PARAM_NAME_USER_ID = "user_id";
	private static final String PARAM_NAME_ACTION = "action";
	private static final String PARAM_NAME_TITLE = "title";
	private static final String PARAM_NAME_EDUCATION = "education";
	private static final String PARAM_NAME_AREA = "area";
	private static final String PARAM_NAME_BEGAN_WORK = "began_work";
	private static final String PARAM_NAME_IMAGE_ID = "image_id";
	private static final String PARAM_NAME_META_VALUE = "meta_value";
	private static final String PARAM_NAME_META_ATTRIBUTE = "meta_atts";
	
	private static final String ACTION_CLOSE = "close";
	private static final String ACTION_SAVE = "save";
	private static final String ACTION_DELETE = "delete";

	private IWResourceBundle _iwrb = null;

	public void main(IWContext iwc) {
		_iwrb = getResourceBundle(iwc);
		_biz = UserInfoBusinessBean.getUserInfoBusiness(iwc);
		
		addTitle(_iwrb.getLocalizedString("staff_admin", "Staff admin"));
		Locale currentLocale = iwc.getCurrentLocale(), chosenLocale;

		String sLocaleId = iwc.getParameter(PARAM_NAME_LOCALE_ID);

		int iLocaleId = -1;
		if (sLocaleId != null) {
			iLocaleId = Integer.parseInt(sLocaleId);
			chosenLocale = ICLocaleBusiness.getLocaleReturnIcelandicLocaleIfNotFound(iLocaleId);
		} else {
			chosenLocale = currentLocale;
			iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);
		}

		processForm(iwc, iLocaleId);
	}

	private void processForm(IWContext iwc, int iLocaleId) {
		String userId = iwc.getParameter(PARAM_NAME_USER_ID);
		UserExtraInfo extraInfo = null;
		if (userId != null) {
			try {
				User user = _biz.getUser(iwc, userId);
				extraInfo = _biz.getInfo(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		String action = iwc.getParameter(PARAM_NAME_ACTION);
		if (action != null) {
			if (action.equalsIgnoreCase(ACTION_CLOSE)) {
				closeEditor(iwc);
			} else if (action.equalsIgnoreCase(ACTION_SAVE)) {
				saveEntry(iwc, iLocaleId, extraInfo);
			} else if (action.equalsIgnoreCase(ACTION_DELETE)) {
				deleteEntry(iwc);
			} else {
				System.out.println("UserExtraInfoEditor: action \"" + action + "\" unknown");
			}
		}

		DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(PARAM_NAME_LOCALE_ID);
		localeDrop.setToSubmit();
		localeDrop.setSelectedElement(Integer.toString(iLocaleId));
		addLeft(_iwrb.getLocalizedString("locale", "Locale") + ": ", localeDrop, false);
		addHiddenInput(new HiddenInput(PARAM_NAME_USER_ID, iwc.getParameter(PARAM_NAME_USER_ID)));

		initializeFields(iwc, iLocaleId, extraInfo);
	}
	
	private void initializeFields(IWContext iwc, int iLocaleId, UserExtraInfo extraInfo) {
		String strTitle = null;
		String strEducation = null;
		String strArea = null;
		IWTimestamp stamp = null;
		if(extraInfo==null) {
			System.out.println("No user extra-info found to edit");
		} else {
			strTitle = extraInfo.getTitle();
			strEducation = extraInfo.getEducation();
			strArea = extraInfo.getArea();
			stamp = new IWTimestamp(extraInfo.getBeganWork());
		}
		
		TextInput title = new TextInput(PARAM_NAME_TITLE);
		title.setLength(24);
		if (strTitle!=null) {
			title.setContent(strTitle);
		}
		addLeft(_iwrb.getLocalizedString("user_title", "Title") + ":", title, true);

		TextArea education = new TextArea(PARAM_NAME_EDUCATION, 55, 3);
		if (strEducation != null) {
			education.setContent(strEducation);
		}
		addLeft(_iwrb.getLocalizedString("user_education", "Education") + ":", education, true);

		TextArea area = new TextArea(PARAM_NAME_AREA, 55, 3);
		if (strArea != null) {
			area.setContent(strArea);
		}
		addLeft(_iwrb.getLocalizedString("user_area", "Area") + ":", area, true);

		DateInput beganWork = new DateInput(PARAM_NAME_BEGAN_WORK);
		beganWork.setYearRange(new IWTimestamp().getYear() - 60, new IWTimestamp().getYear());
		if (stamp != null) {
			beganWork.setDate(stamp.getDate());
		}
		beganWork.setStyleAttribute("style", STYLE);
		addLeft(_iwrb.getLocalizedString("user_began_work", "Began work") + ":", beganWork, true);

		/*Table metaTable = new Table(2, 6);
		metaTable.setColumnVerticalAlignment(1, "top");
		for (int a = 0; a < 6; a++) {
			TextInput attribute =
				new TextInput(StaffBusiness.PARAMETER_META_ATTRIBUTE);
			if (meta != null && meta.length >= a)
				try {
					attribute.setContent(meta[a].getAttribute());
				} catch (Exception e) {
					attribute.setContent("");
				}
			attribute.setMarkupAttribute("style", STYLE);
			attribute.setLength(20);
			metaTable.add(attribute, 1, a + 1);

			TextArea value =
				new TextArea(StaffBusiness.PARAMETER_META_VALUE, 40, 2);
			if (meta != null && meta.length >= a)
				try {
					value.setContent(meta[a].getValue());
				} catch (Exception e) {
					value.setContent("");
				}
			value.setMarkupAttribute("style", STYLE);
			metaTable.add(value, 2, a + 1);
		}
		addLeft(
			_iwrb.getLocalizedString("extra_info", "Extra info") + ":",
			metaTable,
			true,
			false);

		ImageInserter image =
			new ImageInserter(StaffBusiness.PARAMETER_IMAGE_ID);
		image.setWindowClassToOpen(
			com.idega.block.media.presentation.MediaChooserWindow.class);
		image.setHasUseBox(false);
		if (entity != null && entity.getImageID() != -1)
			image.setImageId(entity.getImageID());
		addRight(
			_iwrb.getLocalizedString("image", "Image") + ":",
			image,
			true,
			false);
*/
		addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("close", "CLOSE"), PARAM_NAME_ACTION, ACTION_CLOSE));
		addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("save", "SAVE"), PARAM_NAME_ACTION, ACTION_SAVE));
	}
	
	private void saveEntry(IWContext iwc, int localeID, UserExtraInfo extraInfo) {
		String title = iwc.getParameter(PARAM_NAME_TITLE);
		String education = iwc.getParameter(PARAM_NAME_EDUCATION);
		String area = iwc.getParameter(PARAM_NAME_AREA);
		String beganwork = iwc.getParameter(PARAM_NAME_BEGAN_WORK);
		/*IWTimestamp stamp = null;
		if (beganwork != null) {
			try {
				stamp = new IWTimestamp(beganwork);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		/*String imageID = iwc.getParameter(PARAM_NAME_IMAGE_ID);

		String[] values =
			iwc.getParameterValues(PARAM_NAME_META_VALUE);
		String[] attributes =
			iwc.getParameterValues(PARAM_NAME_META_ATTRIBUTE);*/

		if(title!=null) {
			extraInfo.setTitle(title);
		}
		if(education!=null) {
			extraInfo.setEducation(education);
		}
		if(area!=null) {
			extraInfo.setArea(area);
		}
		if(beganwork!=null) {
			//extraInfo.set
		}
		/*StaffBusiness.saveStaff(
			localeID,
			_userID,
			title,
			education,
			area,
			_stamp,
			imageID);
		StaffBusiness.saveMetaData(localeID, _userID, attributes, values);*/
	}

	private void deleteEntry(IWContext iwc) {
		//StaffBusiness.delete(_userID);
		closeEditor(iwc);
	}

	private void closeEditor(IWContext iwc) {
		setParentToReload();
		close();
	}

	private void noAccess() {
		try {
			addLeft(_iwrb.getLocalizedString("no_access", "Login first!"));
			addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
	}

	private UserInfoBusiness _biz = null;
}
