/*
 * $Id: MessageBusiness.java,v 1.33 2004/10/12 08:32:54 aron Exp $
 * Created on 7.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.business;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.message.data.MessageHandlerInfo;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.message.data.SystemArchivationMessage;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseCode;
import com.idega.business.IBOService;
import com.idega.core.component.data.ICObject;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOException;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * 
 *  Last modified: $Date: 2004/10/12 08:32:54 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.33 $
 */
public interface MessageBusiness extends IBOService, CaseBusiness {
    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#deleteMessage
     */
    public void deleteMessage(String messageType, int messageID)
            throws FinderException, RemoveException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#deleteUserMessage
     */
    public void deleteUserMessage(int messageID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#markMessageAsRead
     */
    public void markMessageAsRead(Message message)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#isMessageRead
     */
    public boolean isMessageRead(Message message)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getCaseCodeSystemArchivationMessage
     */
    public CaseCode getCaseCodeSystemArchivationMessage()
            throws RemoteException, FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getCaseCodeUserMessage
     */
    public CaseCode getCaseCodeUserMessage() throws RemoteException,
            FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getCaseCodePrintedLetterMessage
     */
    public CaseCode getCaseCodePrintedLetterMessage() throws RemoteException,
            FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getMessage
     */
    public Message getMessage(String messageType, int messageID)
            throws FinderException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getUserMessage
     */
    public Message getUserMessage(int messageId) throws FinderException,
            RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getNumberOfMessages
     */
    public int getNumberOfMessages(User user) throws Exception,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getNumberOfNewMessages
     */
    public int getNumberOfNewMessages(User user) throws IDOException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getNumberOfMessages
     */
    public int getNumberOfMessages(User user, Collection groups)
            throws Exception, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#findMessages
     */
    public Collection findMessages(User user) throws Exception,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#findMessages
     */
    public Collection findMessages(User user, int numberOfEntries,
            int startingEntry) throws Exception, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#findMessages
     */
    public Collection findMessages(User user, Collection groups,
            int numberOfEntries, int startingEntry) throws Exception,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#findMessages
     */
    public Collection findMessages(Group group) throws Exception,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#findMessages
     */
    public Collection findMessages(Group group, int numberOfEntries,
            int startingEntry) throws Exception, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(User user, String subject, String body)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(User user, String subject, String body,
            boolean sendLetter) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(User user, String subject, Group handler,
            String body, boolean sendLetter) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(User user, String subject, Group handler,
            String body, boolean sendLetter, String contentCode)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(User receiver, String subject,
            String body, User sender, boolean sendLetter)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            String subject, String body, boolean sendLetter)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            String subject, String body, boolean sendLetter,
            boolean alwaysSendLetter) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            String subject, String body, String letterBody, boolean sendLetter,
            boolean alwaysSendLetter) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            User sender, String subject, String body, boolean sendLetter)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            User sender, Group handler, String subject, String body,
            boolean sendLetter) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            User sender, Group handler, String subject, String body,
            boolean pSendLetterIfNoEmail, String contentCode)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            User sender, Group handler, String subject, String body,
            boolean pSendLetterIfNoEmail, String contentCode,
            boolean alwaysSendLetter) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            User sender, Group handler, String subject, String body,
            String letterBody, boolean pSendLetterIfNoEmail,
            String contentCode, boolean alwaysSendLetter)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            User sender, Group handler, String subject, String body,
            String letterBody, boolean sendLetterIfNoEmail, String contentCode,
            boolean alwaysSendLetter, boolean sendMail)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(MessageValue msgValue)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(int userID, String subject, String body)
            throws CreateException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getPrintedLetterMessages
     */
    public Collection getPrintedLetterMessages() throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getPrintedLetterMessagesByType
     */
    public Collection getPrintedLetterMessagesByType(String type,
            int resultSize, int startingIndex) throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getPrintedLetterMessagesByType
     */
    public Collection getPrintedLetterMessagesByType(String type,
            IWTimestamp from, IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getSinglePrintedLetterMessagesByType
     */
    public Collection getSinglePrintedLetterMessagesByType(String type,
            IWTimestamp from, IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getUnPrintedLetterMessages
     */
    public Collection getUnPrintedLetterMessages() throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getUnPrintedLetterMessagesByType
     */
    public Collection getUnPrintedLetterMessagesByType(String type,
            int resultSize, int startingIndex) throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getUnPrintedLetterMessagesByType
     */
    public Collection getUnPrintedLetterMessagesByType(String type,
            IWTimestamp from, IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getSingleUnPrintedLetterMessagesByType
     */
    public Collection getSingleUnPrintedLetterMessagesByType(String type,
            IWTimestamp from, IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getSingleLettersByTypeAndStatus
     */
    public Collection getSingleLettersByTypeAndStatus(String type,
            String status, IWTimestamp from, IWTimestamp to, int resultSize,
            int startingIndex) throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getLettersByBulkFile
     */
    public Collection getLettersByBulkFile(int file, String type,
            String status, int resultSize, int startingIndex)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#flagPrintedLetterAsPrinted
     */
    public void flagPrintedLetterAsPrinted(User performer,
            PrintedLetterMessage message) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#flagMessageAsPrinted
     */
    public void flagMessageAsPrinted(User performer, Message message)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#flagMessageAsUnPrinted
     */
    public void flagMessageAsUnPrinted(User performer, Message message)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#flagMessageAsInactive
     */
    public void flagMessageAsInactive(User performer, Message message)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#flagMessagesAsInactive
     */
    public void flagMessagesAsInactive(User performer, String[] msgKeys)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#flagMessageWithStatus
     */
    public void flagMessageWithStatus(User performer, Message message,
            String status) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#flagMessagesWithStatus
     */
    public void flagMessagesWithStatus(User performer, String[] msgKeys,
            String status) throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createPrintArchivationMessage
     */
    public Message createPrintArchivationMessage(User user, String subject,
            String body) throws CreateException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createPrintArchivationMessage
     */
    public Message createPrintArchivationMessage(int userID, String subject,
            String body) throws CreateException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createPrintArchivationMessage
     */
    public SystemArchivationMessage createPrintArchivationMessage(User forUser,
            User creator, String subject, String body, ICFile attatchement)
            throws CreateException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createPrintArchivationMessage
     */
    public SystemArchivationMessage createPrintArchivationMessage(
            int forUserID, int creatorUserID, String subject, String body,
            int attatchementFileID) throws CreateException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createPrintedPasswordLetterMessage
     */
    public PrintedLetterMessage createPrintedPasswordLetterMessage(User user,
            String subject, String body) throws CreateException,
            RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createPasswordMessage
     */
    public PrintedLetterMessage createPasswordMessage(User user,
            String username, String password) throws CreateException,
            RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createPrintedLetterMessage
     */
    public Message createPrintedLetterMessage(int userID, String subject,
            String body) throws CreateException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#sendMessage
     */
    public void sendMessage(String email, String subject, String body)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#sendMessage
     */
    public void sendMessage(String email, String subject, String body,
            File attachment) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getIfUserPreferesMessageByEmail
     */
    public boolean getIfUserPreferesMessageByEmail(User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getIfUserPreferesMessageInMessageBox
     */
    public boolean getIfUserPreferesMessageInMessageBox(User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getIfCanSendEmail
     */
    public boolean getIfCanSendEmail() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#setIfUserPreferesMessageByEmail
     */
    public void setIfUserPreferesMessageByEmail(User user, boolean preference)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#setIfUserPreferesMessageInMessageBox
     */
    public void setIfUserPreferesMessageInMessageBox(User user,
            boolean preference) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#sendMessageToCommuneAdministrators
     */
    public void sendMessageToCommuneAdministrators(String subject, String body)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#sendMessageToCommuneAdministrators
     */
    public void sendMessageToCommuneAdministrators(Case theCase,
            String subject, String body) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#getBundleIdentifier
     */
    public String getBundleIdentifier() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.MessageBusinessBean#createMessageHandlerInfo
     */
    public MessageHandlerInfo createMessageHandlerInfo(
            MessagePdfHandler handler, ICObject ico) throws CreateException,
            RemoteException;

}
