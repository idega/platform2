/*
 * $Id: SchoolYearHome.java,v 1.11 2005/10/07 13:23:39 laddi Exp $
 * Created on Oct 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/10/07 13:23:39 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.11 $
 */
public interface SchoolYearHome extends IDOHome {

	public SchoolYear create() throws javax.ejb.CreateException;

	public SchoolYear findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindAllSchoolYears
	 */
	public Collection findAllSchoolYears() throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindAllSchoolYearBySchoolType
	 */
	public Collection findAllSchoolYearBySchoolType(int schoolTypeId) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindAllSchoolYearBySchoolCategory
	 */
	public Collection findAllSchoolYearBySchoolCategory(SchoolCategory schoolCategory) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindAllSchoolYearsBySchoolCategory
	 */
	public Collection findAllSchoolYearsBySchoolCategory(SchoolCategory schoolCategory, boolean showSelectable)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindAllBySchoolAndSchoolCategory
	 */
	public Collection findAllBySchoolAndSchoolCategory(School school, SchoolCategory schoolCategory,
			boolean showSelectable) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindAllByAge
	 */
	public Collection findAllByAge(int age) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindAllByAge
	 */
	public Collection findAllByAge(SchoolType schoolType, int age) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindByAge
	 */
	public SchoolYear findByAge(SchoolCategory category, int age) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindAllByIDs
	 */
	public Collection findAllByIDs(String[] schoolYearIDs) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindByYearName
	 */
	public SchoolYear findByYearName(String name) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindByYearName
	 */
	public SchoolYear findByYearName(SchoolType schoolType, String name) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindByYearName
	 */
	public SchoolYear findByYearName(SchoolCategory schoolCategory, String name) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindBySchoolCategory
	 */
	public Collection findBySchoolCategory(String schoolCategory) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#ejbFindPreviousSchoolYearFromAge
	 */
	public SchoolYear findPreviousSchoolYearFromAge(SchoolYear year) throws FinderException;
}
