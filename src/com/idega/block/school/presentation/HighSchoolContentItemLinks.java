package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.SchoolDepartment;
import com.idega.block.school.data.SchoolManagementType;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.EmailHome;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneHome;
import com.idega.core.contact.data.PhoneType;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;




/**
 * @author gimmi
 */
public class HighSchoolContentItemLinks extends SchoolContentItem {

	String _headerStyle;
	String _headerColor;
	String _headerFontClass;
	int _spaceBetween = 5;
	private int mobilePhoneType = PhoneType.MOBILE_PHONE_ID;

	/**
	 * @see com.idega.block.school.presentation.SchoolContentItem#getObject()
	 */
	protected PresentationObject getObject() throws RemoteException {
		
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		//Malin
		//table.setWidth(200);
		//
		int row = 1; /** breytti ut 0, vegna arrayIndexOutOfBounds Villu... */
		boolean useBreak = false;
		boolean show = true;
		boolean main_headmaster = false;
				
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
		
		if ((address != null || visitaddress != null || zipArea != null || zipCode != null || phone != null || fax != null || email != null)) {
			if (useBreak) {
				++row;
				table.setHeight(row, _spaceBetween);
				++row;
			}
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
			
			//table.add(getHeader(_iwrb.getLocalizedString("school.visiting_address","Visiting address")+":"), 1, row);
			table.add(getHeader(_iwrb.getLocalizedString("school.postal_address","Postal address")+":"), 1, row);
			if (address != null && !address.equals(" ")) {
				++row;
				table.add(getText(address), 1, row);
				++row;
			}			
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
			if (phone != null && !phone.equals(" ")) {
				++row;
				table.add(getText(_iwrb.getLocalizedString("school.Tph","Tph")+": "+phone), 1, row);
			}
			if (fax != null && !fax.equals(" ")) {
				++row;
				table.add(getText(_iwrb.getLocalizedString("school.fax","Fax")+": "+fax), 1, row);
			}
			if (email != null && !email.equals(" ")) {
				++row;
				Link linkEmail = new Link(getText(_iwrb.getLocalizedString("school.email","Email")), "mailto:"+email);
				table.add(linkEmail, 1, row);
			}
			
			if (mapUrl != null && !mapUrl.equals(" ")) {
				++row;
				Link link = new Link(getText(_iwrb.getLocalizedString("school.show_map","Show map")), mapUrl);
				link.setTarget(Link.TARGET_NEW_WINDOW);
				table.add(link, 1, row);
			}
			useBreak = true;
		}


		if (useBreak) {
			++row;
			table.setHeight(row, _spaceBetween);
			++row;
		}

		
		if (useBreak) {
			++row;
			table.setHeight(row, _spaceBetween);
			++row;
		}

		
		// Added the activity here  (Kelly)
		if (activity != null && (email != null) && !email.equals(" ")) {
			table.add(getHeader(_iwrb.getLocalizedString("school.activity", "Activity")+":"), 1, row);
			++row;
			table.add(getText(activity), 1, row);
		}
		//boolean isSchool = isElementarySchool();

		try {
			Collection hmUsers = getSchoolUserBusiness(_iwc).getMainHeadmasters(_school);
			if (hmUsers != null && !hmUsers.isEmpty()) {
//			int headmasterId = _school.getHeadmasterUserId();
//			if (headmasterId > 0 ) {
				if (useBreak) {
					++row;
					table.setHeight(row, _spaceBetween);
					++row;
				}
				//if (isSchool) {
					table.add(getHeader(_iwrb.getLocalizedString("school.headmaster","Headmaster")+":"), 1, row);
					
				//} else {
				//	table.add(getHeader(_iwrb.getLocalizedString("school.childcare_manager","Manager")+":"), 1, row);
				//}
				//UserHome uHome = (UserHome) IDOLookup.getHome(User.class);
				Iterator iter = hmUsers.iterator();
				while (iter.hasNext()) {
					//User user = uHome.findByPrimaryKey(iter.next());
					User user = (User)iter.next();
					row = insertUser(table, row, user, "");  // Main headmaster
				}
				useBreak = true;
			}
		} catch (FinderException e) {
			e.printStackTrace(System.err);
		}
		try {

/////////
			Collection sDepartments = getSchoolBusiness(_iwc).getSchoolDepartmentHome().findAllDepartmentsBySchool(_school);
			
			if (sDepartments != null && !sDepartments.isEmpty()) {
				Iterator depIter = sDepartments.iterator();
				
				try {
				Collection suTypes = getSchoolUserBusiness(_iwc).getSchoolUserTypes(_school);
				++row;
				if (useBreak) {
					++row;
					table.setHeight(row, _spaceBetween);
					++row;
				}
				table.add(getHeader(_iwrb.getLocalizedString("school.contact_us", "Contact us")+":"), 1, row);
				
					while (depIter.hasNext()) {
						++row;
						SchoolDepartment schDep = (SchoolDepartment) depIter.next();
						/*
						table.add(getText(schDep.getDepartment()), 1, row);
						++row;
						table.add(getText(_iwrb.getLocalizedString("school.Tph","Tph")+": " + schDep.getDepartmentPhone()), 1, row);
						*/
					 	if (suTypes != null && !suTypes.isEmpty()) {
							String[] userType;
							Iterator iter = suTypes.iterator();
							int userCount = 1;			
							while (iter.hasNext()) {
								userType = (String[]) iter.next();
							
								Collection users = getSchoolUserBusiness(_iwc).getUsersByDepartm(_school, Integer.parseInt(userType[2]), schDep.getDepartmentID());
								
							 if (users != null && users.size() > 0) {
								Iterator userIter = users.iterator();
								if (userCount == 1) {
									String depPhone = schDep.getDepartmentPhone();
									table.add(getText(schDep.getDepartment()), 1, row);
									++row;
									if (depPhone != null && !depPhone.equals("") && !depPhone.equals(" ")) {
										table.add(getText(_iwrb.getLocalizedString("school.Tph","Tph")+": " + depPhone), 1, row);
									}
									++userCount;
								}
								while (userIter.hasNext()) {
									User user = (User) userIter.next();								
									show = getSchoolUserBusiness(_iwc).getUserShowInContact(user);
									main_headmaster = getSchoolUserBusiness(_iwc).getUserMainHeadmaster(user);
								
									if (show && !main_headmaster) {
										if (userType[2] != null && userType[2].equals("1")){
											row = insertUser(table, row, user, _iwrb.getLocalizedString("school.assistant_headmaster_abbrev","Ass. headmaster"));										
										}
										else {
											row = insertUser(table, row, user, _iwrb.getLocalizedString(userType[0],userType[1]));
										}
									}							
								}
							}	
							
						} //end while suTypes
					} //end if usertypes
					if (useBreak) {
							++row;
							table.setHeight(row, _spaceBetween);
							++row;
					}
				} // end while	
			} //end try
				catch (IDORelationshipException e) {
					e.printStackTrace(System.err);
				}
			
			//}
////////////			
			/*Collection hmUsers = getSchoolUserBusiness(_iwc).getAssistantHeadmasters(_school);
			if (hmUsers != null && !hmUsers.isEmpty()) {
//			int headmasterId = _school.getHeadmasterUserId();
//			if (headmasterId > 0 ) {
				if (useBreak) {
					++row;
					table.setHeight(row, _spaceBetween);
					++row;
				}
				table.add(getHeader(_iwrb.getLocalizedString("school.contact_us", "Contact us")+":"), 1, row);
			
				Iterator iter = hmUsers.iterator();
				while (iter.hasNext()) {
					User user = (User)iter.next();
					row = insertUser(table, row, user);
				}
				useBreak = true;
				*/
			} // end if dep
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
			++row;
		}

//		Moved the management type here  (Kelly)
			 String manType = null;
			 if (_school != null) {
				 SchoolManagementType type = _school.getSchoolManagementType();
				 if (type != null)
					 manType = _iwrb.getLocalizedString(type.getLocalizedKey(), type.getName());
			 }
		
			 if (manType != null) {
				if (useBreak) {
					++row;
					table.setHeight(row, _spaceBetween);
					++row;
				}
				 table.add(getHeader(_iwrb.getLocalizedString("school.management_type","Management Type")+":"), 1, row);
				 ++row;
				 table.add(getText(manType), 1, row);
			 }

		String webPage = _school.getSchoolWebPage();
		if (webPage != null) {
			if (useBreak) {
				++row;
				table.setHeight(row, _spaceBetween);
				++row;
			}
			//Link link = new Link(getText(webPage), webPage);
			Link link = new Link(getText(_iwrb.getLocalizedString("school.school_home_page","The school's home page")), webPage);
			//table.add(getHeader(_iwrb.getLocalizedString("school.web_page","Web Page")+":"), 1, row);
			link.setTarget(Link.TARGET_NEW_WINDOW);	
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

	
	protected int insertUser(Table table, int row, User user, String userType) throws RemoteException {
		++row;
		String name = user.getName();
		Collection emails = user.getEmails();
		if (userType != "") {
			userType =  ", " + userType;
		}
		
		if (emails != null) {
			Iterator eIter = emails.iterator();
			EmailHome eHome = (EmailHome) IDOLookup.getHome(Email.class);
			Email email;
			Link link;
			int emSize = emails.size();
			if (emSize == 1) {
				try {
					email = eHome.findByPrimaryKey(eIter.next());
					link = new Link(getText(name + userType), "mailto:"+email.getEmailAddress());
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
			int phCounter = 1;
			int phMobCounter = 1;
			while (pIter.hasNext()) {
				uPhone = (Phone) pIter.next();	
				
				if (uPhone.getPhoneTypeId() != mobilePhoneType){
					++row;
					if (phCounter == 1) {
						table.add(getText(_iwrb.getLocalizedString("school.Tph","Tph")+": "+uPhone.getNumber()), 1, row);
					}
					if (phCounter >= 2) {
						table.add(getText(uPhone.getNumber()), 1, row);
					}	
					phCounter++;
				}
			}
			pIter = phones.iterator();	
			while (pIter.hasNext()) {
			try {
				uPhone = pHome.findByPrimaryKey(pIter.next());	

				if (uPhone.getPhoneTypeId() == mobilePhoneType){
					++row;
					if (phMobCounter == 1) {
						table.add(getText(_iwrb.getLocalizedString("school.cell_phone","Mobil")+": "+uPhone.getNumber()), 1, row);
					}
					if (phMobCounter >= 2) {
						table.add(getText(uPhone.getNumber()), 1, row);
					}
					phMobCounter++;
				} 

			} catch (FinderException e) {
				e.printStackTrace(System.err);
			}
			}
		}
		return row;
	}

	/*
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
	*/
		
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
}
