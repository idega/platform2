/*
 * Created on 2005-apr-21
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface SchoolStudyPathHome extends IDOHome {
	public SchoolStudyPath create() throws javax.ejb.CreateException;

	public SchoolStudyPath findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindAllStudyPaths
	 */
	public Collection findAllStudyPaths() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindAllStudyPathsByCodeLength
	 */
	public Collection findAllStudyPathsByCodeLength(int codeLength)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindByCode
	 */
	public SchoolStudyPath findByCode(String code) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindByCodeAndSchoolType
	 */
	public SchoolStudyPath findByCodeAndSchoolType(String code, int schoolTypeId)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindStudyPaths
	 */
	public Collection findStudyPaths(School school)
			throws IDORelationshipException, FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindStudyPaths
	 */
	public Collection findStudyPaths(School school, Object schoolTypePK)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindStudyPaths
	 */
	public Collection findStudyPaths(School school, Collection schoolTypePKs)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolType
	 */
	public Collection findBySchoolType(int schoolTypeId) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolTypes
	 */
	public Collection findBySchoolTypes(String[] schoolTypeIDs)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolTypes
	 */
	public Collection findBySchoolTypes(Collection schoolTypes)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolCategory
	 */
	public Collection findBySchoolCategory(SchoolCategory schoolCategory)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolAndSchoolCategory
	 */
	public Collection findBySchoolAndSchoolCategory(School school,
			SchoolCategory schoolCategory) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchool
	 */
	public Collection findBySchool(School school) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindAllByIDs
	 */
	public Collection findAllByIDs(String[] studyPathIDs)
			throws FinderException;

}
