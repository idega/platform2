package com.idega.block.school.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolDepartment;
import com.idega.block.school.data.SchoolDepartmentHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolUser;
import com.idega.block.school.data.SchoolUserBMPBean;
import com.idega.block.school.data.SchoolUserHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

/**
 * @author gimmi
 */

public class SchoolUserBusinessBean extends IBOServiceBean implements SchoolUserBusiness{

	public static final int USER_TYPE_HEADMASTER = SchoolUserBMPBean.USER_TYPE_HEADMASTER;
	public static final int USER_TYPE_ASSISTANT_HEADMASTER = SchoolUserBMPBean.USER_TYPE_ASSISTANT_HEADMASTER;
	public static final int USER_TYPE_TEACHER = SchoolUserBMPBean.USER_TYPE_TEACHER;
	public static final int USER_TYPE_WEB_ADMIN = SchoolUserBMPBean.USER_TYPE_WEB_ADMIN;
	public static final int USER_TYPE_IB_COORDINATOR = SchoolUserBMPBean.USER_TYPE_IB_COORDINATOR;
	public static final int USER_TYPE_STUDY_AND_WORK_COUNCEL = SchoolUserBMPBean.USER_TYPE_STUDY_AND_WORK_COUNCEL;
	
	public SchoolUser addUser(School school, User user, int userType, boolean isEconomicalResponsible) throws RemoteException, CreateException, FinderException {
		return addUser (school, user, userType, true, false, isEconomicalResponsible);
	}
	
	public SchoolUser addUser(School school, User user, int userType, boolean showInContacts, boolean main_headmaster, boolean isEconomicalResponsible) throws RemoteException, CreateException, FinderException {
			SchoolUser sUser = getSchoolUserHome().create();
			sUser.setSchoolId(((Integer) school.getPrimaryKey()).intValue());
			sUser.setUserId(((Integer) user.getPrimaryKey()).intValue());
			sUser.setUserType(userType);
			sUser.setShowInContact(showInContacts);
			sUser.setMainHeadmaster(main_headmaster);
			sUser.setIsEconomicalResponsible (isEconomicalResponsible);
			sUser.store();
		
			setUserGroups(school, user, userType);
		
			return sUser;
		}

	public SchoolUser updateSchUser(School school, User user, int userType, boolean showInContacts, boolean isEconomicalResponsible) throws RemoteException, FinderException {
			//SchoolUser sUser = getSchoolUserHome().findByPrimaryKey(user.getPrimaryKey().intValue());
			Object id = null;
			SchoolUser sUser = null;
			id = getSchoolUserHome().getSchoolUserId(school, user, userType);
			
			if (id != null) {
				sUser = getSchoolUserHome().findByPrimaryKey(id);
				//sUser.setSchoolId(((Integer) school.getPrimaryKey()).intValue());
				//sUser.setUserId(((Integer) user.getPrimaryKey()).intValue());
				//sUser.setUserType(userType);
				sUser.setShowInContact(showInContacts);
				sUser.setIsEconomicalResponsible (isEconomicalResponsible);
				sUser.store();		
				setUserGroups(school, user, userType);
			}			
		
			return sUser;
		}

	public SchoolUser addTeacher(School school, User user) throws RemoteException, CreateException, FinderException {
		return addUser(school, user, USER_TYPE_TEACHER,false);	
	}

	public SchoolUser addHeadmaster(School school, User user) throws RemoteException, CreateException, FinderException {
		SchoolUser sUser = addUser(school, user, USER_TYPE_HEADMASTER,false);
		return sUser;
	}

	public SchoolUser addAssistantHeadmaster(School school, User user) throws RemoteException, CreateException, FinderException {
		return addUser(school, user, USER_TYPE_ASSISTANT_HEADMASTER,false);	
	}

	public SchoolUser addWebAdmin(School school, User user) throws RemoteException, CreateException, FinderException {
		return addUser(school, user, USER_TYPE_WEB_ADMIN,false);	
	}

	public void setUserGroups(School school, User user, int userType) throws RemoteException, FinderException {
		// code of death, fix later... 
		if (userType == USER_TYPE_HEADMASTER || userType == USER_TYPE_ASSISTANT_HEADMASTER || userType == USER_TYPE_WEB_ADMIN) {
			getSchoolBusiness().addHeadmaster(school, user);
		}
	}

