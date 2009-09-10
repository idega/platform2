/*
 * $Id: BasketBusinessHomeImpl.java,v 1.2 2005/07/05 22:36:14 gimmi Exp $
 * Created on 27.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.basket.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2005/07/05 22:36:14 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public class BasketBusinessHomeImpl extends IBOHomeImpl implements BasketBusinessHome {

	protected Class getBeanInterfaceClass() {
		return BasketBusiness.class;
	}

	public BasketBusiness create() throws javax.ejb.CreateException {
		return (BasketBusiness) super.createIBO();
	}
}
