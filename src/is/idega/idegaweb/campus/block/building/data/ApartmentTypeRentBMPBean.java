package is.idega.idegaweb.campus.block.building.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.building.data.ApartmentType;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * 
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * 
 * @version 1.0
 * 
 */
public class ApartmentTypeRentBMPBean extends GenericEntity implements
		ApartmentTypeRent {
	public final static int ZEROYEAR = 2000;
	
	protected final static String COLUMN_OTHER_EXPENSES = "other_expenses";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(getColumnApartmentTypeId(),
				ApartmentType.class);
		addAttribute(getColumnRent(), "Rent", Float.class);
		addAttribute(getColumnValidFrom(), "From date", java.sql.Date.class);
		addAttribute(getColumnValidTo(), "To date", java.sql.Date.class);
		addAttribute(COLUMN_OTHER_EXPENSES, "Other expenses", Double.class);
	}

	public static String getEntityTableName() {
		return "CAM_AP_TP_RENT";
	}

	public static String getColumnApartmentTypeId() {
		return "BU_APRT_TYPE_ID";
	}

	public static String getColumnRent() {
		return "RENT";
	}

	public static String getColumnValidFrom() {
		return "VALID_FROM";
	}

	public static String getColumnValidTo() {
		return "VALID_TO";
	}

	public String getEntityName() {
		return getEntityTableName();
	}

	public int getApartmentTypeId() {
		return getIntColumnValue(getColumnApartmentTypeId());
	}

	public void setApartmentTypeId(int id) {
		setColumn(getColumnApartmentTypeId(), id);
	}

	public void setApartmentTypeId(Integer id) {
		setColumn(getColumnApartmentTypeId(), id);
	}

	public float getRent() {
		return getFloatColumnValue(getColumnRent());
	}

	public void setRent(Float name) {
		setColumn(getColumnRent(), name);
	}

	public double getOtherExpeneses() {
		return getDoubleColumnValue(COLUMN_OTHER_EXPENSES, 0.0d);
	}
	
	public void setOtherExpenses(double otherExpenses) {
		setColumn(COLUMN_OTHER_EXPENSES, otherExpenses);
	}
	
	public java.sql.Date getValidFrom() {
		return (java.sql.Date) getColumnValue(getColumnValidFrom());
	}

	public void setValidFrom(java.sql.Date use_date) {
		setColumn(getColumnValidFrom(), use_date);
	}

	public java.sql.Date getValidTo() {
		return (java.sql.Date) getColumnValue(getColumnValidTo());
	}

	public void setValidTo(java.sql.Date use_date) {
		setColumn(getColumnValidTo(), use_date);
	}

	public Collection ejbFindByType(int apartmentTypeId) throws FinderException {
		IDOQuery query = this.idoQueryGetSelect();
		query.appendWhereEquals(getColumnApartmentTypeId(), apartmentTypeId);
		query.appendOrderBy(getColumnValidFrom());
		return super.idoFindPKsByQuery(query);
	}

	public Object ejbFindByTypeAndValidity(int aprtTypeId, Date dateToCheck)
			throws FinderException {
		IDOQuery query = this.idoQueryGetSelect();
		query.appendWhereEquals(getColumnApartmentTypeId(), aprtTypeId);
		query.appendAnd().append(dateToCheck).appendGreaterThanOrEqualsSign()
				.append(getColumnValidFrom());
		query.appendOrderByDescending(getColumnValidFrom());
		return idoFindOnePKByQuery(query);
	}
}
