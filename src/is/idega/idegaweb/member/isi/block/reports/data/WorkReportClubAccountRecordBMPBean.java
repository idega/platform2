/*
 * Created on May 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

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
	protected final static String COLUMN_NAME_WORK_REPORT_GROUP = "WR_GROUP_ID";
	protected final static String COLUMN_NAME_AMOUNT = "AMOUNT";
	protected final static String COLUMN_NAME_ACCOUNT_KEY_ID = "ACC_KEY_ID";

	public WorkReportClubAccountRecordBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_REPORT_ID, "Id of the work report",true,true,Integer.class,"many-to-one",WorkReport.class);
		addAttribute(COLUMN_NAME_WORK_REPORT_GROUP,"The league id / club",true,true,String.class);
		addAttribute(COLUMN_NAME_AMOUNT,"Amount",true,true,Float.class);	
		addAttribute(COLUMN_NAME_ACCOUNT_KEY_ID, "Account key",true,true,Integer.class,"many-to-one",WorkReportAccountKey.class);
		
		this.addManyToOneRelationship(COLUMN_NAME_WORK_REPORT_GROUP,WorkReportGroup.class);		
	}
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	
	public void setWorkReportGroupId(int wrGroupId){
		setColumn(COLUMN_NAME_WORK_REPORT_GROUP,wrGroupId);
	}
	
	public int getWorkReportGroupId(){
		return getIntColumnValue(COLUMN_NAME_WORK_REPORT_GROUP);
	}
	
	public int getReportId() {
		return getIntColumnValue(COLUMN_NAME_REPORT_ID);
	}

	public void setReportId(int reportId) {
		setColumn(COLUMN_NAME_REPORT_ID,reportId);
	}
	
	public float getAmount() {
		return getFloatColumnValue(COLUMN_NAME_AMOUNT);
	}

	public void setAmount(float amount) {
		setColumn(COLUMN_NAME_AMOUNT,amount);
	}
	
	public int getAccountKeyId() {
		return getIntColumnValue(COLUMN_NAME_ACCOUNT_KEY_ID);
	}

	public void setAccountKeyId(int accountKeyId) {
		setColumn(COLUMN_NAME_ACCOUNT_KEY_ID,accountKeyId);
	}
	
	public Collection ejbFindAllRecordsByWorkReportId(int reportId) throws FinderException{
		return idoFindAllIDsByColumnOrderedBySQL(COLUMN_NAME_REPORT_ID,reportId);
	}
	
}
