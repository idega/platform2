/*
 * $Id: MessageBusinessBean.java,v 1.25 2002/12/31 13:52:32 aron Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.message.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.message.data.MessageHome;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessageHome;
import se.idega.idegaweb.commune.message.data.SystemArchivationMessage;
import se.idega.idegaweb.commune.message.data.SystemArchivationMessageHome;
import se.idega.idegaweb.commune.message.data.UserMessage;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.CaseCode;
import com.idega.business.IBORuntimeException;
import com.idega.core.data.Email;
import com.idega.core.data.ICFile;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWProperty;
import com.idega.idegaweb.IWPropertyList;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserProperties;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author Anders Lindman , <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class MessageBusinessBean extends com.idega.block.process.business.CaseBusinessBean implements MessageBusiness {

  private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	public static final String MESSAGE_PROPERTIES = "message_properties";
	private String TYPE_USER_MESSAGE = "SYMEDAN";
	private String TYPE_SYSTEM_PRINT_MAIL_MESSAGE = "SYMEBRV";
	private String TYPE_SYSTEM_PRINT_ARCHIVATION_MESSAGE = "SYMEARK";

	private static String DEFAULT_SMTP_MAILSERVER="mail.idega.is";
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
			return (MessageHome) this.getIDOHome(SystemArchivationMessage.class);
		}
		if (messageType.equals(TYPE_SYSTEM_PRINT_MAIL_MESSAGE)) {
			return getPrintedLetterMessageHome();
		}
		else {
			throw new java.lang.UnsupportedOperationException("MessageType " + messageType + " not yet implemented");
		}
	}
	
	protected PrintedLetterMessageHome getPrintedLetterMessageHome()throws RemoteException{
		try{
			return (PrintedLetterMessageHome) this.getIDOHome(PrintedLetterMessage.class);
		}
		catch(RemoteException rme){
			throw new IBORuntimeException(rme);	
		}
	}
	
	public void deleteMessage(String messageType, int messageID) throws FinderException,RemoveException,java.rmi.RemoteException {
		getMessageHome(messageType).findByPrimaryKey(new Integer(messageID)).remove();
	}
	
	public void deleteUserMessage(int messageID) throws FinderException,RemoveException,java.rmi.RemoteException {
		getUserMessage(messageID).remove();	
	}
	
	public void markMessageAsRead(Message message) throws RemoteException {
		message.setCaseStatus(this.getCaseStatusGranted());
		message.store();
	}
	
	public boolean isMessageRead(Message message) throws RemoteException {
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

	public Collection findMessages(User user) throws Exception {
		return getMessageHome(TYPE_USER_MESSAGE).findMessages(user);
	}

	public Message createUserMessage(User user, String subject, String body) {
		try {
			Message message = null;
			boolean sendMail = getIfUserPreferesMessageByEmail(user);
			boolean sendToBox = getIfUserPreferesMessageInMessageBox(user);
			
			if ( sendToBox ) {
				message = createMessage(getTypeUserMessage(), user, subject, body);
			}
			if ( sendMail ) {
				Email mail = ((UserBusiness)com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(),UserBusiness.class)).getUserMail(user);	
				if ( mail != null ) {
					sendMessage(mail.getEmailAddress(),subject,body);
				}
			}
			//return message;
			return message;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	public Message createUserMessage(int userID, String subject, String body) throws CreateException, RemoteException {
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
		Message message = createPrintedLetterMessage(user, subject, body,null);
		return message;
	}

	public Message createPrintedLetterMessage(int userID, String subject, String body) throws CreateException, RemoteException {
		User user;
		try {
			user = getUser(userID);
		}
		catch (FinderException fex) {
			throw new IDOCreateException(fex);
		}

		return createPrintedLetterMessage(user, subject, body);
	}
	
	/**	 * @return Collection of PrintedLetterMessage that have already been printed	 */
	public Collection getPrintedLetterMessages()throws FinderException{
		try{	
			return getPrintedLetterMessageHome().findAllPrintedLetters();	
		}
		catch(RemoteException e){
			throw new IBORuntimeException(e);
		}
	}
	
	/**
	 * @return Collection of PrintedLetterMessage that have already been printed
	 */
	public Collection getPrintedLetterMessagesByType(String type)throws FinderException{
		try{	
			return getPrintedLetterMessageHome().findPrintedLettersByType(type);	
		}
		catch(RemoteException e){
			throw new IBORuntimeException(e);
		}
	}
	
	/**
	 * @return Collection of PrintedLetterMessage that have already been printed, created between dates
	 */
	public Collection getPrintedLetterMessagesByType(String type,IWTimestamp from,IWTimestamp to)throws FinderException{
		try{	
			return getPrintedLetterMessageHome().findPrintedLettersByType(type,from,to);	
		}
		catch(RemoteException e){
			throw new IBORuntimeException(e);
		}
	}
	/**
	 * @return Collection of PrintedLetterMessage that have not been printed
	 */	
	public Collection getUnPrintedLetterMessages()throws FinderException{
		try{	
			return getPrintedLetterMessageHome().findAllUnPrintedLetters();	
		}
		catch(RemoteException e){
			throw new IBORuntimeException(e);
		}
	}
	
	/**
	 * @return Collection of PrintedLetterMessage that have not been printed
	 */	
	public Collection getUnPrintedLetterMessagesByType(String type)throws FinderException{
		try{	
			return getPrintedLetterMessageHome().findUnPrintedLettersByType(type);	
		}
		catch(RemoteException e){
			throw new IBORuntimeException(e);
		}
	}
	
	/**
	 * @return Collection of PrintedLetterMessage that have not been printed
	 */	
	public Collection getUnPrintedLetterMessagesByType(String type,IWTimestamp from,IWTimestamp to)throws FinderException{
		try{	
			return getPrintedLetterMessageHome().findUnPrintedLettersByType(type,from,to);	
		}
		catch(RemoteException e){
			throw new IBORuntimeException(e);
		}
	}
	
	/**
	 * Mark the status of the message so that it is printed.
	 * @param performer The User that makes the change
	 * @param message the message to be marked
	 */
	public void flagPrintedLetterAsPrinted(User performer,PrintedLetterMessage message)throws RemoteException{
		String newCaseStatus=getCaseStatusReady().getStatus();
		super.changeCaseStatus(message,newCaseStatus,performer);
	}
	
	public void flagMessageAsPrinted(User performer,Message message)throws RemoteException{
		String newCaseStatus=getCaseStatusReady().getStatus();
		super.changeCaseStatus(message,newCaseStatus,performer);
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
		MessageHome home = this.getMessageHome(messageType);
		Message message = home.create();
		message.setOwner(user);
		message.setSubject(subject);
		message.setBody(body);
		try {
			message.store();
		}
		catch (IDOStoreException idos) {
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
		PrintedLetterMessageHome home = (PrintedLetterMessageHome)this.getMessageHome(getTypeMailMessage());
		PrintedLetterMessage message = (PrintedLetterMessage)home.create();
		message.setOwner(user);
		message.setSubject(subject);
		message.setBody(body);
		if(printedLetterType!=null){
			message.setLetterType(printedLetterType);
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

		String mailServer = DEFAULT_SMTP_MAILSERVER;
		String fromAddress = DEFAULT_MESSAGEBOX_FROM_ADDRESS;
		try{
			IWBundle iwb = getIWApplicationContext().getApplication().getBundle(IW_BUNDLE_IDENTIFIER);
			mailServer = iwb.getProperty(PROP_SYSTEM_SMTP_MAILSERVER,DEFAULT_SMTP_MAILSERVER);
			fromAddress = iwb.getProperty(PROP_MESSAGEBOX_FROM_ADDRESS,DEFAULT_MESSAGEBOX_FROM_ADDRESS);
		}
		catch(Exception e){
			System.err.println("MessageBusinessBean: Error getting mail property from bundle");
			e.printStackTrace();	
		}
			

		try {
			com.idega.util.SendMail.send(fromAddress, email.trim(), "", "", mailServer, subject, body);
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

	public void setIfUserPreferesMessageByEmail(User user,boolean preference){
		IWPropertyList propertyList = getUserMessagePreferences(user);
		propertyList.setProperty(USER_PROP_SEND_TO_EMAIL, new Boolean(preference));
	}

	public void setIfUserPreferesMessageInMessageBox(User user,boolean preference){
		IWPropertyList propertyList = getUserMessagePreferences(user);
		propertyList.setProperty(USER_PROP_SEND_TO_MESSAGE_BOX, new Boolean(preference));
	}
	
	public void sendMessageToCommuneAdministrators(String subject, String body) throws RemoteException {
		try {
			Collection administrators = getCommuneUserBusiness().getAllCommuneAdministrators();
			if (!administrators.isEmpty()) {
				Iterator iterator = administrators.iterator();
				while (iterator.hasNext()) {
					User administrator = (User) iterator.next();
					createUserMessage(administrator,subject,body);
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

}