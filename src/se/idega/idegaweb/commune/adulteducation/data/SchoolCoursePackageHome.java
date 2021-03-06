/*
 * $Id: SchoolCoursePackageHome.java,v 1.2 2005/08/08 22:21:37 laddi Exp $
 * Created on Aug 8, 2005
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
import com.idega.data.IDOException;
import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/08/08 22:21:37 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface SchoolCoursePackageHome extends IDOHome {

	public SchoolCoursePackage create() throws javax.ejb.CreateException;

	public SchoolCoursePackage findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#ejbFindAllBySchool
	 */
	public Collection findAllBySchool(School school) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#ejbFindBySchoolAndSeason
	 */
	public Collection findBySchoolAndSeason(School school, SchoolSeason season) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#ejbFindBySchoolAndSeasonAndPackage
	 */
	public Collection findBySchoolAndSeasonAndPackage(School school, SchoolSeason season, CoursePackage coursePackage)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageBMPBean#ejbHomeGetNumberOfSchoolPackages
	 */
	public int getNumberOfSchoolPackages(CoursePackage coursePackage) throws IDOException;
}
