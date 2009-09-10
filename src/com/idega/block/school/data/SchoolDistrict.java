/*
 * $Id: SchoolDistrict.java,v 1.1 2005/08/09 16:32:21 laddi Exp $
 * Created on Aug 1, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import com.idega.data.IDOEntity;


/**
 * Last modified: $Date: 2005/08/09 16:32:21 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolDistrict extends IDOEntity {

	/**
	 * @see com.idega.block.school.data.SchoolDistrictBMPBean#getSchool
	 */
	public School getSchool();

	/**
	 * @see com.idega.block.school.data.SchoolDistrictBMPBean#getSchoolPK
	 */
	public Object getSchoolPK();

	/**
	 * @see com.idega.block.school.data.SchoolDistrictBMPBean#getDistrict
	 */
	public String getDistrict();

	/**
	 * @see com.idega.block.school.data.SchoolDistrictBMPBean#setSchool
	 */
	public void setSchool(School school);

	/**
	 * @see com.idega.block.school.data.SchoolDistrictBMPBean#setSchool
	 */
	public void setSchool(Object schoolPK);

	/**
	 * @see com.idega.block.school.data.SchoolDistrictBMPBean#setAddress
	 */
	public void setAddress(String address);

	/**
	 * @see com.idega.block.school.data.SchoolDistrictBMPBean#setStreetNumber
	 */
	public void setStreetNumber(String streetNumber);

	/**
	 * @see com.idega.block.school.data.SchoolDistrictBMPBean#setHouseNumber
	 */
	public void setHouseNumber(String houseNumber);

	/**
	 * @see com.idega.block.school.data.SchoolDistrictBMPBean#setDistrict
	 */
	public void setDistrict(String district);
}
