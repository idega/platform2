/*
 * $Id: StaffUserPluginBusiness.java,v 1.1 2005/02/01 13:40:21 laddi Exp $
 * Created on 16.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.staff.business.plugin;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.idega.business.IBOService;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * Last modified: 16.11.2004 15:11:18 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface StaffUserPluginBusiness extends IBOService, UserGroupPlugInBusiness {

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#afterGroupCreateOrUpdate
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#afterUserCreateOrUpdate
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#beforeGroupRemove
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#beforeUserRemove
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#findGroupsByFields
	 */
	public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators, Collection listViewerFieldValues) throws RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#getGroupPropertiesTabs
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#getGroupToolbarElements
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#getListViewerFields
	 */
	public Collection getListViewerFields() throws RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#getMainToolbarElements
	 */
	public List getMainToolbarElements() throws RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#getPresentationObjectClass
	 */
	public Class getPresentationObjectClass() throws RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#getUserPropertiesTabs
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#instanciateEditor
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#instanciateViewer
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#isUserAssignableFromGroupToGroup
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) throws RemoteException;

	/**
	 * @see com.idega.block.staff.business.plugin.StaffUserPluginBusinessBean#isUserSuitedForGroup
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) throws RemoteException;

}
