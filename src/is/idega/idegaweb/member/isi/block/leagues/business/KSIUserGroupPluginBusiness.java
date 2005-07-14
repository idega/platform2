/*
 * $Id: KSIUserGroupPluginBusiness.java,v 1.1 2005/07/14 01:00:43 eiki Exp $
 * Created on Jul 13, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.leagues.business;

import is.idega.idegaweb.member.business.MemberUserBusiness;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.xml.rpc.ServiceException;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOService;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2005/07/14 01:00:43 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.1 $
 */
public interface KSIUserGroupPluginBusiness extends IBOService, UserGroupPlugInBusiness {

	/**
	 * @see is.idega.idegaweb.member.isi.block.leagues.business.KSIUserGroupPluginBusinessBean#afterUserCreateOrUpdate
	 */
	public void afterUserCreateOrUpdate(User user, Group parentGroup) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.leagues.business.KSIUserGroupPluginBusinessBean#isUserSuitedForGroup
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.leagues.business.KSIUserGroupPluginBusinessBean#getClubNumberForPlayerFromWebService
	 */
	public int getClubNumberForPlayerFromWebService(String personalId) throws ServiceException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.leagues.business.KSIUserGroupPluginBusinessBean#registerPlayerToClubViaWebService
	 */
	public String registerPlayerToClubViaWebService(String personalId, int clubNumber, String clubName)
			throws RemoteException, ServiceException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.leagues.business.KSIUserGroupPluginBusinessBean#getMemberUserBusiness
	 */
	public MemberUserBusiness getMemberUserBusiness() throws IBOLookupException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.leagues.business.KSIUserGroupPluginBusinessBean#getGroupBusiness
	 */
	public GroupBusiness getGroupBusiness() throws IBOLookupException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.leagues.business.KSIUserGroupPluginBusinessBean#doClubMemberExchange
	 */
	public String doClubMemberExchange(String personalIdOfPlayer, String clubNumberToRegisterTo, String dateOfActivation)
			throws java.rmi.RemoteException;
}
