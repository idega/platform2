/*
 * $Id: SchoolDistrictHomeImpl.java,v 1.1 2005/08/09 16:32:21 laddi Exp $
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
import com.idega.data.IDOFactory;


/**
 * Last modified: $Date: 2005/08/09 16:32:21 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class SchoolDistrictHomeImpl extends IDOFactory implements SchoolDistrictHome {

	protected Class getEntityInterfaceClass() {
		return SchoolDistrict.class;
	}

	public SchoolDistrict create() throws javax.ejb.CreateException {
		return (SchoolDistrict) super.createIDO();
	}

	public SchoolDistrict findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (SchoolDistrict) super.findByPrimaryKeyIDO(pk);
	}

	public SchoolDistrict findByStreetAndHouseNumber(String streetNumber, String houseNumber) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolDistrictBMPBean) entity).ejbFindByStreetAndHouseNumber(streetNumber, houseNumber);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolDistrict findByAddress(String address) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolDistrictBMPBean) entity).ejbFindByAddress(address);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllByDistrict(String district) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolDistrictBMPBean) entity).ejbFindAllByDistrict(district);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
