package com.idega.block.calendar.business;

/**
 * Title: CalendarBusiness
 * Description: Business class for Calendar Block
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Laddi
 * @version 1.0
 */

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.transaction.TransactionManager;

import com.idega.block.calendar.data.CalendarEntry;
import com.idega.block.calendar.data.CalendarEntryType;
import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.business.CategoryBusiness;
import com.idega.block.category.data.ICCategory;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.LocalizedText;
import com.idega.data.EntityFinder;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.util.IWTimestamp;

public class CalendarBusiness {

	public static IWTimestamp getTimestamp(String day, String month, String year) {
		IWTimestamp stamp = new IWTimestamp();

		if (day != null) {
			stamp.setDay(Integer.parseInt(day));
		}
		// removed dubius behavior A
		/*else {
			stamp.setDay(1);
		}
		*/
		if (month != null) {
			stamp.setMonth(Integer.parseInt(month));
		}
		if (year != null) {
			stamp.setYear(Integer.parseInt(year));
		}

		stamp.setHour(0);
		stamp.setMinute(0);
		stamp.setSecond(0);

		return stamp;
	}

	public static boolean getIsSelectedDay(IWContext iwc) {
		String day = iwc.getParameter(CalendarParameters.PARAMETER_DAY);
		String month = iwc.getParameter(CalendarParameters.PARAMETER_MONTH);
		String year = iwc.getParameter(CalendarParameters.PARAMETER_YEAR);

		if (month != null && year != null && day != null) {
			return true;
		}
		return false;
	}

	public static DropdownMenu getEntryTypes(String name, int iLocaleId) {
		DropdownMenu drp = new DropdownMenu(name);

		List list = null;
		try {
			list = EntityFinder.findAll(com.idega.block.calendar.data.CalendarEntryTypeBMPBean.getStaticInstance(CalendarEntryType.class));
		} catch (SQLException e) {
			list = null;
		}

		if (list != null) {
			for (int a = 0; a < list.size(); a++) {
				LocalizedText locText = TextFinder.getLocalizedText((CalendarEntryType) list.get(a), iLocaleId);
				String locString = "No text in language";
				if (locText != null) {
					locString = locText.getHeadline();
				}
				drp.addMenuElement(((CalendarEntryType) list.get(a)).getID(), locString);
			}
		}

		return drp;
	}
	
	public static DropdownMenu getCategories(String name, Locale locale, int instanceID) {
		DropdownMenu drp = new DropdownMenu(name);

		List list = CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(instanceID);
		if (list != null) {
			for (int a = 0; a < list.size(); a++) {
				ICCategory category = (ICCategory) list.get(a);
				drp.addMenuElement(category.getID(), category.getName(locale));
			}
		}

		return drp;
	}

