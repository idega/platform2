/*
 * $Id: SystemArchivationMessageHome.java 1.1 7.10.2004 aron Exp $
 * Created on 7.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * 
 *  Last modified: $Date: 7.10.2004 11:26:08 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface SystemArchivationMessageHome extends IDOHome {
    public SystemArchivationMessage create() throws javax.ejb.CreateException;

    public SystemArchivationMessage findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbFindMessages
     */
    public Collection findMessages(User user) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbFindMessagesByStatus
     */
    public Collection findMessagesByStatus(User user, String[] status)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbFindPrintedMessages
     */
    public Collection findPrintedMessages() throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbFindUnPrintedMessages
     */
    public Collection findUnPrintedMessages() throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbFindPrintedMessages
     */
    public Collection findPrintedMessages(IWTimestamp from, IWTimestamp to)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbFindUnPrintedMessages
     */
    public Collection findUnPrintedMessages(IWTimestamp from, IWTimestamp to)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbHomeGetNumberOfUnPrintedMessages
     */
    public int getNumberOfUnPrintedMessages();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbHomeGetPrintMessageTypes
     */
    public String[] getPrintMessageTypes();

}
