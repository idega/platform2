/*
 * $Id: ProductBusinessHomeImpl.java,v 1.4 2005/08/24 13:04:24 gimmi Exp $
 * Created on Aug 19, 2005
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
 *  Last modified: $Date: 2005/08/24 13:04:24 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.4 $
 */
public class ProductBusinessHomeImpl extends IBOHomeImpl implements ProductBusinessHome {

	protected Class getBeanInterfaceClass() {
		return ProductBusiness.class;
	}

	public ProductBusiness create() throws javax.ejb.CreateException {
		return (ProductBusiness) super.createIBO();
	}
}
