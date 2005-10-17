/*
 * $Id: ProviderAccountingPropertiesHomeImpl.java,v 1.2 2005/10/17 09:54:16 palli Exp $
 * Created on Oct 17, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;


/**
 * 
 *  Last modified: $Date: 2005/10/17 09:54:16 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.2 $
 */
public class ProviderAccountingPropertiesHomeImpl extends IDOFactory implements ProviderAccountingPropertiesHome {

	protected Class getEntityInterfaceClass() {
		return ProviderAccountingProperties.class;
	}

	public ProviderAccountingProperties create() throws javax.ejb.CreateException {
		return (ProviderAccountingProperties) super.createIDO();
	}

	public ProviderAccountingProperties findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (ProviderAccountingProperties) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByPaymentByInvoice(boolean hasPaymentByInvoice) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ProviderAccountingPropertiesBMPBean) entity).ejbFindAllByPaymentByInvoice(hasPaymentByInvoice);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
