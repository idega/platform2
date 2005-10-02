/*
 * $Id: BatchHomeImpl.java,v 1.1 2005/10/02 14:22:00 palli Exp $
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

import com.idega.data.IDOFactory;


/**
 * 
 *  Last modified: $Date: 2005/10/02 14:22:00 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.1 $
 */
public class BatchHomeImpl extends IDOFactory implements BatchHome {

	protected Class getEntityInterfaceClass() {
		return Batch.class;
	}

	public Batch create() throws javax.ejb.CreateException {
		return (Batch) super.createIDO();
	}

	public Batch findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Batch) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((BatchBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Batch findUnsent() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((BatchBMPBean) entity).ejbFindUnsent();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllNewestFirst() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((BatchBMPBean) entity).ejbFindAllNewestFirst();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
