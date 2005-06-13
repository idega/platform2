/*
 * $Id: SchoolClassHome.java,v 1.33 2005/06/13 07:05:15 laddi Exp $
 * Created on Jun 13, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/06/13 07:05:15 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.33 $
 */
public interface SchoolClassHome extends IDOHome {

	public SchoolClass create() throws javax.ejb.CreateException;

	public SchoolClass findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchool
	 */
	public Collection findBySchool(School school) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchool
	 */
	public Collection findBySchool(int schoolID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeason
	 */
	public Collection findBySchoolAndSeason(School school, SchoolSeason schoolSeason) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeason
	 */
	public Collection findBySchoolAndSeason(int schoolID, int schoolSeasonID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndYear
	 */
	public Collection findBySchoolAndYear(School school, SchoolYear schoolYear) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndYear
	 */
	public Collection findBySchoolAndYear(int schoolID, int schoolYearID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndInYear
	 */
	public Collection findBySchoolAndInYear(int schoolID, int schoolYearID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeasonAndYear
	 */
	public Collection findBySchoolAndSeasonAndYear(School school, SchoolSeason schoolSeason, SchoolYear schoolYear)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeasonAndYear
	 */
	public Collection findBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID, int schoolYearID,
			boolean showSubGroups) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeasonAndInYear
	 */
	public Collection findBySchoolAndSeasonAndInYear(int schoolID, int schoolSeasonID, int schoolYearID,
			boolean showSubGroups) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeasonAndYearAndStudyPath
	 */
	public Collection findBySchoolAndSeasonAndYearAndStudyPath(School school, SchoolSeason schoolSeason,
			SchoolYear schoolYear, SchoolStudyPath studyPath, boolean showSubGroups) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeasonAndYear
	 */
	public Collection findBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID, int schoolYearID)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeasonAndInYear
	 */
	public Collection findBySchoolAndSeasonAndInYear(int schoolID, int schoolSeasonID, int schoolYearID)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeasonAndInYear
	 */
	public Collection findBySchoolAndSeasonAndInYear(int schoolID, int schoolSeasonID, int schoolYearID, int studyPathID)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeasonAndYears
	 */
	public Collection findBySchoolAndSeasonAndYears(int schoolID, int schoolSeasonID, String[] schoolYearIDs)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeasonAndYears
	 */
	public Collection findBySchoolAndSeasonAndYears(int schoolID, int schoolSeasonID, String[] schoolYearIDs,
			boolean showSubGroups) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeasonAndCode
	 */
	public Collection findBySchoolAndSeasonAndCode(School school, SchoolSeason season, String code)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySeasonAndYear
	 */
	public Collection findBySeasonAndYear(SchoolSeason schoolSeason, SchoolYear schoolYear) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSchoolTypeAndSeason
	 */
	public Collection findBySchoolAndSchoolTypeAndSeason(int schoolID, int schoolTypeID, int seasonID,
			Boolean showSubGroups, Boolean showNonSeasonGroups) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySeasonAndYear
	 */
	public Collection findBySeasonAndYear(int schoolSeasonID, int schoolYearID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndCategory
	 */
	public Collection findBySchoolAndCategory(int schoolID, String category) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySeason
	 */
	public Collection findBySeason(SchoolSeason schoolSeason) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySeason
	 */
	public Collection findBySeason(int schoolSeasonID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindByTeacher
	 */
	public Collection findByTeacher(User teacher) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindByTeacher
	 */
	public Collection findByTeacher(int teacherID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndTeacher
	 */
	public Collection findBySchoolAndTeacher(School school, User teacher) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndTeacher
	 */
	public Collection findBySchoolAndTeacher(int schoolID, int teacherID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeasonAndTeacher
	 */
	public Collection findBySchoolAndSeasonAndTeacher(School school, SchoolSeason schoolSeason, User teacher)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolAndSeasonAndTeacher
	 */
	public Collection findBySchoolAndSeasonAndTeacher(int schoolID, int schoolSeasonID, int teacherID)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindByNameAndSchool
	 */
	public SchoolClass findByNameAndSchool(String className, School school) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindByNameAndSchool
	 */
	public SchoolClass findByNameAndSchool(String className, int schoolID) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindBySchoolClassNameSchoolSchoolYearSchoolSeason
	 */
	public SchoolClass findBySchoolClassNameSchoolSchoolYearSchoolSeason(String className, School school,
			SchoolYear schoolYear, SchoolSeason schoolSeason) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbHomeGetNumberOfStudentsInClass
	 */
	public int getNumberOfStudentsInClass(int schoolClassID) throws IDOException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassBMPBean#ejbFindOneBySchool
	 */
	public SchoolClass findOneBySchool(int schoolID) throws FinderException;
}
