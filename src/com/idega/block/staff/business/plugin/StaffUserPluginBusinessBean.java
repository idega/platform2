/*
 * $Id: StaffUserPluginBusinessBean.java,v 1.1 2005/02/01 13:40:20 laddi Exp $
 * Created on Nov 15, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.staff.business.plugin;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.idega.block.staff.presentation.StaffUserTab;
import com.idega.business.IBOServiceBean;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * A user application plugin for staff information.
 *  Last modified: $Date: 2005/02/01 13:40:20 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">Thorhallur Helgason</a>
 * @version $Revision: 1.1 $
 */
public class StaffUserPluginBusinessBean extends IBOServiceBean implements UserGroupPlugInBusiness , StaffUserPluginBusiness{

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#canCreateSubGroup(com.idega.user.data.Group, java.lang.String)
	 */
	public String canCreateSubGroup(Group parentGroup, String groupTypeOfSubGroup) throws RemoteException {
		return null;
	}
	/**
	 * 
	 */
	public StaffUserPluginBusinessBean() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreateOrUpdate(com.idega.user.data.Group)
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreateOrUpdate(com.idega.user.data.User)
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#findGroupsByFields(java.util.Collection, java.util.Collection, java.util.Collection)
	 */
	public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators,
			Collection listViewerFieldValues) throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getListViewerFields()
	 */
	public Collection getListViewerFields() throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	public List getMainToolbarElements() throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getPresentationObjectClass()
	 */
	public Class getPresentationObjectClass() throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		List tabs = new ArrayList();
		tabs.add(new StaffUserTab());
		return tabs;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserAssignableFromGroupToGroup(com.idega.user.data.User, com.idega.user.data.Group, com.idega.user.data.Group)
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup)
			throws RemoteException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserSuitedForGroup(com.idega.user.data.User, com.idega.user.data.Group)
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) throws RemoteException {
		return null;
	}
}