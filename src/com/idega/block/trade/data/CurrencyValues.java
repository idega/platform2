/*
 * $Id: CurrencyValues.java,v 1.4 2005/05/13 04:33:53 gimmi Exp $
 * Created on 13.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.data;

import java.sql.Timestamp;
import com.idega.data.IDOLegacyEntity;


/**
 * 
 *  Last modified: $Date: 2005/05/13 04:33:53 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.4 $
 */
public interface CurrencyValues extends IDOLegacyEntity {

	/**
	 * @see com.idega.block.trade.data.CurrencyValuesBMPBean#getBuyValue
	 */
	public float getBuyValue();

	/**
	 * @see com.idega.block.trade.data.CurrencyValuesBMPBean#getSellValue
	 */
	public float getSellValue();

	/**
	 * @see com.idega.block.trade.data.CurrencyValuesBMPBean#getMiddleValue
	 */
	public float getMiddleValue();

	/**
	 * @see com.idega.block.trade.data.CurrencyValuesBMPBean#getCurrencyDate
	 */
	public Timestamp getCurrencyDate();

	/**
	 * @see com.idega.block.trade.data.CurrencyValuesBMPBean#setBuyValue
	 */
	public void setBuyValue(float buyValue);

	/**
	 * @see com.idega.block.trade.data.CurrencyValuesBMPBean#setSellValue
	 */
	public void setSellValue(float sellValue);

	/**
	 * @see com.idega.block.trade.data.CurrencyValuesBMPBean#setMiddleValue
	 */
	public void setMiddleValue(float middleValue);

	/**
	 * @see com.idega.block.trade.data.CurrencyValuesBMPBean#setCurrencyDate
	 */
	public void setCurrencyDate(Timestamp currencyDate);
}
