/*
 * $Id: MeetingFeeBMPBean.java,v 1.1 2004/12/05 20:59:37 anna Exp $
 * Created on 23.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.data;

import java.sql.Date;
import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * Last modified: 23.11.2004 09:44:45 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public class MeetingFeeBMPBean extends AbstractCaseBMPBean  implements MeetingFee{
	
	public static final String ENTITY_NAME = "me_meeting_fee";
	
	public static final String CASE_CODE = "MEETING";
	
	public static final String COLUMN_MEETING_FEE_ID = "meeting_fee_id";

  public static final String COLUMN_IN_COMMUNE = "in_commune";
  
  public static final String COLUMN_PARTICIPANT_GROUP_ID = "participant_group_id";
  
  public static final String COLUMN_CONGREGATION_GROUP_ID = "gongregation_group_id";
  
  public static final String COLUMN_MEETING_DATE = "meeting_date";
    
  public static final String COLUMN_SIGNED_DATE = "signed_date";
  
  public static final String COLUMN_SIGNED_BY = "signed_by";
  
	/* (non-Javadoc)
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeDescription()
	 */
	public String getCaseCodeDescription() {
		return "Meetings";
	}
	
	/* (non-Javadoc)
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeKey()
	 */
	public String getCaseCodeKey() {
		return CASE_CODE;
	}
	
	public String getEntityName() {
    return ENTITY_NAME;
	}
	
	public void initializeAttributes() {
		 addAttribute(COLUMN_MEETING_FEE_ID);
     setAsPrimaryKey(COLUMN_MEETING_FEE_ID, true);
     
     addAttribute(COLUMN_MEETING_DATE, "Meeting date", Date.class);
     addAttribute(COLUMN_SIGNED_DATE, "Signed date", Date.class);
     addAttribute(COLUMN_IN_COMMUNE, "In/out of commune", Boolean.class);
     
     addManyToOneRelationship(COLUMN_CONGREGATION_GROUP_ID, Group.class);
     addManyToOneRelationship(COLUMN_PARTICIPANT_GROUP_ID, Group.class);
     addManyToOneRelationship(COLUMN_SIGNED_BY, User.class);
	}
	
  ///////////////////////////////////////////////////
  //  getters
  ///////////////////////////////////////////////////
	
	public boolean getInCommune() {
		return getBooleanColumnValue(COLUMN_IN_COMMUNE);
	}
	
	public Group getParticipantGroup() {
		return (Group) getColumnValue(COLUMN_PARTICIPANT_GROUP_ID);
	}
	
	public int getParticipantGroupID() {
		return getIntColumnValue(COLUMN_PARTICIPANT_GROUP_ID);
	}
	
	public Group getCongregationGroup() {
		return (Group) getColumnValue(COLUMN_CONGREGATION_GROUP_ID);
	}
	 public Date getMeetingDate() {
    return getDateColumnValue(COLUMN_MEETING_DATE);
	 }
	 
	 public Date getSignedDate() {
	 	return getDateColumnValue(COLUMN_SIGNED_DATE);
	 }
	 
	 public User getSignedBy() {
	 	return (User) getColumnValue(COLUMN_SIGNED_BY);
	 }
	 
	  ///////////////////////////////////////////////////
	  //  setters
	  ///////////////////////////////////////////////////
	 
	 public void setInCommune(boolean inCommune) {
	 	setColumn(COLUMN_IN_COMMUNE, inCommune);
	 }
	 
	 public void setParticipantGroup(Group participantGroup) {
	 	setColumn(COLUMN_PARTICIPANT_GROUP_ID, participantGroup);
	 }
	 
	 public void setCongregation(Group congregationGroup) {
	 	setColumn(COLUMN_CONGREGATION_GROUP_ID, congregationGroup);
	 }
	 
	  public void setMeetingDate(Date meetingDate) {
	  		setColumn(COLUMN_MEETING_DATE, meetingDate);
	  }
	  
	  public void setSignedDate(Date signedDate) {
	     setColumn(COLUMN_SIGNED_DATE, signedDate);
		}
	  
	  public void setSignedBy(User signedBy) {
	  		 setColumn(COLUMN_SIGNED_BY, signedBy);
	  }
}