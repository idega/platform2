/*
 * $Id: MealVacationDayHomeImpl.java,v 1.1 2005/08/10 23:03:11 laddi Exp $
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
import com.idega.data.IDOFactory;


/**
 * Last modified: $Date: 2005/08/10 23:03:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class MealVacationDayHomeImpl extends IDOFactory implements MealVacationDayHome {

	protected Class getEntityInterfaceClass() {
		return MealVacationDay.class;
	}

	public MealVacationDay create() throws javax.ejb.CreateException {
		return (MealVacationDay) super.createIDO();
	}

	public MealVacationDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (MealVacationDay) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllBySchool(School school) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MealVacationDayBMPBean) entity).ejbFindAllBySchool(school);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySchoolAndPeriod(School school, Date from, Date to) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MealVacationDayBMPBean) entity).ejbFindAllBySchoolAndPeriod(school, from, to);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public MealVacationDay findBySchoolAndDate(School school, Date date) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((MealVacationDayBMPBean) entity).ejbFindBySchoolAndDate(school, date);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}
