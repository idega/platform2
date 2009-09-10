/*
 * $Id: ProductPriceBusinessHome.java,v 1.1 2005/08/11 14:02:46 gimmi Exp $
 * Created on Aug 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2005/08/11 14:02:46 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public interface ProductPriceBusinessHome extends IBOHome {

	public ProductPriceBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
