/*
 * Created on May 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import javax.ejb.FinderException;

import com.idega.core.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;

/**
 * Description: The head data bean for the work reports. One Bean of this type represents a clubs yearly work report.<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportBMPBean extends GenericEntity implements WorkReport{
	protected final static String ENTITY_NAME = "ISI_WORK_REPORT";
	protected final static String COLUMN_NAME_CLUB_ID = "CLUB_ID";
	protected final static String COLUMN_NAME_CLUB_NAME = "CLUB_NAME";
	protected final static String COLUMN_NAME_CLUB_SHORT_NAME = "CLUB_SHORT_NAME";
	protected final static String COLUMN_NAME_CLUB_NUMBER = "CLUB_NUMBER";
	protected final static String COLUMN_NAME_WORK_REPORT_YEAR = "YEAR";
	protected final static String COLUMN_NAME_MEMBERS_DONE = "MEMB_DONE";
	protected final static String COLUMN_NAME_ACCOUNT_DONE = "ACC_DONE";
	protected final static String COLUMN_NAME_BOARD_DONE = "BOARD_DONE";
	protected final static String COLUMN_NAME_STATUS = "STATUS";
	protected final static String COLUMN_NAME_MEMBER_FILE_ID = "MEMBER_PART_FILE_ID";
	protected final static String COLUMN_NAME_ACCOUNT_FILE_ID = "ACCOUNT_PART_FILE_ID";
	protected final static String COLUMN_NAME_BOARD_FILE_ID = "BOARD_PART_FILE_ID";

	
	public WorkReportBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_CLUB_ID, "Group id of club",true,true,Integer.class,"many-to-one",Group.class);
		addAttribute(COLUMN_NAME_MEMBER_FILE_ID, "Members-part file id",true,true,Integer.class,"many-to-one",ICFile.class);
		addAttribute(COLUMN_NAME_ACCOUNT_FILE_ID, "Account-part file id",true,true,Integer.class,"many-to-one",ICFile.class);
		addAttribute(COLUMN_NAME_BOARD_FILE_ID, "Board-part file id",true,true,Integer.class,"many-to-one",ICFile.class);
		addAttribute(COLUMN_NAME_CLUB_NAME, "Club name",true,true,String.class);
		addAttribute(COLUMN_NAME_CLUB_SHORT_NAME, "Club short name",true,true,String.class,30);
		addAttribute(COLUMN_NAME_CLUB_NUMBER, "Club number",true,true,String.class);
		addAttribute(COLUMN_NAME_WORK_REPORT_YEAR,"The year this report is valid for",true,true,Integer.class);
		addAttribute(COLUMN_NAME_MEMBERS_DONE, "Is the members-part of the work report finished", true, true, Boolean.class);
		addAttribute(COLUMN_NAME_ACCOUNT_DONE, "Is the account-part of the work report finished", true, true, Boolean.class);		
		addAttribute(COLUMN_NAME_BOARD_DONE, "Is the board-part of the work report finished", true, true, Boolean.class);	
		addAttribute(COLUMN_NAME_STATUS, "Status",true,true,String.class,30);
		
		addManyToManyRelationShip(WorkReportGroup.class);//so we can get the clubs related to leagues/divisions
		
		//TODO add stats
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	public Integer getClubId(){
		return getIntegerColumnValue(COLUMN_NAME_CLUB_ID);
	}
	
	public void setClubId(int clubId){
		setColumn(COLUMN_NAME_CLUB_ID,clubId);
	}
	
	public void setClubId(Integer clubId){
		setColumn(COLUMN_NAME_CLUB_ID,clubId);
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
		
	
	public String getClubName(){
		return getStringColumnValue(COLUMN_NAME_CLUB_NAME);
	}
	

	public void setClubShortName(String name){
		setColumn(COLUMN_NAME_CLUB_SHORT_NAME,name);
	}
	
	public String getClubShortName(){
		return getStringColumnValue(COLUMN_NAME_CLUB_SHORT_NAME);
	}
	
	public void setStatus(String status){
		setColumn(COLUMN_NAME_STATUS,status);
	}
	
	public String getStatus(){
		return getStringColumnValue(COLUMN_NAME_STATUS);
	}

	public void setClubName(String name){
		setColumn(COLUMN_NAME_CLUB_NAME,name);
	}
	
	public String getClubNumber(){
		return getStringColumnValue(COLUMN_NAME_CLUB_NUMBER);
	}
	

	public void setClubNumber(String number){
		setColumn(COLUMN_NAME_CLUB_NUMBER,number);
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
	

	public void setBordPartDone(boolean isDone){
		setColumn(COLUMN_NAME_BOARD_DONE,isDone);
	}
	
	public boolean isMembersPartDone(){
		return getBooleanColumnValue(COLUMN_NAME_MEMBERS_DONE,false);
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
	
	public Integer ejbFindWorkReportByClubIdAndYearOfReport(int clubId, int yearOfReport) throws FinderException{
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_NAME_CLUB_ID,clubId);
		sql.appendAndEquals(COLUMN_NAME_WORK_REPORT_YEAR,yearOfReport);
		
		return (Integer) this.idoFindOnePKByQuery(sql);
	}
	
	
	
	
	
	
	
}
