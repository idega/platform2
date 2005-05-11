/*
 * $Id: AdultEducationChoiceHome.java,v 1.2 2005/05/11 17:44:48 laddi Exp $
 * Created on May 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.school.data.SchoolSeason;
import com.idega.data.IDOHome;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/05/11 17:44:48 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface AdultEducationChoiceHome extends IDOHome {

	public AdultEducationChoice create() throws javax.ejb.CreateException;

	public AdultEducationChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

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
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceBMPBean#ejbFindByUserAndStudyPathAndChoiceOrder
	 */
	public AdultEducationChoice findByUserAndStudyPathAndChoiceOrder(Object userPK, Object studyPathPK, int choiceOrder,
			String[] statuses) throws FinderException;
}
