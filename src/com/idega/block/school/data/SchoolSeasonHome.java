/*
 * $Id: SchoolSeasonHome.java,v 1.12 2005/10/26 16:03:38 palli Exp $
 * Created on Oct 26, 2005
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
 * 
 *  Last modified: $Date: 2005/10/26 16:03:38 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.12 $
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
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#ejbFindPendingSeasonsByDate
	 */
	public Collection findPendingSeasonsByDate(SchoolCategory category, Date date) throws FinderException;

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
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#ejbFindCurrentSchoolSeasons
	 */
	public Collection findCurrentSchoolSeasons(SchoolCategory category) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#ejbFindNextSeason
	 */
	public SchoolSeason findNextSeason(SchoolSeason currentSeason) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#ejbFindNextSeason
	 */
	public SchoolSeason findNextSeason(SchoolCategory category, Date date) throws FinderException;

}
