/*
 * Created on Feb 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.members.presentation;

import is.idega.idegaweb.golf.entity.PhoneBMPBean;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryBMPBean;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;
import is.idega.idegaweb.member.isi.block.members.data.MemberGroupData;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class MemberOverview extends Block {
	
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	public static final String PARAM_NAME_SHOW_STATUS = "showStatus";
	public static final String PARAM_NAME_SHOW_HISTORY = "showHistory";
	public static final String PARAM_NAME_SHOW_FINANCE_OVERVIEW = "showFinanceOverview";
	public static final String PARAM_NAME_FINANCE_ENTRY_ID = "financeEntryID";
	private IWResourceBundle _iwrb = null;
	
	public void main(IWContext iwc) {
		IWResourceBundle comUserBundle = iwc.getIWMainApplication().getBundle("com.idega.user").getResourceBundle(iwc);
		_collator = Collator.getInstance(iwc.getLocale());
		
		String status = iwc.getParameter(PARAM_NAME_SHOW_STATUS);
		boolean showStatus = status==null || "true".equals(status);
		boolean showHistory = "true".equals(iwc.getParameter(PARAM_NAME_SHOW_HISTORY));
		boolean showFinanceOverview = "true".equals(iwc.getParameter(PARAM_NAME_SHOW_FINANCE_OVERVIEW));
		
		Table mainTable = new Table();
		
		User user = iwc.getCurrentUser();
		_iwrb = getResourceBundle(iwc);
		_data = new MemberGroupData(user, _iwrb, comUserBundle);
		try {
			_financeData = (Collection) ((FinanceEntryHome) com.idega.data.IDOLookup.getHome(FinanceEntry.class)).findAllByUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mainTable.add(getMemberInfo(user), 1, 1);
		addBreak();
		addBreak();
		
		Table table = new Table();
		table.setWidth("100%");
		int row = 1;
		
		// show registration status section
		Link statusLink = new Link(showStatus?"-":"+");
		statusLink.setBold();
		statusLink.setBelongsToParent(true);
		statusLink.addParameter(PARAM_NAME_SHOW_STATUS, showStatus?"false":"true");
		statusLink.addParameter(PARAM_NAME_SHOW_HISTORY, showHistory?"true":"false");
		statusLink.addParameter(PARAM_NAME_SHOW_FINANCE_OVERVIEW, showFinanceOverview?"true":"false");
		String statusHeader = _iwrb.getLocalizedString("member_overview_registration", "Membership status");
		row = insertSectionHeaderIntoTable(table, row, new String[] {statusHeader}, statusLink);
		if(showStatus) {
			row = insertRegistrationInfoIntoTable(table, row, false);
		}
		
		row += 3;
		
		// show registration history section
		Link historyLink = new Link(showHistory?"-":"+");
		historyLink.setBold();
		historyLink.setBelongsToParent(true);
		historyLink.addParameter(PARAM_NAME_SHOW_STATUS, showStatus?"true":"false");
		historyLink.addParameter(PARAM_NAME_SHOW_HISTORY, showHistory?"false":"true");
		historyLink.addParameter(PARAM_NAME_SHOW_FINANCE_OVERVIEW, showFinanceOverview?"true":"false");
		String historyHeader = _iwrb.getLocalizedString("member_overview_history", "Membership history");
		String statusText = _iwrb.getLocalizedString("member_overview_status", "Status");
		String beginText = _iwrb.getLocalizedString("member_overview_begin_date", "Started");
		String endText = _iwrb.getLocalizedString("member_overview_end_date", "Quit");
		String[] historyHeaders = showHistory?(new String[] {historyHeader, statusText, beginText, endText}):(new String[] {historyHeader});
		row = insertSectionHeaderIntoTable(table, row, historyHeaders, historyLink);
		if(showHistory) {
			row = insertRegistrationInfoIntoTable(table, row, true);
		}
		
		row += 3;
		
		// show finance overview section
		Link financeOverviewLink = new Link(showFinanceOverview?"-":"+");
		financeOverviewLink.setBold();
		financeOverviewLink.setBelongsToParent(true);
		financeOverviewLink.addParameter(PARAM_NAME_SHOW_STATUS, showStatus?"true":"false");
		financeOverviewLink.addParameter(PARAM_NAME_SHOW_HISTORY, showHistory?"true":"false");
		financeOverviewLink.addParameter(PARAM_NAME_SHOW_FINANCE_OVERVIEW, showFinanceOverview?"false":"true");
		String financeOverviewHeader = _iwrb.getLocalizedString("member_finance_overview", "Finance entry");
		String entryDateText = _iwrb.getLocalizedString("member_overview_entry_date", "Entry date");
		String amountText = _iwrb.getLocalizedString("member_overview_amount", "Amount");
		String infoText = _iwrb.getLocalizedString("member_overview_info", "Info");
		String[] financeOverviewHeaders = showFinanceOverview?(new String[] {financeOverviewHeader, entryDateText, amountText}):(new String[] {financeOverviewHeader});
		String[] financeOverviewHeaderAlignments = {null, null, "right"};
		row = insertSectionHeaderIntoTable(table, row, financeOverviewHeaders, financeOverviewLink);
		if(showFinanceOverview) {
			row = insertFinanceInfoIntoTable(table, row, true, iwc);
		}
		mainTable.add(table, 1, 2);
		
		/*PrintButton button = new PrintButton(_iwrb.getLocalizedString("button.print","Print"));
		mainTable.add(button, 1, 3);
		mainTable.setAlignment(1, 3, "right");
		*/
		
		add(mainTable);
	}
	
	private PresentationObject getMemberInfo(User user) {
		String name = emptyIfNull(user.getName());
		Text nameLabel = new Text(_iwrb.getLocalizedString("member_overview_name", "Name: "));
		nameLabel.setBold();
		String pNum = emptyIfNull(user.getPersonalID());
		Text pNumLabel = new Text(_iwrb.getLocalizedString("member_overview_pn", "Person number: "));
		pNumLabel.setBold();
		String address = getInfoFromCollection(user.getAddresses(), 1);
		if(address==null) {
			address = _iwrb.getLocalizedString("member_overview_no_info", "N/A");
		}
		Text addressLabel = new Text(_iwrb.getLocalizedString("member_overview_address", "Address: "));
		addressLabel.setBold();
		String phone = getInfoFromCollection(user.getPhones(), -1);
		if(phone==null) {
			phone = _iwrb.getLocalizedString("member_overview_no_info", "N/A");
		}
		Text phoneLabel = new Text(_iwrb.getLocalizedString("member_overview_phone", "Phone: "));
		phoneLabel.setBold();
		
		Table clubs = getClubsTable();
		
		Table emails = getEmailTable(user);
		Text emailLabel = new Text(_iwrb.getLocalizedString("member_overview_email", "Email: "));
		emailLabel.setBold();
		
		user.getSystemImageID();
		
		int imageId = user.getSystemImageID();
		Image image = null;
		if(imageId != -1) {
			try {
				image = new Image(imageId, _iwrb.getLocalizedString("member_overview_imag_text", "User picture"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No image found for user " + user.getName());
		}
		if(image != null) {
			image.setMaxImageWidth(107);
		}
		
		Table table = new Table();
		table.setWidth("100%");
		table.setCellpadding(1);
		table.setCellspacing(2);
		int row = 1;
		table.add(nameLabel, 1, row);
		table.add(name, 2, row++);
		table.add(pNumLabel, 1, row);
		table.add(pNum, 2, row++);
		table.add(addressLabel, 1, row);
		table.add(address, 2, row++);
		table.add(phoneLabel, 1, row);
		table.add(phone, 2, row++);
		table.add(emailLabel, 1, row);
		table.add(emails, 2, row++);
		table.mergeCells(1, row, 2, row);
		table.add(clubs, 1, row++);
		if(image!=null) {
			table.mergeCells(3, 1, 3, row-1);
			table.setVerticalAlignment(3, 1, Table.VERTICAL_ALIGN_MIDDLE);
			table.add(image, 3, 1);
		}
		return table;
	}
	
	private Table getEmailTable(User user) {
		Table table = new Table();
		int row = 1;
		try {
			Iterator emailIter = user.getEmails().iterator();
			while(emailIter.hasNext()) {
				Email email = (Email) emailIter.next();
				Text address = new Text(email.getEmailAddress());
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
	
	/**
	 * Gives a comma separated list of the items in a Collection
	 * @param col The Collection to print items from
	 * @param max The maximum number of items to print, -1 means print all items in Collection
	 * @return
	 */
	private String getInfoFromCollection(Collection col, int max) {
		StringBuffer buf = new StringBuffer();
		if(col!=null && !col.isEmpty()) {
			Iterator iter = col.iterator();
			boolean first = true;
			int count = 0;
			while(iter.hasNext()) {
				// count items have been printed
				if(max != -1 && count == max) {
					break;
				}
				Object o = iter.next();
				if(first) {
					first = false;
				} else {
					buf.append(", ");
				}
				if(o instanceof Address) {
					Address addr = (Address) o;
					String street = addr.getStreetAddress();
					if(street != null && street.length()>0) {
						buf.append(street);
						String pc = addr.getPostalAddress();
						if(pc!=null && pc.length()>0) {
							buf.append(", ").append(pc);
						}
					}
				} else if(o instanceof Phone) {
				    Phone phone = (Phone) o;
				    if (phone != null) {
				        if (phone.getPhoneTypeId() == 1) {
				            buf.append("hs ");
				        } else if(phone.getPhoneTypeId() == 2) {
				            buf.append("vs ");
				        } else if(phone.getPhoneTypeId() == 3) {
				            buf.append("gsm ");
				        } else if(phone.getPhoneTypeId() == 4) {
				            buf.append("fax ");
				        }
				        buf.append(phone.getNumber());
				    }
				} else {
					buf.append(o.toString());
				}
				first = false;
				count++;
			}
		}
		return buf.length()==0?null:buf.toString();
	}
	
	/**
	 * Insert a header (the names of the group categories)
	 * @param table table to insert header into
	 * @param row row in table to insert header
	 * @param headers values to insert into columns
	 * @param link a link to insert in first column (the '+' or '-' links)
	 * @return Index of next empty row in <code>table</code>, <code>row</code>+1
	 */
	private int insertSectionHeaderIntoTable(Table table, int row, String[] headers, Link link) {
		int length = headers.length;
		table.mergeCells(1+length, row, 5, row); // merges last column with value to last column (notice that the first column is the '+' or '-')
		for(int i=0; i<length; i++) {
			if(headers[i]!=null) {
				Text histText = new Text(headers[i]);
				histText.setBold();
				table.add(histText, i+2, row);
			}
		}
		
		table.add(link, 1, row);
		table.setAlignment(1, row, "left");
		
		return ++row;
	}
	
	/**
	 * Inserts registration info into table. Each row show registration info for a group and shows the following>
	 * <ul>
	 *   <li>Groups name</li>
	 *   <li>Users status in group</li>
	 *   <li>Date when user became menber of group (if <code>showHistory</code> is <code>true</code>)</li>
	 *   <li>Date when user quit group, empty if user is still in group (if <code>showHistory</code> is <code>true</code>)</li>
	 * </ul>
	 * @param table The table to insert registration info into
	 * @param row The row in the table to start inserting info into
	 * @param showHistory If <code>true</code> then the time when user begun and quit (possibly empty) in the group
	 *        is shown
	 * @return Index of next empty row in <code>table</code>
	 */
	private int insertRegistrationInfoIntoTable(Table table, int row, boolean showHistory) {
		List regInfoList = _data.getGroupInfoList();
		Collections.sort(regInfoList, new Comparator() {

			public int compare(Object arg0, Object arg1) {
				String[] sa0 = (String[]) arg0;
				String[] sa1 = (String[]) arg1;
				int result = _collator.compare(sa0[1], sa1[1]);
				if(result==0) {
					result = _collator.compare(sa0[0], sa1[0]);
				}
				return result;
			}
		
		});
		Iterator riIter = regInfoList.iterator();
		String previousCategoryName = "";
		resetColor();
		while(riIter.hasNext()) {
			String[] ri = (String[]) riIter.next();
			String name = ri[0];
			String categoryName = ri[1];
			String status = ri[2];
			String begin = ri[3];
			String end = ri[4]!=null?ri[4]:"";
			if(end.length()>0 && !showHistory) {
				// only showing current registration and user has unregisterd from this group
				continue;
			}
			if(categoryName==null) {
				categoryName="";
			}
			if(!categoryName.equals(previousCategoryName)) {
				table.mergeCells(2, row, 5, row);
				table.setColor(2, row, _categoryColor);
				table.add(categoryName, 2, row++);
				previousCategoryName = categoryName;
				resetColor();
			}
			table.add(name, 2, row);
			table.add(status, 3, row);
			String color = getColor();
			table.setColor(2, row, color);
			table.setColor(3, row, color);
			if(showHistory) {
				table.add(begin, 4, row);
				table.setColor(4, row, color);
				table.add(end, 5, row);
				table.setColor(5, row, color);
			} else {
				table.mergeCells(3, row, 5, row);
			}
			
			row++;
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
				return _collator.compare(stamp1.toString(),stamp0.toString());
			}
			
		});
		Iterator finIter = finEntryList.iterator();
		String previousCategoryName = "";
		resetColor();
		NumberFormat format = NumberFormat.getInstance(_iwrb.getLocale());
		format.setMaximumFractionDigits(0);
		format.setMinimumIntegerDigits(1);
		IWBundle iwb = getBundle(iwc);
		while(finIter.hasNext()) {
			FinanceEntry finEntry = (FinanceEntry) finIter.next();
			
			Link financeDetailLink = new Link(iwb.getImage("magnify.gif"));
			financeDetailLink.setWindowToOpen(MemberFinanceEntryDetailWindow.class);
			financeDetailLink.addParameter(PARAM_NAME_FINANCE_ENTRY_ID, finEntry.getPrimaryKey().toString());
			
			String displayName = finEntry.getGroup().getName()+" - "+finEntry.getDivision().getName()+" - "+finEntry.getClub(); 	
			table.add(displayName,2,row);
			table.add(new IWTimestamp(finEntry.getDateOfEntry()).getDateString("dd.MM.yyyy"),3,row);
			Text amountText = new Text(format.format(finEntry.getAmount()));
			if (finEntry.getType() == FinanceEntryBMPBean.TYPE_PAYMENT) {
				amountText.setFontColor("red");
			}
			table.add(amountText,4,row);
			table.setAlignment(4, row, "right");
			table.add(financeDetailLink,5,row);
		row++;
		}
		return row;
	}
	
	private Table getClubsTable() {
		Table table = new Table();
		Iterator clubListIter = _data.getClubList().iterator();
		int row = 1;
		if(clubListIter.hasNext()) {
			Text clubsLabel = new Text(_iwrb.getLocalizedString("member_overview_clubs", "Member of: "));
			clubsLabel.setBold();
			table.add(clubsLabel, 1, row);
			Text contactLabel = new Text(_iwrb.getLocalizedString("member_overview_clubs_contact", "Contact: "));
			contactLabel.setBold();
			table.add(contactLabel, 2, row++);
		} else {
			return null;
		}
		String linkText = _iwrb.getLocalizedString("member_overview_clubs_link_text", "Send message regarding registration");
		while(clubListIter.hasNext()) {
			Group club = (Group) clubListIter.next();
			String name = club.getName();
			String email = null;
			try {
				email = ((Email) club.getEmails().iterator().next()).getEmailAddress();
			} catch(Exception e) {
				// no email for club
			}
			if(name!=null && name.length()>0) {
				table.add(name, 1, row);
				if(email!=null) {
					Link link = new Link(linkText);
					link.setURL("mailto:" + email);
					link.setSessionId(false);
					table.add(link, 2, row);
				}
				row++;
			}
		}
		
		return table;
	}
	
	public String emptyIfNull(String str) {
		return str==null?"":str;
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	private String getColor() {
		if(_currentColor == _color1) {
			_currentColor = _color2;
		} else {
			_currentColor = _color1;
		}
		return _currentColor;
	}
	
	private void resetColor() {
		_currentColor = null;
	}
	
	private String _currentColor = null;
	private String _color1 = "lightgray";
	private String _color2 = "white";
	private String _categoryColor = "gray";
		
	//private Integer _userId = new Integer(338609);
	//private User _user = null;
	private MemberGroupData _data = null;
	private Collection _financeData = null;
	
	private Collator _collator = null;
}
