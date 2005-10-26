/*
 * $Id: SchoolSeason.java,v 1.11 2005/10/26 16:03:38 palli Exp $
 * Created on Oct 26, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.sql.Date;

import javax.ejb.FinderException;

import com.idega.data.IDOEntity;


/**
 * 
 *  Last modified: $Date: 2005/10/26 16:03:38 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.11 $
 */
public interface SchoolSeason extends IDOEntity {

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#getSchoolSeasonName
	 */
	public String getSchoolSeasonName();

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#setSchoolSeasonName
	 */
	public void setSchoolSeasonName(String name);

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#getSchoolSeasonEnd
	 */
	public Date getSchoolSeasonEnd();

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#setSchoolSeasonEnd
	 */
	public void setSchoolSeasonEnd(java.util.Date end);

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#getSchoolSeasonStart
	 */
	public Date getSchoolSeasonStart();

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#setSchoolSeasonStart
	 */
	public void setSchoolSeasonStart(java.util.Date start);

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#getChoiceEndDate
	 */
	public Date getChoiceEndDate();

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#setChoiceEndDate
	 */
	public void setChoiceEndDate(java.util.Date due);

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#getChoiceStartDate
	 */
	public Date getChoiceStartDate();

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#setChoiceStartDate
	 */
	public void setChoiceStartDate(java.util.Date due);

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#getSchoolCategory
	 */
	public SchoolCategory getSchoolCategory();

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#getSchoolCategoryPK
	 */
	public String getSchoolCategoryPK();

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#setSchoolCategory
	 */
	public void setSchoolCategory(SchoolCategory category);

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#setSchoolCategory
	 */
	public void setSchoolCategory(Object categoryPK);

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#getPreviousSeason
	 */
	public SchoolSeason getPreviousSeason() throws FinderException;

}
