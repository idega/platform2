/*
 * Created on May 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.data.GenericEntity;
import com.idega.user.data.Group;

/**
 * Description: The head data bean for the work reports. One Bean of this type represents a clubs yearly work report.<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportBMPBean extends GenericEntity implements WorkReport{
	protected final static String ENTITY_NAME = "ISI_WORK_REPORT";
	protected final static String COLUMN_NAME_CLUB_ID = "CLUB_ID";
	protected final static String COLUMN_NAME_CLUB_NAME = "CLUB_NAME";
	protected final static String COLUMN_NAME_CLUB_NUMBER = "CLUB_NUMBER";
	protected final static String COLUMN_NAME_WORK_REPORT_YEAR = "YEAR";
	protected final static String COLUMN_NAME_MEMBERS_DONE = "MEMB_DONE";
	protected final static String COLUMN_NAME_ACCOUNT_DONE = "ACC_DONE";
	protected final static String COLUMN_NAME_BOARD_DONE = "BOARD_DONE";
	protected final static String COLUMN_NAME_STATUS = "STATUS";
	

	
	public WorkReportBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_CLUB_ID, "Group id of club",true,true,Integer.class,"many-to-one",Group.class);
		addAttribute(COLUMN_NAME_CLUB_NAME, "Club name",true,true,String.class);
		addAttribute(COLUMN_NAME_CLUB_NUMBER, "Club number",true,true,String.class);
		addAttribute(COLUMN_NAME_WORK_REPORT_YEAR,"The year this report is valid for",true,true,Integer.class);
		addAttribute(COLUMN_NAME_MEMBERS_DONE, "Is the members-part of the work report finished", true, true, Boolean.class);
		addAttribute(COLUMN_NAME_ACCOUNT_DONE, "Is the account-part of the work report finished", true, true, Boolean.class);		
		addAttribute(COLUMN_NAME_BOARD_DONE, "Is the board-part of the work report finished", true, true, Boolean.class);		
		addManyToManyRelationShip(WorkReportGroup.class);//so we can get the clubs related to leagues/divisions
		
		//TODO add stats
	}
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
}
