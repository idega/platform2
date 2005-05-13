/*
 * $Id: CurrencyHome.java,v 1.3 2005/05/13 04:33:53 gimmi Exp $
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
public interface CurrencyHome extends IDOHome {

	public Currency create() throws javax.ejb.CreateException;

	public Currency findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	public Currency findByPrimaryKey(int id) throws javax.ejb.FinderException;

	public Currency findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

	/**
	 * @see com.idega.block.trade.data.CurrencyBMPBean#ejbHomeGetCurrenciesByAbbreviation
	 */
	public Collection getCurrenciesByAbbreviation(String currencyAbbreviation) throws FinderException;

	/**
	 * @see com.idega.block.trade.data.CurrencyBMPBean#ejbHomeGetCurrencyByAbbreviation
	 */
	public Currency getCurrencyByAbbreviation(String currencyAbbreviation) throws FinderException, RemoteException;

	/**
	 * @see com.idega.block.trade.data.CurrencyBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;
}
