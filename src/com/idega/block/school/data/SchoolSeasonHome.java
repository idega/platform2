/*
 * $Id: SchoolSeasonHome.java,v 1.9 2005/05/11 07:14:19 laddi Exp $
 * Created on 28.4.2005
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
 * Last modified: $Date: 2005/05/11 07:14:19 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.9 $
 */
public interface SchoolSeasonHome extends IDOHome {

	public SchoolSeason create() throws javax.ejb.CreateException;

	public SchoolSeason findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#ejbFindAllSchoolSeasons
	 */
	public java.util.Collection findAllSchoolSeasons() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#ejbFindAllSchoolSeasonsWithoutCategory
	 */
	public java.util.Collection findAllSchoolSeasonsWithoutCategory() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#ejbFindAllSchoolSeasons
	 */
	public java.util.Collection findAllSchoolSeasons(SchoolCategory category) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#ejbFindAllPreviousSchoolSeasons
	 */
	public java.util.Collection findAllPreviousSchoolSeasons(SchoolSeason schoolSeason) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#ejbFindPreviousSchoolSeason
	 */
	public SchoolSeason findPreviousSchoolSeason(SchoolSeason schoolSeason) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#ejbFindSeasonByDate
	 */
	public SchoolSeason findSeasonByDate(SchoolCategory category, Date date) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#ejbFindCurrentSeason
	 */
	public SchoolSeason findCurrentSeason(SchoolCategory category) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#ejbFindSchoolSeasonsActiveInTimePeriod
	 */
	public Collection findSchoolSeasonsActiveInTimePeriod(SchoolCategory category, Date firstDateInPeriod,
			Date lastDateInPeriod) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#ejbFindNextSeason
	 */
	public SchoolSeason findNextSeason(SchoolSeason currentSeason) throws FinderException;
}
