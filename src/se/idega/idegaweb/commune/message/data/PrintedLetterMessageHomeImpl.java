/*
 * $Id: PrintedLetterMessageHomeImpl.java 1.1 7.10.2004 aron Exp $
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
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * 
 *  Last modified: $Date: 7.10.2004 10:56:43 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class PrintedLetterMessageHomeImpl extends IDOFactory implements
        PrintedLetterMessageHome {
    protected Class getEntityInterfaceClass() {
        return PrintedLetterMessage.class;
    }

    public PrintedLetterMessage create() throws javax.ejb.CreateException {
        return (PrintedLetterMessage) super.createIDO();
    }

    public PrintedLetterMessage findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (PrintedLetterMessage) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findMessages(User user) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindMessages(user);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findMessagesByStatus(User user, String[] status)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindMessagesByStatus(user, status);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllUnPrintedLetters() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindAllUnPrintedLetters();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllPrintedLetters() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindAllPrintedLetters();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int getNumberOfUnprintedLettersByType(String letterType) {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((PrintedLetterMessageBMPBean) entity)
                .ejbHomeGetNumberOfUnprintedLettersByType(letterType);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfPrintedLettersByType(String letterType) {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((PrintedLetterMessageBMPBean) entity)
                .ejbHomeGetNumberOfPrintedLettersByType(letterType);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfLettersByStatusAndType(String caseStatus,
            String letterType) {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((PrintedLetterMessageBMPBean) entity)
                .ejbHomeGetNumberOfLettersByStatusAndType(caseStatus,
                        letterType);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfUnPrintedPasswordLetters() {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((PrintedLetterMessageBMPBean) entity)
                .ejbHomeGetNumberOfUnPrintedPasswordLetters();
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfPrintedPasswordLetters() {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((PrintedLetterMessageBMPBean) entity)
                .ejbHomeGetNumberOfPrintedPasswordLetters();
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfUnPrintedDefaultLetters() {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((PrintedLetterMessageBMPBean) entity)
                .ejbHomeGetNumberOfUnPrintedDefaultLetters();
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfPrintedDefaultLetters() {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((PrintedLetterMessageBMPBean) entity)
                .ejbHomeGetNumberOfPrintedDefaultLetters();
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public String[] getLetterTypes() {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        String[] theReturn = ((PrintedLetterMessageBMPBean) entity)
                .ejbHomeGetLetterTypes();
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findPrintedLettersByType(String letterType,
            int resultSize, int startingIndex) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindPrintedLettersByType(letterType, resultSize,
                        startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findPrintedLettersByType(String letterType,
            IWTimestamp from, IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindPrintedLettersByType(letterType, from, to, resultSize,
                        startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findSinglePrintedLettersByType(String letterType,
            IWTimestamp from, IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindSinglePrintedLettersByType(letterType, from, to,
                        resultSize, startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByBulkFile(int file, String letterType,
            String status, int resultSize, int startingIndex)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindByBulkFile(file, letterType, status, resultSize,
                        startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findSingleByTypeAndStatus(String letterType,
            String status, IWTimestamp from, IWTimestamp to, int resultSize,
            int startingIndex) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindSingleByTypeAndStatus(letterType, status, from, to,
                        resultSize, startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findUnPrintedLettersByType(String letterType,
            int resultSize, int startingIndex) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindUnPrintedLettersByType(letterType, resultSize,
                        startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findUnPrintedLettersByType(String letterType,
            IWTimestamp from, IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindUnPrintedLettersByType(letterType, from, to,
                        resultSize, startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findSingleUnPrintedLettersByType(String letterType,
            IWTimestamp from, IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindSingleUnPrintedLettersByType(letterType, from, to,
                        resultSize, startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findUnPrintedPasswordLetters(int resultSize,
            int startingIndex) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindUnPrintedPasswordLetters(resultSize, startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findPrintedPasswordLetters(int resultSize,
            int startingIndex) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindPrintedPasswordLetters(resultSize, startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findUnPrintedDefaultLetters(int resultSize,
            int startingIndex) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindUnPrintedDefaultLetters(resultSize, startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findPrintedDefaultLetters(int resultSize,
            int startingIndex) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindPrintedDefaultLetters(resultSize, startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public String[] getPrintMessageTypes() {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        String[] theReturn = ((PrintedLetterMessageBMPBean) entity)
                .ejbHomeGetPrintMessageTypes();
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findLettersByChildcare(int providerID, String ssn,
            String msgId, IWTimestamp from, IWTimestamp to)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindLettersByChildcare(providerID, ssn, msgId, from, to);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllLettersBySchool(int providerID, String ssn,
            String msgId, IWTimestamp from, IWTimestamp to)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindAllLettersBySchool(providerID, ssn, msgId, from, to);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findLetters(String[] msgId) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindLetters(msgId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findMessagesByStatus(User user, String[] status,
            int numberOfEntries, int startingEntry) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindMessagesByStatus(user, status, numberOfEntries,
                        startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findMessagesByStatus(Group group, String[] status)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindMessagesByStatus(group, status);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findMessagesByStatus(Group group, String[] status,
            int numberOfEntries, int startingEntry) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindMessagesByStatus(group, status, numberOfEntries,
                        startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findMessagesByStatus(User user, Collection groups,
            String[] status, int numberOfEntries, int startingEntry)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintedLetterMessageBMPBean) entity)
                .ejbFindMessagesByStatus(user, groups, status, numberOfEntries,
                        startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

}
