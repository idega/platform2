/*
 * $Id: CurrencyHomeImpl.java,v 1.3 2005/05/13 04:33:53 gimmi Exp $
 * Created on 13.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.data;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.*;


/**
 * 
 *  Last modified: $Date: 2005/05/13 04:33:53 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.3 $
 */
public class CurrencyHomeImpl extends IDOFactory implements CurrencyHome {

	protected Class getEntityInterfaceClass() {
		return Currency.class;
	}

	public Currency create() throws javax.ejb.CreateException {
		return (Currency) super.createIDO();
	}

	public Currency findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Currency) super.findByPrimaryKeyIDO(pk);
	}

	public Currency createLegacy() {
		try {
			return create();
		}
		catch (javax.ejb.CreateException ce) {
			throw new RuntimeException("CreateException:" + ce.getMessage());
		}
	}

	public Currency findByPrimaryKey(int id) throws javax.ejb.FinderException {
		return (Currency) super.findByPrimaryKeyIDO(id);
	}

	public Currency findByPrimaryKeyLegacy(int id) throws java.sql.SQLException {
		try {
			return findByPrimaryKey(id);
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.sql.SQLException("FinderException:" + fe.getMessage());
		}
	}

	public Collection getCurrenciesByAbbreviation(String currencyAbbreviation) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection theReturn = ((CurrencyBMPBean) entity).ejbHomeGetCurrenciesByAbbreviation(currencyAbbreviation);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Currency getCurrencyByAbbreviation(String currencyAbbreviation) throws FinderException, RemoteException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Currency theReturn = ((CurrencyBMPBean) entity).ejbHomeGetCurrencyByAbbreviation(currencyAbbreviation);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CurrencyBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
