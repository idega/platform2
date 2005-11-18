/**
 * 
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class SchoolClassMemberGradeHomeImpl extends IDOFactory implements
		SchoolClassMemberGradeHome {
	protected Class getEntityInterfaceClass() {
		return SchoolClassMemberGrade.class;
	}

	public SchoolClassMemberGrade create() throws javax.ejb.CreateException {
		return (SchoolClassMemberGrade) super.createIDO();
	}

	public SchoolClassMemberGrade findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (SchoolClassMemberGrade) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByStudent(SchoolClassMember student)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberGradeBMPBean) entity)
				.ejbFindAllByStudent(student);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolClassMemberGrade findByStudent(SchoolClassMember student)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberGradeBMPBean) entity)
				.ejbFindByStudent(student);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClassMemberGrade findFirstGradeSetForStudent(
			SchoolClassMember student) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassMemberGradeBMPBean) entity)
				.ejbFindFirstGradeSetForStudent(student);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
