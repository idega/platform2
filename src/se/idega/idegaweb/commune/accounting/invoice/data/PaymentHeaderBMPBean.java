package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolBMPBean;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolManagementType;
import com.idega.block.school.data.SchoolManagementTypeHome;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author Joakim
 *
 */
public class PaymentHeaderBMPBean extends GenericEntity implements PaymentHeader {
	private static final String ENTITY_NAME = "cacc_payment_header";

	private static final String COLUMN_SCHOOL_ID = "";
	private static final String COLUMN_SCHOOL_CATEGORY_ID = "";
	private static final String COLUMN_SIGNATURE = "";
	private static final String COLUMN_DATE_ATTESTED = "date_attested";
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_PERIOD = "period";
	

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_SCHOOL_ID, School.class);
		addManyToOneRelationship(COLUMN_SCHOOL_CATEGORY_ID, SchoolCategory.class);
		addManyToOneRelationship(COLUMN_SIGNATURE, User.class);
		addAttribute(COLUMN_STATUS, "", true, true, java.lang.String.class,1);
		addAttribute(COLUMN_DATE_ATTESTED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_PERIOD, "", true, true, java.sql.Date.class);
	}
	public int getSchoolID() {
		return getIntColumnValue(COLUMN_SCHOOL_ID);
	}
	public School getSchool() {
		return (School)getColumnValue(COLUMN_SCHOOL_ID);
	}
	public int getSchoolCategoryID() {
		return getIntColumnValue(COLUMN_SCHOOL_CATEGORY_ID);
	}
	public int getSignatureID() {
		return getIntColumnValue(COLUMN_SIGNATURE);
	}
	public char getStatus() {
		return getCharColumnValue(COLUMN_STATUS);
	}
	public Date getDateAttested() {
		return getDateColumnValue(COLUMN_DATE_ATTESTED);
	}
	public Date getPeriod() {
		return getDateColumnValue(COLUMN_PERIOD);
	}


	public void setSchoolID(int i) {
		setColumn(COLUMN_SCHOOL_ID, i);
	}
	public void setSchoolID(School s) {
		setColumn(COLUMN_SCHOOL_ID, s);
	}
	public void setSchoolCategoryID(int i) {
		setColumn(COLUMN_SCHOOL_CATEGORY_ID, i);
	}
	public void setSchoolCategoryID(SchoolCategory s) {
		setColumn(COLUMN_SCHOOL_CATEGORY_ID, s);
	}
	public void setSignaturelID(int i) {
		setColumn(COLUMN_SIGNATURE, i);
	}
	public void setSignaturelID(User u) {
		setColumn(COLUMN_SIGNATURE, u);
	}
	public void setStatus(char c) {
		setColumn(COLUMN_STATUS, c);
	}
	public void setDateAttested(Date d) {
		setColumn(COLUMN_DATE_ATTESTED, d);
	}
	public void setDateChanged(Date d) {
		setColumn(COLUMN_PERIOD, d);
	}
	
	public Integer ejbFindBySchoolCategorySchoolPeriod(School school, SchoolCategory schoolCategory, Date period) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_SCHOOL_ID, school.getPrimaryKey());
		sql.appendWhereEquals(COLUMN_SCHOOL_CATEGORY_ID, schoolCategory.getPrimaryKey());
		sql.appendWhereEquals(COLUMN_PERIOD, period);
		return (Integer)idoFindOnePKByQuery(sql);
	}

	public Collection ejbFindBySchoolCategoryAndPeriodForPrivate(SchoolCategory schoolCategory, Date period) throws IDOLookupException, EJBException, FinderException {
		IWTimestamp start = new IWTimestamp(period);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);
		
		Integer managementType = (Integer)((SchoolManagementTypeHome) IDOLookup.getHome(SchoolManagementType.class)).findPrivateManagementType().getPrimaryKey();
		
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this+" ph, "+SchoolBMPBean.SCHOOL+" s");
		sql.appendWhere("ph."+COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start.getDate());
		sql.appendAnd().append("ph."+COLUMN_PERIOD).appendLessThanSign().append(end.getDate());
		sql.appendAndEquals("ph."+COLUMN_SCHOOL_CATEGORY_ID, schoolCategory.getPrimaryKey());
		sql.appendAndEquals("ph."+COLUMN_SCHOOL_ID,"s.sch_school_id");
		sql.appendAndEquals("s.management_type",managementType);
		return idoFindPKsBySQL(sql.toString());
	}

}
