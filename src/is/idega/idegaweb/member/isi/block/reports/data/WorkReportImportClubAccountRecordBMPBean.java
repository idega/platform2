/*
 * Created on Sep 17, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

/**
 * @author palli
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WorkReportImportClubAccountRecordBMPBean extends GenericEntity implements WorkReportImportClubAccountRecord{
	protected final static String ENTITY_NAME = "TMP_WR_CAR";
	protected final static String COLUMN_NAME_REPORT_ID = "ISI_WORK_REPORT_ID";
	protected final static String COLUMN_NAME_WORK_REPORT_GROUP = "ISI_WR_GROUP_ID";
	protected final static String COLUMN_NAME_AMOUNT = "AMOUNT";
	protected final static String COLUMN_NAME_ACCOUNT_KEY_ID = "ACC_KEY_ID";

	public WorkReportImportClubAccountRecordBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_REPORT_ID, "Id of the work report",true,true,Integer.class,"many-to-one",WorkReport.class);
		addAttribute(COLUMN_NAME_WORK_REPORT_GROUP,"The league id / club",true,true,Integer.class, "many-to-one",WorkReportGroup.class);
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
	
	public void setWorkReportGroup(WorkReportGroup group) {
		setColumn(COLUMN_NAME_WORK_REPORT_GROUP,group);
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
	
	public void setReport(WorkReport report) {
		setColumn(COLUMN_NAME_REPORT_ID,report);
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
	
	public void setAccountKey(WorkReportAccountKey key) {
		setColumn(COLUMN_NAME_ACCOUNT_KEY_ID,key);
	}
	
	public Collection ejbFindAllRecordsByWorkReportId(int reportId) throws FinderException{
		return idoFindAllIDsByColumnOrderedBySQL(COLUMN_NAME_REPORT_ID,reportId);
	}
}


