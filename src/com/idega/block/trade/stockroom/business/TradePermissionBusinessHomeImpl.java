/*
 * $Id: TradePermissionBusinessHomeImpl.java,v 1.1 2005/05/31 19:21:33 gimmi Exp $
 * Created on 10.2.2005
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
 *  Last modified: $Date: 2005/05/31 19:21:33 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public class TradePermissionBusinessHomeImpl extends IBOHomeImpl implements TradePermissionBusinessHome {

	protected Class getBeanInterfaceClass() {
		return TradePermissionBusiness.class;
	}

	public TradePermissionBusiness create() throws javax.ejb.CreateException {
		return (TradePermissionBusiness) super.createIBO();
	}
}
