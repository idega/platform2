/*
 * $Id: SchoolClassMemberLog.java,v 1.1 2005/01/04 13:52:24 laddi Exp $
 * Created on 27.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.sql.Date;


import com.idega.data.IDOEntity;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/01/04 13:52:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolClassMemberLog extends IDOEntity {

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getUserID
	 */
	public int getUserID();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getSchoolClass
	 */
	public SchoolClass getSchoolClass();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getSchoolClassID
	 */
	public int getSchoolClassID();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getStartDate
	 */
	public Date getStartDate();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getEndDate
	 */
	public Date getEndDate();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setUser
	 */
	public void setUser(Object userPK);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setSchoolClass
	 */
	public void setSchoolClass(SchoolClass schoolClass);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setSchoolClass
	 */
	public void setSchoolClass(Object schoolClassPK);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setStartDate
	 */
	public void setStartDate(Date startDate);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setEndDate
	 */
	public void setEndDate(Date endDate);

}
