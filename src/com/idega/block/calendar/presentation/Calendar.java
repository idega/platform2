package com.idega.block.calendar.presentation;

/**
 * Title: Calendar
 * Description: idegaWeb Calendar (Block)
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Laddi
 * @version 1.0
 */

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.idegaweb.presentation.*;
import com.idega.block.calendar.business.CalendarBusiness;
import com.idega.block.calendar.business.CalendarFinder;
import com.idega.block.calendar.data.CalendarCategory;
import com.idega.block.calendar.data.CalendarEntry;
import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.presentation.CategoryBlock;
import com.idega.core.builder.data.ICPage;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;

public class Calendar extends CategoryBlock implements Builderaware {

	private int _timeStyle = IWTimestamp.SHORT;
	private int _dateStyle = IWTimestamp.SHORT;
	private boolean hasEdit = false, hasAdd = false, hasPref = false;
	private int _iLocaleID;
	private int _view = CalendarParameters.MONTH;

	private IWTimestamp _stamp;
	private String _width = "100%";
	private boolean _isSelectedDay = false;
	private Integer _daysAhead = null; //= 7;
	private Integer _daysBack = null; //= 7;
	private int _numberOfShown = 4;

	private String _bodyColor = "#000000";
	private String _headlineColor = "#000000";
	private String _dateColor = "#000000";

	private String _noActionDay = "#999966";
	private String _actionDay = "#660000";
	private ICPage _page;
	private boolean _showMonth = false;
	private boolean _showMonthButton = false;
	private boolean _asLineView = false;

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.calendar";
	protected IWResourceBundle _iwrb;
	protected IWBundle _iwb;
	protected IWBundle _iwbCalendar;

	private String AddPermission = "add";
	private String PrePermission = "pref";
	private int iUserId = -1;
	
	private static final String HEADLINE_STYLE_NAME = "headline";
	private static final String BODY_STYLE_NAME = "body";
	private static final String DATE_STYLE_NAME = "date";
	private String CATEGORY_STYLE_NAME = "category";
	private String HEADLINE_STYLE = "font-family: Arial,Helvetica,sans-serif; font-size: 11px; font-weight: bold;";
	private String BODY_STYLE = "font-family: Arial,Helvetica,sans-serif; font-size: 10px;";
	private String DATE_STYLE = "font-family: Arial,Helvetica,sans-serif; font-size: 10px; font-weight: bold;";
	private String CATEGORY_STYLE = "font-family: Arial,Helvetica,sans-serif; font-size: 10px; font-weight: bold; color: #666666;";

	private int _spaceBetween = 16;
	private boolean _showCategory;

	public Calendar() {
		//_stamp = IWTimestamp.getTimestampRightNow();
	}

	public Calendar(IWTimestamp timestamp) {
		_stamp = timestamp;
	}

	public void registerPermissionKeys() {
		registerPermissionKey(AddPermission);
		registerPermissionKey(PrePermission);
	}

	public String getCategoryType() {
		return ((com.idega.block.calendar.data.CalendarCategoryHome) com.idega.data.IDOLookup.getHomeLegacy(CalendarCategory.class)).createLegacy().getCategoryType();
	}

	public boolean getMultible() {
		return true;
	}

	public void _main(IWContext iwc) throws Exception {
		super._main(iwc);
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		_iwrb = getResourceBundle(iwc);
		_iwb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		_iwbCalendar = getBundle(iwc);

		iUserId = iwc.getUserId();

		hasEdit = iwc.hasEditPermission(this);
		hasAdd = iwc.hasPermission(AddPermission, this);
		hasPref = iwc.hasPermission(PrePermission, this);

		_iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());

		if (iwc.getParameter(CalendarParameters.PARAMETER_VIEW) != null) {
			_view = Integer.parseInt(iwc.getParameter(CalendarParameters.PARAMETER_VIEW));
		}

