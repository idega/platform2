/*
 * $Id: PickupPlaceHomeImpl.java 1.1 Jul 5, 2005 gimmi Exp $
 * Created on Jul 5, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.data;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOFactory;


/**
 * 
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public class PickupPlaceHomeImpl extends IDOFactory implements PickupPlaceHome {

	protected Class getEntityInterfaceClass() {
		return PickupPlace.class;
	}

	public PickupPlace create() throws javax.ejb.CreateException {
		return (PickupPlace) super.createIDO();
	}

	public PickupPlace findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (PickupPlace) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findDropoffPlaces(Service service) throws RemoteException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PickupPlaceBMPBean) entity).ejbFindDropoffPlaces(service);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findHotelPickupPlaces(Service service) throws RemoteException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PickupPlaceBMPBean) entity).ejbFindHotelPickupPlaces(service);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findDropoffPlaces(Product product) throws RemoteException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PickupPlaceBMPBean) entity).ejbFindDropoffPlaces(product);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findHotelPickupPlaces(Product product) throws RemoteException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PickupPlaceBMPBean) entity).ejbFindHotelPickupPlaces(product);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findHotelPickupPlaces(Service service, int PLACE_TYPE) throws RemoteException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PickupPlaceBMPBean) entity).ejbFindHotelPickupPlaces(service, PLACE_TYPE);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findHotelPickupPlaces(Product product, int PLACE_TYPE) throws RemoteException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PickupPlaceBMPBean) entity).ejbFindHotelPickupPlaces(product, PLACE_TYPE);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findHotelPickupPlaces(String serviceID, int PLACE_TYPE) throws RemoteException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PickupPlaceBMPBean) entity).ejbFindHotelPickupPlaces(serviceID, PLACE_TYPE);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findDropoffPlaces(Supplier supplier) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PickupPlaceBMPBean) entity).ejbFindDropoffPlaces(supplier);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findHotelPickupPlaces(Supplier supplier) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PickupPlaceBMPBean) entity).ejbFindHotelPickupPlaces(supplier);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllPlaces(int PLACE_TYPE) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PickupPlaceBMPBean) entity).ejbFindAllPlaces(PLACE_TYPE);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findHotelPickupPlaces(Supplier supplier, int PLACE_TYPE) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PickupPlaceBMPBean) entity).ejbFindHotelPickupPlaces(supplier, PLACE_TYPE);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
