/*
 * $Id: Batch.java,v 1.1 2005/10/02 14:22:00 palli Exp $
 * Created on Oct 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.finance.data;

import java.sql.Timestamp;


import com.idega.data.IDOEntity;


/**
 * 
 *  Last modified: $Date: 2005/10/02 14:22:00 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.1 $
 */
public interface Batch extends IDOEntity {

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#setBatchNumber
	 */
	public void setBatchNumber(String batchNumber);

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#setCreated
	 */
	public void setCreated(Timestamp created);

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#setSent
	 */
	public void setSent(Timestamp sent);

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#getBatchNumber
	 */
	public String getBatchNumber();

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#getCreated
	 */
	public Timestamp getCreated();

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#getSent
	 */
	public Timestamp getSent();

}
