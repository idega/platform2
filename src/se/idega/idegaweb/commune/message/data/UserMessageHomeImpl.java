/*
 * $Id: UserMessageHomeImpl.java,v 1.8 2004/10/12 08:33:33 aron Exp $
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
import com.idega.data.IDOFactory;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/10/12 08:33:33 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.8 $
 */
public class UserMessageHomeImpl extends IDOFactory implements UserMessageHome {
    protected Class getEntityInterfaceClass() {
        return UserMessage.class;
    }

    public UserMessage create() throws javax.ejb.CreateException {
        return (UserMessage) super.createIDO();
    }

    public UserMessage findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (UserMessage) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findMessages(User user) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((UserMessageBMPBean) entity)
                .ejbFindMessages(user);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findMessagesByStatus(User user, String[] status)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((UserMessageBMPBean) entity)
                .ejbFindMessagesByStatus(user, status);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findMessagesByStatus(User user, String[] status,
            int numberOfEntries, int startingEntry) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((UserMessageBMPBean) entity)
                .ejbFindMessagesByStatus(user, status, numberOfEntries,
                        startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findMessagesByStatus(Group group, String[] status)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((UserMessageBMPBean) entity)
                .ejbFindMessagesByStatus(group, status);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findMessagesByStatus(Group group, String[] status,
            int numberOfEntries, int startingEntry) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((UserMessageBMPBean) entity)
                .ejbFindMessagesByStatus(group, status, numberOfEntries,
                        startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findMessagesByStatus(User user, Collection groups,
            String[] status, int numberOfEntries, int startingEntry)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((UserMessageBMPBean) entity)
                .ejbFindMessagesByStatus(user, groups, status, numberOfEntries,
                        startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int getNumberOfMessagesByStatus(User user, String[] status)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((UserMessageBMPBean) entity)
                .ejbHomeGetNumberOfMessagesByStatus(user, status);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfMessagesByStatus(User user, Collection groups,
            String[] status) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((UserMessageBMPBean) entity)
                .ejbHomeGetNumberOfMessagesByStatus(user, groups, status);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public java.util.Collection findMessages(com.idega.user.data.User user,
            String[] status) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((UserMessageBMPBean) entity)
                .ejbFindMessages(user, status);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public java.util.Collection findMessages(com.idega.user.data.Group group,
            String[] status) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((UserMessageBMPBean) entity)
                .ejbFindMessages(group, status);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public java.util.Collection findMessages(com.idega.user.data.User user,
            String[] status, int numberOfEntries, int startingEntry)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((UserMessageBMPBean) entity)
                .ejbFindMessages(user, status, numberOfEntries, startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public java.util.Collection findMessages(com.idega.user.data.Group group,
            String[] status, int numberOfEntries, int startingEntry)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((UserMessageBMPBean) entity)
                .ejbFindMessages(group, status, numberOfEntries, startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public java.util.Collection findMessages(com.idega.user.data.User user,
            java.util.Collection groups, String[] status, int numberOfEntries,
            int startingEntry) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((UserMessageBMPBean) entity)
                .ejbFindMessages(user, groups, status, numberOfEntries,
                        startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int getNumberOfMessages(com.idega.user.data.User user,
            String[] status) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((UserMessageBMPBean) entity)
                .ejbHomeGetNumberOfMessages(user, status);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfMessages(com.idega.user.data.User user,
            java.util.Collection groups, String[] status) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((UserMessageBMPBean) entity)
                .ejbHomeGetNumberOfMessages(user, groups, status);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

}
