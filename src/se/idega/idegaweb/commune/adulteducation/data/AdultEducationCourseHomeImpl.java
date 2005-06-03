/*
 * $Id: AdultEducationCourseHomeImpl.java,v 1.2 2005/06/03 06:51:18 laddi Exp $
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
import com.idega.data.IDOFactory;


/**
 * Last modified: $Date: 2005/06/03 06:51:18 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class AdultEducationCourseHomeImpl extends IDOFactory implements AdultEducationCourseHome {

	protected Class getEntityInterfaceClass() {
		return AdultEducationCourse.class;
	}

	public AdultEducationCourse create() throws javax.ejb.CreateException {
		return (AdultEducationCourse) super.createIDO();
	}

	public AdultEducationCourse findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (AdultEducationCourse) super.findByPrimaryKeyIDO(pk);
	}

	public AdultEducationCourse findBySeasonAndCode(SchoolSeason season, String code) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((AdultEducationCourseBMPBean) entity).ejbFindBySeasonAndCode(season, code);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public AdultEducationCourse findBySeasonAndCode(Object season, String code) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((AdultEducationCourseBMPBean) entity).ejbFindBySeasonAndCode(season, code);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllBySeasonAndSchool(SchoolSeason season, School school) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationCourseBMPBean) entity).ejbFindAllBySeasonAndSchool(season, school);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySeasonAndSchool(Object season, Object school) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationCourseBMPBean) entity).ejbFindAllBySeasonAndSchool(season, school);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySeasonAndTypeAndSchoolAndStudyPathGroup(SchoolSeason season, SchoolType type,
			School school, SchoolStudyPathGroup group) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationCourseBMPBean) entity).ejbFindAllBySeasonAndTypeAndSchoolAndStudyPathGroup(
				season, type, school, group);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySeasonAndTypeAndSchoolAndStudyPathGroup(Object season, Object type, Object school,
			Object group) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationCourseBMPBean) entity).ejbFindAllBySeasonAndTypeAndSchoolAndStudyPathGroup(
				season, type, school, group);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySchoolAndSeasonAndStudyPathGroupConnectedToChoices(Object school, Object season,
			Object group, Object[] statuses) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationCourseBMPBean) entity).ejbFindAllBySchoolAndSeasonAndStudyPathGroupConnectedToChoices(
				school, season, group, statuses);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySeasonAndSchoolAndStudyPath(SchoolSeason season, School school, SchoolStudyPath studyPath)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationCourseBMPBean) entity).ejbFindAllBySeasonAndSchoolAndStudyPath(season,
				school, studyPath);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySeasonAndSchoolAndStudyPath(Object season, Object school, Object studyPath)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationCourseBMPBean) entity).ejbFindAllBySeasonAndSchoolAndStudyPath(season,
				school, studyPath);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
