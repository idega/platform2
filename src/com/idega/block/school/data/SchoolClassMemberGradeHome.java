/**
 * 
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface SchoolClassMemberGradeHome extends IDOHome {
	public SchoolClassMemberGrade create() throws javax.ejb.CreateException;

	public SchoolClassMemberGrade findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#ejbFindAllByStudent
	 */
	public Collection findAllByStudent(SchoolClassMember student)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#ejbFindByStudent
	 */
	public SchoolClassMemberGrade findByStudent(SchoolClassMember student)
			throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#ejbFindFirstGradeSetForStudent
	 */
	public SchoolClassMemberGrade findFirstGradeSetForStudent(
			SchoolClassMember student) throws FinderException;

}