	public void removeUser(School school, User user, int userType, User currentUser)  throws FinderException, RemoteException, RemoveException{
		Object id = null;
		id = getSchoolUserHome().getSchoolUserId(school, user, userType);
		if (id != null) {
			SchoolUser sUser = getSchoolUserHome().findByPrimaryKey(id);
			sUser.remove();
		}
		getUserBusiness().deleteUser(user, currentUser);
	}
	
		public void removeUser(School school, User user, User currentUser) throws FinderException, RemoteException, RemoveException {
		Collection coll	= getSchoolUserHome().findBySchoolAndUser(school, user);
		if (coll != null && coll.size() > 0 ) {
			SchoolUser sUser;
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				sUser = (SchoolUser)iter.next();	
				SchoolDepartmentHome schdephome = getSchoolBusiness().getSchoolDepartmentHome();
				Collection collSchdep = schdephome.findAllDepartmentsByUser(sUser);
				Iterator iter2 = collSchdep.iterator();
				while (iter2.hasNext()) {
					SchoolDepartment sd = (SchoolDepartment) iter2.next();
					try {
						sd.removeSchoolUser(sUser);
					} catch (IDORemoveRelationshipException e) {
						e.printStackTrace();
					}
				}
				
				sUser.remove();
			}
		}
		
		getUserBusiness().deleteUser(user, currentUser);
	}

	/**
	 * Gets the Users of type Teacher for School with id schID
	 * @return A collection of com.idega.user.data.User entites
	 */
	public Collection getTeachers(int schoolID) throws RemoteException, FinderException {
		return getTeachers(getSchoolHome().findByPrimaryKey(new Integer(schoolID)));	
	}
	
	/**
	 * Gets the UserIds for Users of type Teacher for School with id schID
	 * @return A collection of Integer PKs
	 */
	public Collection getTeacherUserIds(int schoolID) throws RemoteException, FinderException {
		return getTeacherUserIds(getSchoolHome().findByPrimaryKey(new Integer(schoolID)));	
	}
	/**
	 * Gets the Users of type Teacher for School school
	 * @return A collection of com.idega.user.data.User entites
	 */
	public Collection getTeachers(School school) throws RemoteException, FinderException{
		return getUsers(school, USER_TYPE_TEACHER);	
	}
	
	/**
	 * Gets the UserIds for Users of type Teacher for School school
	 * @return A collection of Integer PKs
	 */
	public Collection getTeacherUserIds(School school) throws RemoteException, FinderException{
		return getUserIds(school, USER_TYPE_TEACHER);	
	}

	/**
	 * Gets the Users of type Headmaster for School school
	 * @return A collection of com.idega.user.data.User entites
	 */
	public Collection getHeadmasters(School school) throws RemoteException, FinderException{
		return getUsers(school, USER_TYPE_HEADMASTER);	
	}
