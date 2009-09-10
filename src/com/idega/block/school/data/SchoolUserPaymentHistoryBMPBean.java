package com.idega.block.school.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;

/**
 * @author Gimmi
 */
public class SchoolUserPaymentHistoryBMPBean extends GenericEntity {

	private static String TABLE_NAME = "SCH_USER_PAYMENT_HIST";
	private static String COLUMN_SCHOOL_USER_PAYMENT = "SCH_USER_PAYMENT_ID";
	private static String COLUMN_MAX_PAYMENT = "MAX_PAYMENT";
	private static String COLUMN_DATE = "date";
	private static String COLUMN_MODIFICATION_DATE = "mod_date";

	public String getEntityName() {
		return TABLE_NAME;
	}


	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToManyRelationShip(SchoolUserPaymentBMPBean.class, COLUMN_SCHOOL_USER_PAYMENT);
		addAttribute(COLUMN_MAX_PAYMENT, "max payment", true, true, Integer.class);
		addAttribute(COLUMN_DATE, "date", true, true, Date.class);
		addAttribute(COLUMN_MODIFICATION_DATE, "modification date", true, true, Date.class);
	}

	public void setSchoolUserPaymentID(int id) {
		setColumn(COLUMN_SCHOOL_USER_PAYMENT, id);
	}
	
	public void setMaxPayment(int maxPayment) {
		setColumn(COLUMN_MAX_PAYMENT, maxPayment);
	}
	
	public void setDate(Date date) {
		setColumn(COLUMN_DATE, date);
	}
	
	public void setModificationDate(Date date) {
		setColumn(COLUMN_MODIFICATION_DATE, date);
	}
	
	public int getSchoolUserPaymentID() {
		return getIntColumnValue(COLUMN_SCHOOL_USER_PAYMENT);
	}
	
	public int getMaxPayment() {
		return getIntColumnValue(COLUMN_MAX_PAYMENT);
	}
	
	public Date getDate() {
		return getDateColumnValue(COLUMN_DATE);
	}
	
	public Date getModificationDate() {
		return getDateColumnValue(COLUMN_MODIFICATION_DATE);
	}
	
	public Collection ejbFindAllBySchoolUserPaymentID(int schoolUserPaymentID) throws FinderException {
		return this.idoFindAllIDsByColumnOrderedBySQL(COLUMN_SCHOOL_USER_PAYMENT, schoolUserPaymentID, COLUMN_DATE);
	}
	
	public Object ejbHomeMakeHistoryFromSchoolUserPaymentID(int schoolUserPaymentID) throws IDOLookupException, FinderException, CreateException {
		SchoolUserPaymentHistoryHome suphHome = (SchoolUserPaymentHistoryHome) IDOLookup.getHome(SchoolUserPaymentHistory.class);
		SchoolUserPaymentHome supHome = (SchoolUserPaymentHome) IDOLookup.getHome(SchoolUserPayment.class);
		
		SchoolUserPayment paym = supHome.findByPrimaryKey(new Integer(schoolUserPaymentID));
		
		SchoolUserPaymentHistory hist = suphHome.create();
		hist.setDate(paym.getDate());
		hist.setMaxPayment(paym.getMaxPayment());
		hist.setSchoolUserPaymentID(schoolUserPaymentID);
		hist.setModificationDate(IWTimestamp.RightNow().getDate());
		hist.store();
		
		return hist.getPrimaryKey();
	}
	
}
