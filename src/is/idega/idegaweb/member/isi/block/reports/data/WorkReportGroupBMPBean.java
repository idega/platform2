/*
 * Created on May 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.data.GenericEntity;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * Description: The list of board members in a club/division for a particular year<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportGroupBMPBean extends GenericEntity implements WorkReportGroup{
	protected final static String ENTITY_NAME = "ISI_WR_GROUP";
	protected final static String COLUMN_NAME_REPORT_ID = "ISI_WORK_REPORT_ID";
	protected final static String COLUMN_NAME_USER_ID = "IC_USER_ID";
	protected final static String COLUMN_NAME_PERSONAL_ID = "PERSONAL_ID";
	protected final static String COLUMN_NAME_NAME = "NAME";
	protected final static String COLUMN_NAME_AGE = "AGE_FOR_YEAR";
	protected final static String COLUMN_NAME_DATE_OF_BIRTH = "DATE_OF_BIRTH";
	protected final static String COLUMN_NAME_GENDER = "GENDER";
	protected final static String COLUMN_NAME_STATUS = "STATUS";//precident,vice president etc.
	protected final static String COLUMN_NAME_WORK_REPORT_GROUP = "WR_GROUP_ID";//precident,vice president etc.
	
	
	public WorkReportGroupBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_REPORT_ID, "Id of the work report",true,true,Integer.class,"many-to-one",WorkReport.class);
		addAttribute(COLUMN_NAME_USER_ID, "User id",true,true,Integer.class,"one-to-one",User.class);
		addAttribute(COLUMN_NAME_PERSONAL_ID,"Personal id",true,true,String.class,10);
		addAttribute(COLUMN_NAME_NAME,"Name from file",true,true,String.class,180);	
		addAttribute(COLUMN_NAME_DATE_OF_BIRTH,"Date of birth",true,true,Timestamp.class);
		addAttribute(COLUMN_NAME_AGE, "The yearly age of the member",true,true,Integer.class);
		addAttribute(COLUMN_NAME_GENDER,"Gender m/f",true,true,String.class,1);
		addAttribute(COLUMN_NAME_STATUS,"Precident,vice president etc.",true,true,String.class,30);
		addAttribute(COLUMN_NAME_WORK_REPORT_GROUP, "The league/division connection, null then use club",true,true,Integer.class,"many-to-one",WorkReport.class);
	}
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
}
