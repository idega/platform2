/*
 * $Id: AdultEducationChoiceHomeImpl.java,v 1.6 2005/05/25 13:06:37 laddi Exp $
 * Created on May 25, 2005
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
import com.idega.data.IDOFactory;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/05/25 13:06:37 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.6 $
 */
public class AdultEducationChoiceHomeImpl extends IDOFactory implements AdultEducationChoiceHome {

	protected Class getEntityInterfaceClass() {
		return AdultEducationChoice.class;
	}

	public AdultEducationChoice create() throws javax.ejb.CreateException {
		return (AdultEducationChoice) super.createIDO();
	}

	public AdultEducationChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (AdultEducationChoice) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllBySeasonAndStatuses(SchoolSeason season, String[] statuses) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationChoiceBMPBean) entity).ejbFindAllBySeasonAndStatuses(season, statuses);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySeasonAndStatuses(SchoolSeason season, String[] statuses, int choiceOrder)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationChoiceBMPBean) entity).ejbFindAllBySeasonAndStatuses(season, statuses,
				choiceOrder);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByUserAndSeason(User user, SchoolSeason season) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationChoiceBMPBean) entity).ejbFindAllByUserAndSeason(user, season);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByUserAndSeason(User user, SchoolSeason season, int choiceOrder) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationChoiceBMPBean) entity).ejbFindAllByUserAndSeason(user, season,
				choiceOrder);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByUserAndSeasonAndStatuses(User user, SchoolSeason season, String[] statuses)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationChoiceBMPBean) entity).ejbFindAllByUserAndSeasonAndStatuses(user,
				season, statuses);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public AdultEducationChoice findByUserAndCourse(Object userPK, Object coursePK) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((AdultEducationChoiceBMPBean) entity).ejbFindByUserAndCourse(userPK, coursePK);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllByUserAndSeasonAndStudyPath(Object userPK, Object seasonPK, Object studyPathPK,
			String[] statuses) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AdultEducationChoiceBMPBean) entity).ejbFindAllByUserAndSeasonAndStudyPath(userPK,
				seasonPK, studyPathPK, statuses);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public AdultEducationChoice findByUserAndSeasonAndStudyPathAndChoiceOrder(Object userPK, Object seasonPK,
			Object studyPathPK, int choiceOrder, String[] statuses) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((AdultEducationChoiceBMPBean) entity).ejbFindByUserAndSeasonAndStudyPathAndChoiceOrder(userPK,
				seasonPK, studyPathPK, choiceOrder, statuses);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}