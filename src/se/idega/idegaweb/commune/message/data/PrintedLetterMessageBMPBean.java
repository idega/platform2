package se.idega.idegaweb.commune.message.data;

import com.idega.core.data.ICFile;
import com.idega.data.*;
import com.idega.block.process.data.*;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

import javax.ejb.*;

import java.util.Collection;
import java.util.Iterator;
import java.rmi.RemoteException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class PrintedLetterMessageBMPBean extends AbstractCaseBMPBean implements PrintedLetterMessage, PrintMessage, Case {

	private static final String COLUMN_SUBJECT = "SUBJECT";
	private static final String COLUMN_BODY = "BODY";
	private static final String COLUMN_MESSAGE_TYPE = "MESSAGE_TYPE";
	private static final String COLUMN_MESSAGE_DATA = "MESSAGE_DATA";
	private static final String COLUMN_LETTER_TYPE = "LETTER_TYPE";

	private static final String CASE_CODE_KEY = "SYMEBRV";
	private static final String CASE_CODE_DESCRIPTION = "Letter Message";
	
	public static final String LETTER_TYPE_DEFAULT="DEFA";
	public static final String LETTER_TYPE_PASSWORD="PASS";


	public String getEntityName() {
		return "MSG_LETTER_MESSAGE";
	}

	public void initializeAttributes() {
		//    this.addAttribute(this.getIDColumnName());
		addGeneralCaseRelation();
		this.addAttribute(COLUMN_SUBJECT, "Message subject", String.class);
		this.addAttribute(COLUMN_BODY, "Message body", String.class, 1000);
		this.addAttribute(COLUMN_MESSAGE_TYPE, "Message type", String.class, 20);
		this.addManyToOneRelationship(COLUMN_MESSAGE_DATA, "Message data", ICFile.class);
		this.addAttribute(COLUMN_LETTER_TYPE, "Message SubType", String.class, 4);
		//this.addAttribute(COLUMN_DATE,"Test data column",String.class);//temp
		//this.addAttribute(COLUMN_SENDER,"Test data column",String.class);//temp
		//this.addManyToManyRelationShip(SampleEntity.class);
	}

	public String getCaseCodeKey() {
		return CASE_CODE_KEY;
	}

	public String getCaseCodeDescription() {
		return CASE_CODE_DESCRIPTION;
	}
	
	public void setDefaultValues(){
		setLetterType(LETTER_TYPE_DEFAULT);	
	}

	public void insertStartData() {
		try {
			super.insertStartData();
			/*
			UserMessageHome home = (UserMessageHome)com.idega.data.IDOLookup.getHome(UserMessage.class);
			User administrator = (User)com.idega.data.IDOLookup.findByPrimaryKey(User.class,1);
			
			Message msg = home.create();
			msg.setSubject("V?lkommen till BUN24!");
			msg.setBody("Ditt medborgarkonto ?r nu redo.");
			//msg.setDateX("2002-06-02");
			//msg.setSenderNameX("BUN24 Administration");
			msg.setOwner(administrator);
			msg.store();
			
			msg = home.create();
			msg.setSubject("Barnomsorgscheck mottagen");
			msg.setBody("Barnomsorgschecken f?r ditt barn Henrik Mickelin har mottagits av anordnare Svanen.");
			//msg.setDateX("2002-06-03");
			//msg.setSenderNameX("Sonja Westerberg");
			msg.setOwner(administrator);
			msg.store();
			
			msg = home.create();
			msg.setSubject("Nyheter fr?n BUN");
			msg.setBody("Skolorna profilerar sig f?r att tillgodose dina ?nskem?l och du som f?r?lder kan v?lja vilken skola du tycker ?r b?st och vill att ditt barn ska g? i. M?ngfalden berikar, men det stora utbudet g?r ocks? valet sv?rare.");
			//msg.setDateX("2002-06-05");
			//msg.setSenderNameX("Lars Karlsson");
			msg.setOwner(administrator);
			msg.store();
			*/
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public void setSubject(String subject) throws java.rmi.RemoteException {
		this.setColumn(COLUMN_SUBJECT, subject);
	}

	public String getSubject() throws java.rmi.RemoteException {
		return this.getStringColumnValue(COLUMN_SUBJECT);
	}

	public void setBody(String body) throws java.rmi.RemoteException {
		this.setColumn(COLUMN_BODY, body);
	}

	public String getBody() throws java.rmi.RemoteException {
		return this.getStringColumnValue(COLUMN_BODY);
	}

	public String getMessageType() throws java.rmi.RemoteException {
		return this.getStringColumnValue(COLUMN_MESSAGE_TYPE);
	}

	public void setMessageType(String type) throws java.rmi.RemoteException {
		this.setColumn(COLUMN_MESSAGE_TYPE, type);
	}

	public ICFile getMessageData() throws java.rmi.RemoteException {
		return (ICFile) this.getColumnValue(COLUMN_MESSAGE_DATA); //Replace this later
	}

	public int getMessageDataFileID() throws java.rmi.RemoteException {
		return this.getIntColumnValue(COLUMN_MESSAGE_DATA);
	}

	public void setMessageData(ICFile file) throws java.rmi.RemoteException { //Temp (test) method
		this.setColumn(COLUMN_MESSAGE_DATA, file);
	}

	public void setMessageData(int fileID) throws java.rmi.RemoteException { //Temp (test) method
		this.setColumn(COLUMN_MESSAGE_DATA, fileID);
	}

	public String getSenderName() {
		try {
			return getOwner().getName();
		}
		catch (RemoteException e) {
			return "";
		}
	}

	public String getDateString() {
		/**
		 * @todo: implement
		 */
		return "";
	}

	public void setLetterType(String letterType) {
		setColumn(COLUMN_LETTER_TYPE, letterType);
	}

	public String getLetterType() {
		return getStringColumnValue(COLUMN_LETTER_TYPE);
	}
	
	public void setAsPasswordLetter(){
		setLetterType(LETTER_TYPE_PASSWORD);
	}


	public Collection ejbFindMessages(User user) throws FinderException, java.rmi.RemoteException {
		return super.ejbFindAllCasesByUser(user);
	}

	public Collection ejbFindAllUnPrintedLetters() throws FinderException, java.rmi.RemoteException {
		return super.ejbFindAllCasesByStatus(super.getCaseStatusOpen());
	}

	public Collection ejbFindAllPrintedLetters() throws FinderException, java.rmi.RemoteException {
		return super.ejbFindAllCasesByStatus(super.getCaseStatusReady());
	}
	
	protected IDOQuery idoQueryGetCountDefaultLettersWithStatus(String caseStatus){
		return idoQueryGetLettersCountByStatusAndType(caseStatus,LETTER_TYPE_DEFAULT);
	}
		
	protected IDOQuery idoQueryGetCountPasswordLettersWithStatus(String caseStatus){
		return idoQueryGetLettersCountByStatusAndType(caseStatus,COLUMN_LETTER_TYPE);
	}
	
	
	
	public String getUnPrintedCaseStatusForType(String type)throws RemoteException{
	  	return getCaseStatusOpen();
	  	
	}
	
	public String getPrintedCaseStatusForType(String type)throws RemoteException{
		return getCaseStatusReview();
	}
	
	/**
	 *Counts the number of unprinted letters of the given type.
	 */
	public int ejbHomeGetNumberOfUnprintedLettersByType(String letterType){
		try{
			IDOQuery sql = idoQueryGetLettersCountByStatusAndType(getUnPrintedCaseStatusForType(letterType),letterType);				
			return super.idoGetNumberOfRecords(sql);
		}
		catch (RemoteException rme)
		{
			throw new EJBException(rme.getMessage());
		}
		catch (IDOException sqle)
		{
			throw new EJBException(sqle.getMessage());
		}
	}
	
	/**
	 *Counts the number of printed letters of the given type.
	 */
	public int ejbHomeGetNumberOfPrintedLettersByType(String letterType){
		try{
			IDOQuery sql = idoQueryGetLettersCountByStatusAndType(getUnPrintedCaseStatusForType(letterType),letterType);				
			return super.idoGetNumberOfRecords(sql);
		}
		catch (RemoteException rme)
		{
			throw new EJBException(rme.getMessage());
		}
		catch (IDOException sqle)
		{
			throw new EJBException(sqle.getMessage());
		}
	}
	
	/**
	 *Counts the number of letters of the given type and status.
	 */
	public int ejbHomeGetNumberOfLettersByStatusAndType(String caseStatus,String letterType)
	{
		try
		{
			IDOQuery sql = idoQueryGetLettersCountByStatusAndType(caseStatus,letterType);
			return super.idoGetNumberOfRecords(sql);
		}
		
		catch (IDOException sqle)
		{
			throw new EJBException(sqle.getMessage());
		}
	}
	
	
	/**
	 *Counts the number of letters that are of type password and unprinted
	 */
	public int ejbHomeGetNumberOfUnPrintedPasswordLetters()
	{
		try
		{
			IDOQuery sql = idoQueryGetCountPasswordLettersWithStatus(getCaseStatusOpen());
			return super.idoGetNumberOfRecords(sql);
		}
		catch (RemoteException rme)
		{
			throw new EJBException(rme.getMessage());
		}
		catch (IDOException sqle)
		{
			throw new EJBException(sqle.getMessage());
		}
	}
	
	/**
	 *Counts the number of letters that are of type password and printed
	 */
	public int ejbHomeGetNumberOfPrintedPasswordLetters()
	{
		try
		{
			IDOQuery sql = idoQueryGetCountPasswordLettersWithStatus(getCaseStatusReview());
			return super.idoGetNumberOfRecords(sql);
		}
		catch (RemoteException rme)
		{
			throw new EJBException(rme.getMessage());
		}
		catch (IDOException sqle)
		{
			throw new EJBException(sqle.getMessage());
		}
	}



	/**
	 *Counts the number of letters that are of type default and unprinted
	 */
	public int ejbHomeGetNumberOfUnPrintedDefaultLetters()
	{
		try
		{
			IDOQuery sql = idoQueryGetCountDefaultLettersWithStatus(getCaseStatusOpen());
			return super.idoGetNumberOfRecords(sql);
		}
		catch (RemoteException rme)
		{
			throw new EJBException(rme.getMessage());
		}
		catch (IDOException sqle)
		{
			throw new EJBException(sqle.getMessage());
		}
	}

	/**
	 *Counts the number of letters that are of type default and printed
	 */
	public int ejbHomeGetNumberOfPrintedDefaultLetters()
	{
		try
		{
			IDOQuery sql = idoQueryGetCountDefaultLettersWithStatus(getCaseStatusReview());
			return super.idoGetNumberOfRecords(sql);
		}
		catch (RemoteException rme)
		{
			throw new EJBException(rme.getMessage());
		}
		catch (IDOException sqle)
		{
			throw new EJBException(sqle.getMessage());
		}
	}
	
	public String[] ejbHomeGetLetterTypes()
	{
		String[] types = {LETTER_TYPE_DEFAULT,LETTER_TYPE_PASSWORD};
		return types;
	}
	

	protected IDOQuery idoQueryGetPrintedLettersByType(String letterType) {
		try {
			return idoQueryGetLettersByStatusAndType(getCaseStatusReady(), letterType);
		}
		catch (Exception e) {
			throw new IDORuntimeException(e, this);
		}
	}
	
	protected IDOQuery idoQueryGetPrintedLettersByType(String letterType,IWTimestamp from, IWTimestamp to) {
		try {
			return idoQueryGetLettersByStatusAndType(getCaseStatusReady(), letterType,from,to);
		}
		catch (Exception e) {
			throw new IDORuntimeException(e, this);
		}
	}

	protected IDOQuery idoQueryGetUnPrintedLettersByType(String letterType) {
		try {
			return idoQueryGetLettersByStatusAndType(getCaseStatusOpen(), letterType);
		}
		catch (Exception e) {
			throw new IDORuntimeException(e, this);
		}
	}
	
	protected IDOQuery idoQueryGetUnPrintedLettersByType(String letterType,IWTimestamp from, IWTimestamp to) {
		try {
			return idoQueryGetLettersByStatusAndType(getCaseStatusOpen(), letterType,from,to);
		}
		catch (Exception e) {
			throw new IDORuntimeException(e, this);
		}
	}
	
	protected IDOQuery idoQueryGetLettersByStatusAndType(String caseStatus, String letterType) {
		IDOQuery query = super.idoQueryGetAllCasesByStatus(caseStatus);
		query.append(" and a." + COLUMN_LETTER_TYPE + "='" + letterType + "'");
		return query;
	}
	
	protected IDOQuery idoQueryGetLettersByStatusAndType(String caseStatus, String letterType,IWTimestamp from, IWTimestamp to) {
		IDOQuery query = super.idoQueryGetAllCasesByStatus(caseStatus,from,to);
		query.append(" and a." + COLUMN_LETTER_TYPE + "='" + letterType + "'");
		query.append(" order by g.").append(getSQLGeneralCaseCreatedColumnName());
		query.append(" desc ");
		return query;
	}

	protected IDOQuery idoQueryGetLettersCountByStatusAndType(String caseStatus, String letterType) {
		IDOQuery query = super.idoQueryGetCountCasesWithStatus(caseStatus);
		query.append(" and a." + COLUMN_LETTER_TYPE + "='" + letterType + "'");
		return query;
	}

	public Collection ejbFindPrintedLettersByType(String letterType) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetPrintedLettersByType(letterType));
	}
	
	public Collection ejbFindPrintedLettersByType(String letterType,IWTimestamp from, IWTimestamp to) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetPrintedLettersByType(letterType,from,to));
	}
	
	public Collection ejbFindUnPrintedLettersByType(String letterType) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetUnPrintedLettersByType(letterType));
	}
	
	public Collection ejbFindUnPrintedLettersByType(String letterType,IWTimestamp from, IWTimestamp to) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetUnPrintedLettersByType(letterType,from,to));
	}
	

	public Collection ejbFindUnPrintedPasswordLetters() throws FinderException {
		String letterType = LETTER_TYPE_PASSWORD;
		return ejbFindUnPrintedLettersByType(letterType);
	}

	public Collection ejbFindPrintedPasswordLetters() throws FinderException {
		String letterType = LETTER_TYPE_PASSWORD;
		return ejbFindPrintedLettersByType(letterType);
	}

	public Collection ejbFindUnPrintedDefaultLetters() throws FinderException {
		String letterType = LETTER_TYPE_DEFAULT;
		return ejbFindUnPrintedLettersByType(letterType);
	}

	public Collection ejbFindPrintedDefaultLetters() throws FinderException {
		String letterType = LETTER_TYPE_DEFAULT;
		return ejbFindPrintedLettersByType(letterType);
	}
	
	public String getPrintType()throws RemoteException{
		return getMessageType();
	}
	
	public String[] ejbHomeGetPrintMessageTypes(){
		return ejbHomeGetLetterTypes();
	}


}
