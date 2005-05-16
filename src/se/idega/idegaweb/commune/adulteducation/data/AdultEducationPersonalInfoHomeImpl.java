/*
 * $Id: AdultEducationPersonalInfoHomeImpl.java,v 1.4 2005/05/16 08:57:06 laddi Exp $
 * Created on May 14, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.data;

import com.idega.data.IDOFactory;


/**
 * Last modified: $Date: 2005/05/16 08:57:06 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public class AdultEducationPersonalInfoHomeImpl extends IDOFactory implements AdultEducationPersonalInfoHome {

	protected Class getEntityInterfaceClass() {
		return AdultEducationPersonalInfo.class;
	}

	public AdultEducationPersonalInfo create() throws javax.ejb.CreateException {
		return (AdultEducationPersonalInfo) super.createIDO();
	}

	public AdultEducationPersonalInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (AdultEducationPersonalInfo) super.findByPrimaryKeyIDO(pk);
	}

	public AdultEducationPersonalInfo findByUserId(Integer userId) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((AdultEducationPersonalInfoBMPBean) entity).ejbFindByUserId(userId);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}
