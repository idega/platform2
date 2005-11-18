/**
 * 
 */
package com.idega.block.school.data;

import java.sql.Timestamp;


import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface SchoolClassMemberGrade extends IDOEntity {
	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#getStudent
	 */
	public SchoolClassMember getStudent();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#getStudentPK
	 */
	public Object getStudentPK();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#getGrade
	 */
	public Grade getGrade();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#getGradePK
	 */
	public Object getGradePK();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#getCreated
	 */
	public Timestamp getCreated();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#isLocked
	 */
	public boolean isLocked();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#setStudent
	 */
	public void setStudent(SchoolClassMember member);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#setGrade
	 */
	public void setGrade(Grade grade);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#setCreated
	 */
	public void setCreated(Timestamp created);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberGradeBMPBean#setLocked
	 */
	public void setLocked(boolean locked);

}
