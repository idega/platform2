/*
 * $Id: SchoolClassMemberHome.java,v 1.63 2004/12/07 20:39:18 laddi Exp $
 * Created on 7.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2004/12/07 20:39:18 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.63 $
 */
public interface SchoolClassMemberHome extends IDOHome {

	public SchoolClassMember create() throws javax.ejb.CreateException;

	public SchoolClassMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllOrderedByRegisterDate
	 */
	public Collection findAllOrderedByRegisterDate(User user) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllOrderedByRegisterDate
	 */
	public Collection findAllOrderedByRegisterDate(int userID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindBySchoolClass
	 */
	public Collection findBySchoolClass(SchoolClass schoolClass) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindBySchoolClass
	 */
	public Collection findBySchoolClass(int schoolClassID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindBySchoolClasses
	 */
	public Collection findBySchoolClasses(Collection schoolClasses) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindBySchoolClassAndYear
	 */
	public Collection findBySchoolClassAndYear(int schoolClassID, int schoolYearID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindBySchoolClassAndYearAndStudyPath
	 */
	public Collection findBySchoolClassAndYearAndStudyPath(SchoolClass group, SchoolYear schoolYear, SchoolStudyPath studyPath) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByStudent
	 */
	public Collection findByStudent(User student) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByStudent
	 */
	public Collection findByStudent(int studentID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByStudentAndTypes
	 */
	public Collection findByStudentAndTypes(int studentID, Collection schoolTypes) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllBySchoolAndUsersWithSchoolYearAndNotRemoved
	 */
	public java.util.Collection findAllBySchoolAndUsersWithSchoolYearAndNotRemoved(int schoolId, java.util.Collection users) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByStudentAndSchoolAndTypes
	 */
	public Collection findByStudentAndSchoolAndTypes(int studentID, int schoolID, Collection schoolTypes) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByUserAndSchoolClass
	 */
	public SchoolClassMember findByUserAndSchoolClass(User user, SchoolClass schoolClass) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByUserAndSchoolClass
	 */
	public SchoolClassMember findByUserAndSchoolClass(int userID, int schoolClassID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByUserAndSeason
	 */
	public SchoolClassMember findByUserAndSeason(User user, SchoolSeason season) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByUserAndSeason
	 */
	public SchoolClassMember findByUserAndSeason(int userID, int seasonID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByUserAndSchool
	 */
	public SchoolClassMember findByUserAndSchool(int userID, int schoolID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbHomeGetNumberOfPlacingsAtSchool
	 */
	public int getNumberOfPlacingsAtSchool(int userID, int schoolID) throws IDOException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbHomeGetNumberOfPlacings
	 */
	public int getNumberOfPlacings(int userID) throws IDOException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbHomeGetNumberOfPlacings
	 */
	public int getNumberOfPlacings(int userID, int schoolClassID) throws IDOException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbHomeGetNumberOfSubGroupPlacings
	 */
	public int getNumberOfSubGroupPlacings(int userID, int schoolClassID) throws IDOException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllSubGroupPlacements
	 */
	public Collection findAllSubGroupPlacements(int userID, int schoolID, int seasonID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindLatestByUserAndSchool
	 */
	public SchoolClassMember findLatestByUserAndSchool(int userID, int schoolID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindLatestByUserAndSchool
	 */
	public SchoolClassMember findLatestByUserAndSchool(int userID, int schoolID, Collection schoolTypes) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindLatestByUser
	 */
	public SchoolClassMember findLatestByUser(User user) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindLatestByUserAndSchoolType
	 */
	public SchoolClassMember findLatestByUserAndSchoolType(User user, SchoolType type) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindLatestByUserAndSchCategory
	 */
	public SchoolClassMember findLatestByUserAndSchCategory(User user, SchoolCategory cat) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindLatestByUserAndSchCategoryAndSeason
	 */
	public SchoolClassMember findLatestByUserAndSchCategoryAndSeason(User user, SchoolCategory cat, SchoolSeason season) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindActiveByCategorySeasonAndSchools
	 */
	public Collection findActiveByCategorySeasonAndSchools(SchoolCategory cat, SchoolSeason season, String[] schoolIds, boolean notInSchools) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllByCategory
	 */
	public Collection findAllByCategory(SchoolCategory category) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllByUserAndSchoolCategory
	 */
	public Collection findAllByUserAndSchoolCategory(User user, SchoolCategory cat) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindLatestFromElemAndHighSchoolByUser
	 */
	public SchoolClassMember findLatestFromElemAndHighSchoolByUser(User user) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindLatestFromElemAndHighSchoolByUserAndSeason
	 */
	public SchoolClassMember findLatestFromElemAndHighSchoolByUserAndSeason(User user, SchoolSeason season) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByStudentAndSchool
	 */
	public Collection findByStudentAndSchool(int userID, int schoolID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllByUserAndSeason
	 */
	public Collection findAllByUserAndSeason(User user, SchoolSeason season) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllBySchoolStudyPath
	 */
	public Collection findAllBySchoolStudyPath(SchoolStudyPath studyPath) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllByUserAndSeason
	 */
	public Collection findAllByUserAndSeason(int userID, int seasonID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllCurrentInvoiceCompensationBySchoolTypeAndSchools
	 */
	public Collection findAllCurrentInvoiceCompensationBySchoolTypeAndSchools(String operationalField, Collection schools) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByUserAndSchoolAndSeason
	 */
	public SchoolClassMember findByUserAndSchoolAndSeason(int userID, int schoolID, int seasonID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByUserAndSchoolAndSeason
	 */
	public SchoolClassMember findByUserAndSchoolAndSeason(int userID, int schoolID, int seasonID, Collection schoolTypes) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByUserAndSchoolAndSeasonAndStudyPath
	 */
	public SchoolClassMember findByUserAndSchoolAndSeasonAndStudyPath(int userID, int schoolID, int seasonID, int studyPathID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbHomeCountByUserAndSchoolAndSeasonAndStudyPath
	 */
	public int countByUserAndSchoolAndSeasonAndStudyPath(int userID, int schoolID, int seasonID, int studyPathID) throws IDOException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbHomeCountByUserAndSchoolAndSeasonAndStudyPath
	 */
	public int countByUserAndSchoolAndSeasonAndStudyPath(User user, School school, SchoolSeason season, SchoolStudyPath studyPath) throws IDOException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindBySchoolAndSeasonAndYear
	 */
	public Collection findBySchoolAndSeasonAndYear(int schoolID, int seasonID, int yearID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindBySchoolAndSeason
	 */
	public Collection findBySchoolAndSeason(int schoolID, int seasonID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindBySchool
	 */
	public Collection findBySchool(int schoolID, int schoolClassID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindBySchool
	 */
	public Collection findBySchool(int schoolID, int schoolClassID, Date date) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindBySchool
	 */
	public Collection findBySchool(int schoolID, int schoolClassID, String schoolCategory, Date date) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindBySchool
	 */
	public Collection findBySchool(int schoolID, int schoolClassID, Date date, boolean showNotYetActive) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindBySchool
	 */
	public Collection findBySchool(int schoolID, int schoolClassID, String schoolCategory, Date date, boolean showNotYetActive) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllLastYearStudentsBySeasonAndMaximumAge
	 */
	public Collection findAllLastYearStudentsBySeasonAndMaximumAge(SchoolSeason season, int maxAge) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllLastYearStudentsBySeasonAndYear
	 */
	public Collection findAllLastYearStudentsBySeasonAndYear(SchoolSeason season, SchoolYear year) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllLastYearStudentsBySeasonAndYearAndMaximumAge
	 */
	public Collection findAllLastYearStudentsBySeasonAndYearAndMaximumAge(SchoolSeason season, SchoolYear year, int maxAge) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllBySeasonAndSchoolYear
	 */
	public Collection findAllBySeasonAndSchoolYear(SchoolSeason season, SchoolYear schoolYear) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbHomeGetNumberOfUsersNotAssignedToClassOnGivenDate
	 */
	public int getNumberOfUsersNotAssignedToClassOnGivenDate(Group citizenGroup, Date date, Collection classes, Date firstDateOfBirth, Date lastDateOfBirth) throws IDOException, IDOLookupException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbHomeGetNumberOfUsersNotAssignedToClassOnGivenDateNew
	 */
	public int getNumberOfUsersNotAssignedToClassOnGivenDateNew(Group citizenGroup, Date date, SchoolSeason schoolSeason, Date firstDateOfBirth, Date lastDateOfBirth) throws IDOException, IDOLookupException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindAllByUserAndPeriodAndSchoolCategory
	 */
	public Collection findAllByUserAndPeriodAndSchoolCategory(User child, Date period, SchoolCategory category) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindByCategorydManagementCommune
	 */
	public Collection findByCategorydManagementCommune(String category, String managementType, int communeId, int seasonId) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbHomeGetInvoiceIntervalTypes
	 */
	public Collection getInvoiceIntervalTypes();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#ejbFindSubGroupPlacements
	 */
	public Collection findSubGroupPlacements() throws FinderException;

}
