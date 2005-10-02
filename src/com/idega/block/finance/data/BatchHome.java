/*
 * $Id: BatchHome.java,v 1.1 2005/10/02 14:22:00 palli Exp $
 * Created on Oct 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;


/**
 * 
 *  Last modified: $Date: 2005/10/02 14:22:00 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.1 $
 */
public interface BatchHome extends IDOHome {

	public Batch create() throws javax.ejb.CreateException;

	public Batch findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#ejbFindUnsent
	 */
	public Batch findUnsent() throws FinderException;

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#ejbFindAllNewestFirst
	 */
	public Collection findAllNewestFirst() throws FinderException;

}
