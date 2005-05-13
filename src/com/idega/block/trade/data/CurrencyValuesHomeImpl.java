/*
 * $Id: CurrencyValuesHomeImpl.java,v 1.2 2005/05/13 04:33:53 gimmi Exp $
 * Created on 13.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * 
 *  Last modified: $Date: 2005/05/13 04:33:53 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public class CurrencyValuesHomeImpl extends IDOFactory implements CurrencyValuesHome {

	protected Class getEntityInterfaceClass() {
		return CurrencyValues.class;
	}

	public CurrencyValues create() throws javax.ejb.CreateException {
		return (CurrencyValues) super.createIDO();
	}

	public CurrencyValues findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (CurrencyValues) super.findByPrimaryKeyIDO(pk);
	}

	public CurrencyValues createLegacy() {
		try {
			return create();
		}
		catch (javax.ejb.CreateException ce) {
			throw new RuntimeException("CreateException:" + ce.getMessage());
		}
	}

	public CurrencyValues findByPrimaryKey(int id) throws javax.ejb.FinderException {
		return (CurrencyValues) super.findByPrimaryKeyIDO(id);
	}

	public CurrencyValues findByPrimaryKeyLegacy(int id) throws java.sql.SQLException {
		try {
			return findByPrimaryKey(id);
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.sql.SQLException("FinderException:" + fe.getMessage());
		}
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CurrencyValuesBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
