/*
 * $Id: SystemArchivationMessageHomeImpl.java 1.1 7.10.2004 aron Exp $
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

import com.idega.data.IDOFactory;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * 
 *  Last modified: $Date: 7.10.2004 11:26:10 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class SystemArchivationMessageHomeImpl extends IDOFactory implements
        SystemArchivationMessageHome {
    protected Class getEntityInterfaceClass() {
        return SystemArchivationMessage.class;
    }

    public SystemArchivationMessage create() throws javax.ejb.CreateException {
        return (SystemArchivationMessage) super.createIDO();
    }

    public SystemArchivationMessage findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (SystemArchivationMessage) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findMessages(User user) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SystemArchivationMessageBMPBean) entity)
                .ejbFindMessages(user);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findMessagesByStatus(User user, String[] status)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SystemArchivationMessageBMPBean) entity)
                .ejbFindMessagesByStatus(user, status);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findPrintedMessages() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SystemArchivationMessageBMPBean) entity)
                .ejbFindPrintedMessages();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findUnPrintedMessages() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SystemArchivationMessageBMPBean) entity)
                .ejbFindUnPrintedMessages();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findPrintedMessages(IWTimestamp from, IWTimestamp to)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SystemArchivationMessageBMPBean) entity)
                .ejbFindPrintedMessages(from, to);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findUnPrintedMessages(IWTimestamp from, IWTimestamp to)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SystemArchivationMessageBMPBean) entity)
                .ejbFindUnPrintedMessages(from, to);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int getNumberOfUnPrintedMessages() {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SystemArchivationMessageBMPBean) entity)
                .ejbHomeGetNumberOfUnPrintedMessages();
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public String[] getPrintMessageTypes() {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        String[] theReturn = ((SystemArchivationMessageBMPBean) entity)
                .ejbHomeGetPrintMessageTypes();
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

}
