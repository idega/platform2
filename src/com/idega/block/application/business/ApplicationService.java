/*
 * Created on 11.8.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package com.idega.block.application.business;

import java.rmi.RemoteException;


import com.idega.block.application.data.ApplicantHome;
import com.idega.block.application.data.ApplicationHome;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.data.ApplicationSubjectHome;
import com.idega.block.application.data.ApplicationSubjectInfoHome;

/**
 * @author aron
 *
 * ApplicationService TODO Describe this type
 */
public interface ApplicationService {
    /**
     * @see com.idega.block.application.business.ApplicationServiceBean#getApplicationHome
     */
    public ApplicationHome getApplicationHome() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.application.business.ApplicationServiceBean#getApplicantHome
     */
    public ApplicantHome getApplicantHome() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.application.business.ApplicationServiceBean#getSubjectHome
     */
    public ApplicationSubjectHome getSubjectHome() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.application.business.ApplicationServiceBean#getSubjectInfoHome
     */
    public ApplicationSubjectInfoHome getSubjectInfoHome()
            throws RemoteException, java.rmi.RemoteException;

    /**
     * @see com.idega.block.application.business.ApplicationServiceBean#getApplicationStatuses
     */
    public String[] getApplicationStatuses() throws java.rmi.RemoteException;

    /**
     * @see com.idega.block.application.business.ApplicationServiceBean#storeApplicationSubject
     */
    public ApplicationSubject storeApplicationSubject(Integer ID, String sName,
            java.sql.Date expires, String extra) throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.application.business.ApplicationServiceBean#removeApplicationSubject
     */
    public boolean removeApplicationSubject(Integer id) throws RemoteException,
            java.rmi.RemoteException;

}
