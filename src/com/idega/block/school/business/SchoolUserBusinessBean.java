package com.idega.block.school.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.school.data.School;
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

	public SchoolUser addUser(School school, User user, int userType) throws RemoteException, CreateException, FinderException {
		SchoolUser sUser = getSchoolUserHome().create();
		sUser.setSchoolId(((Integer) school.getPrimaryKey()).intValue());
		sUser.setUserId(((Integer) user.getPrimaryKey()).intValue());
		sUser.setUserType(userType);
		sUser.store();
		
		setUserGroups(school, user, userType);
		
		return sUser;
	}

	public SchoolUser addTeacher(School school, User user) throws RemoteException, CreateException, FinderException {
		return addUser(school, user, USER_TYPE_TEACHER);	
	}

	public SchoolUser addHeadmaster(School school, User user) throws RemoteException, CreateException, FinderException {
		SchoolUser sUser = addUser(school, user, USER_TYPE_HEADMASTER);
		return sUser;
	}

	public SchoolUser addAssistantHeadmaster(School school, User user) throws RemoteException, CreateException, FinderException {
		return addUser(school, user, USER_TYPE_ASSISTANT_HEADMASTER);	
	}

	public SchoolUser addWebAdmin(School school, User user) throws RemoteException, CreateException, FinderException {
		return addUser(school, user, USER_TYPE_WEB_ADMIN);	
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
				sUser = getSchoolUserHome().findByPrimaryKey(iter.next());	
				sUser.remove();
			}
		}
		getUserBusiness().deleteUser(user, currentUser);
	}

	public Collection getTeachers(int schoolID) throws RemoteException, FinderException {
		return getTeachers(getSchoolHome().findByPrimaryKey(new Integer(schoolID)));	
	}

	public Collection getTeachers(School school) throws RemoteException, FinderException{
		return getSchoolUserHome().findBySchoolAndType(school, USER_TYPE_TEACHER);	
	}
	
	public Collection getHeadmasters(School school) throws RemoteException, FinderException{
		return getSchoolUserHome().findBySchoolAndType(school, USER_TYPE_HEADMASTER);	
	}

	public Collection getAssistantHeadmasters(School school) throws RemoteException, FinderException{
		return getSchoolUserHome().findBySchoolAndType(school, USER_TYPE_ASSISTANT_HEADMASTER);	
	}
	
	public Collection getWebAdmins(School school) throws RemoteException, FinderException{
		return getSchoolUserHome().findBySchoolAndType(school, USER_TYPE_WEB_ADMIN);	
	}

	public Collection getUsers(School school, int userType) throws RemoteException, FinderException {
		return getSchoolUserHome().findBySchoolAndType(school, userType);	
	}

	public Collection getSchools(User user) throws RemoteException, FinderException {
		Collection csUser = getSchoolUserHome().findByUser(user);
		if (csUser != null || !csUser.isEmpty()) {
			Collection coll = new Vector();
			Iterator iter = csUser.iterator();
			SchoolUser sUser;
			while (iter.hasNext()) {
				sUser = getSchoolUserHome().findByPrimaryKey(iter.next());
				coll.add(new Integer(sUser.getSchoolId()));	
			}
			return coll;
		}

		return null;
	}
	
	/**
	 *	Returns a collection of Strings. "SCHOOL" or "CHILDCARE" or both 
	 * 
	 */
	public Collection getSchoolTypeCategories(School school) throws IDORelationshipException, RemoteException, FinderException {
		Collection sTypes = school.getSchoolTypes();
		SchoolType sType;
		boolean SCHOOL = false;
		boolean CHILDCARE = false;
		
		if (sTypes != null && !sTypes.isEmpty() ) {
			Iterator iter = sTypes.iterator();
			String sCat;
			while (iter.hasNext()) {
				sType = getSchoolTypeHome().findByPrimaryKey(iter.next());
				sCat = sType.getSchoolCategory();
				if (sCat != null && sCat.equals("SCHOOL")){
					SCHOOL = true;
				}else if (sCat != null && sCat.equals("CHILDCARE")) {
					CHILDCARE = true;
				}
			}
		}
		
		Collection coll = new Vector();
		if (SCHOOL) {
			coll.add("SCHOOL");	
		}
		if (CHILDCARE) {
			coll.add("CHILDCARE");	
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
	public Collection getSchoolUserTypes(School school) throws IDORelationshipException, RemoteException, FinderException {
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

			if (category.equals("SCHOOL") || category.equals("BOTH")) {
				userTypes.add(new String[] {"school.headmaster", "Headmaster", Integer.toString(USER_TYPE_HEADMASTER) });
				userTypes.add(new String[] {"school.assistant_headmaster", "Assistant headmaster", Integer.toString(USER_TYPE_ASSISTANT_HEADMASTER) });
				userTypes.add(new String[] {"school.web_administrators", "Web administrators", Integer.toString(USER_TYPE_WEB_ADMIN) });
				userTypes.add(new String[] {"school.teachers", "Teachers", Integer.toString(USER_TYPE_TEACHER) });
			}else if (category.equals("CHILDCARE")) {
				userTypes.add(new String[] {"school.manager", "Manager", Integer.toString(USER_TYPE_HEADMASTER) });
				userTypes.add(new String[] {"school.web_administrators", "Web administrators", Integer.toString(USER_TYPE_WEB_ADMIN) });
				userTypes.add(new String[] {"school.teachers", "Teachers", Integer.toString(USER_TYPE_TEACHER) });
			}
			
		}
		return userTypes;	
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
