/*
 * $Id: CashierQueueHome.java,v 1.1 2005/07/07 02:59:05 gimmi Exp $
 * Created on Jul 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2005/07/07 02:59:05 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public interface CashierQueueHome extends IDOHome {

	public CashierQueue create() throws javax.ejb.CreateException;

	public CashierQueue findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#ejbFindAllBySupplierManager
	 */
	public Collection findAllBySupplierManager(Group supplierManager) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#ejbFindAllByCashier
	 */
	public Collection findAllByCashier(User cashier) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#ejbFindAllByOwner
	 */
	public Collection findAllByOwner(User owner) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#ejbFindAll
	 */
	public Collection findAll(Group supplierManager, User cashier, User owner) throws FinderException;
}
