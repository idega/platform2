/*
 * $Id: SupplierManagerBusinessHome.java,v 1.2 2005/02/08 15:54:36 gimmi Exp $
 * Created on 8.2.2005
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
 *  Last modified: $Date: 2005/02/08 15:54:36 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public interface SupplierManagerBusinessHome extends IBOHome {

	public SupplierManagerBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
