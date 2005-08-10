/*
 * $Id: MealPriceHomeImpl.java,v 1.1 2005/08/10 23:03:11 laddi Exp $
 * Created on Aug 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.data;

import java.sql.Date;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.school.data.School;
import com.idega.data.IDOException;
import com.idega.data.IDOFactory;


/**
 * Last modified: $Date: 2005/08/10 23:03:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class MealPriceHomeImpl extends IDOFactory implements MealPriceHome {

	protected Class getEntityInterfaceClass() {
		return MealPrice.class;
	}

	public MealPrice create() throws javax.ejb.CreateException {
		return (MealPrice) super.createIDO();
	}

	public MealPrice findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (MealPrice) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllBySchool(School school) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MealPriceBMPBean) entity).ejbFindAllBySchool(school);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public MealPrice findBySchoolAndDate(School school, Date date) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((MealPriceBMPBean) entity).ejbFindBySchoolAndDate(school, date);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public int getCountBySchoolAndDate(School school, Date date) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((MealPriceBMPBean) entity).ejbHomeGetCountBySchoolAndDate(school, date);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}
}
