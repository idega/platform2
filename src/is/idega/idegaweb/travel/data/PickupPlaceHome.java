/*
 * $Id: PickupPlaceHome.java 1.1 Jul 5, 2005 gimmi Exp $
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
import com.idega.data.IDOHome;


/**
 * 
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public interface PickupPlaceHome extends IDOHome {

	public PickupPlace create() throws javax.ejb.CreateException;

	public PickupPlace findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#ejbFindDropoffPlaces
	 */
	public Collection findDropoffPlaces(Service service) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#ejbFindHotelPickupPlaces
	 */
	public Collection findHotelPickupPlaces(Service service) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#ejbFindDropoffPlaces
	 */
	public Collection findDropoffPlaces(Product product) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#ejbFindHotelPickupPlaces
	 */
	public Collection findHotelPickupPlaces(Product product) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#ejbFindHotelPickupPlaces
	 */
	public Collection findHotelPickupPlaces(Service service, int PLACE_TYPE) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#ejbFindHotelPickupPlaces
	 */
	public Collection findHotelPickupPlaces(Product product, int PLACE_TYPE) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#ejbFindHotelPickupPlaces
	 */
	public Collection findHotelPickupPlaces(String serviceID, int PLACE_TYPE) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#ejbFindDropoffPlaces
	 */
	public Collection findDropoffPlaces(Supplier supplier) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#ejbFindHotelPickupPlaces
	 */
	public Collection findHotelPickupPlaces(Supplier supplier) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#ejbFindAllPlaces
	 */
	public Collection findAllPlaces(int PLACE_TYPE) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.data.PickupPlaceBMPBean#ejbFindHotelPickupPlaces
	 */
	public Collection findHotelPickupPlaces(Supplier supplier, int PLACE_TYPE) throws FinderException;
}
