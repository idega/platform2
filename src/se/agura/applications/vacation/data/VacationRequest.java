/*
 * $Id: VacationRequest.java,v 1.2 2004/12/09 13:43:37 laddi Exp $
 * Created on 7.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.vacation.data;

import java.sql.Date;

import com.idega.block.process.data.Case;
import com.idega.data.MetaDataCapable;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2004/12/09 13:43:37 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface VacationRequest extends Case, MetaDataCapable {

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getCaseCodeKey
	 */
	public String getCaseCodeKey();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getCaseCodeDescription
	 */
	public String getCaseCodeDescription();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getFromDate
	 */
	public Date getFromDate();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getToDate
	 */
	public Date getToDate();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getOrdinaryWorkingHours
	 */
	public int getOrdinaryWorkingHours();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getVacationType
	 */
	public VacationType getVacationType();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getCreatedDate
	 */
	public Date getCreatedDate();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getGrantedDate
	 */
	public Date getGrantedDate();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getRejectedDate
	 */
	public Date getRejectedDate();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getDecisionBy
	 */
	public User getDecisionBy();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getComment
	 */
	public String getComment();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getSalaryCompensation
	 */
	public boolean getSalaryCompensation();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#setFromDate
	 */
	public void setFromDate(Date fromDate);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#setToDate
	 */
	public void setToDate(Date toDate);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#setOrdinaryWorkingHours
	 */
	public void setOrdinaryWorkingHours(Integer ordinaryWorkingHour);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#setVacationType
	 */
	public void setVacationType(VacationType vacationType);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#setCreatedDate
	 */
	public void setCreatedDate(Date createdDate);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#setUser
	 */
	public void setUser(User icUserId);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#setGrantedDate
	 */
	public void setGrantedDate(Date grantedDate);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#setRejectedDate
	 */
	public void setRejectedDate(Date rejectedDate);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#setDecisionBy
	 */
	public void setDecisionBy(User decisionBy);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#setComment
	 */
	public void setComment(String comment);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#setSalaryCompensation
	 */
	public void setSalaryCompensation(boolean getsCompensation);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#setExtraTypeInformation
	 */
	public void setExtraTypeInformation(String key, String value, String type);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getExtraTypeInformation
	 */
	public String getExtraTypeInformation(String key);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getExtraTypeInformationType
	 */
	public String getExtraTypeInformationType(String key);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#removeExtraTypeInformation
	 */
	public void removeExtraTypeInformation(String key);

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#removeAllExtraTypeInformation
	 */
	public void removeAllExtraTypeInformation();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getCaseStatusDescriptions
	 */
	public String[] getCaseStatusDescriptions();

	/**
	 * @see se.agura.applications.vacation.data.VacationRequestBMPBean#getCaseStatusKeys
	 */
	public String[] getCaseStatusKeys();

}
