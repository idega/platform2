package com.idega.block.calendar.presentation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import com.idega.block.calendar.business.CalendarBusiness;
import com.idega.block.calendar.business.CalendarFinder;
import com.idega.block.calendar.data.CalendarEntry;
import com.idega.block.text.business.TextFinder;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TimestampInput;
import com.idega.util.IWTimestamp;

public class CalendarEditor extends IWAdminWindow {

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.calendar";
	private boolean _isAdmin = false;
	private boolean _superAdmin = false;
	private boolean _update = false;
	private boolean _save = false;

	private int _entryID = -1;
	private int _typeID = -1;
	private int _userID = -1;
	private int _groupID = -1;
	private int _instanceID = -1;
	private IWTimestamp _stamp;
	private IWTimestamp _endStamp;

	private IWBundle _iwb;
	private IWResourceBundle _iwrb;

	public CalendarEditor() {
		setWidth(550);
		setHeight(420);
		setUnMerged();
	}

	public void main(IWContext iwc) throws Exception {
		/**
		 * @todo permission
		 */
		_isAdmin = true;
		_superAdmin = iwc.hasEditPermission(this);
		_iwb = getBundle(iwc);
		_iwrb = getResourceBundle(iwc);
		addTitle(_iwrb.getLocalizedString("calendar_admin", "Calendar Admin"));
		Locale currentLocale = iwc.getCurrentLocale(), chosenLocale;

		try {
			_userID = LoginBusinessBean.getUser(iwc).getID();
		} catch (Exception e) {
			_userID = -1;
		}
		try {
			_groupID = LoginBusinessBean.getUser(iwc).getPrimaryGroupID();
		} catch (Exception e) {
			_groupID = -1;
		}

		String sLocaleId = iwc.getParameter(CalendarParameters.PARAMETER_LOCALE_DROP);
		int iCategoryId = iwc.isParameterSet(CalendarParameters.PARAMETER_IC_CAT) ? Integer.parseInt(iwc.getParameter(CalendarParameters.PARAMETER_IC_CAT)) : -1;

		int iLocaleId = -1;
		if (sLocaleId != null) {
			iLocaleId = Integer.parseInt(sLocaleId);
			chosenLocale = ICLocaleBusiness.getLocaleReturnIcelandicLocaleIfNotFound(iLocaleId);
		} else {
			chosenLocale = currentLocale;
			iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);
		}

