package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.SchoolDepartment;
import com.idega.block.school.data.SchoolManagementType;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneType;
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
		
		String address = this._school.getSchoolAddress();
		String zipArea = this._school.getSchoolZipArea();
		String zipCode = this._school.getSchoolZipCode();
		String phone = this._school.getSchoolPhone();
		String fax = this._school.getSchoolFax();
		String mapUrl = this._school.getMapUrl();
		String activity = this._school.getActivity();
		String open_hours = this._school.getOpenHours();
		String email = this._school.getSchoolEmail();
		String visitaddress = this._school.getSchoolVisitAddress();
		
		if ((address != null || visitaddress != null || zipArea != null || zipCode != null || phone != null || fax != null || email != null)) {
			if (useBreak) {
				++row;
				table.setHeight(row, this._spaceBetween);
				++row;
			}
			if (visitaddress != null && !visitaddress.equals(" ")) {
				table.add(getHeader(this._iwrb.getLocalizedString("school.visiting_address","Visiting address")+":"), 1, row);
				++row;
				table.add(getText(visitaddress), 1, row);
				++row;
				if (useBreak) {
					++row;
					table.setHeight(row, this._spaceBetween);
					++row;
				}
			}
			
			//table.add(getHeader(_iwrb.getLocalizedString("school.visiting_address","Visiting address")+":"), 1, row);
			table.add(getHeader(this._iwrb.getLocalizedString("school.postal_address","Postal address")+":"), 1, row);
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
				table.add(getText(this._iwrb.getLocalizedString("school.Tph","Tph")+": "+phone), 1, row);
			}
			if (fax != null && !fax.equals(" ")) {
				++row;
				table.add(getText(this._iwrb.getLocalizedString("school.fax","Fax")+": "+fax), 1, row);
			}
			if (email != null && !email.equals(" ")) {
				++row;
				Link linkEmail = new Link(getText(this._iwrb.getLocalizedString("school.email","Email")), "mailto:"+email);
				table.add(linkEmail, 1, row);
			}
			
			if (mapUrl != null && !mapUrl.equals(" ")) {
				++row;
				Link link = new Link(getText(this._iwrb.getLocalizedString("school.show_map","Show map")), mapUrl);
				link.setTarget(Link.TARGET_NEW_WINDOW);
				table.add(link, 1, row);
			}
			useBreak = true;
		}


		if (useBreak) {
			++row;
			table.setHeight(row, this._spaceBetween);
			++row;
		}

		
		if (useBreak) {
			++row;
			table.setHeight(row, this._spaceBetween);
			++row;
		}

		
		// Added the activity here  (Kelly)
		if (activity != null && (email != null) && !email.equals(" ")) {
			table.add(getHeader(this._iwrb.getLocalizedString("school.activity", "Activity")+":"), 1, row);
			++row;
			table.add(getText(activity), 1, row);
		}
		//boolean isSchool = isElementarySchool();

		try {
			Collection hmUsers = getSchoolUserBusiness(this._iwc).getMainHeadmasters(this._school);
			if (hmUsers != null && !hmUsers.isEmpty()) {
//			int headmasterId = _school.getHeadmasterUserId();
//			if (headmasterId > 0 ) {
				if (useBreak) {
					++row;
					table.setHeight(row, this._spaceBetween);
					++row;
				}
				//if (isSchool) {
					table.add(getHeader(this._iwrb.getLocalizedString("school.headmaster","Headmaster")+":"), 1, row);
					
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
			Collection sDepartments = getSchoolBusiness(this._iwc).getSchoolDepartmentHome().findAllDepartmentsBySchool(this._school);
			
			if (sDepartments != null && !sDepartments.isEmpty()) {
				Iterator depIter = sDepartments.iterator();
				
				try {
				Collection suTypes = getSchoolUserBusiness(this._iwc).getSchoolUserTypes(this._school);
				++row;
				if (useBreak) {
					++row;
					table.setHeight(row, this._spaceBetween);
					++row;
				}
				table.add(getHeader(this._iwrb.getLocalizedString("school.contact_us", "Contact us")+":"), 1, row);
				
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
							
								Collection users = getSchoolUserBusiness(this._iwc).getUsersByDepartm(this._school, Integer.parseInt(userType[2]), schDep.getDepartmentID());
								
							 if (users != null && users.size() > 0) {
								Iterator userIter = users.iterator();
								if (userCount == 1) {
									String depPhone = schDep.getDepartmentPhone();
									table.add(getText(schDep.getDepartment()), 1, row);
									++row;
									if (depPhone != null && !depPhone.equals("") && !depPhone.equals(" ")) {
										table.add(getText(this._iwrb.getLocalizedString("school.Tph","Tph")+": " + depPhone), 1, row);
									}
									++userCount;
								}
								while (userIter.hasNext()) {
									User user = (User) userIter.next();								
									show = getSchoolUserBusiness(this._iwc).getUserShowInContact(user);
									main_headmaster = getSchoolUserBusiness(this._iwc).getUserMainHeadmaster(user);
								
									if (show && !main_headmaster) {
										if (userType[2] != null && userType[2].equals("1")){
											row = insertUser(table, row, user, this._iwrb.getLocalizedString("school.assistant_headmaster_abbrev","Ass. headmaster"));										
										}
										else {
											row = insertUser(table, row, user, this._iwrb.getLocalizedString(userType[0],userType[1]));
										}
									}							
								}
							}	
							
						} //end while suTypes
					} //end if usertypes
					if (useBreak) {
							++row;
							table.setHeight(row, this._spaceBetween);
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
			table.setHeight(row, this._spaceBetween);
			++row;
		}
		if (open_hours != null) {
			table.add(getHeader(this._iwrb.getLocalizedString("school.open_hours", "Open hours")+":"), 1, row);
			++row;
			table.add(getText(open_hours), 1, row);
			++row;
		}

//		Moved the management type here  (Kelly)
			 String manType = null;
			 if (this._school != null) {
				 SchoolManagementType type = this._school.getSchoolManagementType();
				 if (type != null) {
					manType = this._iwrb.getLocalizedString(type.getLocalizedKey(), type.getName());
				}
			 }
		
			 if (manType != null) {
				if (useBreak) {
					++row;
					table.setHeight(row, this._spaceBetween);
					++row;
				}
				 table.add(getHeader(this._iwrb.getLocalizedString("school.management_type","Management Type")+":"), 1, row);
				 ++row;
				 table.add(getText(manType), 1, row);
			 }

		String webPage = this._school.getSchoolWebPage();
		if (webPage != null) {
			if (useBreak) {
				++row;
				table.setHeight(row, this._spaceBetween);
				++row;
			}
			//Link link = new Link(getText(webPage), webPage);
			Link link = new Link(getText(this._iwrb.getLocalizedString("school.school_home_page","The school's home page")), webPage);
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
			Email email;
			Link link;
			int emSize = emails.size();
			if (emSize == 1) {
				email = (Email) eIter.next();
				link = new Link(getText(name + userType), "mailto:"+email.getEmailAddress());
				table.add(link, 1, row);
			}else if (emSize < 1) {
				table.add(name, 1, row);
			}else if (emSize > 1) {
				table.add(name, 1, row);
				while (eIter.hasNext()) {
					email = (Email) eIter.next();
					link = new Link(getText(email.getEmailAddress()), "mailto:"+email.getEmailAddress());
					++row;
					table.add(link, 1, row);
				}
			}
		}
		
		Collection phones = user.getPhones();
		if (phones != null && phones.size() > 0) {
			Iterator pIter = phones.iterator();	
			Phone uPhone;
			int phCounter = 1;
			int phMobCounter = 1;
			while (pIter.hasNext()) {
				uPhone = (Phone) pIter.next();	
				
				if (uPhone.getPhoneTypeId() != this.mobilePhoneType){
					++row;
					if (phCounter == 1) {
						table.add(getText(this._iwrb.getLocalizedString("school.Tph","Tph")+": "+uPhone.getNumber()), 1, row);
					}
					if (phCounter >= 2) {
						table.add(getText(uPhone.getNumber()), 1, row);
					}	
					phCounter++;
				}
			}
			pIter = phones.iterator();	
			while (pIter.hasNext()) {
				uPhone = (Phone) pIter.next();	

				if (uPhone.getPhoneTypeId() == this.mobilePhoneType){
					++row;
					if (phMobCounter == 1) {
						table.add(getText(this._iwrb.getLocalizedString("school.cell_phone","Mobil")+": "+uPhone.getNumber()), 1, row);
					}
					if (phMobCounter >= 2) {
						table.add(getText(uPhone.getNumber()), 1, row);
					}
					phMobCounter++;
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
		if (this._headerStyle != null) {
			text.setFontStyle(this._headerStyle);
		}	else {
			text.setBold(true);	
		}
		if (this._headerColor != null) {
			text.setFontColor(this._headerColor);	
		}
		if (this._headerFontClass != null) {
			text.setFontClass(this._headerFontClass);	
		}
		return text;
	}

	
	public void setHeaderStyle(String style) {
		this._headerStyle = style;	
	}
	
	public void setHeaderFontClass(String fontClass) {
		this._headerFontClass = fontClass;	
	}
	
	public void setHeaderColor(String color) {
		this._headerColor = color;	
	}
	
	public void setSpaceBetween(int spaceBetween) {
		this._spaceBetween = spaceBetween;
	}

	/*	
	private UserBusiness getUserBusiness(IWApplicationContext iwac) throws RemoteException {
		return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
	}
	*/
	
	private SchoolUserBusiness getSchoolUserBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolUserBusiness) IBOLookup.getServiceInstance(iwac, SchoolUserBusiness.class);	
	}
}
