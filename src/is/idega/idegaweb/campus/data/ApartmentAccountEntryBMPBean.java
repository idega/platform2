/*
 * Created on Mar 15, 2004
 *
 */
package is.idega.idegaweb.campus.data;

import com.idega.block.building.data.Apartment;
import com.idega.block.finance.data.AccountEntry;
import com.idega.data.GenericEntity;

/**
 * ApartmentAccountEntryBMPBean
 * @author aron 
 * @version 1.0
 */
public class ApartmentAccountEntryBMPBean extends GenericEntity implements ApartmentAccountEntry {
	public final static String ENTITY_NAME = "CAM_APRT_ACC_ENTRY";
	public final static String COLUMN_APARTMENT_ID = "APRT_ID";
	public final static String COLUMN_ENTRY_ID = "ENTRY_ID";
	
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getIDColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_ENTRY_ID;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addManyToOneRelationship(COLUMN_APARTMENT_ID, Apartment.class);
		addManyToOneRelationship(COLUMN_ENTRY_ID, AccountEntry.class);
		setAsPrimaryKey(COLUMN_ENTRY_ID, true);
		
		setNullable(COLUMN_APARTMENT_ID, false);
		setNullable(COLUMN_ENTRY_ID, false);
	}
	
	public void setAccountEntryID(Integer entryID){
		setColumn(COLUMN_ENTRY_ID,entryID);
	}
	
	public Integer getAccountEntryID(){
		return getIntegerColumnValue(COLUMN_ENTRY_ID);
	}
	
	public void setApartmentID(Integer apartmentID){
		setColumn(COLUMN_APARTMENT_ID,apartmentID);
	}
	
	public Integer getApartmentID(){
		return getIntegerColumnValue(COLUMN_APARTMENT_ID);
	}
	
}
