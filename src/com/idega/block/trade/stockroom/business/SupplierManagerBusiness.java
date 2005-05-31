/*
 * $Id: SupplierManagerBusiness.java,v 1.4 2005/05/31 19:23:12 gimmi Exp $
 * Created on 4.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.business;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierStaffGroup;
import com.idega.business.IBOService;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWUserContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2005/05/31 19:23:12 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.4 $
 */
public interface SupplierManagerBusiness extends IBOService {

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#updateSupplierManager
	 */
	public Group updateSupplierManager(Object pk, String name, String description) throws IDOLookupException,
			FinderException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#createSupplierManager
	 */
	public Group createSupplierManager(String name, String description, String adminName, String loginName,
			String password, IWUserContext iwuc) throws RemoteException, CreateException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#setRole
	 */
	public void setRole(Group supplierManager, String role, boolean setRole) throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#setRole
	 */
	public void setRole(Supplier supplier, String role, boolean setRole) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getRoles
	 */
	public Collection getRoles(Group supplierManager) throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getRolesAsString
	 */
	public Collection getRolesAsString(Group supplierManager) throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getRoles
	 */
	public Collection getRoles(Supplier supplier) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getRolesAsString
	 */
	public Collection getRolesAsString(Supplier supplier) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getSupplierManagerAdmins
	 */
	public Collection getSupplierManagerAdmins(Group supplierManager) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#findAllSupplierManagers
	 */
	public Collection findAllSupplierManagers() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getSupplierManagerGroup
	 */
	public Group getSupplierManagerGroup() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getSupplierManager
	 */
	public Group getSupplierManager(User user) throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getGroupBusiness
	 */
	public GroupBusiness getGroupBusiness() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#deleteSupplier
	 */
	public void deleteSupplier(int id) throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#updateSupplier
	 */
	public Supplier updateSupplier(int supplierId, String name, String description, int[] addressIds, int[] phoneIds,
			int[] emailIds, String organizationID) throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#createSupplier
	 */
	public Supplier createSupplier(String name, String userName, String password, String description, int[] addressIds,
			int[] phoneIds, int[] emailIds, String organizationID) throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#invalidateSupplier
	 */
	public void invalidateSupplier(Supplier supplier) throws FinderException, RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#validateSupplier
	 */
	public void validateSupplier(Supplier supplier) throws SQLException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getPermissionGroup
	 */
	public Group getPermissionGroup(Supplier supplier) throws FinderException, RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getSupplierStaffGroup
	 */
	public SupplierStaffGroup getSupplierStaffGroup(Supplier supplier) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#addUser
	 */
	public void addUser(Supplier supplier, User user, boolean addToPermissionGroup) throws FinderException,
			RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getUsersInPermissionGroup
	 */
	public List getUsersInPermissionGroup(Supplier supplier) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getUsersNotInPermissionGroup
	 */
	public List getUsersNotInPermissionGroup(Supplier supplier) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getUsers
	 */
	public List getUsers(Supplier supplier) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getUsersIncludingResellers
	 */
	public List getUsersIncludingResellers(Supplier supplier) throws RemoteException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getUsersIncludingResellers
	 */
	public List getUsersIncludingResellers(Supplier supplier, Object objBetweenResellers) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getUsersIncludingResellers
	 */
	public List getUsersIncludingResellers(Supplier supplier, boolean includeSupplierUsers) throws RemoteException,
			FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean#getMainUser
	 */
	public User getMainUser(Supplier supplier) throws RemoteException, FinderException;
}
