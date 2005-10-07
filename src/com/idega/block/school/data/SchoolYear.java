/*
 * $Id: SchoolYear.java,v 1.9 2005/10/07 13:23:39 laddi Exp $
 * Created on Oct 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;


/**
 * Last modified: $Date: 2005/10/07 13:23:39 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.9 $
 */
public interface SchoolYear extends IDOEntity {

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#setSchoolYearName
	 */
	public void setSchoolYearName(String name);

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#getSchoolYearName
	 */
	public String getSchoolYearName();

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#setSchoolYearInfo
	 */
	public void setSchoolYearInfo(String info);

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#getSchoolYearInfo
	 */
	public String getSchoolYearInfo();

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#setSchoolYearAge
	 */
	public void setSchoolYearAge(int age);

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#getSchoolYearAge
	 */
	public int getSchoolYearAge();

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#getSchoolTypeId
	 */
	public int getSchoolTypeId();

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#getSchoolType
	 */
	public SchoolType getSchoolType();

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#setSchoolTypeId
	 */
	public void setSchoolTypeId(int id);

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#getSchoolCategoryPK
	 */
	public Object getSchoolCategoryPK();

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#getSchoolCategory
	 */
	public SchoolCategory getSchoolCategory();

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#setSchoolCategory
	 */
	public void setSchoolCategory(Object schoolCategory);

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#isSelectable
	 */
	public boolean isSelectable();

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#setIsSelectable
	 */
	public void setIsSelectable(boolean isSelectable);

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#getLocalizedKey
	 */
	public String getLocalizedKey();

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#setLocalizedKey
	 */
	public void setLocalizedKey(String localizedKey);

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#getPreviousSchoolYearFromAge
	 */
	public SchoolYear getPreviousSchoolYearFromAge() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolYearBMPBean#getSchoolYears
	 */
	public Collection getSchoolYears(SchoolType schoolType) throws FinderException;
}
