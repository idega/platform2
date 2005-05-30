/*
 * $Id: AdultEducationChoiceHome.java,v 1.7 2005/05/30 10:01:42 laddi Exp $
 * Created on May 30, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import java.sql.Date;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.data.IDOHome;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/05/30 10:01:42 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
 */
public interface AdultEducationChoiceHome extends IDOHome {

	public AdultEducationChoice create() throws javax.ejb.CreateException;

	public AdultEducationChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#ejbFindAllBySeasonAndStatuses
	 */
	public Collection findAllBySeasonAndStatuses(SchoolSeason season, String[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#ejbFindAllBySeasonAndStatuses
	 */
	public Collection findAllBySeasonAndStatuses(SchoolSeason season, String[] statuses, int choiceOrder)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#ejbFindAllBySeasonAndTypeAndDateAndStatuses
	 */
	public Collection findAllBySeasonAndTypeAndDateAndStatuses(SchoolSeason season, SchoolType type, Date date,
			String[] statuses, int choiceOrder) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#ejbFindAllBySeasonAndTypeAndDateAndHandlerAndStatuses
	 */
	public Collection findAllBySeasonAndTypeAndDateAndHandlerAndStatuses(SchoolSeason season, SchoolType type, Date date,
			User handler, String[] statuses, int choiceOrder) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#ejbFindAllByUserAndSeason
	 */
	public Collection findAllByUserAndSeason(User user, SchoolSeason season) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#ejbFindAllByUserAndSeason
	 */
	public Collection findAllByUserAndSeason(User user, SchoolSeason season, int choiceOrder) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#ejbFindAllByUserAndSeasonAndStatuses
	 */
	public Collection findAllByUserAndSeasonAndStatuses(User user, SchoolSeason season, String[] statuses)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#ejbFindByUserAndCourse
	 */
	public AdultEducationChoice findByUserAndCourse(Object userPK, Object coursePK) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#ejbFindAllByUserAndSeasonAndStudyPath
	 */
	public Collection findAllByUserAndSeasonAndStudyPath(Object userPK, Object seasonPK, Object studyPathPK,
			String[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#ejbFindByUserAndSeasonAndStudyPathAndChoiceOrder
	 */
	public AdultEducationChoice findByUserAndSeasonAndStudyPathAndChoiceOrder(Object userPK, Object seasonPK,
			Object studyPathPK, int choiceOrder, String[] statuses) throws FinderException;
}
