/*
 * $Id: MessageSession.java 1.1 2.11.2004 aron Exp $
 * Created on 2.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.business;


import com.idega.business.IBOSession;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2.11.2004 21:01:49 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface MessageSession extends IBOSession {
    /**
     * @see se.idega.idegaweb.commune.message.business.MessageSessionBean#getIfUserCanReceiveEmails
     */
    public boolean getIfUserCanReceiveEmails(User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageSessionBean#getIfUserPreferesMessageByEmail
     */
    public boolean getIfUserPreferesMessageByEmail()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageSessionBean#getIfUserPreferesMessageInMessageBox
     */
    public boolean getIfUserPreferesMessageInMessageBox()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageSessionBean#setIfUserPreferesMessageByEmail
     */
    public void setIfUserPreferesMessageByEmail(boolean preference)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageSessionBean#setIfUserPreferesMessageInMessageBox
     */
    public void setIfUserPreferesMessageInMessageBox(boolean preference)
            throws java.rmi.RemoteException;

}
