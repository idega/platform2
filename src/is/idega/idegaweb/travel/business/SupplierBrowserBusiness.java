/*
 * $Id: SupplierBrowserBusiness.java,v 1.1 2005/07/07 02:59:05 gimmi Exp $
 * Created on Jul 6, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.business;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import com.idega.block.basket.business.BasketBusiness;
import com.idega.business.IBOService;
import com.idega.data.IDOAddRelationshipException;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2005/07/07 02:59:05 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public interface SupplierBrowserBusiness extends IBOService {

	/**
	 * @see is.idega.idegaweb.travel.business.SupplierBrowserBusinessBean#sendToCashier
	 */
	public void sendToCashier(Group supplierManager, String clientName, User cashier, User performer, BasketBusiness basketBusiness) throws CreateException, RemoteException, IDOAddRelationshipException;
}