	public static int saveEntry(int entryID, int userID, int groupID, int localeID, int categoryId, String entryHeadline, String entryBody, String entryDate, String entryEndDate, String entryType) {
		int returnInt = -1;
		boolean update = false;
		if (entryID != -1) {
			update = true;
		}

		CalendarEntry entry = null;
		if (update) {
			entry = CalendarFinder.getInstance().getEntry(entryID);
			if (entry == null) {
				entry = ((com.idega.block.calendar.data.CalendarEntryHome) com.idega.data.IDOLookup.getHomeLegacy(CalendarEntry.class)).createLegacy();
				update = false;
			}
		} else {
			entry = ((com.idega.block.calendar.data.CalendarEntryHome) com.idega.data.IDOLookup.getHomeLegacy(CalendarEntry.class)).createLegacy();
		}

		entry.setCategoryId(categoryId);
		entry.setEntryTypeID(Integer.parseInt(entryType));
		entry.setUserID(userID);
		entry.setGroupID(groupID);
		entry.setDate(new IWTimestamp(entryDate).getTimestamp());
		if (entryEndDate != null && entryEndDate.length() > 0)
			entry.setEndDate(new IWTimestamp(entryEndDate).getTimestamp());

		if (!update) {
			try {
				entry.store();
				returnInt = new Integer(entry.getPrimaryKey().toString()).intValue();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		} else {
			try {
				entry.store();
				returnInt = new Integer(entry.getPrimaryKey().toString()).intValue();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}

		boolean newLocText = false;
		LocalizedText locText = TextFinder.getLocalizedText(entry, localeID);
		if (locText == null) {
			locText = ((com.idega.block.text.data.LocalizedTextHome) com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
			newLocText = true;
		}

		locText.setHeadline(entryHeadline);
		locText.setBody(entryBody);

		if (newLocText) {
			locText.setLocaleId(localeID);
			try {
				locText.insert();
				locText.addTo(entry);
			} catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		} else {
			try {
				locText.update();
			} catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		}

		return returnInt;
	}

	public static void deleteEntry(int entryID) {
		CalendarEntry cal = CalendarFinder.getInstance().getEntry(entryID);
		if (cal != null) {
			try {
				cal.removeFrom(com.idega.block.text.data.LocalizedTextBMPBean.getStaticInstance(LocalizedText.class));
				cal.delete();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}

	public static int saveEntryType(int typeID, int localeID, String typeHeadline, String fileID) {
		int returnInt = -1;
		boolean update = false;
		if (typeID != -1) {
			update = true;
		}

		CalendarEntryType type = null;
		if (update) {
			type = CalendarFinder.getInstance().getEntryType(typeID);
			if (type == null) {
				type = ((com.idega.block.calendar.data.CalendarEntryTypeHome) com.idega.data.IDOLookup.getHomeLegacy(CalendarEntryType.class)).createLegacy();
				update = false;
			}
		} else {
			type = ((com.idega.block.calendar.data.CalendarEntryTypeHome) com.idega.data.IDOLookup.getHomeLegacy(CalendarEntryType.class)).createLegacy();
		}

		int imageID = -1;
		try {
			imageID = Integer.parseInt(fileID);
		} catch (NumberFormatException e) {
			imageID = -1;
		}

		if (imageID != -1)
			type.setImageID(imageID);

		if (!update) {
			try {
				type.insert();
				returnInt = type.getID();
			} catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		} else {
			try {
				type.update();
				returnInt = type.getID();
			} catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		}

		boolean newLocText = false;
		LocalizedText locText = TextFinder.getLocalizedText(type, localeID);
		if (locText == null) {
			locText = ((com.idega.block.text.data.LocalizedTextHome) com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
			newLocText = true;
		}

		locText.setHeadline(typeHeadline);

		if (newLocText) {
			locText.setLocaleId(localeID);
			try {
				locText.insert();
				locText.addTo(type);
			} catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		} else {
			try {
				locText.update();
			} catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		}

		return returnInt;
	}

	public static void deleteEntryType(int typeID) {
		CalendarEntryType type = CalendarFinder.getInstance().getEntryType(typeID);
		if (type != null) {
			try {
				CalendarEntry[] entries = (CalendarEntry[]) com.idega.block.calendar.data.CalendarEntryBMPBean.getStaticInstance().findAllByColumn(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryTypeID(), typeID);
				if (entries != null)
					for (int a = 0; a < entries.length; a++) {
						entries[a].removeFrom(com.idega.block.text.data.LocalizedTextBMPBean.getStaticInstance(LocalizedText.class));
						entries[a].delete();
					}
				type.removeFrom(com.idega.block.text.data.LocalizedTextBMPBean.getStaticInstance(LocalizedText.class));
				type.delete();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}

	public static void initializeCalendarEntry() throws SQLException {
		TransactionManager t = IdegaTransactionManager.getInstance();
		try {
			t.begin();

			CalendarEntry entry = ((com.idega.block.calendar.data.CalendarEntryHome) com.idega.data.IDOLookup.getHomeLegacy(CalendarEntry.class)).createLegacy();
			entry.setDate(new com.idega.util.IWTimestamp(1, 1, 2000).getTimestamp());
			entry.setEntryTypeID(3);
	
			LocalizedText text = ((com.idega.block.text.data.LocalizedTextHome) com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
			text.setLocaleId(TextFinder.getLocaleId(new Locale("is", "IS")));
			text.setHeadline("idega hf. stofna?");
	
			LocalizedText text2 = ((com.idega.block.text.data.LocalizedTextHome) com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
			text2.setLocaleId(TextFinder.getLocaleId(Locale.ENGLISH));
			text2.setHeadline("idega co. founded");
	
			entry.insert();
			text.insert();
			text2.insert();
	
			text.addTo(entry);
			text2.addTo(entry);
			t.commit();
		} catch (Exception e) {
			try {
				t.rollback();
			} catch (Exception e1) {
				throw new SQLException(e1.getMessage());
			}
			throw new SQLException(e.getMessage());
		}
	}

	public static boolean deleteBlock(int iObjectInstanceId) {
		return CategoryBusiness.getInstance().deleteBlock(iObjectInstanceId);
	}
}
