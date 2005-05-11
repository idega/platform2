/*
 * $Id: SchoolStudyPathHome.java,v 1.17 2005/05/11 07:14:19 laddi Exp $
 * Created on 28.4.2005
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
import com.idega.data.IDORelationshipException;


/**
 * Last modified: $Date: 2005/05/11 07:14:19 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.17 $
 */
public interface SchoolStudyPathHome extends IDOHome {

	public SchoolStudyPath create() throws javax.ejb.CreateException;

	public SchoolStudyPath findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindAllStudyPaths
	 */
	public Collection findAllStudyPaths() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindAllStudyPathsByCodeLength
	 */
	public Collection findAllStudyPathsByCodeLength(int codeLength) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindByCode
	 */
	public SchoolStudyPath findByCode(String code) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindByCodeAndSchoolType
	 */
	public SchoolStudyPath findByCodeAndSchoolType(String code, int schoolTypeId) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindStudyPaths
	 */
	public Collection findStudyPaths(School school) throws IDORelationshipException, FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindStudyPaths
	 */
	public Collection findStudyPaths(School school, SchoolStudyPathGroup group) throws IDORelationshipException,
			FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindStudyPaths
	 */
	public Collection findStudyPaths(School school, Object schoolTypePK) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindStudyPaths
	 */
	public Collection findStudyPaths(School school, SchoolStudyPathGroup group, Object schoolTypePK)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindStudyPaths
	 */
	public Collection findStudyPaths(School school, Collection schoolTypePKs) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindStudyPaths
	 */
	public Collection findStudyPaths(School school, SchoolStudyPathGroup group, Collection schoolTypePKs)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolType
	 */
	public Collection findBySchoolType(int schoolTypeId) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolTypes
	 */
	public Collection findBySchoolTypes(String[] schoolTypeIDs) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolTypes
	 */
	public Collection findBySchoolTypes(Collection schoolTypes) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolType
	 */
	public Collection findBySchoolType(SchoolType schoolType, SchoolStudyPathGroup group) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolCategory
	 */
	public Collection findBySchoolCategory(SchoolCategory schoolCategory) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolAndSchoolCategory
	 */
	public Collection findBySchoolAndSchoolCategory(School school, SchoolCategory schoolCategory) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchool
	 */
	public Collection findBySchool(School school) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindAllByIDs
	 */
	public Collection findAllByIDs(String[] studyPathIDs) throws FinderException;
}
