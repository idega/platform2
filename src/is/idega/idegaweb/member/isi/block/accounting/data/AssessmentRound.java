/*
 * $Id: AssessmentRound.java,v 1.6 2005/10/28 11:02:20 palli Exp $
 * Created on Oct 4, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import java.sql.Date;
import java.sql.Timestamp;


import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2005/10/28 11:02:20 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.6 $
 */
public interface AssessmentRound extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#addTariffType
	 */
	public void addTariffType(ClubTariffType tariffType) throws IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setExecutionDate
	 */
	public void setExecutionDate(Timestamp date);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setExecutedById
	 */
	public void setExecutedById(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setExecutedBy
	 */
	public void setExecutedBy(User user);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setClubId
	 */
	public void setClubId(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setClub
	 */
	public void setClub(Group club);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setDivisionId
	 */
	public void setDivisionId(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setDivision
	 */
	public void setDivision(Group club);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setGroupId
	 */
	public void setGroupId(int id);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setGroup
	 */
	public void setGroup(Group group);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setStartTime
	 */
	public void setStartTime(Timestamp time);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setEndTime
	 */
	public void setEndTime(Timestamp time);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setIncludeChildren
	 */
	public void setIncludeChildren(boolean include);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setDeleted
	 */
	public void setDeleted(boolean deleted);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setPaymentDate
	 */
	public void setPaymentDate(Timestamp time);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setRunOnDate
	 */
	public void setRunOnDate(Timestamp time);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setAmount
	 */
	public void setAmount(double amount);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setPeriodFrom
	 */
	public void setPeriodFrom(Date from);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#setPeriodTo
	 */
	public void setPeriodTo(Date to);

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getExecutionDate
	 */
	public Timestamp getExecutionDate();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getExecutedById
	 */
	public int getExecutedById();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getExecutedBy
	 */
	public User getExecutedBy();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getClubId
	 */
	public int getClubId();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getClub
	 */
	public Group getClub();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getDivisionId
	 */
	public int getDivisionId();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getDivision
	 */
	public Group getDivision();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getGroupId
	 */
	public int getGroupId();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getGroup
	 */
	public Group getGroup();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getStartTime
	 */
	public Timestamp getStartTime();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getEndTime
	 */
	public Timestamp getEndTime();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getIncludeChildren
	 */
	public boolean getIncludeChildren();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getDeleted
	 */
	public boolean getDeleted();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getPaymentDate
	 */
	public Timestamp getPaymentDate();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getRunOnDate
	 */
	public Timestamp getRunOnDate();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getAmount
	 */
	public double getAmount();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getPeriodFrom
	 */
	public Date getPeriodFrom();

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundBMPBean#getPeriodTo
	 */
	public Date getPeriodTo();

}
