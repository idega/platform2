package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.SchoolManagementType;
import com.idega.core.contact.data.Email;
//import com.idega.core.contact.data.EmailHome;
import com.idega.core.contact.data.Phone;
//import com.idega.core.contact.data.PhoneHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;


/**
 * @author gimmi
 */
public class SchoolContentItemLinks extends SchoolContentItem {

	String _headerStyle;
	String _headerColor;
	String _headerFontClass;
	int _spaceBetween = 5;
	boolean _showManagementType = true;

	/**
	 * @see com.idega.block.school.presentation.SchoolContentItem#getObject()
	 */
	protected PresentationObject getObject() throws RemoteException {
		
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		
		int row = 1; /** breytti ut 0, vegna arrayIndexOutOfBounds Villu... */
		boolean useBreak = false;

				
//		uncommented by Kelly
//				
//		String manType = getSchoolBusiness(_iwc).getSchoolManagementTypeString(_school.getSchoolManagermentType());
//		if (manType != null) {
//	Hans bad um thetta... held eg, Gimmi			
//			table.add(getHeader(_iwrb.getLocalizedString("school.management_type","Management Type")+":"), 1, row);
//			++row;
//  We add this further down as "Management" 
//			table.add(getText(_iwrb.getLocalizedString(manType)), 1, row);
//			useBreak = true;
//		}
		
		useBreak = true;
		
		String address = _school.getSchoolAddress();
		String zipArea = _school.getSchoolZipArea();
		String zipCode = _school.getSchoolZipCode();
		String phone = _school.getSchoolPhone();
		String fax = _school.getSchoolFax();
		String mapUrl = _school.getMapUrl();
		String activity = _school.getActivity();
		String open_hours = _school.getOpenHours();
		String email = _school.getSchoolEmail();
		String visitaddress = _school.getSchoolVisitAddress();
		
		if ((address != null || zipArea != null || zipCode != null || phone != null || fax != null)) {
			if (useBreak) {
				++row;
				table.setHeight(row, _spaceBetween);
				++row;
			}
			/*
			table.add(getHeader(_iwrb.getLocalizedString("school.address","Address")+":"), 1, row);
			if (address != null) {
				++row;
				table.add(getText(address), 1, row);
			}*/
			if (visitaddress != null && !visitaddress.equals(" ")) {
				table.add(getHeader(_iwrb.getLocalizedString("school.visiting_address","Visiting address")+":"), 1, row);
				++row;
				table.add(getText(visitaddress), 1, row);
				++row;
				if (useBreak) {
					++row;
					table.setHeight(row, _spaceBetween);
					++row;
				}
			}
			
			table.add(getHeader(_iwrb.getLocalizedString("school.postal_address","Postal address")+":"), 1, row);
			if (address != null && !address.equals(" ")) {
				++row;
				table.add(getText(address), 1, row);
				++row;
			}		
			/*if (zipCode != null) {
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
			}*/
			if (zipCode != null && !zipCode.equals(" ")) {
				++row;
				table.add(getText(zipCode), 1, row);
				if (zipArea != null) {
					table.add(getText(" "+zipArea), 1, row);
				}
			}else {
				if (zipArea != null && !zipArea.equals(" ")) {
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
			/*if (email != null && !email.equals(" ")) {
				++row;
				table.add(getText(_iwrb.getLocalizedString("school.email","Email")+": "+email), 1, row);
			}*/
			if (email != null && !email.equals(" ")) {
				++row;
				Link linkEmail = new Link(getText(_iwrb.getLocalizedString("school.email","Email")), "mailto:"+email);
				table.add(linkEmail, 1, row);
			}
			if (mapUrl != null) {
				++row;
				Link link = new Link(getText(_iwrb.getLocalizedString("school.show_map","Show map")), mapUrl);
				link.setTarget(Link.TARGET_NEW_WINDOW);
				table.add(link, 1, row);
			}
			useBreak = true;
		}


		String webPage = _school.getSchoolWebPage();
		if (webPage != null) {
			if (useBreak) {
				++row;
				table.setHeight(row, _spaceBetween);
				++row;
			}
			/*Link link = new Link(getText(webPage), webPage);
			link.setTarget(Link.TARGET_NEW_WINDOW);
			table.add(getHeader(_iwrb.getLocalizedString("school.web_page","Web Page")+":"), 1, row);
			++row;
			table.add(link, 1, row);
			useBreak = true;
			*/
//			Link link = new Link(getText(webPage), webPage);
		  Link link = new Link(getText(_iwrb.getLocalizedString("school.school_home_page","Home page of the school")), webPage);
		  //table.add(getHeader(_iwrb.getLocalizedString("school.web_page","Web Page")+":"), 1, row);
		  link.setTarget(Link.TARGET_NEW_WINDOW);	
		  ++row;
		  table.add(link, 1, row);
		  useBreak = true;
		}

		if (useBreak) {
			++row;
			table.setHeight(row, _spaceBetween);
			++row;
		}

		// Moved the management type here  (Kelly)
		String manType = null;
		if (_school != null) {
			SchoolManagementType type = _school.getSchoolManagementType();
			if (type != null)
				manType = _iwrb.getLocalizedString(type.getLocalizedKey(), type.getName());
		}
		
		if (_showManagementType && manType != null) {
			table.add(getHeader(_iwrb.getLocalizedString("school.management_type","Management Type")+":"), 1, row);
			++row;
			table.add(getText(manType), 1, row);
		}
		if (useBreak) {
			++row;
			table.setHeight(row, _spaceBetween);
			++row;
		}

		// Added the activity here  (Kelly)
		if (activity != null) {
			table.add(getHeader(_iwrb.getLocalizedString("school.activity", "Activity")+":"), 1, row);
			++row;
			table.add(getText(activity), 1, row);
		}
		boolean isSchool = isElementarySchool();

		try {
			Collection hmUsers = getSchoolUserBusiness(_iwc).getHeadmasters(_school);
			if (hmUsers != null && !hmUsers.isEmpty()) {
//			int headmasterId = _school.getHeadmasterUserId();
//			if (headmasterId > 0 ) {
				if (useBreak) {
					++row;
					table.setHeight(row, _spaceBetween);
					++row;
				}
				if (isSchool) {
					table.add(getHeader(_iwrb.getLocalizedString("school.headmaster","Headmaster")+":"), 1, row);
				} else {
					table.add(getHeader(_iwrb.getLocalizedString("school.childcare_manager","Manager")+":"), 1, row);
				}
				//UserHome uHome = (UserHome) IDOLookup.getHome(User.class);
				Iterator iter = hmUsers.iterator();
				while (iter.hasNext()) {
					//User user = uHome.findByPrimaryKey(iter.next());
					User user = (User)iter.next();
					row = insertUser(table, row, user);
				}
				useBreak = true;
			}
		} catch (FinderException e) {
			e.printStackTrace(System.err);
		}
		try {
			Collection hmUsers = getSchoolUserBusiness(_iwc).getAssistantHeadmasters(_school);
			if (hmUsers != null && !hmUsers.isEmpty()) {
//			int headmasterId = _school.getHeadmasterUserId();
//			if (headmasterId > 0 ) {
				if (useBreak) {
					++row;
					table.setHeight(row, _spaceBetween);
					++row;
				}
				if (isSchool) {
					table.add(getHeader(_iwrb.getLocalizedString("school.assistant_headmaster","Assistant Headmaster")+":"), 1, row);
				} else {
					table.add(getHeader(_iwrb.getLocalizedString("school.childcare_assistant_manager","Assistant Manager")+":"), 1, row);
				}
				Iterator iter = hmUsers.iterator();
				while (iter.hasNext()) {
					User user = (User)iter.next();
					row = insertUser(table, row, user);
				}
				useBreak = true;
			}
		} catch (FinderException e) {
			e.printStackTrace(System.err);
		}
		if (useBreak) {
			++row;
			table.setHeight(row, _spaceBetween);
			++row;
		}
		if (open_hours != null) {
			table.add(getHeader(_iwrb.getLocalizedString("school.open_hours", "Open hours")+":"), 1, row);
			++row;
			table.add(getText(open_hours), 1, row);
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

	protected int insertUser(Table table, int row, User user) {
		++row;
		String name = user.getName();
		Collection emails = user.getEmails();
		if (emails != null) {
			Iterator eIter = emails.iterator();
			//EmailHome eHome = (EmailHome) IDOLookup.getHome(Email.class);
			Email email;
			Link link;
			int emSize = emails.size();
			if (emSize == 1) {
				//try {
					email = (Email) eIter.next();
					//email = eHome.findByPrimaryKey(eIter.next());
					link = new Link(getText(name), "mailto:"+email.getEmailAddress());
					table.add(link, 1, row);
				//} catch (FinderException e) {
				//	e.printStackTrace(System.err);
				//}
			}else if (emSize < 1) {
				table.add(name, 1, row);
			}else if (emSize > 1) {
				table.add(name, 1, row);
				while (eIter.hasNext()) {
					//try {
						email = (Email) eIter.next();
						//email = eHome.findByPrimaryKey(eIter.next());
						link = new Link(getText(email.getEmailAddress()), "mailto:"+email.getEmailAddress());
						++row;
						table.add(link, 1, row);
					//} catch (FinderException e) {
					//	e.printStackTrace(System.err);
					//}
				}
			}
		}
		
		Collection phones = user.getPhones();
		if (phones != null && phones.size() > 0) {
			Iterator pIter = phones.iterator();	
			//PhoneHome pHome = (PhoneHome) IDOLookup.getHome(Phone.class);
			Phone uPhone;
			int phCounter = 1;
			while (pIter.hasNext()) {
				//try {
					uPhone = (Phone) pIter.next();
					//uPhone = pHome.findByPrimaryKey(pIter.next());	
					++row;
					if (phCounter == 1) {
						table.add(getText(_iwrb.getLocalizedString("school.Tph","Tph")+": "+uPhone.getNumber()), 1, row);
					}
					if (phCounter >= 2) {
						table.add(getText(_iwrb.getLocalizedString("school.cell_phone","Mobil")+": "+uPhone.getNumber()), 1, row);
					}
					phCounter++;
				//} catch (FinderException e) {
				//	e.printStackTrace(System.err);
				//}
			}
		}
		return row;
	}

	private boolean isElementarySchool() {
		try {
			String category = getSchoolUserBusiness(_iwc).getSchoolCategory(_school);
			if (category.equalsIgnoreCase(getSchoolUserBusiness(_iwc).getSchoolBusiness().getElementarySchoolSchoolCategory())) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return false;
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
		if (_headerFontClass != null) {
			text.setFontClass(_headerFontClass);	
		}
		return text;
	}
	
	public void setHeaderStyle(String style) {
		_headerStyle = style;	
	}
	
	public void setHeaderFontClass(String fontClass) {
		_headerFontClass = fontClass;	
	}
	
	public void setHeaderColor(String color) {
		_headerColor = color;	
	}
	
	public void setSpaceBetween(int spaceBetween) {
		_spaceBetween = spaceBetween;
	}

	/*
	private UserBusiness getUserBusiness(IWApplicationContext iwac) throws RemoteException {
		return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
	}
	*/
	
	private SchoolUserBusiness getSchoolUserBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolUserBusiness) IDOLookup.getServiceInstance(iwac, SchoolUserBusiness.class);	
	}	
	/**
	 * @param managementType The _showManagementType to set.
	 */
	public void setShowManagementType(boolean managementType) {
		_showManagementType = managementType;
	}
}
