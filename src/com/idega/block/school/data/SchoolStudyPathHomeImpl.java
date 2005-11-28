/**
 * 
 */
package com.idega.block.school.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;
import com.idega.data.IDORelationshipException;


/**
 * <p>
 * TODO Dainis Describe Type SchoolStudyPathHomeImpl
 * </p>
 *  Last modified: $Date: 2005/11/28 18:54:52 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.19.2.1 $
 */
public class SchoolStudyPathHomeImpl extends IDOFactory implements SchoolStudyPathHome {

	protected Class getEntityInterfaceClass() {
		return SchoolStudyPath.class;
	}

	public SchoolStudyPath create() throws javax.ejb.CreateException {
		return (SchoolStudyPath) super.createIDO();
	}

	public SchoolStudyPath findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (SchoolStudyPath) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllStudyPaths() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindAllStudyPaths();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllStudyPathsByCodeLength(int codeLength) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindAllStudyPathsByCodeLength(codeLength);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolStudyPath findByCode(String code) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolStudyPathBMPBean) entity).ejbFindByCode(code);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolStudyPath findByCodeAndSchoolType(String code, int schoolTypeId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolStudyPathBMPBean) entity).ejbFindByCodeAndSchoolType(code, schoolTypeId);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findStudyPaths(School school) throws IDORelationshipException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindStudyPaths(school);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findStudyPaths(School school, SchoolStudyPathGroup group) throws IDORelationshipException,
			FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindStudyPaths(school, group);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findStudyPaths(School school, Object schoolTypePK) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindStudyPaths(school, schoolTypePK);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findStudyPaths(School school, SchoolStudyPathGroup group, Object schoolTypePK)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindStudyPaths(school, group, schoolTypePK);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findStudyPaths(School school, Collection schoolTypePKs) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindStudyPaths(school, schoolTypePKs);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findStudyPaths(School school, SchoolStudyPathGroup group, Collection schoolTypePKs)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindStudyPaths(school, group, schoolTypePKs);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolType(int schoolTypeId) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindBySchoolType(schoolTypeId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolTypes(String[] schoolTypeIDs) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindBySchoolTypes(schoolTypeIDs);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolTypes(Collection schoolTypes) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindBySchoolTypes(schoolTypes);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolType(SchoolType schoolType, SchoolStudyPathGroup group) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindBySchoolType(schoolType, group);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolStudyPathGroup(SchoolStudyPathGroup group) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindBySchoolStudyPathGroup(group);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolCategory(SchoolCategory schoolCategory) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindBySchoolCategory(schoolCategory);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSchoolCategory(School school, SchoolCategory schoolCategory)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindBySchoolAndSchoolCategory(school,
				schoolCategory);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchool(School school) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindBySchool(school);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByIDs(String[] studyPathIDs) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathBMPBean) entity).ejbFindAllByIDs(studyPathIDs);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
