/*
 * Created on Mar 29, 2004
 *
 */
package is.idega.idegaweb.campus.data;

import com.idega.data.PrimaryKey;

/**
 * ApartmentAccountEntryKey
 * @author aron 
 * @version 1.0
 */
public class ApartmentAccountEntryKey extends PrimaryKey {
	
	private String COLUMN_APARTMENT= ApartmentAccountEntryBMPBean.COLUMN_APARTMENT_ID;
	private String COLUMN_ENTRY = ApartmentAccountEntryBMPBean.COLUMN_ENTRY_ID;
	
	/**
	 * @param scorecardID
	 * @param holeID
	 */
	public ApartmentAccountEntryKey(Object apartmentID, Object entryID) {
		this();
		setApartment(apartmentID);
		setAccountEntry(entryID);
	}
	
	public ApartmentAccountEntryKey() {
		super();
	}
	
	public void setApartment(Object apartmentID) {
		setPrimaryKeyValue(COLUMN_APARTMENT, apartmentID);
	}

	public Object getApartment() {
		return getPrimaryKeyValue(COLUMN_APARTMENT);
	}

	public void setAccountEntry(Object entryID) {
		setPrimaryKeyValue(COLUMN_ENTRY, entryID);
	}

	public Object getAccountEntry() {
		return getPrimaryKeyValue(COLUMN_ENTRY);
	}
}
