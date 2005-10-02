/*
 * $Id: MealChoiceMonthHome.java,v 1.2 2005/10/02 13:44:24 laddi Exp $
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
import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/10/02 13:44:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface MealChoiceMonthHome extends IDOHome {

	public MealChoiceMonth create() throws javax.ejb.CreateException;

	public MealChoiceMonth findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceMonthBMPBean#ejbFindAllByChoice
	 */
	public Collection findAllByChoice(MealChoice choice) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceMonthBMPBean#ejbFindByChoice
	 */
	public MealChoiceMonth findByChoice(MealChoice choice, int month, int year) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.data.MealChoiceMonthBMPBean#ejbHomeGetNumberOfChoicesForUser
	 */
	public int getNumberOfChoicesForUser(User user, School school, SchoolSeason season, int month, int year)
			throws IDOException;
}
