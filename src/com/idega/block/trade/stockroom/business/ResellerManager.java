package com.idega.block.trade.stockroom.business;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.ResellerStaffGroup;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.business.IBOService;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * @author gimmi
 */
public interface ResellerManager extends IBOService {

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#deleteReseller
	 */
	public boolean deleteReseller(int id) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#deleteReseller
	 */
	public boolean deleteReseller(Reseller reseller) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#updateReseller
	 */
	public Reseller updateReseller(int resellerId, String name, String description, int[] addressIds, int[] phoneIds,
			int[] emailIds) throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#createReseller
	 */
	public Reseller createReseller(Reseller parentReseller, String name, String userName, String password,
			String description, int[] addressIds, int[] phoneIds, int[] emailIds) throws Exception,
			java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#invalidateReseller
	 */
	public void invalidateReseller(Reseller reseller) throws SQLException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#validateReseller
	 */
	public void validateReseller(Reseller reseller) throws SQLException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#updateSupplier
	 */
	public void updateSupplier(Supplier supplier, String name, String description, int[] addressIds, int[] phoneIds,
			int[] emailIds) throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getSuppliers
	 */
	public Supplier[] getSuppliers(int resellerId) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getSuppliers
	 */
	public Supplier[] getSuppliers(int resellerId, String orderBy) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getResellerChilds
	 */
	public Iterator getResellerChilds(Reseller reseller) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getResellerChilds
	 */
	public Iterator getResellerChilds(Reseller reseller, String orderBy) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getResellersAvailable
	 */
	public List getResellersAvailable(Reseller reseller) throws SQLException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getResellersAvailable
	 */
	public List getResellersAvailable(Reseller reseller, String orderBy) throws SQLException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getResellers
	 */
	public Iterator getResellers(Supplier supplier) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getResellers
	 */
	public Iterator getResellers(Supplier supplier, String orderBy) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getPermissionGroup
	 */
	public Group getPermissionGroup(Reseller reseller) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getResellerStaffGroup
	 */
	public ResellerStaffGroup getResellerStaffGroup(Reseller reseller) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#addUser
	 */
	public void addUser(Reseller reseller, User user, boolean addToPermissionGroup) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getUsersInPermissionGroup
	 */
	public List getUsersInPermissionGroup(Reseller reseller) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getUsersNotInPermissionGroup
	 */
	public List getUsersNotInPermissionGroup(Reseller reseller) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getUsers
	 */
	public List getUsers(Reseller reseller) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getUsersIncludingSubResellers
	 */
	public List getUsersIncludingSubResellers(Reseller reseller) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getUsersIncludingSubResellers
	 */
	public List getUsersIncludingSubResellers(Reseller reseller, Object objectBetweenResellers) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getReseller
	 */
	public Reseller getReseller(User user) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.ResellerManagerBean#getMainUser
	 */
	public User getMainUser(Reseller reseller) throws SQLException, java.rmi.RemoteException;
}
