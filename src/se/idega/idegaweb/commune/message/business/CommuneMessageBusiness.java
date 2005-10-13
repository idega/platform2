/*
 * $Id: CommuneMessageBusiness.java,v 1.1 2005/10/13 18:36:11 laddi Exp $
 * Created on 2.11.2004
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
import se.idega.idegaweb.commune.message.data.MessageHandlerInfo;
import se.idega.idegaweb.commune.message.data.PrintMessage;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.message.data.SystemArchivationMessage;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseCode;
import com.idega.block.process.message.business.MessageBusiness;
import com.idega.block.process.message.data.Message;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOService;
import com.idega.core.component.data.ICObject;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWUserContext;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * 
 *  Last modified: $Date: 2005/10/13 18:36:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface CommuneMessageBusiness extends IBOService, MessageBusiness {

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#deleteUserMessage
     */
    public void deleteUserMessage(int messageID)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#markMessageAsRead
     */
    public void markMessageAsRead(Message message)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#isMessageRead
     */
    public boolean isMessageRead(Message message)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getCaseCodeSystemArchivationMessage
     */
    public CaseCode getCaseCodeSystemArchivationMessage()
            throws RemoteException, FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getCaseCodeUserMessage
     */
    public CaseCode getCaseCodeUserMessage() throws RemoteException,
            FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getCaseCodePrintedLetterMessage
     */
    public CaseCode getCaseCodePrintedLetterMessage() throws RemoteException,
            FinderException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getUserMessage
     */
    public Message getUserMessage(int messageId) throws FinderException,
            RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getNumberOfMessages
     */
    public int getNumberOfMessages(User user) throws Exception,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getNumberOfNewMessages
     */
    public int getNumberOfNewMessages(User user) throws IDOException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getNumberOfMessages
     */
    public int getNumberOfMessages(User user, Collection groups)
            throws Exception, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#findMessages
     */
    public Collection findMessages(User user) throws Exception,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#findMessages
     */
    public Collection findMessages(User user, int numberOfEntries,
            int startingEntry) throws Exception, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#findMessages
     */
    public Collection findMessages(User user, Collection groups,
            int numberOfEntries, int startingEntry) throws Exception,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#findMessages
     */
    public Collection findMessages(Group group) throws Exception,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#findMessages
     */
    public Collection findMessages(Group group, int numberOfEntries,
            int startingEntry) throws Exception, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(User user, String subject, String body)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(User user, String subject, String body,
            boolean sendLetter) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(User user, String subject, Group handler,
            String body, boolean sendLetter) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(User user, String subject, Group handler,
            String body, boolean sendLetter, String contentCode)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(User receiver, String subject,
            String body, User sender, boolean sendLetter)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            String subject, String body, boolean sendLetter)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            String subject, String body, boolean sendLetter,
            boolean alwaysSendLetter) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            String subject, String body, String letterBody, boolean sendLetter,
            boolean alwaysSendLetter) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            User sender, String subject, String body, boolean sendLetter)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            User sender, Group handler, String subject, String body,
            boolean sendLetter) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            User sender, Group handler, String subject, String body,
            boolean pSendLetterIfNoEmail, String contentCode)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            User sender, Group handler, String subject, String body,
            boolean pSendLetterIfNoEmail, String contentCode,
            boolean alwaysSendLetter) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            User sender, Group handler, String subject, String body,
            String letterBody, boolean pSendLetterIfNoEmail,
            String contentCode, boolean alwaysSendLetter)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(Case parentCase, User receiver,
            User sender, Group handler, String subject, String body,
            String letterBody, boolean sendLetterIfNoEmail, String contentCode,
            boolean alwaysSendLetter, boolean sendMail)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(MessageValue msgValue)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createUserMessage
     */
    public Message createUserMessage(int userID, String subject, String body)
            throws CreateException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getPrintedLetterMessages
     */
    public Collection getPrintedLetterMessages() throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getPrintedLetterMessagesByType
     */
    public Collection getPrintedLetterMessagesByType(String type,
            int resultSize, int startingIndex) throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getPrintedLetterMessagesByType
     */
    public Collection getPrintedLetterMessagesByType(String type,
            IWTimestamp from, IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getSinglePrintedLetterMessagesByType
     */
    public Collection getSinglePrintedLetterMessagesByType(String type,
            IWTimestamp from, IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getUnPrintedLetterMessages
     */
    public Collection getUnPrintedLetterMessages() throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getUnPrintedLetterMessagesByType
     */
    public Collection getUnPrintedLetterMessagesByType(String type,
            int resultSize, int startingIndex) throws FinderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getUnPrintedLetterMessagesByType
     */
    public Collection getUnPrintedLetterMessagesByType(String type,
            IWTimestamp from, IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getSingleUnPrintedLetterMessagesByType
     */
    public Collection getSingleUnPrintedLetterMessagesByType(String type,
            IWTimestamp from, IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getSingleLettersByTypeAndStatus
     */
    public Collection getSingleLettersByTypeAndStatus(String type,
            String status, IWTimestamp from, IWTimestamp to, int resultSize,
            int startingIndex) throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getLettersByBulkFile
     */
    public Collection getLettersByBulkFile(int file, String type,
            String status, int resultSize, int startingIndex)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#flagPrintedLetterAsPrinted
     */
    public void flagPrintedLetterAsPrinted(User performer,
            PrintedLetterMessage message) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#flagMessageAsPrinted
     */
    public void flagMessageAsPrinted(User performer, Message message)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#flagMessageAsUnPrinted
     */
    public void flagMessageAsUnPrinted(User performer, Message message)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#flagMessageAsInactive
     */
    public void flagMessageAsInactive(User performer, Message message)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#flagMessagesAsInactive
     */
    public void flagMessagesAsInactive(User performer, String[] msgKeys)
            throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#flagMessageWithStatus
     */
    public void flagMessageWithStatus(User performer, Message message,
            String status) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#flagMessagesWithStatus
     */
    public void flagMessagesWithStatus(User performer, String[] msgKeys,
            String status) throws FinderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createPrintArchivationMessage
     */
    public Message createPrintArchivationMessage(User user, String subject,
            String body) throws CreateException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createPrintArchivationMessage
     */
    public Message createPrintArchivationMessage(int userID, String subject,
            String body) throws CreateException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createPrintArchivationMessage
     */
    public SystemArchivationMessage createPrintArchivationMessage(User forUser,
            User creator, String subject, String body, ICFile attatchement)
            throws CreateException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createPrintArchivationMessage
     */
    public SystemArchivationMessage createPrintArchivationMessage(
            int forUserID, int creatorUserID, String subject, String body,
            int attatchementFileID) throws CreateException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createPrintedPasswordLetterMessage
     */
    public PrintedLetterMessage createPrintedPasswordLetterMessage(User user,
            String subject, String body) throws CreateException,
            RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createPasswordMessage
     */
    public PrintedLetterMessage createPasswordMessage(User user,
            String username, String password) throws CreateException,
            RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createPrintedLetterMessage
     */
    public Message createPrintedLetterMessage(int userID, String subject,
            String body) throws CreateException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#sendMessage
     */
    public void sendMessage(String email, String subject, String body)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#sendMessage
     */
    public void sendMessage(String email, String subject, String body,
            File attachment) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getIfUserPreferesMessageByEmail
     */
    public boolean getIfUserPreferesMessageByEmail(User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getIfUserPreferesMessageInMessageBox
     */
    public boolean getIfUserPreferesMessageInMessageBox(User user)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getIfCanSendEmail
     */
    public boolean getIfCanSendEmail() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#setIfUserPreferesMessageByEmail
     */
    public void setIfUserPreferesMessageByEmail(User user, boolean preference)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#setIfUserPreferesMessageInMessageBox
     */
    public void setIfUserPreferesMessageInMessageBox(User user,
            boolean preference) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#sendMessageToCommuneAdministrators
     */
    public void sendMessageToCommuneAdministrators(String subject, String body)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#sendMessageToCommuneAdministrators
     */
    public void sendMessageToCommuneAdministrators(Case theCase,
            String subject, String body) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getMessageSession
     */
    public MessageSession getMessageSession(IWUserContext iwuc)
            throws IBOLookupException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#getBundleIdentifier
     */
    public String getBundleIdentifier() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#createMessageHandlerInfo
     */
    public MessageHandlerInfo createMessageHandlerInfo(
            MessagePdfHandler handler, ICObject ico) throws CreateException,
            RemoteException;

    /**
     * @see se.idega.idegaweb.commune.message.business.CommuneMessageBusinessBean#setMessageFile
     */
    public void setMessageFile(PrintMessage msg, boolean flagPrinted,
            User performer, ICFile file) throws java.rmi.RemoteException;

}
