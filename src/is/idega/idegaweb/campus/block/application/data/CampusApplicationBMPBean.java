package is.idega.idegaweb.campus.block.application.data;

import java.sql.Date;
import java.sql.SQLException;

/**
 * A specific application for the campus system.
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CampusApplicationBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.campus.block.application.data.CampusApplication {
  private static final String name_ = "cam_application";
  private static final String applicationId_ = "app_application_id";
  private static final String currentResidenceId_ = "cam_curr_res_id";
  private static final String spouseOccupationId_ = "cam_spouse_occ_id";
  private static final String studyBeginMonth_ = "study_begin_mo";
  private static final String studyBeginYear_ = "study_begin_yr";
  private static final String studyEndMonth_ = "study_end_mo";
  private static final String studyEndYear_ = "study_end_yr";
  private static final String faculty_ = "faculty";
  private static final String studyTrack_ = "study_track";
  private static final String spouseName_ = "spouse_name";
  private static final String spouseSSN_ = "spouse_ssn";
  private static final String spouseSchool_ = "spouse_school";
  private static final String spouseStudyTrack_ = "spouse_study_track";
  private static final String spouseStudyBeginMonth_ = "spouse_study_begin_mo";
  private static final String spouseStudyBeginYear_ = "spouse_study_begin_yr";
  private static final String spouseStudyEndMonth_ = "spouse_study_end_mo";
  private static final String spouseStudyEndYear_ = "spouse_study_end_yr";
  private static final String children_ = "children";
  private static final String income_ = "income";
  private static final String spouseIncome_ = "spouse_income";
  private static final String housingFrom_ = "housing_from";
  private static final String onWaitinglist_ = "on_waitinglist";
  private static final String wantFurniture_ = "want_furniture";
  private static final String contactPhone_ = "contact_phone";
  private static final String otherInfo_ = "other_info";
  private static final String email_ = "email";
  private static final String PRIORITY_LEVEL = "priority_level";

  public CampusApplicationBMPBean() {
    super();
  }

  public CampusApplicationBMPBean(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(applicationId_,"Campus application id",true,true,java.lang.Integer.class,"one-to-one",com.idega.block.application.data.Application.class);
    addAttribute(currentResidenceId_,"Current residency",true,true,java.lang.Integer.class,"one-to-many",is.idega.idegaweb.campus.block.application.data.CurrentResidency.class);
    addAttribute(spouseOccupationId_,"Spouse occupation",true,true,java.lang.Integer.class,"one-to-many",is.idega.idegaweb.campus.block.application.data.SpouseOccupation.class);
    addAttribute(studyBeginMonth_,"Study begins (month)",true,true,java.lang.Integer.class);
    addAttribute(studyBeginYear_,"Study begins (year)",true,true,java.lang.Integer.class);
    addAttribute(studyEndMonth_,"Study ends (month)",true,true,java.lang.Integer.class);
    addAttribute(studyEndYear_,"Study ends (year)",true,true,java.lang.Integer.class);
    addAttribute(faculty_,"Faculty",true,true,java.lang.String.class);
    addAttribute(studyTrack_,"Study track",true,true,java.lang.String.class);
    addAttribute(spouseName_,"Spouses name",true,true,java.lang.String.class);
    addAttribute(spouseSSN_,"Spouses SSN",true,true,java.lang.String.class);
    addAttribute(spouseSchool_,"Spouses school",true,true,java.lang.String.class);
    addAttribute(spouseStudyTrack_,"Spouses study track",true,true,java.lang.String.class);
    addAttribute(spouseStudyBeginMonth_,"Spouses study begins (month)",true,true,java.lang.Integer.class);
    addAttribute(spouseStudyBeginYear_,"Spouses study begins (year)",true,true,java.lang.Integer.class);
    addAttribute(spouseStudyEndMonth_,"Spouses study ends (month)",true,true,java.lang.Integer.class);
    addAttribute(spouseStudyEndYear_,"Spouses study ends (year)",true,true,java.lang.Integer.class);
    addAttribute(children_,"Children info",true,true,java.lang.String.class);
    addAttribute(income_,"Income",true,true,java.lang.Integer.class);
    addAttribute(spouseIncome_,"Spouses income",true,true,java.lang.Integer.class);
    addAttribute(housingFrom_,"Want housing from",true,true,java.sql.Date.class);
    addAttribute(onWaitinglist_,"Want to be on waiting list",true,true,java.lang.String.class);
    addAttribute(wantFurniture_,"Want to rent furniture",true,true,java.lang.String.class);
    addAttribute(contactPhone_,"If not reachable, call",true,true,java.lang.String.class);
    addAttribute(otherInfo_,"Other info",true,true,java.lang.String.class);
    addAttribute(email_,"Email",true,true,java.lang.String.class);
    addAttribute(PRIORITY_LEVEL,"Priority level",true,true,String.class);
    setMaxLength(faculty_,255);
    setMaxLength(studyTrack_,255);
    setMaxLength(spouseName_,255);
    setMaxLength(spouseSSN_,20);
    setMaxLength(spouseSchool_,255);
    setMaxLength(spouseStudyTrack_,255);
    setMaxLength(children_,4000);
    setMaxLength(onWaitinglist_,1);
    setMaxLength(wantFurniture_,1);
    setMaxLength(contactPhone_,40);
    setMaxLength(otherInfo_,4000);
    setMaxLength(email_,255);
    setMaxLength(PRIORITY_LEVEL,1);
    setNullable(currentResidenceId_,true);
    setNullable(spouseOccupationId_,true);
  }

  public String getEntityName() {
    return(name_);
  }

  public String getApplicationIdColumnName() {
    return(applicationId_);
  }

  public String getCurrentResidenceIdColumnName() {
    return(currentResidenceId_);
  }

  public String getSpouseOccupationIdColumnName() {
    return(spouseOccupationId_);
  }

  public String getStudyBeginMonthColumnName() {
    return(studyBeginMonth_);
  }

  public String getStudyBeginYearColumnName() {
    return(studyBeginYear_);
  }

  public String getStudyEndMonthColumnName() {
    return(studyEndMonth_);
  }

  public String getStudyEndYearColumnName() {
    return(studyEndYear_);
  }

  public String getFacultyColumnName() {
    return(faculty_);
  }

  public String getStudyTrackColumnName() {
    return(studyTrack_);
  }

  public String getSpouseNameColumnName() {
    return(spouseName_);
  }

  public String getSpouseSSNColumnName() {
    return(spouseSSN_);
  }

  public String getSpouseSchoolColumnName() {
    return(spouseSchool_);
  }

  public String getSpouseStudyTrackColumnName() {
    return(spouseStudyTrack_);
  }

  public String getSpouseStudyBeginMonthColumnName() {
    return(spouseStudyBeginMonth_);
  }

  public String getSpouseStudyBeginYearColumnName() {
    return(spouseStudyBeginYear_);
  }

  public String getSpouseStudyEndMonthColumnName() {
    return(spouseStudyEndMonth_);
  }

  public String getSpouseStudyEndYearColumnName() {
    return(spouseStudyEndYear_);
  }

  public String getChildrenColumnName() {
    return(children_);
  }

  public String getIncomeColumnName() {
    return(income_);
  }

  public String getSpouseIncomeColumnName() {
    return(spouseIncome_);
  }

  public String getHousingFromColumnName() {
    return(housingFrom_);
  }

  public String getOnWaitinglistColumnName() {
    return(onWaitinglist_);
  }

  public String getWantFurnitureColumnName() {
    return(wantFurniture_);
  }

  public String getContactPhoneColumnName() {
    return(contactPhone_);
  }

  public String getOtherInfoColumnName() {
    return(otherInfo_);
  }

  public String getEmailColumnName() {
    return(email_);
  }

  public void setAppApplicationId(int id) {
    setColumn(applicationId_,id);
  }

  public void setAppApplicationId(Integer id) {
    setColumn(applicationId_,id);
  }

  public Integer getAppApplicationId() {
    return(getIntegerColumnValue(applicationId_));
  }

  public Integer getCurrentResidenceId() {
    return(getIntegerColumnValue(currentResidenceId_));
  }

  public Integer getSpouseOccupationId() {
    return(getIntegerColumnValue(spouseOccupationId_));
  }

  public Integer getStudyBeginMonth() {
    return(getIntegerColumnValue(studyBeginMonth_));
  }

  public Integer getStudyBeginYear() {
    return(getIntegerColumnValue(studyBeginYear_));
  }

  public Integer getStudyEndMonth() {
    return(getIntegerColumnValue(studyEndMonth_));
  }

  public Integer getStudyEndYear() {
    return(getIntegerColumnValue(studyEndYear_));
  }

  public String getFaculty() {
    return(getStringColumnValue(faculty_));
  }

  public String getStudyTrack() {
    return(getStringColumnValue(studyTrack_));
  }

  public String getSpouseName() {
    return(getStringColumnValue(spouseName_));
  }

  public String getSpouseSSN() {
    return(getStringColumnValue(spouseSSN_));
  }

  public String getSpouseSchool() {
    return(getStringColumnValue(spouseSchool_));
  }

  public String getSpouseStudyTrack() {
    return(getStringColumnValue(spouseStudyTrack_));
  }

  public Integer getSpouseStudyBeginMonth() {
    return(getIntegerColumnValue(spouseStudyBeginMonth_));
  }

  public Integer getSpouseStudyBeginYear() {
    return(getIntegerColumnValue(spouseStudyBeginYear_));
  }

  public Integer getSpouseStudyEndMonth() {
    return(getIntegerColumnValue(spouseStudyEndMonth_));
  }

  public Integer getSpouseStudyEndYear() {
    return(getIntegerColumnValue(spouseStudyEndYear_));
  }

  public String getChildren() {
    return(getStringColumnValue(children_));
  }

  public Integer getIncome() {
    return(getIntegerColumnValue(income_));
  }

  public Integer getSpouseIncome() {
    return(getIntegerColumnValue(spouseIncome_));
  }

  public Date getHousingFrom() {
    return((Date)getColumnValue(housingFrom_));
  }

  public boolean getOnWaitinglist() {
    String tmp = getStringColumnValue(onWaitinglist_);
    if (tmp.equalsIgnoreCase("y"))
      return(true);
    else
      return(false);
  }

  public boolean getWantFurniture() {
    String tmp = getStringColumnValue(wantFurniture_);
    if (tmp.equalsIgnoreCase("y"))
      return(true);
    else
      return(false);
  }

  public String getContactPhone() {
    return(getStringColumnValue(contactPhone_));
  }

  public String getOtherInfo() {
    return(getStringColumnValue(otherInfo_));
  }

  public String getEmail() {
    return(getStringColumnValue(email_));
  }

  public void setCurrentResidenceId(Integer id) {
    setColumn(currentResidenceId_,id);
  }

  public void setCurrentResidenceId(int id) {
    setColumn(currentResidenceId_,id);
  }

  public void setSpouseOccupationId(Integer id) {
    setColumn(spouseOccupationId_,id);
  }

  public void setSpouseOccupationId(int id) {
    setColumn(spouseOccupationId_,id);
  }

  public void setStudyBeginMonth(Integer month) {
    setColumn(studyBeginMonth_,month);
  }

  public void setStudyBeginMonth(int month) {
    setColumn(studyBeginMonth_,month);
  }

  public void setStudyBeginYear(Integer year) {
    setColumn(studyBeginYear_,year);
  }

  public void setStudyBeginYear(int year) {
    setColumn(studyBeginYear_,year);
  }

  public void setStudyEndMonth(Integer month) {
    setColumn(studyEndMonth_,month);
  }

  public void setStudyEndMonth(int month) {
    setColumn(studyEndMonth_,month);
  }

  public void setStudyEndYear(Integer year) {
    setColumn(studyEndYear_,year);
  }

  public void setStudyEndYear(int year) {
    setColumn(studyEndYear_,year);
  }

  public void setFaculty(String faculty) {
    setColumn(faculty_,faculty);
  }

  public void setStudyTrack(String studyTrack) {
    setColumn(studyTrack_,studyTrack);
  }

  public void setSpouseName(String spouseName) {
    setColumn(spouseName_,spouseName);
  }

  public void setSpouseSSN(String ssn) {
    setColumn(spouseSSN_,ssn);
  }

  public void setSpouseSchool(String school) {
    setColumn(spouseSchool_,school);
  }

  public void setSpouseStudyTrack(String studyTrack) {
    setColumn(spouseStudyTrack_,studyTrack);
  }

  public void setSpouseStudyBeginMonth(Integer month) {
    setColumn(spouseStudyBeginMonth_,month);
  }

  public void setSpouseStudyBeginMonth(int month) {
    setColumn(spouseStudyBeginMonth_,month);
  }

  public void setSpouseStudyBeginYear(Integer year) {
    setColumn(spouseStudyBeginYear_,year);
  }

  public void setSpouseStudyBeginYear(int year) {
    setColumn(spouseStudyBeginYear_,year);
  }

  public void setSpouseStudyEndMonth(Integer month) {
    setColumn(spouseStudyEndMonth_,month);
  }

  public void setSpouseStudyEndMonth(int month) {
    setColumn(spouseStudyEndMonth_,month);
  }

  public void setSpouseStudyEndYear(Integer year) {
    setColumn(spouseStudyEndYear_,year);
  }

  public void setSpouseStudyEndYear(int year) {
    setColumn(spouseStudyEndYear_,year);
  }

  public void setChildren(String children) {
    setColumn(children_,children);
  }

  public void setIncome(Integer income) {
    setColumn(income_,income);
  }

  public void setIncome(int income) {
    setColumn(income_,income);
  }

  public void setSpouseIncome(Integer income) {
    setColumn(spouseIncome_,income);
  }

  public void setSpouseIncome(int income) {
    setColumn(spouseIncome_,income);
  }

  public void setHousingFrom(Date from) {
    setColumn(housingFrom_,from);
  }

  public void setOnWaitinglist(boolean putOnList) {
    if (putOnList)
      setColumn(onWaitinglist_,"Y");
    else
      setColumn(onWaitinglist_,"N");
  }

  public void setWantFurniture(boolean furniture) {
    if (furniture)
      setColumn(wantFurniture_,"Y");
    else
      setColumn(wantFurniture_,"N");
  }

  public void setContactPhone(String contactPhone) {
    setColumn(contactPhone_,contactPhone);
  }

  public void setOtherInfo(String info) {
    setColumn(otherInfo_,info);
  }

  public void setEmail(String email) {
    setColumn(email_,email);
  }

  public String getPriorityLevel() {
    return getStringColumnValue(PRIORITY_LEVEL);
  }

  public void setPriorityLevel(String level) {
    setColumn(PRIORITY_LEVEL,level);
  }

  public static String getPriorityColumnName() {
    return PRIORITY_LEVEL;
  }

  public java.util.Collection ejbFindAllByApplicationId(int id) throws javax.ejb.FinderException {
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(getTableName());
    sql.append(" where ");
    sql.append(applicationId_);
    sql.append(" = ");
    sql.append(id);
    return super.idoFindIDsBySQL(sql.toString());
  }
}