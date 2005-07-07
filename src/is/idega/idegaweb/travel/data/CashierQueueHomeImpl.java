/*
 * $Id: CashierQueueHomeImpl.java,v 1.1 2005/07/07 02:59:05 gimmi Exp $
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
import com.idega.data.IDOFactory;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2005/07/07 02:59:05 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public class CashierQueueHomeImpl extends IDOFactory implements CashierQueueHome {

	protected Class getEntityInterfaceClass() {
		return CashierQueue.class;
	}

	public CashierQueue create() throws javax.ejb.CreateException {
		return (CashierQueue) super.createIDO();
	}

	public CashierQueue findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (CashierQueue) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllBySupplierManager(Group supplierManager) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CashierQueueBMPBean) entity).ejbFindAllBySupplierManager(supplierManager);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByCashier(User cashier) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CashierQueueBMPBean) entity).ejbFindAllByCashier(cashier);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByOwner(User owner) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CashierQueueBMPBean) entity).ejbFindAllByOwner(owner);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll(Group supplierManager, User cashier, User owner) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CashierQueueBMPBean) entity).ejbFindAll(supplierManager, cashier, owner);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
