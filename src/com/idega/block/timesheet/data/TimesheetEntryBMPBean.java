// idega 2000 - Gimmi
package com.idega.block.timesheet.data;
//import java.util.*;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.user.data.*;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
public class TimesheetEntryBMPBean
	extends com.idega.data.GenericEntity
	implements com.idega.block.timesheet.data.TimesheetEntry {
	protected static final String USER_ID = "USER_ID";
	protected static final String ENTRY_DATE = "ENTRY_DATE";
	protected static final String REGISTERED = "REGISTERED";
	protected static final String BOOKED = "BOOKED";
	protected static final String DESCRIPTION = "DESCRIPTION";
	protected static final String QUANTITY = "QUANTITY";
	protected static final String PROJECT_ID = "PROJECT_ID";
	protected static final String RESOURCE_ID = "RESOURCE_ID";
	protected static final String TIMESHEET_ENTRY_ID = "TMS_ENTRY_ID";
	public static final String TIMESHEET_ENTRY = "TMS_ENTRY";
	public final static String TABLE_NAME = TIMESHEET_ENTRY;
	
	public TimesheetEntryBMPBean() {
		super();
	}
	public TimesheetEntryBMPBean(int id) throws SQLException {
		super(id);
	}
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(ENTRY_DATE, "date", true, true, Timestamp.class);
		addAttribute(USER_ID,USER_ID,true,true,Integer.class,"many-to-one",User.class);
		addAttribute(RESOURCE_ID, RESOURCE_ID, true, true, Integer.class, "many-to-one", Resource.class);
		addAttribute(PROJECT_ID, "n�mer verkefnis", true, true, Integer.class, "many-to-one", TimesheetProject.class);
		addAttribute(QUANTITY, "fjoldi", true, true, Double.class);
		addAttribute(DESCRIPTION, "stutt l�sing", true, true, String.class);
		addAttribute(BOOKED, "b�ka�", true, true, Boolean.class);
		addAttribute(REGISTERED, "skr��", true, true, Boolean.class);
	}
	public String getIDColumnName() {
		return TIMESHEET_ENTRY_ID;
	}
	public String getEntityName() {
		return TIMESHEET_ENTRY;
	}
	public java.sql.Timestamp getDate() {
		return (java.sql.Timestamp) getColumnValue(ENTRY_DATE);
	}
	public void setDate(java.sql.Timestamp date) {
		setColumn(ENTRY_DATE, date);
	}
	public int getUserId() {
		return getIntColumnValue(USER_ID);
	}
	public void setUserId(int user_id) {
		setColumn(USER_ID, user_id);
	}
	public User getUser() {
		return (User) getColumnValue(USER_ID);
	}
	public int getResourceId() {
		return getIntColumnValue(RESOURCE_ID);
	}
	public void setResourceId(int resource_id) {
		setColumn(RESOURCE_ID, (new Integer(resource_id)));
	}
	public Resource getResource() {
		return (Resource)getColumnValue(RESOURCE_ID);
	}
	public void setProjectId(int project_id) {
		setColumn(PROJECT_ID, (new Integer(project_id)));
	}
	public int getProjectId() {
		return getIntColumnValue(PROJECT_ID);
	}
	public TimesheetProject getProject() {
		return (TimesheetProject)getColumnValue(PROJECT_ID);
	}
	public double getQuantity() {
		return ((Double) getColumnValue(QUANTITY)).doubleValue();
	}
	public void setQuantity(double qty) {
		setColumn(QUANTITY, new Double(qty));
	}
	public String getDescription() {
		return getStringColumnValue(DESCRIPTION);
	}
	public void setDescription(String description) {
		setColumn(DESCRIPTION, description);
	}
	public boolean isBooked() {
		return ((Boolean) getColumnValue(BOOKED)).booleanValue();
	}
	public void setBooked(boolean booked) {
		setColumn(BOOKED, booked);
	}
	public boolean isRegistered() {
		return ((Boolean) getColumnValue(REGISTERED)).booleanValue();
	}
	public void setRegistered(boolean registered) {
		setColumn(REGISTERED, registered);
	}
	
	
	public int ejbHomeCountByUserBeforeDate(Integer userID,Date date)throws IDOException{
		return idoGetNumberOfRecords(getQueryByUserBeforeDate(userID,date));
	}
	
	private IDOQuery getQueryByUserBeforeDate(Integer userID,Date date){
		return super.idoQueryGetSelect().appendWhereEquals(USER_ID,userID).appendAnd().append(ENTRY_DATE).appendLessThanSign().append(date).appendAndEqualsQuoted(BOOKED,"N");
	}
	
	public Collection ejbFindUnbookedByUserBeforeDate(Integer userID,Date date)throws FinderException{
		return super.idoFindPKsByQuery(getQueryByUserBeforeDate(userID,date).appendOrderBy(ENTRY_DATE));
	}
	
	public Collection ejbFindByDateAndUser(java.sql.Date date, Integer userID) throws FinderException {
		String[] orderBy = {TIMESHEET_ENTRY_ID,PROJECT_ID};
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(ENTRY_DATE,date).appendAndEquals(USER_ID,userID.intValue()).appendOrderBy(orderBy));
	}
	
	public Collection ejbFindByProjectWithinPeriod(Integer projectID,java.sql.Date dateFrom,java.sql.Date dateTo)throws FinderException{
		return ejbFindByUserAndProjectWithinPeriod(null,projectID,dateFrom,dateTo);
	}
	
	public Collection ejbFindByUserWithinPeriod(Integer userID,java.sql.Date dateFrom,java.sql.Date dateTo)throws FinderException{
		return ejbFindByUserAndProjectWithinPeriod(userID,null,dateFrom,dateTo);
	}
	
	private IDOQuery queryByUserAndProjectWithinPeriod(Integer userID,Integer projectID,java.sql.Date dateFrom,java.sql.Date dateTo){
		IDOQuery query = idoQueryGetSelect();
		query.appendWhere().appendWithinDates(ENTRY_DATE,dateFrom,dateTo);
		if(projectID!=null) {
			query.appendAndEquals(PROJECT_ID,projectID);
		}
		if(userID!=null) {
			query.appendAndEquals(USER_ID,userID);
		}
		return query;
	}
	
	public Collection ejbFindByUserAndProjectWithinPeriod(Integer userID,Integer projectID,java.sql.Date dateFrom,java.sql.Date dateTo)throws FinderException{
		return super.idoFindPKsByQuery(queryByUserAndProjectWithinPeriod(userID,projectID,dateFrom,dateTo));
	}
	
	public Collection ejbFindUnbookedByUserWithinPeriod(Integer userID,Date fromDate,Date toDate)throws FinderException{
		String[] orderBy = {ENTRY_DATE,PROJECT_ID};
		return super.idoFindPKsByQuery(queryByUserAndProjectWithinPeriod(userID,null,fromDate,toDate).appendAndEqualsQuoted(BOOKED,"N").appendAndEqualsQuoted(REGISTERED,"N").appendOrderBy(orderBy));
	}
	
	public Collection ejbFindBookedByUserWithinPeriod(Integer userID,Date fromDate,Date toDate)throws FinderException{
		String[] orderBy = {ENTRY_DATE,PROJECT_ID};
		return super.idoFindPKsByQuery(queryByUserAndProjectWithinPeriod(userID,null,fromDate,toDate).appendAndEqualsQuoted(BOOKED,"Y").appendAndEqualsQuoted(REGISTERED,"N").appendOrderBy(orderBy));
	}
}
