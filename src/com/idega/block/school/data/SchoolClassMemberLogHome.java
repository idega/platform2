/*
 * $Id: SchoolClassMemberLogHome.java,v 1.3 2005/01/19 09:39:51 anders Exp $
 * Created on 10.1.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/01/19 09:39:51 $ by $Author: anders $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public interface SchoolClassMemberLogHome extends IDOHome {

	public SchoolClassMemberLog create() throws javax.ejb.CreateException;

	public SchoolClassMemberLog findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindAllBySchoolClassMember
	 */
	public Collection findAllBySchoolClassMember(SchoolClassMember member) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindOpenLogByUser
	 */
	public SchoolClassMemberLog findOpenLogByUser(SchoolClassMember member) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindOpenLogByUserAndSchoolClass
	 */
	public SchoolClassMemberLog findOpenLogByUserAndSchoolClass(SchoolClassMember member, SchoolClass schoolClass) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindLatestLogByUser
	 */
	public SchoolClassMemberLog findLatestLogByUser(SchoolClassMember member) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindByPlacementAndDate
	 */
	public SchoolClassMemberLog findByPlacementAndDate(SchoolClassMember member, Date date) throws FinderException;
}
