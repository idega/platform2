/*
 * $Id: CurrencyValuesHome.java,v 1.2 2005/05/13 04:33:53 gimmi Exp $
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
import com.idega.data.IDOHome;


/**
 * 
 *  Last modified: $Date: 2005/05/13 04:33:53 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public interface CurrencyValuesHome extends IDOHome {

	public CurrencyValues create() throws javax.ejb.CreateException;

	public CurrencyValues findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	public CurrencyValues findByPrimaryKey(int id) throws javax.ejb.FinderException;

	public CurrencyValues findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

	/**
	 * @see com.idega.block.trade.data.CurrencyValuesBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;
}
