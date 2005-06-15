/*
 * $Id: ServiceSearchSession.java,v 1.2 2005/06/15 16:38:33 gimmi Exp $
 * Created on 15.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.block.search.business;

import java.util.Collection;
import com.idega.business.IBOSession;


/**
 * 
 *  Last modified: $Date: 2005/06/15 16:38:33 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public interface ServiceSearchSession extends IBOSession {

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchSessionBean#getProducts
	 */
	public Collection getProducts() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchSessionBean#setProducts
	 */
	public void setProducts(Collection coll) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchSessionBean#setState
	 */
	public void setState(int state) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchSessionBean#getState
	 */
	public int getState() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchSessionBean#setBookingsSavedFromBasket
	 */
	public void setBookingsSavedFromBasket(Collection bookings) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchSessionBean#getBookingsSavedFromBasket
	 */
	public Collection getBookingsSavedFromBasket() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchSessionBean#setException
	 */
	public void setException(Exception e) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchSessionBean#throwException
	 */
	public void throwException() throws Exception, java.rmi.RemoteException;
}
