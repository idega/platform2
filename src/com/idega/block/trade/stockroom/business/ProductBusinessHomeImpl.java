/*
 * $Id: ProductBusinessHomeImpl.java,v 1.5 2005/08/30 02:01:14 gimmi Exp $
 * Created on Aug 29, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2005/08/30 02:01:14 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.5 $
 */
public class ProductBusinessHomeImpl extends IBOHomeImpl implements ProductBusinessHome {

	protected Class getBeanInterfaceClass() {
		return ProductBusiness.class;
	}

	public ProductBusiness create() throws javax.ejb.CreateException {
		return (ProductBusiness) super.createIBO();
	}
}
