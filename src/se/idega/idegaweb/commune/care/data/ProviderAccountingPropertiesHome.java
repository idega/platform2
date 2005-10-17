/*
 * $Id: ProviderAccountingPropertiesHome.java,v 1.2 2005/10/17 09:54:16 palli Exp $
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

import com.idega.data.IDOHome;


/**
 * 
 *  Last modified: $Date: 2005/10/17 09:54:16 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.2 $
 */
public interface ProviderAccountingPropertiesHome extends IDOHome {

	public ProviderAccountingProperties create() throws javax.ejb.CreateException;

	public ProviderAccountingProperties findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.ProviderAccountingPropertiesBMPBean#ejbFindAllByPaymentByInvoice
	 */
	public Collection findAllByPaymentByInvoice(boolean hasPaymentByInvoice) throws FinderException;

}
