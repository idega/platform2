/*
 * $Id: SchoolClassMemberLog.java,v 1.3 2005/02/16 10:36:31 laddi Exp $
 * Created on 10.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.sql.Date;


import com.idega.data.IDOEntity;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/02/16 10:36:31 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public interface SchoolClassMemberLog extends IDOEntity {

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getUserPlacing
	 */
	public User getUserPlacing();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getUserPlacingID
	 */
	public int getUserPlacingID();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getUserTerminating
	 */
	public User getUserTerminating();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getUserTerminatingID
	 */
	public int getUserTerminatingID();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getSchoolClass
	 */
	public SchoolClass getSchoolClass();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getSchoolClassID
	 */
	public int getSchoolClassID();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getSchoolClassMember
	 */
	public SchoolClassMember getSchoolClassMember();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getSchoolClassMemberID
	 */
	public int getSchoolClassMemberID();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getStartDate
	 */
	public Date getStartDate();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#getEndDate
	 */
	public Date getEndDate();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setUserPlacing
	 */
	public void setUserPlacing(User user);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setUserPlacing
	 */
	public void setUserPlacing(Object userPK);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setUserTerminating
	 */
	public void setUserTerminating(User user);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setUserTerminating
	 */
	public void setUserTerminating(Object userPK);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setSchoolClass
	 */
	public void setSchoolClass(SchoolClass schoolClass);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setSchoolClass
	 */
	public void setSchoolClass(Object schoolClassPK);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setSchoolClassMember
	 */
	public void setSchoolClassMember(SchoolClassMember schoolClassMember);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setSchoolClassMember
	 */
	public void setSchoolClassMember(Object schoolClassMemberPK);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setStartDate
	 */
	public void setStartDate(Date startDate);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#setEndDate
	 */
	public void setEndDate(Date endDate);

}
