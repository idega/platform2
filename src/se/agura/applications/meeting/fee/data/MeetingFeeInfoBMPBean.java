/*
 * $Id: MeetingFeeInfoBMPBean.java,v 1.3 2004/12/29 15:47:09 laddi Exp $ Created on
 * 23.11.2004
 * 
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.agura.applications.meeting.fee.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.User;

/**
 * Last modified: 23.11.2004 10:59:07 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna </a>
 * @version $Revision: 1.3 $
 */
public class MeetingFeeInfoBMPBean extends GenericEntity  implements MeetingFeeInfo{

	public static final String ENTITY_NAME = "me_meeting_fee_info";

	public static final String COLUMN_MEETING_FEE_INFO_ID = "meeting_fee_info_id";

	public static final String COLUMN_MEETING_FEE_ID = "meeting_fee_id";

	public static final String COLUMN_MEETING_FEE_FORMULA_ID = "meeting_fee_formula_id";

	public static final String COLUMN_IC_USER_ID = "ic_user_id";

	public static final String COLUMN_MEETING_DURATION = "meeting_duration";

	public static final String COLUMN_AMOUNT = "amount";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(COLUMN_MEETING_FEE_INFO_ID);
		setAsPrimaryKey(COLUMN_MEETING_FEE_INFO_ID, true);
		
		addManyToOneRelationship(COLUMN_IC_USER_ID, User.class);
		addAttribute(COLUMN_MEETING_DURATION, "Meeting duration", Integer.class);
		addAttribute(COLUMN_AMOUNT, "Amount", Integer.class);

		addOneToOneRelationship(COLUMN_MEETING_FEE_FORMULA_ID, MeetingFeeFormula.class);
		addOneToOneRelationship(COLUMN_MEETING_FEE_ID, MeetingFee.class);
	}

	// getters
	public MeetingFee getMeetingFee() {
		return (MeetingFee) getColumnValue(COLUMN_MEETING_FEE_ID);
	}
	
	public MeetingFeeFormula getMeetingFeeFormula() {
		return (MeetingFeeFormula) getColumnValue(COLUMN_MEETING_FEE_FORMULA_ID);
	}
	
	public User getUser() {
		return (User) getColumnValue(COLUMN_IC_USER_ID);
	}

	public int getMeetingDuration() {
		return getIntColumnValue(COLUMN_MEETING_DURATION);
	}

	public int getAmount() {
		return getIntColumnValue(COLUMN_AMOUNT);
	}

	// setters
	public void setMeetingFee(MeetingFee meetingFee) {
		setColumn(COLUMN_MEETING_FEE_ID, meetingFee);
	}
	
	public void setMeetingFeeFormula(MeetingFeeFormula meetingFeeFormula) {
		setColumn(COLUMN_MEETING_FEE_FORMULA_ID, meetingFeeFormula);
	}
	
	public void setUser(User user) {
		setColumn(COLUMN_IC_USER_ID, user);
	}

	public void setUserID(int userID) {
		setColumn(COLUMN_IC_USER_ID, userID);
	}

	public void setMeetingDuration(int meetingDuration) {
		setColumn(COLUMN_MEETING_DURATION, meetingDuration);
	}

	public void setAmount(int amount) {
		setColumn(COLUMN_AMOUNT, amount);
	}
	
	
	//Finders
	public Collection ejbFindByMeetingFee(MeetingFee meetingFee) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, COLUMN_MEETING_FEE_ID, MatchCriteria.EQUALS, meetingFee));
		return idoFindPKsByQuery(query);
	}
	
	public Integer ejbFindByUserAndMeetingFee(User user, MeetingFee meetingFee) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, COLUMN_MEETING_FEE_ID, MatchCriteria.EQUALS, meetingFee));
		query.addCriteria(new MatchCriteria(table, COLUMN_IC_USER_ID, MatchCriteria.EQUALS, user));
		return (Integer) idoFindOnePKByQuery(query);
	}
}