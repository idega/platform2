/**
 * 
 */
package com.idega.block.school.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;


/**
 * <p>
 * TODO Dainis Describe Type SchoolStudyPathHome
 * </p>
 *  Last modified: $Date: 2006/03/08 11:56:44 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.18.2.2 $
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
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolTypeOrderByDescription
	 */
	public Collection findBySchoolTypeOrderByDescription(SchoolType schoolType, SchoolStudyPathGroup group)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolType
	 */
	public Collection findBySchoolType(SchoolType schoolType, SchoolStudyPathGroup group, String orderByColumn)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolStudyPathGroup
	 */
	public Collection findBySchoolStudyPathGroup(SchoolStudyPathGroup group) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolCategory
	 */
	public Collection findBySchoolCategory(SchoolCategory schoolCategory) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchoolAndSchoolCategory
	 */
	public Collection findBySchoolAndSchoolCategory(School school, SchoolCategory schoolCategory)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindBySchool
	 */
	public Collection findBySchool(School school) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathBMPBean#ejbFindAllByIDs
	 */
	public Collection findAllByIDs(String[] studyPathIDs) throws FinderException;
}
