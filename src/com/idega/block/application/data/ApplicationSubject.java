package com.idega.block.application.data;


import java.sql.Date;
import com.idega.data.IDOEntity;

public interface ApplicationSubject extends IDOEntity {
	/**
	 * @see com.idega.block.application.data.ApplicationSubjectBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.application.data.ApplicationSubjectBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see com.idega.block.application.data.ApplicationSubjectBMPBean#getCreated
	 */
	public Date getCreated();

	/**
	 * @see com.idega.block.application.data.ApplicationSubjectBMPBean#getExpires
	 */
	public Date getExpires();

	/**
	 * @see com.idega.block.application.data.ApplicationSubjectBMPBean#getStatus
	 */
	public String getStatus();

	/**
	 * @see com.idega.block.application.data.ApplicationSubjectBMPBean#getExtraAttribute
	 */
	public String getExtraAttribute();

	/**
	 * @see com.idega.block.application.data.ApplicationSubjectBMPBean#getStarts
	 */
	public Date getStarts();

	/**
	 * @see com.idega.block.application.data.ApplicationSubjectBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see com.idega.block.application.data.ApplicationSubjectBMPBean#setCreated
	 */
	public void setCreated(Date date);

	/**
	 * @see com.idega.block.application.data.ApplicationSubjectBMPBean#setExpires
	 */
	public void setExpires(Date date);

	/**
	 * @see com.idega.block.application.data.ApplicationSubjectBMPBean#setStatus
	 */
	public void setStatus(String status);

	/**
	 * @see com.idega.block.application.data.ApplicationSubjectBMPBean#setExtraAttribute
	 */
	public void setExtraAttribute(String attribute);

	/**
	 * @see com.idega.block.application.data.ApplicationSubjectBMPBean#setStarts
	 */
	public void setStarts(Date starts);
}