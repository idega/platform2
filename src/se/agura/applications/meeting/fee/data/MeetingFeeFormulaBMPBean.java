/*
 * $Id: MeetingFeeFormulaBMPBean.java,v 1.1 2004/12/05 20:59:37 anna Exp $ Created on
 * 23.11.2004
 * 
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.agura.applications.meeting.fee.data;

import java.util.Date;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.User;

/**
 * Last modified: 23.11.2004 11:45:32 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna </a>
 * @version $Revision: 1.1 $
 */
public class MeetingFeeFormulaBMPBean extends GenericEntity  implements MeetingFeeFormula{

	public static final String ENTITY_NAME = "me_meeting_fee_formula";

	public static final String COLUMN_MEETING_FEE_FORMULA_ID = "meeting_fee_formula_id";

	public static final String COLUMN_CREATION_DATE = "creation_date";

	public static final String COLUMN_FIRST_HOUR_AMOUNT = "first_hour_amount";

	public static final String COLUMN_PROCEEDING_TIME_AMOUNT = "proceeding_time_amount";

	public static final String COLUMN_CREATED_BY = "created_by";

	public static final String COLUMN_PROCEEDING_TIME_INTERVAL = "proceeding_time_interval";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(COLUMN_MEETING_FEE_FORMULA_ID);
		setAsPrimaryKey(COLUMN_MEETING_FEE_FORMULA_ID, true);
		addAttribute(COLUMN_CREATION_DATE, "Creation date", Date.class);
		addAttribute(COLUMN_FIRST_HOUR_AMOUNT, "First hour amount", Integer.class);
		addAttribute(COLUMN_PROCEEDING_TIME_AMOUNT, "Proceeding time amount", Integer.class);
		addManyToOneRelationship(COLUMN_CREATED_BY, User.class);
		addAttribute(COLUMN_PROCEEDING_TIME_INTERVAL, "Proceeding time interval", Integer.class);
	}

	public int getMeetingFeeFormulaId() {
		return getIntColumnValue(COLUMN_MEETING_FEE_FORMULA_ID);
	}
	
	public Date getCreationDate() {
		return getDateColumnValue(COLUMN_CREATION_DATE);
	}

	public int getFirstHourAmount() {
		return getIntColumnValue(COLUMN_FIRST_HOUR_AMOUNT);
	}

	public int getProceedingTimeAmount() {
		return getIntColumnValue(COLUMN_PROCEEDING_TIME_AMOUNT);
	}

	public User getCreatedBy() {
		return (User) getColumnValue(COLUMN_CREATED_BY);
	}

	public int getProceedingTimeInterval() {
		return getIntColumnValue(COLUMN_PROCEEDING_TIME_INTERVAL);
	}
	
	public void setMeetingFeeFormulaId(int meetingFeeFormulaId) {
		setColumn(COLUMN_MEETING_FEE_FORMULA_ID, meetingFeeFormulaId);
	}

	public void setCreationDate(Date creationDate) {
		setColumn(COLUMN_CREATION_DATE, creationDate);
	}

	public void setFirstHourAmount(int firstHourAmount) {
		setColumn(COLUMN_FIRST_HOUR_AMOUNT, firstHourAmount);
	}

	public void setProceedingTimeAmount(int proceedingTimeAmount) {
		setColumn(COLUMN_PROCEEDING_TIME_AMOUNT, proceedingTimeAmount);
	}

	public void setCreatedBy(User createdBy) {
		setColumn(COLUMN_CREATED_BY, createdBy);
	}

	public void setProceedingTimeInterval(int proceedingTimeInterval) {
		setColumn(COLUMN_PROCEEDING_TIME_INTERVAL, proceedingTimeInterval);
	}
	
	public Integer ejbFindLatestFormula() throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addOrder(table, COLUMN_CREATION_DATE, false);
		
		return (Integer) idoFindOnePKByQuery(query);
	}
}