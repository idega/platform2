package com.idega.block.school.data;

import com.idega.data.GenericEntity;
import com.idega.user.data.User;

/**
 * @author Gimmi
 */
public class SchoolUserInfoBMPBean extends GenericEntity {

	private static String TABLE_NAME = "sch_user_info";
	private static String COLUMN_USER_ID = "ic_user_id";
	private static String COLUMN_CHILD_CARE_AGE = "child_care_age";
	private static String COLUMN_SIBLING_NUMBER = "sibling_number";
	private static String COLUMN_INVOICE_RECEIVER = "invoice_receiver";

	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToManyRelationShip(User.class, COLUMN_USER_ID);
		addAttribute(COLUMN_CHILD_CARE_AGE, "child careage", true, true, Integer.class);
		addAttribute(COLUMN_SIBLING_NUMBER, "sibling number", true, true, Integer.class);
		addManyToManyRelationShip(User.class, COLUMN_INVOICE_RECEIVER);
	}
	
	/* SET */
	public void setUserID(int id) {
		setColumn(COLUMN_USER_ID, id);
	}
	
	public void setChildCareAge(int age) {
		setColumn(COLUMN_CHILD_CARE_AGE, age);
	}
	
	public void setSiblingNumber(int number) {
		setColumn(COLUMN_SIBLING_NUMBER, number);
	}
	
	public void setInvoiceReceiver(int userId) {
		setColumn(COLUMN_INVOICE_RECEIVER, userId);
	}
	
	/* GET */
	public int getUserID() {
		return getIntColumnValue(COLUMN_USER_ID);
	}
	
	public int getChildCareAge() {
		return getIntColumnValue(COLUMN_CHILD_CARE_AGE);
	}
	
	public int getSiblingNumber() {
		return getIntColumnValue(COLUMN_SIBLING_NUMBER);
	}

	public int getInvoiceReceiver() {
		return getIntColumnValue(COLUMN_INVOICE_RECEIVER);
	}

}
