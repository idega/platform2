/*
 * Created on Jul 21, 2003
 *
 */
package is.idega.idegaweb.campus.block.application.business;

/**
 * ApplicantInfo
 * @author aron 
 * @version 1.0
 */

public class ApplicantInfo {
			String name ;
			String ssn ;
			String legalResidence ;
			String currentResidence ;
			String PO ;
			String phone ;
			String mobile ;
			String email ;
			String faculty ;
			String track ;
			Double income ;
			Integer studyBeginMonth ;
			Integer studyBeginYear;
			Integer studyEndMonth;
			Integer studyEndYear;
	public ApplicantInfo(String name,String ssn,
	String legalResidence,String currentResidence,
	String PO,String phone,String mobile,String email,
	String faculty,String track,Double income,Integer beginMonth,Integer beginYear,Integer endMonth,Integer endYear){
		this.name = name;
		this.ssn = ssn;
		this.legalResidence = legalResidence;
		this.currentResidence = currentResidence;
		this.PO = PO;
		this.phone = phone;
		this.mobile = mobile;
		this.email = email;
		this.faculty = faculty;
		this.track = track;
		this.income = income;
		this.studyBeginMonth = beginMonth;
		this.studyBeginYear = beginYear;
		this.studyEndMonth = endMonth;
		this.studyEndYear = endYear;
	}
	
			/**
			 * @return
			 */
			public String getCurrentResidence() {
				return currentResidence;
			}

			/**
			 * @return
			 */
			public String getEmail() {
				return email;
			}

			/**
			 * @return
			 */
			public String getFaculty() {
				return faculty;
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
			public String getLegalResidence() {
				return legalResidence;
			}

			/**
			 * @return
			 */
			public String getMobile() {
				return mobile;
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
			public String getPhone() {
				return phone;
			}

			/**
			 * @return
			 */
			public String getPO() {
				return PO;
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
			 * @param string
			 */
			public void setCurrentResidence(String string) {
				currentResidence = string;
			}

			/**
			 * @param string
			 */
			public void setEmail(String string) {
				email = string;
			}

			/**
			 * @param string
			 */
			public void setFaculty(String string) {
				faculty = string;
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
			public void setLegalResidence(String string) {
				legalResidence = string;
			}

			/**
			 * @param string
			 */
			public void setMobile(String string) {
				mobile = string;
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
			public void setPhone(String string) {
				phone = string;
			}

			/**
			 * @param string
			 */
			public void setPO(String string) {
				PO = string;
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
