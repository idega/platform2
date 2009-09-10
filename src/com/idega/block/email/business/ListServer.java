package com.idega.block.email.business;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import com.idega.core.contact.data.EmailDataView;

/**

 *  This class provides basic List Server type capabilities, that is, it
 *  retrieves emails for a specific user (e.g. listserv@host.com) broadcasts it
 *  to all email ids provided in the emailListFile. The emailList file contains
 *  one valid email id (user@host.com) per line.
 *
 * @author  Aron Birkir
 * @created    9. mars 2002

 */
public class ListServer {
	private final static String INBOX = "INBOX", POP_MAIL = "pop3", SMTP_MAIL = "smtp";
	private boolean debugOn = false;
	private String _smtpHost = null,
		_pop3Host = null,
		_user = null,
		_password = null,
		_emailListFile = null,
		_fromName = null;
	private InternetAddress[] _toList = null;
	/**
	 *  main() is used to start an instance of the ListServer
	 *
	 * @param  args           Description of the Parameter
	 * @exception  Exception  Description of the Exception
	 */
	public static void main(String args[]) throws Exception {
		//  check usage
		//
		if (args.length < 6) {
			System.err.println(
				"Usage: java ListServer SMTPHost POP3Host user password EmailListFile CheckPeriodFromName");
			System.exit(1);
		}
		//  Assign command line arguments to meaningful variable names
		//
		String smtpHost = args[0];
		//  Assign command line arguments to meaningful variable names
		//
		String pop3Host = args[1];
		//  Assign command line arguments to meaningful variable names
		//
		String user = args[2];
		//  Assign command line arguments to meaningful variable names
		//
		String password = args[3];
		//  Assign command line arguments to meaningful variable names
		//
		String emailListFile = args[4];
		//  Assign command line arguments to meaningful variable names
		//
		String fromName = null;
		int checkPeriod = Integer.parseInt(args[5]);
		if (args.length > 6) {
			fromName = args[6];
		}
		// Process every "checkPeriod" minutes
		//
		ListServer ls = new ListServer();
		ls.setDebug(false);
		while (true) {
			ls.debugMsg("SESSION START");
			ls.process(smtpHost, pop3Host, user, password, emailListFile, fromName);
			ls.debugMsg("SESSION END (Going to sleep for " + checkPeriod + " minutes)");
			Thread.sleep(checkPeriod * 1000 * 60);
		}
	}
	/**
	 *  process() checks for new messages and calls processMsg() for every new
	 *  message
	 *
	 * @param  smtpHost       Description of the Parameter
	 * @param  pop3Host       Description of the Parameter
	 * @param  user           Description of the Parameter
	 * @param  password       Description of the Parameter
	 * @param  emailListFile  Description of the Parameter
	 * @param  fromName       Description of the Parameter
	 * @exception  Exception  Description of the Exception
	 */
	public void process(
		String smtpHost,
		String pop3Host,
		String user,
		String password,
		String emailListFile,
		String fromName)
		throws Exception {
		this._smtpHost = smtpHost;
		this._pop3Host = pop3Host;
		this._user = user;
		this._password = password;
		this._emailListFile = emailListFile;
		if (fromName != null) {
			this._fromName = fromName;
		}
		// Read in email list file into java.util.Vector
		//
		Vector vList = new Vector(10);
		BufferedReader listFile = new BufferedReader(new FileReader(emailListFile));
		String line = null;
		while ((line = listFile.readLine()) != null) {
			vList.addElement(new InternetAddress(line));
		}
		listFile.close();
		debugMsg("Found " + vList.size() + " email ids in list");
		this._toList = new InternetAddress[vList.size()];
		vList.copyInto(this._toList);
		vList = null;
		//
		// Get individual emails and broadcast them to all email ids
		//
		// Get a Session object
		//
		Properties sysProperties = System.getProperties();
		Session session = Session.getDefaultInstance(sysProperties, null);
		session.setDebug(this.debugOn);
		// Connect to host
		//
		Store store = session.getStore(POP_MAIL);
		store.connect(pop3Host, -1, this._user, this._password);
		// Open the default folder
		//
		Folder folder = store.getDefaultFolder();
		if (folder == null) {
			throw new NullPointerException("No default mail folder");
		}
		folder = folder.getFolder(INBOX);
		if (folder == null) {
			throw new NullPointerException("Unable to get folder: " + folder);
		}
		// Get message count
		//
		folder.open(Folder.READ_WRITE);
		int totalMessages = folder.getMessageCount();
		if (totalMessages == 0) {
			debugMsg(folder + " is empty");
			folder.close(false);
			store.close();
			return;
		}
		// Get attributes & flags for all messages
		//
		Message[] messages = folder.getMessages();
		FetchProfile fp = new FetchProfile();
		fp.add(FetchProfile.Item.ENVELOPE);
		fp.add(FetchProfile.Item.FLAGS);
		fp.add("X-Mailer");
		folder.fetch(messages, fp);
		// Process each message
		//
		for (int i = 0; i < messages.length; i++) {
			if (!messages[i].isSet(Flags.Flag.SEEN)) {
				processMsg(smtpHost, messages[i]);
			}
			messages[i].setFlag(Flags.Flag.DELETED, true);
		}
		folder.close(true);
		store.close();
	}
	/**
	 *  processMsg() parses any newly received messages and calls sendMsg() to
	 *  broadcast the message
	 *
	 * @param  smtpHost       Description of the Parameter
	 * @param  message        Description of the Parameter
	 * @exception  Exception  Description of the Exception
	 */
	private void processMsg(String smtpHost, Message message) throws Exception {
		String replyTo = this._user;
		String subject;
		Date sentDate;
		Address[] a = null;
		// Get Headers (from, to, subject, date, etc.)
		//
		if ((a = message.getFrom()) != null) {
			replyTo = a[0].toString();
		}
		subject = message.getSubject();
		sentDate = message.getSentDate();
		// Send message
		//
		sendMsg(this._user, sentDate, replyTo, subject, message);
	}
	/**
	 *  sendMsg() broadcasts a message to all subscribers
	 *
	 * @param  from           Description of the Parameter
	 * @param  sentDate       Description of the Parameter
	 * @param  replyTo        Description of the Parameter
	 * @param  subject        Description of the Parameter
	 * @param  message        Description of the Parameter
	 * @exception  Exception  Description of the Exception
	 */
	private void sendMsg(String from, Date sentDate, String replyTo, String subject, Message message)
		throws Exception {
		// create some properties and get the default Session
		//
		Properties props = new Properties();
		props.put("mail.smtp.host", this._smtpHost);
		Session session = Session.getDefaultInstance(props, null);
		// create a message
		//
		Address replyToList[] = { new InternetAddress(replyTo)};
		Message newMessage = new MimeMessage(session);
		if (this._fromName != null) {
			newMessage.setFrom(new InternetAddress(from, this._fromName + " on behalf of " + replyTo));
		}
		else {
			newMessage.setFrom(new InternetAddress(from));
		}
		newMessage.setReplyTo(replyToList);
		newMessage.setRecipients(Message.RecipientType.BCC, this._toList);
		newMessage.setSubject(subject);
		newMessage.setSentDate(sentDate);
		// Set message contents
		//
		Object content = message.getContent();
		String debugText = "Subject: " + subject + ", Sent date: " + sentDate;
		if (content instanceof Multipart) {
			debugMsg("Sending Multipart message (" + debugText + ")");
			Multipart mp = (Multipart) message.getContent();
			mp.getContentType();
			newMessage.setContent((Multipart) message.getContent());
		}
		else {
			debugMsg("Sending Text message (" + debugText + ")");
			newMessage.setText((String) content);
		}
		// Send newMessage
		//
		Transport transport = session.getTransport(SMTP_MAIL);
		transport.connect(this._smtpHost, this._user, this._password);
		transport.sendMessage(newMessage, this._toList);
	}
	/**
	 *  @todo Description of the Method
	 *
	 * @param  from      Description of the Parameter
	 * @param  sentDate  Description of the Parameter
	 * @param  replyTo   Description of the Parameter
	 * @param  subject   Description of the Parameter
	 * @param  body      Description of the Parameter
	 */
	private void sendMsg(String from, Date sentDate, String replyTo, String subject, String body) throws Exception {
		// create some properties and get the default Session
		//
		Properties props = new Properties();
		props.put("mail.smtp.host", this._smtpHost);
		Session session = Session.getDefaultInstance(props, null);
		// create a message
		//
		Address replyToList[] = { new InternetAddress(replyTo)};
		Message newMessage = new MimeMessage(session);
		if (this._fromName != null) {
			newMessage.setFrom(new InternetAddress(from, this._fromName));
		}
		else {
			newMessage.setFrom(new InternetAddress(from));
		}
		newMessage.setReplyTo(replyToList);
		newMessage.setRecipients(Message.RecipientType.BCC, this._toList);
		newMessage.setSubject(subject);
		newMessage.setSentDate(sentDate);
		// Set message contents
		//
		newMessage.setText(body);
		// Send newMessage
		//
		Transport transport = session.getTransport(SMTP_MAIL);
		transport.connect(this._smtpHost, this._user, this._password);
		transport.sendMessage(newMessage, this._toList);
	}
	/**
	
	 * @param  s  Description of the Parameter
	 * @todo      Description of the Method
	 */
	private void debugMsg(String s) {
		if (this.debugOn) {
			System.out.println(new Date() + "> " + s);
		}
	}
	/**
	 *  Sets the debug attribute of the ListServer object
	 *
	 * @param  state  The new debug value
	 */
	public void setDebug(boolean state) {
		this.debugOn = state;
	}
	/**
	 *  @todo Description of the Method
	 *
	 * @param  letter  Description of the Parameter
	 * @param  smtp    Description of the Parameter
	 * @param  emails  Description of the Parameter
	 */
	public void sendMailLetter(EmailLetter letter, EmailAccount smtp, Collection emails) {
		try {
			this._smtpHost = smtp.getHost();
			this._user = smtp.getUser();
			this._password = smtp.getPassword();
			this._fromName = letter.getFromName();
			this._toList = new InternetAddress[emails.size()];
			Iterator iter = emails.iterator();
			Vector vList = new Vector(10);
			while (iter.hasNext()) {
				vList.addElement(new InternetAddress(((EmailDataView) iter.next()).getEmailAddress()));
			}
			vList.copyInto(this._toList);
			vList = null;
			sendMsg(
				letter.getFromAddress(),
				new Date(),
				letter.getFromAddress(),
				letter.getSubject(),
				letter.getBody());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
