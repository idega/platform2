package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.core.data.Email;
import com.idega.core.data.EmailHome;
import com.idega.core.data.Phone;
import com.idega.core.data.PhoneHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

/**
 * @author gimmi
 */
public class SchoolContentItemLinks extends SchoolContentItem {

	String _headerStyle;
	String _headerColor;
	int spaceBetween = 5;

	/**
	 * @see com.idega.block.school.presentation.SchoolContentItem#getObject()
	 */
	protected PresentationObject getObject() throws RemoteException {
		
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		
		int row = 1; /** breytti ut 0, vegna arrayIndexOutOfBounds Villu... */
		boolean useBreak = false;
				
		String manType = getSchoolBusiness(_iwc).getSchoolManagementTypeString(_school.getSchoolManagermentType());
		if (manType != null) {
			++row;
			table.add(getHeader(_iwrb.getLocalizedString("school.management_type","Management Type")+":"), 1, row);
			++row;
			table.add(getText(_iwrb.getLocalizedString(manType)), 1, row);
			useBreak = true;
		}
		

		String address = _school.getSchoolAddress();
		String zipArea = _school.getSchoolZipArea();
		String zipCode = _school.getSchoolZipCode();
		String phone = _school.getSchoolPhone();
		String fax = _school.getSchoolFax();
		if (address != null || zipArea != null || zipCode != null || phone != null || fax != null) {
			if (useBreak) {
				++row;
				table.setHeight(row, spaceBetween);
				++row;
			}
			table.add(getHeader(_iwrb.getLocalizedString("school.address","Address")+":"), 1, row);
			if (address != null) {
				++row;
				table.add(getText(address), 1, row);
			}
			if (zipCode != null) {
				++row;
				table.add(getText(zipCode), 1, row);
				if (zipArea != null) {
					table.add(getText(", "+zipArea), 1, row);
				}
			}else {
				if (zipArea != null) {
					++row;
					table.add(getText(zipArea), 1, row);
				}
			}
			if (phone != null) {
				++row;
				table.add(getText(_iwrb.getLocalizedString("school.Tph","Tph")+": "+phone), 1, row);
			}
			if (fax != null) {
				++row;
				table.add(getText(_iwrb.getLocalizedString("school.fax","Fax")+": "+fax), 1, row);
			}
			useBreak = true;
		}
		
		try {
			if (useBreak) {
				++row;
				table.setHeight(row, spaceBetween);
				++row;
			}
			table.add(getHeader(_iwrb.getLocalizedString("school.headmaster","Headmaster")+":"), 1, row);
			int headmasterId = _school.getHeadmasterUserId();
			if (headmasterId > 0 ) {
				UserHome uHome = (UserHome) IDOLookup.getHome(User.class);
				User user = uHome.findByPrimaryKey(new Integer(headmasterId));
				row = insertUser(table, row, user);
				useBreak = true;
			}
		} catch (FinderException e) {
			e.printStackTrace(System.err);
		}
		
		try {
			if (useBreak) {
				++row;
				table.setHeight(row, spaceBetween);
				++row;
			}
			table.add(getHeader(_iwrb.getLocalizedString("school.assistant_headmaster","Assistant Headmaster")+":"), 1, row);
			int assistantHeadmasterId = _school.getAssistantHeadmasterUserId();
			if (assistantHeadmasterId > 0 ) {
				UserHome uHome = (UserHome) IDOLookup.getHome(User.class);
				User user = uHome.findByPrimaryKey(new Integer(assistantHeadmasterId));
				row = insertUser(table, row, user);
				useBreak = true;
			}
		} catch (FinderException e) {
			e.printStackTrace(System.err);
		}

		
		String webPage = _school.getSchoolWebPage();
		if (webPage != null) {
			if (useBreak) {
				++row;
				table.setHeight(row, spaceBetween);
				++row;
			}
			Link link = new Link(getText(webPage), webPage);
			table.add(getHeader(_iwrb.getLocalizedString("school.web_page","Web Page")+":"), 1, row);	
			++row;
			table.add(link, 1, row);
			
			useBreak = true;
		}
		

		/** Her mun koma linkur i boxid eda boxid sjalft 		
			if (useBreak) {
				++row;
				table.setHeight(row, spaceBetween);
				++row;
			}
		*/
		
		
		return table;
	}

	protected int insertUser(Table table, int row, User user) throws RemoteException {
		++row;
		String name = user.getName();
		Collection emails = user.getEmails();
		if (emails != null) {
			Iterator eIter = emails.iterator();
			EmailHome eHome = (EmailHome) IDOLookup.getHome(Email.class);
			Email email;
			Link link;
			int emSize = emails.size();
			if (emSize == 1) {
				try {
					email = eHome.findByPrimaryKey(eIter.next());
					link = new Link(getText(name), "mailto:"+email.getEmailAddress());
					table.add(link, 1, row);
				} catch (FinderException e) {
					e.printStackTrace(System.err);
				}
			}else if (emSize < 1) {
				table.add(name, 1, row);
			}else if (emSize > 1) {
				table.add(name, 1, row);
				while (eIter.hasNext()) {
					try {
						email = eHome.findByPrimaryKey(eIter.next());
						link = new Link(getText(email.getEmailAddress()), "mailto:"+email.getEmailAddress());
						++row;
						table.add(link, 1, row);
					} catch (FinderException e) {
						e.printStackTrace(System.err);
					}
				}
			}
		}
		
		Collection phones = user.getPhones();
		if (phones != null && phones.size() > 0) {
			Iterator pIter = phones.iterator();	
			PhoneHome pHome = (PhoneHome) IDOLookup.getHome(Phone.class);
			Phone uPhone;
			while (pIter.hasNext()) {
				try {
					uPhone = pHome.findByPrimaryKey(pIter.next());	
					++row;
					table.add(getText(_iwrb.getLocalizedString("school.Tph","Tph")+": "+uPhone.getNumber()), 1, row);
				} catch (FinderException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return row;
	}
	
	private Text getHeader(String content) {
		Text text = new Text(content);
		if (_headerStyle != null) {
			text.setFontStyle(_headerStyle);
		}	else {
			text.setBold(true);	
		}
		if (_headerColor != null) {
			text.setFontColor(_headerColor);	
		}
		return text;
	}
	
	public void setHeaderStyle(String style) {
		_headerStyle = style;	
	}
	
	public void setHeaderColor(String color) {
		_headerColor = color;	
	}

	private UserBusiness getUserBusiness(IWApplicationContext iwac) throws RemoteException {
		return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
	}

}
