/*
 * $Id: ProviderSession.java,v 1.2 2004/10/13 15:29:57 thomas Exp $
 * Created on 28.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.block.school.data.School;
import com.idega.business.IBOSession;

/**
 * 
 *  Last modified: $Date: 2004/10/13 15:29:57 $ by $Author: thomas $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2 $
 */
public interface ProviderSession extends IBOSession {
    /**
     * @see se.idega.idegaweb.commune.care.business.ProviderSessionBean#getProviderID
     */
    public int getProviderID() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.business.ProviderSessionBean#getProvider
     */
    public School getProvider() throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.business.ProviderSessionBean#setProviderID
     */
    public void setProviderID(int providerID) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.business.ProviderSessionBean#getParameterSeasonID
     */
    public String getParameterSeasonID() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.business.ProviderSessionBean#getParameterYearID
     */
    public String getParameterYearID() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.business.ProviderSessionBean#getParameterStudyPathID
     */
    public String getParameterStudyPathID() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.business.ProviderSessionBean#getParameterProviderID
     */
    public String getParameterProviderID() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.business.ProviderSessionBean#getSeasonID
     */
    public int getSeasonID() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.business.ProviderSessionBean#setSeasonID
     */
    public void setSeasonID(int seasonID) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.business.ProviderSessionBean#getYearID
     */
    public int getYearID() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.business.ProviderSessionBean#setYearID
     */
    public void setYearID(int yearID) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.business.ProviderSessionBean#getStudyPathID
     */
    public int getStudyPathID() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.care.business.ProviderSessionBean#setStudyPathID
     */
    public void setStudyPathID(int pathID) throws java.rmi.RemoteException;

}
