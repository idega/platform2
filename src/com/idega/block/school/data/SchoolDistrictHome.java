/*
 * $Id: SchoolDistrictHome.java,v 1.1 2005/08/09 16:32:21 laddi Exp $
 * Created on Aug 1, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/08/09 16:32:21 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolDistrictHome extends IDOHome {

	public SchoolDistrict create() throws javax.ejb.CreateException;

	public SchoolDistrict findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolDistrictBMPBean#ejbFindByStreetAndHouseNumber
	 */
	public SchoolDistrict findByStreetAndHouseNumber(String streetNumber, String houseNumber) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolDistrictBMPBean#ejbFindByAddress
	 */
	public SchoolDistrict findByAddress(String address) throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolDistrictBMPBean#ejbFindAllByDistrict
	 */
	public Collection findAllByDistrict(String district) throws FinderException;
}
