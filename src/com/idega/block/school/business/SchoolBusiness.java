/*
 * $Id: SchoolBusiness.java,v 1.91 2005/01/10 15:06:05 anders Exp $
 * Created on 10.1.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolAreaHome;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolClassMemberLogHome;
import com.idega.block.school.data.SchoolDepartment;
import com.idega.block.school.data.SchoolDepartmentHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolManagementTypeHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.block.school.data.SchoolStudyPathHome;
import com.idega.block.school.data.SchoolSubArea;
import com.idega.block.school.data.SchoolSubAreaHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolUser;
import com.idega.block.school.data.SchoolYear;
import com.idega.block.school.data.SchoolYearHome;
import com.idega.block.school.data.SchoolYearPlaces;
import com.idega.block.school.data.SchoolYearPlacesHome;
import com.idega.business.IBOService;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/01/10 15:06:05 $ by $Author: anders $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.91 $
 */
public interface SchoolBusiness extends IBOService {

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolDepartmentHome
	 */
	public SchoolDepartmentHome getSchoolDepartmentHome() throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolHome
	 */
	public SchoolHome getSchoolHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolClassMemberHome
	 */
	public SchoolClassMemberHome getSchoolClassMemberHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolClassHome
	 */
	public SchoolClassHome getSchoolClassHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolAreaHome
	 */
	public SchoolAreaHome getSchoolAreaHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolTypeHome
	 */
	public SchoolTypeHome getSchoolTypeHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolCategoryHome
	 */
	public SchoolCategoryHome getSchoolCategoryHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolSeasonHome
	 */
	public SchoolSeasonHome getSchoolSeasonHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolStudyPathHome
	 */
	public SchoolStudyPathHome getSchoolStudyPathHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolManagementTypeHome
	 */
	public SchoolManagementTypeHome getSchoolManagementTypeHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getDepartments
	 */
	public Collection getDepartments(School school) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getDepartmentID
	 */
	public int getDepartmentID(SchoolDepartment schDepm) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#removeDepartment
	 */
	public void removeDepartment(SchoolDepartment schDep) throws RemoveException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#addSchoolUsr
	 */
	public void addSchoolUsr(int schDep_id, SchoolUser schUser) throws FinderException, RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#removeSchoolUsr
	 */
	public void removeSchoolUsr(int schDep_id, SchoolUser schUser) throws FinderException, RemoteException, IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolCategories
	 */
	public Collection getSchoolCategories() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getCategoryMusicSchool
	 */
	public SchoolCategory getCategoryMusicSchool() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getCategoryChildcare
	 */
	public SchoolCategory getCategoryChildcare() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getCategoryElementarySchool
	 */
	public SchoolCategory getCategoryElementarySchool() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getCategoryHighSchool
	 */
	public SchoolCategory getCategoryHighSchool() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getCategoryCollege
	 */
	public SchoolCategory getCategoryCollege() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getCategoryUniversity
	 */
	public SchoolCategory getCategoryUniversity() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolManagementTypes
	 */
	public Collection getSchoolManagementTypes() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchool
	 */
	public School getSchool(Object primaryKey) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#removeProvider
	 */
	public void removeProvider(int id) throws RemoveException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#removeSchool
	 */
	public void removeSchool(int id) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchools
	 */
	public Collection findAllSchools() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolsByAreaAndType
	 */
	public Collection findAllSchoolsByAreaAndType(int area, int type) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolsByAreaAndTypeAndYear
	 */
	public Collection findAllSchoolsByAreaAndTypeAndYear(int areaID, int typeID, int yearID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolsByAreaAndTypes
	 */
	public Collection findAllSchoolsByAreaAndTypes(int area, Collection types) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolsBySubAreaAndTypes
	 */
	public Collection findAllSchoolsBySubAreaAndTypes(int subarea, Collection types) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllCentralizedAdministrated
	 */
	public java.util.Collection findAllCentralizedAdministrated() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllCentralizedAdministratedByType
	 */
	public java.util.Collection findAllCentralizedAdministratedByType(Collection typeIds) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#createSchool
	 */
	public School createSchool(String name, String address, String zipcode, String ziparea, String phone, int school_type, Object communePK) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#createSchool
	 */
	public School createSchool(String name, String address, String zipcode, String ziparea, String phone, int area_id, int[] sch_types, Object communePK) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#createSchool
	 */
	public School createSchool(String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int[] type_ids, Object communePK) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#createSchool
	 */
	public School createSchool(String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int[] type_ids, int[] year_ids, Object communePK) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchool
	 */
	public School storeSchool(int id, String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int[] type_ids, int[] year_ids, Object communePK) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchool
	 */
	public School storeSchool(int id, String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int[] type_ids, int[] year_ids, Object communePK, String providerStringId) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchool
	 */
	public School storeSchool(int id, String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int sub_area_id, int[] type_ids, int[] year_ids, String organizationNumber, String extraProviderId, String managementTypeId, java.sql.Date terminationDate, Object communePK, int countryId, Boolean centralizedAdministration, Boolean invisibleForCitizen) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchool
	 */
	public School storeSchool(int id, String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int sub_area_id, int[] type_ids, int[] year_ids, String organizationNumber, String extraProviderId, String managementTypeId, java.sql.Date terminationDate, Object communePK, int countryId, Boolean centralizedAdministration, Boolean invisibleForCitizen, String providerStringId) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolRelatedSchoolTypes
	 */
	public Map getSchoolRelatedSchoolTypes(School school) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolRelatedSchoolYears
	 */
	public Map getSchoolRelatedSchoolYears(School school) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolAndSchoolTypeRelatedSchoolCourses
	 */
	public Map getSchoolAndSchoolTypeRelatedSchoolCourses(School school, Object schoolTypeId) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getMapOfSchools
	 */
	public Map getMapOfSchools() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getNewSchoolGroup
	 */
	public Group getNewSchoolGroup(String name, String info) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getRootSchoolGroup
	 */
	public Group getRootSchoolGroup() throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#hasEditPermission
	 */
	public boolean hasEditPermission(User user, School school) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolGroups
	 */
	public Collection getSchoolGroups(User user) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolClass
	 */
	public SchoolClass storeSchoolClass(String schoolClassName, School school, SchoolYear year, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolClassMember
	 */
	public SchoolClassMember storeSchoolClassMember(SchoolClass sClass, User user) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchClMemberInvoiceIntervalTypes
	 */
	public Collection findAllSchClMemberInvoiceIntervalTypes() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getAssistantHeadmasters
	 */
	public Collection getAssistantHeadmasters(School school) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolUsers
	 */
	public Collection getSchoolUsers(School school) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getHeadmasters
	 */
	public Collection getHeadmasters(School school) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getHeadmaster
	 */
	public User getHeadmaster(int schoolID) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#addHeadmaster
	 */
	public void addHeadmaster(School school, User user) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolsByType
	 */
	public Collection findAllSchoolsByType(Collection typeIds) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolsByType
	 */
	public Collection findAllSchoolsByType(int type) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolYearsInSchool
	 */
	public Collection findAllSchoolYearsInSchool(int schoolID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getRootSchoolAdministratorGroup
	 */
	public Group getRootSchoolAdministratorGroup() throws CreateException, FinderException, RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getRootMusicSchoolAdministratorGroup
	 */
	public Group getRootMusicSchoolAdministratorGroup() throws CreateException, FinderException, RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getRootProviderAdministratorGroup
	 */
	public Group getRootProviderAdministratorGroup() throws CreateException, FinderException, RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#addSchoolAdministrator
	 */
	public void addSchoolAdministrator(User user) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolYearPlaces
	 */
	public SchoolYearPlaces getSchoolYearPlaces(Object primaryKey) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#removeSchoolYearPlace
	 */
	public void removeSchoolYearPlace(int id) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolYearPlaces
	 */
	public Collection findAllSchoolYearPlaces(int iSchoolId) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getMapOfSchoolYearPlaces
	 */
	public Map getMapOfSchoolYearPlaces(int iSchoolId) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolYearPlacesHome
	 */
	public SchoolYearPlacesHome getSchoolYearPlacesHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolYearPlaces
	 */
	public void storeSchoolYearPlaces(int id, int school_id, int school_year_id, int places) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolYear
	 */
	public SchoolYear getSchoolYear(Object primaryKey) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#removeSchoolYear
	 */
	public void removeSchoolYear(int id) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolYears
	 */
	public Collection findAllSchoolYears() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolYearsBySchoolType
	 */
	public Collection findAllSchoolYearsBySchoolType(int schoolTypeId) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolYearsBySchoolCategory
	 */
	public Collection findSchoolYearsBySchoolCategory(String schoolCategory) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolYearsByAge
	 */
	public Collection findAllSchoolYearsByAge(int age) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolYear
	 */
	public void storeSchoolYear(int pk, String name, int schoolTypeId, String category, String info, String localizedKey, int age) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolYearHome
	 */
	public SchoolYearHome getSchoolYearHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolType
	 */
	public SchoolType getSchoolType(Object primaryKey) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolTypesForCategory
	 */
	public Collection getSchoolTypesForCategory(SchoolCategory category, boolean showFreetimeTypes) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#removeSchoolType
	 */
	public void removeSchoolType(int id) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolTypes
	 */
	public Collection findAllSchoolTypes() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolTypesInCategory
	 */
	public Collection findAllSchoolTypesInCategory(String Category) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolTypesInCategory
	 */
	public Collection findAllSchoolTypesInCategory(String Category, boolean showFreetimeTypes) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolTypesInCategoryFreeTime
	 */
	public Collection findAllSchoolTypesInCategoryFreeTime(String Category) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getChildCareSchoolCategory
	 */
	public String getChildCareSchoolCategory() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getElementarySchoolSchoolCategory
	 */
	public String getElementarySchoolSchoolCategory() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getHighSchoolSchoolCategory
	 */
	public String getHighSchoolSchoolCategory() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolTypesForChildCare
	 */
	public Collection findAllSchoolTypesForChildCare() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolTypesForSchool
	 */
	public Collection findAllSchoolTypesForSchool() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolType
	 */
	public void storeSchoolType(int id, String name, String info, String category, String locKey, int maxAge, boolean isFreetimeType, boolean isFamilyFreetimeType, int order) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolType
	 */
	public void storeSchoolType(int id, String name, String info, String category, String locKey, int maxAge, boolean isFreetimeType, boolean isFamilyFreetimeType, int order, String typeStringId) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolSeason
	 */
	public SchoolSeason getSchoolSeason(Object primaryKey) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getCurrentSchoolSeason
	 */
	public SchoolSeason getCurrentSchoolSeason() throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#removeSchoolSeason
	 */
	public void removeSchoolSeason(int id) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolSeasons
	 */
	public Collection findAllSchoolSeasons() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllPreviousSchoolSeasons
	 */
	public Collection findAllPreviousSchoolSeasons(SchoolSeason schoolSeason) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllPreviousSchoolSeasons
	 */
	public Collection findAllPreviousSchoolSeasons(int schoolSeasonID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findPreviousSchoolSeason
	 */
	public SchoolSeason findPreviousSchoolSeason(int schoolSeasonID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolSeason
	 */
	public void storeSchoolSeason(int id, String name, Date start, Date end, Date due_date) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findClassMemberInClass
	 */
	public SchoolClassMember findClassMemberInClass(int studentID, int schoolClassID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findByStudentAndSeason
	 */
	public SchoolClassMember findByStudentAndSeason(int userID, int seasonID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findByStudentAndSchoolAndSeason
	 */
	public SchoolClassMember findByStudentAndSchoolAndSeason(int userID, int schoolID, int seasonID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findByStudentAndSeason
	 */
	public SchoolClassMember findByStudentAndSeason(User user, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findByStudentAndSeason
	 */
	public SchoolClassMember findByStudentAndSeason(SchoolClassMember student, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findClassMember
	 */
	public Collection findClassMember(int studentID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSubGroupPlacements
	 */
	public Collection findSubGroupPlacements(int studentID, int schoolID, int seasonID) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findClassMemberInSchool
	 */
	public Collection findClassMemberInSchool(int studentID, int schoolID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findClassMemberInChildCare
	 */
	public Collection findClassMemberInChildCare(int studentID, int schoolID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findStudentsInClass
	 */
	public Collection findStudentsInClass(int studentClassID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findStudentsInClassAndYear
	 */
	public Collection findStudentsInClassAndYear(int studentClassID, int schoolYearID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findStudentsInSchool
	 */
	public Collection findStudentsInSchool(int schoolID, int schoolClassID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolByDate
	 */
	public Collection findSchoolByDate(int schoolID, int schoolClassID, java.sql.Date date) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findStudentsInSchoolByDate
	 */
	public Collection findStudentsInSchoolByDate(int schoolID, int schoolClassID, java.sql.Date date, boolean showNotYetActive) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findStudentsInSchoolByDate
	 */
	public Collection findStudentsInSchoolByDate(int schoolID, int schoolClassID, String schoolCategory, java.sql.Date date, boolean showNotYetActive) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findStudentsInSchoolByDateChildcare
	 */
	public Collection findStudentsInSchoolByDateChildcare(int schoolID, int schoolClassID, String schoolCategory, java.sql.Date date, boolean showNotYetActive) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findStudentsInSchoolByDate
	 */
	public Collection findStudentsInSchoolByDate(int schoolID, int schoolClassID, java.sql.Date date) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findStudentsInSchoolByDate
	 */
	public Collection findStudentsInSchoolByDate(int schoolID, int schoolClassID, String schoolCategory, java.sql.Date date) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassMember
	 */
	public SchoolClassMember findSchoolClassMember(int userID, int schoolClassID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findStudentsBySchoolAndSeasonAndYear
	 */
	public Collection findStudentsBySchoolAndSeasonAndYear(int schoolID, int seasonID, int yearID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findStudentsBySchoolAndSeason
	 */
	public Collection findStudentsBySchoolAndSeason(int schoolID, int seasonID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#removeSchoolClassMemberFromClass
	 */
	public void removeSchoolClassMemberFromClass(int studentID, int schoolClassID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#removeSchoolClassMember
	 */
	public void removeSchoolClassMember(int studentID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolTypeIdFromSchoolClass
	 */
	public int getSchoolTypeIdFromSchoolClass(int schoolClassID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolClassMemberCC
	 */
	public SchoolClassMember storeSchoolClassMemberCC(int studentID, int schoolClassID, int schoolTypeID, Timestamp registerDate, int registrator) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolClassMemberCC
	 */
	public SchoolClassMember storeSchoolClassMemberCC(int studentID, int schoolClassID, int schoolTypeID, Timestamp registerDate, int registrator, String notes) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolClassMemberCC
	 */
	public SchoolClassMember storeSchoolClassMemberCC(int studentID, int schoolClassID, int schoolTypeID, Timestamp registerDate, Timestamp removedDate, int registrator, String notes) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolClassMember
	 */
	public SchoolClassMember storeSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, int registrator) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolClassMember
	 */
	public SchoolClassMember storeSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, int registrator, String notes) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolClassMember
	 */
	public SchoolClassMember storeSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, Timestamp removedDate, int registrator, String notes) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolClassMember
	 */
	public SchoolClassMember storeSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, Timestamp removedDate, int registrator, String notes, String language) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeNewSchoolClassMember
	 */
	public SchoolClassMember storeNewSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, int registrator, String notes, String languageID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeNewSchoolClassMember
	 */
	public SchoolClassMember storeNewSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, Timestamp removedDate, int registrator, String notes, String sLanguage) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClass
	 */
	public SchoolClass findSchoolClass(Object primaryKey) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassesBySchool
	 */
	public Collection findSchoolClassesBySchool(int schoolID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassesBySchoolAndCategory
	 */
	public Collection findSchoolClassesBySchoolAndCategory(int schoolID, String category) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findElementarySchoolClassesBySchool
	 */
	public Collection findElementarySchoolClassesBySchool(int schoolID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findChildcareClassesBySchool
	 */
	public Collection findChildcareClassesBySchool(int schoolID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassesBySchoolAndSeason
	 */
	public Collection findSchoolClassesBySchoolAndSeason(int schoolID, int schoolSeasonID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassesBySchoolAndYear
	 */
	public Collection findSchoolClassesBySchoolAndYear(int schoolID, int schoolYearID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassesBySchoolAndSeasonAndYear
	 */
	public Collection findSchoolClassesBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID, int schoolYearID, boolean showSubGroups) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassesBySchoolAndSchoolTypeAndSeason
	 */
	public Collection findSchoolClassesBySchoolAndSchoolTypeAndSeason(int schoolID, int schoolTypeID, int schoolSeasonID, Boolean showSubGroups, Boolean showNonSeasonGroups) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassesBySchoolAndSeasonAndYear
	 */
	public Collection findSchoolClassesBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID, int schoolYearID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassesBySchoolAndSeasonAndYearAndStudyPath
	 */
	public Collection findSchoolClassesBySchoolAndSeasonAndYearAndStudyPath(int schoolID, int schoolSeasonID, int schoolYearID, int studyPathID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassesBySchoolAndSeasonAndYears
	 */
	public Collection findSchoolClassesBySchoolAndSeasonAndYears(int schoolID, int schoolSeasonID, String[] schoolYearIDs) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassesBySchoolAndSeasonAndYears
	 */
	public Collection findSchoolClassesBySchoolAndSeasonAndYears(int schoolID, int schoolSeasonID, String[] schoolYearIDs, boolean showSubGroups) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassesByTeacher
	 */
	public Collection findSchoolClassesByTeacher(int teacherID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSubGroupPlacements
	 */
	public Collection findSubGroupPlacements(SchoolClass group) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassesBySchoolAndTeacher
	 */
	public Collection findSchoolClassesBySchoolAndTeacher(int schoolID, int teacherID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findSchoolClassesBySchoolAndSeasonAndTeacher
	 */
	public Collection findSchoolClassesBySchoolAndSeasonAndTeacher(int schoolID, int schoolSeasonID, int teacherID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getNumberOfStudentsInClass
	 */
	public int getNumberOfStudentsInClass(int schoolClassID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#removeSchoolClass
	 */
	public void removeSchoolClass(int schoolClassID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#invalidateSchoolClass
	 */
	public void invalidateSchoolClass(int schoolClassID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolClass
	 */
	public SchoolClass storeSchoolClass(int schoolClassID, String className, int schoolID, int schoolTypeID, int seasonID, String[] schoolYearIDs, String[] teacherIDs) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolClass
	 */
	public SchoolClass storeSchoolClass(int schoolClassID, String className, int schoolID, int schoolTypeID, int seasonID, String[] schoolYearIDs, String[] teacherIDs, String[] studyPathIDs) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolClass
	 */
	public SchoolClass storeSchoolClass(int schoolClassID, String className, int schoolID, int schoolTypeID, int seasonID, String[] schoolYearIDs, String[] teacherIDs, String[] studyPathIDs, String groupStringId) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolClass
	 */
	public SchoolClass storeSchoolClass(int schoolClassID, String className, int schoolID, int schoolSeasonID, int schoolYearID, int teacherID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolArea
	 */
	public SchoolArea getSchoolArea(Object primaryKey) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolSubArea
	 */
	public SchoolSubArea getSchoolSubArea(Object primaryKey) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#removeSchoolArea
	 */
	public void removeSchoolArea(int id) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#removeSchoolSubArea
	 */
	public void removeSchoolSubArea(int id) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolAreas
	 */
	public Collection findAllSchoolAreas() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolSubAreas
	 */
	public Collection findAllSchoolSubAreas() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolAreasByType
	 */
	public Collection findAllSchoolAreasByType(int type_id) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolAreasByTypes
	 */
	public Collection findAllSchoolAreasByTypes(Collection types) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolArea
	 */
	public void storeSchoolArea(int id, String name, String info, String city) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolSubArea
	 */
	public void storeSchoolSubArea(int id, String name, int areaid) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#storeSchoolDepartment
	 */
	public void storeSchoolDepartment(String description, String phone, int schoolID, int schDepID) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolUserBusiness
	 */
	public SchoolUserBusiness getSchoolUserBusiness() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#hasSchoolRelationToYear
	 */
	public boolean hasSchoolRelationToYear(School school, SchoolYear schoolYear) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#hasAfterSchoolActivities
	 */
	public boolean hasAfterSchoolActivities(int schoolID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#hasSchoolPlacements
	 */
	public boolean hasSchoolPlacements(int userID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#hasGroupPlacement
	 */
	public boolean hasGroupPlacement(int userID, int groupID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#hasGroupPlacement
	 */
	public boolean hasGroupPlacement(int userID, int groupID, boolean isSubGroup) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolPhone
	 */
	public String getSchoolPhone(int schoolID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getHomeCommuneSchools
	 */
	public Collection getHomeCommuneSchools(Collection schools) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolsByCategory
	 */
	public Collection findAllSchoolsByCategory(String categoryString) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getProperty
	 */
	public String getProperty(School school, String propertyName) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#setProperty
	 */
	public void setProperty(School school, String propertyName, String propertyValue) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#findAllSchoolSubAreasByArea
	 */
	public Collection findAllSchoolSubAreasByArea(String area) throws RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolClassMemberLogHome
	 */
	public SchoolClassMemberLogHome getSchoolClassMemberLogHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#getSchoolSubAreaHome
	 */
	public SchoolSubAreaHome getSchoolSubAreaHome() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#addToSchoolClassMemberLog
	 */
	public void addToSchoolClassMemberLog(SchoolClassMember member, Date endDate, User performer) throws IllegalArgumentException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#addToSchoolClassMemberLog
	 */
	public void addToSchoolClassMemberLog(SchoolClassMember member, SchoolClass schoolClass, Date endDate, User performer) throws IllegalArgumentException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#addToSchoolClassMemberLog
	 */
	public void addToSchoolClassMemberLog(int schoolClassMemberID, int schoolClassID, Date startDate, Date endDate, User performer) throws IllegalArgumentException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.school.business.SchoolBusinessBean#addToSchoolClassMemberLog
	 */
	public void addToSchoolClassMemberLog(SchoolClassMember member, SchoolClass schoolClass, Date startDate, Date endDate, User performer) throws IllegalArgumentException, java.rmi.RemoteException;

}
