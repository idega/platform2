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
 * Description: Financial records for a club for a specified work report<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportClubAccountRecordBMPBean extends GenericEntity implements WorkReportClubAccountRecord{
	protected final static String ENTITY_NAME = "ISI_WR_CLUB_ACC_REC";
	protected final static String COLUMN_NAME_REPORT_ID = "ISI_WORK_REPORT_ID";
	protected final static String COLUMN_NAME_WORK_REPORT_LEAGUE = "LEAGUE_ID";
	protected final static String COLUMN_NAME_AMOUNT = "AMOUNT";
	protected final static String COLUMN_NAME_ACCOUNT_KEY = "ACC_KEY";
	
	public WorkReportClubAccountRecordBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_REPORT_ID, "Id of the work report",true,true,Integer.class,"many-to-one",WorkReport.class);
		addAttribute(COLUMN_NAME_WORK_REPORT_LEAGUE,"The league id",true,true,String.class);
		addAttribute(COLUMN_NAME_AMOUNT,"Amount",true,true,Float.class);	
		addAttribute(COLUMN_NAME_ACCOUNT_KEY, "Account key",true,true,Integer.class,"many-to-one",WorkReportAccountKey.class);
	}
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
}
