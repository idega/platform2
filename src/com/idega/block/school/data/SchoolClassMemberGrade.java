/*
 * $Id: SchoolClassMemberGrade.java,v 1.1 2005/06/20 12:55:48 laddi Exp $
 * Created on Jun 20, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.sql.Timestamp;
import com.idega.data.IDOEntity;


/**
 * Last modified: $Date: 2005/06/20 12:55:48 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
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
