/*
 * Created on Feb 17, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.members.presentation;

import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;
import is.idega.idegaweb.member.isi.block.members.data.MemberGroupData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author jonas
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class MemberOverview extends Block {

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	public static final String PARAM_NAME_SHOW_STATUS = "showStatus";
	public static final String PARAM_NAME_SHOW_HISTORY = "showHistory";
	public static final String PARAM_NAME_SHOW_FINANCE_OVERVIEW = "showFinanceOverview";
	public static final String PARAM_NAME_FINANCE_ENTRY_ID = "financeEntryID";
	
	public static final String STYLENAME_HEADING = "heading";
	public static final String STYLENAME_HEADER = "header";
	public static final String STYLENAME_COLUMN_HEADING = "columnHeading";
	public static final String STYLENAME_TEXT = "text";
	public static final String STYLENAME_DARK_ROW = "darkRow";
	public static final String STYLENAME_LIGHT_ROW = "lightRow";
	public static final String STYLENAME_COLUMN_ROW = "columnRow";
	public static final String STYLENAME_HEADING_ROW = "headingRow";

	private IWResourceBundle _iwrb = null;
	private IWBundle _iwb = null;

	private MemberGroupData _data = null;

	private Collection _financeData = null;
	private Collator _collator = null;

	public void main(IWContext iwc) {
		IWResourceBundle comUserBundle = iwc.getIWMainApplication().getBundle("com.idega.user").getResourceBundle(iwc);
		_collator = Collator.getInstance(iwc.getLocale());
		
		String status = iwc.getParameter(PARAM_NAME_SHOW_STATUS);
		boolean showStatus = status==null || "true".equals(status);
		boolean showHistory = "true".equals(iwc.getParameter(PARAM_NAME_SHOW_HISTORY));
		boolean showFinanceOverview = "true".equals(iwc.getParameter(PARAM_NAME_SHOW_FINANCE_OVERVIEW));

		Table mainTable = new Table(1, 4);
		mainTable.setBorder(0);
		mainTable.setCellpadding(0);
		mainTable.setCellspacing(0);
		mainTable.setWidth(Table.HUNDRED_PERCENT);

		User user = iwc.getCurrentUser();
		_iwb = getBundle(iwc);
		_iwrb = getResourceBundle(iwc);
		_data = new MemberGroupData(user, _iwrb, comUserBundle);
		try {
			_financeData = (Collection) ((FinanceEntryHome) com.idega.data.IDOLookup.getHome(FinanceEntry.class)).findAllByUser(user);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		mainTable.add(getMemberInfo(user), 1, 1);
		
		mainTable.setColor(1, 2, "#D7D7D7");
		mainTable.setColor(1, 3, "#FFFFFF");

		Table table = new Table();
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setColumns(4);
		int row = 1;
		
		Image minusImg = comUserBundle.getImage("minus.gif");
		Image plusImg = comUserBundle.getImage("plus.gif");

		Link statusLink = new Link(showStatus?minusImg:plusImg);
		statusLink.setBold();
		statusLink.setBelongsToParent(true);
		statusLink.addParameter(PARAM_NAME_SHOW_STATUS, showStatus?"false":"true");
		statusLink.addParameter(PARAM_NAME_SHOW_HISTORY, showHistory?"true":"false");
		statusLink.addParameter(PARAM_NAME_SHOW_FINANCE_OVERVIEW, showFinanceOverview?"true":"false");
		String statusHeader = _iwrb.getLocalizedString("member_overview_registration", "Membership status");
		String statusText = _iwrb.getLocalizedString("member_overview_status", "Status");
		row = insertSectionHeaderIntoTable(table, row, new String[] { statusHeader, statusText }, statusLink);
		if(showStatus) {
			row = insertRegistrationInfoIntoTable(table, row, false);
		}
		
		Link historyLink = new Link(showHistory?minusImg:plusImg);
		historyLink.setBold();
		historyLink.setBelongsToParent(true);
		historyLink.addParameter(PARAM_NAME_SHOW_STATUS, showStatus?"true":"false");
		historyLink.addParameter(PARAM_NAME_SHOW_HISTORY, showHistory?"false":"true");
		historyLink.addParameter(PARAM_NAME_SHOW_FINANCE_OVERVIEW, showFinanceOverview?"true":"false");
		String historyHeader = _iwrb.getLocalizedString("member_overview_history", "Membership history");
		String beginText = _iwrb.getLocalizedString("member_overview_begin_date", "Started");
		String endText = _iwrb.getLocalizedString("member_overview_end_date", "Quit");
		String[] historyHeaders = new String[] { historyHeader, statusText, beginText, endText };
		row = insertSectionHeaderIntoTable(table, row, historyHeaders, historyLink);
		if(showHistory) {
			row = insertRegistrationInfoIntoTable(table, row, true);
		}
		

		Date usersDOB = user.getDateOfBirth();
		if(usersDOB != null) {
			if(isUserOverEighteen(usersDOB)) {
				Link financeOverviewLink = new Link(showFinanceOverview?minusImg:plusImg);
				financeOverviewLink.setBold();
				financeOverviewLink.setBelongsToParent(true);
				financeOverviewLink.addParameter(PARAM_NAME_SHOW_STATUS, showStatus?"true":"false");
				financeOverviewLink.addParameter(PARAM_NAME_SHOW_HISTORY, showHistory?"true":"false");
				financeOverviewLink.addParameter(PARAM_NAME_SHOW_FINANCE_OVERVIEW, showFinanceOverview?"false":"true");
				String financeOverviewHeader = _iwrb.getLocalizedString("member_finance_overview", "Finance entry");
				String entryDateText = _iwrb.getLocalizedString("member_overview_entry_date", "Entry date");
				String amountText = _iwrb.getLocalizedString("member_overview_amount", "Amount");
				String infoText = _iwrb.getLocalizedString("member_overview_info", "Info");
				String[] financeOverviewHeaders = new String[] { financeOverviewHeader, entryDateText, amountText };
				String[] financeOverviewHeaderAlignments = { null, null, "right" };
				row = insertSectionHeaderIntoTable(table, row, financeOverviewHeaders, financeOverviewLink);
				if(showFinanceOverview) {
					row = insertFinanceInfoIntoTable(table, row, true, iwc);
				}
			}
		}
		mainTable.add(table, 1, 4);
		add(mainTable);
	}

	private PresentationObject getMemberInfo(User user) {
		Text name = getStyleText(emptyIfNull(user.getName()), STYLENAME_TEXT);
		Text nameLabel = getStyleText(_iwrb.getLocalizedString("member_overview_name", "Name"), STYLENAME_HEADING);

		Text pNum = getStyleText(emptyIfNull(user.getPersonalID()), STYLENAME_TEXT);
		Text pNumLabel = getStyleText(_iwrb.getLocalizedString("member_overview_pn", "Person number"), STYLENAME_HEADING);
		
		String addressString = getInfoFromCollection(user.getAddresses(), 1);
		if (addressString == null) {
			addressString = _iwrb.getLocalizedString("member_overview_no_info", "N/A");
		}
		Text address = getStyleText(addressString, STYLENAME_TEXT);
		Text addressLabel = getStyleText(_iwrb.getLocalizedString("member_overview_address", "Address"), STYLENAME_HEADING);
		
		String phoneString = getInfoFromCollection(user.getPhones(), -1);
		if (phoneString == null) {
			phoneString = _iwrb.getLocalizedString("member_overview_no_info", "N/A");
		}
		Text phone = getStyleText(phoneString, STYLENAME_TEXT);
		Text phoneLabel = getStyleText(_iwrb.getLocalizedString("member_overview_phone", "Phone"), STYLENAME_HEADING);

		Text clubs = getStyleText(getClubs(), STYLENAME_TEXT);
		Text clubsLabel = getStyleText(_iwrb.getLocalizedString("member_overview_clubs", "Clubs"), STYLENAME_HEADING);

		Text emails = getStyleText(getEmail(user), STYLENAME_TEXT);
		Text emailLabel = getStyleText(_iwrb.getLocalizedString("member_overview_email", "Email"), STYLENAME_HEADING);

		int imageId = user.getSystemImageID();
		Image image = null;
		if (imageId != -1) {
			try {
				image = new Image(imageId, _iwrb.getLocalizedString("member_overview_imag_text", "User picture"));
			}
			catch (SQLException e) {
				image = _iwb.getImage("nouser.jpg");
			}
		}
		else {
			image = _iwb.getImage("nouser.jpg");
		}
		if (image != null) {
			image.setMaxImageWidth(107);
		}

		Table table = new Table(3, 6);
		table.setCellpadding(3);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setWidth(2, 10);
		int row = 1;

		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_TOP);
		table.add(nameLabel, 1, row);
		table.add(name, 3, row++);

		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_TOP);
		table.add(pNumLabel, 1, row);
		table.add(pNum, 3, row++);

		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_TOP);
		table.add(addressLabel, 1, row);
		table.add(address, 3, row++);

		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_TOP);
		table.add(phoneLabel, 1, row);
		table.add(phone, 3, row++);

		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_TOP);
		table.add(emailLabel, 1, row);
		table.add(emails, 3, row++);

		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_TOP);
		table.add(clubsLabel, 1, row);
		table.add(clubs, 1, row++);
		
		Table frameTable = new Table(2, 1);
		frameTable.setCellpadding(15);
		frameTable.setCellspacing(0);
		frameTable.setCellpaddingLeft(2, 1, 0);
		frameTable.add(image, 1, 1);
		frameTable.add(table, 2, 1);
		frameTable.setRowVerticalAlignment(1, Table.VERTICAL_ALIGN_TOP);
		
		return frameTable;
	}

	private String getEmail(User user) {
		StringBuffer buffer = new StringBuffer();
		try {
			Iterator emailIter = user.getEmails().iterator();
			while (emailIter.hasNext()) {
				Email email = (Email) emailIter.next();
				buffer.append(email.getEmailAddress());
				if (emailIter.hasNext()) {
					buffer.append(", ");
				}
			}
		}
		catch (Exception e) {
			// don't give a pair of donkeys kiddneys, most likely means there
			// are no emails for user
		}
		return buffer.toString();
	}

	/**
	 * Gives a comma separated list of the items in a Collection
	 * 
	 * @param col
	 *            The Collection to print items from
	 * @param max
	 *            The maximum number of items to print, -1 means print all items
	 *            in Collection
	 * @return
	 */
	private String getInfoFromCollection(Collection col, int max) {
		StringBuffer buf = new StringBuffer();
		if (col != null && !col.isEmpty()) {
			Iterator iter = col.iterator();
			boolean first = true;
			int count = 0;
			while (iter.hasNext()) {
				// count items have been printed
				if (max != -1 && count == max) {
					break;
				}
				Object o = iter.next();
				if (o instanceof Address) {
					Address addr = (Address) o;
					String street = addr.getStreetAddress();
					if (street != null && street.length() > 0) {
						buf.append(street);
						String pc = addr.getPostalAddress();
						if (pc != null && pc.length() > 0) {
							buf.append(", ").append(pc);
						}
					}
				}
				else if (o instanceof Phone) {
					Phone phone = (Phone) o;
					if (phone != null) {
						if (phone.getPhoneTypeId() == 1) {
							buf.append("hs ");
						}
						else if (phone.getPhoneTypeId() == 2) {
							buf.append("vs ");
						}
						else if (phone.getPhoneTypeId() == 3) {
							buf.append("gsm ");
						}
						else if (phone.getPhoneTypeId() == 4) {
							buf.append("fax ");
						}
						buf.append(phone.getNumber());
					}
				}
				else {
					buf.append(o.toString());
				}
				first = false;
				count++;
				if (iter.hasNext()) {
					buf.append(Text.BREAK);
				}
			}
		}
		return buf.length() == 0 ? null : buf.toString();
	}

	/**
	 * Insert a header (the names of the group categories)
	 * 
	 * @param table
	 *            table to insert header into
	 * @param row
	 *            row in table to insert header
	 * @param headers
	 *            values to insert into columns
	 * @param link
	 *            a link to insert in first column (the '+' or '-' links)
	 * @return Index of next empty row in <code>table</code>,
	 *         <code>row</code>+1
	 */
	private int insertSectionHeaderIntoTable(Table table, int row, String[] headers, Link link) {
		int length = headers.length;
		table.mergeCells(1 + length, row, table.getColumns(), row); // merges last column with
												   // value to last column
												   // (notice that the first
												   // column is the '+' or '-')
		for (int i = 0; i < length; i++) {
			if (headers[i] != null) {
				Text histText = getStyleText(headers[i], STYLENAME_HEADER);
				histText.setBold();
				table.setRowStyleClass(row, getStyleName(STYLENAME_HEADING_ROW));
				table.add(histText, i + 1, row);
				if (i > 0) {
					table.setCellpaddingLeft(i+1, row, 2);
				}
			}
		}
		
		table.add(link, 1, row);

		table.setCellpaddingLeft(1, row, 20);
		table.setCellpaddingRight(table.getColumns(), row, 20);
		table.setAlignment(1, row, "left");

		return ++row;
	}

	/**
	 * Inserts registration info into table. Each row show registration info for
	 * a group and shows the following>
	 * <ul>
	 * <li>Groups name</li>
	 * <li>Users status in group</li>
	 * <li>Date when user became menber of group (if <code>showHistory</code>
	 * is <code>true</code>)</li>
	 * <li>Date when user quit group, empty if user is still in group (if
	 * <code>showHistory</code> is <code>true</code>)</li>
	 * </ul>
	 * 
	 * @param table
	 *            The table to insert registration info into
	 * @param row
	 *            The row in the table to start inserting info into
	 * @param showHistory
	 *            If <code>true</code> then the time when user begun and quit
	 *            (possibly empty) in the group is shown
	 * @return Index of next empty row in <code>table</code>
	 */
	private int insertRegistrationInfoIntoTable(Table table, int row, boolean showHistory) {
		List regInfoList = _data.getGroupInfoList();
		Collections.sort(regInfoList, new Comparator() {

			public int compare(Object arg0, Object arg1) {
				String[] sa0 = (String[]) arg0;
				String[] sa1 = (String[]) arg1;
				int result = _collator.compare(sa0[1], sa1[1]);
				if (result == 0) {
					result = _collator.compare(sa0[0], sa1[0]);
				}
				return result;
			}

		});
		Iterator riIter = regInfoList.iterator();
		String previousCategoryName = "";
		int regRow = 1;
		while (riIter.hasNext()) {
			String[] ri = (String[]) riIter.next();
			String name = ri[0];
			String categoryName = ri[1];
			String status = ri[2];
			String begin = ri[3];
			String end = ri[4] != null ? ri[4] : "";
			if (end.length() > 0) {
				// only showing current registration and user has unregisterd
				// from this group
				continue;
			}
			if (categoryName == null) {
				categoryName = "";
			}
			if (!categoryName.equals(previousCategoryName)) {
				table.mergeCells(1, row, table.getColumns(), row);
				table.setCellpaddingLeft(1, row, 30);
				table.setRowStyleClass(row, STYLENAME_COLUMN_ROW);
				table.add(getStyleText(categoryName, STYLENAME_COLUMN_HEADING), 1, row++);
				previousCategoryName = categoryName;
				regRow = 1;
			}
			table.add(getStyleText(name, STYLENAME_TEXT), 1, row);
			table.add(getStyleText(status, STYLENAME_TEXT), 2, row);
			if (showHistory) {
				table.add(getStyleText(begin, STYLENAME_TEXT), 3, row);
				table.add(getStyleText(end, STYLENAME_TEXT), 4, row);
			}
			
			if (regRow % 2 == 0) {
				table.setRowStyleClass(row, getStyleName(STYLENAME_DARK_ROW));
			}
			else {
				table.setRowStyleClass(row, getStyleName(STYLENAME_LIGHT_ROW));
			}
			table.setCellpaddingLeft(1, row, 38);
			table.setCellpaddingRight(table.getColumns(), row, 20);
			
			row++;
			regRow++;
		}
		return row;
	}

	private int insertFinanceInfoIntoTable(Table table, int row, boolean showHistory, IWContext iwc) {
		ArrayList finEntryList = new ArrayList(_financeData);
		Collections.sort(finEntryList, new Comparator() {

			public int compare(Object arg0, Object arg1) {
				FinanceEntryBMPBean fin0 = (FinanceEntryBMPBean) arg0;
				FinanceEntryBMPBean fin1 = (FinanceEntryBMPBean) arg1;
				Timestamp stamp0 = fin0.getDateOfEntry();
				Timestamp stamp1 = fin1.getDateOfEntry();
				return _collator.compare(stamp1.toString(), stamp0.toString());
			}

		});
		Iterator finIter = finEntryList.iterator();
		NumberFormat format = NumberFormat.getInstance(_iwrb.getLocale());
		format.setMaximumFractionDigits(0);
		format.setMinimumIntegerDigits(1);
		IWBundle iwb = getBundle(iwc);
		int financeRow = 1;
		while (finIter.hasNext()) {
			FinanceEntry finEntry = (FinanceEntry) finIter.next();

			Link financeDetailLink = new Link(iwb.getImage("magnify.gif"));
			financeDetailLink.setWindowToOpen(MemberFinanceEntryDetailWindow.class);
			financeDetailLink.addParameter(PARAM_NAME_FINANCE_ENTRY_ID, finEntry.getPrimaryKey().toString());

			Text displayName = getStyleText(finEntry.getGroup().getName() + " - " + finEntry.getDivision().getName() + " - " + finEntry.getClub(), STYLENAME_TEXT);
			table.add(displayName, 1, row);
			table.add(getStyleText(new IWTimestamp(finEntry.getDateOfEntry()).getDateString("dd.MM.yyyy"), STYLENAME_TEXT), 2, row);
			Text amountText = getStyleText(format.format(finEntry.getAmount()), STYLENAME_TEXT);
			if (finEntry.getType() == FinanceEntryBMPBean.TYPE_PAYMENT) {
				amountText.setFontColor("red");
			}
			table.add(amountText, 3, row);
			table.setAlignment(3, row, "right");
			table.add(financeDetailLink, 3, row);
			
			if (financeRow % 2 == 0) {
				table.setRowStyleClass(row, getStyleName(STYLENAME_DARK_ROW));
			}
			else {
				table.setRowStyleClass(row, getStyleName(STYLENAME_LIGHT_ROW));
			}
			table.setCellpaddingLeft(1, row, 38);
			table.setCellpaddingRight(table.getColumns(), row, 20);
			
			row++;
			financeRow++;
		}
		return row;
	}

	private String getClubs() {
		/*Table table = new Table();
		Iterator clubListIter = _data.getClubList().iterator();
		int row = 1;
		if (clubListIter.hasNext()) {
			Text clubsLabel = new Text(_iwrb.getLocalizedString("member_overview_clubs", "Member of: "));
			clubsLabel.setBold();
			table.add(clubsLabel, 1, row);
			Text contactLabel = new Text(_iwrb.getLocalizedString("member_overview_clubs_contact", "Contact: "));
			contactLabel.setBold();
			table.add(contactLabel, 2, row++);
		}
		else {
			return null;
		}
		String linkText = _iwrb.getLocalizedString("member_overview_clubs_link_text", "Send message regarding registration");
		while (clubListIter.hasNext()) {
			Group club = (Group) clubListIter.next();
			String name = club.getName();
			String email = null;
			try {
				email = ((Email) club.getEmails().iterator().next()).getEmailAddress();
			}
			catch (Exception e) {
				// no email for club
			}
			if (name != null && name.length() > 0) {
				table.add(name, 1, row);
				if (email != null) {
					Link link = new Link(linkText);
					link.setURL("mailto:" + email);
					link.setSessionId(false);
					table.add(link, 2, row);
				}
				row++;
			}
		}

		return table;*/
		
		StringBuffer buffer = new StringBuffer();
		Iterator clubListIter = _data.getClubList().iterator();
		while (clubListIter.hasNext()) {
			Group club = (Group) clubListIter.next();
			String name = club.getName();
			buffer.append(name);
			if (clubListIter.hasNext()) {
				buffer.append(Text.BREAK);
			}
		}
		
		return buffer.toString();
	}

	public String emptyIfNull(String str) {
		return str == null ? "" : str;
	}
	
	private boolean isUserOverEighteen(Date dateOfBirth) {
		Calendar rightNow = Calendar.getInstance();
    int currentYear = rightNow.get(Calendar.YEAR);
    int currentMonth = rightNow.get(Calendar.MONTH);
    int currentDay = rightNow.get(Calendar.DAY_OF_MONTH);

		GregorianCalendar birth = new GregorianCalendar();
		birth.setTime(dateOfBirth);
		int yearOfBirth = birth.get(Calendar.YEAR);
		int monthOfBirth = birth.get(Calendar.MONTH);
		int dayOfBirth = birth.get(Calendar.DAY_OF_MONTH);
		
		int age;
		
		if(currentMonth < monthOfBirth || (currentMonth == monthOfBirth && currentDay < dayOfBirth)) {
			age = (currentYear - 1) - yearOfBirth;
		}
		else {
			age = currentYear - yearOfBirth;
		}
    if(age < 18) {
    		return false;
    }
    else {
    		return true;
    }
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public Map getStyleNames() {
		Map map = new HashMap();
		map.put(STYLENAME_HEADING, "font-family: Arial,Helvetica,sans-serif;font-size: 11px;font-weight: bold;color: #3D3D3D;");
		map.put(STYLENAME_HEADER, "font-family: Arial,Helvetica,sans-serif;font-size: 11px;font-weight: bold;color: #1B2E45;");
		map.put(STYLENAME_COLUMN_HEADING, "font-family: Arial,Helvetica,sans-serif;font-size: 10px;font-weight: bold;color: #3D3D3D;");
		map.put(STYLENAME_TEXT, "font-family: Arial,Helvetica,sans-serif;font-size: 10px;color: #828282;");
		map.put(STYLENAME_DARK_ROW, "background-color: #EFEFEF;padding: 2px;");
		map.put(STYLENAME_LIGHT_ROW, "background-color: #FFFFFF;padding: 2px;");
		map.put(STYLENAME_COLUMN_ROW, "background-color: #EFEFEF;padding: 2px;border-top: 1px #C4C2C2 solid;border-bottom: 1px #C4C2C2 solid;");
		map.put(STYLENAME_HEADING_ROW, "background-color: #F8F8F8;padding: 5px;padding-top: 10px;");

		return map;
	}
}