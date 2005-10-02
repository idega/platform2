/*
 * $Id: MealChoiceMonthHomeImpl.java,v 1.2 2005/10/02 13:44:24 laddi Exp $
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
import com.idega.data.IDOFactory;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/10/02 13:44:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class MealChoiceMonthHomeImpl extends IDOFactory implements MealChoiceMonthHome {

	protected Class getEntityInterfaceClass() {
		return MealChoiceMonth.class;
	}

	public MealChoiceMonth create() throws javax.ejb.CreateException {
		return (MealChoiceMonth) super.createIDO();
	}

	public MealChoiceMonth findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (MealChoiceMonth) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByChoice(MealChoice choice) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MealChoiceMonthBMPBean) entity).ejbFindAllByChoice(choice);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public MealChoiceMonth findByChoice(MealChoice choice, int month, int year) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((MealChoiceMonthBMPBean) entity).ejbFindByChoice(choice, month, year);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public int getNumberOfChoicesForUser(User user, School school, SchoolSeason season, int month, int year)
			throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((MealChoiceMonthBMPBean) entity).ejbHomeGetNumberOfChoicesForUser(user, school, season, month,
				year);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}
}
