/*
 * $Id: ManagementTypeHomeImpl.java,v 1.1 2005/09/07 11:39:46 palli Exp $
 * Created on Sep 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.regulations.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;


/**
 * 
 *  Last modified: $Date: 2005/09/07 11:39:46 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.1 $
 */
public class ManagementTypeHomeImpl extends IDOFactory implements ManagementTypeHome {

	protected Class getEntityInterfaceClass() {
		return ManagementType.class;
	}

	public ManagementType create() throws javax.ejb.CreateException {
		return (ManagementType) super.createIDO();
	}

	public ManagementType findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (ManagementType) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ManagementTypeBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
