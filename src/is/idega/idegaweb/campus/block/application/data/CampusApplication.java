package is.idega.idegaweb.campus.block.application.data;


import java.util.Collection;
import com.idega.block.application.data.Application;
import java.sql.Date;
import com.idega.data.IDOEntity;

public interface CampusApplication extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getApplicationIdColumnName
	 */
	public String getApplicationIdColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getApplication
	 */
	public Application getApplication();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getCurrentResidenceIdColumnName
	 */
	public String getCurrentResidenceIdColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseOccupationIdColumnName
	 */
	public String getSpouseOccupationIdColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getStudyBeginMonthColumnName
	 */
	public String getStudyBeginMonthColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getStudyBeginYearColumnName
	 */
	public String getStudyBeginYearColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getStudyEndMonthColumnName
	 */
	public String getStudyEndMonthColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getStudyEndYearColumnName
	 */
	public String getStudyEndYearColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getFacultyColumnName
	 */
	public String getFacultyColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getStudyTrackColumnName
	 */
	public String getStudyTrackColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseNameColumnName
	 */
	public String getSpouseNameColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseSSNColumnName
	 */
	public String getSpouseSSNColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseSchoolColumnName
	 */
	public String getSpouseSchoolColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseStudyTrackColumnName
	 */
	public String getSpouseStudyTrackColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseStudyBeginMonthColumnName
	 */
	public String getSpouseStudyBeginMonthColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseStudyBeginYearColumnName
	 */
	public String getSpouseStudyBeginYearColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseStudyEndMonthColumnName
	 */
	public String getSpouseStudyEndMonthColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseStudyEndYearColumnName
	 */
	public String getSpouseStudyEndYearColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getChildrenColumnName
	 */
	public String getChildrenColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getIncomeColumnName
	 */
	public String getIncomeColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseIncomeColumnName
	 */
	public String getSpouseIncomeColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getHousingFromColumnName
	 */
	public String getHousingFromColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getOnWaitinglistColumnName
	 */
	public String getOnWaitinglistColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getWantFurnitureColumnName
	 */
	public String getWantFurnitureColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getContactPhoneColumnName
	 */
	public String getContactPhoneColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getOtherInfoColumnName
	 */
	public String getOtherInfoColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getEmailColumnName
	 */
	public String getEmailColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setAppApplicationId
	 */
	public void setAppApplicationId(int id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setAppApplicationId
	 */
	public void setAppApplicationId(Integer id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getAppApplicationId
	 */
	public Integer getAppApplicationId();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getCurrentResidenceId
	 */
	public Integer getCurrentResidenceId();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseOccupationId
	 */
	public Integer getSpouseOccupationId();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getStudyBeginMonth
	 */
	public Integer getStudyBeginMonth();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getStudyBeginYear
	 */
	public Integer getStudyBeginYear();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getStudyEndMonth
	 */
	public Integer getStudyEndMonth();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getStudyEndYear
	 */
	public Integer getStudyEndYear();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getFaculty
	 */
	public String getFaculty();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getStudyTrack
	 */
	public String getStudyTrack();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseName
	 */
	public String getSpouseName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseSSN
	 */
	public String getSpouseSSN();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseSchool
	 */
	public String getSpouseSchool();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseStudyTrack
	 */
	public String getSpouseStudyTrack();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseStudyBeginMonth
	 */
	public Integer getSpouseStudyBeginMonth();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseStudyBeginYear
	 */
	public Integer getSpouseStudyBeginYear();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseStudyEndMonth
	 */
	public Integer getSpouseStudyEndMonth();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseStudyEndYear
	 */
	public Integer getSpouseStudyEndYear();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getChildren
	 */
	public String getChildren();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getIncome
	 */
	public Integer getIncome();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSpouseIncome
	 */
	public Integer getSpouseIncome();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getHousingFrom
	 */
	public Date getHousingFrom();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getOnWaitinglist
	 */
	public boolean getOnWaitinglist();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getWantFurniture
	 */
	public boolean getWantFurniture();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getContactPhone
	 */
	public String getContactPhone();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getOtherInfo
	 */
	public String getOtherInfo();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getEmail
	 */
	public String getEmail();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSchoolID
	 */
	public int getSchoolID();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getSchool
	 */
	public School getSchool();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setCurrentResidenceId
	 */
	public void setCurrentResidenceId(Integer id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setCurrentResidenceId
	 */
	public void setCurrentResidenceId(int id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseOccupationId
	 */
	public void setSpouseOccupationId(Integer id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseOccupationId
	 */
	public void setSpouseOccupationId(int id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setStudyBeginMonth
	 */
	public void setStudyBeginMonth(Integer month);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setStudyBeginMonth
	 */
	public void setStudyBeginMonth(int month);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setStudyBeginYear
	 */
	public void setStudyBeginYear(Integer year);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setStudyBeginYear
	 */
	public void setStudyBeginYear(int year);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setStudyEndMonth
	 */
	public void setStudyEndMonth(Integer month);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setStudyEndMonth
	 */
	public void setStudyEndMonth(int month);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setStudyEndYear
	 */
	public void setStudyEndYear(Integer year);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setStudyEndYear
	 */
	public void setStudyEndYear(int year);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setFaculty
	 */
	public void setFaculty(String faculty);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setStudyTrack
	 */
	public void setStudyTrack(String studyTrack);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseName
	 */
	public void setSpouseName(String spouseName);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseSSN
	 */
	public void setSpouseSSN(String ssn);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseSchool
	 */
	public void setSpouseSchool(String school);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseStudyTrack
	 */
	public void setSpouseStudyTrack(String studyTrack);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseStudyBeginMonth
	 */
	public void setSpouseStudyBeginMonth(Integer month);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseStudyBeginMonth
	 */
	public void setSpouseStudyBeginMonth(int month);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseStudyBeginYear
	 */
	public void setSpouseStudyBeginYear(Integer year);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseStudyBeginYear
	 */
	public void setSpouseStudyBeginYear(int year);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseStudyEndMonth
	 */
	public void setSpouseStudyEndMonth(Integer month);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseStudyEndMonth
	 */
	public void setSpouseStudyEndMonth(int month);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseStudyEndYear
	 */
	public void setSpouseStudyEndYear(Integer year);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseStudyEndYear
	 */
	public void setSpouseStudyEndYear(int year);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setChildren
	 */
	public void setChildren(String children);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setIncome
	 */
	public void setIncome(Integer income);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setIncome
	 */
	public void setIncome(int income);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseIncome
	 */
	public void setSpouseIncome(Integer income);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSpouseIncome
	 */
	public void setSpouseIncome(int income);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setHousingFrom
	 */
	public void setHousingFrom(Date from);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setOnWaitinglist
	 */
	public void setOnWaitinglist(boolean putOnList);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setWantFurniture
	 */
	public void setWantFurniture(boolean furniture);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setContactPhone
	 */
	public void setContactPhone(String contactPhone);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setOtherInfo
	 */
	public void setOtherInfo(String info);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setEmail
	 */
	public void setEmail(String email);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getPriorityLevel
	 */
	public String getPriorityLevel();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setPriorityLevel
	 */
	public void setPriorityLevel(String level);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSchoolID
	 */
	public void setSchoolID(int schoolID);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#setSchool
	 */
	public void setSchool(School school);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#getApplied
	 */
	public Collection getApplied();
}