package com.idega.block.school.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolUser;
import com.idega.block.school.data.SchoolUserBMPBean;
import com.idega.block.school.data.SchoolUserHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
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

	public SchoolUser addUser(School school, User user, int userType) throws RemoteException, CreateException {
		SchoolUser sUser = getSchoolUserHome().create();
		sUser.setSchoolId(((Integer) school.getPrimaryKey()).intValue());
		sUser.setUserId(((Integer) user.getPrimaryKey()).intValue());
		sUser.setUserType(userType);
		sUser.store();
		return sUser;
	}

	public SchoolUser addTeacher(School school, User user) throws RemoteException, CreateException {
		return addUser(school, user, USER_TYPE_TEACHER);	
	}

	public SchoolUser addHeadmaster(School school, User user) throws RemoteException, CreateException {
		return addUser(school, user, USER_TYPE_HEADMASTER);	
	}

	public SchoolUser addAssistantHeadmaster(School school, User user) throws RemoteException, CreateException {
		return addUser(school, user, USER_TYPE_ASSISTANT_HEADMASTER);	
	}

	public SchoolUser addWebAdmin(School school, User user) throws RemoteException, CreateException {
		return addUser(school, user, USER_TYPE_WEB_ADMIN);	
	}

	public void removeUser(School school, User user, int userType)  throws FinderException, RemoteException, RemoveException{
		Object id = null;
		id = getSchoolUserHome().getSchoolUserId(school, user, userType);
		if (id != null) {
			SchoolUser sUser = getSchoolUserHome().findByPrimaryKey(id);
			sUser.remove();
		}
	}
	
	public void removeUser(School school, User user) throws FinderException, RemoteException, RemoveException {
		Collection coll	= getSchoolUserHome().findBySchoolAndUser(school, user);
		if (coll != null && coll.size() > 0 ) {
			SchoolUser sUser;
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				sUser = getSchoolUserHome().findByPrimaryKey(iter.next());	
				sUser.remove();
			}
		}
	}

	public Collection getTeachers(School school) throws RemoteException, FinderException{
		return getSchoolUserHome().findBySchoolAndType(school, USER_TYPE_TEACHER);	
	}
	
	public Collection getHeadmasters(School school) throws RemoteException, FinderException{
		Collection coll = getSchoolUserHome().findBySchoolAndType(school, USER_TYPE_HEADMASTER);	

		/** Backwards compatability */
		if ( coll == null || coll.size() < 1 ) {
			int headmasterId = school.getHeadmasterUserId();
			if (headmasterId > 0) {
				User user = ((UserHome) IDOLookup.getHome(User.class)).findByPrimaryKey(new Integer(headmasterId));
				coll = new Vector();
				coll.add(user.getPrimaryKey());
				
				try {
					addHeadmaster(school, user);
					System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school headmasters");
				} catch (CreateException e) {
					System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school headmasters failed (addHeadmaster())");
//					e.printStackTrace(System.err);
				}
			}
		}

		return coll;
	}

	public Collection getAssistantHeadmasters(School school) throws RemoteException, FinderException{
		Collection coll = getSchoolUserHome().findBySchoolAndType(school, USER_TYPE_ASSISTANT_HEADMASTER);	

		/** Backwards compatability */
		if ( coll == null || coll.size() < 1 ) {
			int ahgi = school.getAssistantHeadmasterGroupId();
			if (ahgi > 0) {
				Collection users = getUserBusiness().getGroupBusiness().getUsers(ahgi);
				if ( users != null && users.size() > 0 ) {
					User user;
					Iterator iter = users.iterator();
					coll = new Vector();
					while (iter.hasNext()) {
						user = (User) iter.next();
						coll.add(user.getPrimaryKey());
						try {
							addWebAdmin(school, user);
							System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school assistant headmasters");
						} catch (CreateException e) {
							System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school assistant headmasters failed (addWebAdmin())");
	//					e.printStackTrace(System.err);
						}
					}
					
				}
			}
		}

		return coll;
	}
	
	public Collection getWebAdmins(School school) throws RemoteException, FinderException{
		Collection coll = getSchoolUserHome().findBySchoolAndType(school, USER_TYPE_WEB_ADMIN);	
		
		/** backwards compatability */
		if ( coll == null || coll.size() < 1 ) {
			int hgi = school.getHeadmasterGroupId();
			if (hgi > 0) {
				Collection users = getUserBusiness().getGroupBusiness().getUsers(hgi);
				if ( users != null && users.size() > 0 ) {
					User user;
					Iterator iter = users.iterator();
					coll = new Vector();
					while (iter.hasNext()) {
						user = (User) iter.next();
						coll.add(user.getPrimaryKey());
						try {
							addWebAdmin(school, user);
							System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school web admins");
						} catch (CreateException e) {
							System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school web admins failed (addWebAdmin())");
	//					e.printStackTrace(System.err);
						}
					}
					
				}
			}
		}
		
		return coll;
	}


	private UserBusiness getUserBusiness() throws RemoteException {
		return  (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
	}

	public SchoolUserHome getSchoolUserHome() throws RemoteException {
		return (SchoolUserHome) IDOLookup.getHome(SchoolUser.class);
	}

}
