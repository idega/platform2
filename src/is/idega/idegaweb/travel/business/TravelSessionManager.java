/*
 * $Id: TravelSessionManager.java,v 1.6 2005/05/31 19:15:20 gimmi Exp $
 * Created on 10.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.business;

import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine;
import java.util.Locale;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.business.IBOSession;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2005/05/31 19:15:20 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.6 $
 */
public interface TravelSessionManager extends IBOSession {

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#clearLocale
	 */
	public void clearLocale() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#clearAll
	 */
	public void clearAll() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#isSupplierManager
	 */
	public boolean isSupplierManager() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#isSet
	 */
	public boolean isSet() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#setIsSupplierManager
	 */
	public void setIsSupplierManager(boolean isSupplierManager) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#setSupplierManager
	 */
	public void setSupplierManager(Group supplierManager) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getSupplierManager
	 */
	public Group getSupplierManager() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getUser
	 */
	public User getUser() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getUserId
	 */
	public int getUserId() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getLocale
	 */
	public Locale getLocale() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getLocaleId
	 */
	public int getLocaleId() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getIWResourceBundle
	 */
	public IWResourceBundle getIWResourceBundle() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getIWBundle
	 */
	public IWBundle getIWBundle() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getSupplier
	 */
	public Supplier getSupplier() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#setSupplier
	 */
	public void setSupplier(Supplier supplier) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getReseller
	 */
	public Reseller getReseller() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#setReseller
	 */
	public void setReseller(Reseller reseller) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#setSearchEngine
	 */
	public void setSearchEngine(ServiceSearchEngine engine) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getSearchEngine
	 */
	public ServiceSearchEngine getSearchEngine() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#hasRole
	 */
	public boolean hasRole(String role) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#clearRoleCache
	 */
	public void clearRoleCache() throws java.rmi.RemoteException;
}
