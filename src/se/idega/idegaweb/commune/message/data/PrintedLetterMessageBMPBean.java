package se.idega.idegaweb.commune.message.data;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORuntimeException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

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
	private static final String COLUMN_BULK_DATA = "BULK_DATA";
	private static final String COLUMN_CONTENT_CODE = "CONTENT_CODE";

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
		this.addAttribute(COLUMN_BODY, "Message body", String.class, 4000);
		this.addAttribute(COLUMN_MESSAGE_TYPE, "Message type", String.class, 20);
		this.addManyToOneRelationship(COLUMN_MESSAGE_DATA, "Message data", ICFile.class);
		this.addAttribute(COLUMN_LETTER_TYPE, "Message SubType", String.class, 4);
		this.addManyToOneRelationship(COLUMN_BULK_DATA, "Message bulk data", ICFile.class);
		this.addAttribute(COLUMN_CONTENT_CODE, "Message contentcode", String.class, 20);
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

	public User getSender() { throw new UnsupportedOperationException(); }
	public void setSender(User sender){ throw new UnsupportedOperationException(); }
	public int getSenderID() { throw new UnsupportedOperationException(); }
	public void setSenderID(int senderID){ throw new UnsupportedOperationException(); }

	public void setSubject(String subject) {
		this.setColumn(COLUMN_SUBJECT, subject);
	}

	public String getSubject() {
		return this.getStringColumnValue(COLUMN_SUBJECT);
	}

	public void setBody(String body) {
		this.setColumn(COLUMN_BODY, body);
	}

	public String getBody() {
		return this.getStringColumnValue(COLUMN_BODY);
	}

	public String getMessageType() {
		return this.getStringColumnValue(COLUMN_MESSAGE_TYPE);
	}

	public void setMessageType(String type) {
		this.setColumn(COLUMN_MESSAGE_TYPE, type);
	}

	public ICFile getMessageData() {
		return (ICFile) this.getColumnValue(COLUMN_MESSAGE_DATA); //Replace this later
	}

	public int getMessageDataFileID() {
		return this.getIntColumnValue(COLUMN_MESSAGE_DATA);
	}

	public void setMessageData(ICFile file) { //Temp (test) method
		this.setColumn(COLUMN_MESSAGE_DATA, file);
	}

	public void setMessageData(int fileID) { //Temp (test) method
		this.setColumn(COLUMN_MESSAGE_DATA, fileID);
	}
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.message.data.PrintMessage#getContentType()
	 */
	public String getContentCode() {
		return this.getStringColumnValue(COLUMN_CONTENT_CODE);
	}
	
	public void setContentCode(String contentCode){
		this.setColumn(COLUMN_CONTENT_CODE,contentCode);
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

	public String getSenderName() {
		return getOwner().getName();
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


	public Collection ejbFindMessages(User user) throws FinderException {
		return super.ejbFindAllCasesByUser(user);
	}

	public Collection ejbFindMessagesByStatus(User user, String[] status)throws FinderException{
		return super.ejbFindAllCasesByUserAndStatusArray(user, status);
	}

	public Collection ejbFindAllUnPrintedLetters() throws FinderException {
		return super.ejbFindAllCasesByStatus(super.getCaseStatusOpen());
	}

	public Collection ejbFindAllPrintedLetters() throws FinderException {
		return super.ejbFindAllCasesByStatus(super.getCaseStatusReady());
	}
	
	protected IDOQuery idoQueryGetCountDefaultLettersWithStatus(String caseStatus){
		return idoQueryGetLettersCountByStatusAndType(caseStatus,LETTER_TYPE_DEFAULT);
	}
		
	protected IDOQuery idoQueryGetCountPasswordLettersWithStatus(String caseStatus){
		return idoQueryGetLettersCountByStatusAndType(caseStatus,COLUMN_LETTER_TYPE);
	}
	
	
	
	public String getUnPrintedCaseStatusForType(String type) {
	  	return getCaseStatusOpen();
	  	
	}
	
	public String getPrintedCaseStatusForType(String type) {
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
	
	protected IDOQuery idoQueryGetSinglePrintedLettersByType(String letterType,IWTimestamp from, IWTimestamp to) {
			try {
				return idoQueryGetSingleLettersByStatusAndType(getCaseStatusReady(), letterType,from,to);
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
	
	protected IDOQuery idoQueryGetSingleUnPrintedLettersByType(String letterType,IWTimestamp from, IWTimestamp to) {
			try {
				return idoQueryGetSingleLettersByStatusAndType(getCaseStatusOpen(), letterType,from,to);
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
	
	protected IDOQuery idoQueryGetSingleLettersByStatusAndType(String caseStatus, String letterType,IWTimestamp from, IWTimestamp to) {
		IDOQuery query = super.idoQueryGetAllCasesByStatus(caseStatus,from,to);
		query.append(" and a." + COLUMN_LETTER_TYPE + "='" + letterType + "'");
		query.append(" and a." + COLUMN_BULK_DATA + " is null ");
		query.append(" order by g.").append(getSQLGeneralCaseCreatedColumnName());
		query.append(" desc ");

		return query;
	}
	
	protected IDOQuery idoQueryGetLettersByBulkFile(int file,String caseStatus,String letterType) {
			IDOQuery query = super.idoQueryGetAllCasesByStatus(caseStatus);
		query.append(" and a." + COLUMN_LETTER_TYPE + "='" + letterType + "'");
			query.append(" and a." + COLUMN_BULK_DATA + "=  "+file);
			query.append(" order by g.").append(getSQLGeneralCaseCreatedColumnName());
			query.append(" desc ");
			//System.err.println(query.toString());
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
	
	public Collection ejbFindSinglePrintedLettersByType(String letterType,IWTimestamp from, IWTimestamp to) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetSinglePrintedLettersByType(letterType,from,to));
	}
	
	public Collection ejbFindByBulkFile(int file,String letterType, String status) throws FinderException {
			return super.idoFindPKsByQuery(idoQueryGetLettersByBulkFile(file,status,letterType));
	}
	
	public Collection ejbFindSingleByTypeAndStatus(String letterType,String status,IWTimestamp from, IWTimestamp to) throws FinderException {
			return super.idoFindPKsByQuery(idoQueryGetSingleLettersByStatusAndType(status,letterType,from,to));
	}
	
	public Collection ejbFindUnPrintedLettersByType(String letterType) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetUnPrintedLettersByType(letterType));
	}
	
	public Collection ejbFindUnPrintedLettersByType(String letterType,IWTimestamp from, IWTimestamp to) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetUnPrintedLettersByType(letterType,from,to));
	}
	
	public Collection ejbFindSingleUnPrintedLettersByType(String letterType,IWTimestamp from, IWTimestamp to) throws FinderException {
		return super.idoFindPKsByQuery(idoQueryGetSingleUnPrintedLettersByType(letterType,from,to));
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
	
	public String getPrintType() {
		return getMessageType();
	}
	
	public String[] ejbHomeGetPrintMessageTypes(){
		return ejbHomeGetLetterTypes();
	}
	
	//TODO Handle this in more general way...
	public Collection ejbFindLettersByChildcare(int providerID, String ssn, String msgId, IWTimestamp from, IWTimestamp to) throws FinderException {
		IDOQuery sql = idoQuery();
		String sqlFrom = this.getEntityName() + " m, proc_case p, comm_childcare c";
		if (ssn != null && ! ssn.equals("")){
			sqlFrom += ", ic_user u";
		}
		
		sql.appendSelectAllFrom(sqlFrom);
		sql.appendWhereEquals("m.msg_letter_message_id","p.proc_case_id");
		sql.appendAndEquals("p.parent_case_id", "c.comm_childcare_id");
		sql.appendAndEquals("c.provider_id", providerID);
		if (ssn != null && ! ssn.equals("")){
			sql.appendAndEquals("p.user_id", "u.ic_user_id");
			sql.appendAnd().append("u.personal_id").appendLike().appendWithinSingleQuotes( ssn);
		}
		if (msgId != null && ! msgId.equals("")){
			sql.appendAndEqualsQuoted("m.msg_letter_message_id", msgId);
		}
		
		to.setHour(23);
		to.setMinute(59);
		to.setSecond(59);
		from.setHour(0);
		from.setMinute(0);
		from.setSecond(0);
		sql.appendAnd();
		sql.append("p.created");
		sql.append(" >= '");
		sql.append(from.toSQLString());
		sql.append("'");
		sql.appendAnd();
		sql.append("p.created");
		sql.append(" <= '");
		sql.append(to.toSQLString());
		sql.append("' ");
		sql.appendOrderBy("p.created");
		
//		System.out.println("########### SQL:" + sql.toString() + ".");
		
		Collection tmp = this.idoFindPKsByQuery(sql);
		return tmp;
		
	}
	
	public Collection ejbFindAllLettersBySchool(int providerID, String ssn, String msgId, IWTimestamp from, IWTimestamp to) throws FinderException {
		IDOQuery sql = idoQuery();
		String sqlFrom = this.getEntityName() + " m, proc_case p, comm_sch_choice c";
		if (ssn != null && ! ssn.equals("")){
			sqlFrom += ", ic_user u";
		}
			
		sql.appendSelectAllFrom(sqlFrom);
		sql.appendWhereEquals("m.msg_letter_message_id","p.proc_case_id");
		sql.appendAndEquals("p.parent_case_id", "c.comm_sch_choice_id");
		sql.appendAndEquals("c.school_id", providerID);
		if (ssn != null && ! ssn.equals("")){
			sql.appendAndEquals("p.user_id", "u.ic_user_id");
			sql.appendAnd().append("u.personal_id").appendLike().appendWithinSingleQuotes( ssn);
		}
		if (msgId != null && ! msgId.equals("")){
			sql.appendAndEqualsQuoted("m.msg_letter_message_id", msgId);
		}
			
		to.setHour(23);
		to.setMinute(59);
		to.setSecond(59);
		from.setHour(0);
		from.setMinute(0);
		from.setSecond(0);
		sql.appendAnd();
		sql.append("p.created");
		sql.append(" >= '");
		sql.append(from.toSQLString());
		sql.append("'");
		sql.appendAnd();
		sql.append("p.created");
		sql.append(" <= '");
		sql.append(to.toSQLString());
		sql.append("' ");
		sql.appendOrderBy("p.created");
			
	//	System.out.println("########### SQL:" + sql.toString() + ".");
			
		Collection tmp = this.idoFindPKsByQuery(sql);
		return tmp;
			
	}
	
		
	public Collection ejbFindLetters(String[] msgId) throws FinderException {
		IDOQuery sql = idoQuery();
		
		sql.appendSelectAllFrom(this.getEntityName() + " m");
		sql.appendWhere("m.msg_letter_message_id");
		sql.appendInArray(msgId);
		
//		System.out.println("########### SQL:" + sql.toString() + ".");
		
		Collection tmp = this.idoFindPKsByQuery(sql);
		return tmp;
		
	}	
	
	

}