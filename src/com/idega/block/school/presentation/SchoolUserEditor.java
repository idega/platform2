package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.login.presentation.LoginEditor;
import com.idega.block.login.presentation.LoginEditorWindow;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolHome;
import com.idega.business.IBOLookup;
import com.idega.core.data.Email;
import com.idega.core.data.EmailHome;
import com.idega.core.data.Phone;
import com.idega.core.data.PhoneHome;
import com.idega.user.business.UserBusiness;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.text.TextFormat;

/**
 * @author gimmi
 */
public class SchoolUserEditor extends Block {
	
	TextFormat _tFormat;
	IWResourceBundle _iwrb;
	School _school;
  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";
  
  String PARAMETER_ACTION = "sue_act";
  String ACTION_VIEW_SCHOOL = "sue_pvs";
  String ACTION_UPDATE = "sue_up_usrs";
  String PARAMETER_SCHOOL_ID = "sue_sId";

	private String PARAMETER_SCHOOL_USER_NAME = "sue_un";
	private String PARAMETER_SCHOOL_USER_TELEPHONE = "sue_utf";
	private String PARAMETER_SCHOOL_USER_EMAIL = "sue_uem";
	private String PARAMETER_SCHOOL_USER_ID = "sue_uid";

	private String PARAMETER_SCHOOL_HEADMASTER_NAME = "sue_hn";
	private String PARAMETER_SCHOOL_HEADMASTER_TELEPHONE = "sue_htf";
	private String PARAMETER_SCHOOL_HEADMASTER_EMAIL = "sue_hem";
	private String PARAMETER_SCHOOL_HEADMASTER_ID = "sue_hid";

	private String PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_NAME = "sue_ahn";
	private String PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_TELEPHONE = "sue_ahtf";
	private String PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_EMAIL = "sue_ahem";
	private String PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_ID = "sue_ahid";

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
  
  
  
  
  
  private Table schoolList(IWContext iwc) throws RemoteException{
  	Collection schools = getSchoolBusiness(iwc).findAllSchools();
  	Table table = new Table();
  	int row = 1;
  	
  	table.add(_tFormat.format(_iwrb.getLocalizedString("school.select_school","Select School"),TextFormat.HEADER), 2, row);
  	
  	if (schools != null) {
  		Iterator iter = schools.iterator();
  		Link link;
  		School school;
  		int textFormatType = 1;
  		while ( iter.hasNext() ) {
  			++row;
  			try {
					school = getSchoolHome().findByPrimaryKey(iter.next());
	  			link = new Link(_tFormat.format(_iwrb.getLocalizedString("school.edit","edit"),TextFormat.NORMAL));
	  			link.addParameter(PARAMETER_ACTION, ACTION_VIEW_SCHOOL);
	  			link.addParameter(PARAMETER_SCHOOL_ID, school.getPrimaryKey().toString());
	  			table.add(link, 1, row);
					if (school.equals(_school)) {
						textFormatType = TextFormat.HEADER;
					}else {
						textFormatType = TextFormat.NORMAL;
					}
	  			table.add(_tFormat.format(school.getName(),textFormatType), 2, row);
				} catch (FinderException e) {
					e.printStackTrace(System.err);
				}
  		}
  	}

  	return table;
  }

