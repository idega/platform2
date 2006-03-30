/*
 * $Id: GolfUserPluginBusiness.java,v 1.7.4.2 2006/03/30 14:55:22 eiki Exp $
 * Created on Mar 30, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.business.plugin;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import com.idega.business.IBOService;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2006/03/30 14:55:22 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.7.4.2 $
 */
public interface GolfUserPluginBusiness extends IBOService, UserGroupPlugInBusiness {

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#afterGroupCreateOrUpdate
	 */
	public void afterGroupCreateOrUpdate(Group group, Group parentGroup) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#afterUserCreateOrUpdate
	 */
	public void afterUserCreateOrUpdate(User user, Group parentGroup) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#beforeGroupRemove
	 */
	public void beforeGroupRemove(Group group, Group parentGroup) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#beforeUserRemove
	 */
	public void beforeUserRemove(User user, Group parentGroup) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getGroupPropertiesTabs
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getGroupToolbarElements
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getMainToolbarElements
	 */
	public List getMainToolbarElements() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getUserPropertiesTabs
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#isCurrentUserGolfAdmin
	 */
	public boolean isCurrentUserGolfAdmin(IWContext iwc) throws RemoteException;

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

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getUserBusiness
	 */
	public UserBusiness getUserBusiness() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#canCreateSubGroup
	 */
	public String canCreateSubGroup(Group group, String groupTypeOfSubGroup) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#registerGolferToClubAndGolfDotIs
	 */
	public String registerGolferToClubAndGolfDotIs(String ssn, String clubNumber, String clubMembershipType)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#disableGolferInClub
	 */
	public String disableGolferInClub(String ssn, String clubNumber) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.business.plugin.GolfUserPluginBusinessBean#getGolferInfo
	 */
	public Map getGolferInfo(String ssn) throws Exception, java.rmi.RemoteException;
}
