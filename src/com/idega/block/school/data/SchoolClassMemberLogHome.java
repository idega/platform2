/*
 * $Id: SchoolClassMemberLogHome.java,v 1.1 2005/01/04 13:52:24 laddi Exp $
 * Created on 27.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/01/04 13:52:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolClassMemberLogHome extends IDOHome {

	public SchoolClassMemberLog create() throws javax.ejb.CreateException;

	public SchoolClassMemberLog findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindAllByUser
	 */
	public Collection findAllByUser(User user) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindOpenLogByUser
	 */
	public SchoolClassMemberLog findOpenLogByUser(User user) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindOpenLogByUserAndSchoolClass
	 */
	public SchoolClassMemberLog findOpenLogByUserAndSchoolClass(User user, SchoolClass schoolClass) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindLatestLogByUser
	 */
	public SchoolClassMemberLog findLatestLogByUser(User user) throws FinderException;

}
