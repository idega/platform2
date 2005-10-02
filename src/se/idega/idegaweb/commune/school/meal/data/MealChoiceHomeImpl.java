/*
 * $Id: MealChoiceHomeImpl.java,v 1.2 2005/10/02 22:12:23 laddi Exp $
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
import com.idega.data.IDOFactory;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/10/02 22:12:23 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class MealChoiceHomeImpl extends IDOFactory implements MealChoiceHome {

	protected Class getEntityInterfaceClass() {
		return MealChoice.class;
	}

	public MealChoice create() throws javax.ejb.CreateException {
		return (MealChoice) super.createIDO();
	}

	public MealChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (MealChoice) super.findByPrimaryKeyIDO(pk);
	}

	public MealChoice findByUserAndSeason(User user, School school, SchoolSeason season) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((MealChoiceBMPBean) entity).ejbFindByUserAndSeason(user, school, season);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllBySchoolAndSeason(School school, SchoolSeason season) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MealChoiceBMPBean) entity).ejbFindAllBySchoolAndSeason(school, season);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySchoolAndClaimStatus(School school, String[] statuses) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MealChoiceBMPBean) entity).ejbFindAllBySchoolAndClaimStatus(school, statuses);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
