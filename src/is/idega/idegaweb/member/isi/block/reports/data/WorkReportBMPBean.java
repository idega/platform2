/*
 * Created on May 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDOReportableEntity;
import com.idega.user.data.Group;

/**
 * Description: The head data bean for the work reports. One Bean of this type
 * represents a clubs yearly work report. <br>Copyright: Idega Software 2003
 * <br>Company: Idega Software <br>
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportBMPBean extends GenericEntity implements WorkReport, IDOReportableEntity {
	protected final static String ENTITY_NAME = "ISI_WORK_REPORT";
	protected final static String COLUMN_NAME_GROUP_ID = "GROUP_ID"; //Could
																	 // be a
																	 // club,league
																	 // or a
																	 // regional
																	 // union
	protected final static String COLUMN_NAME_REGIONAL_UNION_GROUP_ID = "REG_UNI_GR_ID"; //a
																						 // connection
																						 // for
																						 // a
																						 // club

	protected final static String COLUMN_NAME_GROUP_NAME = "GROUP_NAME";
	protected final static String COLUMN_NAME_GROUP_TYPE = "GROUP_TYPE";
	protected final static String COLUMN_NAME_GROUP_SHORT_NAME = "GROUP_SHORT_NAME";
	protected final static String COLUMN_NAME_GROUP_NUMBER = "GROUP_NUMBER";
	protected final static String COLUMN_NAME_GROUP_INACTIVE = "INACTIVE_GROUP";
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
	protected final static String COLUMN_NAME_NUMBER_OF_MEMBERS = "TOTAL_MEMBERS";
	protected final static String COLUMN_NAME_NUMBER_OF_PLAYERS = "TOTAL_PLAYERS";
	protected final static String COLUMN_NAME_NUMBER_OF_COMPETITORS = "TOTAL_COMPETITORS";
	protected final static String COLUMN_NAME_REGIONAL_UNION_NAME = "REG_UNI_NAME";
	protected final static String COLUMN_NAME_REGIONAL_UNION_NR = "REG_UNI_NR";
	protected final static String COLUMN_NAME_REGIONAL_UNION_ABBR = "REG_UNI_ABBR";
	protected final static String COLUMN_NAME_CONTINUANCE_TILL = "CONTINUANCE_TILL";

	protected final static String COLUMN_NAME_CLUB_TYPE = "CLUB_TYPE";
	protected final static String COLUMN_NAME_IS_IN_UMFI = "IN_UMFI";

	public WorkReportBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_GROUP_ID, "Group id of club/league/regional union", true, true, Integer.class, "many-to-one", Group.class);
		addAttribute(COLUMN_NAME_REGIONAL_UNION_GROUP_ID, "Regional union group id", true, true, Integer.class, "many-to-one", Group.class);

		addAttribute(COLUMN_NAME_MEMBER_FILE_ID, "Members-part file id", true, true, Integer.class, "many-to-one", ICFile.class);
		addAttribute(COLUMN_NAME_ACCOUNT_FILE_ID, "Account-part file id", true, true, Integer.class, "many-to-one", ICFile.class);
		addAttribute(COLUMN_NAME_BOARD_FILE_ID, "Board-part file id", true, true, Integer.class, "many-to-one", ICFile.class);
		addAttribute(COLUMN_NAME_GROUP_NAME, "Group name", true, true, String.class);
		addAttribute(COLUMN_NAME_GROUP_SHORT_NAME, "Group short name", true, true, String.class, 30);
		addAttribute(COLUMN_NAME_GROUP_TYPE, "Group type", true, true, String.class, 30);
		addAttribute(COLUMN_NAME_GROUP_NUMBER, "Group number", true, true, String.class);
		addAttribute(COLUMN_NAME_WORK_REPORT_YEAR, "The year this report is valid for", true, true, Integer.class);
		addAttribute(COLUMN_NAME_MEMBERS_DONE, "Is the members-part of the work report finished", true, true, Boolean.class);
		addAttribute(COLUMN_NAME_ACCOUNT_DONE, "Is the account-part of the work report finished", true, true, Boolean.class);
		addAttribute(COLUMN_NAME_BOARD_DONE, "Is the board-part of the work report finished", true, true, Boolean.class);

		addAttribute(COLUMN_NAME_GROUP_INACTIVE, "Is the group inactive", true, true, Boolean.class);
		addAttribute(COLUMN_NAME_NUMBER_OF_MEMBERS, "Total sum of members", true, true, Integer.class);
		addAttribute(COLUMN_NAME_NUMBER_OF_PLAYERS, "Total sum of players", true, true, Integer.class);
		addAttribute(COLUMN_NAME_NUMBER_OF_COMPETITORS, "Total sum of players", true, true, Integer.class);

		addAttribute(COLUMN_NAME_CREATION_FROM_DATABASE_DONE, "Has the data been created from database?", true, true, Boolean.class);
		addAttribute(COLUMN_NAME_STATUS, "Status", true, true, String.class, 30);
		addAttribute(COLUMN_NAME_SENT, "Has the workreport been sent, finalized", true, true, Boolean.class);
		addAttribute(COLUMN_NAME_SENT_REPORT, "Results from report check", true, true, String.class, 3500);

		addAttribute(COLUMN_NAME_REGIONAL_UNION_NAME, "Regional union name", true, true, String.class);
		addAttribute(COLUMN_NAME_REGIONAL_UNION_NR, "Regional union nr", true, true, String.class, 30);
		addAttribute(COLUMN_NAME_REGIONAL_UNION_ABBR, "Regional union abbreviation", true, true, String.class, 30);
		addAttribute(COLUMN_NAME_CONTINUANCE_TILL, "Continuance till text field", true, true, String.class, 30);

		addAttribute(COLUMN_NAME_CLUB_TYPE, "Type of club, single-, multidivision or no members", true, true, String.class, 2);
		
		addAttribute(COLUMN_NAME_IS_IN_UMFI, "Is the club in UMFI", true, true, Boolean.class);

		addManyToManyRelationShip(WorkReportGroup.class); //so we can get the
														  // clubs related to
														  // leagues/divisions
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public Integer getGroupId() {
		return getIntegerColumnValue(COLUMN_NAME_GROUP_ID);
	}

	public void setGroupId(int groupId) {
		setColumn(COLUMN_NAME_GROUP_ID, groupId);
	}

	public void setGroupId(Integer groupId) {
		setColumn(COLUMN_NAME_GROUP_ID, groupId);
	}

	public Integer getRegionalUnionGroupId() {
		return getIntegerColumnValue(COLUMN_NAME_REGIONAL_UNION_GROUP_ID);
	}

	public void setRegionalUnionGroupId(int groupId) {
		setColumn(COLUMN_NAME_REGIONAL_UNION_GROUP_ID, groupId);
	}

	public void setRegionalUnionGroupId(Integer groupId) {
		setColumn(COLUMN_NAME_REGIONAL_UNION_GROUP_ID, groupId);
	}

	public Integer getMemberFileId() {
		return getIntegerColumnValue(COLUMN_NAME_MEMBER_FILE_ID);
	}

	public Integer getAccountFileId() {
		return getIntegerColumnValue(COLUMN_NAME_ACCOUNT_FILE_ID);
	}

	public Integer getBoardFileId() {
		return getIntegerColumnValue(COLUMN_NAME_BOARD_FILE_ID);
	}

	public void setMemberFileId(int fileId) {
		setColumn(COLUMN_NAME_MEMBER_FILE_ID, fileId);
	}

	public void setAccountFileId(int fileId) {
		setColumn(COLUMN_NAME_ACCOUNT_FILE_ID, fileId);
	}

	public void setBoardFileId(int fileId) {
		setColumn(COLUMN_NAME_BOARD_FILE_ID, fileId);
	}

	public String getGroupName() {
		return getStringColumnValue(COLUMN_NAME_GROUP_NAME);
	}

	public void setGroupShortName(String name) {
		setColumn(COLUMN_NAME_GROUP_SHORT_NAME, name);
	}

	public String getGroupShortName() {
		return getStringColumnValue(COLUMN_NAME_GROUP_SHORT_NAME);
	}

	public void setStatus(String status) {
		setColumn(COLUMN_NAME_STATUS, status);
	}

	public String getStatus() {
		return getStringColumnValue(COLUMN_NAME_STATUS);
	}

	public void setContinuanceTill(String continuanceString) {
		setColumn(COLUMN_NAME_CONTINUANCE_TILL, continuanceString);
	}

	public String getContinuanceTill() {
		return getStringColumnValue(COLUMN_NAME_CONTINUANCE_TILL);
	}

	public void setGroupName(String name) {
		setColumn(COLUMN_NAME_GROUP_NAME, name);
	}

	public String getGroupNumber() {
		return getStringColumnValue(COLUMN_NAME_GROUP_NUMBER);
	}

	public void setGroupNumber(String number) {
		setColumn(COLUMN_NAME_GROUP_NUMBER, number);
	}

	public String getRegionalUnionNumber() {
		return getStringColumnValue(COLUMN_NAME_REGIONAL_UNION_NR);
	}

	public void setRegionalUnionNumber(String number) {
		setColumn(COLUMN_NAME_REGIONAL_UNION_NR, number);
	}

	public String getRegionalUnionName() {
		return getStringColumnValue(COLUMN_NAME_REGIONAL_UNION_NAME);
	}

	public void setRegionalUnionName(String name) {
		setColumn(COLUMN_NAME_REGIONAL_UNION_NAME, name);
	}

	public String getRegionalUnionAbbreviation() {
		return getStringColumnValue(COLUMN_NAME_REGIONAL_UNION_ABBR);
	}

	public void setRegionalUnionAbbreviation(String abbr) {
		setColumn(COLUMN_NAME_REGIONAL_UNION_ABBR, abbr);
	}

	public Integer getYearOfReport() {
		return getIntegerColumnValue(COLUMN_NAME_WORK_REPORT_YEAR);
	}

	public void setYearOfReport(int year) {
		setColumn(COLUMN_NAME_WORK_REPORT_YEAR, year);
	}

	public boolean isBoardPartDone() {
		return getBooleanColumnValue(COLUMN_NAME_BOARD_DONE, false);
	}

	public void setBoardPartDone(boolean isDone) {
		setColumn(COLUMN_NAME_BOARD_DONE, isDone);
	}

	public void setAsInactive() {
		setColumn(COLUMN_NAME_GROUP_INACTIVE, true);
	}

	public void setAsActive() {
		setColumn(COLUMN_NAME_GROUP_INACTIVE, false);
	}

	public boolean isInActive() {
		return getBooleanColumnValue(COLUMN_NAME_GROUP_INACTIVE, false);
	}

	public boolean isMembersPartDone() {
		return getBooleanColumnValue(COLUMN_NAME_MEMBERS_DONE, false);
	}
	
	public void setType(String type) {
		setColumn(COLUMN_NAME_CLUB_TYPE, type);
	}
	
	public String getType() {
		return getStringColumnValue(COLUMN_NAME_CLUB_TYPE);
	}
	
	public void setIsInUMFI(boolean value) {
		setColumn(COLUMN_NAME_IS_IN_UMFI, value);
	}

	public boolean isInUMFI() {
		return getBooleanColumnValue(COLUMN_NAME_IS_IN_UMFI, false);
	}

	public void setAsSent(boolean sent) {
		setColumn(COLUMN_NAME_SENT, sent);
	}

	public boolean isSent() {
		return getBooleanColumnValue(COLUMN_NAME_SENT, false);
	}

	public void setSentReportText(String text) {
		setColumn(COLUMN_NAME_SENT_REPORT, text);
	}

	public String getSentReportText() {
		return getStringColumnValue(COLUMN_NAME_SENT_REPORT);
	}

	public void setMembersPartDone(boolean isDone) {
		setColumn(COLUMN_NAME_MEMBERS_DONE, isDone);
	}

	public boolean isAccountPartDone() {
		return getBooleanColumnValue(COLUMN_NAME_ACCOUNT_DONE, false);
	}

	public void setAccountPartDone(boolean isDone) {
		setColumn(COLUMN_NAME_ACCOUNT_DONE, isDone);
	}

	public void setCreationFromDatabaseDone(boolean isDone) {
		setColumn(COLUMN_NAME_CREATION_FROM_DATABASE_DONE, isDone);
	}

	public boolean isCreationFromDatabaseDone() {
		return getBooleanColumnValue(COLUMN_NAME_CREATION_FROM_DATABASE_DONE, false);
	}

	public Integer ejbFindWorkReportByGroupIdAndYearOfReport(int groupId, int yearOfReport) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getEntityName());
		sql.appendWhereEquals(COLUMN_NAME_GROUP_ID, groupId);
		sql.appendAndEquals(COLUMN_NAME_WORK_REPORT_YEAR, yearOfReport);

		return (Integer) this.idoFindOnePKByQuery(sql);
	}

	public Collection ejbFindAllWorkReportsByYearOrderedByRegionalUnionNumberAndGroupNumber(int yearOfReport) throws FinderException {
		IDOQuery sql = idoQuery();
		String columns[] = { COLUMN_NAME_REGIONAL_UNION_NR, COLUMN_NAME_GROUP_NUMBER };

		sql.appendSelectAllFrom(this.getEntityName());
		sql.appendWhereEquals(COLUMN_NAME_WORK_REPORT_YEAR, yearOfReport);
		sql.appendOrderBy(columns);

		return this.idoFindPKsByQuery(sql);
	}

	public Collection ejbFindAllWorkReportsByYearOrderedByGroupType(int yearOfReport) throws FinderException {
		return idoFindAllIDsByColumnOrderedBySQL(COLUMN_NAME_WORK_REPORT_YEAR, yearOfReport, COLUMN_NAME_GROUP_TYPE);
	}

	public Collection ejbFindAllWorkReportsByYearAndRegionalUnionGroupsOrderedByRegionalUnionNameAndClubName(
		int year,
		Collection regionalUnionGroups)
		throws FinderException {
		IDOQuery sql = idoQuery();
		String[] ordering = { COLUMN_NAME_REGIONAL_UNION_NAME, COLUMN_NAME_GROUP_NAME };
		sql.appendSelectAllFrom(this.getEntityName()).appendWhere().appendEquals(COLUMN_NAME_WORK_REPORT_YEAR, year);
		if (regionalUnionGroups != null && !regionalUnionGroups.isEmpty()) {
			sql.appendAnd().append(COLUMN_NAME_REGIONAL_UNION_GROUP_ID).appendInCollection(regionalUnionGroups);
		}
		sql.appendOrderBy(ordering);

		return idoFindIDsBySQL(sql.toString());

	}

	/**
	 * @param year
	 * @param regionalUnionGroups
	 * @param clubs
	 * @param leagues
	 * @return
	 * @throws FinderException
	 */
	public Collection ejbFindAllWorkReportsByYearRegionalUnionsAndClubsOrderedByRegionalUnionNameAndClubName(
		int year,
		Collection regionalUnionGroups,
		Collection clubs)
		throws FinderException {
		
		IDOQuery sql = idoQuery();
		String[] ordering = { COLUMN_NAME_REGIONAL_UNION_NAME, COLUMN_NAME_GROUP_NAME };
		String tableName = this.getEntityName();
		
		sql.appendSelectAllFrom(tableName)
		.appendWhere().appendEquals(COLUMN_NAME_WORK_REPORT_YEAR, year);
		if (regionalUnionGroups != null && !regionalUnionGroups.isEmpty()) {
			sql.appendAnd().append(COLUMN_NAME_REGIONAL_UNION_GROUP_ID).appendInCollection(regionalUnionGroups);
		}
		if (clubs != null && !clubs.isEmpty()) {
			sql.appendAnd().append(COLUMN_NAME_GROUP_ID).appendInCollection(clubs);
		}

		sql.appendOrderBy(ordering);

		return idoFindIDsBySQL(sql.toString());

	}

	public int ejbHomeGetCountOfWorkReportsByStatusAndYear(String status, int year) {
		IDOQuery sql = idoQueryGetSelectCount();

		sql.appendWhereEqualsQuoted(COLUMN_NAME_STATUS, status).appendAndEquals(COLUMN_NAME_WORK_REPORT_YEAR, year);

		try {
			return idoGetNumberOfRecords(sql);
		}
		catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public Collection getLeagues() throws IDOException {
		return idoGetRelatedEntities(WorkReportGroup.class);
	}

	public void addLeague(WorkReportGroup group) throws IDORelationshipException {
		idoAddTo(group);
	}

	public String getGroupType() {
		return getStringColumnValue(COLUMN_NAME_GROUP_TYPE);
	}

	public void setGroupType(String groupType) {
		setColumn(COLUMN_NAME_GROUP_TYPE, groupType);
	}

	public int getNumberOfMembers() {
		return getIntColumnValue(COLUMN_NAME_NUMBER_OF_MEMBERS);
	}

	public void setNumberOfMembers(int totalMembersCount) {
		setColumn(COLUMN_NAME_NUMBER_OF_MEMBERS, totalMembersCount);
	}

	public int getNumberOfCompetitors() {
		return getIntColumnValue(COLUMN_NAME_NUMBER_OF_COMPETITORS);
	}

	public void setNumberOfCompetitors(int totalCompetitorsCount) {
		setColumn(COLUMN_NAME_NUMBER_OF_COMPETITORS, totalCompetitorsCount);
	}

	public int getNumberOfPlayers() {
		return getIntColumnValue(COLUMN_NAME_NUMBER_OF_PLAYERS);
	}

	public void setNumberOfPlayers(int totalPlayersCount) {
		setColumn(COLUMN_NAME_NUMBER_OF_PLAYERS, totalPlayersCount);
	}
}
