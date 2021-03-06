/*
 * $Id: MealChoiceHome.java,v 1.2 2005/10/02 22:12:23 laddi Exp $
 * Created on Oct 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.data.IDOHome;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/10/02 22:12:23 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface MealChoiceHome extends IDOHome {

	public MealChoice create() throws javax.ejb.CreateException;

	public MealChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#ejbFindByUserAndSeason
	 */
	public MealChoice findByUserAndSeason(User user, School school, SchoolSeason season) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#ejbFindAllBySchoolAndSeason
	 */
	public Collection findAllBySchoolAndSeason(School school, SchoolSeason season) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceBMPBean#ejbFindAllBySchoolAndClaimStatus
	 */
	public Collection findAllBySchoolAndClaimStatus(School school, String[] statuses) throws FinderException;
}
