package com.idega.block.school.business;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.UserTransaction;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolAreaHome;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolUser;
import com.idega.block.school.data.SchoolYear;
import com.idega.block.school.data.SchoolYearHome;
import com.idega.block.school.data.SchoolYearPlaces;
import com.idega.block.school.data.SchoolYearPlacesHome;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.TxText;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.core.data.ICFile;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.GroupType;
import com.idega.user.data.GroupTypeHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */
public class SchoolBusinessBean extends IBOServiceBean implements SchoolBusiness {

	public static final String GROUP_TYPE_SCHOOL_GROUP = "school_staff_group";
	public static String MANAGEMENT_TYPE_PRIVATE = "school_man_type_private";
	public static final int MANAGEMENT_TYPE_PRIVATE_ID = 1;
	public static String MANAGEMENT_TYPE_PUBLIC = "school_man_type_public";
	public static final int MANAGEMENT_TYPE_PUBLIC_ID = 2;

	public SchoolHome getSchoolHome() throws java.rmi.RemoteException {
		return (SchoolHome) IDOLookup.getHome(School.class);
	}

	public SchoolClassMemberHome getSchoolClassMemberHome() throws RemoteException {
		return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
	}

	public SchoolClassHome getSchoolClassHome() throws RemoteException {
		return (SchoolClassHome) IDOLookup.getHome(SchoolClass.class);
	}