	private Form schoolUsers(IWContext iwc) throws RemoteException {
		Form form = new Form();
		Table contTable = new Table();
		form.add(contTable);
		try {
			UserHome uHome = (UserHome) IDOLookup.getHome(User.class);

			contTable.add(_tFormat.format(_iwrb.getLocalizedString("school.headmaster","Headmaster"), TextFormat.TITLE), 1, 1);

			int headmasterId = _school.getHeadmasterUserId();
			if (headmasterId > 0) {
				User user = uHome.findByPrimaryKey(new Integer(headmasterId));
				Table table = new Table();
				insertUserIntoTable(table, 1, user, 1);
				contTable.add(table, 1, 2);
			}else {
				contTable.add(getUserForm(1), 1, 2);	
			}
			
			contTable.add(_tFormat.format(_iwrb.getLocalizedString("school.assistant_headmaster","Assistant headmaster"), TextFormat.TITLE), 1, 3);
				int assistantHeadmasterId = _school.getAssistantHeadmasterUserId();
			if (assistantHeadmasterId > 0) {
				User user = uHome.findByPrimaryKey(new Integer(assistantHeadmasterId));
				Table table = new Table();
				insertUserIntoTable(table, 1, user, 2);
				contTable.add(table, 1, 4);
			}else {
				contTable.add(getUserForm(2), 1, 4);	
			}		
			
			contTable.add(_tFormat.format(_iwrb.getLocalizedString("school.other_users","Other Users"), TextFormat.TITLE), 1, 5);
			/** Populated User field */
			Collection users = getUserBusiness(iwc).getGroupBusiness().getUsersContained(_school.getHeadmasterGroupId());
			if (users != null && users.size() > 0) {
				Iterator iter = users.iterator();
				Table table = new Table();
				int row = 1;
				table.add(_tFormat.format(_iwrb.getLocalizedString("school.name","Name"), TextFormat.NORMAL), 1, 1);
				table.add(_tFormat.format(_iwrb.getLocalizedString("school.email","E-mail"), TextFormat.NORMAL), 2, 1);
				table.add(_tFormat.format(_iwrb.getLocalizedString("school.phone","Phone"), TextFormat.NORMAL), 3, 1);

				++row;
				while (iter.hasNext()) {
					User hm = (User) iter.next();
					row = insertUserIntoTable(table, row, hm, 3);
				}
				contTable.add(table, 1, 5);
			}
			
			/** Empty User field */
			Table table = this.getUserForm(3);

			contTable.add(table, 1, 6);

			SubmitButton update = new SubmitButton(_iwrb.getLocalizedImageButton("school.save","Save"), PARAMETER_ACTION, ACTION_UPDATE);
			form.maintainParameter(PARAMETER_SCHOOL_ID);
			contTable.add(update, 1, 7);
			
		} catch (FinderException e) {
			e.printStackTrace(System.err);
		}
		return form;
	}





	private int insertUserIntoTable(Table table, int row, User hm, int userType) throws RemoteException {
		String sname = PARAMETER_SCHOOL_USER_NAME;
		String semail = PARAMETER_SCHOOL_USER_EMAIL;
		String sphone = PARAMETER_SCHOOL_USER_TELEPHONE;
		String sid = PARAMETER_SCHOOL_USER_ID;
		
		switch (userType) {
			case 1 :
				sname = PARAMETER_SCHOOL_HEADMASTER_NAME;
				semail = PARAMETER_SCHOOL_HEADMASTER_EMAIL;
				sphone = PARAMETER_SCHOOL_HEADMASTER_TELEPHONE;
				sid = PARAMETER_SCHOOL_HEADMASTER_ID;
				break;
			case 2 : 
				sname = PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_NAME;
				semail = PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_EMAIL;
				sphone = PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_TELEPHONE;
				sid = PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_ID;
				break;	
		}
		Collection emails;
		Collection phones;
		int uRow;
		int mRow;
							uRow = row;
							String hmId = hm.getPrimaryKey().toString();
							emails = hm.getEmails();
							phones = hm.getPhones();
							HiddenInput inp = new HiddenInput(sid, hmId);
							TextInput pName = new TextInput(sname+"_"+hmId, hm.getName());
							Link login = new Link(_tFormat.format(_iwrb.getLocalizedString("school.login","Login"), TextFormat.NORMAL));
							login.setWindowToOpen(LoginEditorWindow.class);
							login.addParameter(LoginEditor.prmUserId, hmId);
							
							
							table.add(inp, 1, row);
							table.add(login, 4, row);
							
							table.add(pName, 1, row);
							if (emails != null) {
								Email email;
								EmailHome eHome = (EmailHome) IDOLookup.getHome(Email.class);
								Iterator iEm = emails.iterator();
								while (iEm.hasNext()) {
									try {
										email = eHome.findByPrimaryKey(iEm.next());
										TextInput pEmail = new TextInput(semail+"_"+hmId+"_"+email.getPrimaryKey() , email.getEmailAddress() );
										table.add(pEmail, 2, row);
									} catch (FinderException e) {
										table.add(_tFormat.format(_iwrb.getLocalizedString("school.not_fount","Not found"), TextFormat.NORMAL), 2, row);
									}
									++row;
								}
							}
							TextInput pEmail = new TextInput(semail+"_"+hmId );
							table.add(pEmail, 2, row);
		//					this.addLeft(_iwrb.getLocalizedString("school.add_email","Add E-mail"), pEmail, true);
		
							mRow = row;
							row = uRow;
							if (phones != null) {
								Phone phone;
								PhoneHome pHome = (PhoneHome) IDOLookup.getHome(Phone.class);
								Iterator iPh = phones.iterator();
								while (iPh.hasNext()) {
									try {
										phone = pHome.findByPrimaryKey(iPh.next());
										TextInput pPhone = new TextInput(sphone+"_"+hmId+"_"+phone.getPrimaryKey(), phone.getNumber());
										table.add(pPhone, 3, row);
									} catch (FinderException e) {
										table.add(_tFormat.format(_iwrb.getLocalizedString("school.not_fount","Not found"), TextFormat.NORMAL), 3, row);
									}
									++row;
								}
							}
							TextInput pPhone = new TextInput(sphone+"_"+hmId);
							table.add(pPhone, 3, row);
		//					this.addLeft(_iwrb.getLocalizedString("school.add_phone","Add Phone"), pPhone, true);
							if (row >= mRow) {
								++row;
							}else {
								row = mRow + 1;
							}
		return row;
	}





