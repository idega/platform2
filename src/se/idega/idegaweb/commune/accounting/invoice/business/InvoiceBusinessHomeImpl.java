/*
 * $Id: InvoiceBusinessHomeImpl.java,v 1.2 2004/10/20 17:05:09 thomas Exp $
 * Created on Oct 20, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.invoice.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2004/10/20 17:05:09 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.2 $
 */
public class InvoiceBusinessHomeImpl extends IBOHomeImpl implements InvoiceBusinessHome {

	protected Class getBeanInterfaceClass() {
		return InvoiceBusiness.class;
	}

	public InvoiceBusiness create() throws javax.ejb.CreateException {
		return (InvoiceBusiness) super.createIBO();
	}
}
