/*
 * $Id: SchoolClassMemberHomeImpl.java,v 1.68 2005/01/19 08:11:06 anders Exp $
 * Created on 29.12.2004
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
import com.idega.data.IDOFactory;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/01/19 08:11:06 $ by $Author: anders $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.68 $
 */
public class SchoolClassMemberHomeImpl extends IDOFactory implements SchoolClassMemberHome {

	protected Class getEntityInterfaceClass() {
		return SchoolClassMember.class;
	}

	public SchoolClassMember create() throws javax.ejb.CreateException {
		return (SchoolClassMember) super.createIDO();
	}

	public SchoolClassMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (SchoolClassMember) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllOrderedByRegisterDate(User user) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllOrderedByRegisterDate(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllOrderedByRegisterDate(int userID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllOrderedByRegisterDate(userID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolClass(SchoolClass schoolClass) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchoolClass(schoolClass);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolClass(int schoolClassID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchoolClass(schoolClassID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolClasses(Collection schoolClasses) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchoolClasses(schoolClasses);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolClassAndYear(int schoolClassID, int schoolYearID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchoolClassAndYear(schoolClassID, schoolYearID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolClassAndYearAndStudyPath(SchoolClass group, SchoolYear schoolYear, SchoolStudyPath studyPath) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchoolClassAndYearAndStudyPath(group, schoolYear, studyPath);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByStudent(User student) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindByStudent(student);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByStudent(int studentID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindByStudent(studentID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByStudentAndTypes(int studentID, Collection schoolTypes) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindByStudentAndTypes(studentID, schoolTypes);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public java.util.Collection findAllBySchoolAndUsersWithSchoolYearAndNotRemoved(int schoolId, java.util.Collection users) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllBySchoolAndUsersWithSchoolYearAndNotRemoved(schoolId, users);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByStudentAndSchoolAndTypes(int studentID, int schoolID, Collection schoolTypes) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindByStudentAndSchoolAndTypes(studentID, schoolID, schoolTypes);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolClassMember findByUserAndSchoolClass(User user, SchoolClass schoolClass) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindByUserAndSchoolClass(user, schoolClass);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMember findByUserAndSchoolClass(int userID, int schoolClassID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindByUserAndSchoolClass(userID, schoolClassID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMember findByUserAndSeason(User user, SchoolSeason season) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindByUserAndSeason(user, season);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMember findByUserAndSeason(int userID, int seasonID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindByUserAndSeason(userID, seasonID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMember findByUserAndSchool(int userID, int schoolID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindByUserAndSchool(userID, schoolID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public int getNumberOfPlacingsAtSchool(int userID, int schoolID) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((SchoolClassMemberBMPBean) entity).ejbHomeGetNumberOfPlacingsAtSchool(userID, schoolID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfPlacings(int userID) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((SchoolClassMemberBMPBean) entity).ejbHomeGetNumberOfPlacings(userID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfPlacings(int userID, int schoolClassID) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((SchoolClassMemberBMPBean) entity).ejbHomeGetNumberOfPlacings(userID, schoolClassID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfPlacingsBySchoolCategory(User child, SchoolCategory schoolCategory) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((SchoolClassMemberBMPBean) entity).ejbHomeGetNumberOfPlacingsBySchoolCategory(child, schoolCategory);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfSubGroupPlacings(int userID, int schoolClassID) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((SchoolClassMemberBMPBean) entity).ejbHomeGetNumberOfSubGroupPlacings(userID, schoolClassID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAllSubGroupPlacements(int userID, int schoolID, int seasonID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllSubGroupPlacements(userID, schoolID, seasonID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolClassMember findLatestByUserAndSchool(int userID, int schoolID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindLatestByUserAndSchool(userID, schoolID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMember findLatestByUserAndSchool(int userID, int schoolID, Collection schoolTypes) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindLatestByUserAndSchool(userID, schoolID, schoolTypes);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMember findLatestByUser(User user) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindLatestByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMember findLatestByUserAndSchoolType(User user, SchoolType type) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindLatestByUserAndSchoolType(user, type);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMember findLatestByUserAndSchCategory(User user, SchoolCategory cat) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindLatestByUserAndSchCategory(user, cat);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMember findLatestByUserAndSchCategoryAndSeason(User user, SchoolCategory cat, SchoolSeason season) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindLatestByUserAndSchCategoryAndSeason(user, cat, season);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findActiveByCategorySeasonAndSchools(SchoolCategory cat, SchoolSeason season, String[] schoolIds, boolean notInSchools) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindActiveByCategorySeasonAndSchools(cat, season, schoolIds, notInSchools);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByCategory(SchoolCategory category) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllByCategory(category);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	
	public SchoolClassMember findActiveByStudentSchoolAndCategory(int studentId, int schoolId, SchoolCategory category) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindActiveByStudentSchoolAndCategory(studentId, schoolId, category);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
	
	public SchoolClassMember findNotTerminatedByStudentSchoolAndCategory(int studentId, int schoolId, SchoolCategory category) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindNotTerminatedByStudentSchoolAndCategory(studentId, schoolId, category);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}


	public Collection findAllByUserAndSchoolCategory(User user, SchoolCategory cat) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllByUserAndSchoolCategory(user, cat);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolClassMember findLatestFromElemAndHighSchoolByUser(User user) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindLatestFromElemAndHighSchoolByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMember findLatestFromElemAndHighSchoolByUserAndSeason(User user, SchoolSeason season) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindLatestFromElemAndHighSchoolByUserAndSeason(user, season);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByStudentAndSchool(int userID, int schoolID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindByStudentAndSchool(userID, schoolID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByUserAndSeason(User user, SchoolSeason season) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllByUserAndSeason(user, season);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySchoolStudyPath(SchoolStudyPath studyPath) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllBySchoolStudyPath(studyPath);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByUserAndSeason(int userID, int seasonID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllByUserAndSeason(userID, seasonID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllCurrentInvoiceCompensationBySchoolTypeAndSchools(String operationalField, Collection schools) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllCurrentInvoiceCompensationBySchoolTypeAndSchools(operationalField, schools);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolClassMember findByUserAndSchoolAndSeason(int userID, int schoolID, int seasonID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindByUserAndSchoolAndSeason(userID, schoolID, seasonID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMember findByUserAndSchoolAndSeason(int userID, int schoolID, int seasonID, Collection schoolTypes) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindByUserAndSchoolAndSeason(userID, schoolID, seasonID, schoolTypes);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMember findByUserAndSchoolAndSeasonAndStudyPath(int userID, int schoolID, int seasonID, int studyPathID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberBMPBean) entity).ejbFindByUserAndSchoolAndSeasonAndStudyPath(userID, schoolID, seasonID, studyPathID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public int countByUserAndSchoolAndSeasonAndStudyPath(int userID, int schoolID, int seasonID, int studyPathID) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((SchoolClassMemberBMPBean) entity).ejbHomeCountByUserAndSchoolAndSeasonAndStudyPath(userID, schoolID, seasonID, studyPathID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int countByUserAndSchoolAndSeasonAndStudyPath(User user, School school, SchoolSeason season, SchoolStudyPath studyPath) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((SchoolClassMemberBMPBean) entity).ejbHomeCountByUserAndSchoolAndSeasonAndStudyPath(user, school, season, studyPath);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findBySchoolAndSeasonAndYear(int schoolID, int seasonID, int yearID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchoolAndSeasonAndYear(schoolID, seasonID, yearID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeason(int schoolID, int seasonID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchoolAndSeason(schoolID, seasonID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchool(int schoolID, int schoolClassID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchool(schoolID, schoolClassID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchool(int schoolID, int schoolClassID, Date date) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchool(schoolID, schoolClassID, date);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchool(int schoolID, int schoolClassID, String schoolCategory, Date date) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchool(schoolID, schoolClassID, schoolCategory, date);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchool(int schoolID, int schoolClassID, Date date, boolean showNotYetActive) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchool(schoolID, schoolClassID, date, showNotYetActive);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchool(int schoolID, int schoolClassID, String schoolCategory, Date date, boolean showNotYetActive) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchool(schoolID, schoolClassID, schoolCategory, date, showNotYetActive);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolChildcare(int schoolID, int schoolClassID, Date date, boolean showNotYetActive) throws FinderException{
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchoolChildcare(schoolID, schoolClassID, date, showNotYetActive);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	public Collection findBySchoolChildcare(int schoolID, int schoolClassID, String schoolCategory, Date date, boolean showNotYetActive) throws FinderException{
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindBySchoolChildcare(schoolID, schoolClassID, schoolCategory, date, showNotYetActive);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	
	public Collection findAllLastYearStudentsBySeasonAndMaximumAge(SchoolSeason season, int maxAge) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllLastYearStudentsBySeasonAndMaximumAge(season, maxAge);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllLastYearStudentsBySeasonAndYear(SchoolSeason season, SchoolYear year) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllLastYearStudentsBySeasonAndYear(season, year);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllLastYearStudentsBySeasonAndYearAndMaximumAge(SchoolSeason season, SchoolYear year, int maxAge) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllLastYearStudentsBySeasonAndYearAndMaximumAge(season, year, maxAge);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySeasonAndSchoolYear(SchoolSeason season, SchoolYear schoolYear) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllBySeasonAndSchoolYear(season, schoolYear);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getNumberOfUsersNotAssignedToClassOnGivenDate(Group citizenGroup, Date date, Collection classes, Date firstDateOfBirth, Date lastDateOfBirth) throws IDOException, IDOLookupException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((SchoolClassMemberBMPBean) entity).ejbHomeGetNumberOfUsersNotAssignedToClassOnGivenDate(citizenGroup, date, classes, firstDateOfBirth, lastDateOfBirth);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfUsersNotAssignedToClassOnGivenDateNew(Group citizenGroup, Date date, SchoolSeason schoolSeason, Date firstDateOfBirth, Date lastDateOfBirth) throws IDOException, IDOLookupException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((SchoolClassMemberBMPBean) entity).ejbHomeGetNumberOfUsersNotAssignedToClassOnGivenDateNew(citizenGroup, date, schoolSeason, firstDateOfBirth, lastDateOfBirth);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAllByUserAndPeriodAndSchoolCategory(User child, Date period, SchoolCategory category) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindAllByUserAndPeriodAndSchoolCategory(child, period, category);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByCategorydManagementCommune(String category, String managementType, int communeId, int seasonId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindByCategorydManagementCommune(category, managementType, communeId, seasonId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection getInvoiceIntervalTypes() {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection theReturn = ((SchoolClassMemberBMPBean) entity).ejbHomeGetInvoiceIntervalTypes();
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findSubGroupPlacements() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean) entity).ejbFindSubGroupPlacements();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