		if (_isAdmin) {
			processForm(iwc, iLocaleId, iCategoryId);
		} else {
			noAccess();
		}
	}

	private void processForm(IWContext iwc, int iLocaleId, int iCategoryId) {
		if (iwc.getParameter(CalendarParameters.PARAMETER_INSTANCE_ID) != null) {
			try {
				_instanceID = Integer.parseInt(iwc.getParameter(CalendarParameters.PARAMETER_INSTANCE_ID));
			} catch (NumberFormatException e) {
				_instanceID = -1;
			}
		}

		if (iwc.getParameter(CalendarParameters.PARAMETER_ENTRY_ID) != null) {
			try {
				_entryID = Integer.parseInt(iwc.getParameter(CalendarParameters.PARAMETER_ENTRY_ID));
			} catch (NumberFormatException e) {
				_entryID = -1;
			}
		}

		if (iwc.getParameter(CalendarParameters.PARAMETER_TYPE_ID) != null) {
			try {
				_typeID = Integer.parseInt(iwc.getParameter(CalendarParameters.PARAMETER_TYPE_ID));
			} catch (NumberFormatException e) {
				_typeID = -1;
			}
		}

		if (iwc.getParameter(CalendarParameters.PARAMETER_ENTRY_DATE) != null) {
			try {
				_stamp = new IWTimestamp(iwc.getParameter(CalendarParameters.PARAMETER_ENTRY_DATE));
			} catch (Exception e) {
				_stamp = new IWTimestamp();
			}
		} else {
			_stamp = new IWTimestamp();
		}

		if (iwc.getParameter(CalendarParameters.PARAMETER_ENTRY_END_DATE) != null) {
			try {
				_endStamp = new IWTimestamp(iwc.getParameter(CalendarParameters.PARAMETER_ENTRY_END_DATE));
			} catch (Exception e) {
			}
		}
		
		if (iwc.getParameter(CalendarParameters.PARAMETER_MODE) != null) {
			if (iwc.getParameter(CalendarParameters.PARAMETER_MODE).equalsIgnoreCase(CalendarParameters.PARAMETER_MODE_CLOSE)) {
				closeEditor(iwc);
			} else
				if (iwc.getParameter(CalendarParameters.PARAMETER_MODE).equalsIgnoreCase(CalendarParameters.PARAMETER_MODE_SAVE)) {
					saveEntry(iwc, iLocaleId, iCategoryId);
				}
		}

		if (_entryID == -1 && iwc.getSessionAttribute(CalendarParameters.PARAMETER_ENTRY_ID) != null) {
			try {
				_entryID = Integer.parseInt((String) iwc.getSessionAttribute(CalendarParameters.PARAMETER_ENTRY_ID));
			} catch (NumberFormatException e) {
				_entryID = -1;
			}
			iwc.removeSessionAttribute(CalendarParameters.PARAMETER_ENTRY_ID);
		}

		if (_entryID != -1) {
			if (iwc.getParameter(CalendarParameters.PARAMETER_MODE_DELETE) != null) {
				deleteEntry(iwc);
			} else {
				_update = true;
			}
		}

		DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(CalendarParameters.PARAMETER_LOCALE_DROP);
		localeDrop.setToSubmit();
		localeDrop.setSelectedElement(Integer.toString(iLocaleId));
		addLeft(_iwrb.getLocalizedString("locale", "Locale") + ": ", localeDrop, false);
		addHiddenInput(new HiddenInput(CalendarParameters.PARAMETER_ENTRY_ID, Integer.toString(_entryID)));

		initializeFields(iwc, iLocaleId, iCategoryId);
	}

	private void initializeFields(IWContext iwc, int iLocaleId, int iCategoryId) {
		CalendarEntry entry = null;
		if (_update)
			entry = CalendarFinder.getInstance().getEntry(_entryID);

		String[] locTexts = null;
		if (entry != null)
			locTexts = TextFinder.getLocalizedString(entry, iLocaleId);
		if (entry != null) {
			_stamp = new IWTimestamp(entry.getDate());
			if (entry.getEndDate() != null)
				_endStamp = new IWTimestamp(entry.getEndDate());
		}

		DropdownMenu categories = CalendarBusiness.getCategories(CalendarParameters.PARAMETER_IC_CAT, iwc.getCurrentLocale(), _instanceID);
		if (entry != null)
			categories.setSelectedElement(Integer.toString(entry.getCategoryId()));
		addLeft(_iwrb.getLocalizedString("category", "Category") + ":", categories, false);
		
		DropdownMenu entryTypes = CalendarBusiness.getEntryTypes(CalendarParameters.PARAMETER_TYPE_ID, iLocaleId);
		if (entry != null)
			entryTypes.setSelectedElement(Integer.toString(entry.getEntryTypeID()));
		addLeft(_iwrb.getLocalizedString("type", "Type") + ":", entryTypes, false);

		TextInput entryHeadline = new TextInput(CalendarParameters.PARAMETER_ENTRY_HEADLINE);
		entryHeadline.setLength(24);
		if (locTexts != null && locTexts[0] != null) {
			entryHeadline.setContent(locTexts[0]);
		}
		addLeft(_iwrb.getLocalizedString("headline", "Headline") + ":", entryHeadline, true);

		TextArea entryBody = new TextArea(CalendarParameters.PARAMETER_ENTRY_BODY);
		if (locTexts != null && locTexts[1] != null) {
			entryBody.setContent(locTexts[1]);
		}
		entryBody.setWidth("100%");
		entryBody.setRows(7);
		addLeft(_iwrb.getLocalizedString("body", "Body") + ":", entryBody, true);

		IWTimestamp stamp = new IWTimestamp();
		TimestampInput entryDate = new TimestampInput(CalendarParameters.PARAMETER_ENTRY_DATE);
		entryDate.setYearRange(stamp.getYear() - 5, stamp.getYear() + 10);
		if (_stamp == null) {
			_stamp = IWTimestamp.RightNow();
		}
		entryDate.setTimestamp(_stamp.getTimestamp());
		entryDate.setStyleAttribute(STYLE);

		TimestampInput entryEndDate = new TimestampInput(CalendarParameters.PARAMETER_ENTRY_END_DATE);
		entryEndDate.setYearRange(stamp.getYear() - 5, stamp.getYear() + 10);
		if (_endStamp != null)
			entryEndDate.setTimestamp(_endStamp.getTimestamp());
		entryEndDate.setStyleAttribute(STYLE);

		addLeft(_iwrb.getLocalizedString("date_start", "Start date") + ":", entryDate, true);
		addLeft(_iwrb.getLocalizedString("date_end", "End date") + ":", entryEndDate, true);
		addHiddenInput(new HiddenInput(CalendarParameters.PARAMETER_IC_CAT, String.valueOf(iCategoryId)));
		addHiddenInput(new HiddenInput(CalendarParameters.PARAMETER_INSTANCE_ID, String.valueOf(_instanceID)));
		addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("close", "CLOSE"), CalendarParameters.PARAMETER_MODE, CalendarParameters.PARAMETER_MODE_CLOSE));
		addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("save", "SAVE"), CalendarParameters.PARAMETER_MODE, CalendarParameters.PARAMETER_MODE_SAVE));
	}

	private void saveEntry(IWContext iwc, int localeID, int categoryId) {
		String entryHeadline = iwc.getParameter(CalendarParameters.PARAMETER_ENTRY_HEADLINE);
		String entryBody = iwc.getParameter(CalendarParameters.PARAMETER_ENTRY_BODY);
		String entryDate = iwc.getParameter(CalendarParameters.PARAMETER_ENTRY_DATE);
		String entryEndDate = iwc.getParameter(CalendarParameters.PARAMETER_ENTRY_END_DATE);
		String entryType = iwc.getParameter(CalendarParameters.PARAMETER_TYPE_ID);

		int entryID = CalendarBusiness.saveEntry(_entryID, _userID, _groupID, localeID, categoryId, entryHeadline, entryBody, entryDate, entryEndDate, entryType);
		iwc.setSessionAttribute(CalendarParameters.PARAMETER_ENTRY_ID, Integer.toString(entryID));
	}

	private void deleteEntry(IWContext iwc) {
		CalendarBusiness.deleteEntry(_entryID);
		closeEditor(iwc);
	}

	private void closeEditor(IWContext iwc) {
		setParentToReload();
		close();
	}

	private void noAccess() throws IOException, SQLException {
		addLeft(_iwrb.getLocalizedString("no_access", "Login first!"));
		addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}
