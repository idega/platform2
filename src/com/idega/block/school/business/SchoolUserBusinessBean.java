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
		SchoolUser sUser = addUser(school, user, USER_TYPE_ASSISTANT_HEADMASTER);	
		return sUser;
	}

	public SchoolUser addWebAdmin(School school, User user) throws RemoteException, CreateException, FinderException {
		SchoolUser sUser = addUser(school, user, USER_TYPE_WEB_ADMIN);	
		return sUser;
	}

	public void setUserGroups(School school, User user, int userType) throws RemoteException, FinderException {
		// code of death, fix later... 
		if (userType == USER_TYPE_HEADMASTER || userType == USER_TYPE_ASSISTANT_HEADMASTER || userType == USER_TYPE_WEB_ADMIN) {
			getSchoolBusiness().addHeadmaster(school, user);
		}
	}

	public void removeUser(School school, User user, int userType)  throws FinderException, RemoteException, RemoveException{
		Object id = null;
		id = getSchoolUserHome().getSchoolUserId(school, user, userType);
		if (id != null) {
			SchoolUser sUser = getSchoolUserHome().findByPrimaryKey(id);
			sUser.remove();
		}
		getUserBusiness().deleteUser(user.getID());
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
		getUserBusiness().deleteUser(user.getID());
	}

	public Collection getTeachers(int schoolID) throws RemoteException, FinderException {
		return getTeachers(getSchoolHome().findByPrimaryKey(new Integer(schoolID)));	
	}

	public Collection getTeachers(School school) throws RemoteException, FinderException{
		return getSchoolUserHome().findBySchoolAndType(school, USER_TYPE_TEACHER);	
	}
	
	public Collection getHeadmasters(School school) throws RemoteException, FinderException{
		Collection coll = getSchoolUserHome().findBySchoolAndType(school, USER_TYPE_HEADMASTER);	
/*
		// Backwards compatability 
		if ( coll == null || coll.size() < 1 ) {
			int headmasterId = school.getHeadmasterUserId();
			if (headmasterId > 0) {
				try {
					User user = ((UserHome) IDOLookup.getHome(User.class)).findByPrimaryKey(new Integer(headmasterId));
					coll = new Vector();
					coll.add(user.getPrimaryKey());
				
					addHeadmaster(school, user);
					System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school headmasters");
				} catch (CreateException e) {
					System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school headmasters failed (addHeadmaster())");
				} catch (FinderException e) {
					System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school headmasters failed cannot find hm with ID = "+headmasterId);
				}
			}
		}
*/
		return coll;
	}

	public Collection getAssistantHeadmasters(School school) throws RemoteException, FinderException{
		Collection coll = getSchoolUserHome().findBySchoolAndType(school, USER_TYPE_ASSISTANT_HEADMASTER);	
/*
		// Backwards compatability 
		if ( coll == null || coll.size() < 1 ) {
			int ahgi = school.getAssistantHeadmasterGroupId();
			if (ahgi > 0) {
				try {
					Collection users = getUserBusiness().getGroupBusiness().getUsers(ahgi);
					if ( users != null && users.size() > 0 ) {
						User user;
						Iterator iter = users.iterator();
						coll = new Vector();
						while (iter.hasNext()) {
							try {
								user = (User) iter.next();
								coll.add(user.getPrimaryKey());
								addWebAdmin(school, user);
								System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school assistant headmasters");
							} catch (CreateException e) {
								System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school assistant headmasters failed (addWebAdmin())");
		//					e.printStackTrace(System.err);
							} catch (FinderException e) {
								System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school assistant headmasters failed, cannot find user with ID "+ahgi);
							}
						}
						
					}
				}catch (Exception fe) {
					System.out.println("[SchoolUserBusinessBean] : getAssistantHeadmasters() : cannot find any users...");
				}
			}
		}
*/
		return coll;
	}
	
	public Collection getWebAdmins(School school) throws RemoteException, FinderException{
		Collection coll = getSchoolUserHome().findBySchoolAndType(school, USER_TYPE_WEB_ADMIN);	
/*		
		// backwards compatability 
		if ( coll == null || coll.size() < 1 ) {
			int hgi = school.getHeadmasterGroupId();
			if (hgi > 0) {
				try {
					Collection users = getUserBusiness().getGroupBusiness().getUsers(hgi);
					if ( users != null && users.size() > 0 ) {
						User user;
						Iterator iter = users.iterator();
						coll = new Vector();
						while (iter.hasNext()) {
							try {
								user = (User) iter.next();
								coll.add(user.getPrimaryKey());
								addWebAdmin(school, user);
								System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school web admins");
							} catch (CreateException e) {
								System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school web admins failed (addWebAdmin())");
		//					e.printStackTrace(System.err);
							} catch (FinderException e) {
								System.out.println("[SchoolUserBusinessBean] : Backwards compatability for school web admins failed, cannot find user with Id = "+hgi);
		//					e.printStackTrace(System.err);
							}
						}
						
					}
				}catch (Exception fe) {
					System.out.println("[SchoolUserBusinessBean] : getWebAdmins() : cannot find any users...");
				}
			}
		}
*/		
		return coll;
	}

	public Collection getSchools(User user) throws RemoteException, FinderException {
//		System.out.println("[SchoolUserBusinessBean] : halo eg heitir recursive");
		Collection csUser = getSchoolUserHome().findByUser(user);
		if (csUser != null || !csUser.isEmpty()) {
//			System.out.println("[SchoolUserBusinessBean] : found school(s)....");
			Collection coll = new Vector();
			Iterator iter = csUser.iterator();
			SchoolUser sUser;
			while (iter.hasNext()) {
				sUser = getSchoolUserHome().findByPrimaryKey(iter.next());
				coll.add(new Integer(sUser.getSchoolId()));	
			}
			return coll;
		}
		/*else {
//			System.out.println("[SchoolUserBusinessBean] : trying to backwards.....");
			Collection schools =
				((SchoolBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), SchoolBusiness.class))
					.getSchoolHome()
					.findAllBySchoolGroup(user);
			if (schools != null && !schools.isEmpty()) {
//				System.out.println("[SchoolUserBusinessBean] : trying to backwards .. school != null and not empty");
				School school;
				Iterator iter = schools.iterator();
				while (iter.hasNext()) {
					school = getSchoolHome().findByPrimaryKey(iter.next());
					try {
						addWebAdmin(school, user);
						System.out.println("[SchoolUserBusinessBean] : Backwards compatability for schools ...");
					} catch (Exception e) {
						System.out.println("[SchoolUserBusinessBean] : Backwards compatability for schools FAILED");
					}	
				}
			}else {
//				System.out.println("[SchoolUserBusinessBean] : trying to backwards .. school == null or empty");
			}		
			
			return getSchools(user);
			
		}*/
		return null;
	}


	private UserBusiness getUserBusiness() throws RemoteException {
		return  (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
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
