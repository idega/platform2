package se.idega.idegaweb.commune.message.data;
import com.idega.core.data.ICFile;
import com.idega.data.*;
import com.idega.block.process.data.*;
import com.idega.user.data.User;
import javax.ejb.*;
import java.util.Collection;
import java.util.Iterator;
import java.rmi.RemoteException;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class SystemArchivationMessageBMPBean extends AbstractCaseBMPBean implements SystemArchivationMessage, Message, Case
{
	private static final String COLUMN_SUBJECT = "SUBJECT";
	private static final String COLUMN_BODY = "BODY";
	private static final String COLUMN_MESSAGE_TYPE = "MESSAGE_TYPE";
	private static final String COLUMN_MESSAGE_DATA = "MESSAGE_DATA";
	private static final String COLUMN_ATTATCHED_FILE_ID = "ATTATCHED_FILE_ID";
	private static final String CASE_CODE_KEY = "SYMEARK";
	private static final String CASE_CODE_DESCRIPTION = "System Archivation Message";
	public String getEntityName()
	{
		return "MSG_SYSTEM_ARCH_MESSAGE";
	}
	public void initializeAttributes()
	{
		addGeneralCaseRelation();
		this.addAttribute(COLUMN_SUBJECT, "Message subject", String.class);
		this.addAttribute(COLUMN_BODY, "Message body", String.class, 1000);
		this.addAttribute(COLUMN_MESSAGE_TYPE, "Message type", String.class, 20);
		this.addManyToOneRelationship(COLUMN_MESSAGE_DATA, "Message data", ICFile.class);
		this.addManyToOneRelationship(COLUMN_ATTATCHED_FILE_ID, "Attatched file", ICFile.class);
		//this.addAttribute(COLUMN_DATE,"Test data column",String.class);//temp
		//this.addAttribute(COLUMN_SENDER,"Test data column",String.class);//temp
	}
	public String getCaseCodeKey()
	{
		return CASE_CODE_KEY;
	}
	public String getCaseCodeDescription()
	{
		return CASE_CODE_DESCRIPTION;
	}
	public void setSubject(String subject) throws java.rmi.RemoteException
	{
		this.setColumn(COLUMN_SUBJECT, subject);
	}
	public String getSubject() throws java.rmi.RemoteException
	{
		return this.getStringColumnValue(COLUMN_SUBJECT);
	}
	public void setBody(String body) throws java.rmi.RemoteException
	{
		this.setColumn(COLUMN_BODY, body);
	}
	public String getBody() throws java.rmi.RemoteException
	{
		return this.getStringColumnValue(COLUMN_BODY);
	}
	public String getMessageType() throws java.rmi.RemoteException
	{
		return this.getStringColumnValue(COLUMN_MESSAGE_TYPE);
	}
	public void setMessageType(String type) throws java.rmi.RemoteException
	{
		this.setColumn(COLUMN_MESSAGE_TYPE, type);
	}
	public ICFile getMessageData() throws java.rmi.RemoteException
	{
		return (ICFile) this.getColumnValue(COLUMN_MESSAGE_DATA); //Replace this later
	}
	public int getMessageDataFileID() throws java.rmi.RemoteException
	{
		return this.getIntColumnValue(COLUMN_MESSAGE_DATA);
	}
	public void setMessageData(ICFile file) throws java.rmi.RemoteException
	{ //Temp (test) method
		this.setColumn(COLUMN_MESSAGE_DATA, file);
	}
	public void setMessageData(int fileID) throws java.rmi.RemoteException
	{ //Temp (test) method
		this.setColumn(COLUMN_MESSAGE_DATA, fileID);
	}
	public void setAttatchedFile(ICFile file) throws java.rmi.RemoteException
	{ //Temp (test) method
		this.setColumn(COLUMN_ATTATCHED_FILE_ID, file);
	}
	public void setAttatchedFile(int fileID) throws java.rmi.RemoteException
	{ //Temp (test) method
		this.setColumn(COLUMN_ATTATCHED_FILE_ID, fileID);
	}
	public ICFile getAttatchedFile() throws java.rmi.RemoteException
	{
		return (ICFile) this.getColumnValue(COLUMN_ATTATCHED_FILE_ID); //Replace this later
	}
	public int getAttatchedFileID() throws java.rmi.RemoteException
	{
		return this.getIntColumnValue(COLUMN_ATTATCHED_FILE_ID);
	}
	public String getSenderName()
	{
		try
		{
			return getOwner().getName();
		}
		catch (RemoteException e)
		{
			return "";
		}
	}
	public String getDateString()
	{
		/**
		 * @todo: implement
		 */
		return "";
	}
	public Collection ejbFindMessages(User user) throws FinderException, java.rmi.RemoteException
	{
		return super.ejbFindAllCasesByUser(user);
	}
}
