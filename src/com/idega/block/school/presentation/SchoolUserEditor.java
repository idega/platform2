package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.login.presentation.LoginEditor;
import com.idega.block.login.presentation.LoginEditorWindow;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolContentBusinessBean;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.EmailHome;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.TextFormat;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

/**
 * @author gimmi
 */
public class SchoolUserEditor extends Block {
	
	TextFormat _tFormat;
	IWResourceBundle _iwrb;
	protected School _school;
  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";
  
  String PARAMETER_ACTION = "sue_act";
  String ACTION_VIEW_SCHOOL = "sue_pvs";
  String ACTION_UPDATE = "sue_up_usrs";
  //String PARAMETER_SCHOOL_ID = "sue_sId";
	String PARAMETER_SCHOOL_ID = SchoolContentBusinessBean.PARAMETER_SCHOOL_ID;
	
	private String PARAMETER_SCHOOL_USER_NAME = "sue_un";
	private String PARAMETER_SCHOOL_USER_TELEPHONE = "sue_utf";
	private String PARAMETER_SCHOOL_USER_CELLPHONE = "sue_ucf";
	private String PARAMETER_SCHOOL_USER_EMAIL = "sue_uem";
	private String PARAMETER_SCHOOL_USER_ID = "sue_uid";
	private String PARAMETER_SCHOOL_USER_TYPE = "sue_sut";
	
	private String PARAMETER_EDIT_USER = "sue_eds";
	private String PARAMETER_DELTE_USER = "sue_dls";

	private Text TEXT_NORMAL;
	private Text TEXT_TITLE;
	private String INPUT_STYLE;
	
	private int userToEdit = -1;
	private List parameterNames;
	private List parameterValues;
	private Collection schoolTypeIds;

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
  
  public SchoolUserEditor(){
  	
  }
  
  
  
  private Table schoolList(IWContext iwc) throws RemoteException{
		Collection schools = new java.util.Vector(0);
		if (schoolTypeIds == null || schoolTypeIds.isEmpty()) {
			schools = getSchoolBusiness(iwc).findAllSchools();
		}else {
			schools = getSchoolBusiness(iwc).findAllSchoolsByType(schoolTypeIds);	
		}
//  	Collection schools = getSchoolBusiness(iwc).findAllSchools();
  	Table table = new Table();
  	int row = 0;
 	
  	if (schools != null) {
  		Iterator iter = schools.iterator();
  		Link link;
  		School school;
  		int textFormatType = 1;
  		while ( iter.hasNext() ) {
  			++row;
					school = (School)(iter.next());
	  			link = getLink(_tFormat.format(_iwrb.getLocalizedString("school.edit","edit"),TextFormat.NORMAL), ACTION_VIEW_SCHOOL, false);
	  			link.addParameter(this.PARAMETER_SCHOOL_ID, school.getPrimaryKey().toString());
	  			table.add(link, 1, row);
					if (school.equals(_school)) {
						textFormatType = TextFormat.HEADER;
					}else {
						textFormatType = TextFormat.NORMAL;
					}
	  			table.add(_tFormat.format(school.getName(),textFormatType), 2, row);
  		}
  	}

  	return table;
  }
	private Form schoolUsers(IWContext iwc, School school) throws RemoteException {
		Form form = new Form();
		form.add(schoolUsersTable(iwc, school, true));
		form.maintainParameter(PARAMETER_SCHOOL_ID);
		if (parameterNames != null) {
			int pnLength = parameterNames.size();
			for (int i = 0; i < pnLength; i++) {
				form.addParameter((String) parameterNames.get(i), (String) parameterValues.get(i));
			}
		}
		return form;
	}

	private Text getTextNormal(String content) {
		if (TEXT_NORMAL == null) {
			return _tFormat.format(content, TextFormat.NORMAL);
		}else {
			Text text = (Text) TEXT_NORMAL.clone();
			text.setText(content);
			return text;	
		}
	}

	private Text getTextTitle(String content) {
		if (TEXT_TITLE == null) {
			return _tFormat.format(content, TextFormat.TITLE);
		}else {
			Text text = (Text) TEXT_TITLE.clone();
			text.setText(content);
			return text;	
		}
	}

