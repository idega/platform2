/*
 * Created on May 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.core.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.Group;

/**
 * Description: The head data bean for the work reports. One Bean of this type represents a clubs yearly work report.<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportBMPBean extends GenericEntity implements WorkReport {
	protected final static String ENTITY_NAME = "ISI_WORK_REPORT";
	protected final static String COLUMN_NAME_GROUP_ID = "GROUP_ID";//Could be a club,league or a regional union
	protected final static String COLUMN_NAME_REGIONAL_UNION_GROUP_ID = "REG_UNI_GR_ID";//a connection for a club
	
	protected final static String COLUMN_NAME_GROUP_NAME = "GROUP_NAME";
	protected final static String COLUMN_NAME_GROUP_SHORT_NAME = "GROUP_SHORT_NAME";
	protected final static String COLUMN_NAME_GROUP_NUMBER = "GROUP_NUMBER";
	protected final static String COLUMN_NAME_WORK_REPORT_YEAR = "YEAR_OF_REPORT";
	protected final static String COLUMN_NAME_MEMBERS_DONE = "MEMB_DONE";
	protected final static String COLUMN_NAME_ACCOUNT_DONE = "ACC_DONE";
	protected final static String COLUMN_NAME_BOARD_DONE = "BOARD_DONE";
  protected final static String COLUMN_NAME_CREATION_FROM_DATABASE_DONE = "CREATION_FROM_DATABASE_DONE";
	protected final static String COLUMN_NAME_STATUS = "STATUS";
	protected final static String COLUMN_NAME_SENT = "SENT";
	protected final static String COLUMN_NAME_SENT_REPORT = "SENT_REPORT";
	protected final static String COLUMN_NAME_MEMBER_FILE_ID = "MEMBER_PART_FILE_ID";
	protected final static String COLUMN_NAME_ACCOUNT_FILE_ID = "ACCOUNT_PART_FILE_ID";
	protected final static String COLUMN_NAME_BOARD_FILE_ID = "BOARD_PART_FILE_ID";

	
	public WorkReportBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_GROUP_ID, "Group id of club/league/regional union",true,true,Integer.class,"many-to-one",Group.class);
		addAttribute(COLUMN_NAME_REGIONAL_UNION_GROUP_ID, "Regional union group id",true,true,Integer.class,"many-to-one",Group.class);
		
		addAttribute(COLUMN_NAME_MEMBER_FILE_ID, "Members-part file id",true,true,Integer.class,"many-to-one",ICFile.class);
		addAttribute(COLUMN_NAME_ACCOUNT_FILE_ID, "Account-part file id",true,true,Integer.class,"many-to-one",ICFile.class);
		addAttribute(COLUMN_NAME_BOARD_FILE_ID, "Board-part file id",true,true,Integer.class,"many-to-one",ICFile.class);
		addAttribute(COLUMN_NAME_GROUP_NAME, "Group name",true,true,String.class);
		addAttribute(COLUMN_NAME_GROUP_SHORT_NAME, "Group short name",true,true,String.class,30);
		addAttribute(COLUMN_NAME_GROUP_NUMBER, "Group number",true,true,String.class);
		addAttribute(COLUMN_NAME_WORK_REPORT_YEAR,"The year this report is valid for",true,true,Integer.class);
		addAttribute(COLUMN_NAME_MEMBERS_DONE, "Is the members-part of the work report finished", true, true, Boolean.class);
		addAttribute(COLUMN_NAME_ACCOUNT_DONE, "Is the account-part of the work report finished", true, true, Boolean.class);		
		addAttribute(COLUMN_NAME_BOARD_DONE, "Is the board-part of the work report finished", true, true, Boolean.class);	
		addAttribute(COLUMN_NAME_CREATION_FROM_DATABASE_DONE, "Has the data been created from database?", true, true, Boolean.class);
    addAttribute(COLUMN_NAME_STATUS, "Status",true,true,String.class,30);
		addAttribute(COLUMN_NAME_SENT, "Has the workreport been sent, finalized", true, true, Boolean.class);
		addAttribute(COLUMN_NAME_SENT_REPORT, "Results from report check",true,true,String.class,10000);
		
		
		addManyToManyRelationShip(WorkReportGroup.class);//so we can get the clubs related to leagues/divisions
		
		//TODO add stats
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	public Integer getGroupId(){
		return getIntegerColumnValue(COLUMN_NAME_GROUP_ID);
	}
	
	public void setGroupId(int groupId){
		setColumn(COLUMN_NAME_GROUP_ID,groupId);
	}
	
	public void setGroupId(Integer groupId){
		setColumn(COLUMN_NAME_GROUP_ID,groupId);
	}
	
	public Integer getRegionalUnionGroupId(){
		return getIntegerColumnValue(COLUMN_NAME_REGIONAL_UNION_GROUP_ID);
	}
	
	public void setRegionalUnionGroupId(int groupId){
		setColumn(COLUMN_NAME_REGIONAL_UNION_GROUP_ID,groupId);
	}
	
	public void setRegionalUnionGroupId(Integer groupId){
		setColumn(COLUMN_NAME_REGIONAL_UNION_GROUP_ID,groupId);
	}
	
	public Integer getMemberFileId(){
		return getIntegerColumnValue(COLUMN_NAME_MEMBER_FILE_ID);
	}
	
	public Integer getAccountFileId(){
		return getIntegerColumnValue(COLUMN_NAME_ACCOUNT_FILE_ID);
	}
	
	public Integer getBoardFileId(){
		return getIntegerColumnValue(COLUMN_NAME_BOARD_FILE_ID);
	}
	
	public void setMemberFileId(int fileId){
		setColumn(COLUMN_NAME_MEMBER_FILE_ID,fileId);
	}
	
	public void setAccountFileId(int fileId){
			setColumn(COLUMN_NAME_ACCOUNT_FILE_ID,fileId);
		}
		
	public void setBoardFileId(int fileId){
			setColumn(COLUMN_NAME_BOARD_FILE_ID,fileId);
	}
		
	
	public String getGroupName(){
		return getStringColumnValue(COLUMN_NAME_GROUP_NAME);
	}
	

	public void setGroupShortName(String name){
		setColumn(COLUMN_NAME_GROUP_SHORT_NAME,name);
	}
	
	public String getGroupShortName(){
		return getStringColumnValue(COLUMN_NAME_GROUP_SHORT_NAME);
	}
	
	public void setStatus(String status){
		setColumn(COLUMN_NAME_STATUS,status);
	}
	
	public String getStatus(){
		return getStringColumnValue(COLUMN_NAME_STATUS);
	}

	public void setGroupName(String name){
		setColumn(COLUMN_NAME_GROUP_NAME,name);
	}
	
	public String getGroupNumber(){
		return getStringColumnValue(COLUMN_NAME_GROUP_NUMBER);
	}
	

	public void setGroupNumber(String number){
		setColumn(COLUMN_NAME_GROUP_NUMBER,number);
	}
	
	public Integer getYearOfReport(){
		return getIntegerColumnValue(COLUMN_NAME_WORK_REPORT_YEAR);
	}
	

	public void setYearOfReport(int year){
		setColumn(COLUMN_NAME_WORK_REPORT_YEAR,year);
	}
	
	public boolean isBoardPartDone(){
		return getBooleanColumnValue(COLUMN_NAME_BOARD_DONE,false);
	}
	

	public void setBoardPartDone(boolean isDone){
		setColumn(COLUMN_NAME_BOARD_DONE,isDone);
	}
	
	public boolean isMembersPartDone(){
		return getBooleanColumnValue(COLUMN_NAME_MEMBERS_DONE,false);
	}
	
	public void setAsSent(boolean sent){
		setColumn(COLUMN_NAME_SENT,sent);
	}
	
	public boolean isSent(){
		return getBooleanColumnValue(COLUMN_NAME_SENT,false);
	}
	
	public void setSentReportText(String text){
		setColumn(COLUMN_NAME_SENT_REPORT,text);
	}
	
	public String getSentReportText(){
		return getStringColumnValue(COLUMN_NAME_SENT_REPORT);
	}
	
	public void setMembersPartDone(boolean isDone){
		setColumn(COLUMN_NAME_MEMBERS_DONE,isDone);
	}
	
	public boolean isAccountPartDone(){
		return getBooleanColumnValue(COLUMN_NAME_ACCOUNT_DONE,false);
	}
	

	public void setAccountPartDone(boolean isDone){
		setColumn(COLUMN_NAME_ACCOUNT_DONE,isDone);
	}
  
  public void setCreationFromDatabaseDone(boolean isDone) {
    setColumn(COLUMN_NAME_CREATION_FROM_DATABASE_DONE, isDone);
  }
  
  public boolean isCreationFromDatabaseDone() {
    return getBooleanColumnValue(COLUMN_NAME_CREATION_FROM_DATABASE_DONE, false);
  }
	
	public Integer ejbFindWorkReportByGroupIdAndYearOfReport(int groupId, int yearOfReport) throws FinderException{
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getEntityName());
		sql.appendWhereEquals(COLUMN_NAME_GROUP_ID,groupId);
		sql.appendAndEquals(COLUMN_NAME_WORK_REPORT_YEAR,yearOfReport);
		
		return (Integer) this.idoFindOnePKByQuery(sql);
	}
	
	public Collection getLeagues() throws IDOException {
		return idoGetRelatedEntities(WorkReportGroup.class);
	}
	
  public void addLeague(WorkReportGroup group) throws IDORelationshipException {
    idoAddTo(group);
  }
    
	
	
	
	
}
