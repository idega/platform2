/*
 * $Id: Currency.java,v 1.9 2005/05/13 04:33:53 gimmi Exp $
 * Created on 13.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.data;

import com.idega.data.*;


/**
 * 
 *  Last modified: $Date: 2005/05/13 04:33:53 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.9 $
 */
public interface Currency extends IDOLegacyEntity {

	/**
	 * @see com.idega.block.trade.data.CurrencyBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.trade.data.CurrencyBMPBean#getCurrencyName
	 */
	public String getCurrencyName();

	/**
	 * @see com.idega.block.trade.data.CurrencyBMPBean#getCurrencyAbbreviation
	 */
	public String getCurrencyAbbreviation();

	/**
	 * @see com.idega.block.trade.data.CurrencyBMPBean#setCurrencyName
	 */
	public void setCurrencyName(String name);

	/**
	 * @see com.idega.block.trade.data.CurrencyBMPBean#setCurrencyAbbreviation
	 */
	public void setCurrencyAbbreviation(String abbreviation);
}
