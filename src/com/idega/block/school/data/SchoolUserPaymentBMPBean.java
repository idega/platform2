package com.idega.block.school.data;

import java.sql.Date;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.user.data.User;

/**
 * @author Gimmi
 */
public class SchoolUserPaymentBMPBean extends GenericEntity {

	private static String TABLE_NAME = "sch_user_payment";
	private static String COLUMN_USER_ID = "ic_user_id";
	private static String COLUMN_MAX_PAYMENT = "max_payment";
	private static String COLUMN_DATE = "date";
	

	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToManyRelationShip(User.class, COLUMN_USER_ID);
		addAttribute(COLUMN_MAX_PAYMENT, "max_payment", true, true, Integer.class);
		addAttribute(COLUMN_DATE, "date of payment", true, true, Date.class);
	}
	
	/* SET */
	public void setUserID(int userId) {
		setColumn(COLUMN_USER_ID, userId);
	}
	
	public void setMaxPayment(int maxPayment) {
		setColumn(COLUMN_MAX_PAYMENT, maxPayment);
	}

	public void setDate(Date date) {
		setColumn(COLUMN_DATE, date);
	}
	
	/* GET */
	public int getUserID() {
		return getIntColumnValue(COLUMN_USER_ID);
	}
	
	public int getMaxPayment() {
		return getIntColumnValue(COLUMN_MAX_PAYMENT);
	}
	
	public Date getDate() {
		return getDateColumnValue(COLUMN_DATE);
	}

	/* MET */
	public void updateHistortAndStore() {
		try {
			int id = -1;
			id = new Integer(getPrimaryKey().toString()).intValue();
			if (id > 0) {
				SchoolUserPaymentHistoryHome suphHome = (SchoolUserPaymentHistoryHome) IDOLookup.getHome(SchoolUserPaymentHistory.class);
				suphHome.makeHistoryFromSchoolUserPaymentID(id);
				debug("Updated history");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		super.store();
	}

}
