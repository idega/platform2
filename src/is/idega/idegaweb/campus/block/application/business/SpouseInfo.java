/*
 * Created on Jul 21, 2003
 *
 */
package is.idega.idegaweb.campus.block.application.business;

/**
 * SpouseInfo
 * @author aron 
 * @version 1.0
 */

public class SpouseInfo {
	Integer ID;
	String name;
	String ssn;
	String school;
	String track;
	Double income;
	Integer studyBeginMonth;
	Integer studyBeginYear;
	Integer studyEndMonth;
	Integer studyEndYear;

	/**
	 * 
	 */
	public SpouseInfo(
		Integer ID,
		String name,
		String ssn,
		String school,
		String track,
		Double income,
		Integer studyBeginMonth,
		Integer studyBeginYear,
		Integer studyEndMonth,
		Integer studyEndYear) {
		this.name = name;
		this.ssn = ssn;
		this.school = school;
		this.track = track;
		this.income = income;
		this.studyBeginMonth = studyBeginMonth;
		this.studyBeginYear = studyBeginYear;
		this.studyEndMonth = studyEndMonth;
		this.studyEndYear =studyEndYear;

	}

	/**
	 * @return
	 */
	public Integer getID() {
		return ID;
	}

	/**
	 * @return
	 */
	public Double getIncome() {
		return income;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getSchool() {
		return school;
	}

	/**
	 * @return
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * @return
	 */
	public Integer getStudyBeginMonth() {
		return studyBeginMonth;
	}

	/**
	 * @return
	 */
	public Integer getStudyBeginYear() {
		return studyBeginYear;
	}

	/**
	 * @return
	 */
	public Integer getStudyEndMonth() {
		return studyEndMonth;
	}

	/**
	 * @return
	 */
	public Integer getStudyEndYear() {
		return studyEndYear;
	}

	/**
	 * @return
	 */
	public String getTrack() {
		return track;
	}

	/**
	 * @param integer
	 */
	public void setID(Integer integer) {
		ID = integer;
	}

	/**
	 * @param double1
	 */
	public void setIncome(Double double1) {
		income = double1;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setSchool(String string) {
		school = string;
	}

	/**
	 * @param string
	 */
	public void setSsn(String string) {
		ssn = string;
	}

	/**
	 * @param integer
	 */
	public void setStudyBeginMonth(Integer integer) {
		studyBeginMonth = integer;
	}

	/**
	 * @param integer
	 */
	public void setStudyBeginYear(Integer integer) {
		studyBeginYear = integer;
	}

	/**
	 * @param integer
	 */
	public void setStudyEndMonth(Integer integer) {
		studyEndMonth = integer;
	}

	/**
	 * @param integer
	 */
	public void setStudyEndYear(Integer integer) {
		studyEndYear = integer;
	}

	/**
	 * @param string
	 */
	public void setTrack(String string) {
		track = string;
	}

}
