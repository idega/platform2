/*
 * $Id: KortathjonustanAuthorisationEntriesHomeImpl.java,v 1.4 2005/06/15 16:37:14 gimmi Exp $
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
 *  Last modified: $Date: 2005/06/15 16:37:14 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.4 $
 */
public class KortathjonustanAuthorisationEntriesHomeImpl extends IDOFactory implements
		KortathjonustanAuthorisationEntriesHome {

	protected Class getEntityInterfaceClass() {
		return KortathjonustanAuthorisationEntries.class;
	}

	public KortathjonustanAuthorisationEntries create() throws javax.ejb.CreateException {
		return (KortathjonustanAuthorisationEntries) super.createIDO();
	}

	public KortathjonustanAuthorisationEntries findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (KortathjonustanAuthorisationEntries) super.findByPrimaryKeyIDO(pk);
	}

	public KortathjonustanAuthorisationEntries findByAuthorizationCode(String code, IWTimestamp stamp)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((KortathjonustanAuthorisationEntriesBMPBean) entity).ejbFindByAuthorizationCode(code, stamp);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByDates(IWTimestamp from, IWTimestamp to) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((KortathjonustanAuthorisationEntriesBMPBean) entity).ejbFindByDates(from, to);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findRefunds(IWTimestamp from, IWTimestamp to) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((KortathjonustanAuthorisationEntriesBMPBean) entity).ejbFindRefunds(from, to);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