	private Link getLink(Text text, String action, boolean maintainSchoolId) {
		Link link = new Link(text);
		link.addParameter(PARAMETER_ACTION, action);
		if (_school != null && maintainSchoolId) {
			link.addParameter(PARAMETER_SCHOOL_ID, _school.getPrimaryKey().toString());	
		}
		if (parameterNames != null) {
			int pnLength = parameterNames.size();
			for (int i = 0; i < pnLength; i++) {
				link.addParameter((String) parameterNames.get(i), (String) parameterValues.get(i));
			}
		}
		return link;
	}

	private void setTextInputStyle(TextInput input) {
		if (this.INPUT_STYLE != null) {
      		input.setMarkupAttribute("style",INPUT_STYLE);
		}
	}

	private Table schoolUsersTable(IWContext iwc, School school, boolean addSubmitButton) throws RemoteException {
		Table contTable = new Table();
		try {
			
			int cRow = 0;
			Collection suTypes = getSchoolUserBusiness(iwc).getSchoolUserTypes(school);
			if (suTypes != null && !suTypes.isEmpty()) {
				String[] userType;
				Iterator iter = suTypes.iterator();
				while (iter.hasNext()) {
					userType = (String[]) iter.next();
					++cRow;	

					contTable.add(getTextTitle(_iwrb.getLocalizedString(userType[0],userType[1])), 1, cRow);
					Collection users = getSchoolUserBusiness(iwc).getUsers(school, Integer.parseInt(userType[2]));
					if (users != null && users.size() > 0) {
						Iterator userIter = users.iterator();
						Table table = new Table();
						int row = 1;
						while (userIter.hasNext()) {
							User hm = (User) userIter.next();
						
							int userId = ((Integer) hm.getPrimaryKey()).intValue();
							if (userId == userToEdit) {
								row = insertEditableUserIntoTable(table, hm, Integer.parseInt(userType[2]), row);
							}else {
								row = insertUserIntoTable(table, hm, row);
							}
						}
						contTable.add(table, 1, ++cRow);
					}
				}
			} else {
				cRow = 1;	
			}
/*
			contTable.add(getTextTitle(_iwrb.getLocalizedString("school.headmaster","Headmaster")), 1, 1);
			Collection users = getSchoolUserBusiness(iwc).getHeadmasters(school);
			if (users != null && users.size() > 0) {
				Iterator iter = users.iterator();
				Table table = new Table();
				int row = 1;
				while (iter.hasNext()) {
					User hm = uHome.findByPrimaryKey(iter.next());
					int userId = ((Integer) hm.getPrimaryKey()).intValue();
					if (userId == userToEdit) {
						row = insertEditableUserIntoTable(table, hm, SchoolUserBusinessBean.USER_TYPE_HEADMASTER, row);
					}else {
						row = insertUserIntoTable(table, hm, row);
					}
				}
				contTable.add(table, 1, 2);
			}

			contTable.add(getTextTitle(_iwrb.getLocalizedString("school.assistant_headmaster","Assistant headmaster")), 1, 3);
			users = getSchoolUserBusiness(iwc).getAssistantHeadmasters(school);
			if (users != null && users.size() > 0) {
				Iterator iter = users.iterator();
				Table table = new Table();
				int row = 1;
				while (iter.hasNext()) {
					User hm = uHome.findByPrimaryKey(iter.next());
					int userId = ((Integer) hm.getPrimaryKey()).intValue();
					if (userId == userToEdit) {
						row = insertEditableUserIntoTable(table, hm, SchoolUserBusinessBean.USER_TYPE_ASSISTANT_HEADMASTER, row);
					}else {
						row = insertUserIntoTable(table, hm, row);
					}
//						row = insertUserIntoTable(table, row, hm);
				}
				contTable.add(table, 1, 4);
			}

			contTable.add(getTextTitle(_iwrb.getLocalizedString("school.web_administrators","Web administrators")), 1, 5);
			users = getSchoolUserBusiness(iwc).getWebAdmins(school);
			if (users != null && users.size() > 0) {
				Iterator iter = users.iterator();
				Table table = new Table();
				int row = 1;
				while (iter.hasNext()) {
					User hm = uHome.findByPrimaryKey(iter.next());
					int userId = ((Integer) hm.getPrimaryKey()).intValue();
					if (userId == userToEdit) {
						row = insertEditableUserIntoTable(table, hm, SchoolUserBusinessBean.USER_TYPE_WEB_ADMIN, row);
					}else {
						row = insertUserIntoTable(table, hm, row);
					}
//						row = insertUserIntoTable(table, row, hm);
				}
				contTable.add(table, 1, 5);
			}
				
			contTable.add(getTextTitle(_iwrb.getLocalizedString("school.teachers","Teachers")), 1, 6);
			users = getSchoolUserBusiness(iwc).getTeachers(school);
			if (users != null && users.size() > 0) {
				Iterator iter = users.iterator();
				Table table = new Table();
				int row = 1;
				while (iter.hasNext()) {
					User hm = uHome.findByPrimaryKey(iter.next());
					int userId = ((Integer) hm.getPrimaryKey()).intValue();
					if (userId == userToEdit) {
						row = insertEditableUserIntoTable(table, hm, SchoolUserBusinessBean.USER_TYPE_TEACHER, row);
					}else {
						row = insertUserIntoTable(table, hm, row);
					}
//						row = insertUserIntoTable(table, row, hm);
				}
				contTable.add(table, 1, 7);
			}
			*/
			/** ATH SETJA USERA I GROUPUR, SEM HAEGT ER AD SETJA IB_PAGE_ID A.... EKKI GLEYMA THESSU */
			/** VIRKAR !!! HURRA  
			String rui = iwc.getParameter("repp_user_id");
			contTable.add("rui : " +rui , 1, 7);
			UserChooser uc = new UserChooser("repp_user_id");
			uc.setValidUserPks(users);
			contTable.add(uc, 1, 7);
			*/
						
			/** Empty User field */
			Table table = this.getUserForm(iwc, school);

			contTable.add(table, 1, cRow);

			if (addSubmitButton) {
				SubmitButton update = new SubmitButton(_iwrb.getLocalizedImageButton("school.save","Save"), PARAMETER_ACTION, ACTION_UPDATE);
				contTable.add(update, 1, cRow);
			}
			
		} catch (FinderException e) {
			e.printStackTrace(System.err);
		} catch (IDORelationshipException e) {
			e.printStackTrace(System.err);
		}
		return contTable;
	}

	
	private int insertUserIntoTable(Table table, User hm, int row) throws RemoteException {
		Collection emails;
		Collection phones;
		int uRow;
		int mRow;
		table.setWidth(2, "5");
		table.setWidth(4, "5");
		table.setWidth(6, "5");
		table.setWidth(8, "5");
		table.setWidth(10, "5");

		uRow = row;
		String hmId = hm.getPrimaryKey().toString();
		emails = hm.getEmails();
		phones = hm.getPhones();

		Text tName = getTextNormal(hm.getName());
		Link login = new Link(getTextNormal(_iwrb.getLocalizedString("school.login","Login")));
		login.setWindowToOpen(LoginEditorWindow.class);
		login.addParameter(LoginEditor.prmUserId, hmId);
		Link edit = getLink(getTextNormal(_iwrb.getLocalizedString("school.edit","Edit")), ACTION_VIEW_SCHOOL, true);
		edit.addParameter(PARAMETER_EDIT_USER, hmId);
		Link delete = getLink(getTextNormal(_iwrb.getLocalizedString("school.delete","Delete")), ACTION_VIEW_SCHOOL, true);
		delete.addParameter(PARAMETER_DELTE_USER, hmId);
		
		
		table.add(tName, 1, row);
		table.add(edit, 7, row);
		table.add(login, 9, row);
		table.add(delete, 11, row);
		
		if (emails != null) {
			Email email;
			Iterator iEm = emails.iterator();
			while (iEm.hasNext()) {
				email = (Email) iEm.next();
				table.add(getTextNormal(email.getEmailAddress() ), 3, row);
				++row;
			}
		}
		
		mRow = row;
		row = uRow;
		if (phones != null) {
			Phone phone;
			Iterator iPh = phones.iterator();
			while (iPh.hasNext()) {
				phone = (Phone) iPh.next();
				table.add(getTextNormal(phone.getNumber()), 5, row);
				++row;
			}
		}
		if (row >= mRow) {
			++row;
		}else {
			row = mRow + 1;
		}
		return row;
	}



