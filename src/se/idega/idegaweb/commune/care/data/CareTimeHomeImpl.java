/*
 * $Id: CareTimeHomeImpl.java,v 1.2 2004/12/05 09:05:33 laddi Exp $
 * Created on 1.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;


/**
 * Last modified: $Date: 2004/12/05 09:05:33 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class CareTimeHomeImpl extends IDOFactory implements CareTimeHome {

	protected Class getEntityInterfaceClass() {
		return CareTime.class;
	}

	public CareTime create() throws javax.ejb.CreateException {
		return (CareTime) super.createIDO();
	}

	public CareTime findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (CareTime) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CareTimeBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
