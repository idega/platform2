package is.idega.idegaweb.campus.block.application.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.application.data.Application;
import com.idega.block.building.data.Apartment;
import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public class RejectionHistoryBMPBean extends GenericEntity implements
		RejectionHistory {

	protected static final String ENTITY_NAME = "cam_rejection_history";
	
	protected static final String COLUMN_APPLICATION = "app_application_id";
	
	protected static final String COLUMN_DATE = "rejection_date";
	
	protected static final String COLUMN_APARTMENT = "bu_apartment_id";
	
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_APPLICATION, Application.class);
		addAttribute(COLUMN_DATE, "Date of rejection", Timestamp.class);
		addManyToOneRelationship(COLUMN_APARTMENT, Apartment.class);
	}
	
	//getters
	public Application getApplication() {
		return (Application) getColumnValue(COLUMN_APPLICATION);
	}
	
	public Timestamp getRejectionDate() {
		return getTimestampColumnValue(COLUMN_DATE);
	}
	
	public Apartment getApartment() {
		return (Apartment) getColumnValue(COLUMN_APARTMENT);
	}
	
	//setters
	public void setApplication(Application application) {
		setColumn(COLUMN_APPLICATION, application);
	}
	
	public void setRejectionDate(Timestamp date) {
		setColumn(COLUMN_DATE, date);
	}
	
	public void setApartment(Apartment apartment) {
		setColumn(COLUMN_APARTMENT, apartment);
	}
	
	//ejb
	public Collection ejbFindAllByApplication(Application application) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_APPLICATION), MatchCriteria.EQUALS, application));
		
		return idoFindPKsByQuery(query);
	}
}