	private int insertEditableUserIntoTable(Table table, User hm, int userType, int row) throws RemoteException {
		
		String sname = PARAMETER_SCHOOL_USER_NAME;
		String semail = PARAMETER_SCHOOL_USER_EMAIL;
		String sphone = PARAMETER_SCHOOL_USER_TELEPHONE;
		String sid = PARAMETER_SCHOOL_USER_ID;

		Collection emails;
		Collection phones;
		int uRow = row;
		int mRow;
		
							String hmId = hm.getPrimaryKey().toString();
							emails = hm.getEmails();
							phones = hm.getPhones();
							HiddenInput inp = new HiddenInput(sid, hmId);
							HiddenInput utInp = new HiddenInput(PARAMETER_SCHOOL_USER_TYPE+"_"+hmId, Integer.toString(userType));
							TextInput pName = new TextInput(sname+"_"+hmId, hm.getName());
							this.setTextInputStyle(pName);
							Link login = new Link(getTextNormal(_iwrb.getLocalizedString("school.login","Login")));
							login.setWindowToOpen(LoginEditorWindow.class);
							login.addParameter(LoginEditor.prmUserId, hmId);
							
							
							
							table.add(inp, 1, row);
							table.add(utInp, 1, row);
							table.add(login, 7, row);
							
							table.add(pName, 1, row);
							if (emails != null) {
								Email email;
								Iterator iEm = emails.iterator();
								while (iEm.hasNext()) {
									email = (Email) iEm.next();
									TextInput pEmail = new TextInput(semail+"_"+hmId+"_"+email.getPrimaryKey() , email.getEmailAddress() );
									this.setTextInputStyle(pEmail);
									table.add(pEmail, 3, row);
									++row;
								}
							}
							TextInput pEmail = new TextInput(semail+"_"+hmId );
							this.setTextInputStyle(pEmail);
							table.add(pEmail, 3, row);
		//					this.addLeft(_iwrb.getLocalizedString("school.add_email","Add E-mail"), pEmail, true);
		
							mRow = row;
							row = uRow;
							if (phones != null) {
								Phone phone;
								Iterator iPh = phones.iterator();
								while (iPh.hasNext()) {
									phone = (Phone) iPh.next();
									TextInput pPhone = new TextInput(sphone+"_"+hmId+"_"+phone.getPrimaryKey(), phone.getNumber());
									this.setTextInputStyle(pPhone);
									table.add(pPhone, 5, row);
									++row;
								}
							}
							TextInput pPhone = new TextInput(sphone+"_"+hmId);
							this.setTextInputStyle(pPhone);
							table.add(pPhone, 5, row);
		//					this.addLeft(_iwrb.getLocalizedString("school.add_phone","Add Phone"), pPhone, true);
							if (row >= mRow) {
								++row;
							}else {
								row = mRow + 1;
							}
		return row;
	}

