/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import java.sql.Timestamp;

import com.idega.data.GenericEntity;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author palli
 */
public class AssessmentRoundBMPBean extends GenericEntity implements AssessmentRound {
	protected final static String ENTITY_NAME = "isi_ass_round"; //Hehe, just had to keep that one in here :)
	
	protected final static String COLUMN_NAME = "name";
	protected final static String COLUMN_EXECUTION_DATE = "executed_date";
	protected final static String COLUMN_EXECUTED_BY = "user_id";
	protected final static String COLUMN_CLUB = "club_id";
	protected final static String COLUMN_GROUP = "group_id";
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME,"Assessment round name",true,true,String.class);
		addAttribute(COLUMN_EXECUTION_DATE,"The date of the assessment",true,true,Timestamp.class);
		addManyToOneRelationship(COLUMN_EXECUTED_BY,User.class);
		addManyToOneRelationship(COLUMN_CLUB,Group.class);
		addManyToOneRelationship(COLUMN_GROUP,Group.class);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME,name);
	}
	
	public void setExecutionDate(Timestamp date) {
		setColumn(COLUMN_EXECUTION_DATE,date);
	}
	
	public void setExecutedById(int id) {
		setColumn(COLUMN_EXECUTED_BY,id);
	}
	
	public void setExecutedBy(User user) {
		setColumn(COLUMN_EXECUTED_BY,user);
	}
	
	public void setClubId(int id) {
		setColumn(COLUMN_CLUB,id);
	}
	
	public void setClub(Group club) {
		setColumn(COLUMN_CLUB,club);
	}
	
	public void setGroupId(int id) {
		setColumn(COLUMN_GROUP,id);
	}
	
	public void setGroup(Group group) {
		setColumn(COLUMN_GROUP,group);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public Timestamp getExecutionDate() {
		return getTimestampColumnValue(COLUMN_EXECUTION_DATE);
	}
	
	public int getExecutedById() {
		return getIntColumnValue(COLUMN_EXECUTED_BY);
	}
	
	public User getExecutedBy() {
		return (User) getColumnValue(COLUMN_EXECUTED_BY);
	}
	
	public int getClubId() {
		return getIntColumnValue(COLUMN_CLUB);
	}
	
	public Group getClub() {
		return (Group) getColumnValue(COLUMN_CLUB);
	}
	
	public int getGroupId() {
		return getIntColumnValue(COLUMN_GROUP);
	}
	
	public Group setGroup() {
		return (Group) getColumnValue(COLUMN_GROUP);
	}
}