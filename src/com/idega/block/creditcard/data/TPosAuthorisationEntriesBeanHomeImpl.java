/*
 * $Id: TPosAuthorisationEntriesBeanHomeImpl.java,v 1.4 2005/06/15 16:36:24 gimmi Exp $
 * Created on 8.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.creditcard.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;
import com.idega.util.IWTimestamp;


/**
 * 
 *  Last modified: $Date: 2005/06/15 16:36:24 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.4 $
 */
public class TPosAuthorisationEntriesBeanHomeImpl extends IDOFactory implements TPosAuthorisationEntriesBeanHome {

	protected Class getEntityInterfaceClass() {
		return TPosAuthorisationEntriesBean.class;
	}

	public TPosAuthorisationEntriesBean create() throws javax.ejb.CreateException {
		return (TPosAuthorisationEntriesBean) super.createIDO();
	}

	public TPosAuthorisationEntriesBean findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (TPosAuthorisationEntriesBean) super.findByPrimaryKeyIDO(pk);
	}

	public TPosAuthorisationEntriesBean findByAuthorisationIdRsp(String authIdRsp, IWTimestamp stamp)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((TPosAuthorisationEntriesBeanBMPBean) entity).ejbFindByAuthorisationIdRsp(authIdRsp, stamp);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findRefunds(IWTimestamp from, IWTimestamp to) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((TPosAuthorisationEntriesBeanBMPBean) entity).ejbFindRefunds(from, to);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByDates(IWTimestamp from, IWTimestamp to) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((TPosAuthorisationEntriesBeanBMPBean) entity).ejbFindByDates(from, to);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