	/**
	 * Returns a UserForm	 * @param userType 1 = Headmaster, 2 = Assistant Headmaster, 3 = User	 * @return Table	 *
	 */
	
	private Table getUserForm(IWContext iwc, School school) {
		String name = PARAMETER_SCHOOL_USER_NAME;
		String email = PARAMETER_SCHOOL_USER_EMAIL;
		String phone = PARAMETER_SCHOOL_USER_TELEPHONE;
		
		Table table = new Table();
		
		Text tName = getTextNormal(_iwrb.getLocalizedString("school.name","Name"));
		Text tEmail = getTextNormal(_iwrb.getLocalizedString("school.email","E-post"));
		Text tPhone = getTextNormal(_iwrb.getLocalizedString("school.phone","Phone"));
		Text tType = getTextNormal(_iwrb.getLocalizedString("school.type","Type"));
		
		
		DropdownMenu pType = new DropdownMenu(PARAMETER_SCHOOL_USER_TYPE);
		Collection suTypes;
		try {
			suTypes = getSchoolUserBusiness(iwc).getSchoolUserTypes(school);
			if (suTypes != null && !suTypes.isEmpty() ) {
				Iterator iter = suTypes.iterator();
				String[] str;
				while (iter.hasNext()) {
					str = (String[]) iter.next();
					pType.addMenuElement(str[2], _iwrb.getLocalizedString(str[0],str[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
//			pType.addMenuElement(SchoolUserBusinessBean.USER_TYPE_HEADMASTER , _iwrb.getLocalizedString("headmaster","Headmaster"));
//			pType.addMenuElement(SchoolUserBusinessBean.USER_TYPE_ASSISTANT_HEADMASTER , _iwrb.getLocalizedString("assistant_headmaster","Assistant headmaster"));
//			pType.addMenuElement(SchoolUserBusinessBean.USER_TYPE_WEB_ADMIN, _iwrb.getLocalizedString("web_administrator","Web administrator"));
//			pType.addMenuElement(SchoolUserBusinessBean.USER_TYPE_TEACHER , _iwrb.getLocalizedString("teacher","Teacher"));
		
		TextInput pName = new TextInput(name);
		TextInput pEmail = new TextInput(email);
		TextInput pPhone = new TextInput(phone);
		
		this.setTextInputStyle(pName);
		this.setTextInputStyle(pEmail);
		this.setTextInputStyle(pPhone);
		
		table.add(tType, 1, 1);
		table.add(pType, 1, 2);
		
		table.add(tName, 2, 1);
		table.add(pName, 2, 2);
		
		table.add(tEmail, 3, 1);
		table.add(pEmail, 3, 2);

		table.add(tPhone, 4, 1);
		table.add(pPhone, 4, 2);


		return table;
	}

	public void deleteUser(IWContext iwc, School school) throws RemoteException, FinderException {
		String uId = iwc.getParameter(PARAMETER_DELTE_USER);
		if (uId != null) {
			UserHome userHome = (UserHome) IDOLookup.getHome(User.class);
			User user = userHome.findByPrimaryKey(new Integer(uId));
			try {
				if (iwc == null)    System.out.println("[SchoolUserEditor:deleteUser] iwc    == null");
				if (user == null)   System.out.println("[SchoolUserEditor:deleteUser] user   == null");
				if (school == null) System.out.println("[SchoolUserEditor:deleteUser] school == null");
//      		get current user (added by Thomas)
		        User currentUser = iwc.getCurrentUser();      
				getSchoolUserBusiness(iwc).removeUser(school, user, currentUser);
//				user.remove();
			} catch (RemoveException e) {
				System.out.println("user to delete ERROR");
				e.printStackTrace(System.err);
			}
		}
		
	}


	public boolean updateUsers(IWContext iwc, School school) throws RemoteException {
		String sname = PARAMETER_SCHOOL_USER_NAME;
		String semail = PARAMETER_SCHOOL_USER_EMAIL;
		String sphone = PARAMETER_SCHOOL_USER_TELEPHONE;
		String sid = PARAMETER_SCHOOL_USER_ID;
		
		String sUserType = iwc.getParameter(PARAMETER_SCHOOL_USER_TYPE);
		int iUserType = Integer.parseInt(sUserType);
		String category = getSchoolUserBusiness(iwc).getSchoolCategory(school);
		Group priGroup = null;
		try {
			if (category.equalsIgnoreCase(getSchoolUserBusiness(iwc).getSchoolBusiness().getElementarySchoolSchoolCategory()))
				priGroup = getSchoolBusiness(iwc).getRootSchoolAdministratorGroup();
			else if (category.equalsIgnoreCase(getSchoolUserBusiness(iwc).getSchoolBusiness().getChildCareSchoolCategory()))
				priGroup = getSchoolBusiness(iwc).getRootProviderAdministratorGroup();
		}
		catch (CreateException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e1) {
			e1.printStackTrace();
		}

		/** Updateing headmasters */
		try {
			String hId = iwc.getParameter(sid);
			
			if (hId != null && priGroup != null) {
				UserHome userHome = (UserHome) IDOLookup.getHome(User.class);
				User user;
				user = userHome.findByPrimaryKey(new Integer(hId));

					String name         = iwc.getParameter(sname+"_"+hId);
					sUserType = iwc.getParameter(PARAMETER_SCHOOL_USER_TYPE+"_"+hId);
					iUserType = Integer.parseInt(sUserType);
					
					if (name.equals("")) {
						try {
              				// get current user (added by Thomas)
              				User currentUser = iwc.getCurrentUser();
							getSchoolUserBusiness(iwc).removeUser(school, user, currentUser);
						} catch (RemoveException e) {
							e.printStackTrace(System.err);
						}
					}else {
						getUserBusiness(iwc).updateUser(user, name, "", "", null, null, null, null, null, (Integer) priGroup.getPrimaryKey());

						try {
							getSchoolUserBusiness(iwc).setUserGroups(school, user, iUserType);
						} catch (Exception e) {
							debug("User already in headmasterGroup");
						}
						
						Collection emails = user.getEmails();
						Collection phones =	user.getPhones();
						
						if (emails != null) {
							user.removeAllEmails();
							Email email;
							Iterator iter = emails.iterator();
							while (iter.hasNext()) {
								Object prK = iter.next();
								String sEmail = iwc.getParameter(semail+"_"+hId+"_"+prK);
								if (sEmail != null && !sEmail.equals("")) {
									email = (Email) prK;
									email.setEmailAddress(sEmail);
									email.store();	
									user.addEmail(email);
								}
							}	
						}
			
						if (phones != null) {
							user.removeAllPhones();
							Phone phone;
							Iterator iter = phones.iterator();
							while (iter.hasNext()) {
								Object prK = iter.next();
								String sPhone = iwc.getParameter(sphone+"_"+hId+"_"+prK);
								if (sPhone != null && !sPhone.equals("")) {
									phone = (Phone) prK;
									phone.setNumber(sPhone);
									phone.store();	
									user.addPhone(phone);
								}
							}	
						}
						
						String newEmail  = iwc.getParameter(semail+"_"+hId);
						String newPhone  = iwc.getParameter(sphone+"_"+hId);
						
						if (newEmail != null && !newEmail.equals("")) {
							Email email = ((EmailHome) IDOLookup.getHome(Email.class)).create();
							email.setEmailAddress(newEmail);
							email.store();
							user.addEmail(email);
						}
			
						if (newPhone != null && !newPhone.equals("")) {
							Phone phone = ((PhoneHome) IDOLookup.getHome(Phone.class)).create();
							phone.setNumber(newPhone);
							phone.store();
							user.addPhone(phone);
						}
						postSaveUpdate(school, user, iUserType);
					}
			
			}
			
			
			/** Adding headmaster */
			String headmaster = iwc.getParameter(sname);
			String hmEmail    = iwc.getParameter(semail);
			String hmPhone    = iwc.getParameter(sphone);
			
			if (headmaster != null && !headmaster.equals("") && priGroup != null) {
				User user = getUserBusiness(iwc).createUser(headmaster, "","", ((Integer)priGroup.getPrimaryKey()).intValue());
//				getSchoolUserBusiness(iwc).addWebAdmin(school, user);
//				getSchoolBusiness(iwc).addHeadmaster(school, user);
				
				if (hmEmail != null && !hmEmail.equals("")) {
					Email email = ((EmailHome) IDOLookup.getHome(Email.class)).create();
					email.setEmailAddress(hmEmail);
					email.store();
					user.addEmail(email);
				}
			
				if (hmPhone != null && !hmPhone.equals("")) {
					Phone phone = ((PhoneHome) IDOLookup.getHome(Phone.class)).create();
					phone.setNumber(hmPhone);
					phone.store();
					user.addPhone(phone);
				}


				getSchoolUserBusiness(iwc).addUser(school, user, iUserType);
				postSaveNew(school, user, iUserType);
			}
			
			return true;
		} catch (IDORemoveRelationshipException e) {
			e.printStackTrace(System.err);
		} catch (IDOAddRelationshipException e) {
			e.printStackTrace(System.err);
		} catch (FinderException e) {
			e.printStackTrace(System.err);
		} catch (CreateException e) {
			e.printStackTrace(System.err);
		}
		return false;
	}

	/** 
	 * Override me please.
	 * @param school School
	 * @param user User
	 */
	protected void postSaveNew(School school, User user, int userType) throws RemoteException{

	}

	/** 
	 * Override me please.
	 * @param school School
	 * @param user User
	 */
	protected void postSaveUpdate(School school, User user, int userType) throws RemoteException{

	}
	
	private Table mainForm(IWContext iwc) throws RemoteException {
		Table table = new Table(2, 2);
		table.add(getTextTitle(_iwrb.getLocalizedString("school.select_school","Select School")), 1, 1);
		table.add(getTextTitle(_school.getName()), 2, 1);
		table.add(schoolList(iwc), 1, 2);
		table.add(schoolUsers(iwc, _school), 2, 2);
		table.setVerticalAlignment(1, 2, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(2, 2, Table.VERTICAL_ALIGN_TOP);
		return table;
	}
	

	protected UserBusiness getUserBusiness(IWApplicationContext iwac) throws RemoteException {
		return  (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
	}

	protected SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}

	protected SchoolHome getSchoolHome() throws RemoteException {
		return (SchoolHome) IDOLookup.getHome(School.class);	
	}
  
	public Table getSchoolUsersTable(IWContext iwc, School school, boolean addSubmitButton) throws RemoteException{
		_school = school;
		return schoolUsersTable(iwc, school, addSubmitButton);	
	}

  private void init(IWContext iwc) throws RemoteException {
  	_iwrb = super.getResourceBundle(iwc);
  	_tFormat = TextFormat.getInstance();
  	
  	String schoolId = iwc.getParameter(PARAMETER_SCHOOL_ID);
  	if (schoolId != null) {
  		try {
	  		_school = getSchoolHome().findByPrimaryKey(new Integer(schoolId));
  		}catch (FinderException e){
  			e.printStackTrace(System.err);	
  		}
  	}
  	
  	String uId = iwc.getParameter(PARAMETER_EDIT_USER);
  	if (uId != null) {
  		userToEdit = Integer.parseInt(uId);
  	}

	try {
		deleteUser(iwc, _school);
	} catch (FinderException e) {
		add(getTextNormal(_iwrb.getLocalizedString("user_not_deleted","User not deleted")));
	}

  }
  
  public SchoolUserEditor(IWContext iwc) throws RemoteException{
  	init(iwc);
  }
  
  public void setTextStyleNormal(Text text) {
  	this.TEXT_NORMAL = text;	
  }
  
  public void setTextStyleTitle(Text text) {
  	this.TEXT_TITLE = text;	
  }
  
  public void setInputStyle(String style) {
  	this.INPUT_STYLE = style;	
  }
  
	public void main(IWContext iwc) throws RemoteException {
		init(iwc);
		
		String action = iwc.getParameter(PARAMETER_ACTION);
		
		if (action == null) {
			add(schoolList(iwc));
		}else if (action.equals(ACTION_VIEW_SCHOOL) && _school != null) {
			add(mainForm(iwc));
		}else if (action.equals(ACTION_UPDATE) && _school != null) {
			updateUsers(iwc, _school);
			add(mainForm(iwc));
		}
		
	}
	
	protected SchoolUserBusiness getSchoolUserBusiness(IWContext iwc) throws RemoteException {
		return (SchoolUserBusiness) IBOLookup.getServiceInstance(iwc, SchoolUserBusiness.class);
	}
	
	public void addParameter(String parameterName, String parameterValue) {
		if (parameterName != null && parameterValue != null) {
			if (this.parameterNames == null) {
				parameterNames = new Vector();	
			}	
			if (this.parameterValues == null) {
				parameterValues = new Vector();	
			}
			parameterNames.add(parameterName);
			parameterValues.add(parameterValue);
			
		}
	}
	
	public void setSchoolTypeCategory(String typeCategory) {
		if (typeCategory != null && !typeCategory.equals("") ) {
			try {
				SchoolTypeHome sth = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
				schoolTypeIds = sth.findAllByCategory(typeCategory);
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
  	
	}	
}