	public SchoolAreaHome getSchoolAreaHome() throws RemoteException {
		return (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
	}

	public SchoolTypeHome getSchoolTypeHome() throws RemoteException {
		return (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
	}

	public School getSchool(Object primaryKey) throws RemoteException {
		try {
			return getSchoolHome().findByPrimaryKey(primaryKey);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public void removeSchool(int id) throws RemoteException {
		try {
			School school = getSchool(new Integer(id));
			school.addSchoolTypesRemoveOther(new int[0]);
			school.addSchoolYearsRemoveOther(new int[0]);
			try {
				school.removeFromClass(ICFile.class);
			} catch (IDORemoveRelationshipException e) {
				System.err.println("Cannot remove file from school");
			}
			try {
				school.removeFromClass(TxText.class);
			} catch (IDORemoveRelationshipException e) {
				System.err.println("Cannot remove text from school");
			}
			try {
				school.removeFromClass(LocalizedText.class);
			} catch (IDORemoveRelationshipException e) {
				System.err.println("Cannot remove text from school");
			}
			
			try {
				Collection coll = getSchoolUserBusiness().getSchoolUserHome().findBySchool(school);
				SchoolUser sUser;
				if (coll != null && !coll.isEmpty()) {
					Iterator iter = coll.iterator();
					while (iter.hasNext()) {
						sUser = getSchoolUserBusiness().getSchoolUserHome().findByPrimaryKey(iter.next());
						sUser.remove();	
					}	
				}
			}catch (Exception e){
				e.printStackTrace(System.err);
			}
			
			school.remove();
		}
		catch (RemoveException re) {
			re.printStackTrace();
		}
	}

	public Collection findAllSchools() throws RemoteException {
		try {
			return getSchoolHome().findAllSchools();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new Vector();
		}
	}

	public Collection findAllSchoolsByAreaAndType(int area, int type) throws RemoteException {
		try {
			return getSchoolHome().findAllByAreaAndType(area, type);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new Vector();
		}
	}

	public Collection findAllSchoolsByAreaAndTypeAndYear(int areaID, int typeID, int yearID) throws RemoteException {
		try {
			return getSchoolHome().findAllByAreaAndTypeAndYear(areaID, typeID, yearID);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new Vector();
		}
	}

	public Collection findAllSchoolsByAreaAndTypes(int area, Collection types) throws RemoteException {
		try {
			return getSchoolHome().findAllByAreaAndTypes(area, types);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new Vector();
		}
	}

	public School createSchool(String name, String address, String zipcode, String ziparea, String phone, int school_type) throws RemoteException {
		/**
		 * @todo figure out how to implement
		 */
		int area_id = -1;
		int sch_types[] = { school_type };
		return createSchool(name, null, address, zipcode, ziparea, phone, null, null, null, area_id, sch_types);
	}

	public School createSchool(String name, String address, String zipcode, String ziparea, String phone, int area_id, int[] sch_types) throws RemoteException {
		return createSchool(name, null, address, zipcode, ziparea, phone, null, null, null, area_id, sch_types);
	}

	public School createSchool(String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int[] type_ids) throws RemoteException {
		return createSchool(name, info, address, zipcode, ziparea, phone, keycode, latitude, longitude, area_id, type_ids, null);
	}

	public School createSchool(String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int[] type_ids, int[] year_ids) throws RemoteException {
		return storeSchool(-1, name, info, address, zipcode, ziparea, phone, keycode, latitude, longitude, area_id, type_ids, year_ids);
	}

	public School storeSchool(int id, String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int[] type_ids, int[] year_ids) throws RemoteException {
		SchoolHome shome = getSchoolHome();
		School newSchool;
		try {
			if (id > 0) {
				newSchool = shome.findByPrimaryKey(new Integer(id));
			}
			else {
				newSchool = shome.create();
			}
			if (newSchool.getHeadmasterGroupId() < 0) {
				newSchool.setHeadmasterGroupId(((Integer) getNewSchoolGroup(name, name).getPrimaryKey()).intValue());
			}
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.rmi.RemoteException(fe.getMessage());
		}
		catch (javax.ejb.CreateException ce) {
			throw new java.rmi.RemoteException(ce.getMessage());
		}

		if (area_id > 0)
			newSchool.setSchoolAreaId(area_id);
		if (address != null)
			newSchool.setSchoolAddress(address);
		if (info != null)
			newSchool.setSchoolInfo(info);
		if (keycode != null)
			newSchool.setSchoolKeyCode(keycode);
		if (latitude != null)
			newSchool.setSchoolLatitude(latitude);
		if (longitude != null)
			newSchool.setSchoolLongitude(longitude);
		if (name != null)
			newSchool.setSchoolName(name);
		if (phone != null)
			newSchool.setSchoolPhone(phone);
		if (ziparea != null)
			newSchool.setSchoolZipArea(ziparea);
		if (zipcode != null)
			newSchool.setSchoolZipCode(zipcode);

		newSchool.store();
		if (type_ids != null)
			newSchool.addSchoolTypesRemoveOther(type_ids);
		if (year_ids != null)
			newSchool.addSchoolYearsRemoveOther(year_ids);
		return newSchool;
	}

	public Map getSchoolRelatedSchoolTypes(School school) throws RemoteException {
		try {
			Collection types = school.findRelatedSchoolTypes();
			if (types != null && !types.isEmpty()) {
				HashMap map = new HashMap(types.size());
				Iterator iter = types.iterator();
				SchoolType type;
				while (iter.hasNext()) {
					type = (SchoolType) iter.next();
					map.put((Integer) type.getPrimaryKey(), type);
				}
				return map;
			}
		}
		catch (IDORelationshipException ie) {
			ie.printStackTrace();
		}
		return null;
	}

	public Map getSchoolRelatedSchoolYears(School school) throws RemoteException {
		try {
			Collection years = school.findRelatedSchoolYears();
			if (years != null && !years.isEmpty()) {
				HashMap map = new HashMap(years.size());
				Iterator iter = years.iterator();
				SchoolYear year;
				while (iter.hasNext()) {
					year = (SchoolYear) iter.next();
					map.put(year.getPrimaryKey(), year);
				}
				return map;
			}
		}
		catch (IDORelationshipException ie) {
			ie.printStackTrace();
		}
		return null;
	}

	public Map getMapOfSchools() {
		try {
			Collection schools = findAllSchools();
			if (schools != null && !schools.isEmpty()) {
				HashMap map = new HashMap(schools.size());
				Iterator iter = schools.iterator();
				School school;
				while (iter.hasNext()) {
					school = (School) iter.next();
					map.put(school.getPrimaryKey(), school);
				}
				return map;
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Group getNewSchoolGroup(String name, String info) throws RemoteException {
		try {
			GroupTypeHome typeHome = (GroupTypeHome) this.getIDOHome(GroupType.class);
			GroupType type = typeHome.create();

			Group rootGroup = this.getRootSchoolGroup();
			Group schoolGroup = getUserBusiness().getGroupBusiness().createGroup(name, info, type.getGeneralGroupTypeString());
			rootGroup.addGroup(schoolGroup);
			return schoolGroup;
		}
		catch (CreateException ce) {
			ce.printStackTrace(System.err);
			return null;
		}
	}

	public Group getRootSchoolGroup() throws RemoteException {
		try {
			String ROOT_SCHOOL_GROUP_ID_PARAMETER = "root_school_group_id";
			Group rootGroup = null;
			//create the default group
			IWBundle bundle = this.getIWApplicationContext().getApplication().getBundle("com.idega.block.school");
			String groupId = bundle.getProperty(ROOT_SCHOOL_GROUP_ID_PARAMETER);
			//String groupId = (String) this.getIWApplicationContext().getApplicationSettings().getProperty(ROOT_CITIZEN_GROUP_ID_PARAMETER_NAME);
			if (groupId != null) {
				rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));

			}
			else {
				try {
					System.err.println("trying to store School Root group");
					/**@todo this seems a wrong way to do things**/
					GroupTypeHome typeHome = (GroupTypeHome) this.getIDOHome(GroupType.class);
					GroupType type = typeHome.create();

					rootGroup = getUserBusiness().getGroupBusiness().createGroup("School Root Group", "The School Root Group.", type.getGeneralGroupTypeString());
					bundle.setProperty(ROOT_SCHOOL_GROUP_ID_PARAMETER, rootGroup.getPrimaryKey().toString());
				}
				catch (CreateException ce) {
					ce.printStackTrace(System.err);
				}
			}

			return rootGroup;
		}
		catch (FinderException fe) {
			fe.printStackTrace(System.err);
			return null;
		}
	}

	public boolean hasEditPermission(User user, School school) throws RemoteException {
		UserBusiness uBus = (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);

		if (user != null && school != null) {
			Collection users = null;
			try {
				users = uBus.getGroupBusiness().getUsers(school.getHeadmasterGroupId());
			}
			catch (FinderException e) {
				e.printStackTrace(System.err);
			}

			if (user != null) {
				Object prKey = user.getPrimaryKey();
				Object obj;
				Iterator iter = users.iterator();
				while (iter.hasNext()) {
					if (iter.next().equals(user)) {
						return true;
					}
				}
			}

		}

		return false;

	}

	public Collection getSchoolGroups(User user) throws RemoteException {
		Collection userSchoolGroups = getUserBusiness().getUserGroups(user, new String[] { GROUP_TYPE_SCHOOL_GROUP }, true);
		if (userSchoolGroups != null) {
			//System.out.println("SchooBusiness.getSchoolGroup : found schoolGroups...");
			return userSchoolGroups;
		}
		else {
			//System.out.println("SchooBusiness.getSchoolGroup : schoolGroups NOT FOUND...");
			/** backwards compatabilit, ... finna betri leid ?? */
			Collection userGroups = getUserBusiness().getUserGroups(user);
			Collection schools = this.findAllSchools();

			if (schools != null && userGroups != null) {
				//System.out.println("SchooBusiness.getSchoolGroup : schools != null && userGroups != null...");
				Collection returner = new Vector();

				School school;
				Group group;
				GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);

				Iterator itSchools = schools.iterator();
				Iterator itGroups = userGroups.iterator();
				while (itSchools.hasNext()) {
					try {
						school = getSchoolHome().findByPrimaryKey(itSchools.next());

						while (itGroups.hasNext()) {
							group = gHome.findByPrimaryKey(itGroups.next());
							if (group.getName().equals(school.getName())) {
								//System.out.println("SchooBusiness.getSchoolGroup : group.getName().equals(school.getName()) ...");
								group.setGroupType(GROUP_TYPE_SCHOOL_GROUP);
								group.store();

								returner.add(group.getPrimaryKey());
							}
						}
					}
					catch (FinderException e) {
						e.printStackTrace(System.err);
					}
				}

				return returner;
			}

		}

		return null;
	}

	protected UserBusiness getUserBusiness() throws RemoteException {
		return (UserBusiness) this.getServiceInstance(UserBusiness.class);
	}

	public SchoolClass storeSchoolClass(String schoolClassName, School school, SchoolYear year, SchoolSeason season) throws RemoteException {
		try {
			SchoolClassHome sClassHome = (SchoolClassHome) this.getIDOHome(SchoolClass.class);
			SchoolClass sClass = sClassHome.create();

			sClass.setSchoolClassName(schoolClassName);
			sClass.setSchoolId(((Integer) school.getPrimaryKey()).intValue());
			sClass.setSchoolSeasonId(((Integer) season.getPrimaryKey()).intValue());
			sClass.setSchoolYearId(((Integer) year.getPrimaryKey()).intValue());

			return sClass;
		}
		catch (CreateException ce) {
			ce.printStackTrace(System.err);
			return null;
		}
	}

	public SchoolClassMember storeSchoolClassMember(SchoolClass sClass, User user) throws RemoteException {
		try {
			SchoolClassMemberHome sClassMemberHome = (SchoolClassMemberHome) this.getIDOHome(SchoolClassMember.class);
			SchoolClassMember sClassMember = sClassMemberHome.create();
			sClassMember.setSchoolClassId(((Integer) sClass.getPrimaryKey()).intValue());
			sClassMember.setClassMemberId(((Integer) user.getPrimaryKey()).intValue());
			sClassMember.setRegisterDate(IWTimestamp.getTimestampRightNow());
			//NEEDS THE CURRENT USER ID FOR REGISTERING USER

			return sClassMember;
		}
		catch (CreateException ce) {
			ce.printStackTrace(System.err);
			return null;
		}
	}

	public Collection getHeadmasters(School school) throws RemoteException {
		try {
			return getUserBusiness().getGroupBusiness().getUsers(school.getHeadmasterGroupId());
		}
		catch (FinderException fe) {
			throw new RemoteException(fe.getMessage());
		}
	}

	public User getHeadmaster(int schoolID) throws RemoteException {
		try {
			School school = getSchoolHome().findByPrimaryKey(new Integer(schoolID));
			return getUserBusiness().getUser(school.getHeadmasterUserId());
		}
		catch (FinderException fe) {
			return null;
		}
	}

	/**
	 *	Used for user who have admin rights, not only headmasters... 
	 */
	public void addHeadmaster(School school, User user) throws RemoteException, FinderException{
		GroupBusiness gBus = (GroupBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), GroupBusiness.class);
		Group schoolGroup = gBus.getGroupHome().findByPrimaryKey(new Integer(school.getHeadmasterGroupId()));
		schoolGroup.addGroup(user);
		//getUserBusiness().getGroupBusiness().addUser(school.getHeadmasterGroupId(), user);
	}

	public Collection findAllSchoolsByType(Collection typeIds) {
		try {
			SchoolHome shome = getSchoolHome();
			return shome.findAllBySchoolType(typeIds);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	public Collection findAllSchoolsByType(int type) throws RemoteException {
		try {
			SchoolHome shome = getSchoolHome();
			return shome.findAllBySchoolType(type);
		}
		catch (FinderException ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	public Collection findAllSchoolYearsInSchool(int schoolID) throws RemoteException {
		try {
			School school = this.getSchool(new Integer(schoolID));
			return school.findRelatedSchoolYears();
		}
		catch (IDORelationshipException ie) {
			return new Vector();
		}
	}

	public String getSchoolManagementTypeString(int managementTypeId) {
		switch (managementTypeId) {
			case MANAGEMENT_TYPE_PRIVATE_ID :
				return MANAGEMENT_TYPE_PRIVATE;
			case MANAGEMENT_TYPE_PUBLIC_ID :
				return MANAGEMENT_TYPE_PUBLIC;
		}
		return null;
	}

	/**
	 * @deprecated SHOULD NOT BE HERE
	 */
	private IWBundle getCommuneBundle() {
		return this.getIWApplicationContext().getApplication().getBundle("se.idega.idegaweb.commune");
	}

	/**
	* Returns or creates (if not available) the default usergroup all school administors have as their primary group.
	* @throws CreateException if it failed to create the group.
	* @throws FinderException if it failed to locate the group.
	*/
	public Group getRootSchoolAdministratorGroup() throws CreateException, FinderException, RemoteException {
		Group rootGroup = null;
		//create the default group
		String ROOT_SCHOOL_ADMINISTRATORS_GROUP = "school_administrators_group_id";
		IWBundle bundle = getCommuneBundle();
		String groupId = bundle.getProperty(ROOT_SCHOOL_ADMINISTRATORS_GROUP);
		if (groupId != null) {
			rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
		}
		else {
			System.err.println("trying to store Commune Root school administrators group");
			/**@todo this seems a wrong way to do things**/
			GroupTypeHome typeHome = (GroupTypeHome) this.getIDOHome(GroupType.class);
			GroupType type = typeHome.create();
			rootGroup = getUserBusiness().getGroupBusiness().createGroup("School Administrators", "The Commune Root School Administrators Group.", type.getGeneralGroupTypeString());
			bundle.setProperty(ROOT_SCHOOL_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
		}
		return rootGroup;
	}

	/**
	* Returns or creates (if not available) the default usergroup all provider(childcare) administors have as their primary group.
	* @throws CreateException if it failed to create the group.
	* @throws FinderException if it failed to locate the group.
	*/
	public Group getRootProviderAdministratorGroup() throws CreateException, FinderException, RemoteException {
		Group rootGroup = null;
		//create the default group
		String ROOT_SCHOOL_ADMINISTRATORS_GROUP = "provider_administrators_group_id";
		IWBundle bundle = getCommuneBundle();
		String groupId = bundle.getProperty(ROOT_SCHOOL_ADMINISTRATORS_GROUP);
		if (groupId != null) {
			rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
		}
		else {
			System.err.println("trying to store Commune Root school administrators group");
			/**@todo this seems a wrong way to do things**/
			GroupTypeHome typeHome = (GroupTypeHome) this.getIDOHome(GroupType.class);
			GroupType type = typeHome.create();
			rootGroup = getUserBusiness().getGroupBusiness().createGroup("Provider Administrators", "The Commune Root Provider Administrators Group.", type.getGeneralGroupTypeString());
			bundle.setProperty(ROOT_SCHOOL_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
		}
		return rootGroup;
	}

	public void addSchoolAdministrator(User user) throws RemoteException {
		try {
			getUserBusiness().getGroupBusiness().addUser(((Integer) getRootSchoolAdministratorGroup().getPrimaryKey()).intValue(), user);
		}
		catch (FinderException fe) {
			throw new RemoteException("No root school administrator group found: "+fe.getMessage());	
		}
		catch (CreateException ce) {
			throw new RemoteException("Could not set user with ID = "+user.getPrimaryKey().toString()+" as school administrator: "+ce.getMessage());
		}
	}

	public SchoolYearPlaces getSchoolYearPlaces(Object primaryKey) {
		try {
			SchoolYearPlacesHome shome = (SchoolYearPlacesHome) IDOLookup.getHome(SchoolYearPlaces.class);
			return shome.findByPrimaryKey(primaryKey);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public void removeSchoolYearPlace(int id) {
		try {
			SchoolYearPlacesHome shome = (SchoolYearPlacesHome) IDOLookup.getHome(SchoolYearPlaces.class);
			SchoolYearPlaces schoolYearPlaces = getSchoolYearPlaces(new Integer(id));
			schoolYearPlaces.remove();
		}
		catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	public Collection findAllSchoolYearPlaces(int iSchoolId) {
		try {
			SchoolYearPlacesHome shome = getSchoolYearPlacesHome();
			return shome.findAllBySchool(iSchoolId);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	public Map getMapOfSchoolYearPlaces(int iSchoolId) {
		Collection c = findAllSchoolYearPlaces(iSchoolId);
		Map m = new HashMap();
		try {
			Iterator iter = c.iterator();
			while (iter.hasNext()) {
				SchoolYearPlaces p = (SchoolYearPlaces) iter.next();
				m.put((Integer) p.getPrimaryKey(), p);
			}

		}
		catch (Exception ex) {

		}
		return m;
	}

	public SchoolYearPlacesHome getSchoolYearPlacesHome() throws java.rmi.RemoteException {
		return (SchoolYearPlacesHome) IDOLookup.getHome(SchoolYearPlaces.class);
	}

	public void storeSchoolYearPlaces(int id, int school_id, int school_year_id, int places) throws java.rmi.RemoteException {

		SchoolYearPlacesHome shome = getSchoolYearPlacesHome();
		SchoolYearPlaces newSchoolYearPlaces;
		try {
			if (id > 0) {
				newSchoolYearPlaces = shome.findByPrimaryKey(new Integer(id));
			}
			else {
				newSchoolYearPlaces = shome.create();
			}
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.rmi.RemoteException(fe.getMessage());
		}
		catch (javax.ejb.CreateException ce) {
			throw new java.rmi.RemoteException(ce.getMessage());
		}
		newSchoolYearPlaces.setSchoolId(school_id);
		newSchoolYearPlaces.setSchoolYearId(school_year_id);
		newSchoolYearPlaces.setPlaces(places);
		newSchoolYearPlaces.store();

	}

	public SchoolYear getSchoolYear(Object primaryKey) throws java.rmi.RemoteException {
		try {
			SchoolYearHome shome = getSchoolYearHome();
			return shome.findByPrimaryKey(primaryKey);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public void removeSchoolYear(int id) throws java.rmi.RemoteException {
		try {
			SchoolYearHome shome = getSchoolYearHome();
			SchoolYear area = getSchoolYear(new Integer(id));
			area.remove();
		}
		catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	public Collection findAllSchoolYears() throws java.rmi.RemoteException {
		try {
			SchoolYearHome shome = getSchoolYearHome();
			return shome.findAllSchoolYears();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	public Collection findAllSchoolYearsByAge(int age) throws java.rmi.RemoteException {
		try {
			SchoolYearHome shome = getSchoolYearHome();
			return shome.findAllByAge(age);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	public void storeSchoolYear(int pk, String name, String info, int age) throws java.rmi.RemoteException {

		SchoolYearHome shome = getSchoolYearHome();
		SchoolYear newYear;
		try {
			if (pk > 0) {
				newYear = shome.findByPrimaryKey(new Integer(pk));
			}
			else {
				newYear = shome.create();

			}
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.rmi.RemoteException(fe.getMessage());
		}
		catch (javax.ejb.CreateException ce) {
			throw new java.rmi.RemoteException(ce.getMessage());
		}

		newYear.setSchoolYearName(name);
		newYear.setSchoolYearInfo(info);
		newYear.setSchoolYearAge(age);
		newYear.store();
	}

	public SchoolYearHome getSchoolYearHome() throws java.rmi.RemoteException {
		return (SchoolYearHome) IDOLookup.getHome(SchoolYear.class);
	}

	public SchoolType getSchoolType(Object primaryKey) {
		try {
			SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
			return shome.findByPrimaryKey(primaryKey);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void removeSchoolType(int id) {
		try {
			SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
			SchoolType type = getSchoolType(new Integer(id));
			type.remove();
		}
		catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	public Collection findAllSchoolTypes() {
		try {
			SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
			return shome.findAllSchoolTypes();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return com.idega.util.ListUtil.getEmptyList();
		}
	}

	public Collection findAllSchoolTypesInCategory(String Category) {
		try {
			SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
			return shome.findAllByCategory(Category);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return com.idega.util.ListUtil.getEmptyList();
		}
	}

	public Collection findAllSchoolTypesForChildCare() {
		return findAllSchoolTypesInCategory("CHILDCARE");
	}

	public Collection findAllSchoolTypesForSchool() {
		return findAllSchoolTypesInCategory("SCHOOL");
	}

	public void storeSchoolType(int id, String name, String info, String category, String locKey) throws java.rmi.RemoteException {

		SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
		SchoolType newType;

		try {
			if (id > 0) {
				newType = shome.findByPrimaryKey(new Integer(id));
			}
			else {
				newType = shome.create();
			}
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.rmi.RemoteException(fe.getMessage());
		}
		catch (javax.ejb.CreateException ce) {
			throw new java.rmi.RemoteException(ce.getMessage());
		}
		newType.setSchoolTypeInfo(info);
		newType.setSchoolTypeName(name);
		newType.setSchoolCategory(category);
		newType.setLocalizationKey(locKey);
		newType.store();
	}

	public SchoolSeason getSchoolSeason(Object primaryKey) {
		try {
			SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
			return shome.findByPrimaryKey(primaryKey);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public void removeSchoolSeason(int id) {
		try {
			SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
			SchoolSeason season = getSchoolSeason(new Integer(id));
			season.remove();
		}
		catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	public Collection findAllSchoolSeasons() {
		try {
			SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
			return shome.findAllSchoolSeasons();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	public Collection findAllPreviousSchoolSeasons(SchoolSeason schoolSeason) {
		try {
			SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
			return shome.findAllPreviousSchoolSeasons(schoolSeason);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	public Collection findAllPreviousSchoolSeasons(int schoolSeasonID) throws RemoteException {
		try {
			SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
			SchoolSeason season = shome.findByPrimaryKey(new Integer(schoolSeasonID));
			return shome.findAllPreviousSchoolSeasons(season);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public void storeSchoolSeason(int id, String name, Date start, Date end, Date due_date) throws java.rmi.RemoteException {

		SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
		SchoolSeason newSeason;
		try {
			if (id > 0) {
				newSeason = shome.findByPrimaryKey(new Integer(id));
			}
			else {
				newSeason = shome.create();
			}
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.rmi.RemoteException(fe.getMessage());
		}
		catch (javax.ejb.CreateException ce) {
			throw new java.rmi.RemoteException(ce.getMessage());
		}
		newSeason.setSchoolSeasonName(name);
		newSeason.setSchoolSeasonStart(start);
		newSeason.setSchoolSeasonEnd(end);
		newSeason.setSchoolSeasonDueDate(due_date);
		newSeason.store();
	}

	public SchoolClassMember findClassMemberInClass(int studentID, int schoolClassID) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findByUserAndSchoolClass(studentID, schoolClassID);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public SchoolClassMember findByStudentAndSeason(int userID, int seasonID) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findByUserAndSeason(userID, seasonID);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public SchoolClassMember findByStudentAndSeason(User user, SchoolSeason season) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findByUserAndSeason(user, season);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public SchoolClassMember findByStudentAndSeason(SchoolClassMember student, SchoolSeason season) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findByUserAndSeason(student.getClassMemberId(), ((Integer)season.getPrimaryKey()).intValue());
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public Collection findClassMember(int studentID) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findByStudent(studentID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public Collection findStudentsInClass(int studentClassID) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findBySchoolClass(studentClassID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}
	
	public Collection findStudentsInSchool(int schoolID, int schoolClassID) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findBySchool(schoolID, schoolClassID);
		}
		catch (FinderException e) {
			return new Vector();
		}
	}

	public Collection findStudentsBySchoolAndSeasonAndYear(int schoolID, int seasonID, int yearID) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findBySchoolAndSeasonAndYear(schoolID, seasonID, yearID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public Collection findStudentsBySchoolAndSeason(int schoolID, int seasonID) throws RemoteException {
		try {
			return getSchoolClassMemberHome().findBySchoolAndSeason(schoolID, seasonID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public void removeSchoolClassMemberFromClass(int studentID, int schoolClassID) throws RemoteException {
		try {
			SchoolClassMember member = getSchoolClassMemberHome().findByUserAndSchoolClass(studentID, schoolClassID);
			member.remove();
		}
		catch (FinderException fe) {
		}
		catch (RemoveException re) {
		}
	}

	public void removeSchoolClassMember(int studentID) throws RemoteException {
		try {
			Collection members = findClassMember(studentID);
			if (!members.isEmpty()) {
				Iterator iter = members.iterator();
				while (iter.hasNext()) {
					SchoolClassMember member = (SchoolClassMember) iter.next();
					member.remove();
				}
			}
		}
		catch (RemoveException re) {
		}
	}

	public SchoolClassMember storeSchoolClassMember(int studentID, int schoolClassID, Timestamp registerDate, int registrator) throws RemoteException {
		return storeSchoolClassMember(studentID, schoolClassID, registerDate, registrator, null);
	}

	public SchoolClassMember storeSchoolClassMember(int studentID, int schoolClassID, Timestamp registerDate, int registrator, String notes) throws RemoteException {
		try {
			SchoolClassMember member = findClassMemberInClass(studentID, schoolClassID);
			if (member == null)
				member = this.getSchoolClassMemberHome().create();

			if (member != null) {
				member.setClassMemberId(studentID);
				member.setSchoolClassId(schoolClassID);
				if (registerDate != null)
					member.setRegisterDate(registerDate);
				if (registrator != -1)
					member.setRegistratorId(registrator);
				if (notes != null)
					member.setNotes(notes);

				member.store();
			}
			return member;
		}
		catch (CreateException ce) {
			ce.printStackTrace(System.err);
			return null;
		}
	}

	public SchoolClass findSchoolClass(Object primaryKey) throws RemoteException {
		try {
			return getSchoolClassHome().findByPrimaryKey(primaryKey);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public Collection findSchoolClassesBySchool(int schoolID) throws RemoteException {
		try {
			return getSchoolClassHome().findBySchool(schoolID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public Collection findSchoolClassesBySchoolAndSeason(int schoolID, int schoolSeasonID) throws RemoteException {
		try {
			return getSchoolClassHome().findBySchoolAndSeason(schoolID, schoolSeasonID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public Collection findSchoolClassesBySchoolAndYear(int schoolID, int schoolYearID) throws RemoteException {
		try {
			return getSchoolClassHome().findBySchoolAndYear(schoolID, schoolYearID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public Collection findSchoolClassesBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID, int schoolYearID) throws RemoteException {
		try {
			return getSchoolClassHome().findBySchoolAndSeasonAndYear(schoolID, schoolSeasonID, schoolYearID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public Collection findSchoolClassesByTeacher(int teacherID) throws RemoteException {
		try {
			return getSchoolClassHome().findByTeacher(teacherID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public Collection findSchoolClassesBySchoolAndTeacher(int schoolID, int teacherID) throws RemoteException {
		try {
			return getSchoolClassHome().findBySchoolAndTeacher(schoolID, teacherID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public Collection findSchoolClassesBySchoolAndSeasonAndTeacher(int schoolID, int schoolSeasonID, int teacherID) throws RemoteException {
		try {
			return getSchoolClassHome().findBySchoolAndSeasonAndTeacher(schoolID, schoolSeasonID, teacherID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public int getNumberOfStudentsInClass(int schoolClassID) throws RemoteException {
		try {
			return getSchoolClassHome().getNumberOfStudentsInClass(schoolClassID);
		}
		catch (FinderException fe) {
			return 0;
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public void removeSchoolClass(int schoolClassID) throws RemoteException {
		try {
			SchoolClass schoolClass = getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassID));
			schoolClass.remove();
		}
		catch (FinderException fe) {
			fe.printStackTrace(System.err);
		}
		catch (RemoveException re) {
			re.printStackTrace(System.err);
		}
	}

	public void invalidateSchoolClass(int schoolClassID) throws RemoteException {
		try {
			SchoolClass schoolClass = getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassID));
			schoolClass.setValid(false);
			schoolClass.store();

			UserTransaction trans = getSessionContext().getUserTransaction();
			try {
				trans.begin();
				Collection students = findStudentsInClass(schoolClassID);
				Iterator iter = students.iterator();
				while (iter.hasNext()) {
					SchoolClassMember element = (SchoolClassMember) iter.next();
					element.remove();
				}
			}
			catch (Exception ex) {
				try {
					trans.rollback();
				}
				catch (javax.transaction.SystemException e) {
				}
			}
		}
		catch (FinderException fe) {
			fe.printStackTrace(System.err);
		}
	}

	public void storeSchoolClass(int schoolClassID, String className, int schoolID, int schoolSeasonID, int schoolYearID, int teacherID) throws RemoteException {
		try {
			SchoolClass schoolClass;
			if (schoolClassID != -1)
				schoolClass = getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassID));
			else
				schoolClass = getSchoolClassHome().create();

			schoolClass.setSchoolClassName(className);
			schoolClass.setSchoolId(schoolID);
			if (schoolSeasonID != -1)
				schoolClass.setSchoolSeasonId(schoolSeasonID);
			if (schoolYearID != -1)
				schoolClass.setSchoolYearId(schoolYearID);
			if (teacherID != -1)
				schoolClass.setTeacherId(teacherID);
			schoolClass.setValid(true);

			schoolClass.store();
		}
		catch (FinderException fe) {
			fe.printStackTrace(System.err);
		}
		catch (CreateException ce) {
			ce.printStackTrace(System.err);
		}
	}

	public SchoolArea getSchoolArea(Object primaryKey) {
		try {
			SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
			return shome.findByPrimaryKey(primaryKey);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public void removeSchoolArea(int id) {
		try {
			SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
			SchoolArea area = getSchoolArea(new Integer(id));
			area.remove();
		}
		catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	public Collection findAllSchoolAreas() {
		try {
			SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
			return shome.findAllSchoolAreas();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	public Collection findAllSchoolAreasByType(int type_id) {
		try {
			SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
			return shome.findAllBySchoolType(type_id);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	public Collection findAllSchoolAreasByTypes(Collection types) {
		try {
			SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
			return shome.findAllBySchoolTypes(types);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	public void storeSchoolArea(int id, String name, String info, String city) throws java.rmi.RemoteException {

		SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
		SchoolArea newArea;
		try {
			if (id > 0) {
				newArea = shome.findByPrimaryKey(new Integer(id));
			}
			else {
				newArea = shome.create();
			}
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.rmi.RemoteException(fe.getMessage());
		}
		catch (javax.ejb.CreateException ce) {
			throw new java.rmi.RemoteException(ce.getMessage());
		}
		newArea.setSchoolAreaCity(city);
		newArea.setSchoolAreaInfo(info);
		newArea.setSchoolAreaName(name);
		newArea.store();
	}
	
	public SchoolUserBusiness getSchoolUserBusiness() throws RemoteException{
		return (SchoolUserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolUserBusiness.class);	
	}
	
	public boolean hasSchoolRelationToYear(School school, SchoolYear schoolYear) throws RemoteException {
		try {
			int relations = getSchoolHome().getNumberOfRelations(school, schoolYear);
			if (relations > 0)
				return true;
			return false;
		}
		catch (IDOException ie) {
			return false;
		}
	}
	
	public String getSchoolPhone(int schoolID) {
		try {
			School school = getSchool(new Integer(schoolID));
			return school.getSchoolPhone();
		}
		catch (RemoteException e) {
			return null;
		}
	}
}