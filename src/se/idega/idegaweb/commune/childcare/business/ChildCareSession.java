/*
 * $Id: ChildCareSession.java,v 1.12 2005/03/07 16:40:30 laddi Exp $
 * Created on 7.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOSession;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/03/07 16:40:30 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.12 $
 */
public interface ChildCareSession extends IBOSession {

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getCommuneUserBusiness
	 */
	public CommuneUserBusiness getCommuneUserBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getChildCareBusiness
	 */
	public ChildCareBusiness getChildCareBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getProvider
	 */
	public School getProvider() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#hasPrognosis
	 */
	public boolean hasPrognosis() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setHasPrognosis
	 */
	public void setHasPrognosis(boolean hasPrognosis) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#hasOutdatedPrognosis
	 */
	public boolean hasOutdatedPrognosis() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setHasOutdatedPrognosis
	 */
	public void setHasOutdatedPrognosis(boolean hasOutdatedPrognosis) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getChildCareID
	 */
	public int getChildCareID() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getUserID
	 */
	public int getUserID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setChildCareID
	 */
	public void setChildCareID(int childcareID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setProvider
	 */
	public void setProvider(School provider) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setUserID
	 */
	public void setUserID(int userID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getParameterChildCareID
	 */
	public String getParameterChildCareID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getParameterUserID
	 */
	public String getParameterUserID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getParameterApplicationID
	 */
	public String getParameterApplicationID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getParameterCaseCode
	 */
	public String getParameterCaseCode() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getParameterSchoolTypeID
	 */
	public String getParameterSchoolTypeID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getParameterGroupID
	 */
	public String getParameterGroupID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getParameterCheckID
	 */
	public String getParameterCheckID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getApplicationID
	 */
	public int getApplicationID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getChildID
	 */
	public int getChildID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getCheckID
	 */
	public int getCheckID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setApplicationID
	 */
	public void setApplicationID(int applicationID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setChildID
	 */
	public void setChildID(int childID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setCheckID
	 */
	public void setCheckID(int checkID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getParameterFrom
	 */
	public String getParameterFrom() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getParameterSeasonID
	 */
	public String getParameterSeasonID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getParameterStatus
	 */
	public String getParameterStatus() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getParameterSortBy
	 */
	public String getParameterSortBy() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getParameterTo
	 */
	public String getParameterTo() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getStatus
	 */
	public String getStatus() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getSortBy
	 */
	public int getSortBy() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getFromTimestamp
	 */
	public IWTimestamp getFromTimestamp() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getToTimestamp
	 */
	public IWTimestamp getToTimestamp() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setStatus
	 */
	public void setStatus(String status) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setSortBy
	 */
	public void setSortBy(int sortBy) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setFromTimestamp
	 */
	public void setFromTimestamp(String timestamp) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setToTimestamp
	 */
	public void setToTimestamp(String timestamp) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getSchoolTypeID
	 */
	public int getSchoolTypeID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setSchoolTypeID
	 */
	public void setSchoolTypeID(int schTypeID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getGroupID
	 */
	public int getGroupID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setGroupID
	 */
	public void setGroupID(int groupID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getSeasonID
	 */
	public int getSeasonID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setSeasonID
	 */
	public void setSeasonID(int seasonID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#getCaseCode
	 */
	public String getCaseCode() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareSessionBean#setCaseCode
	 */
	public void setCaseCode(String caseCode) throws java.rmi.RemoteException;
}
