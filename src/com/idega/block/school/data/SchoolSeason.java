/*
 * $Id: SchoolSeason.java,v 1.7 2005/03/19 16:38:22 laddi Exp $
 * Created on 19.3.2005
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
 * <p>
 * TODO laddi Describe Type SchoolSeason
 * </p>
 *  Last modified: $Date: 2005/03/19 16:38:22 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
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
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#getSchoolSeasonDueDate
	 */
	public Date getSchoolSeasonDueDate();

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#setSchoolSeasonDueDate
	 */
	public void setSchoolSeasonDueDate(java.util.Date due);

	/**
	 * @see com.idega.block.school.data.SchoolSeasonBMPBean#getPreviousSeason
	 */
	public SchoolSeason getPreviousSeason() throws FinderException;
}
