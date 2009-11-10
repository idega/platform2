package com.idega.block.application.data;


import com.idega.data.IDOEntity;
import com.idega.data.TreeableEntity;

public interface Applicant extends IDOEntity, TreeableEntity {
	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setFirstName
	 */
	public void setFirstName(String name);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getFirstName
	 */
	public String getFirstName();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setMiddleName
	 */
	public void setMiddleName(String name);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getMiddleName
	 */
	public String getMiddleName();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setLastName
	 */
	public void setLastName(String name);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getLastName
	 */
	public String getLastName();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setSSN
	 */
	public void setSSN(String ssn);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getSSN
	 */
	public String getSSN();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setLegalResidence
	 */
	public void setLegalResidence(String legal);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getLegalResidence
	 */
	public String getLegalResidence();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setResidence
	 */
	public void setResidence(String residence);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getResidence
	 */
	public String getResidence();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setResidencePhone
	 */
	public void setResidencePhone(String phone);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getResidencePhone
	 */
	public String getResidencePhone();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setPO
	 */
	public void setPO(String po);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getPO
	 */
	public String getPO();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setMobilePhone
	 */
	public void setMobilePhone(String mobilePhone);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setResignationAddress
	 */
	public void setResignationAddress(String resignationAddress);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setResignationPO
	 */
	public void setResignationPO(String resignationPO);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setResignationPhone
	 */
	public void setResignationPhone(String resignationPhone);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setResignationEmail
	 */
	public void setResignationEmail(String resignationEmail);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getMobilePhone
	 */
	public String getMobilePhone();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getSendSMS
	 */
	public boolean getSendSMS();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setSendSMS
	 */
	public void setSendSMS(boolean send);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setStatus
	 */
	public void setStatus(String status);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getStatus
	 */
	public String getStatus();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getFullName
	 */
	public String getFullName();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#setFullName
	 */
	public void setFullName(String fullName);

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getResignationAddress
	 */
	public String getResignationAddress();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getResignationPO
	 */
	public String getResignationPO();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getResignationPhone
	 */
	public String getResignationPhone();

	/**
	 * @see com.idega.block.application.data.ApplicantBMPBean#getResignationEmail
	 */
	public String getResignationEmail();
}