		if (_stamp == null) {
			String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
			String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
			String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);
			_stamp = CalendarBusiness.getTimestamp(day, month, year);
		}

		_isSelectedDay = CalendarBusiness.getIsSelectedDay(iwc);

		switch (_view) {
			case CalendarParameters.DAY :
				drawDay(iwc);
				break;
			case CalendarParameters.MONTH :
				drawMonth(iwc);
				break;
			case CalendarParameters.YEAR :
				drawYear(iwc);
				break;
			case CalendarParameters.AHEAD_VIEW :
				drawAheadView(iwc);
				break;
		}
	}

	private void getParameter(IWContext iwc) {
		String string = iwc.getParameter(CalendarParameters.PARAMETER_SHOW_CALENDAR);
		if (string != null && string.equalsIgnoreCase(CalendarParameters.PARAMETER_TRUE)) {
			_showMonth = true;
			iwc.setSessionAttribute(CalendarParameters.PARAMETER_SHOW_CALENDAR, CalendarParameters.PARAMETER_TRUE);
		} else
			if (string != null && string.equalsIgnoreCase(CalendarParameters.PARAMETER_FALSE)) {
				_showMonth = false;
				iwc.removeSessionAttribute(CalendarParameters.PARAMETER_SHOW_CALENDAR);
			} else
				if (string == null) {
					if (iwc.getSessionAttribute(CalendarParameters.PARAMETER_SHOW_CALENDAR) != null)
						_showMonth = true;
				}
	}

	private void drawDay(IWContext iwc) {
		getParameter(iwc);

		Table outerTable = new Table();
		outerTable.setCellpaddingAndCellspacing(0);
		if (_width != null)
			outerTable.setWidth(_width);
		outerTable.setWidth(1, Table.HUNDRED_PERCENT);

		Table entriesTable = new Table();
		entriesTable.setWidth(Table.HUNDRED_PERCENT);
		outerTable.add(entriesTable, 1, 1);

		String[] localeStrings = null;
		Text headlineText = null;
		Text bodyText = null;
		IWTimestamp stamp = null;
		boolean hasImage = true;
		int imageID;
		int ypos = 1;

		///// Permisson Area ////////////
		boolean buttonsAdded = false;
		if (hasAdd || hasEdit) {
			entriesTable.add(getAddIcon(), 1, ypos);
			buttonsAdded = true;
		}
		if (hasPref || hasEdit) {
			entriesTable.add(getPropertiesIcon(), 1, ypos);
			buttonsAdded = true;
		}
		if (hasEdit) {
			entriesTable.add(getCategoryIcon(), 1, ypos);
			buttonsAdded = true;
		}
		if (buttonsAdded)
			ypos++;
		/////////////////////////////////

		int numberOfShown = 0;

		List entries = null;
		if (_isSelectedDay) {

			entries = CalendarFinder.getInstance().listOfEntries(_stamp, getCategoryIds());
			if (entries != null)
				numberOfShown = entries.size();
		} else {
			if (_daysAhead != null || _daysBack != null)
				entries = CalendarFinder.getInstance().listOfWeekEntries(_stamp, _daysAhead.intValue(), _daysBack.intValue(), getCategoryIds());
			else
				entries = CalendarFinder.getInstance().listOfNextEntries(getCategoryIds());
			if (entries != null) {
				if (entries.size() > _numberOfShown)
					numberOfShown = _numberOfShown;
				else
					numberOfShown = entries.size();
			}
		}

		if (entries != null) {
			CalendarEntry entry;
			for (int a = 0; a < numberOfShown; a++) {
				Image typeImage = null;
				entry = (CalendarEntry) entries.get(a);
				localeStrings = CalendarFinder.getInstance().getEntryStrings(entry, _iLocaleID);
				imageID = CalendarFinder.getInstance().getImageID(entry.getEntryTypeID());

				if (imageID != -1) {
					try {
						typeImage = new Image(imageID);
						typeImage.setHorizontalSpacing(3);
					} catch (Exception e) {
						typeImage = null;
					}
				}
				if (typeImage == null) {
					typeImage = _iwbCalendar.getImage("shared/day_dot.gif");
				}

				if (localeStrings != null) {
					if (localeStrings[0] != null)
						headlineText = new Text(localeStrings[0]);
					else
						headlineText = null;

					if (localeStrings[1] != null)
						bodyText = new Text(localeStrings[1]);
					else
						bodyText = null;
				}

				int xpos = 1;

				if (headlineText != null) {
					if (typeImage != null) {
						typeImage.setName(CalendarFinder.getInstance().getEntryTypeName(entry.getEntryTypeID(), _iLocaleID));
						entriesTable.add(typeImage, xpos, ypos);
						entriesTable.setWidth(xpos, ypos, "1");
						hasImage = true;
						xpos++;
					}

					headlineText.setFontStyle("font-family: Arial,Helvetica,sans-serif; font-size: 11px; font-weight: bold; color: " + _headlineColor + ";");
					entriesTable.add(headlineText, xpos, ypos);

					stamp = new IWTimestamp(entry.getDate());
					DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, iwc.getCurrentLocale());
					Date date = new Date(stamp.getTimestamp().getTime());
					Text dateText = new Text(format.format(date));
					dateText.setFontStyle("font-family: Arial,Helvetica,sans-serif; font-size: 10px; color: " + _dateColor + ";");

					xpos++;
					entriesTable.setAlignment(xpos, ypos, "right");
					entriesTable.add(dateText, xpos, ypos);

					// Checking permissions
					if (hasEdit || hasPref || this.iUserId == entry.getUserID()) {
						xpos++;
						entriesTable.add(getEditButtons(entry.getID()), xpos, ypos);
					}

					if (bodyText != null && bodyText.getText().length() > 0) {
						ypos++;
						bodyText.setFontStyle("font-family: Arial,Helvetica,sans-serif; font-size: 11px; color: " + _bodyColor + ";");
						if (hasImage) {
							entriesTable.mergeCells(2, ypos, entriesTable.getColumns(), ypos);
							entriesTable.add(bodyText, 2, ypos);
						} else {
							entriesTable.mergeCells(1, ypos, entriesTable.getColumns(), ypos);
							entriesTable.add(bodyText, 1, ypos);
						}
					}

					ypos++;
				}
			}
		} else {
			headlineText = new Text(_iwrb.getLocalizedString("no_entries", "No entries in calendar"));
			headlineText.setFontStyle("font-family: Verdana,Arial,Helvetica,sans-serif; font-size: 11px; font-weight: bold; color: " + _headlineColor + ";");
			entriesTable.add(headlineText, 1, 2);
			ypos++;
		}
		if (buttonsAdded) {
			entriesTable.mergeCells(1, 1, entriesTable.getColumns(), 1);
		}

		if (_showMonth) {
			SmallCalendar cal = getCalendar(_stamp);
			cal.setICObjectInstanceID(this.getICObjectInstanceID());
			cal.setWidth(100);
			outerTable.setWidth(2, 1, "6");
			outerTable.setAlignment(3, 1, Table.HORIZONTAL_ALIGN_RIGHT);
			outerTable.add(cal, 3, 1);
		}

		if (_showMonthButton) {
			outerTable.mergeCells(1, 2, outerTable.getColumns(), 2);
			outerTable.setAlignment(1, 2, Table.HORIZONTAL_ALIGN_RIGHT);
			if (_showMonth) {
				Link link = new Link(_iwrb.getLocalizedImageButton("hide_month", "Hide month"));
				link.addParameter(CalendarParameters.PARAMETER_SHOW_CALENDAR, CalendarParameters.PARAMETER_FALSE);
				outerTable.add(link, 1, 2);
			} else {
				Link link = new Link(_iwrb.getLocalizedImageButton("show_month", "Show month"));
				link.addParameter(CalendarParameters.PARAMETER_SHOW_CALENDAR, CalendarParameters.PARAMETER_TRUE);
				outerTable.add(link, 1, 2);
			}
		}
		outerTable.setColumnVerticalAlignment(1, Table.VERTICAL_ALIGN_TOP);

		add(outerTable);
	}

	private void drawAheadView(IWContext iwc) {
		Table outerTable = new Table();
		outerTable.setCellpaddingAndCellspacing(0);
		if (_width != null)
			outerTable.setWidth(_width);
		outerTable.setWidth(1, Table.HUNDRED_PERCENT);
		
		Table dateTable;

		Table entriesTable;

		String[] localeStrings = null;
		Text headlineText = null;
		Text bodyText = null;
		IWTimestamp stamp = null;
		int ypos = 1;

		///// Permisson Area ////////////
		boolean buttonsAdded = false;
		if (hasAdd || hasEdit) {
			outerTable.add(getAddIcon(), 1, ypos);
			buttonsAdded = true;
		}
		if (hasPref || hasEdit) {
			outerTable.add(getPropertiesIcon(), 1, ypos);
			buttonsAdded = true;
		}
		if (hasEdit) {
			outerTable.add(getCategoryIcon(), 1, ypos);
			buttonsAdded = true;
		}
		if (buttonsAdded) {
			ypos++;
			outerTable.setHeight(ypos++,3);
		}
		/////////////////////////////////

		int numberOfShown = 0;

		List entries = CalendarFinder.getInstance().listOfNextEntries(getCategoryIds());

		if (entries != null) {
			if (entries.size() > _numberOfShown)
				numberOfShown = _numberOfShown;
			else
				numberOfShown = entries.size();

			CalendarEntry entry;
			for (int a = 0; a < numberOfShown; a++) {
				entriesTable = new Table();
			  entriesTable.setWidth(Table.HUNDRED_PERCENT);
			  entriesTable.setCellpaddingAndCellspacing(0);
			  outerTable.add(entriesTable, 1, ypos++);
			  
			  dateTable = new Table(2,1);
			  dateTable.setWidth(Table.HUNDRED_PERCENT);
			  dateTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
			  dateTable.setCellpaddingAndCellspacing(0);
			  
			  if ((a + 1) < numberOfShown && _spaceBetween > 0) {
			  	outerTable.setHeight(ypos++, _spaceBetween);
			  }
	
				entry = (CalendarEntry) entries.get(a);
				localeStrings = CalendarFinder.getInstance().getEntryStrings(entry, _iLocaleID);

				if (localeStrings != null) {
					if (localeStrings[0] != null)
						headlineText = getStyleText(localeStrings[0],HEADLINE_STYLE_NAME);
					else
						headlineText = null;

					if (localeStrings[1] != null)
						bodyText = getStyleText(localeStrings[1],BODY_STYLE_NAME);
					else
						bodyText = null;
				}
				int row = 1;
				if (headlineText != null) {
					stamp = new IWTimestamp(entry.getDate());
					Text dateText = getStyleText(stamp.getLocaleDateAndTime(iwc.getCurrentLocale(), _dateStyle, _timeStyle), DATE_STYLE_NAME);
					Text category = getStyleText(CategoryFinder.getInstance().getCategory(entry.getCategoryId()).getName(iwc.getCurrentLocale()), CATEGORY_STYLE_NAME);
					dateTable.add(dateText, 1, 1);
					if (_showCategory)
						dateTable.add(category, 2, 1);
					entriesTable.add(dateTable, 1, row++);
					entriesTable.add(headlineText, 1, row++);

					if (bodyText != null && bodyText.getText().length() > 0) {
						entriesTable.add(bodyText, 1, row++);
					}
					if (hasEdit || hasPref || this.iUserId == entry.getUserID()) {
						entriesTable.setHeight(row++,3);
						entriesTable.add(getEditButtons(entry.getID()), 1, row);
					}
				}
			}
		}
		else {
			headlineText = new Text(_iwrb.getLocalizedString("no_entries", "No entries in calendar"));
			headlineText.setFontStyle("font-family: Verdana,Arial,Helvetica,sans-serif; font-size: 11px; font-weight: bold; color: " + _headlineColor + ";");
			outerTable.add(headlineText, 1, ypos);
		}

		add(outerTable);
	}

	private void drawMonth(IWContext iwc) {
		SmallCalendar cal = getCalendar(_stamp);
		cal.setAsLineView(_asLineView);
		cal.setICObjectInstanceID(this.getICObjectInstanceID());

		if (_width != null) {
			try {
				cal.setWidth(Integer.parseInt(_width));
			} catch (NumberFormatException e) {
				cal.setWidth(110);
			}
		}

		Table monthTable = new Table();
		monthTable.setCellpadding(0);
		monthTable.setCellspacing(0);
		int ypos = 1;

		///// Permisson Area ////////////
		boolean buttonsAdded = false;
		if (hasAdd || hasEdit) {
			monthTable.add(getAddIcon(), 1, ypos);
			buttonsAdded = true;
		}
		if (hasPref || hasEdit) {
			monthTable.add(getPropertiesIcon(), 1, ypos);
			buttonsAdded = true;
		}
		if (hasEdit) {
			monthTable.add(getCategoryIcon(), 1, ypos);
			buttonsAdded = true;
		}
		if (buttonsAdded)
			ypos++;
		/////////////////////////////////

		monthTable.add(cal, 1, ypos);
		add(monthTable);
	}

	private SmallCalendar getCalendar(IWTimestamp stamp) {
		List list = CalendarFinder.getInstance().getMonthEntries(stamp, getCategoryIds());

		SmallCalendar calendar = new SmallCalendar(stamp);
		calendar.setDaysAsLink(true);
		calendar.setDayTextColor(_noActionDay);
		if (_page != null) {
			calendar.setPage(_page);
		}

		if (list != null) {
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				calendar.setDayFontColor(new IWTimestamp(((CalendarEntry) iter.next()).getDate()), _actionDay);
			}
		}

		return calendar;
	}

	private void drawYear(IWContext iwc) {
		Table yearTable = new Table();
		IWTimestamp yearStamp = null;
		SmallCalendar calendar = null;
		int ypos = 1;
		int xpos = 1;

		///// Permisson Area ////////////
		boolean buttonsAdded = false;
		if (hasEdit || hasAdd) {
			yearTable.add(getAddIcon(), 1, ypos);
			buttonsAdded = true;
		}
		if (hasEdit || hasPref) {
			yearTable.add(getPropertiesIcon(), 1, ypos);
			buttonsAdded = true;
		}
		if (hasEdit) {
			yearTable.add(getCategoryIcon(), 1, ypos);
			buttonsAdded = true;
		}
		if (buttonsAdded)
			ypos++;
		/////////////////////////////////

		for (int a = 1; a <= 12; a++) {
			yearStamp = new IWTimestamp(_stamp.getDay(), a, _stamp.getYear());
			calendar = getCalendar(yearStamp);
			calendar.setICObjectInstanceID(this.getICObjectInstanceID());
			calendar.setOnlySelectedHighlighted(true);
			calendar.useNextAndPreviousLinks(false);
			calendar.setAsLineView(_asLineView);

			yearTable.add(calendar, xpos, ypos);
			yearTable.setRowVerticalAlignment(ypos, "top");

			if (!_asLineView) {
				xpos = xpos % 3 + 1;
				if (xpos == 1)
					ypos++;
			} else {
				ypos++;
			}
		}

		add(yearTable);
	}

	private Link getAddIcon() {
		Image image = _iwb.getImage("shared/create.gif");
		Link link = new Link(image);
		link.setWindowToOpen(CalendarEditor.class);
		link.addParameter(CalendarParameters.PARAMETER_IC_CAT, getCategoryId());
		link.addParameter(CalendarParameters.PARAMETER_INSTANCE_ID, getICObjectInstanceID());
		if (this._isSelectedDay)
			link.addParameter(CalendarParameters.PARAMETER_ENTRY_DATE, _stamp.toSQLString());
		return link;
	}

	private Link getPropertiesIcon() {
		Image image = _iwb.getImage("shared/edit.gif", "Types");
		Link link = new Link(image);
		link.setWindowToOpen(CalendarTypeEditor.class);
		return link;
	}

	private Link getCategoryIcon() {
		Image image = _iwb.getImage("shared/edit.gif", "Categories");
		Link link = getCategoryLink(((com.idega.block.calendar.data.CalendarCategoryHome) com.idega.data.IDOLookup.getHomeLegacy(CalendarCategory.class)).createLegacy().getCategoryType());
		link.setImage(image);
		// link.setWindowToOpen(CalendarTypeEditor.class);
		return link;
	}

	private Table getEditButtons(int entryID) {
		Table table = new Table(2, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);

		Image editImage = _iwb.getImage("shared/edit.gif");
		Image deleteImage = _iwb.getImage("shared/delete.gif");

		Link editLink = new Link(editImage);
		editLink.setWindowToOpen(CalendarEditor.class);
		editLink.addParameter(CalendarParameters.PARAMETER_ENTRY_ID, entryID);
		editLink.addParameter(CalendarParameters.PARAMETER_MODE, CalendarParameters.PARAMETER_MODE_EDIT);
		editLink.addParameter(CalendarParameters.PARAMETER_IC_CAT, getCategoryId());
		editLink.addParameter(CalendarParameters.PARAMETER_INSTANCE_ID, getICObjectInstanceID());
		table.add(editLink, 1, 1);
		Link deleteLink = new Link(deleteImage);
		deleteLink.setWindowToOpen(ConfirmDeleteWindow.class);
		deleteLink.addParameter(ConfirmDeleteWindow.PRM_DELETE_ID, entryID);
		deleteLink.addParameter(ConfirmDeleteWindow.PRM_DELETE, CalendarParameters.PARAMETER_TRUE);
		table.add(deleteLink, 2, 1);

		return table;
	}

	public void setView(int view) {
		_view = view;
	}

	public void setWidth(int width) {
		_width = Integer.toString(width);
	}

	public void setWidth(String width) {
		_width = width;
	}

	public void setNumberOfShown(int numberOfShown) {
		_numberOfShown = numberOfShown;
	}

	public void setDaysAhead(int daysAhead) {
		_daysAhead = new Integer(daysAhead);
	}

	public void setDaysBack(int daysBack) {
		_daysBack = new Integer(daysBack);
	}

	public void setHeadlineColor(String headlineColor) {
		_headlineColor = headlineColor;
	}

	public void setBodyColor(String bodyColor) {
		_bodyColor = bodyColor;
	}

	public void setDateColor(String dateColor) {
		_dateColor = dateColor;
	}

	public void setInActiveDayColor(String color) {
		_noActionDay = color;
	}

	public void setActiveDayColor(String color) {
		_actionDay = color;
	}

	public void setAsLineView(boolean line) {
		_asLineView = line;
	}

	public void setDate(int year, int month, int day) {
		if (_stamp == null) {
			_stamp = new IWTimestamp(day, month, year);
		} else {
			_stamp.setDay(day);
			_stamp.setMonth(month);
			_stamp.setYear(year);
		}
	}

	public void setPage(ICPage page) {
		_page = page;
	}

	public void setShowMonthButton(boolean showButton) {
		_showMonthButton = showButton;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public boolean deleteBlock(int ICObjectInstanceID) {
		return CalendarBusiness.deleteBlock(getICObjectInstanceID());
	}

	/**
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
	public Map getStyleNames() {
		HashMap map = new HashMap();
		String[] styleNames = { HEADLINE_STYLE_NAME, BODY_STYLE_NAME, DATE_STYLE_NAME, CATEGORY_STYLE_NAME };
		String[] styleValues = { HEADLINE_STYLE, BODY_STYLE, DATE_STYLE, CATEGORY_STYLE };

		for (int a = 0; a < styleNames.length; a++) {
			map.put(styleNames[a], styleValues[a]);
		}

		return map;
	}
	/**
	 * Sets the spaceBetween.
	 * @param spaceBetween The spaceBetween to set
	 */
	public void setSpaceBetween(int spaceBetween) {
		_spaceBetween = spaceBetween;
	}

	/**
	 * Sets the showCategory.
	 * @param showCategory The showCategory to set
	 */
	public void setShowCategory(boolean showCategory) {
		_showCategory = showCategory;
	}

	/**
	 * Sets the dateStyle.
	 * @param dateStyle The dateStyle to set
	 */
	public void setDateStyle(int dateStyle) {
		_dateStyle = dateStyle;
	}

	/**
	 * Sets the timeStyle.
	 * @param timeStyle The timeStyle to set
	 */
	public void setTimeStyle(int timeStyle) {
		_timeStyle = timeStyle;
	}

}