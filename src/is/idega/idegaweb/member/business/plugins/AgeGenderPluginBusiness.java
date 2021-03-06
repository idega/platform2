/*
 * $Id: AgeGenderPluginBusiness.java,v 1.12 2005/07/14 01:02:25 eiki Exp $
 * Created on Jul 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.business.plugins;

import java.rmi.RemoteException;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.business.IBOService;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2005/07/14 01:02:25 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.12 $
 */
public interface AgeGenderPluginBusiness extends IBOService, UserGroupPlugInBusiness {

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#setMale
	 */
	public void setMale(Group group) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#setFemale
	 */
	public void setFemale(Group group) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#setNeutral
	 */
	public void setNeutral(Group group) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#isFemale
	 */
	public boolean isFemale(Group group) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#isNeutral
	 */
	public boolean isNeutral(Group group) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#isMale
	 */
	public boolean isMale(Group group) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#setAgeLimitIsStringentCondition
	 */
	public void setAgeLimitIsStringentCondition(Group group, boolean ageLimitIsStringentCondition)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#isAgeLimitStringentCondition
	 */
	public boolean isAgeLimitStringentCondition(Group group) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#setLowerAgeLimit
	 */
	public void setLowerAgeLimit(Group group, int lowerAgeLimit) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#getLowerAgeLimit
	 */
	public int getLowerAgeLimit(Group group) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#setUpperAgeLimit
	 */
	public void setUpperAgeLimit(Group group, int upperAgeLimit) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#getUpperAgeLimit
	 */
	public int getUpperAgeLimit(Group group) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#getLowerAgeLimitDefault
	 */
	public int getLowerAgeLimitDefault() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#getUpperAgeLimitDefault
	 */
	public int getUpperAgeLimitDefault() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#setKeyDateForAge
	 */
	public void setKeyDateForAge(Group group, String keyDateForAge) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#getKeyDateForAge
	 */
	public String getKeyDateForAge(Group group) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#afterGroupCreateOrUpdate
	 */
	public void afterGroupCreateOrUpdate(Group group, Group parentGroup) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#afterUserCreateOrUpdate
	 */
	public void afterUserCreateOrUpdate(User user, Group parentGroup) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#beforeGroupRemove
	 */
	public void beforeGroupRemove(Group group, Group parentGroup) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#beforeUserRemove
	 */
	public void beforeUserRemove(User user, Group parentGroup) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#getGroupPropertiesTabs
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#getUserPropertiesTabs
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#instanciateEditor
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#instanciateViewer
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#isUserAssignableFromGroupToGroup
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#isUserSuitedForGroup
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#getKeyDateForYearZero
	 */
	public GregorianCalendar getKeyDateForYearZero(Group group) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#getMainToolbarElements
	 */
	public List getMainToolbarElements() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#getGroupToolbarElements
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#canCreateSubGroup
	 */
	public String canCreateSubGroup(Group group, String groupTypeOfSubGroup) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#setNationalityDependent
	 */
	public void setNationalityDependent(Group group, boolean isDependent) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#setClubMemberExchangeDependent
	 */
	public void setClubMemberExchangeDependent(Group group, boolean isDependent) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#isNationalityDependent
	 */
	public boolean isNationalityDependent(Group group) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean#isClubMemberExchangeDependent
	 */
	public boolean isClubMemberExchangeDependent(Group group) throws java.rmi.RemoteException;
}
