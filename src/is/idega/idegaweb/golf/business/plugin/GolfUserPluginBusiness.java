/*
 * $Id: GolfUserPluginBusiness.java,v 1.2 2004/11/17 19:16:45 eiki Exp $
 * Created on Nov 16, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.business.plugin;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import com.idega.business.IBOService;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/11/17 19:16:45 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.2 $
 */
public interface GolfUserPluginBusiness extends IBOService, UserGroupPlugInBusiness {

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#afterGroupCreateOrUpdate
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#afterUserCreateOrUpdate
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#beforeGroupRemove
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#beforeUserRemove
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#findGroupsByFields
	 */
	public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators,
			Collection listViewerFieldValues) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getGroupPropertiesTabs
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getGroupToolbarElements
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getListViewerFields
	 */
	public Collection getListViewerFields() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getMainToolbarElements
	 */
	public List getMainToolbarElements() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getPresentationObjectClass
	 */
	public Class getPresentationObjectClass() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getUserPropertiesTabs
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#instanciateEditor
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#instanciateViewer
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#isUserAssignableFromGroupToGroup
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#isUserSuitedForGroup
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getGolfClubs
	 */
	public Collection getGolfClubs() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getGroupBusiness
	 */
	public GroupBusiness getGroupBusiness() throws java.rmi.RemoteException;
}