	/**
	 * Returns a UserForm	 * @param userType 1 = Headmaster, 2 = Assistant Headmaster, 3 = User	 * @return Table	 */
	private Table getUserForm(int userType) {
		String name = PARAMETER_SCHOOL_USER_NAME;
		String email = PARAMETER_SCHOOL_USER_EMAIL;
		String phone = PARAMETER_SCHOOL_USER_TELEPHONE;
		String id = PARAMETER_SCHOOL_USER_ID;
		
		switch (userType) {
			case 1 :
				name = PARAMETER_SCHOOL_HEADMASTER_NAME;
				email = PARAMETER_SCHOOL_HEADMASTER_EMAIL;
				phone = PARAMETER_SCHOOL_HEADMASTER_TELEPHONE;
				id = PARAMETER_SCHOOL_HEADMASTER_ID;
				break;
			case 2 : 
				name = PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_NAME;
				email = PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_EMAIL;
				phone = PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_TELEPHONE;
				id = PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_ID;
				break;	
		}
		
		Table table = new Table();
		int row = 1;
		
		
		Text tName = new Text(_iwrb.getLocalizedString("school.name","Name"));
		Text tEmail = new Text(_iwrb.getLocalizedString("school.email","E-mail"));
		Text tPhone = new Text(_iwrb.getLocalizedString("school.phone","Phone"));
		
		
		TextInput pName = new TextInput(name);
		TextInput pEmail = new TextInput(email);
		TextInput pPhone = new TextInput(phone);
		
		
		table.add(tName, 1, 1);
		table.add(tEmail, 2, 1);
		table.add(tPhone, 3, 1);
		
		table.add(pName, 1, 2);
		table.add(pEmail, 2, 2);
		table.add(pPhone, 3, 2);
		return table;
	}


	private boolean updateUsers(IWContext iwc) throws RemoteException {
		
		return ( updateUsers(iwc, 1) && updateUsers(iwc, 2) && updateUsers(iwc, 3) );
			
	}

