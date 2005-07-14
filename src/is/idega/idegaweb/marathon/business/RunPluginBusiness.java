/*
 * $Id: RunPluginBusiness.java,v 1.5 2005/07/14 11:24:57 eiki Exp $
 * Created on Dec 7, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.business;

import java.rmi.RemoteException;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import com.idega.business.IBOService;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2005/07/14 11:24:57 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.5 $
 */
public interface RunPluginBusiness extends IBOService, UserGroupPlugInBusiness {

	/**
	 * @see is.idega.idegaweb.marathon.business.RunPluginBusinessBean#afterGroupCreateOrUpdate
	 */
	public void afterGroupCreateOrUpdate(Group group,Group parentGroup) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunPluginBusinessBean#afterUserCreateOrUpdate
	 */
	public void afterUserCreateOrUpdate(User user,Group parentGroup) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunPluginBusinessBean#beforeGroupRemove
	 */
	public void beforeGroupRemove(Group group,Group parentGroup) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunPluginBusinessBean#beforeUserRemove
	 */
	public void beforeUserRemove(User user,Group parentGroup) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunPluginBusinessBean#getGroupPropertiesTabs
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunPluginBusinessBean#getUserPropertiesTabs
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunPluginBusinessBean#instanciateEditor
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunPluginBusinessBean#instanciateViewer
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunPluginBusinessBean#isUserAssignableFromGroupToGroup
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunPluginBusinessBean#isUserSuitedForGroup
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunPluginBusinessBean#getMainToolbarElements
	 */
	public List getMainToolbarElements() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunPluginBusinessBean#getGroupToolbarElements
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException;
}
