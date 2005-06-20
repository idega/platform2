/*
 * $Id: Grade.java,v 1.1 2005/06/20 12:55:48 laddi Exp $
 * Created on Jun 20, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import com.idega.data.IDOEntity;


/**
 * Last modified: $Date: 2005/06/20 12:55:48 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface Grade extends IDOEntity {

	/**
	 * @see com.idega.block.school.data.GradeBMPBean#getSchoolType
	 */
	public SchoolType getSchoolType();

	/**
	 * @see com.idega.block.school.data.GradeBMPBean#getSchoolTypePK
	 */
	public Object getSchoolTypePK();

	/**
	 * @see com.idega.block.school.data.GradeBMPBean#getGrade
	 */
	public String getGrade();

	/**
	 * @see com.idega.block.school.data.GradeBMPBean#getLocalizedKey
	 */
	public String getLocalizedKey();

	/**
	 * @see com.idega.block.school.data.GradeBMPBean#setSchoolType
	 */
	public void setSchoolType(SchoolType type);

	/**
	 * @see com.idega.block.school.data.GradeBMPBean#setGrade
	 */
	public void setGrade(String grade);

	/**
	 * @see com.idega.block.school.data.GradeBMPBean#setLocalizedKey
	 */
	public void setLocalizedKey(String localizedKey);
}
