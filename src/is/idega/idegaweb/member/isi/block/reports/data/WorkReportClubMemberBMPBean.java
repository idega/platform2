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
 * Description: The list of people in a club for a particular year<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportClubMemberBMPBean extends GenericEntity implements WorkReportClubMember{
	protected final static String ENTITY_NAME = "ISI_PEOPLE_IN_CLUB";
	protected final static String COLUMN_NAME_REPORT_ID = "ISI_WORK_REPORT_ID";
	protected final static String COLUMN_NAME_USER_ID = "IC_USER_ID";
	protected final static String COLUMN_NAME_PERSONAL_ID = "PERSONAL_ID";
	protected final static String COLUMN_NAME_NAME = "NAME";
	protected final static String COLUMN_NAME_AGE = "AGE_FOR_YEAR";
	protected final static String COLUMN_NAME_DATE_OF_BIRTH = "DATE_OF_BIRTH";
	protected final static String COLUMN_NAME_GENDER = "GENDER";
	
	public WorkReportClubMemberBMPBean() {
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
	}
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
}
