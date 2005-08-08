/*
 * $Id: SchoolCoursePackage.java,v 1.2 2005/08/08 22:21:37 laddi Exp $
 * Created on Aug 8, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import java.util.Collection;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;


/**
 * Last modified: $Date: 2005/08/08 22:21:37 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface SchoolCoursePackage extends IDOEntity {

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#getPackage
	 */
	public CoursePackage getPackage();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#getPackagePK
	 */
	public Object getPackagePK();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#getSchool
	 */
	public School getSchool();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#getSchoolPK
	 */
	public Object getSchoolPK();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#getSeason
	 */
	public SchoolSeason getSeason();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#getSeasonPK
	 */
	public Object getSeasonPK();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#getFreeText
	 */
	public String getFreeText();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#isActive
	 */
	public boolean isActive();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#getCourses
	 */
	public Collection getCourses() throws IDORelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#setPackage
	 */
	public void setPackage(CoursePackage coursePackage);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#setSchool
	 */
	public void setSchool(School school);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#setSeason
	 */
	public void setSeason(SchoolSeason season);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#setFreeText
	 */
	public void setFreeText(String text);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#setActive
	 */
	public void setActive(boolean active);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#addCourse
	 */
	public void addCourse(AdultEducationCourse course) throws IDOAddRelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#addCourses
	 */
	public void addCourses(Collection courses) throws IDOAddRelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#removeCourse
	 */
	public void removeCourse(AdultEducationCourse course) throws IDORemoveRelationshipException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#removeCourses
	 */
	public void removeCourses() throws IDORemoveRelationshipException;
}
