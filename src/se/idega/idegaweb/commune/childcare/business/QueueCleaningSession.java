/*
 * $Id: QueueCleaningSession.java,v 1.1 2004/11/25 10:44:06 aron Exp $
 * Created on 25.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;

import javax.ejb.FinderException;


import com.idega.business.IBOSession;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/11/25 10:44:06 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface QueueCleaningSession extends IBOSession {
    /**
     * @see se.idega.idegaweb.commune.childcare.business.QueueCleaningSessionBean#cleanQueueInThread
     */
    public boolean cleanQueueInThread(int providerID, User performer)
            throws FinderException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.QueueCleaningSessionBean#cleanQueue
     */
    public boolean cleanQueue(int providerID, User performer)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.QueueCleaningSessionBean#isStillCleaningQueue
     */
    public boolean isStillCleaningQueue() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.childcare.business.QueueCleaningSessionBean#getService
     */
    public ChildCareBusiness getService() throws RemoteException;

}
