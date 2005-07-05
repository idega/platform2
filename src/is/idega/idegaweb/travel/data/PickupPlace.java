/*
 * $Id: PickupPlace.java 1.1 Jul 5, 2005 gimmi Exp $
 * Created on Jul 5, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.data;

import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.core.location.data.Address;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORemoveRelationshipException;


/**
 * 
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public interface PickupPlace extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#getAddress
	 */
	public Address getAddress();

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#setAddress
	 */
	public void setAddress(Address address);

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#setAddressId
	 */
	public void setAddressId(int addressId);

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#addToSupplier
	 */
	public void addToSupplier(Supplier supplier) throws IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#removeFromSupplier
	 */
	public void removeFromSupplier(Supplier supplier) throws IDORemoveRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#addToService
	 */
	public void addToService(Service service) throws IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#removeFromService
	 */
	public void removeFromService(Service service) throws IDORemoveRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#setAsPickup
	 */
	public void setAsPickup();

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#setAsDropoff
	 */
	public void setAsDropoff();

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#getIsPickup
	 */
	public boolean getIsPickup();

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#getIsDropoff
	 */
	public boolean getIsDropoff();

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#getType
	 */
	public int getType();

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#setType
	 */
	public void setType(int type);
}
