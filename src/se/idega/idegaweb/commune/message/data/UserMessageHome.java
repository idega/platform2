/*
 * $Id: UserMessageHome.java,v 1.4 2004/10/12 08:33:33 aron Exp $
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

import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/10/12 08:33:33 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.4 $
 */
public interface UserMessageHome extends IDOHome {
    public UserMessage create() throws javax.ejb.CreateException;

    public UserMessage findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbFindMessages
     */
    public Collection findMessages(User user) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbFindMessagesByStatus
     */
    public Collection findMessagesByStatus(User user, String[] status)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbFindMessagesByStatus
     */
    public Collection findMessagesByStatus(User user, String[] status,
            int numberOfEntries, int startingEntry) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbFindMessagesByStatus
     */
    public Collection findMessagesByStatus(Group group, String[] status)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbFindMessagesByStatus
     */
    public Collection findMessagesByStatus(Group group, String[] status,
            int numberOfEntries, int startingEntry) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbFindMessagesByStatus
     */
    public Collection findMessagesByStatus(User user, Collection groups,
            String[] status, int numberOfEntries, int startingEntry)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbHomeGetNumberOfMessagesByStatus
     */
    public int getNumberOfMessagesByStatus(User user, String[] status)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbHomeGetNumberOfMessagesByStatus
     */
    public int getNumberOfMessagesByStatus(User user, Collection groups,
            String[] status) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbFindMessages
     */
    public java.util.Collection findMessages(com.idega.user.data.User user,
            String[] status) throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbFindMessages
     */
    public java.util.Collection findMessages(com.idega.user.data.Group group,
            String[] status) throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbFindMessages
     */
    public java.util.Collection findMessages(com.idega.user.data.User user,
            String[] status, int numberOfEntries, int startingEntry)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbFindMessages
     */
    public java.util.Collection findMessages(com.idega.user.data.Group group,
            String[] status, int numberOfEntries, int startingEntry)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbFindMessages
     */
    public java.util.Collection findMessages(com.idega.user.data.User user,
            java.util.Collection groups, String[] status, int numberOfEntries,
            int startingEntry) throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbHomeGetNumberOfMessages
     */
    public int getNumberOfMessages(com.idega.user.data.User user,
            String[] status) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.message.data.UserMessageBMPBean#ejbHomeGetNumberOfMessages
     */
    public int getNumberOfMessages(com.idega.user.data.User user,
            java.util.Collection groups, String[] status) throws IDOException;

}
