/*
 * $Id: SchoolClassMemberLogHome.java,v 1.7 2005/03/07 17:02:19 laddi Exp $
 * Created on 7.3.2005
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
 * Last modified: $Date: 2005/03/07 17:02:19 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
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

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindFutureLogByPlacementAndDate
	 */
	public SchoolClassMemberLog findFutureLogByPlacementAndDate(SchoolClassMember member, Date date) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindByPlacementAndEndDate
	 */
	public SchoolClassMemberLog findByPlacementAndEndDate(SchoolClassMember member, Date date) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindByPlacementAndDateBack
	 */
	public SchoolClassMemberLog findByPlacementAndDateBack(SchoolClassMember member, Date date) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindByPlacementAndDates
	 */
	public Collection findByPlacementAndDates(SchoolClassMember member, Date fromDate, Date toDate) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberLogBMPBean#ejbFindAllByPlacementAndDates
	 */
	public Collection findAllByPlacementAndDates(SchoolClassMember member, Date fromDate, Date toDate) throws FinderException;
}
