/*
 * $Id: VacationTypeHomeImpl.java,v 1.3 2005/01/11 09:29:15 laddi Exp $
 * Created on 11.1.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.vacation.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;


/**
 * Last modified: $Date: 2005/01/11 09:29:15 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class VacationTypeHomeImpl extends IDOFactory implements VacationTypeHome {

	protected Class getEntityInterfaceClass() {
		return VacationType.class;
	}

	public VacationType create() throws javax.ejb.CreateException {
		return (VacationType) super.createIDO();
	}

	public VacationType findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (VacationType) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((VacationTypeBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public VacationType findByName(String name) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((VacationTypeBMPBean) entity).ejbFindByName(name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
