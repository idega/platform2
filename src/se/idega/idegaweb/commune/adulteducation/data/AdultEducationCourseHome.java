/*
 * $Id: AdultEducationCourseHome.java,v 1.2 2005/06/03 06:51:18 laddi Exp $
 * Created on Jun 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolType;
import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/06/03 06:51:18 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface AdultEducationCourseHome extends IDOHome {

	public AdultEducationCourse create() throws javax.ejb.CreateException;

	public AdultEducationCourse findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindBySeasonAndCode
	 */
	public AdultEducationCourse findBySeasonAndCode(SchoolSeason season, String code) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindBySeasonAndCode
	 */
	public AdultEducationCourse findBySeasonAndCode(Object season, String code) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySeasonAndSchool
	 */
	public Collection findAllBySeasonAndSchool(SchoolSeason season, School school) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySeasonAndSchool
	 */
	public Collection findAllBySeasonAndSchool(Object season, Object school) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySeasonAndTypeAndSchoolAndStudyPathGroup
	 */
	public Collection findAllBySeasonAndTypeAndSchoolAndStudyPathGroup(SchoolSeason season, SchoolType type,
			School school, SchoolStudyPathGroup group) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySeasonAndTypeAndSchoolAndStudyPathGroup
	 */
	public Collection findAllBySeasonAndTypeAndSchoolAndStudyPathGroup(Object season, Object type, Object school,
			Object group) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySchoolAndSeasonAndStudyPathGroupConnectedToChoices
	 */
	public Collection findAllBySchoolAndSeasonAndStudyPathGroupConnectedToChoices(Object school, Object season,
			Object group, Object[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySeasonAndSchoolAndStudyPath
	 */
	public Collection findAllBySeasonAndSchoolAndStudyPath(SchoolSeason season, School school, SchoolStudyPath studyPath)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySeasonAndSchoolAndStudyPath
	 */
	public Collection findAllBySeasonAndSchoolAndStudyPath(Object season, Object school, Object studyPath)
			throws FinderException;
}
