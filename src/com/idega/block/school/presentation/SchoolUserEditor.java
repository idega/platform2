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
import com.idega.block.school.data.SchoolDepartment;
import com.idega.block.school.data.SchoolDepartmentHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolUser;
import com.idega.block.school.data.SchoolUserHome;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.EmailHome;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneHome;
import com.idega.core.contact.data.PhoneType;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
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
	IWBundle _iwb;
	protected School _school;
	public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";
	String PARAMETER_ACTION = "sue_act";
	String ACTION_UPDATE_DEPM = "sue_up_dep";
	String ACTION_VIEW_SCHOOL = "sue_pvs";
	String ACTION_UPDATE = "sue_up_usrs";
	String ACTION_VIEW_SCHOOL_DEPM = "sue_pvsd";
//String PARAMETER_SCHOOL_ID = "sue_sId";
	public static String PARAMETER_TOPIC_EDITOR = "sue_to_edi";
	public static String PARAMETER_TOPIC_DEPM = "sue_to_dep";
	public static String PARAMETER_TOPIC_PERSON = "sue_to_per";
	String PARAMETER_SCHOOL_ID = SchoolContentBusinessBean.PARAMETER_SCHOOL_ID;
	private String PARAMETER_SCHOOL_USER_NAME = "sue_un";
	private String PARAMETER_SCHOOL_USER_TELEPHONE = "sue_utf";
	//private String PARAMETER_SCHOOL_USER_CELLPHONE = "sue_ucf";
	private String PARAMETER_SCHOOL_USER_MOBILEPHONE = "sue_umf";
	private String PARAMETER_SCHOOL_USER_EMAIL = "sue_uem";
	private String PARAMETER_SCHOOL_USER_ID = "sue_uid";
	private String PARAMETER_SCHOOL_USER_TYPE = "sue_sut";
	private String PARAMETER_SCHOOL_DEPARTMENT = "sch_dep";
	private String PARAMETER_SCHOOL_DEPARTMENT_PHONE = "sch_dep_ph";
	private String PARAMETER_SCHOOL_DEPARTMENT_ID = "sue_did";
	private String PARAMETER_SCHOOL_DEPARTMENT_ID_DROP = "sue_did_dr";
	private String PARAMETER_SCHOOL_SHOW_CONTACT = "sue_show_cont";
	private String PARAMETER_SCHOOL_SHOW_CONTACT_EDIT = "sue_show_cont_edi";
	private String PARAMETER_SCHOOL_MAIN_HEADMASTER = "sue_main_headm";
	private boolean PARAMETER_SCHOOL_HIGHSCHOOL = false;
	private String PARAMETER_EDIT_USER = "sue_eds";
	private String PARAMETER_DELTE_USER = "sue_dls";
	private String PARAMETER_EDIT_SCH_DEP = "sue_edsd";
	private String PARAMETER_DELETE_SCH_DEP = "sue_dlsd";
	private static final String PARAMETER_IS_ECONOMICAL_RESP = "sue_is_economical_responsible";
	private Text TEXT_NORMAL;
	private Text TEXT_TITLE;
	private String INPUT_STYLE;
	private int userToEdit = -1;
	private int depmToEdit = -1;
	private List parameterNames;
	private List parameterValues;
	private Collection schoolTypeIds;
	private int mobilePhoneType = PhoneType.MOBILE_PHONE_ID;
	private boolean _highSchoolCategory = false;

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public SchoolUserEditor() {

	}

	private Table schoolList(IWContext iwc) throws RemoteException {
		Collection schools = new java.util.Vector(0);
		if (schoolTypeIds == null || schoolTypeIds.isEmpty()) {
			schools = getSchoolBusiness(iwc).findAllSchools();
		}
		else {
			schools = getSchoolBusiness(iwc).findAllSchoolsByType(schoolTypeIds);
		}

		Table table = new Table();
		int row = 0;

		if (schools != null) {
			Iterator iter = schools.iterator();
			Link link;
			School school;
			int textFormatType = 1;
			while (iter.hasNext()) {
				++row;
				school = (School) (iter.next());
				link = getLink(_tFormat.format(_iwrb.getLocalizedString("school.edit", "edit"), TextFormat.NORMAL), ACTION_VIEW_SCHOOL, false);
				link.addParameter(this.PARAMETER_SCHOOL_ID, school.getPrimaryKey().toString());
				table.add(link, 1, row);
				if (school.equals(_school)) {
					textFormatType = TextFormat.HEADER;
				}
				else {
					textFormatType = TextFormat.NORMAL;
				}
				table.add(_tFormat.format(school.getName(), textFormatType), 2, row);
			}
		}

		return table;
	}

	private Form schoolUsers(IWContext iwc, School school) throws RemoteException {
		Form form = new Form();
		String category = getSchoolUserBusiness(iwc).getSchoolCategory(school);
		PARAMETER_SCHOOL_HIGHSCHOOL = category.equalsIgnoreCase(getSchoolUserBusiness(iwc).getSchoolBusiness().getHighSchoolSchoolCategory());

		try {
			if (PARAMETER_SCHOOL_HIGHSCHOOL) {
				form.add(highschoolUsersTable(iwc, school, true));
			}
			else {
				form.add(schoolUsersTable(iwc, school, true));
			}
		}
		catch (Exception e) {

		}

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
		}
		else {
			Text text = (Text) TEXT_NORMAL.clone();
			text.setText(content);
			return text;
		}
	}

	private Text getTextTitle(String content) {
		if (TEXT_TITLE == null) {
			return _tFormat.format(content, TextFormat.TITLE);
		}
		else {
			Text text = (Text) TEXT_TITLE.clone();
			text.setText(content);
			return text;
		}
	}

	private Text getTextTitleGray(String content) {
		if (TEXT_TITLE == null) {
			return _tFormat.format(content, TextFormat.TITLE);
		}
		else {
			Text text = (Text) TEXT_TITLE.clone();
			text.setText(content);
			text.setFontColor("#386cb7");
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
			input.setMarkupAttribute("style", INPUT_STYLE);
		}
	}

	private Table schoolUsersTable(IWContext iwc, School school, boolean addSubmitButton) throws RemoteException {
		Table contTable = new Table();
		int cRow = 1;
		Table tableUf = this.getUserForm(iwc, school);
		contTable.add(tableUf, 1, cRow++);

		contTable.setColor(1, cRow, "#c7c7c7");
		contTable.setHeight(1, cRow++, "1");

		try {
			Collection suTypes = getSchoolUserBusiness(iwc).getSchoolUserTypes(school);

			if (suTypes != null && !suTypes.isEmpty()) {
				String[] userType;
				Iterator iter = suTypes.iterator();
				while (iter.hasNext()) {
					userType = (String[]) iter.next();
					++cRow;

					contTable.add(getTextTitle(_iwrb.getLocalizedString(userType[0], userType[1])), 1, cRow);
					Collection users = getSchoolUserBusiness(iwc).getUsers(school, Integer.parseInt(userType[2]));

					if (users != null && users.size() > 0) {
						Iterator userIter = users.iterator();
						Table table = new Table();
						int row = 1;
						while (userIter.hasNext()) {
							User hm = (User) userIter.next();

							int userId = ((Integer) hm.getPrimaryKey()).intValue();
							if (userId == userToEdit) {
								table.setHeight(row++, 3);
								row = insertEditableUserIntoTable(table, hm, Integer.parseInt(userType[2]), row);
								table.setHeight(row++, 6);
							}
							else {
								row = insertUserIntoTable(table, hm, row);
							}
						}
						contTable.add(table, 1, ++cRow);
					}
				}
			}
			else {
				cRow = 1;
			}
			if (addSubmitButton) {
				++cRow;
				SubmitButton update = new SubmitButton(_iwrb.getLocalizedImageButton("school.save", "Save"), PARAMETER_ACTION, ACTION_UPDATE);
				contTable.add(update, 1, cRow);
			}

		}
		catch (FinderException e) {
			e.printStackTrace(System.err);
		}
		catch (IDORelationshipException e) {
			e.printStackTrace(System.err);
		}
		return contTable;
	}

	private Table highschoolUsersTable(IWContext iwc, School school, boolean addSubmitButton) throws Exception {
		Table contTable = new Table();
		int cRow = 1;
		boolean show = true;
		boolean main_headmaster = false;
		String rowColor = "#C7C7C7";

		Table tableDepForm = this.getDepartmentForm();
		Table tableDep = this.schoolDepartmentTable(iwc, school);
		Table tableUser = this.getUserFormHighSchool(iwc, school);
		Table tableShowInfo = this.getShowInfoTable();

		contTable.add(tableDepForm, 1, cRow);
		++cRow;
		contTable.setColor(1, cRow, rowColor);
		contTable.setHeight(1, cRow, "1");
		++cRow;
		contTable.add(tableDep, 1, cRow);
		++cRow;
		contTable.setColor(1, cRow, rowColor);
		contTable.setHeight(1, cRow, "1");
		++cRow;
		contTable.add(tableUser, 1, cRow);
		++cRow;
		contTable.setColor(1, cRow, rowColor);
		contTable.setHeight(1, cRow, "1");
		++cRow;
		contTable.add(tableShowInfo, 1, cRow);
		++cRow;
		contTable.setColor(1, cRow, rowColor);
		contTable.setHeight(1, cRow, "1");
		++cRow;
		++cRow;

		try {
			Collection sDepartments = getSchoolBusiness(iwc).getSchoolDepartmentHome().findAllDepartmentsBySchool(school);
			Text tMainHeadmaster = getTextTitle(_iwrb.getLocalizedString("school.main_headmaster", "Main headmaster"));

			if (sDepartments != null & !sDepartments.isEmpty()) {
				Iterator depIter = sDepartments.iterator();
				Collection suTypes = getSchoolUserBusiness(iwc).getSchoolUserTypes(school);

				while (depIter.hasNext()) {
					SchoolDepartment schDep = (SchoolDepartment) depIter.next();
					++cRow;
					contTable.setColor(1, cRow, rowColor);
					++cRow;
					contTable.add(getTextTitleGray(schDep.getDepartment()), 1, cRow);
					++cRow;

					if (suTypes != null && !suTypes.isEmpty()) {
						String[] userType;
						Iterator iter = suTypes.iterator();
						while (iter.hasNext()) {
							userType = (String[]) iter.next();
							++cRow;

							contTable.add(getTextTitle(_iwrb.getLocalizedString(userType[0], userType[1])), 1, cRow);
							Collection users = getSchoolUserBusiness(iwc).getUsersByDepartm(school, Integer.parseInt(userType[2]), schDep.getDepartmentID());
							//Collection users = getSchoolUserBusiness(iwc).getUsers(school,
							// Integer.parseInt(userType[2]));

							if (users != null && users.size() > 0) {
								Iterator userIter = users.iterator();
								Table table = new Table();
								Table tableMHM = new Table();

								int row = 1;
								int rowMHM = 1;
								while (userIter.hasNext()) {
									User hm = (User) userIter.next();
									show = getSchoolUserBusiness(iwc).getUserShowInContact(hm);
									main_headmaster = getSchoolUserBusiness(iwc).getUserMainHeadmaster(hm);
									int userId = ((Integer) hm.getPrimaryKey()).intValue();
									if (main_headmaster) {
										contTable.add(tMainHeadmaster, 1, 9);

										if (userId == userToEdit) {
											table.setHeight(rowMHM++, 3);
											rowMHM = insertEditableHighschUserIntoTable(tableMHM, hm, Integer.parseInt(userType[2]), rowMHM, show);
											table.setHeight(rowMHM++, 6);
										}
										else {
											rowMHM = insertHighschUserIntoTable(tableMHM, hm, rowMHM, show);
										}
										contTable.add(tableMHM, 1, 10);
									}
									else {
										if (userId == userToEdit) {
											table.setHeight(row++, 3);
											row = insertEditableHighschUserIntoTable(table, hm, Integer.parseInt(userType[2]), row, show);
											table.setHeight(row++, 6);
										}
										else {
											row = insertHighschUserIntoTable(table, hm, row, show);
										}
									}
								}

								contTable.add(table, 1, ++cRow);
								++cRow;
								//contTable.setBorder(1);
								//contTable.setBorderColor("black");
							}

						} //end while suTypes
						//							Malin

						/*
						 * Collection schUsers =
						 * getSchoolUserBusiness(iwc).getSchoolUsers(school, hm); Iterator
						 * iterUsers = schUsers.iterator(); SchoolUser schUser; boolean
						 * showcontact; while (iterUsers.hasNext()) { schUser = (SchoolUser)
						 * iterUsers.next(); showcontact = schUser.getShowInContact();
						 *  }
						 */
						//

					}
				}
			}
			else {
				cRow = 1;
			}

			if (addSubmitButton) {
				++cRow;
				SubmitButton update = new SubmitButton(_iwrb.getLocalizedImageButton("school.save", "Save"), PARAMETER_ACTION, ACTION_UPDATE);
				contTable.add(update, 1, cRow);
			}

		}
		catch (FinderException e) {
			e.printStackTrace(System.err);
		}
		catch (IDORelationshipException e) {
			e.printStackTrace(System.err);
		}
		return contTable;
	}

	private Table schoolDepartmentTable(IWContext iwc, School school) {
		Table contTable = new Table();
		int schoolId = ((Integer) school.getPrimaryKey()).intValue();
		try {

			int cRow = 1;
			Collection sDepartments = getSchoolBusiness(iwc).getSchoolDepartmentHome().findAllDepartmentsBySchool(school);
			if (sDepartments == null || sDepartments.isEmpty()) {
				getSchoolBusiness(iwc).storeSchoolDepartment(" ", " ", new Integer(schoolId).intValue(), -1);
				sDepartments = getSchoolBusiness(iwc).getSchoolDepartmentHome().findAllDepartmentsBySchool(school);
			}

			if (sDepartments != null && !sDepartments.isEmpty()) {

				contTable.add(getTextTitle(_iwrb.getLocalizedString("school.department")), 1, cRow);

				Iterator departmIter = sDepartments.iterator();
				Table table = new Table();
				int row = 1;
				while (departmIter.hasNext()) {
					SchoolDepartment schDep = (SchoolDepartment) departmIter.next();

					int departmId = ((Integer) schDep.getPrimaryKey()).intValue();
					if (departmId == depmToEdit) {
						row = insertEditableDepmIntoTable(table, schDep, row);
					}
					else {
						row = insertDepmIntoTable(table, schDep, row);
					}

				}
				contTable.add(table, 1, ++cRow);

			}

			else {
				cRow = 1;
			}
		} //end try
		catch (Exception e) {
		}

		return contTable;
	}

	private Table getShowInfoTable() {
		Table contTable = new Table();
		contTable.setWidth(2, "5");
		contTable.setWidth(4, "5");

		//Image imgContactGreen =
		// getBundle().getImage("shared/checkmark_green.gif", 11, 11);
		Image imgContactRed = getBundle().getImage("shared/checkmark_red.gif", 11, 11);
		Text tShowInList = getTextNormal(_iwrb.getLocalizedString("school.show_in_contactl", "Show in contactlist"));
		Text tNotShowInList = getTextNormal(_iwrb.getLocalizedString("school.show_not_in_contactl", "Don't show in contactlist"));

		//Link newLink = new Link(core.getImage("/shared/create.gif"));
		Link imgContactGreen = new Link(getBundle().getImage("shared/checkmark_green.gif"));

		contTable.add(imgContactGreen, 1, 1);
		contTable.add(" = ", 1, 1);
		contTable.add(tShowInList, 1, 1);
		contTable.add(imgContactRed, 3, 1);
		contTable.add(" = ", 3, 1);
		contTable.add(tNotShowInList, 3, 1);

		return contTable;
	}

	private int insertDepmIntoTable(Table table, SchoolDepartment schDep, int row) {

		table.setWidth(2, "5");
		table.setWidth(4, "5");
		table.setWidth(6, "5");
		table.setWidth(8, "5");
		//table.setWidth(10, "5");
		//table.setWidth(12, "5");

		String schDepId = schDep.getPrimaryKey().toString();

		String phone = schDep.getDepartmentPhone();
		String department = schDep.getDepartment();

		Text tNameDep = getTextNormal(department);
		Link edit = getLink(getTextNormal(_iwrb.getLocalizedString("school.edit", "Edit")), ACTION_VIEW_SCHOOL_DEPM, true);
		edit.addParameter(PARAMETER_EDIT_SCH_DEP, schDepId);
		Link delete = getLink(getTextNormal(_iwrb.getLocalizedString("school.delete", "Delete")), ACTION_VIEW_SCHOOL_DEPM, true);
		delete.addParameter(PARAMETER_DELETE_SCH_DEP, schDepId);

		if (department != null) {
			table.add(tNameDep, 1, row);
		}
		table.add(edit, 5, row);
		table.add(delete, 7, row);

		if (phone != null) {
			table.add(getTextNormal(phone), 3, row);
		}

		++row;

		//mRow = row;
		//row = uRow;
		//mobRow = uRow;
		//PhoneTypeHome ptHome = (PhoneTypeHome)IDOLookup.getHome(PhoneType.class);

		/*
		 * if (department != null) { table.add(getTextNormal(department), 5,
		 * mobRow); ++row; }
		 */

		/*
		 * if (row >= mRow && row >= mobRow) { ++row; }else if (mobRow >= row &&
		 * mobRow >= mRow) { row = mobRow + 1; }else { row = mRow + 1; }
		 */
		return row;
	}

	private int insertEditableDepmIntoTable(Table table, SchoolDepartment schDep, int row) {

		String sdepname = PARAMETER_SCHOOL_DEPARTMENT;
		String sphone = PARAMETER_SCHOOL_DEPARTMENT_PHONE;
		String sdid = PARAMETER_SCHOOL_DEPARTMENT_ID;

		//int uRow = row;
		//int mRow;
		//int mobRow;

		String schDepId = schDep.getPrimaryKey().toString();
		//String department = schDep.getDepartment();
		//String phone = schDep.getDepartmentPhone();

		HiddenInput inp = new HiddenInput(sdid, schDepId);
		//HiddenInput dphInp = new
		// HiddenInput(PARAMETER_SCHOOL_DEPARTMENT_PHONE+"_"+hmId,
		// Integer.toString(userType));
		TextInput pDepName = new TextInput(sdepname + "_" + schDepId, schDep.getDepartment());
		this.setTextInputStyle(pDepName);
		//Link login = new
		// Link(getTextNormal(_iwrb.getLocalizedString("school.login","Login")));
		//login.setWindowToOpen(LoginEditorWindow.class);
		//login.addParameter(LoginEditor.prmUserId, hmId);

		table.add(inp, 1, row);
		//table.add(utInp, 1, row);
		//table.add(login, 9, row);

		table.add(pDepName, 1, row);

		//if (phone != null) {

		TextInput pPhone = new TextInput(sphone + "_" + schDepId, schDep.getDepartmentPhone());
		this.setTextInputStyle(pPhone);
		table.add(pPhone, 3, row);

		//}

		++row;
		//TextInput pEmail = new TextInput(semail+"_"+hmId );
		//this.setTextInputStyle(pEmail);
		//table.add(pEmail, 3, row);
		//						this.addLeft(_iwrb.getLocalizedString("school.add_email","Add E-mail"),
		// pEmail, true);

		//mRow = row;
		//row = uRow;
		//mobRow = uRow;

		//					this.addLeft(_iwrb.getLocalizedString("school.add_phone","Add Phone"),
		// pPhone, true);
		/*
		 * if (row >= mRow && row >= mobRow) {
		 * 
		 * ++row; }else if (mobRow >= row && mobRow >= mRow) { row = mobRow + 1;
		 * }else { row = mRow + 1; }
		 */
		return row;
	}

	private int insertHighschUserIntoTable(Table table, User hm, int row, boolean show) {
		Collection emails;
		Collection phones;

		int uRow;
		int mRow;
		int mobRow;
		table.setWidth(2, "5");
		table.setWidth(4, "5");
		table.setWidth(6, "5");
		table.setWidth(8, "5");
		table.setWidth(10, "5");
		table.setWidth(12, "5");
		table.setWidth(14, "5");

		uRow = row;
		String hmId = hm.getPrimaryKey().toString();
		emails = hm.getEmails();
		phones = hm.getPhones();

		Text tName = getTextNormal(hm.getName());

		Link login = new Link(getTextNormal(_iwrb.getLocalizedString("school.login", "Login")));
		login.setWindowToOpen(LoginEditorWindow.class);
		login.addParameter(LoginEditor.prmUserId, hmId);
		Link edit = getLink(getTextNormal(_iwrb.getLocalizedString("school.edit", "Edit")), ACTION_VIEW_SCHOOL, true);
		edit.addParameter(PARAMETER_EDIT_USER, hmId);
		Link delete = getLink(getTextNormal(_iwrb.getLocalizedString("school.delete", "Delete")), ACTION_VIEW_SCHOOL, true);
		delete.addParameter(PARAMETER_DELTE_USER, hmId);

		Image imgContact;
		if (show) {
			imgContact = getBundle().getImage("shared/checkmark_green.gif", 11, 11);
		}
		else {
			imgContact = getBundle().getImage("shared/checkmark_red.gif", 11, 11);
		}

		table.add(imgContact, 1, row);
		table.add(tName, 3, row);
		table.add(edit, 11, row);
		table.add(login, 13, row);
		table.add(delete, 15, row);

		if (emails != null) {
			Email email;
			Iterator iEm = emails.iterator();
			while (iEm.hasNext()) {
				email = (Email) iEm.next();
				table.add(getTextNormal(email.getEmailAddress()), 5, row);
				++row;
			}
		}

		mRow = row;
		row = uRow;
		mobRow = uRow;
		//PhoneTypeHome ptHome = (PhoneTypeHome)IDOLookup.getHome(PhoneType.class);

		if (phones != null) {
			Phone phone;
			Iterator iPh = phones.iterator();
			while (iPh.hasNext()) {
				phone = (Phone) iPh.next();
				//Malin
				if (phone.getPhoneTypeId() == mobilePhoneType) {
					table.add(getTextNormal(phone.getNumber()), 9, mobRow);
					++mobRow;

				}
				else {
					table.add(getTextNormal(phone.getNumber()), 7, row);
					++row;

				}

			}
		}

		if (row >= mRow && row >= mobRow) {
			++row;
		}
		else if (mobRow >= row && mobRow >= mRow) {
			row = mobRow + 1;
		}
		else {
			row = mRow + 1;
		}
		return row;
	}

	private int insertEditableHighschUserIntoTable(Table table, User hm, int userType, int row, boolean show) {

		String sname = PARAMETER_SCHOOL_USER_NAME;
		String semail = PARAMETER_SCHOOL_USER_EMAIL;
		String sphone = PARAMETER_SCHOOL_USER_TELEPHONE;
		String smobilephone = PARAMETER_SCHOOL_USER_MOBILEPHONE;
		String sid = PARAMETER_SCHOOL_USER_ID;
		//String schDepId = PARAMETER_SCHOOL_DEPARTMENT_ID;

		Collection emails;
		Collection phones;
		int uRow = row;
		int mRow;
		int mobRow;

		String hmId = hm.getPrimaryKey().toString();

		emails = hm.getEmails();
		phones = hm.getPhones();

		HiddenInput inp = new HiddenInput(sid, hmId);
		HiddenInput utInp = new HiddenInput(PARAMETER_SCHOOL_USER_TYPE + "_" + hmId, Integer.toString(userType));
		//HiddenInput depInp = new
		// HiddenInput(PARAMETER_SCHOOL_DEPARTMENT_ID+"_"+hmId,
		// Integer.toString(dpmID)); //Malin
		//HiddenInput showInp = new HiddenInput(PARAMETER_SCHOOL_SHOW_CONTACT,
		// Boolean.toString(hm.get)); //Malin

		TextInput pName = new TextInput(sname + "_" + hmId, hm.getName());
		this.setTextInputStyle(pName);
		Link login = new Link(getTextNormal(_iwrb.getLocalizedString("school.login", "Login")));
		login.setWindowToOpen(LoginEditorWindow.class);
		login.addParameter(LoginEditor.prmUserId, hmId);

		CheckBox chbShow = new CheckBox(PARAMETER_SCHOOL_SHOW_CONTACT_EDIT, "true");
		if (show) {
			chbShow.setChecked(true);
		}
		else {
			chbShow.setChecked(false);
		}

		table.add(inp, 1, row);
		table.add(utInp, 1, row);
		table.add(login, 11, row);
		table.add(chbShow, 1, row);

		table.add(pName, 3, row);

		if (emails != null) {
			Email email;
			Iterator iEm = emails.iterator();
			while (iEm.hasNext()) {
				email = (Email) iEm.next();
				TextInput pEmail = new TextInput(semail + "_" + hmId + "_" + email.getPrimaryKey(), email.getEmailAddress());
				this.setTextInputStyle(pEmail);
				table.add(pEmail, 5, row);
				++row;
			}
		}
		TextInput pEmail = new TextInput(semail + "_" + hmId);
		this.setTextInputStyle(pEmail);
		table.add(pEmail, 5, row);
		//					this.addLeft(_iwrb.getLocalizedString("school.add_email","Add E-mail"),
		// pEmail, true);

		mRow = row;
		row = uRow;
		mobRow = uRow;

		if (phones != null) {
			Phone phone;

			Iterator iPhHome = phones.iterator();
			while (iPhHome.hasNext()) {
				phone = (Phone) iPhHome.next();
				TextInput pPhone;
				if (phone.getPhoneTypeId() != mobilePhoneType) {
					pPhone = new TextInput(sphone + "_" + hmId + "_" + phone.getPrimaryKey(), phone.getNumber());
					//String adsad = phone.getNumber();
					this.setTextInputStyle(pPhone);
					table.add(pPhone, 7, row);
					++row;
				}
				else if (phone.getPhoneTypeId() == mobilePhoneType) {
					pPhone = new TextInput(smobilephone + "_" + hmId + "_" + phone.getPrimaryKey(), phone.getNumber());
					//String adsad = phone.getNumber();
					this.setTextInputStyle(pPhone);
					table.add(pPhone, 9, mobRow);
					++mobRow;
				}

			}

			TextInput pPhone = new TextInput(sphone + "_" + hmId);
			this.setTextInputStyle(pPhone);
			table.add(pPhone, 7, row);

			if (_highSchoolCategory) {
				TextInput pMobilePhone = new TextInput(smobilephone + "_" + hmId);
				this.setTextInputStyle(pMobilePhone);
				table.add(pMobilePhone, 9, mobRow);
			}
		}

		//					this.addLeft(_iwrb.getLocalizedString("school.add_phone","Add Phone"),
		// pPhone, true);
		if (row >= mRow && row >= mobRow) {
			++row;
		}
		else if (mobRow >= row && mobRow >= mRow) {
			row = mobRow;
		}
		else {
			row = mRow;
		}

		table.setHeight(row++, 6);
		addIsEconomicalResponsibleCheckBox(table, row++, hm);
		magnifyTableRows(table, uRow, row);

		return row;
	}

	private void magnifyTableRows(final Table table, final int startRow, final int endRow) {
		for (int i = startRow; i < endRow; i++) {
			table.setRowColor(i, "#d0daea");
		}
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
	}

	private int insertUserIntoTable(Table table, User hm, int row) {
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
		Link login = new Link(getTextNormal(_iwrb.getLocalizedString("school.login", "Login")));
		login.setWindowToOpen(LoginEditorWindow.class);
		login.addParameter(LoginEditor.prmUserId, hmId);
		Link edit = getLink(getTextNormal(_iwrb.getLocalizedString("school.edit", "Edit")), ACTION_VIEW_SCHOOL, true);
		edit.addParameter(PARAMETER_EDIT_USER, hmId);
		Link delete = getLink(getTextNormal(_iwrb.getLocalizedString("school.delete", "Delete")), ACTION_VIEW_SCHOOL, true);
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
				table.add(getTextNormal(email.getEmailAddress()), 3, row);
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
		}
		else {
			row = mRow + 1;
		}
		return row;
	}

	private int insertEditableUserIntoTable(Table table, User hm, int userType, int row) {

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
		HiddenInput utInp = new HiddenInput(PARAMETER_SCHOOL_USER_TYPE + "_" + hmId, Integer.toString(userType));
		TextInput pName = new TextInput(sname + "_" + hmId, hm.getName());
		this.setTextInputStyle(pName);
		Link login = new Link(getTextNormal(_iwrb.getLocalizedString("school.login", "Login")));
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
				TextInput pEmail = new TextInput(semail + "_" + hmId + "_" + email.getPrimaryKey(), email.getEmailAddress());
				this.setTextInputStyle(pEmail);
				table.add(pEmail, 3, row);
				++row;
			}
		}
		TextInput pEmail = new TextInput(semail + "_" + hmId);
		this.setTextInputStyle(pEmail);
		table.add(pEmail, 3, row);
		//					this.addLeft(_iwrb.getLocalizedString("school.add_email","Add E-mail"),
		// pEmail, true);

		mRow = row;
		row = uRow;
		if (phones != null) {
			Phone phone;
			Iterator iPh = phones.iterator();
			while (iPh.hasNext()) {
				phone = (Phone) iPh.next();
				TextInput pPhone = new TextInput(sphone + "_" + hmId + "_" + phone.getPrimaryKey(), phone.getNumber());
				this.setTextInputStyle(pPhone);
				table.add(pPhone, 5, row);
				++row;
			}
		}
		TextInput pPhone = new TextInput(sphone + "_" + hmId);
		this.setTextInputStyle(pPhone);
		table.add(pPhone, 5, row);
		//					this.addLeft(_iwrb.getLocalizedString("school.add_phone","Add Phone"),
		// pPhone, true);
		if (row < mRow) {
			row = mRow;
		}
		table.setHeight(row++, 6);
		addIsEconomicalResponsibleCheckBox(table, row++, hm);
		magnifyTableRows(table, uRow, row);

		return row;
	}

	private void addIsEconomicalResponsibleCheckBox(final Table table, final int row, final User user) {

		boolean isEconomicalResponsible = false;
		if (null != user) {
			try {
				final Collection users = getSchoolUserHome().findByUser(user);
				if (null != users && !users.isEmpty()) {
					final SchoolUser schoolUser = (SchoolUser) users.iterator().next();
					isEconomicalResponsible = schoolUser.isEconomicalResponsible();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		final String parameterKey = getIsEconomicalResponsibleKey(user);
		final CheckBox isEconomicalResponsibleButton = new CheckBox(parameterKey, "true");
		isEconomicalResponsibleButton.setChecked(isEconomicalResponsible);
		table.mergeCells(1, row, table.getColumns(), row);
		table.add(isEconomicalResponsibleButton, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getTextNormal(_iwrb.getLocalizedString(PARAMETER_IS_ECONOMICAL_RESP, PARAMETER_IS_ECONOMICAL_RESP)), 1, row);
	}

	private static String getIsEconomicalResponsibleKey(final User user) {
		return getIsEconomicalResponsibleKey(null == user ? "" : user.getPrimaryKey() + "");
	}

	private static String getIsEconomicalResponsibleKey(final String postFix) {
		final String result = PARAMETER_IS_ECONOMICAL_RESP + (null == postFix || 0 == postFix.length() ? "" : '_' + postFix);
		return result;
	}

	/**
	 * Returns a UserForm
	 * 
	 * @param userType
	 *          1 = Headmaster, 2 = Assistant Headmaster, 3 = User
	 * @return Table
	 *  
	 */

	private Table getUserForm(IWContext iwc, School school) {
		String name = PARAMETER_SCHOOL_USER_NAME;
		String email = PARAMETER_SCHOOL_USER_EMAIL;
		String phone = PARAMETER_SCHOOL_USER_TELEPHONE;

		Table table = new Table();

		Text tName = getTextNormal(_iwrb.getLocalizedString("school.name", "Name"));
		Text tEmail = getTextNormal(_iwrb.getLocalizedString("school.email", "E-post"));
		Text tPhone = getTextNormal(_iwrb.getLocalizedString("school.phone", "Phone"));
		Text tType = getTextNormal(_iwrb.getLocalizedString("school.type", "Type"));

		DropdownMenu pType = new DropdownMenu(PARAMETER_SCHOOL_USER_TYPE);
		Collection suTypes;
		try {
			suTypes = getSchoolUserBusiness(iwc).getSchoolUserTypes(school);
			if (suTypes != null && !suTypes.isEmpty()) {
				Iterator iter = suTypes.iterator();
				String[] str;
				while (iter.hasNext()) {
					str = (String[]) iter.next();
					pType.addMenuElement(str[2], _iwrb.getLocalizedString(str[0], str[1]));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}

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

		addIsEconomicalResponsibleCheckBox(table, 5, null);

		return table;
	}

	private Table getUserFormHighSchool(IWContext iwc, School school) {
		String name = PARAMETER_SCHOOL_USER_NAME;
		String email = PARAMETER_SCHOOL_USER_EMAIL;
		String phone = PARAMETER_SCHOOL_USER_TELEPHONE;
		String mobilephone = PARAMETER_SCHOOL_USER_MOBILEPHONE;
		//String school_department = PARAMETER_SCHOOL_DEPARTMENT;
		//String school_department_phone = PARAMETER_SCHOOL_DEPARTMENT_PHONE;
		String showcontact = PARAMETER_SCHOOL_SHOW_CONTACT;
		String mainheadmaster = PARAMETER_SCHOOL_MAIN_HEADMASTER;

		Table table = new Table();
		table.setWidth(2, "5");
		table.setWidth(4, "5");
		table.setWidth(6, "5");

		Text tName = getTextNormal(_iwrb.getLocalizedString("school.name", "Name"));
		Text tEmail = getTextNormal(_iwrb.getLocalizedString("school.email", "E-post"));
		Text tPhone = getTextNormal(_iwrb.getLocalizedString("school.phone", "Phone"));
		Text tMobilePhone = getTextNormal(_iwrb.getLocalizedString("school.mobile", "Mobile"));
		Text tType = getTextNormal(_iwrb.getLocalizedString("school.type", "Type"));
		//Malin departement = Enhet
		//Text tDepartment =
		// getTextTitle(_iwrb.getLocalizedString("school.department","Department"));
		Text tDepartmentNorm = getTextNormal(_iwrb.getLocalizedString("school.department", "Department"));
		//Text tCreateDepm =
		// getTextTitle(_iwrb.getLocalizedString("school.create_department","Create
		// new department"));
		Text tAddPerson = getTextTitle(_iwrb.getLocalizedString("school.add_person", "Add new person"));
		Text tShowinContactlist = getTextNormal(_iwrb.getLocalizedString("school.show_in_contactl", "Show in contactlist"));
		Text tMainHeadmaster = getTextNormal(_iwrb.getLocalizedString("school.main_headmaster", "Main headmaster"));

		DropdownMenu pDepartment = new DropdownMenu(PARAMETER_SCHOOL_DEPARTMENT_ID_DROP);

		Collection sDepartments;
		try {
			//PresentationObject parent = this.getParentObject();
			//while (!(parent instanceof SchoolContentEditorBlock)) {
			//parent = parent.getParentObject();
			//}

			//if (parent != null && hasPermission("edit_department", parent, iwc)) {

			sDepartments = getSchoolBusiness(iwc).getSchoolDepartmentHome().findAllDepartmentsBySchool(school);
			if (sDepartments != null && !sDepartments.isEmpty()) {
				Iterator iter = sDepartments.iterator();
				SchoolDepartment schDep;
				while (iter.hasNext()) {
					schDep = (SchoolDepartment) iter.next();

					pDepartment.addMenuElement(schDep.getPrimaryKey().toString(), schDep.getDepartment());
				}
			}
			//}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		DropdownMenu pType = new DropdownMenu(PARAMETER_SCHOOL_USER_TYPE);
		Collection suTypes;
		try {
			suTypes = getSchoolUserBusiness(iwc).getSchoolUserTypes(school);
			if (suTypes != null && !suTypes.isEmpty()) {
				Iterator iter = suTypes.iterator();
				String[] str;
				while (iter.hasNext()) {
					str = (String[]) iter.next();
					pType.addMenuElement(str[2], _iwrb.getLocalizedString(str[0], str[1]));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		//				pType.addMenuElement(SchoolUserBusinessBean.USER_TYPE_HEADMASTER ,
		// _iwrb.getLocalizedString("headmaster","Headmaster"));
		//				pType.addMenuElement(SchoolUserBusinessBean.USER_TYPE_ASSISTANT_HEADMASTER
		// , _iwrb.getLocalizedString("assistant_headmaster","Assistant
		// headmaster"));
		//				pType.addMenuElement(SchoolUserBusinessBean.USER_TYPE_WEB_ADMIN,
		// _iwrb.getLocalizedString("web_administrator","Web administrator"));
		//				pType.addMenuElement(SchoolUserBusinessBean.USER_TYPE_TEACHER ,
		// _iwrb.getLocalizedString("teacher","Teacher"));

		TextInput pName = new TextInput(name);
		TextInput pEmail = new TextInput(email);
		TextInput pPhone = new TextInput(phone);
		TextInput pMobilePhone = new TextInput(mobilephone);

		CheckBox chShowContact = new CheckBox(showcontact, "true");
		CheckBox chMainHeadmaster = new CheckBox(mainheadmaster, "true");

		Link linkiPers = new Link(getBundle().getImage("shared/info.gif"));
		linkiPers.addParameter(PARAMETER_TOPIC_PERSON, SchoolEditorInfoText.PARAMETER_TOPIC_ID_PERSON);
		linkiPers.setWindowToOpen(SchoolEditorInfoText.class);

		this.setTextInputStyle(pName);
		this.setTextInputStyle(pEmail);
		this.setTextInputStyle(pPhone);
		this.setTextInputStyle(pMobilePhone);
		//this.setTextInputStyle(pDepartmentName);
		//this.setTextInputStyle(pDepartmentPhone);

		try {
			//if (hasPermission("edit_department",
			// this.parentObject.getParentObject(), iwc)) {
			/*
			 * table.add(tCreateDepm, 1, 1); table.add(tDepartmentName, 1, 2);
			 * table.add(pDepartmentName, 1, 3); table.add(tPhone, 2, 2);
			 * table.add(tDepartment2, 2, 4); table.add(pDepartmentPhone, 2, 3);
			 * table.add(pDepartment, 2, 5); table.mergeCells(2, 5, 3, 5);
			 */
			//}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		table.add(tAddPerson, 1, 1);
		table.add(tType, 1, 2); //1,4
		table.add(pType, 1, 3); //1,5

		table.add(linkiPers, 3, 1);

		table.add(tDepartmentNorm, 1, 4);
		table.add(pDepartment, 1, 5);

		table.add(tName, 1, 6); //1,6
		table.add(pName, 1, 7);//1,7

		table.add(chShowContact, 5, 3);
		table.add(tShowinContactlist, 5, 3);
		table.add(chMainHeadmaster, 7, 3);
		table.add(tMainHeadmaster, 7, 3);
		table.add(tEmail, 3, 6); //2,6
		table.add(pEmail, 3, 7); //2,7

		table.add(tPhone, 5, 6); //3,6
		table.add(pPhone, 5, 7);//3,7

		table.add(tMobilePhone, 7, 6); //4,6
		table.add(pMobilePhone, 7, 7); //4,7
		table.mergeCells(1, 3, 3, 3);
		table.mergeCells(1, 5, 7, 5);

		addIsEconomicalResponsibleCheckBox(table, 8, null);

		return table;
	}

	private Table getDepartmentForm() {
		String school_department = PARAMETER_SCHOOL_DEPARTMENT;
		String school_department_phone = PARAMETER_SCHOOL_DEPARTMENT_PHONE;

		Table table = new Table();

		table.setWidth(3, "300");
		table.setAlignment(3, 1, "right");
		//Malin departement = Enhet
		Text tPhone = getTextNormal(_iwrb.getLocalizedString("school.phone", "Phone"));
		//Text tDepartment =
		// getTextTitle(_iwrb.getLocalizedString("school.department","Department"));
		Text tDepartmentName = getTextNormal(_iwrb.getLocalizedString("school.department_name", "Department name"));
		//Text tDepartment2 =
		// getTextNormal(_iwrb.getLocalizedString("school.department","Department"));
		Text tCreateDepm = getTextTitle(_iwrb.getLocalizedString("school.create_department", "Create new department"));
		//Text tAddPerson =
		// getTextTitle(_iwrb.getLocalizedString("school.add_person","Add new
		// person"));
		//Text tShowinContactlist =
		// getTextNormal(_iwrb.getLocalizedString("school.show_in_contactl","Show in
		// contactlist"));

		TextInput pDepartmentName = new TextInput(school_department);
		TextInput pDepartmentPhone = new TextInput(school_department_phone);

		Link linkiDepm = new Link(getBundle().getImage("shared/info.gif"));
		linkiDepm.addParameter(PARAMETER_TOPIC_DEPM, SchoolEditorInfoText.PARAMETER_TOPIC_ID_DEPM);
		linkiDepm.setWindowToOpen(SchoolEditorInfoText.class);

		Link linkiGeneralInfo = new Link(getBundle().getImage("shared/info.gif"));
		linkiGeneralInfo.addParameter(PARAMETER_TOPIC_EDITOR, SchoolEditorInfoText.PARAMETER_TOPIC_ID_EDITOR);
		linkiGeneralInfo.setWindowToOpen(SchoolEditorInfoText.class);
		//String sText = _iwrb.getLocalizedString("school.infoDepm","Om
		// organisationen är enhetsindelad kan du i systemet skapa enheter genom att
		// skriva in enhetens namn och eventuellt telefonnummer under rubriken
		// 'Skapa en enhet' och klicka på 'Spara'. ");
		//linkiDepm.setToolTip(sText);

		this.setTextInputStyle(pDepartmentName);
		this.setTextInputStyle(pDepartmentPhone);

		try {
			//if (hasPermission("edit_department",
			// this.parentObject.getParentObject(), iwc)) {
			table.add(linkiGeneralInfo, 3, 1);
			table.add(tCreateDepm, 1, 2);
			table.add(tDepartmentName, 1, 3);
			table.add(pDepartmentName, 1, 4);
			table.add(linkiDepm, 2, 2);
			table.add(tPhone, 2, 3);
			//table.add(tDepartment2, 2, 4);
			table.add(pDepartmentPhone, 2, 4);
			//table.add(pDepartment, 2, 5);
			table.mergeCells(2, 5, 3, 5);

			//}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}

		return table;
	}

	public void deleteUser(IWContext iwc, School school) throws RemoteException, FinderException {
		String uId = iwc.getParameter(PARAMETER_DELTE_USER);
		if (uId != null) {
			UserHome userHome = (UserHome) IDOLookup.getHome(User.class);
			User user = userHome.findByPrimaryKey(new Integer(uId));
			try {
				if (iwc == null) System.out.println("[SchoolUserEditor:deleteUser] iwc    == null");
				if (user == null) System.out.println("[SchoolUserEditor:deleteUser] user   == null");
				if (school == null) System.out.println("[SchoolUserEditor:deleteUser] school == null");
				//      		get current user (added by Thomas)
				User currentUser = iwc.getCurrentUser();
				getSchoolUserBusiness(iwc).removeUser(school, user, currentUser);
				//				user.remove();
			}
			catch (RemoveException e) {
				System.out.println("user to delete ERROR");
				e.printStackTrace(System.err);
			}
		}

	}

	public boolean updateUsers(IWContext iwc, School school) throws RemoteException {
		String sname = PARAMETER_SCHOOL_USER_NAME;
		String semail = PARAMETER_SCHOOL_USER_EMAIL;
		String sphone = PARAMETER_SCHOOL_USER_TELEPHONE;
		String smobilephone = PARAMETER_SCHOOL_USER_MOBILEPHONE;
		String sid = PARAMETER_SCHOOL_USER_ID;
		String sdepid = PARAMETER_SCHOOL_DEPARTMENT_ID_DROP;

		String sUserType = iwc.getParameter(PARAMETER_SCHOOL_USER_TYPE);
		String sDep_ID = iwc.getParameter(sdepid);
		boolean showcontact = Boolean.valueOf(iwc.getParameter(PARAMETER_SCHOOL_SHOW_CONTACT)).booleanValue();
		boolean showcontactEdit = Boolean.valueOf(iwc.getParameter(PARAMETER_SCHOOL_SHOW_CONTACT_EDIT)).booleanValue();
		boolean main_headmaster = Boolean.valueOf(iwc.getParameter(PARAMETER_SCHOOL_MAIN_HEADMASTER)).booleanValue();

		int iUserType = Integer.parseInt(sUserType);
		String category = getSchoolUserBusiness(iwc).getSchoolCategory(school);
		Group priGroup = null;
		try {
			if (category.equalsIgnoreCase(getSchoolUserBusiness(iwc).getSchoolBusiness().getElementarySchoolSchoolCategory()))
				priGroup = getSchoolBusiness(iwc).getRootSchoolAdministratorGroup();
			else if (category.equalsIgnoreCase(getSchoolUserBusiness(iwc).getSchoolBusiness().getChildCareSchoolCategory()))
				priGroup = getSchoolBusiness(iwc).getRootProviderAdministratorGroup();
			else if (category.equalsIgnoreCase(getSchoolUserBusiness(iwc).getSchoolBusiness().getHighSchoolSchoolCategory()))
				priGroup = getSchoolBusiness(iwc).getRootProviderAdministratorGroup();
			if (category.equalsIgnoreCase(getSchoolUserBusiness(iwc).getSchoolBusiness().getCategoryMusicSchool().getCategory()))
				priGroup = getSchoolBusiness(iwc).getRootMusicSchoolAdministratorGroup();
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

				String name = iwc.getParameter(sname + "_" + hId);
				sUserType = iwc.getParameter(PARAMETER_SCHOOL_USER_TYPE + "_" + hId);
				iUserType = Integer.parseInt(sUserType);

				if (name.equals("")) {
					try {
						// get current user (added by Thomas)
						User currentUser = iwc.getCurrentUser();
						getSchoolUserBusiness(iwc).removeUser(school, user, currentUser);
					}
					catch (RemoveException e) {
						e.printStackTrace(System.err);
					}
				}
				else {
					getUserBusiness(iwc).updateUser(user, name, "", "", null, null, null, null, null, (Integer) priGroup.getPrimaryKey());
					final String parameterKey = getIsEconomicalResponsibleKey(hId);
					final boolean isEconomicalResponsible = Boolean.valueOf(iwc.getParameter(parameterKey)).booleanValue();
					getSchoolUserBusiness(iwc).updateSchUser(school, user, iUserType, showcontactEdit, isEconomicalResponsible);

					try {
						getSchoolUserBusiness(iwc).setUserGroups(school, user, iUserType);
					}
					catch (Exception e) {
						debug("User already in headmasterGroup");
					}

					Collection emails = user.getEmails();
					Collection phones = user.getPhones();

					if (emails != null) {
						user.removeAllEmails();
						Email email;
						Iterator iter = emails.iterator();
						while (iter.hasNext()) {
							Object prK = iter.next();
							String sEmail = iwc.getParameter(semail + "_" + hId + "_" + prK);
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
							String sPhone = iwc.getParameter(sphone + "_" + hId + "_" + prK);
							String sMobilePhone = iwc.getParameter(smobilephone + "_" + hId + "_" + prK);
							if (sPhone != null && !sPhone.equals("")) {
								phone = (Phone) prK;
								phone.setNumber(sPhone);
								phone.store();
								user.addPhone(phone);
							}
							else if (sMobilePhone != null && !sMobilePhone.equals("")) {
								phone = (Phone) prK;
								phone.setNumber(sMobilePhone);
								phone.setPhoneTypeId(phone.getPhoneTypeId());
								phone.store();
								user.addPhone(phone);
							}
						}
					}

					String newEmail = iwc.getParameter(semail + "_" + hId);
					String newPhone = iwc.getParameter(sphone + "_" + hId);
					String newMobilePhone = iwc.getParameter(smobilephone + "_" + hId);

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

					if (newMobilePhone != null && !newMobilePhone.equals("")) {
						Phone phone = ((PhoneHome) IDOLookup.getHome(Phone.class)).create();
						phone.setNumber(newMobilePhone);
						phone.setPhoneTypeId(mobilePhoneType);
						phone.store();
						user.addPhone(phone);
					}

					postSaveUpdate(school, user, iUserType);
				}

			}

			/** Adding headmaster */
			String headmaster = iwc.getParameter(sname);
			String hmEmail = iwc.getParameter(semail);
			String hmPhone = iwc.getParameter(sphone);
			String hmMobile = iwc.getParameter(smobilephone);
			int schdep_id = -1;

			if (sDep_ID != null && !sDep_ID.equals("")) {
				schdep_id = Integer.parseInt(sDep_ID);
			}

			if (headmaster != null && !headmaster.equals("") && priGroup != null) {
				final String parameterKey = getIsEconomicalResponsibleKey("");
				final boolean isEconomicalResponsible = Boolean.valueOf(iwc.getParameter(parameterKey)).booleanValue();
				User user = getUserBusiness(iwc).createUser(headmaster, "", "", ((Integer) priGroup.getPrimaryKey()).intValue());

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

				if (hmMobile != null && !hmMobile.equals("")) {
					Phone phone = ((PhoneHome) IDOLookup.getHome(Phone.class)).create();
					phone.setNumber(hmMobile);
					phone.setPhoneTypeId(mobilePhoneType);
					phone.store();
					user.addPhone(phone);
				}

				SchoolUser schUsr;
				if (_highSchoolCategory) {
					schUsr = getSchoolUserBusiness(iwc).addUser(school, user, iUserType, showcontact, main_headmaster, isEconomicalResponsible);
				}
				else {
					schUsr = getSchoolUserBusiness(iwc).addUser(school, user, iUserType, isEconomicalResponsible);
				}

				postSaveNew(school, user, iUserType);
				try {
					if (schdep_id != -1) {
						getSchoolBusiness(iwc).addSchoolUsr(schdep_id, schUsr);
					}
				}
				catch (RemoteException e2) {
					e2.printStackTrace();
				}
				catch (RemoveException e2) {
					e2.printStackTrace();
				}

			}

			return true;
		}
		catch (IDORemoveRelationshipException e) {
			e.printStackTrace(System.err);
		}
		catch (IDOAddRelationshipException e) {
			e.printStackTrace(System.err);
		}
		catch (FinderException e) {
			e.printStackTrace(System.err);
		}
		catch (CreateException e) {
			e.printStackTrace(System.err);
		}
		return false;
	}

	public void deleteDepartment(IWContext iwc, School school) throws FinderException, RemoteException {
		String depmId = iwc.getParameter(PARAMETER_DELETE_SCH_DEP);
		if (depmId != null) {
			//String schoolId = iwc.getParameter(PARAMETER_SCHOOL_ID);

			SchoolDepartmentHome schoolDepHome = (SchoolDepartmentHome) IDOLookup.getHome(SchoolDepartment.class);
			SchoolDepartment schDep;
			schDep = schoolDepHome.findByPrimaryKey(new Integer(depmId));

			Collection sUsers = getSchoolUserBusiness(iwc).getUsersByDepartm(school, new Integer(depmId).intValue());
			try {
				if (sUsers != null && !sUsers.isEmpty()) {
					add(getTextNormal(_iwrb.getLocalizedString("depmartment_not_deleted", "There are users belonging to this department. Start deleting user.")));
				}
				else {
					getSchoolBusiness(iwc).removeDepartment(schDep);
				}
			}
			catch (RemoveException e) {
				System.out.println("department to delete ERROR");
				e.printStackTrace(System.err);
			}

		}

	}

	public boolean updateDepartment(IWContext iwc) throws RemoteException {
		String sdepartmentname = PARAMETER_SCHOOL_DEPARTMENT;
		String sdepartmentphone = PARAMETER_SCHOOL_DEPARTMENT_PHONE;

		String schoolId = iwc.getParameter(PARAMETER_SCHOOL_ID);
		String sdid = iwc.getParameter(PARAMETER_SCHOOL_DEPARTMENT_ID);
		//String idid = iwc.getParameter(sdid);
		////int school_id = PARAMETER_SCHOOL_ID;
		//String dep_upd = iwc.getParameter(ACTION_UPDATE_DEPM);

		/** Updating departments */
		//try {
		if (sdid != null) {
			/*
			 * SchoolDepartmentHome schoolDepHome = (SchoolDepartmentHome)
			 * IDOLookup.getHome(SchoolDepartment.class); SchoolDepartment schDep; try {
			 * schDep = schoolDepHome.findByPrimaryKey(new Integer(sdid)); }catch
			 * (Exception e) { return false; }
			 */

			String departmentname = iwc.getParameter(sdepartmentname + "_" + sdid);
			String departmentphone = iwc.getParameter(sdepartmentphone + "_" + sdid);
			if (!departmentname.equals("")) {
				try {
					getSchoolBusiness(iwc).storeSchoolDepartment(departmentname, departmentphone, new Integer(schoolId).intValue(), new Integer(sdid).intValue());
					//getSchoolBusiness(iwc).removeDepartment(schDep);
				}
				catch (Exception e) {
					return false;
				}
			}

		} //end if sdid
		else {
			/** Adding department* */
			//storeSchooldepartment(string string int, -1 for new department_id)
			String departmentname = iwc.getParameter(sdepartmentname);
			String departmentphone = iwc.getParameter(sdepartmentphone);

			if (departmentname != null && !departmentname.equals("")) {
				getSchoolBusiness(iwc).storeSchoolDepartment(departmentname, departmentphone, new Integer(schoolId).intValue(), -1);
			}
			/** Adding department end* */

		}

		return true;
	}

	/**
	 * Override me please.
	 * 
	 * @param school
	 *          School
	 * @param user
	 *          User
	 */
	protected void postSaveNew(School school, User user, int userType) throws RemoteException {
		// This is overridden in
		// se.idega.idegaweb.commune.school.presentation.SchoolUserEditor,
		//so pahaaleeeeease do NOT remove the parameters, or the users int the
		// school system will create incorrectly
	}

	/**
	 * Override me please.
	 * 
	 * @param school
	 *          School
	 * @param user
	 *          User
	 */
	protected void postSaveUpdate(School school, User user, int userType) throws RemoteException {
		// This is overridden in
		// se.idega.idegaweb.commune.school.presentation.SchoolUserEditor,
		//so pahaaleeeeease do NOT remove the parameters, or the users int the
		// school system will create incorrectly
	}

	private Table mainForm(IWContext iwc) throws RemoteException {
		Table table = new Table(2, 2);
		table.add(getTextTitle(_iwrb.getLocalizedString("school.select_school", "Select School")), 1, 1);
		table.add(getTextTitle(_school.getName()), 2, 1);
		table.add(schoolList(iwc), 1, 2);
		table.add(schoolUsers(iwc, _school), 2, 2);
		table.setVerticalAlignment(1, 2, Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(2, 2, Table.VERTICAL_ALIGN_TOP);
		return table;
	}

	protected UserBusiness getUserBusiness(IWApplicationContext iwac) throws RemoteException {
		return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
	}

	protected SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}

	protected SchoolHome getSchoolHome() throws RemoteException {
		return (SchoolHome) IDOLookup.getHome(School.class);
	}

	public Table getSchoolUsersTable(IWContext iwc, School school, boolean addSubmitButton) throws RemoteException {
		_school = school;
		return schoolUsersTable(iwc, school, addSubmitButton);
	}

	public Table getHighSchoolUsersTable(IWContext iwc, School school, boolean addSubmitButton) throws Exception {
		_school = school;
		return highschoolUsersTable(iwc, school, addSubmitButton);
	}

	private void init(IWContext iwc) throws RemoteException {
		_iwrb = super.getResourceBundle(iwc);
		_tFormat = TextFormat.getInstance();
		_iwb = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);

		String schoolId = iwc.getParameter(PARAMETER_SCHOOL_ID);
		if (schoolId != null) {
			try {
				_school = getSchoolHome().findByPrimaryKey(new Integer(schoolId));
			}
			catch (FinderException e) {
				e.printStackTrace(System.err);
			}
		}

		String uId = iwc.getParameter(PARAMETER_EDIT_USER);
		if (uId != null) {
			userToEdit = Integer.parseInt(uId);
		}

		String schdepmId = iwc.getParameter(PARAMETER_EDIT_SCH_DEP);
		if (schdepmId != null) {
			depmToEdit = Integer.parseInt(schdepmId);
		}

		try {
			deleteUser(iwc, _school);
		}
		catch (FinderException e) {
			add(getTextNormal(_iwrb.getLocalizedString("user_not_deleted", "User not deleted")));
		}

		try {
			deleteDepartment(iwc, _school);
		}
		catch (FinderException e) {
			add(getTextNormal(_iwrb.getLocalizedString("depmartment_not_deleted", "Department not deleted")));
		}

	}

	public SchoolUserEditor(IWContext iwc) throws RemoteException {
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
		if (_school != null) {
			try {
				Collection coll = _school.getSchoolTypes();
				Iterator iterCollection = coll.iterator();

				while (iterCollection.hasNext()) {
					SchoolType schoolType = (SchoolType) iterCollection.next();
					String schoolCategory = schoolType.getSchoolCategory();
					if (schoolCategory.equalsIgnoreCase(getSchoolUserBusiness(iwc).getSchoolBusiness().getHighSchoolSchoolCategory())) {
						_highSchoolCategory = true;
					}
				}

			}
			catch (Exception e) {
			}
		}

		String action = iwc.getParameter(PARAMETER_ACTION);

		if (action == null) {
			add(schoolList(iwc));
		}
		else if (action.equals(ACTION_VIEW_SCHOOL) && _school != null) {
			add(mainForm(iwc));
		}
		else if (action.equals(ACTION_UPDATE) && _school != null) {
			updateUsers(iwc, _school);
			add(mainForm(iwc));
		}
		else if (action.equals(ACTION_UPDATE_DEPM)) {
			updateDepartment(iwc); //Malin
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

	public IWBundle getBundle() {
		return this._iwb;
	}

	public void setSchoolTypeCategory(String typeCategory) {
		if (typeCategory != null && !typeCategory.equals("")) {
			try {
				SchoolTypeHome sth = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
				schoolTypeIds = sth.findAllByCategory(typeCategory);
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}

	}

	private static SchoolUserHome getSchoolUserHome() throws IDOLookupException {
		return (SchoolUserHome) IDOLookup.getHome(SchoolUser.class);
	}
}