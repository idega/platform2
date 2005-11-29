package is.idega.idegaweb.campus.block.application.data;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationBMPBean;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * A specific application for the campus system.
 * 
 * @author <a href="mailto:palli@idega.is">Pall Helgason </a>
 * @version 1.0
 */
public class CampusApplicationBMPBean extends GenericEntity implements
		CampusApplication {
	private static final String ENTITY_NAME = "cam_application";

	private static final String COLUMN_APPLICATION = "app_application_id";

	private static final String COLUMN_CURR_RESIDENCE = "cam_curr_res_id";

	private static final String COLUMN_SPOUSE_OCCUPATION = "cam_spouse_occ_id";

	private static final String COLUMN_STUDY_BEGIN_MONTH = "study_begin_mo";

	private static final String COLUMN_STUDY_BEGIN_YEAR = "study_begin_yr";

	private static final String COLUMN_STUDY_END_MONTH = "study_end_mo";

	private static final String COLUMN_STUDY_END_YEAR = "study_end_yr";

	private static final String COLUMN_FACULTY = "faculty";

	private static final String COLUMN_STUDY_TRACK = "study_track";

	private static final String COLUMN_SPOUSE_NAME = "spouse_name";

	private static final String COLUMN_SPOUSE_SSN = "spouse_ssn";

	private static final String COLUMN_SPOUSE_SCHOOL = "spouse_school";

	private static final String COLUMN_SPOUSE_STUDY_TRACK = "spouse_study_track";

	private static final String COLUMN_SPOUSE_STUDY_BEGIN_MONTH = "spouse_study_begin_mo";

	private static final String COLUMN_SPOUSE_BEGIN_YEAR = "spouse_study_begin_yr";

	private static final String COLUMN_SPOUSE_STUDY_END_MONTH = "spouse_study_end_mo";

	private static final String COLUMN_SPOUSE_STUDY_END_YEAR = "spouse_study_end_yr";

	private static final String COLUMN_CHILDREN = "children";

	private static final String COLUMN_INCOME = "income";

	private static final String COLUMN_SPOUSE_INCOME = "spouse_income";

	private static final String COLUMN_HOUSING_FROM = "housing_from";

	private static final String COLUMN_ON_WAITING_LIST = "on_waitinglist";

	private static final String COLUMN_WANT_FURNITURE = "want_furniture";

	private static final String COLUMN_CONTACT_PHONE = "contact_phone";

	private static final String COLUMN_OTHER_INFO = "other_info";

	private static final String COLUMN_EMAIL = "email";

	private static final String COLUMN_PRIORITY_LEVEL = "priority_level";

	private static final String COLUMN_SCHOOL = "school";

	public CampusApplicationBMPBean() {
		super();
	}

	public CampusApplicationBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addOneToOneRelationship(COLUMN_APPLICATION, Application.class);
		addManyToOneRelationship(COLUMN_CURR_RESIDENCE, CurrentResidency.class);
		addManyToOneRelationship(COLUMN_SPOUSE_OCCUPATION,
				SpouseOccupation.class);
		addAttribute(COLUMN_STUDY_BEGIN_MONTH, "Study begins (month)", true,
				true, Integer.class);
		addAttribute(COLUMN_STUDY_BEGIN_YEAR, "Study begins (year)", true,
				true, Integer.class);
		addAttribute(COLUMN_STUDY_END_MONTH, "Study ends (month)", true, true,
				Integer.class);
		addAttribute(COLUMN_STUDY_END_YEAR, "Study ends (year)", true, true,
				Integer.class);
		addAttribute(COLUMN_FACULTY, "Faculty", true, true, String.class, 255);
		addAttribute(COLUMN_STUDY_TRACK, "Study track", true, true, String.class, 255);
		addAttribute(COLUMN_SPOUSE_NAME, "Spouses name", true, true, String.class, 255);
		addAttribute(COLUMN_SPOUSE_SSN, "Spouses SSN", true, true, String.class, 20);
		addAttribute(COLUMN_SPOUSE_SCHOOL, "Spouses school", true, true, String.class,
				255);
		addAttribute(COLUMN_SPOUSE_STUDY_TRACK, "Spouses study track", true, true,
				String.class, 255);
		addAttribute(COLUMN_SPOUSE_STUDY_BEGIN_MONTH, "Spouses study begins (month)",
				true, true, Integer.class);
		addAttribute(COLUMN_SPOUSE_BEGIN_YEAR, "Spouses study begins (year)",
				true, true, Integer.class);
		addAttribute(COLUMN_SPOUSE_STUDY_END_MONTH, "Spouses study ends (month)", true,
				true, Integer.class);
		addAttribute(COLUMN_SPOUSE_STUDY_END_YEAR, "Spouses study ends (year)", true,
				true, Integer.class);
		addAttribute(COLUMN_CHILDREN, "Children info", true, true, String.class, 4000);
		addAttribute(COLUMN_INCOME, "Income", true, true, Integer.class);
		addAttribute(COLUMN_SPOUSE_INCOME, "Spouses income", true, true, Integer.class);
		addAttribute(COLUMN_HOUSING_FROM, "Want housing from", true, true,
				Date.class);
		addAttribute(COLUMN_ON_WAITING_LIST, "Want to be on waiting list", true, true,
				Boolean.class);
		addAttribute(COLUMN_WANT_FURNITURE, "Want to rent furniture", true, true,
				Boolean.class);
		addAttribute(COLUMN_CONTACT_PHONE, "If not reachable, call", true, true,
				String.class, 40);
		addAttribute(COLUMN_OTHER_INFO, "Other info", true, true, String.class, 4000);
		addAttribute(COLUMN_EMAIL, "Email", true, true, String.class, 255);
		addAttribute(COLUMN_PRIORITY_LEVEL, "Priority level", true, true,
				String.class, 1);
		addManyToOneRelationship(COLUMN_SCHOOL, School.class);
		setNullable(COLUMN_CURR_RESIDENCE, true);
		setNullable(COLUMN_SPOUSE_OCCUPATION, true);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getApplicationIdColumnName() {
		return COLUMN_APPLICATION;
	}

	public Application getApplication() {
		return (Application) getColumnValue(COLUMN_APPLICATION);
	}

	public String getCurrentResidenceIdColumnName() {
		return COLUMN_CURR_RESIDENCE;
	}

	public String getSpouseOccupationIdColumnName() {
		return COLUMN_SPOUSE_OCCUPATION;
	}

	public String getStudyBeginMonthColumnName() {
		return COLUMN_STUDY_BEGIN_MONTH;
	}

	public String getStudyBeginYearColumnName() {
		return COLUMN_STUDY_BEGIN_YEAR;
	}

	public String getStudyEndMonthColumnName() {
		return COLUMN_STUDY_END_MONTH;
	}

	public String getStudyEndYearColumnName() {
		return COLUMN_STUDY_END_YEAR;
	}

	public String getFacultyColumnName() {
		return COLUMN_FACULTY;
	}

	public String getStudyTrackColumnName() {
		return COLUMN_STUDY_TRACK;
	}

	public String getSpouseNameColumnName() {
		return COLUMN_SPOUSE_NAME;
	}

	public String getSpouseSSNColumnName() {
		return COLUMN_SPOUSE_SSN;
	}

	public String getSpouseSchoolColumnName() {
		return COLUMN_SPOUSE_SCHOOL;
	}

	public String getSpouseStudyTrackColumnName() {
		return COLUMN_SPOUSE_STUDY_TRACK;
	}

	public String getSpouseStudyBeginMonthColumnName() {
		return COLUMN_SPOUSE_STUDY_BEGIN_MONTH;
	}

	public String getSpouseStudyBeginYearColumnName() {
		return COLUMN_SPOUSE_BEGIN_YEAR;
	}

	public String getSpouseStudyEndMonthColumnName() {
		return COLUMN_SPOUSE_STUDY_END_MONTH;
	}

	public String getSpouseStudyEndYearColumnName() {
		return COLUMN_SPOUSE_STUDY_END_YEAR;
	}

	public String getChildrenColumnName() {
		return COLUMN_CHILDREN;
	}

	public String getIncomeColumnName() {
		return COLUMN_INCOME;
	}

	public String getSpouseIncomeColumnName() {
		return COLUMN_SPOUSE_INCOME;
	}

	public String getHousingFromColumnName() {
		return COLUMN_HOUSING_FROM;
	}

	public String getOnWaitinglistColumnName() {
		return COLUMN_ON_WAITING_LIST;
	}

	public String getWantFurnitureColumnName() {
		return COLUMN_WANT_FURNITURE;
	}

	public String getContactPhoneColumnName() {
		return COLUMN_CONTACT_PHONE;
	}

	public String getOtherInfoColumnName() {
		return COLUMN_OTHER_INFO;
	}

	public String getEmailColumnName() {
		return COLUMN_EMAIL;
	}

	public void setAppApplicationId(int id) {
		setColumn(COLUMN_APPLICATION, id);
	}

	public void setAppApplicationId(Integer id) {
		setColumn(COLUMN_APPLICATION, id);
	}

	public Integer getAppApplicationId() {
		return getIntegerColumnValue(COLUMN_APPLICATION);
	}

	public Integer getCurrentResidenceId() {
		return getIntegerColumnValue(COLUMN_CURR_RESIDENCE);
	}

	public Integer getSpouseOccupationId() {
		return getIntegerColumnValue(COLUMN_SPOUSE_OCCUPATION);
	}

	public Integer getStudyBeginMonth() {
		return getIntegerColumnValue(COLUMN_STUDY_BEGIN_MONTH);
	}

	public Integer getStudyBeginYear() {
		return getIntegerColumnValue(COLUMN_STUDY_BEGIN_YEAR);
	}

	public Integer getStudyEndMonth() {
		return getIntegerColumnValue(COLUMN_STUDY_END_MONTH);
	}

	public Integer getStudyEndYear() {
		return getIntegerColumnValue(COLUMN_STUDY_END_YEAR);
	}

	public String getFaculty() {
		return getStringColumnValue(COLUMN_FACULTY);
	}

	public String getStudyTrack() {
		return getStringColumnValue(COLUMN_STUDY_TRACK);
	}

	public String getSpouseName() {
		return getStringColumnValue(COLUMN_SPOUSE_NAME);
	}

	public String getSpouseSSN() {
		return getStringColumnValue(COLUMN_SPOUSE_SSN);
	}

	public String getSpouseSchool() {
		return getStringColumnValue(COLUMN_SPOUSE_SCHOOL);
	}

	public String getSpouseStudyTrack() {
		return getStringColumnValue(COLUMN_SPOUSE_STUDY_TRACK);
	}

	public Integer getSpouseStudyBeginMonth() {
		return getIntegerColumnValue(COLUMN_SPOUSE_STUDY_BEGIN_MONTH);
	}

	public Integer getSpouseStudyBeginYear() {
		return getIntegerColumnValue(COLUMN_SPOUSE_BEGIN_YEAR);
	}

	public Integer getSpouseStudyEndMonth() {
		return getIntegerColumnValue(COLUMN_SPOUSE_STUDY_END_MONTH);
	}

	public Integer getSpouseStudyEndYear() {
		return getIntegerColumnValue(COLUMN_SPOUSE_STUDY_END_YEAR);
	}

	public String getChildren() {
		return getStringColumnValue(COLUMN_CHILDREN);
	}

	public Integer getIncome() {
		return getIntegerColumnValue(COLUMN_INCOME);
	}

	public Integer getSpouseIncome() {
		return getIntegerColumnValue(COLUMN_SPOUSE_INCOME);
	}

	public Date getHousingFrom() {
		return (Date) getColumnValue(COLUMN_HOUSING_FROM);
	}

	public boolean getOnWaitinglist() {
		return getBooleanColumnValue(COLUMN_ON_WAITING_LIST, true);
	}

	public boolean getWantFurniture() {
		return getBooleanColumnValue(COLUMN_WANT_FURNITURE, false);
	}

	public String getContactPhone() {
		return getStringColumnValue(COLUMN_CONTACT_PHONE);
	}

	public String getOtherInfo() {
		return getStringColumnValue(COLUMN_OTHER_INFO);
	}

	public String getEmail() {
		return getStringColumnValue(COLUMN_EMAIL);
	}
	
	public int getSchoolID() {
		return getIntColumnValue(COLUMN_SCHOOL);
	}
	
	public School getSchool() {
		return (School) getColumnValue(COLUMN_SCHOOL);		
	}

	public void setCurrentResidenceId(Integer id) {
		setColumn(COLUMN_CURR_RESIDENCE, id);
	}

	public void setCurrentResidenceId(int id) {
		setColumn(COLUMN_CURR_RESIDENCE, id);
	}

	public void setSpouseOccupationId(Integer id) {
		setColumn(COLUMN_SPOUSE_OCCUPATION, id);
	}

	public void setSpouseOccupationId(int id) {
		setColumn(COLUMN_SPOUSE_OCCUPATION, id);
	}

	public void setStudyBeginMonth(Integer month) {
		setColumn(COLUMN_STUDY_BEGIN_MONTH, month);
	}

	public void setStudyBeginMonth(int month) {
		setColumn(COLUMN_STUDY_BEGIN_MONTH, month);
	}

	public void setStudyBeginYear(Integer year) {
		setColumn(COLUMN_STUDY_BEGIN_YEAR, year);
	}

	public void setStudyBeginYear(int year) {
		setColumn(COLUMN_STUDY_BEGIN_YEAR, year);
	}

	public void setStudyEndMonth(Integer month) {
		setColumn(COLUMN_STUDY_END_MONTH, month);
	}

	public void setStudyEndMonth(int month) {
		setColumn(COLUMN_STUDY_END_MONTH, month);
	}

	public void setStudyEndYear(Integer year) {
		setColumn(COLUMN_STUDY_END_YEAR, year);
	}

	public void setStudyEndYear(int year) {
		setColumn(COLUMN_STUDY_END_YEAR, year);
	}

	public void setFaculty(String faculty) {
		setColumn(COLUMN_FACULTY, faculty);
	}

	public void setStudyTrack(String studyTrack) {
		setColumn(COLUMN_STUDY_TRACK, studyTrack);
	}

	public void setSpouseName(String spouseName) {
		setColumn(COLUMN_SPOUSE_NAME, spouseName);
	}

	public void setSpouseSSN(String ssn) {
		setColumn(COLUMN_SPOUSE_SSN, ssn);
	}

	public void setSpouseSchool(String school) {
		setColumn(COLUMN_SPOUSE_SCHOOL, school);
	}

	public void setSpouseStudyTrack(String studyTrack) {
		setColumn(COLUMN_SPOUSE_STUDY_TRACK, studyTrack);
	}

	public void setSpouseStudyBeginMonth(Integer month) {
		setColumn(COLUMN_SPOUSE_STUDY_BEGIN_MONTH, month);
	}

	public void setSpouseStudyBeginMonth(int month) {
		setColumn(COLUMN_SPOUSE_STUDY_BEGIN_MONTH, month);
	}

	public void setSpouseStudyBeginYear(Integer year) {
		setColumn(COLUMN_SPOUSE_BEGIN_YEAR, year);
	}

	public void setSpouseStudyBeginYear(int year) {
		setColumn(COLUMN_SPOUSE_BEGIN_YEAR, year);
	}

	public void setSpouseStudyEndMonth(Integer month) {
		setColumn(COLUMN_SPOUSE_STUDY_END_MONTH, month);
	}

	public void setSpouseStudyEndMonth(int month) {
		setColumn(COLUMN_SPOUSE_STUDY_END_MONTH, month);
	}

	public void setSpouseStudyEndYear(Integer year) {
		setColumn(COLUMN_SPOUSE_STUDY_END_YEAR, year);
	}

	public void setSpouseStudyEndYear(int year) {
		setColumn(COLUMN_SPOUSE_STUDY_END_YEAR, year);
	}

	public void setChildren(String children) {
		setColumn(COLUMN_CHILDREN, children);
	}

	public void setIncome(Integer income) {
		setColumn(COLUMN_INCOME, income);
	}

	public void setIncome(int income) {
		setColumn(COLUMN_INCOME, income);
	}

	public void setSpouseIncome(Integer income) {
		setColumn(COLUMN_SPOUSE_INCOME, income);
	}

	public void setSpouseIncome(int income) {
		setColumn(COLUMN_SPOUSE_INCOME, income);
	}

	public void setHousingFrom(Date from) {
		setColumn(COLUMN_HOUSING_FROM, from);
	}

	public void setOnWaitinglist(boolean putOnList) {
		setColumn(COLUMN_ON_WAITING_LIST, putOnList);
	}

	public void setWantFurniture(boolean furniture) {
		setColumn(COLUMN_WANT_FURNITURE, furniture);
	}

	public void setContactPhone(String contactPhone) {
		setColumn(COLUMN_CONTACT_PHONE, contactPhone);
	}

	public void setOtherInfo(String info) {
		setColumn(COLUMN_OTHER_INFO, info);
	}

	public void setEmail(String email) {
		setColumn(COLUMN_EMAIL, email);
	}

	public String getPriorityLevel() {
		return getStringColumnValue(COLUMN_PRIORITY_LEVEL);
	}

	public void setPriorityLevel(String level) {
		setColumn(COLUMN_PRIORITY_LEVEL, level);
	}
	
	public void setSchoolID(int schoolID) {
		setColumn(COLUMN_SCHOOL, schoolID);
	}
	
	public void setSchool(School school) {
		setColumn(COLUMN_SCHOOL, school);		
	}

	public static String getPriorityColumnName() {
		return COLUMN_PRIORITY_LEVEL;
	}

	public java.util.Collection ejbFindAllByApplicationId(int id)
			throws javax.ejb.FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getTableName());
		sql.append(" where ");
		sql.append(COLUMN_APPLICATION);
		sql.append(" = ");
		sql.append(id);
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySubjectAndStatus(Integer subjectID,
			String status, String order) throws FinderException {
		return ejbFindBySubjectAndStatus(subjectID, status, order, -1, -1);

	}

	public Collection ejbFindBySubjectAndStatus(Integer subjectID,
			String status, String order, int numberOfRecords, int startingIndex)
			throws FinderException {
		try {
			return idoFindPKsBySQL(getSQLBySubjectAndStatus(subjectID, status,
					order, false), numberOfRecords, startingIndex);
		} catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}

	}

	public int ejbHomeGetCountBySubjectAndStatus(Integer subjectID,
			String status) throws IDORelationshipException, IDOException {
		return super.idoGetNumberOfRecords(getSQLBySubjectAndStatus(subjectID,
				status, null, true));
	}

	private String getSQLBySubjectAndStatus(Integer subjectID, String status,
			String order, boolean count) throws IDORelationshipException {

		Table campusApplication = new Table(this, "c");
		Table application = new Table(Application.class, "a");
		Table applicant = new Table(Applicant.class, "b");

		SelectQuery query = new SelectQuery(campusApplication);
		query.setAsCountQuery(count);
		// query.setAsDistinct(true);
		if (!count)
			query.addColumn(new WildCardColumn(campusApplication));
		else
			query.addColumn(new WildCardColumn());

		query.addJoin(campusApplication, application);
		query.addJoin(application, applicant);
		if (subjectID != null && subjectID.intValue() > 0)
			query.addCriteria(new MatchCriteria(new Column(application,
					ApplicationBMPBean.getSubjectIdColumnName()),
					MatchCriteria.EQUALS, subjectID.intValue()));
		if (status != null)
			query.addCriteria(new MatchCriteria(new Column(application,
					ApplicationBMPBean.getStatusColumnName()),
					MatchCriteria.EQUALS, status));
		if (order != null && !count)
			query.addOrder(applicant, order, true);
		return query.toString();
	}

	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindBySQL(String sql) throws FinderException {
		return super.idoFindPKsBySQL(sql);
	}

	public Collection getApplied() {
		try {
			return ((AppliedHome) IDOLookup.getHome(Applied.class))
					.findByApplicationID((Integer) this.getPrimaryKey());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getApplied() : "
					+ e.getMessage());
		}
	}

	public Collection ejbFindByApartmentTypeAndComplex(Integer typeId,
			Integer complexID) throws FinderException {
		StringBuffer sql = new StringBuffer("select ");
		sql.append(" distinct ");
		sql.append(getEntityName());
		sql.append(".* ");
		sql
				.append(" from cam_application ca,app_application an,app_applicant aa");
		sql.append(",cam_waiting_list wl ,cam_applied ad");
		sql.append(" where ca.app_application_id = an.app_application_id ");
		sql.append(" and an.app_applicant_id = aa.app_applicant_id ");
		sql.append(" and aa.app_applicant_id = wl.app_applicant_id ");
		if (typeId != null && typeId.intValue() > 0) {
			sql.append(" and wl.bu_apartment_type_id =  ");
			sql.append(typeId);
		}
		if (complexID != null && complexID.intValue() > 0) {
			sql.append(" and wl.bu_complex_id =  ");
			sql.append(complexID);
		}
		return super.idoFindPKsBySQL(sql.toString());
	}
}