	private boolean updateUsers(IWContext iwc, int userType) throws RemoteException {
		String sname = PARAMETER_SCHOOL_USER_NAME;
		String semail = PARAMETER_SCHOOL_USER_EMAIL;
		String sphone = PARAMETER_SCHOOL_USER_TELEPHONE;
		String sid = PARAMETER_SCHOOL_USER_ID;
		
		switch (userType) {
			case 1 :
				sname = PARAMETER_SCHOOL_HEADMASTER_NAME;
				semail = PARAMETER_SCHOOL_HEADMASTER_EMAIL;
				sphone = PARAMETER_SCHOOL_HEADMASTER_TELEPHONE;
				sid = PARAMETER_SCHOOL_HEADMASTER_ID;
				break;
			case 2 : 
				sname = PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_NAME;
				semail = PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_EMAIL;
				sphone = PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_TELEPHONE;
				sid = PARAMETER_SCHOOL_ASSISTANT_HEADMASTER_ID;
				break;	
		}

				/** Updateing headmasters */
		try {
			String[] hIds = iwc.getParameterValues(sid);
			
			if (hIds != null) {
				UserHome userHome = (UserHome) IDOLookup.getHome(User.class);
				User user;
				for (int i = 0; i < hIds.length; i++) {
					String name         = iwc.getParameter(sname+"_"+hIds[i]);
					user = userHome.findByPrimaryKey(new Integer(hIds[i]));
					
					if (name.equals("")) {
						System.out.println("tying to remove user / or NOT");
//						_school.getHeadmasterGroup().removeUser(user);
//						getUserBusiness(iwc).deleteUser((new Integer(hIds[i])).intValue());
					}else {
						getUserBusiness(iwc).updateUser(user, name, "", "", null, null, null, null, null, null);
//						user.setName(name);	
//						user.store();
						
						Collection emails = user.getEmails();
						Collection phones =	user.getPhones();
						
						if (emails != null) {
							EmailHome eHome = (EmailHome) IDOLookup.getHome(Email.class);
							user.removeAllEmails();
							Email email;
							Iterator iter = emails.iterator();
							while (iter.hasNext()) {
								Object prK = iter.next();
								String sEmail = iwc.getParameter(semail+"_"+hIds[i]+"_"+prK);
								if (sEmail != null && !sEmail.equals("")) {
									email = eHome.findByPrimaryKey(prK);
									email.setEmailAddress(sEmail);
									email.store();	
									user.addEmail(email);
								}
							}	
						}
			
						if (phones != null) {
							PhoneHome pHome = (PhoneHome) IDOLookup.getHome(Phone.class);
							user.removeAllPhones();
							Phone phone;
							Iterator iter = phones.iterator();
							while (iter.hasNext()) {
								Object prK = iter.next();
								String sPhone = iwc.getParameter(sphone+"_"+hIds[i]+"_"+prK);
								if (sPhone != null && !sPhone.equals("")) {
									phone = pHome.findByPrimaryKey(prK);
									phone.setNumber(sPhone);
									phone.store();	
									user.addPhone(phone);
								}
							}	
						}
						
						String newEmail  = iwc.getParameter(semail+"_"+hIds[i]);
						String newPhone  = iwc.getParameter(sphone+"_"+hIds[i]);
						
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
					}
					
				}
			
			}
			
			
			/** Adding headmaster */
			String headmaster = iwc.getParameter(sname);
			String hmEmail    = iwc.getParameter(semail);
			String hmPhone    = iwc.getParameter(sphone);
			
			if (headmaster != null && !headmaster.equals("")) {
				User user = getUserBusiness(iwc).createUser(headmaster, "","");
				
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
				
				if (userType ==1) {
					_school.setHeadmasterUserId( ((Integer)user.getPrimaryKey()).intValue());
					_school.store();
				}else if (userType == 2) {
					_school.setAssistantHeadmasterUserId( ((Integer)user.getPrimaryKey()).intValue());
					_school.store();
				}else if (userType ==3) {
					getSchoolBusiness(iwc).addHeadmaster(_school, user);
				}
			
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

	private Table mainForm(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.add(schoolList(iwc), 1, 1);
		table.add(schoolUsers(iwc), 2, 1);
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_TOP);
		return table;
	}

	private UserBusiness getUserBusiness(IWApplicationContext iwac) throws RemoteException {
		return  (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
	}

	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}

	private SchoolHome getSchoolHome() throws RemoteException {
		return (SchoolHome) IDOLookup.getHome(School.class);	
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
  	
  }
  
  
 	public void main(IWContext iwc) throws RemoteException {
		init(iwc);
		
		String action = iwc.getParameter(PARAMETER_ACTION);
		if (action == null) {
			add(schoolList(iwc));
		}else if (action.equals(ACTION_VIEW_SCHOOL) && _school != null) {
			add(mainForm(iwc));
		}else if (action.equals(ACTION_UPDATE) && _school != null) {
			updateUsers(iwc);
			add(mainForm(iwc));
		}
		
	}
}
