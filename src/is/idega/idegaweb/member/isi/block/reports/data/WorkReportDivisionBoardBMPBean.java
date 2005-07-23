package is.idega.idegaweb.member.isi.block.reports.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.core.location.data.PostalCode;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jul 23, 2003
 */
public class WorkReportDivisionBoardBMPBean extends GenericEntity implements WorkReportDivisionBoard {

	protected final static String ENTITY_NAME = "ISI_WR_DIV_BOARD";

	// id of the work report to that this instance belongs
	protected final static String COLUMN_NAME_REPORT_ID = "ISI_WORK_REPORT_ID";
	// id of the division group
	protected final static String COLUMN_NAME_GROUP_ID = "IC_GROUP_ID";
	// id of the league group
	protected final static String COLUMN_NAME_WORK_REPORT_GROUP_ID = "ISI_WR_GROUP_ID";

	protected final static String COLUMN_NAME_HOME_PAGE = "HOME_PAGE";
	protected final static String COLUMN_NAME_PERSONAL_ID = "PERSONAL_ID";
	protected final static String COLUMN_NAME_STREET_NAME = "STREET_NAME";
	protected final static String COLUMN_NAME_POSTAL_CODE_ID = "POSTAL_CODE_ID";
	protected final static String COLUMN_NAME_FIRST_PHONE = "FIRST_PHONE";
	protected final static String COLUMN_NAME_SECOND_PHONE = "SECOND_PHONE";
	protected final static String COLUMN_NAME_FAX = "FAX";
	protected final static String COLUMN_NAME_EMAIL = "EMAIL";
    protected final static String COLUMN_NAME_HAS_NATIONAL_LEAGUE = "HAS_NATIONAL_LEAGUE";

	protected final static String COLUMN_NAME_NUMBER_OF_PLAYERS = "TOTAL_PLAYERS";
	protected final static String COLUMN_NAME_NUMBER_OF_COMPETITORS = "TOTAL_COMPETITORS";

	public WorkReportDivisionBoardBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_REPORT_ID, "Id of the work report", true, true, Integer.class, "many-to-one", WorkReport.class);
		addAttribute(COLUMN_NAME_GROUP_ID, "Group id", true, true, Integer.class, "many-to-one", Group.class);
		addAttribute(COLUMN_NAME_HOME_PAGE, "Home page", true, true, String.class, 50);
		addAttribute(COLUMN_NAME_PERSONAL_ID, "Personal id", true, true, String.class, 10);
		addAttribute(COLUMN_NAME_STREET_NAME, "Streetname", true, true, String.class);
		addAttribute(COLUMN_NAME_POSTAL_CODE_ID, "Postal code id", true, true, Integer.class, "many-to-one", PostalCode.class);
		addAttribute(COLUMN_NAME_FIRST_PHONE, "First phone number", true, true, String.class);
		addAttribute(COLUMN_NAME_SECOND_PHONE, "Second phone number", true, true, String.class);
		addAttribute(COLUMN_NAME_FAX, "Fax number", true, true, String.class);
		addAttribute(COLUMN_NAME_EMAIL, "Email", true, true, String.class);
		addAttribute(COLUMN_NAME_NUMBER_OF_PLAYERS, "Total sum of players", true, true, Integer.class);
		addAttribute(COLUMN_NAME_NUMBER_OF_COMPETITORS, "Total sum of competitors", true, true, Integer.class);
		addAttribute(COLUMN_NAME_WORK_REPORT_GROUP_ID, "Work report group id", true, true, Integer.class, "many-to-one", WorkReportGroup.class);
        addAttribute(COLUMN_NAME_HAS_NATIONAL_LEAGUE, "has national league", true, true, Boolean.class);	

        addIndex("IDX_ISI_WR_DIV_BOARD_1", COLUMN_NAME_REPORT_ID);
  }

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getHomePage() {
		return getStringColumnValue(COLUMN_NAME_HOME_PAGE);
	}

	public void setHomePage(String homePage) {
		setColumn(COLUMN_NAME_HOME_PAGE, homePage);
	}

	public String getPersonalId() {
		return getStringColumnValue(COLUMN_NAME_PERSONAL_ID);
	}

	public void setPersonalId(String pin) {
		setColumn(COLUMN_NAME_PERSONAL_ID, pin);
	}

	public int getReportId() {
		return getIntColumnValue(COLUMN_NAME_REPORT_ID);
	}

	public void setReportId(int reportId) {
		setColumn(COLUMN_NAME_REPORT_ID, reportId);
	}

	public int getNumberOfPlayers() {
		return getIntColumnValue(COLUMN_NAME_NUMBER_OF_PLAYERS);
	}

	public void setNumberOfPlayers(int totalPlayersCount) {
		setColumn(COLUMN_NAME_NUMBER_OF_PLAYERS, totalPlayersCount);
	}

	public int getNumberOfCompetitors() {
		return getIntColumnValue(COLUMN_NAME_NUMBER_OF_PLAYERS);
	}

	public void setNumberOfCompetitors(int totalCompetitorsCount) {
		setColumn(COLUMN_NAME_NUMBER_OF_PLAYERS, totalCompetitorsCount);
	}

	public int getGroupId() {
		return getIntColumnValue(COLUMN_NAME_GROUP_ID);
	}

	public void setGroupId(int groupId) {
		setColumn(COLUMN_NAME_GROUP_ID, groupId);
	}

	public WorkReportGroup getLeague() throws IDOException {
		return (WorkReportGroup)getColumnValue(COLUMN_NAME_WORK_REPORT_GROUP_ID);
	}

	public void setLeague(int workReportGroupId) {
		setColumn(COLUMN_NAME_WORK_REPORT_GROUP_ID, workReportGroupId);
	}

	public String getStreetName() {
		return (String)getColumnValue(COLUMN_NAME_STREET_NAME);
	}
	public void setStreetName(String streetName) {
		setColumn(COLUMN_NAME_STREET_NAME, streetName);
	} 

	public PostalCode getPostalCode() throws SQLException {
		return (PostalCode)getColumnValue(COLUMN_NAME_POSTAL_CODE_ID);
	}

	public int getPostalCodeID() {
		return getIntColumnValue(COLUMN_NAME_POSTAL_CODE_ID);
	}

	public void setPostalCode(PostalCode postalCode) {
		setColumn(COLUMN_NAME_POSTAL_CODE_ID, postalCode);
	}
	public void setPostalCodeID(int postalCodeId) {
		setColumn(COLUMN_NAME_POSTAL_CODE_ID, postalCodeId);
	}

	public void setFirstPhone(String number) {
		setColumn(COLUMN_NAME_FIRST_PHONE, number);
	}

	public String getFirstPhone() {
		return getStringColumnValue(COLUMN_NAME_FIRST_PHONE);
	}

	public void setSecondPhone(String number) {
		setColumn(COLUMN_NAME_SECOND_PHONE, number);
	}

	public String getSecondPhone() {
		return getStringColumnValue(COLUMN_NAME_SECOND_PHONE);
	}

	public void setFax(String number) {
		setColumn(COLUMN_NAME_FAX, number);
	}

	public String getFax() {
		return getStringColumnValue(COLUMN_NAME_FAX);
	}

	public void setEmail(String email) {
		setColumn(COLUMN_NAME_EMAIL, email);
	}

	public String getEmail() {
		return getStringColumnValue(COLUMN_NAME_EMAIL);
	}

	public void setWorkReportGroupID(int workReportGroupID) {
		setColumn(COLUMN_NAME_WORK_REPORT_GROUP_ID, workReportGroupID);
	}

	public int getWorkReportGroupID() {
		return getIntColumnValue(COLUMN_NAME_WORK_REPORT_GROUP_ID);
	}
  
    public void setHasNationalLeague(boolean aBoolean)  {
      setColumn(COLUMN_NAME_HAS_NATIONAL_LEAGUE, aBoolean);
    }
    
    public boolean hasNationalLeague()  {
      return getBooleanColumnValue(COLUMN_NAME_HAS_NATIONAL_LEAGUE, false);
    }

	public Collection ejbFindAllWorkReportDivisionBoardByWorkReportId(int reportId) throws FinderException {
		return idoFindAllIDsByColumnOrderedBySQL(COLUMN_NAME_REPORT_ID, reportId);
	}
	
	public Integer ejbFindWorkReportDivisionBoardByWorkReportIdAndWorkReportGroupId(int reportId, int wrGroupId) throws FinderException{
		IDOQuery sql = idoQuery();
		sql.appendSelect().append(getIDColumnName()).appendFrom().append(this.getEntityName());
		sql.appendWhereEquals(COLUMN_NAME_WORK_REPORT_GROUP_ID,wrGroupId);
		sql.appendAndEquals(COLUMN_NAME_REPORT_ID,reportId);
		
		
		return (Integer) this.idoFindOnePKByQuery(sql);
	}	
	
}