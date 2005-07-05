/*
 * $Id: BasketBusiness.java,v 1.7 2005/07/05 22:36:14 gimmi Exp $
 * Created on 27.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.basket.business;

import java.util.List;
import java.util.Map;
import com.idega.block.basket.data.BasketItem;
import com.idega.business.IBOSession;


/**
 * 
 *  Last modified: $Date: 2005/07/05 22:36:14 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.7 $
 */
public interface BasketBusiness extends IBOSession {

	/**
	 * @see com.idega.block.basket.business.BasketBusinessBean#addItem
	 */
	public void addItem(BasketItem item) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.basket.business.BasketBusinessBean#addItem
	 */
	public void addItem(BasketItem item, int quantity) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.basket.business.BasketBusinessBean#removeItem
	 */
	public void removeItem(BasketItem item) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.basket.business.BasketBusinessBean#changeQuantity
	 */
	public void changeQuantity(BasketItem item, int quantity) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.basket.business.BasketBusinessBean#checkout
	 */
	public void checkout(BasketCheckoutBusiness checkoutBusiness) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.basket.business.BasketBusinessBean#getBasket
	 */
	public Map getBasket() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.basket.business.BasketBusinessBean#checkForItemInBasket
	 */
	public boolean checkForItemInBasket(BasketItem item) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.basket.business.BasketBusinessBean#emptyBasket
	 */
	public void emptyBasket() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.basket.business.BasketBusinessBean#getExtraInfo
	 */
	public List getExtraInfo() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.basket.business.BasketBusinessBean#emptyExtraInfo
	 */
	public void emptyExtraInfo() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.basket.business.BasketBusinessBean#addExtraInfo
	 */
	public void addExtraInfo(Object info) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.basket.business.BasketBusinessBean#removeExtraInfo
	 */
	public void removeExtraInfo(Object info) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.basket.business.BasketBusinessBean#getQuantity
	 */
	public int getQuantity() throws java.rmi.RemoteException;
}
