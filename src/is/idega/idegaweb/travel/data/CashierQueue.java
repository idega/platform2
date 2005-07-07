/*
 * $Id: CashierQueue.java,v 1.1 2005/07/07 02:59:05 gimmi Exp $
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
import javax.ejb.RemoveException;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2005/07/07 02:59:05 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public interface CashierQueue extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#removeAndDisableBookings
	 */
	public void removeAndDisableBookings() throws RemoveException;

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#store
	 */
	public void store();

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#addBooking
	 */
	public void addBooking(GeneralBooking booking) throws IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#setSupplierManager
	 */
	public void setSupplierManager(Group supplierManager);

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#getSupplierManager
	 */
	public Group getSupplierManager();

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#setCashier
	 */
	public void setCashier(User cashier);

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#getCashier
	 */
	public User getCashier();

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#setOwner
	 */
	public void setOwner(User owner);

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#getOwner
	 */
	public User getOwner();

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#setAuthenticationNumber
	 */
	public void setAuthenticationNumber(String authNumber);

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#setClientName
	 */
	public void setClientName(String name);

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#getClientName
	 */
	public String getClientName();

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#getAuthenticationNumber
	 */
	public String getAuthenticationNumber();

	/**
	 * @see is.idega.idegaweb.travel.data.CashierQueueBMPBean#getBookingIDs
	 */
	public Collection getBookingIDs() throws IDORelationshipException, FinderException;
}
