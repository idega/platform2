/*
 * Created on May 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.core.location.data.PostalCode;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Description: The list of people that are members in a club/union/league for a particular year<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportMemberBMPBean extends GenericEntity implements WorkReportMember{
	protected final static String ENTITY_NAME = "ISI_WR_CLUB_MEMB";
	protected final static String COLUMN_NAME_REPORT_ID = "ISI_WORK_REPORT_ID";
	
	protected final static String COLUMN_NAME_USER_ID = "IC_USER_ID";
	protected final static String COLUMN_NAME_PERSONAL_ID = "PERSONAL_ID";
	protected final static String COLUMN_NAME_NAME = "NAME";
	protected final static String COLUMN_NAME_AGE = "AGE_FOR_YEAR";
	protected final static String COLUMN_NAME_DATE_OF_BIRTH = "DATE_OF_BIRTH";
	protected final static String COLUMN_NAME_GENDER = "GENDER";
	
	protected final static String COLUMN_NAME_STREET_NAME = "STREET_NAME";
	protected final static String COLUMN_NAME_POSTAL_CODE_ID = "POSTAL_CODE_ID";
	protected final static String COLUMN_NAME_HOME_PHONE = "HOME_PHONE";
	protected final static String COLUMN_NAME_WORK_PHONE = "WORK_PHONE";
	protected final static String COLUMN_NAME_FAX = "FAX";
	protected final static String COLUMN_NAME_EMAIL = "EMAIL";

	
	protected final static String MALE = "m";
	protected final static String FEMALE = "f";

	
	public WorkReportMemberBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_REPORT_ID, "Id of the work report",true,true,Integer.class,"many-to-one",WorkReport.class);
		addAttribute(COLUMN_NAME_USER_ID, "User id",true,true,Integer.class,"one-to-one",User.class);
		addAttribute(COLUMN_NAME_PERSONAL_ID,"Personal id",true,true,String.class,10);
		addAttribute(COLUMN_NAME_NAME,"Name from file",true,true,String.class,180);	
		addAttribute(COLUMN_NAME_DATE_OF_BIRTH,"Date of birth",true,true,Timestamp.class);
		addAttribute(COLUMN_NAME_AGE, "The yearly age of the member",true,true,Integer.class);
		addAttribute(COLUMN_NAME_GENDER,"Gender m/f",true,true,String.class,1);
	
		
		addAttribute(COLUMN_NAME_STREET_NAME,"Streetname",true,true,String.class);
		addAttribute(COLUMN_NAME_POSTAL_CODE_ID, "Postal code id",true,true,Integer.class,"many-to-one",PostalCode.class);
		addAttribute(COLUMN_NAME_HOME_PHONE,"Home phone number",true,true,String.class);
		addAttribute(COLUMN_NAME_WORK_PHONE,"Work phone number",true,true,String.class);
		addAttribute(COLUMN_NAME_FAX,"Fax number",true,true,String.class);
		addAttribute(COLUMN_NAME_EMAIL,"Email",true,true,String.class);
		
		
		addManyToManyRelationShip(WorkReportGroup.class);
		
		addIndex("IDX_ISI_WR_CLUB_MEMBER_1", COLUMN_NAME_REPORT_ID);
		addIndex("IDX_ISI_WR_CLUB_MEMBER_2", new String[]{ COLUMN_NAME_REPORT_ID, COLUMN_NAME_USER_ID});
		addIndex("IDX_ISI_WR_CLUB_MEMBER_3", new String[]{ COLUMN_NAME_REPORT_ID, COLUMN_NAME_GENDER, COLUMN_NAME_DATE_OF_BIRTH});
	}
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	
	public int getAge() {
		return getIntColumnValue(COLUMN_NAME_AGE);
	}

	public void setAge(int age) {
		setColumn(COLUMN_NAME_AGE,age);
	}

	public Timestamp getDateOfBirth() {
		return (Timestamp) getTimestampColumnValue(COLUMN_NAME_DATE_OF_BIRTH);
	}


	public void setDateOfBirth(Timestamp dateOfBirth) {
		setColumn(COLUMN_NAME_DATE_OF_BIRTH,dateOfBirth);
	}

	public boolean isMale() {
		return MALE.equals(getStringColumnValue(COLUMN_NAME_GENDER));
	}
	
	public boolean isFemale() {
		return FEMALE.equals(getStringColumnValue(COLUMN_NAME_GENDER));
	}
	
	public String ejbHomeGetMaleGenderString(){
		return MALE;
	}
	
	public String ejbHomeGetFemaleGenderString(){
		return FEMALE;
	}
		

	public void setAsMale() {
		setColumn(COLUMN_NAME_GENDER,MALE);
	}
	
	public void setAsFemale() {
		setColumn(COLUMN_NAME_GENDER,FEMALE);
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME_NAME);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME_NAME,name);
	}

	public String getPersonalId() {
		return getStringColumnValue(COLUMN_NAME_PERSONAL_ID);
	}

	public void setPersonalId(String pin) {
		setColumn(COLUMN_NAME_PERSONAL_ID,pin);
	}
	
	public int getReportId() {
		return getIntColumnValue(COLUMN_NAME_REPORT_ID);
	}

	public void setReportId(int reportId) {
		setColumn(COLUMN_NAME_REPORT_ID,reportId);
	}

	public int getUserId() {
		return getIntColumnValue(COLUMN_NAME_USER_ID);
	}

	public void setUserId(int userId) {
		setColumn(COLUMN_NAME_USER_ID,userId);
	}
	
	public Collection ejbFindAllWorkReportMembersByWorkReportIdOrderedByMemberName(int reportId) throws FinderException{
		return idoFindAllIDsByColumnOrderedBySQL(COLUMN_NAME_REPORT_ID,reportId,COLUMN_NAME_NAME);
	}
  
  public Integer ejbFindWorkReportMemberBySocialSecurityNumberAndWorkReportId(String ssn, int reportId) throws FinderException  {
    IDOQuery sql = idoQuery();
    
    sql.appendSelectAllFrom(this.getEntityName())
    .appendWhere()
    .append(COLUMN_NAME_PERSONAL_ID).appendEqualSign()
    .append('\'').append(ssn).append('\'')
    .appendAndEquals(COLUMN_NAME_REPORT_ID,reportId);
  
    return (Integer) idoFindOnePKByQuery(sql);
  }
	
	public Integer ejbFindWorkReportMemberByUserIdAndWorkReportId(int userId, int reportId) throws FinderException{
		IDOQuery sql = idoQuery();
		
		sql.appendSelectAllFrom(this.getEntityName())
		.appendWhere()
		.append(COLUMN_NAME_USER_ID).appendEqualSign().append(userId)
		.appendAndEquals(COLUMN_NAME_REPORT_ID,reportId);
	
		return (Integer) idoFindOnePKByQuery(sql);
		
	}
	
	public Collection ejbFindAllWorkReportMembersByWorkReportIdAndWorkReportGroup(int reportId,WorkReportGroup wrGroup) throws FinderException{
		StringBuffer sql = new StringBuffer();
		String middleTableName = this.getNameOfMiddleTable(this,wrGroup);
		String primaryKeyName = "ISI_WR_GROUP_ID";
		
		sql.append("Select e.* from ").append(ENTITY_NAME).append(" e ,").append(middleTableName).append(" middle")
		.append(" where ").append("e."+COLUMN_NAME_REPORT_ID).append("=").append(reportId)
		.append(" and ").append("( middle."+primaryKeyName).append("=").append((Integer)wrGroup.getPrimaryKey()).append(" ) ")
		.append(" and ").append("( e."+this.getIDColumnName()).append("=").append(" middle."+this.getIDColumnName()).append(" ) ");		

		
		return idoFindIDsBySQL(sql.toString());
	}
	
	/**
	 * If age is less than 0 it returns null
	 * @param age
	 * @param year
	 * @return
	 */
	private IWTimestamp getYearlyAgeBorderIWTimestamp(int age, int year){
		if(age<0) return null;
		IWTimestamp stamp = new IWTimestamp(31,12,year-1);//work reports are for the year before
		stamp.addYears(-age);
		
		return stamp;
	}

	
	
	private int getCountOfPlayersOfEqualAgeAndByGenderWorkReportAndWorkReportGroup(int age,String gender, WorkReport report,WorkReportGroup league) {
		IDOQuery sql = idoQuery();
		int year = report.getYearOfReport().intValue();
		
		String leagueIDColumnName =  "ISI_WR_GROUP_ID";
		String IDColumnName = getIDColumnName();
		
		sql.appendSelectCountFrom(this.getEntityName()).append(" memb, ")
		.append(getNameOfMiddleTable(this,league)).append(" middle ")
		.appendWhere()
		.appendEquals("memb."+COLUMN_NAME_REPORT_ID, ((Integer)report.getPrimaryKey()).intValue());
		
		if(gender!=null){
			sql.appendAnd();
			sql.appendEqualsQuoted("memb."+COLUMN_NAME_GENDER, gender);
		}
		
		if(age>0){
			IWTimestamp fromstamp = new IWTimestamp(1,1,year-1);//work reports are for the year before
			fromstamp.addYears(-age);
			IWTimestamp toStamp = new IWTimestamp(31,12,year-1);//work reports are for the year before
			toStamp.addYears(-age);
			
			sql.appendAnd();
			sql.append("memb."+COLUMN_NAME_DATE_OF_BIRTH)
			.appendGreaterThanOrEqualsSign()
			.appendSingleQuote().append(fromstamp.toSQLString()).appendSingleQuote();
			sql.appendAnd();
			
			sql.append("memb."+COLUMN_NAME_DATE_OF_BIRTH)
			.appendLessThanOrEqualsSign()
			.appendSingleQuote().append(toStamp.toSQLString()).appendSingleQuote();
		}
		
		sql.appendAnd();
		
		sql.append("memb.")
		.append(IDColumnName)
		.appendEqualSign()
		.append("middle.")
		.append(IDColumnName)
		.appendAnd()
		.append("middle.")
		.append(leagueIDColumnName)
		.appendEqualSign()
		.append(league.getPrimaryKey());
		

		try {
			return idoGetNumberOfRecords(sql);
		}
		catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private int getCountOfPlayersEqualOrOlderThanAgeAndByGenderWorkReportAndWorkReportGroup(int age,String gender, WorkReport report,WorkReportGroup league) {
		IDOQuery sql = idoQuery();
		
		IWTimestamp stamp = getYearlyAgeBorderIWTimestamp(age,report.getYearOfReport().intValue());
		String leagueIDColumnName =  "ISI_WR_GROUP_ID";
		String IDColumnName = getIDColumnName();
		
		sql.appendSelectCountFrom(this.getEntityName()).append(" memb, ")
		.append(getNameOfMiddleTable(this,league)).append(" middle ")
		.appendWhere()
		.appendEquals("memb."+COLUMN_NAME_REPORT_ID, ((Integer)report.getPrimaryKey()).intValue());
		
		if(gender!=null){
			sql.appendAnd();
			sql.appendEqualsQuoted("memb."+COLUMN_NAME_GENDER, gender);
		}
		
		if(stamp!=null){
			sql.appendAnd();
			sql.append("memb."+COLUMN_NAME_DATE_OF_BIRTH)
			.appendLessThanOrEqualsSign()
			.appendSingleQuote().append(stamp.toSQLString()).appendSingleQuote();
		}
		
		sql.appendAnd();
		
		sql.append("memb.")
		.append(IDColumnName)
		.appendEqualSign()
		.append("middle.")
		.append(IDColumnName)
		.appendAnd()
		.append("middle.")
		.append(leagueIDColumnName)
		.appendEqualSign()
		.append(league.getPrimaryKey());
		

		try {
			return idoGetNumberOfRecords(sql);
		}
		catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private int getCountOfPlayersOfYoungerAgeAndByGenderWorkReportAndWorkReportGroup(int age,String gender, WorkReport report,WorkReportGroup league) {
		IDOQuery sql = idoQuery();
		IWTimestamp stamp = getYearlyAgeBorderIWTimestamp(age,report.getYearOfReport().intValue());
		String leagueIDColumnName =  "ISI_WR_GROUP_ID";
		String IDColumnName = getIDColumnName();
		
		sql.appendSelectCountFrom(this.getEntityName()).append(" memb, ")
		.append(getNameOfMiddleTable(this,league)).append(" middle ")
		.appendWhere()
		.appendEquals("memb."+COLUMN_NAME_REPORT_ID, ((Integer)report.getPrimaryKey()).intValue());
		
		if(gender!=null){
			sql.appendAnd();
			sql.appendEqualsQuoted("memb."+COLUMN_NAME_GENDER, gender);
		}
		
		if(stamp!=null){
			sql.appendAnd();
			sql.append("memb."+COLUMN_NAME_DATE_OF_BIRTH)
			.appendGreaterThanSign()
			.appendSingleQuote().append(stamp.toSQLString()).appendSingleQuote();
		}

		sql.appendAnd();
		
		sql.append("memb.")
		.append(IDColumnName)
		.appendEqualSign()
		.append("middle.")
		.append(IDColumnName)
		.appendAnd()
		.append("middle.")
		.append(leagueIDColumnName)
		.appendEqualSign()
		.append(league.getPrimaryKey());
		

		try {
			return idoGetNumberOfRecords(sql);
		}
		catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private int getCountOfPlayersEqualOrOlderThanAgeAndByGenderAndWorkReport(int age,String gender, WorkReport report) {
		WorkReportGroup league = null;
		try {
			league = (WorkReportGroup) report.getLeagues().toArray()[0];
		} catch(Exception e) {
			// no leagues -> no players
			return 0;
		}
		IDOQuery sql = idoQuery();
		
		IWTimestamp stamp = getYearlyAgeBorderIWTimestamp(age,report.getYearOfReport().intValue());
		String leagueIDColumnName =  "ISI_WR_GROUP_ID";
		String IDColumnName = getIDColumnName();

		sql.appendSelectCountFrom(this.getEntityName()).append(" memb, ")
		.append(getNameOfMiddleTable(this,league)).append(" middle ")
		.appendWhere()
		.appendEquals("memb."+COLUMN_NAME_REPORT_ID, ((Integer)report.getPrimaryKey()).intValue());

		if(gender!=null){
			sql.appendAnd();
			sql.appendEqualsQuoted("memb."+COLUMN_NAME_GENDER, gender);
		}

		if(stamp!=null){
			sql.appendAnd();
			sql.append("memb."+COLUMN_NAME_DATE_OF_BIRTH)
			.appendLessThanOrEqualsSign()
			.appendSingleQuote().append(stamp.toSQLString()).appendSingleQuote();
		}

		sql.appendAnd();

		sql.append("memb.")
		.append(IDColumnName)
		.appendEqualSign()
		.append("middle.")
		.append(IDColumnName);

		try {
			return idoGetNumberOfRecords(sql);
		}
		catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private int getCountOfPlayersYoungerThanAgeAndByGenderAndWorkReport(int age,String gender, WorkReport report) {
		WorkReportGroup league = null;
		try {
			league = (WorkReportGroup) report.getLeagues().toArray()[0];
		} catch(Exception e) {
			// no leagues -> no players.printStackTrace();
			return 0;
		}
		IDOQuery sql = idoQuery();
		IWTimestamp stamp = getYearlyAgeBorderIWTimestamp(age,report.getYearOfReport().intValue());
		String leagueIDColumnName =  "ISI_WR_GROUP_ID";
		String IDColumnName = getIDColumnName();

		sql.appendSelectCountFrom(this.getEntityName()).append(" memb, ")
		.append(getNameOfMiddleTable(this,league)).append(" middle ")
		.appendWhere()
		.appendEquals("memb."+COLUMN_NAME_REPORT_ID, ((Integer)report.getPrimaryKey()).intValue());

		if(gender!=null){
			sql.appendAnd();
			sql.appendEqualsQuoted("memb."+COLUMN_NAME_GENDER, gender);
		}

		if(stamp!=null){
			sql.appendAnd();
			sql.append("memb."+COLUMN_NAME_DATE_OF_BIRTH)
			.appendGreaterThanSign()
			.appendSingleQuote().append(stamp.toSQLString()).appendSingleQuote();
		}

		sql.appendAnd();

		sql.append("memb.")
		.append(IDColumnName)
		.appendEqualSign()
		.append("middle.")
		.append(IDColumnName);


		try {
			return idoGetNumberOfRecords(sql);
		}
		catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private int getCountOfMembersOfYoungerAgeByGenderAndWorkReport(int age,String gender, WorkReport report) {
		IDOQuery sql = idoQuery();
		IWTimestamp stamp = getYearlyAgeBorderIWTimestamp(age,report.getYearOfReport().intValue());
		String IDColumnName = getIDColumnName();
		
		sql.appendSelectCountFrom(this.getEntityName()).append(" memb ")
		.appendWhere()
		.appendEquals("memb."+COLUMN_NAME_REPORT_ID, ((Integer)report.getPrimaryKey()).intValue());
		if(gender!=null){
			sql.appendAnd()
			.appendEqualsQuoted("memb."+COLUMN_NAME_GENDER, gender);
			
		}
		if(stamp!=null){
			sql.appendAnd()
			.append("memb."+COLUMN_NAME_DATE_OF_BIRTH)
			.appendGreaterThanSign()
			.appendSingleQuote().append(stamp.toSQLString()).appendSingleQuote();
		}
		
		try {
			return idoGetNumberOfRecords(sql);
		}
		catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private int getCountOfMembersOfEqualOrOlderThanAgeByGenderAndWorkReport(int age,String gender, WorkReport report) {
		IDOQuery sql = idoQuery();
		IWTimestamp stamp = getYearlyAgeBorderIWTimestamp(age,report.getYearOfReport().intValue());
		String IDColumnName = getIDColumnName();
		
		sql.appendSelectCountFrom(this.getEntityName()).append(" memb ")
		.appendWhere()
		.appendEquals("memb."+COLUMN_NAME_REPORT_ID, ((Integer)report.getPrimaryKey()).intValue());
		if(gender!=null){
			sql.appendAnd()
			.appendEqualsQuoted("memb."+COLUMN_NAME_GENDER, gender);
		}
		if(stamp!=null){
			sql.appendAnd()
			.append("memb."+COLUMN_NAME_DATE_OF_BIRTH)
			.appendLessThanOrEqualsSign()
			.appendSingleQuote().append(stamp.toSQLString()).appendSingleQuote();
		}
		
		try {
			return idoGetNumberOfRecords(sql);
		}
		catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//any age
	public int ejbHomeGetCountOfPlayersByWorkReportAndWorkReportGroup(WorkReport report,WorkReportGroup league) {
		return getCountOfPlayersEqualOrOlderThanAgeAndByGenderWorkReportAndWorkReportGroup(-1,null,report,league);
	}
	
	public int ejbHomeGetCountOfMalePlayersByWorkReportAndWorkReportGroup(WorkReport report,WorkReportGroup league) {
		return getCountOfPlayersEqualOrOlderThanAgeAndByGenderWorkReportAndWorkReportGroup(-1,MALE,report,league);
	}
	
	public int ejbHomeGetCountOfFemalePlayersByWorkReportAndWorkReportGroup(WorkReport report,WorkReportGroup league) {
		return getCountOfPlayersEqualOrOlderThanAgeAndByGenderWorkReportAndWorkReportGroup(-1,FEMALE,report,league);
	}
	
	public int ejbHomeGetCountOfMembersByWorkReport(WorkReport report) {
		return getCountOfMembersOfEqualOrOlderThanAgeByGenderAndWorkReport(-1,null,report);
	}
	
	public int ejbHomeGetCountOfMaleMembersByWorkReport(WorkReport report) {
		return getCountOfMembersOfEqualOrOlderThanAgeByGenderAndWorkReport(-1,MALE,report);
	}
	
	public int ejbHomeGetCountOfFemaleMembersByWorkReport(WorkReport report) {
		return getCountOfMembersOfEqualOrOlderThanAgeByGenderAndWorkReport(-1,FEMALE,report);
	}
	
	public int ejbHomeGetCountOfPlayersByWorkReport(WorkReport report) {
		return getCountOfPlayersEqualOrOlderThanAgeAndByGenderAndWorkReport(-1,null,report);
	}

	public int ejbHomeGetCountOfMalePlayersByWorkReport(WorkReport report) {
		return getCountOfPlayersEqualOrOlderThanAgeAndByGenderAndWorkReport(-1,MALE,report);
	}

	public int ejbHomeGetCountOfFemalePlayersByWorkReport(WorkReport report) {
		return getCountOfPlayersEqualOrOlderThanAgeAndByGenderAndWorkReport(-1,FEMALE,report);
	}
	
	//equal
	public int ejbHomeGetCountOfPlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(int age, String genderMorF, WorkReport report,WorkReportGroup league) {
		return getCountOfPlayersOfEqualAgeAndByGenderWorkReportAndWorkReportGroup(age,genderMorF,report,league);
	}
	
	public int ejbHomeGetCountOfPlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(int age,WorkReport report,WorkReportGroup league) {
		return getCountOfPlayersOfEqualAgeAndByGenderWorkReportAndWorkReportGroup(age,null,report,league);
	}

	public int ejbHomeGetCountOfMalePlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(int age,WorkReport report,WorkReportGroup league) {
		return getCountOfPlayersOfEqualAgeAndByGenderWorkReportAndWorkReportGroup(age,MALE,report,league);
	}

	public int ejbHomeGetCountOfFemalePlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(int age,WorkReport report,WorkReportGroup league) {
		return getCountOfPlayersOfEqualAgeAndByGenderWorkReportAndWorkReportGroup(age,FEMALE,report,league);
	}
	
	
	//equal or older
	public int ejbHomeGetCountOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int age,WorkReport report,WorkReportGroup league) {
		return getCountOfPlayersEqualOrOlderThanAgeAndByGenderWorkReportAndWorkReportGroup(age,null,report,league);
	}
	
	public int ejbHomeGetCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int age,WorkReport report,WorkReportGroup league) {
		return getCountOfPlayersEqualOrOlderThanAgeAndByGenderWorkReportAndWorkReportGroup(age,MALE,report,league);
	}
	
	public int ejbHomeGetCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int age,WorkReport report,WorkReportGroup league) {
		return getCountOfPlayersEqualOrOlderThanAgeAndByGenderWorkReportAndWorkReportGroup(age,FEMALE,report,league);
	}
	
	public int ejbHomeGetCountOfMembersEqualOrOlderThanAgeByWorkReport(int age, WorkReport report) {
		return getCountOfMembersOfEqualOrOlderThanAgeByGenderAndWorkReport(age,null,report);
	}
	
	public int ejbHomeGetCountOfMaleMembersEqualOrOlderThanAgeByWorkReport(int age, WorkReport report) {
		return getCountOfMembersOfEqualOrOlderThanAgeByGenderAndWorkReport(age,MALE,report);
	}
	
	public int ejbHomeGetCountOfFemaleMembersEqualOrOlderThanAgeByWorkReport(int age, WorkReport report) {
		return getCountOfMembersOfEqualOrOlderThanAgeByGenderAndWorkReport(age,FEMALE,report);
	}
	
	public int ejbHomeGetCountOfPlayersEqualOrOlderThanAgeByWorkReport(int age, WorkReport report) {
		return getCountOfPlayersEqualOrOlderThanAgeAndByGenderAndWorkReport(age,null,report);
	}

	public int ejbHomeGetCountOfMalePlayersEqualOrOlderThanAgeByWorkReport(int age, WorkReport report) {
		return getCountOfPlayersEqualOrOlderThanAgeAndByGenderAndWorkReport(age,MALE,report);
	}

	public int ejbHomeGetCountOfFemalePlayersEqualOrOlderThanAgeByWorkReport(int age, WorkReport report) {
		return getCountOfPlayersEqualOrOlderThanAgeAndByGenderAndWorkReport(age,FEMALE,report);
	}
	
	//younger
	public int ejbHomeGetCountOfPlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int age,WorkReport report,WorkReportGroup league) {
		return getCountOfPlayersOfYoungerAgeAndByGenderWorkReportAndWorkReportGroup(age,null,report,league);
	}
	
	public int ejbHomeGetCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int age,WorkReport report,WorkReportGroup league) {
		return getCountOfPlayersOfYoungerAgeAndByGenderWorkReportAndWorkReportGroup(age,MALE,report,league);
	}
	
	public int ejbHomeGetCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int age,WorkReport report,WorkReportGroup league) {
		return getCountOfPlayersOfYoungerAgeAndByGenderWorkReportAndWorkReportGroup(age,FEMALE,report,league);
	}

	public int ejbHomeGetCountOfMembersOfYoungerAgeByWorkReport(int age, WorkReport report) {
		return getCountOfMembersOfYoungerAgeByGenderAndWorkReport(age,null,report);
	}
	
	public int ejbHomeGetCountOfMaleMembersOfYoungerAgeByWorkReport(int age, WorkReport report) {
		return getCountOfMembersOfYoungerAgeByGenderAndWorkReport(age,MALE,report);
	}
	
	public int ejbHomeGetCountOfFemaleMembersOfYoungerAgeByWorkReport(int age, WorkReport report) {
		return getCountOfMembersOfYoungerAgeByGenderAndWorkReport(age,FEMALE,report);
	}
	
	public int ejbHomeGetCountOfPlayersOfYoungerAgeByWorkReport(int age, WorkReport report) {
		return getCountOfPlayersYoungerThanAgeAndByGenderAndWorkReport(age,null,report);
	}

	public int ejbHomeGetCountOfMalePlayersOfYoungerAgeByWorkReport(int age, WorkReport report) {
		return getCountOfPlayersYoungerThanAgeAndByGenderAndWorkReport(age,MALE,report);
	}

	public int ejbHomeGetCountOfFemalePlayersOfYoungerAgeByWorkReport(int age, WorkReport report) {
		return getCountOfPlayersYoungerThanAgeAndByGenderAndWorkReport(age,FEMALE,report);
	}
	
	public Collection getLeaguesForMember() throws IDOException {
		//could be optimized by only getting league workreportgroups
		return idoGetRelatedEntities(WorkReportGroup.class);
	}
	
	public String getStreetName() {
		return (String) getColumnValue(COLUMN_NAME_STREET_NAME);
	}
	public void setStreetName(String streetName) {
		setColumn(COLUMN_NAME_STREET_NAME, streetName);
	}
	
	public PostalCode getPostalCode() throws SQLException {
		return (PostalCode) getColumnValue(COLUMN_NAME_POSTAL_CODE_ID);
	}

	public int getPostalCodeID() {
		return getIntColumnValue(COLUMN_NAME_POSTAL_CODE_ID);
	}

	public void setPostalCode(PostalCode postalCode) {
		setColumn(COLUMN_NAME_POSTAL_CODE_ID, postalCode);
	}
	public void setPostalCodeID(int postal_code_id) {
		setColumn(COLUMN_NAME_POSTAL_CODE_ID, postal_code_id);
	}
	

	public void setHomePhone(String number){
		setColumn(COLUMN_NAME_HOME_PHONE, number);
	}
	
	public String getHomePhone(){
		return getStringColumnValue(COLUMN_NAME_HOME_PHONE);
	}
	
	public void setWorkPhone(String number){
		setColumn(COLUMN_NAME_WORK_PHONE, number);
	}
	
	public String getWorkPhone(){
		return getStringColumnValue(COLUMN_NAME_WORK_PHONE);
	}
	
	public void setFax(String number){
		setColumn(COLUMN_NAME_FAX, number);
	}
	
	public String getFax(){
		return getStringColumnValue(COLUMN_NAME_FAX);
	}
	
	public void setEmail(String email){
		setColumn(COLUMN_NAME_EMAIL, email);
	}
	
	public String getEmail(){
		return getStringColumnValue(COLUMN_NAME_EMAIL);
	}
	
	
	/* (non-Javadoc)
	 * @see javax.ejb.EJBLocalObject#remove()
	 */
	public void remove() throws RemoveException {
		try {
			idoRemoveFrom(WorkReportGroup.class);
		}
		catch (IDORemoveRelationshipException e) {
			e.printStackTrace();
		}
		super.remove();
	}

}
