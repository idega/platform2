package is.idega.idegaweb.travel.service.hotel.data;

import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * @author gimmi
 */
public class HotelTypeBMPBean extends GenericEntity implements HotelType {

	private static String HOTEL_TYPE_TABLE 			= "TB_ACCOMOTATION_TYPE";
	private static String COLUMN_LOCALIZATION_KEY 	= "LOCALIZATION_KEY";
	private static String COLUMN_USE_RATING = "USE_RATING";
	private static String COLUMN_VALID 	= "IS_VALID";
	
	
	public String getEntityName() {
		return HOTEL_TYPE_TABLE;
	}

	public void initializeAttributes() {
		this.addAttribute(getIDColumnName());
		this.addAttribute(COLUMN_LOCALIZATION_KEY, "localized key", true, true, String.class);
		this.addAttribute(COLUMN_VALID, "isValid", true, true, Boolean.class);
		this.addAttribute(COLUMN_USE_RATING, "useRating", true, true, Boolean.class);
	}
	
	public void setDefaultValues() {
		setColumn(COLUMN_VALID, true);
	}
	
	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZATION_KEY);
	}
	
	public boolean getUseRating() {
		return getBooleanColumnValue(COLUMN_USE_RATING);
	}
	
	public void setLocalizationKey(String locKey) {
		setColumn(COLUMN_LOCALIZATION_KEY, locKey);
	}
	
	public void setUseRating(boolean useRating) {
		setColumn(COLUMN_USE_RATING, useRating);
	}
	
	public Object ejbFindByLocalizationKey(String locKey) throws FinderException {
		IDOQuery query = this.idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_VALID, true)
		.appendAndEqualsQuoted(COLUMN_LOCALIZATION_KEY, locKey);
		return this.idoFindOnePKByQuery(query);
	}
	
	public Collection ejbFindAll() throws FinderException {
		IDOQuery query = this.idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_VALID, true);
		return this.idoFindPKsByQuery(query);
	}
	
	public void remove() throws RemoveException {
		setColumn(COLUMN_VALID, false);
		store();
	}
	
}
