/**
 * 
 */
package com.idega.block.application.data;

import java.sql.Timestamp;


import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface Application extends IDOEntity {
	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#setSubjectId
	 */
	public void setSubjectId(int id);

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#setSubjectId
	 */
	public void setSubjectId(Integer id);

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#getSubjectId
	 */
	public int getSubjectId();

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#getSubject
	 */
	public ApplicationSubject getSubject();

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#setApplicantId
	 */
	public void setApplicantId(int id);

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#setApplicantId
	 */
	public void setApplicantId(Integer id);

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#getApplicantId
	 */
	public int getApplicantId();

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#getApplicant
	 */
	public Applicant getApplicant();

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#setSubmitted
	 */
	public void setSubmitted(Timestamp submitted);

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#getSubmitted
	 */
	public Timestamp getSubmitted();

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#setStatus
	 */
	public void setStatus(String status) throws IllegalStateException;

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#setStatusSubmitted
	 */
	public void setStatusSubmitted();

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#setStatusApproved
	 */
	public void setStatusApproved();

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#setStatusRejected
	 */
	public void setStatusRejected();

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#setStatusSigned
	 */
	public void setStatusSigned();

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#getStatus
	 */
	public String getStatus();

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#setStatusChanged
	 */
	public void setStatusChanged(Timestamp statusChanged);

	/**
	 * @see com.idega.block.application.data.ApplicationBMPBean#getStatusChanged
	 */
	public Timestamp getStatusChanged();

}
