/*
 * $Id: SupplierBrowserBusinessHomeImpl.java,v 1.1 2005/07/07 02:59:05 gimmi Exp $
 * Created on Jul 6, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2005/07/07 02:59:05 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public class SupplierBrowserBusinessHomeImpl extends IBOHomeImpl implements SupplierBrowserBusinessHome {

	protected Class getBeanInterfaceClass() {
		return SupplierBrowserBusiness.class;
	}

	public SupplierBrowserBusiness create() throws javax.ejb.CreateException {
		return (SupplierBrowserBusiness) super.createIBO();
	}
}