/**
	 * Gets the Users of type Main Headmaster for School school
	 * @return A collection of com.idega.user.data.User entites
	 */
	public Collection getMainHeadmasters(School school) throws RemoteException, FinderException{
		return getUsersByMainHeadMaster(school, USER_TYPE_HEADMASTER, true);	
	}
	/**
	 * Gets the Users of type AssistantHeadMaster for School school
	 * @return A collection of com.idega.user.data.User entites
	 */
	public Collection getAssistantHeadmasters(School school) throws RemoteException, FinderException{
		return getUsers(school, USER_TYPE_ASSISTANT_HEADMASTER);	
	}
	/**
	 * Gets the Users of type WebAdmin for School school
	 * @return A collection of com.idega.user.data.User entites
	 */
	public Collection getWebAdmins(School school) throws RemoteException, FinderException{
		return getUsers(school, USER_TYPE_WEB_ADMIN);	
	}

	public Collection getEconomicalResponsibles(School school) throws RemoteException, FinderException{
		Collection schUsers = getSchoolUserHome().findBySchoolAndIsEconomicalResponsible(school);
		Collection users = new Vector();
		Iterator iter = schUsers.iterator();
		while (iter.hasNext()) {
			SchoolUser sUser = (SchoolUser)iter.next();
			users.add(sUser.getUser());
		}
		return users;
	}

	/**
	 * Gets the Users of a aspecific type for School school
	 * @return A collection of com.idega.user.data.User entites
	 */
	public Collection getUsers(School school, int userType) throws RemoteException, FinderException {
		Collection schUsers = getSchoolUserHome().findBySchoolAndType(school, userType);
		Collection users = new Vector();
		Iterator iter = schUsers.iterator();
		while (iter.hasNext()) {
			SchoolUser sUser = (SchoolUser)iter.next();
			users.add(sUser.getUser());
		}
		return users;
	}

	/**Malin
		 * Gets the Users of a specific type for School school and usertype and specific department
		 * @return A collection of com.idega.user.data.User entites
		 */
		public Collection getUsersByDepartm(School school, int userType, int departmentID) throws RemoteException, FinderException {
			Collection schUsers = getSchoolUserHome().findBySchoolAndTypeAndDepartment(school, userType, departmentID);
			Collection users = new Vector();
			Iterator iter = schUsers.iterator();
			while (iter.hasNext()) {
				SchoolUser sUser = (SchoolUser)iter.next();
				users.add(sUser.getUser());
			}
			return users;
		}	
		
	/**Malin
			 * Gets the Users of a specific type for School school and specific department
			 * @return A collection of com.idega.user.data.User entites
			 */
		public Collection getUsersByDepartm(School school, int departmentID) throws RemoteException, FinderException {
			Collection schUsers = getSchoolUserHome().findBySchoolAndDepartment(school, departmentID);
			Collection users = new Vector();
			Iterator iter = schUsers.iterator();
			while (iter.hasNext()) {
				SchoolUser sUser = (SchoolUser)iter.next();
				users.add(sUser.getUser());
			}
			return users;
		}	
	
	/**Malin
			 * Gets the Users of a specific type for School school and specific department
			 * @return A collection of com.idega.user.data.User entites
			 */
		public Collection getUsersByMainHeadMaster(School school,  int userType, boolean main_headmaster) throws RemoteException, FinderException {
			Collection schUsers = getSchoolUserHome().findBySchoolAndMainHeadmaster(school, userType, main_headmaster);
			Collection users = new Vector();
			Iterator iter = schUsers.iterator();
			while (iter.hasNext()) {
				SchoolUser sUser = (SchoolUser)iter.next();
				users.add(sUser.getUser());
			}
			return users;
		}	
	
	
	
	/**
	 * Malin
		 * Gets a collection of true or false if SchoolUsers should be shown in the contact list 
		 *  @return A collection of com.idega.user.data.SchoolUser entites
		 */
	public boolean getUserShowInContact(User user) throws RemoteException, FinderException {
			//Collection schUsers = getSchoolUserHome().findBySchoolAndUser(school, user);
			//borde ev göras om lite eftersom det skulle kunna finnas flera school users på en user
		Collection schUsers = getSchoolUserHome().findByUser(user);
			Iterator iter = schUsers.iterator();
			boolean show = true;
			while (iter.hasNext()) {
				SchoolUser sUser = (SchoolUser)iter.next();
				show = sUser.getShowInContact();
				//showContact = (String[]) show;
			}
			
			return show;
		}
	public boolean getUserMainHeadmaster(User user) throws RemoteException, FinderException {
		//Collection schUsers = getSchoolUserHome().findBySchoolAndUser(school, user);
		//borde ev göras om lite eftersom det skulle kunna finnas flera school users på en user
		Collection schUsers = getSchoolUserHome().findByUser(user);
		Iterator iter = schUsers.iterator();
		boolean main_head = false;
		while (iter.hasNext()) {
			SchoolUser sUser = (SchoolUser)iter.next();
			main_head = sUser.getMainHeadmaster();
			
		}
	
		return main_head;
	}

	/**
	 * Gets the UserIds of a aspecific type for School school
	 * @return A collection of Integer Primary keys for com.idega.user.data.User
	 */
	public Collection getUserIds(School school, int userType) throws RemoteException, FinderException {
		Collection schUsers = getSchoolUserHome().findBySchoolAndType(school, userType);
		Collection users = new Vector();
		Iterator iter = schUsers.iterator();
		while (iter.hasNext()) {
			SchoolUser sUser = (SchoolUser)iter.next();
			users.add(sUser.getUser().getPrimaryKey());
		}
		return users;
	}


	public Collection getSchools(User user) throws RemoteException, FinderException {
		Collection schUsers = getSchoolUserHome().findByUser(user);
		if (schUsers != null || !schUsers.isEmpty()) {
			Collection coll = new Vector();
			Iterator iter = schUsers.iterator();
			SchoolUser sUser;
			while (iter.hasNext()) {
				sUser = (SchoolUser)iter.next();
				coll.add(new Integer(sUser.getSchoolId()));	
			}
			return coll;
		}

		return null;
	}
	
	/**
 *	Returns a collection of Strings. "SCHOOL" or "CHILDCARE" or both or "HIGH_SCHOOL" 
	 *	//added handling for Highschool (Malin) 
	 */
	public Collection getSchoolTypeCategories(School school) throws IDORelationshipException, RemoteException {
		Collection sTypes = school.getSchoolTypes();
		SchoolType sType;
		boolean SCHOOL = false;
		boolean CHILDCARE = false;
		boolean HIGH_SCHOOL = false; 
		boolean MUSIC_SCHOOL = false; 
		
		if (sTypes != null && !sTypes.isEmpty() ) {
			Iterator iter = sTypes.iterator();
			String sCat;
			while (iter.hasNext()) {
				sType = (SchoolType)(iter.next());
				sCat = sType.getSchoolCategory();
				String strElementary = getSchoolBusiness().getElementarySchoolSchoolCategory();
				String strChildcare = getSchoolBusiness().getChildCareSchoolCategory();
				String strHighschool = getSchoolBusiness().getHighSchoolSchoolCategory();
				String strMusicschool = getSchoolBusiness().getCategoryMusicSchool().getCategory();

				if (sCat != null && sCat.equals(strElementary)){
					SCHOOL = true;
				}else if (sCat != null && sCat.equals(strChildcare)) {
					CHILDCARE = true;
				}else if (sCat != null && sCat.equals(strHighschool)) {
					HIGH_SCHOOL = true;
				}else if (sCat != null && sCat.equals(strMusicschool)) {
					MUSIC_SCHOOL = true;
				}
			}
		}
		
		Collection coll = new Vector();
		if (SCHOOL) {
			coll.add(getSchoolBusiness().getElementarySchoolSchoolCategory());	
		}
		if (CHILDCARE) {
			coll.add(getSchoolBusiness().getChildCareSchoolCategory());	
		}
		if (HIGH_SCHOOL){
			coll.add(getSchoolBusiness().getHighSchoolSchoolCategory());
		}
		if (MUSIC_SCHOOL){
			coll.add(getSchoolBusiness().getCategoryMusicSchool().getCategory());
		}
		
		return coll;
	}
	/**
	 * Returns a collection of String[], [0] = localization key for schoolUserType
	 * [1] = default value if localization in null  [2] = userTypeId.
	 * @param school
	 * @return Collection
	 * @throws IDORelationshipException
	 * @throws RemoteException
	 * @throws FinderException
	 */
	public Collection getSchoolUserTypes(School school) throws IDORelationshipException, RemoteException {
		Collection schoolTypeCategories = getSchoolTypeCategories(school);
		Collection userTypes = null;
		if (schoolTypeCategories != null && !schoolTypeCategories.isEmpty()) {
			userTypes = new Vector();
			String category = "";
			if (schoolTypeCategories.size() == 1){
				Iterator iter = schoolTypeCategories.iterator();
				while (iter.hasNext()) {
					category = (String) iter.next();
				}	
			}else {
				category = "BOTH";
			}

			if (category.equals(getSchoolBusiness().getElementarySchoolSchoolCategory()) || category.equals("BOTH")) {
				userTypes.add(new String[] {"school.headmaster", "Headmaster", Integer.toString(USER_TYPE_HEADMASTER) });
				userTypes.add(new String[] {"school.assistant_headmaster", "Assistant headmaster", Integer.toString(USER_TYPE_ASSISTANT_HEADMASTER) });
				userTypes.add(new String[] {"school.web_administrators", "Web administrators", Integer.toString(USER_TYPE_WEB_ADMIN) });
				userTypes.add(new String[] {"school.teachers", "Teachers", Integer.toString(USER_TYPE_TEACHER) });
				userTypes.add(new String[] {"school.ib_coordinator", "IB-coordinator", Integer.toString(USER_TYPE_IB_COORDINATOR) });
				userTypes.add(new String[] {"school.study_work_councel", "Study and work councel", Integer.toString(USER_TYPE_STUDY_AND_WORK_COUNCEL) });
			}
			else if (category.equals(getSchoolBusiness().getChildCareSchoolCategory())) {
				userTypes.add(new String[] {"school.manager", "Manager", Integer.toString(USER_TYPE_HEADMASTER) });
				userTypes.add(new String[] {"school.assistant_manager", "Assistant manager", Integer.toString(USER_TYPE_ASSISTANT_HEADMASTER) });
				userTypes.add(new String[] {"school.web_administrators", "Web administrators", Integer.toString(USER_TYPE_WEB_ADMIN) });
				userTypes.add(new String[] {"school.teachers", "Teachers", Integer.toString(USER_TYPE_TEACHER) });
			}
			else if (category.equals(getSchoolBusiness().getHighSchoolSchoolCategory())) {
				//userTypes.add(new String[] {"school.main_headmaster", "Main headmaster", Integer.toString(USER_TYPE_MAIN_HEADMASTER) });
				userTypes.add(new String[] {"school.headmaster", "Headmaster", Integer.toString(USER_TYPE_HEADMASTER) });
				userTypes.add(new String[] {"school.assistant_headmaster_abbrev", "Ass. headmaster", Integer.toString(USER_TYPE_ASSISTANT_HEADMASTER) });
				userTypes.add(new String[] {"school.web_administrators", "Web administrators", Integer.toString(USER_TYPE_WEB_ADMIN) });
				userTypes.add(new String[] {"school.teachers", "Teachers", Integer.toString(USER_TYPE_TEACHER) });
				userTypes.add(new String[] {"school.ib_coordinator", "IB-coordinator", Integer.toString(USER_TYPE_IB_COORDINATOR) });
				userTypes.add(new String[] {"school.study_work_councel", "Study and work councel", Integer.toString(USER_TYPE_STUDY_AND_WORK_COUNCEL) });
			}
			else if (category.equals(getSchoolBusiness().getCategoryMusicSchool().getCategory())) {
				userTypes.add(new String[] {"school.manager", "Manager", Integer.toString(USER_TYPE_HEADMASTER) });
				userTypes.add(new String[] {"school.assistant_manager", "Assistant manager", Integer.toString(USER_TYPE_ASSISTANT_HEADMASTER) });
				userTypes.add(new String[] {"school.web_administrators", "Web administrators", Integer.toString(USER_TYPE_WEB_ADMIN) });
				userTypes.add(new String[] {"school.teachers", "Teachers", Integer.toString(USER_TYPE_TEACHER) });
			}
			
		}
		return userTypes;	
	}
	
	public String getSchoolCategory(School school) throws RemoteException {
		try {
			Collection schoolTypeCategories = getSchoolTypeCategories(school);
			if (schoolTypeCategories != null && !schoolTypeCategories.isEmpty()) {
				if (schoolTypeCategories.size() == 1) {
					Iterator iter = schoolTypeCategories.iterator();
					while (iter.hasNext()) {
						return (String) iter.next();
					}
				}	
			}
			return getSchoolBusiness().getElementarySchoolSchoolCategory();
		}
		catch (IDORelationshipException e) {
			return getSchoolBusiness().getElementarySchoolSchoolCategory();
		}
	}


	private UserBusiness getUserBusiness() throws RemoteException {
		return  (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
	}
	
	public SchoolTypeHome getSchoolTypeHome() throws RemoteException {
		return (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);	
	}

	public UserHome getUserHome() throws RemoteException {
		return (UserHome) IDOLookup.getHome(User.class);	
	}

	public SchoolUserHome getSchoolUserHome() throws RemoteException {
		return (SchoolUserHome) IDOLookup.getHome(SchoolUser.class);
	}
	
	public SchoolHome getSchoolHome() throws RemoteException {
		return (SchoolHome) IDOLookup.getHome(School.class);	
	}

	public SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);	
	}
}
