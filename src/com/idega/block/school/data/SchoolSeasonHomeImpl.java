/*
 * $Id: SchoolSeasonHomeImpl.java,v 1.8 2005/03/19 16:38:22 laddi Exp $
 * Created on 19.3.2005
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
import com.idega.data.IDOFactory;


/**
 * <p>
 * TODO laddi Describe Type SchoolSeasonHomeImpl
 * </p>
 *  Last modified: $Date: 2005/03/19 16:38:22 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.8 $
 */
public class SchoolSeasonHomeImpl extends IDOFactory implements SchoolSeasonHome {

	protected Class getEntityInterfaceClass() {
		return SchoolSeason.class;
	}

	public SchoolSeason create() throws javax.ejb.CreateException {
		return (SchoolSeason) super.createIDO();
	}

	public SchoolSeason findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (SchoolSeason) super.findByPrimaryKeyIDO(pk);
	}

	public java.util.Collection findAllSchoolSeasons() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolSeasonBMPBean) entity).ejbFindAllSchoolSeasons();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public java.util.Collection findAllPreviousSchoolSeasons(SchoolSeason schoolSeason) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolSeasonBMPBean) entity).ejbFindAllPreviousSchoolSeasons(schoolSeason);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolSeason findPreviousSchoolSeason(SchoolSeason schoolSeason) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolSeasonBMPBean) entity).ejbFindPreviousSchoolSeason(schoolSeason);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolSeason findSeasonByDate(Date date) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolSeasonBMPBean) entity).ejbFindSeasonByDate(date);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolSeason findCurrentSeason() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolSeasonBMPBean) entity).ejbFindCurrentSeason();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findSchoolSeasonsActiveInTimePeriod(Date firstDateInPeriod, Date lastDateInPeriod)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolSeasonBMPBean) entity).ejbFindSchoolSeasonsActiveInTimePeriod(firstDateInPeriod,
				lastDateInPeriod);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolSeason findNextSeason(SchoolSeason currentSeason) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolSeasonBMPBean) entity).ejbFindNextSeason(currentSeason);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}
