package se.idega.idegaweb.commune.message.data;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOException;
import com.idega.data.query.SelectQuery;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class SystemArchivationMessageBMPBean extends AbstractCaseBMPBean implements SystemArchivationMessage, PrintMessage, Case
{
	private static final String COLUMN_SUBJECT = "SUBJECT";
	private static final String COLUMN_BODY = "BODY";
	private static final String COLUMN_MESSAGE_TYPE = "MESSAGE_TYPE";
	private static final String COLUMN_MESSAGE_DATA = "MESSAGE_DATA";
	private static final String COLUMN_ATTATCHED_FILE_ID = "ATTATCHED_FILE_ID";
	private static final String COLUMN_BULK_DATA = "BULK_DATA";
	private static final String COLUMN_CONTENT_CODE = "CONTENT_CODE";
	private static final String CASE_CODE_KEY = "SYMEARK";
	private static final String CASE_CODE_DESCRIPTION = "System Archivation Message";
	public static final String PRINT_TYPE = "ARCH";
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
		this.addAttribute(COLUMN_CONTENT_CODE, "Message content code", String.class, 20);
		this.addManyToOneRelationship(COLUMN_MESSAGE_DATA, "Message data", ICFile.class);
		this.addManyToOneRelationship(COLUMN_ATTATCHED_FILE_ID, "Attatched file", ICFile.class);
		//this.addAttribute(COLUMN_DATE,"Test data column",String.class);//temp
		//this.addAttribute(COLUMN_SENDER,"Test data column",String.class);//temp
		this.addManyToOneRelationship(COLUMN_BULK_DATA, "Message bulk data", ICFile.class);
	}
	public String getCaseCodeKey()
	{
		return CASE_CODE_KEY;
	}
	public String getCaseCodeDescription()
	{
		return CASE_CODE_DESCRIPTION;
	}
	public void setSubject(String subject)
	{
		this.setColumn(COLUMN_SUBJECT, subject);
	}
	public String getSubject()
	{
		return this.getStringColumnValue(COLUMN_SUBJECT);
	}
	public void setBody(String body)
	{
		this.setColumn(COLUMN_BODY, body);
	}
	public String getBody()
	{
		return this.getStringColumnValue(COLUMN_BODY);
	}
	public String getMessageType()
	{
		return this.getStringColumnValue(COLUMN_MESSAGE_TYPE);
	}
	public void setMessageType(String type) 
	{
		this.setColumn(COLUMN_MESSAGE_TYPE, type);
	}
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.message.data.PrintMessage#getContentCode()
	 */
	public String getContentCode() {
		return this.getStringColumnValue(COLUMN_CONTENT_CODE);
	}
	public void setContentCode(String contentCode){
		this.setColumn(COLUMN_CONTENT_CODE,contentCode);
	}
	public ICFile getMessageData()
	{
		return (ICFile) this.getColumnValue(COLUMN_MESSAGE_DATA); //Replace this later
	}
	public int getMessageDataFileID()
	{
		return this.getIntColumnValue(COLUMN_MESSAGE_DATA);
	}
	public void setMessageData(ICFile file)
	{ //Temp (test) method
		this.setColumn(COLUMN_MESSAGE_DATA, file);
	}
	public void setMessageData(int fileID)
	{ //Temp (test) method
		this.setColumn(COLUMN_MESSAGE_DATA, fileID);
	}
	public void setAttachedFile(ICFile file)
	{ //Temp (test) method
		this.setColumn(COLUMN_ATTATCHED_FILE_ID, file);
	}
	public void setAttachedFile(int fileID)
	{ //Temp (test) method
		this.setColumn(COLUMN_ATTATCHED_FILE_ID, fileID);
	}
	public ICFile getAttachedFile()
	{
		return (ICFile) this.getColumnValue(COLUMN_ATTATCHED_FILE_ID); //Replace this later
	}
	public int getAttachedFileID()
	{
		return this.getIntColumnValue(COLUMN_ATTATCHED_FILE_ID);
	}
	
	public ICFile getMessageBulkData() {
			return (ICFile) this.getColumnValue(COLUMN_BULK_DATA); //Replace this later
		}

		public int getMessageBulkDataFileID() {
			return this.getIntColumnValue(COLUMN_BULK_DATA);
		}

		public void setMessageBulkData(ICFile file) { //Temp (test) method
			this.setColumn(COLUMN_BULK_DATA, file);
		}

		public void setMessageBulkData(int fileID) { //Temp (test) method
			this.setColumn(COLUMN_BULK_DATA, fileID);
		}
	
	public User getSender() { throw new UnsupportedOperationException(); }
	public void setSender(User sender){ throw new UnsupportedOperationException(); }
	public int getSenderID() { throw new UnsupportedOperationException(); }
	public void setSenderID(int senderID){ throw new UnsupportedOperationException(); }

	public String getSenderName()
	{
		return getOwner().getName();
	}
	public String getDateString()
	{
		/**
		 * @todo: implement
		 */
		return "";
	}
	public Collection ejbFindMessages(User user) throws FinderException
	{
		return super.ejbFindAllCasesByUser(user);
	}
	
	public Collection ejbFindMessagesByStatus(User user, String[] status)throws FinderException{
		return super.ejbFindAllCasesByUserAndStatusArray(user, status);
	}

	public Collection ejbFindPrintedMessages()throws FinderException{
		return super.idoFindPKsByQuery(super.idoSelectQueryGetAllCasesByStatusOrderedByCreation(getCaseStatusReady()));
	}
	
	public Collection ejbFindUnPrintedMessages()throws FinderException{
		return super.idoFindPKsByQuery(super.idoSelectQueryGetAllCasesByStatusOrderedByCreation(getCaseStatusOpen()));
	}
	
	public Collection ejbFindPrintedMessages(IWTimestamp from, IWTimestamp to) throws FinderException {
		//IDOQuery query = super.idoQueryGetAllCasesByStatus(getCaseStatusReady(),from,to);
		//query.append(" order by g.").append(getSQLGeneralCaseCreatedColumnName());
		//query.append(" desc ");
	    SelectQuery query = idoSelectQueryGetAllCases();
	    query.addCriteria(idoCriteriaForStatus(getCaseStatusReady()));
	    query.addCriteria(idoCriteriaForCreatedWithinDates(from,to));
	    query.addOrder(idoOrderByCreationDate(false));
		return super.idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindUnPrintedMessages(IWTimestamp from, IWTimestamp to) throws FinderException {
		//IDOQuery query = super.idoQueryGetAllCasesByStatus(getCaseStatusOpen(),from,to);
		//query.append(" order by g.").append(getSQLGeneralCaseCreatedColumnName());
		//query.append(" desc ");
	    SelectQuery query = idoSelectQueryGetAllCases();
	    query.addCriteria(idoCriteriaForStatus(getCaseStatusOpen()));
	    query.addCriteria(idoCriteriaForCreatedWithinDates(from,to));
	    query.addOrder(idoOrderByCreationDate(false));
		return super.idoFindPKsByQuery(query);
	}
	
	/**
	 *Counts the number of letters that are of type default and unprinted
	 */
	public int ejbHomeGetNumberOfUnPrintedMessages()
	{
		try
		{
			SelectQuery sql = super.idoSelectQueryGetCountCasesWithStatus(getCaseStatusOpen());
			return super.idoGetNumberOfRecords(sql);
		}
		catch (IDOException sqle)
		{
			throw new EJBException(sqle.getMessage());
		}
	}
	
	public String getPrintType(){
		return PRINT_TYPE;
	}
	
	public String[] ejbHomeGetPrintMessageTypes(){
		String[] types= new String[1];
		types[0] = PRINT_TYPE;
		return types;
	}
	

}
