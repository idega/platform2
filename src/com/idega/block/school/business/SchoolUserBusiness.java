/*
 * Created on 2005-maj-03
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.school.business;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolUser;
import com.idega.block.school.data.SchoolUserHome;
import com.idega.business.IBOService;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface SchoolUserBusiness extends IBOService {
	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#addUser
	 */
	public SchoolUser addUser(School school, User user, int userType,
			boolean isEconomicalResponsible) throws RemoteException,
			CreateException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#addUser
	 */
	public SchoolUser addUser(School school, User user, int userType,
			boolean showInContacts, boolean main_headmaster,
			boolean isEconomicalResponsible) throws RemoteException,
			CreateException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#updateSchUser
	 */
	public SchoolUser updateSchUser(School school, User user, int userType,
			boolean showInContacts, boolean isEconomicalResponsible)
			throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#addTeacher
	 */
	public SchoolUser addTeacher(School school, User user)
			throws RemoteException, CreateException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#addHeadmaster
	 */
	public SchoolUser addHeadmaster(School school, User user)
			throws RemoteException, CreateException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#addAssistantHeadmaster
	 */
	public SchoolUser addAssistantHeadmaster(School school, User user)
			throws RemoteException, CreateException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#addWebAdmin
	 */
	public SchoolUser addWebAdmin(School school, User user)
			throws RemoteException, CreateException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#setUserGroups
	 */
	public void setUserGroups(School school, User user, int userType)
			throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#removeUser
	 */
	public void removeUser(School school, User user, int userType,
			User currentUser) throws FinderException, RemoteException,
			RemoveException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#removeUser
	 */
	public void removeUser(School school, User user, User currentUser)
			throws FinderException, RemoteException, RemoveException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getTeachers
	 */
	public Collection getTeachers(int schoolID) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getTeacherUserIds
	 */
	public Collection getTeacherUserIds(int schoolID) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getTeachers
	 */
	public Collection getTeachers(School school) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getTeacherUserIds
	 */
	public Collection getTeacherUserIds(School school) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getHeadmasters
	 */
	public Collection getHeadmasters(School school) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getMainHeadmasters
	 */
	public Collection getMainHeadmasters(School school) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getAssistantHeadmasters
	 */
	public Collection getAssistantHeadmasters(School school)
			throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getWebAdmins
	 */
	public Collection getWebAdmins(School school) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getEconomicalResponsibles
	 */
	public Collection getEconomicalResponsibles(School school)
			throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getUsers
	 */
	public Collection getUsers(School school, int userType)
			throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getUsersByDepartm
	 */
	public Collection getUsersByDepartm(School school, int userType,
			int departmentID) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getUsersByDepartm
	 */
	public Collection getUsersByDepartm(School school, int departmentID)
			throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getUsersByMainHeadMaster
	 */
	public Collection getUsersByMainHeadMaster(School school, int userType,
			boolean main_headmaster) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getUserShowInContact
	 */
	public boolean getUserShowInContact(User user) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getUserMainHeadmaster
	 */
	public boolean getUserMainHeadmaster(User user) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getUserIds
	 */
	public Collection getUserIds(School school, int userType)
			throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getSchools
	 */
	public Collection getSchools(User user) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getSchoolTypeCategories
	 */
	public Collection getSchoolTypeCategories(School school)
			throws IDORelationshipException, RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getSchoolUserTypes
	 */
	public Collection getSchoolUserTypes(School school)
			throws IDORelationshipException, RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getSchoolCategory
	 */
	public String getSchoolCategory(School school) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getSchoolTypeHome
	 */
	public SchoolTypeHome getSchoolTypeHome() throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getUserHome
	 */
	public UserHome getUserHome() throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getSchoolUserHome
	 */
	public SchoolUserHome getSchoolUserHome() throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getSchoolHome
	 */
	public SchoolHome getSchoolHome() throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolUserBusinessBean#getSchoolBusiness
	 */
	public SchoolBusiness getSchoolBusiness() throws RemoteException;

}
