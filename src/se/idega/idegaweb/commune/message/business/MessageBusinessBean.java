/*
 * $Id: MessageBusinessBean.java,v 1.62 2004/05/20 12:53:08 laddi Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.message.business;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.message.data.MessageHandlerInfo;
import se.idega.idegaweb.commune.message.data.MessageHandlerInfoHome;
import se.idega.idegaweb.commune.message.data.MessageHome;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessageHome;
import se.idega.idegaweb.commune.message.data.SystemArchivationMessage;
import se.idega.idegaweb.commune.message.data.SystemArchivationMessageHome;
import se.idega.idegaweb.commune.message.data.UserMessage;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseCode;
import com.idega.business.IBORuntimeException;
import com.idega.core.component.data.ICObject;
import com.idega.core.contact.data.Email;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOException;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWPropertyList;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserProperties;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author Anders Lindman , <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class MessageBusinessBean extends com.idega.block.process.business.CaseBusinessBean implements MessageBusiness {

  private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	public static final String MESSAGE_PROPERTIES = "message_properties";
	public static final String MAIL_PROPERTIES = "mail_properties";
	private String TYPE_USER_MESSAGE = "SYMEDAN";
	private String TYPE_SYSTEM_PRINT_MAIL_MESSAGE = "SYMEBRV";
	private String TYPE_SYSTEM_PRINT_ARCHIVATION_MESSAGE = "SYMEARK";

	private static String DEFAULT_SMTP_MAILSERVER="mail.agurait.com";
	private static String PROP_SYSTEM_SMTP_MAILSERVER="messagebox_smtp_mailserver";
	private static String PROP_MESSAGEBOX_FROM_ADDRESS="messagebox_from_mailaddress";
	private static String DEFAULT_MESSAGEBOX_FROM_ADDRESS="messagebox@idega.com";
	
	public static final String USER_PROP_SEND_TO_MESSAGE_BOX = "msg_send_box";
	public static final String USER_PROP_SEND_TO_EMAIL = "msg_send_email";

	public MessageBusinessBean() {
	}

	private MessageHome getMessageHome(String messageType) throws java.rmi.RemoteException {
//		System.out.println("Getting MessageHome for messageType = " + messageType);
		if (messageType.equals(TYPE_USER_MESSAGE)) {
			return (MessageHome) this.getIDOHome(UserMessage.class);
		}
		if (messageType.equals(TYPE_SYSTEM_PRINT_ARCHIVATION_MESSAGE)) {
			return getSystemArchivationMessageHome();
		}
		if (messageType.equals(TYPE_SYSTEM_PRINT_MAIL_MESSAGE)) {
			return getPrintedLetterMessageHome();
		}
		else {
			throw new java.lang.UnsupportedOperationException("MessageType " + messageType + " not yet implemented");
		}
	}
	
	protected PrintedLetterMessageHome getPrintedLetterMessageHome(){
		try{
			return (PrintedLetterMessageHome) this.getIDOHome(PrintedLetterMessage.class);
		}
		catch(RemoteException rme){
			throw new IBORuntimeException(rme);	
		}
	}
	
	protected SystemArchivationMessageHome getSystemArchivationMessageHome(){
		try{
			return (SystemArchivationMessageHome) this.getIDOHome(SystemArchivationMessage.class);
		}
		catch(RemoteException rme){
			throw new IBORuntimeException(rme);	
		}
	}
	
	public void deleteMessage(String messageType, int messageID) throws FinderException,RemoveException,java.rmi.RemoteException {
		getMessageHome(messageType).findByPrimaryKey(new Integer(messageID)).remove();
	}
	
	public void deleteUserMessage(int messageID) {
		try {
			Message message = getUserMessage(messageID);
			changeCaseStatus(message, getCaseStatusInactive().getPrimaryKey().toString(), message.getOwner());	
		}
		catch (FinderException fe) {
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	public void markMessageAsRead(Message message) {
		try {
			changeCaseStatus(message, getCaseStatusGranted().getPrimaryKey().toString(), message.getOwner());	
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	public boolean isMessageRead(Message message) {
		if ( (message.getCaseStatus()).equals(getCaseStatusGranted()) )
			return true;
		return false;
	}

	private String getTypeUserMessage() {
		return TYPE_USER_MESSAGE;
	}
	
	private String getTypeMailMessage() {
		return TYPE_SYSTEM_PRINT_MAIL_MESSAGE;
	}
	
	private String getTypeArchivationMessage() {
		return TYPE_SYSTEM_PRINT_ARCHIVATION_MESSAGE;
	}
	
	public CaseCode getCaseCodeSystemArchivationMessage()throws RemoteException,FinderException{
		return getCaseBusiness().getCaseCode(getTypeArchivationMessage());	
	}
	
	public CaseCode getCaseCodeUserMessage()throws RemoteException,FinderException{
		return getCaseBusiness().getCaseCode(getTypeUserMessage());	
	}
	
	public CaseCode getCaseCodePrintedLetterMessage()throws RemoteException,FinderException{
		return getCaseBusiness().getCaseCode(getTypeMailMessage());	
	}

	protected CaseBusiness getCaseBusiness(){
		return this;	
	}

	public Message getMessage(String messageType, int messageId) throws FinderException, RemoteException {
		return getMessageHome(messageType).findByPrimaryKey(new Integer(messageId));
	}

	public Message getUserMessage(int messageId) throws FinderException, RemoteException {
		return getMessage(getTypeUserMessage(), messageId);
	}

	public int getNumberOfMessages(User user) throws Exception {
		String[] validStatuses = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus() };
		return getMessageHome(TYPE_USER_MESSAGE).getNumberOfMessages(user,validStatuses);
	}
	
	public int getNumberOfNewMessages(User user) throws IDOException {
		try {
			String[] validStatuses = { getCaseStatusOpen().getStatus() };
			return getMessageHome(TYPE_USER_MESSAGE).getNumberOfMessages(user,validStatuses);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public int getNumberOfMessages(User user, Collection groups) throws Exception {
		String[] validStatuses = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus() };
		return getMessageHome(TYPE_USER_MESSAGE).getNumberOfMessages(user,groups,validStatuses);
	}
	

	public Collection findMessages(User user) throws Exception {
		String[] validStatuses = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus() };
		return getMessageHome(TYPE_USER_MESSAGE).findMessages(user,validStatuses);
	}
	
	public Collection findMessages(User user, int numberOfEntries, int startingEntry) throws Exception {
		String[] validStatuses = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus() };
		return getMessageHome(TYPE_USER_MESSAGE).findMessages(user,validStatuses,numberOfEntries,startingEntry);
	}
	
	public Collection findMessages(User user, Collection groups, int numberOfEntries, int startingEntry) throws Exception {
		String[] validStatuses = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus() };
		return getMessageHome(TYPE_USER_MESSAGE).findMessages(user,groups,validStatuses,numberOfEntries,startingEntry);
	}
	
	public Collection findMessages(Group group) throws Exception {
		String[] validStatuses = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus() };
		return getMessageHome(TYPE_USER_MESSAGE).findMessages(group, validStatuses);
	}	

	public Collection findMessages(Group group, int numberOfEntries, int startingEntry) throws Exception {
		String[] validStatuses = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus() };
		return getMessageHome(TYPE_USER_MESSAGE).findMessages(group,validStatuses,numberOfEntries,startingEntry);
	}
	
	public Message createUserMessage(User user, String subject, String body) {
		return createUserMessage(null, user, subject, body, true);
	}
	
	public Message createUserMessage(User user, String subject, String body, boolean sendLetter) {
		return createUserMessage(null, user, subject, body, sendLetter);
	}
	
	public Message createUserMessage(User user, String subject, Group handler, String body, boolean sendLetter) {
		return createUserMessage(null, user, null, handler, subject, body, sendLetter);
	}	
	
	public Message createUserMessage(User user, String subject, Group handler, String body, boolean sendLetter,String contentCode) {
		return createUserMessage(null, user, null, handler, subject, body, sendLetter,contentCode, false);
	}	
	
	public Message createUserMessage(User receiver, String subject, String body, User sender, boolean sendLetter) {
		return createUserMessage(null, receiver, sender, subject, body, sendLetter);
	}
	
	public Message createUserMessage(Case parentCase, User receiver, String subject, String body, boolean sendLetter) {
		return createUserMessage(parentCase, receiver, null, subject, body, sendLetter);
	}
	
	public Message createUserMessage(Case parentCase, User receiver, String subject, String body, boolean sendLetter, boolean alwaysSendLetter) {
		return createUserMessage(parentCase, receiver, null, null, subject, body, sendLetter, null, alwaysSendLetter);
	}
	
	public Message createUserMessage(Case parentCase, User receiver, String subject, String body, String letterBody, boolean sendLetter, boolean alwaysSendLetter) {
		return createUserMessage(parentCase, receiver, null, null, subject, body, letterBody, sendLetter, null, alwaysSendLetter);
	}
	
	public Message createUserMessage(Case parentCase, User receiver, User sender, String subject, String body, boolean sendLetter) {
		return createUserMessage(parentCase, receiver, sender, null, subject, body, sendLetter);	
	}
	
	public Message createUserMessage(Case parentCase, User receiver, User sender, Group handler, String subject, String body, boolean sendLetter) {
		return createUserMessage(parentCase, receiver,sender,handler,subject,body,sendLetter,null, false);
	}
	public Message createUserMessage(Case parentCase, User receiver, User sender, Group handler, String subject, String body, boolean pSendLetterIfNoEmail,String contentCode) {
		return createUserMessage(parentCase, receiver,sender,handler,subject,body,pSendLetterIfNoEmail,contentCode, false);
	}

	public Message createUserMessage(Case parentCase, User receiver, User sender, Group handler, String subject, String body, boolean pSendLetterIfNoEmail,String contentCode, boolean alwaysSendLetter) {
		return createUserMessage(parentCase, receiver, sender, handler, subject, body, body, pSendLetterIfNoEmail, contentCode, alwaysSendLetter);
	}
	
	public Message createUserMessage(Case parentCase, User receiver, User sender, Group handler, String subject, String body, String letterBody, boolean pSendLetterIfNoEmail,String contentCode, boolean alwaysSendLetter) {
		return createUserMessage(parentCase, receiver, sender, handler, subject, body, letterBody, pSendLetterIfNoEmail, contentCode, alwaysSendLetter, true); 
	}
	
	public Message createUserMessage(Case parentCase, User receiver, User sender, Group handler, String subject, String body, String letterBody, boolean pSendLetterIfNoEmail,String contentCode, boolean alwaysSendLetter, boolean sendEMail) {
		try {
			if (letterBody == null) {
				letterBody = body;
			}
			
			Message message = null;
			boolean sendMail = getIfUserPreferesMessageByEmail(receiver) && sendEMail;
			boolean sendToBox = getIfUserPreferesMessageInMessageBox(receiver);
			boolean canSendEmail = getIfCanSendEmail();
			boolean sendLetterEvenWhenHavingEmail=getIfCreateLetterMessageHavingEmail();
			//By default: copies in-parameter value:
			boolean doSendLetter=pSendLetterIfNoEmail;
			
			if (sendToBox) {
				message = createMessage(getTypeUserMessage(), receiver, sender, handler, subject, body, parentCase);
			}
			
			if (sendMail) {
				boolean sendEmail = false;
				Email mail = ((UserBusiness)com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(),UserBusiness.class)).getUserMail(receiver);	
				if (mail != null) {
					if (mail.getEmailAddress() != null)
						sendEmail = true;
				}
				
				if ( sendEmail ) {
					if (canSendEmail)
						try {
							sendMessage(mail.getEmailAddress(),subject,body);
							if(!sendLetterEvenWhenHavingEmail){
								//do not send message letter when having email address
								doSendLetter=false;
							}
						}
						catch (Exception e) {
							System.err.println("Couldn't send message to user via e-mail.");
						}
				}
				//else {
				//	if (pSendLetterIfNoEmail)
				//		createPrintedLetterMessage(parentCase, receiver, subject, body,null,contentCode);
				//}
				
				
				
			}
			//else {
			//	if (pSendLetterIfNoEmail)
			//		createPrintedLetterMessage(parentCase, receiver, subject, body,null,contentCode);
			//}
			if (alwaysSendLetter) {
				doSendLetter = true;
			}
			if(doSendLetter){
				createPrintedLetterMessage(parentCase, receiver, subject, letterBody, null, contentCode);
			}

			if (IWMainApplication.isDebugActive()) {
				System.out.println("[MessageBusiness] Creating user message with subject:" + subject);
				System.out.println("[MessageBusiness] Body: " + body);
				if (parentCase != null)
					System.out.println("[MessageBusiness] Parent case:" + parentCase.getClass().getName() + " (" + parentCase.getPrimaryKey().toString() + ")");
				System.out.println("[MessageBusiness] Receiver: " + receiver.getName() + " (" + receiver.getPrimaryKey().toString() + ")");
				if (sender != null)
					System.out.println("[MessageBusiness] Sender: " + sender.getName() + " (" + sender.getPrimaryKey().toString() + ")");
				if (handler != null)
					System.out.println("[MessageBusiness] Handler: " + handler.getName() + " (" + handler.getPrimaryKey().toString() + ")");
			}

			//return message;
			return message;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}
	
	/**
	 * This property is for setting if to create letter messages even when the user has an email address.
	 * @return value of the set property. Default is false.
	 */
	protected boolean getIfCreateLetterMessageHavingEmail(){
		return getBundle().getBooleanProperty("create_letter_message_having_email",false);
	}

	public Message createUserMessage(int userID, String subject, String body) throws CreateException {
		User user;
		try {
			user = getUser(userID);
		}
		catch (FinderException fex) {
			throw new IDOCreateException(fex);
		}

		return createUserMessage(user, subject, body);
	}

	public Message createPrintedLetterMessage(User user, String subject, String body) throws CreateException, RemoteException {
		Message message = createPrintedLetterMessage(null, user, subject, body, null);
		return message;
	}

	private Message createPrintedLetterMessage(Case parentCase, User user, String subject, String body) throws CreateException, RemoteException {
		Message message = createPrintedLetterMessage(parentCase, user, subject, body, null);
		return message;
	}

	public Message createPrintedLetterMessage(int userID, String subject, String body) throws CreateException, RemoteException {
		return createPrintedLetterMessage(null, userID, subject, body);
	}

	private Message createPrintedLetterMessage(Case parentCase, int userID, String subject, String body) throws CreateException, RemoteException {
		User user;
		try {
			user = getUser(userID);
		}
		catch (FinderException fex) {
			throw new IDOCreateException(fex);
		}

		return createPrintedLetterMessage(parentCase, user, subject, body);
	}
	
	/**	 * @return Collection of PrintedLetterMessage that have already been printed	 */
	public Collection getPrintedLetterMessages()throws FinderException{
		return getPrintedLetterMessageHome().findAllPrintedLetters();	
	}
	
	/**
	 * @return Collection of PrintedLetterMessage that have already been printed
	 */
	public Collection getPrintedLetterMessagesByType(String type)throws FinderException{
		return getPrintedLetterMessageHome().findPrintedLettersByType(type);	
	}
	
	/**
	 * @return Collection of PrintedLetterMessage that have already been printed, created between dates
	 */
	public Collection getPrintedLetterMessagesByType(String type,IWTimestamp from,IWTimestamp to)throws FinderException{
		return getPrintedLetterMessageHome().findPrintedLettersByType(type,from,to);	
	}
	
	public Collection getSinglePrintedLetterMessagesByType(String type,IWTimestamp from,IWTimestamp to)throws FinderException{
		return getPrintedLetterMessageHome().findSinglePrintedLettersByType(type,from,to);	
	}
	/**
	 * @return Collection of PrintedLetterMessage that have not been printed
	 */	
	public Collection getUnPrintedLetterMessages()throws FinderException{
		return getPrintedLetterMessageHome().findAllUnPrintedLetters();	
	}
	
	/**
	 * @return Collection of PrintedLetterMessage that have not been printed
	 */	
	public Collection getUnPrintedLetterMessagesByType(String type)throws FinderException{
		return getPrintedLetterMessageHome().findUnPrintedLettersByType(type);	
	}
	
	/**
	 * @return Collection of PrintedLetterMessage that have not been printed
	 */	
	public Collection getUnPrintedLetterMessagesByType(String type,IWTimestamp from,IWTimestamp to)throws FinderException{
		return getPrintedLetterMessageHome().findUnPrintedLettersByType(type,from,to);	
	}
	
	public Collection getSingleUnPrintedLetterMessagesByType(String type,IWTimestamp from,IWTimestamp to)throws FinderException{
		return getPrintedLetterMessageHome().findSingleUnPrintedLettersByType(type,from,to);	
	}
		
	public Collection getSingleLettersByTypeAndStatus(String type,String status,IWTimestamp from,IWTimestamp to)throws FinderException{
		return getPrintedLetterMessageHome().findSingleByTypeAndStatus(type,status,from,to);	
	}
	
	public Collection getLettersByBulkFile(int file, String type , String status)throws FinderException{
		return getPrintedLetterMessageHome().findByBulkFile(file,type,status);	
	}
	
	/**
	 * Mark the status of the message so that it is printed.
	 * @param performer The User that makes the change
	 * @param message the message to be marked
	 */
	public void flagPrintedLetterAsPrinted(User performer,PrintedLetterMessage message) {
		String newCaseStatus=getCaseStatusReady().getStatus();
		super.changeCaseStatus(message,newCaseStatus,performer);
	}
	
	public void flagMessageAsPrinted(User performer,Message message) {
		String newCaseStatus=getCaseStatusReady().getStatus();
		super.changeCaseStatus(message,newCaseStatus,performer);
	}
	
	public void flagMessageAsUnPrinted(User performer,Message message) {
		String newCaseStatus=getCaseStatusOpen().getStatus();
		super.changeCaseStatus(message,newCaseStatus,performer);
	}
	
	public void flagMessageAsInactive(User performer,Message message) {
		String newCaseStatus=getCaseStatusInactive().getStatus();
		super.changeCaseStatus(message,newCaseStatus,performer);
	}
	
	public void flagMessagesAsInactive(User performer, String[] msgKeys)throws FinderException{
		String newCaseStatus=getCaseStatusInactive().getStatus();
		flagMessagesWithStatus(performer,msgKeys,newCaseStatus);
	}
	
	public void  flagMessageWithStatus(User performer,Message message,String status) {
		super.changeCaseStatus(message,status,performer);
	}
	
	public void flagMessagesWithStatus(User performer, String[] msgKeys,String status)throws FinderException{
			for (int i = 0; i < msgKeys.length; i++) {
				super.changeCaseStatus( Integer.parseInt(msgKeys[i]),status,performer);
			}
		}
	

	public Message createPrintArchivationMessage(User user, String subject, String body) throws CreateException, RemoteException {
		Message message = createMessage(getTypeArchivationMessage(), user, subject, body);
		return message;
	}

	public Message createPrintArchivationMessage(int userID, String subject, String body) throws CreateException, RemoteException {
		User user;
		try {
			user = getUser(userID);
		}
		catch (FinderException fex) {
			throw new IDOCreateException(fex);
		}

		return createPrintArchivationMessage(user, subject, body);
	}

	public SystemArchivationMessage createPrintArchivationMessage(User forUser, User creator, String subject, String body,ICFile attatchement) throws CreateException {
		int forUserID=-1;
		int creatorUserID=-1;
		int fileID=-1;
		try{
			forUserID=((Integer)forUser.getPrimaryKey()).intValue();
			creatorUserID=((Integer)creator.getPrimaryKey()).intValue();
			fileID=((Integer)attatchement.getPrimaryKey()).intValue();
		}
		catch(Exception e){
			throw new IDOCreateException(e);	
		}
		return createPrintArchivationMessage(forUserID,creatorUserID,subject,body,fileID);
	}

	public SystemArchivationMessage createPrintArchivationMessage(int forUserID, int creatorUserID, String subject, String body,int attatchementFileID) throws CreateException {

		/**
		 * @todo implement support for creator
		 */
		try{
			Message message = createMessage(getTypeArchivationMessage(), forUserID, subject, body);
			SystemArchivationMessage saMessage = (SystemArchivationMessage)message;
			saMessage.setAttachedFile(attatchementFileID);
			message.store();
			return saMessage;
		}
		catch(Exception e){
			throw new IDOCreateException(e);	
		}

	}

	private Message createMessage(String messageType, int userID, String subject, String body) throws CreateException, RemoteException {
		User user;
		try {
			user = getUser(userID);
		}
		catch (FinderException fex) {
			throw new IDOCreateException(fex);
		}

		return createMessage(messageType, user, subject, body);
	}

	private Message createMessage(String messageType, User user, String subject, String body) throws CreateException, RemoteException {
		return createMessage(messageType, user, subject, body, null);
	}
	
	private Message createMessage(String messageType, User user, String subject, String body, Case parentCase) throws CreateException, RemoteException {
		return createMessage(messageType, user, null, subject, body, parentCase);
	}

	private Message createMessage(String messageType, User receiver, User sender, String subject, String body, Case parentCase) throws CreateException, RemoteException {
		return createMessage(messageType, receiver, sender, null, subject, body, parentCase);
	}
	
	private Message createMessage(String messageType, User receiver, User sender, Group handler, String subject, String body, Case parentCase) throws CreateException, RemoteException {
		MessageHome home = this.getMessageHome(messageType);
		Message message = home.create();
		message.setOwner(receiver);
		if (sender != null){
			message.setSender(sender);
		}
		if (handler != null){
			message.setHandler(handler);
		}
		message.setSubject(subject);
		message.setBody(body);
		if (parentCase != null){
			message.setParentCase(parentCase);
		}
		
		try {
			message.store();
		} catch (IDOStoreException idos) {
			throw new IDOCreateException(idos);
		}
		return message;
	}

	public PrintedLetterMessage createPrintedPasswordLetterMessage(User user, String subject, String body) throws CreateException, RemoteException {
		PrintedLetterMessageHome home = (PrintedLetterMessageHome)this.getMessageHome(getTypeMailMessage());
		PrintedLetterMessage message = (PrintedLetterMessage)home.create();
		message.setOwner(user);
		message.setSubject(subject);
		message.setBody(body);
		message.setAsPasswordLetter();
		try {
			message.store();
		}
		catch (IDOStoreException idos) {
			throw new IDOCreateException(idos);
		}
		return message;
	}
	
	public PrintedLetterMessage createPasswordMessage(User user, String username,String password) throws CreateException, RemoteException {
		PrintedLetterMessageHome home = (PrintedLetterMessageHome)this.getMessageHome(getTypeMailMessage());
		PrintedLetterMessage message = (PrintedLetterMessage)home.create();
		message.setOwner(user);
		message.setSubject("Username and Password");
		message.setBody(username+"|"+password);
		message.setAsPasswordLetter();
		try {
			message.store();
		}
		catch (IDOStoreException idos) {
			throw new IDOCreateException(idos);
		}
		return message;
	}

	public PrintedLetterMessage createPrintedLetterMessage(User user, String subject, String body,String printedLetterType) throws CreateException, RemoteException {
		return createPrintedLetterMessage(null, user, subject, body, printedLetterType);
	}
	private PrintedLetterMessage createPrintedLetterMessage(Case parentCase, User user, String subject, String body,String printedLetterType) throws CreateException, RemoteException {
		return createPrintedLetterMessage(parentCase,user,subject,body,printedLetterType,null);
	}
	private PrintedLetterMessage createPrintedLetterMessage(Case parentCase, User user, String subject, String body,String printedLetterType,String contentCode) throws CreateException, RemoteException {
		PrintedLetterMessageHome home = (PrintedLetterMessageHome)this.getMessageHome(getTypeMailMessage());
		PrintedLetterMessage message = (PrintedLetterMessage)home.create();
		message.setOwner(user);
		message.setSubject(subject);
		message.setBody(body);
		if (parentCase != null)
			message.setParentCase(parentCase);
		if(printedLetterType!=null){
			message.setLetterType(printedLetterType);
		}
		if(contentCode!=null){
			message.setContentCode(contentCode);
		}
		try {
			message.store();
		}
		catch (IDOStoreException idos) {
			throw new IDOCreateException(idos);
		}
		return message;
	}

	public void sendMessage(String email, String subject, String body) {
		sendMessage(email, subject, body, null);
	}
	
	public void sendMessage(String email, String subject, String body, File attachment) {

		String mailServer = DEFAULT_SMTP_MAILSERVER;
		String fromAddress = DEFAULT_MESSAGEBOX_FROM_ADDRESS;
		try{
			IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
			mailServer = iwb.getProperty(PROP_SYSTEM_SMTP_MAILSERVER,DEFAULT_SMTP_MAILSERVER);
			fromAddress = iwb.getProperty(PROP_MESSAGEBOX_FROM_ADDRESS,DEFAULT_MESSAGEBOX_FROM_ADDRESS);
		}
		catch(Exception e){
			System.err.println("MessageBusinessBean: Error getting mail property from bundle");
			e.printStackTrace();	
		}
			

		try {
			if (attachment == null) {
				com.idega.util.SendMail.send(fromAddress, email.trim(), "", "", mailServer, subject, body);
			} else {
				com.idega.util.SendMail.send(fromAddress, email.trim(), "", "", mailServer, subject, body, attachment);
			}
		}
		catch (javax.mail.MessagingException me) {
			System.err.println("Error sending mail to address: " + email + " Message was: " + me.getMessage());
		}
	}


	protected UserProperties getUserPreferences(User user) throws Exception {
		UserProperties property = getCommuneUserBusiness().getUserProperties(user);	
		return property;
	}
	
	protected IWPropertyList getUserMessagePreferences(User user) {
		try{
			IWPropertyList messageProperties = getUserPreferences(user).getProperties(MESSAGE_PROPERTIES);
			return messageProperties;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public boolean getIfUserPreferesMessageByEmail(User user){
		IWPropertyList propertyList = getUserMessagePreferences(user);
		
		if (propertyList != null) {
			String property = propertyList.getProperty(USER_PROP_SEND_TO_EMAIL);
			if(property!=null)
				return Boolean.valueOf(property).booleanValue();
		}
		return true;
	}

	public boolean getIfUserPreferesMessageInMessageBox(User user){
		IWPropertyList propertyList = getUserMessagePreferences(user);
		if (propertyList != null) {
			String property = propertyList.getProperty(USER_PROP_SEND_TO_MESSAGE_BOX);
			if(property!=null)
				return Boolean.valueOf(property).booleanValue();
		}
		return true;
	}
	
	public boolean getIfCanSendEmail() {
		boolean canSend = false;
		IWPropertyList propertyList = getIWApplicationContext().getSystemProperties().getProperties("mail_properties");
		if (propertyList != null) {
			String property = propertyList.getProperty("can_send_email");
			if (property != null)
				canSend = Boolean.valueOf(property).booleanValue();
		}
		return canSend;
	}

	public void setIfUserPreferesMessageByEmail(User user,boolean preference){
		IWPropertyList propertyList = getUserMessagePreferences(user);
		propertyList.setProperty(USER_PROP_SEND_TO_EMAIL, new Boolean(preference));
	}

	public void setIfUserPreferesMessageInMessageBox(User user,boolean preference){
		IWPropertyList propertyList = getUserMessagePreferences(user);
		propertyList.setProperty(USER_PROP_SEND_TO_MESSAGE_BOX, new Boolean(preference));
	}
	
	public void sendMessageToCommuneAdministrators(String subject, String body) throws RemoteException {
		sendMessageToCommuneAdministrators(null, subject, body);
	}
	
	public void sendMessageToCommuneAdministrators(Case theCase, String subject, String body) throws RemoteException {
		try {
			Collection administrators = getCommuneUserBusiness().getAllCommuneAdministrators();
			if (!administrators.isEmpty()) {
				Iterator iterator = administrators.iterator();
				while (iterator.hasNext()) {
					User administrator = (User) iterator.next();
					createUserMessage(theCase,administrator,subject,body,false);
				}
			}
		}
		catch (FinderException fe) {
			fe.printStackTrace();		
		}
	}
	
	private CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) getServiceInstance(CommuneUserBusiness.class);
	}
	
	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public MessageHandlerInfo createMessageHandlerInfo(MessagePdfHandler handler,ICObject ico) throws CreateException, RemoteException{
		MessageHandlerInfoHome mhhome= (MessageHandlerInfoHome)getIDOHome(MessageHandlerInfo.class);
		MessageHandlerInfo info = mhhome.create();
		info.setHandlerCode(handler.getHandlerCode());
		info.setICObject(ico);
		info.store();
		return info;
	}

}
