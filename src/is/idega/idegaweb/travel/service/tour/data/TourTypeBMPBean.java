package is.idega.idegaweb.travel.service.tour.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

/**
 * @author gimmi
 */
public class TourTypeBMPBean extends GenericEntity implements TourType {

	private static String ENTITY_NAME = "TB_TOUR_TYPE";
	private static String COLUMN_NAME = "TB_TOUR_TYPE_NAME";
	private static String COLUMN_TOUR_CATEGORY = "TB_TOUR_CATEGORY";
	private static String COLUMN_LOCALIZATION_KEY = "LOCALIZATION_KEY";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		this.addAttribute(getIDColumnName());
		this.addAttribute(COLUMN_NAME, "primKey", true, true, String.class);
		this.addAttribute(COLUMN_LOCALIZATION_KEY, "locKEy", true, true, String.class);
		
		this.addOneToOneRelationship(COLUMN_TOUR_CATEGORY, TourCategory.class);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZATION_KEY);
	}
	
	public void setLocalizationKey(String key) {
		setColumn(COLUMN_LOCALIZATION_KEY, key);
	}
	
	public String getTourCategory() {
		return getStringColumnValue(COLUMN_TOUR_CATEGORY);
	}
	
	public void setTourCategory(String category) {
		setColumn(COLUMN_TOUR_CATEGORY, category);
	}
	
	public Collection ejbFindByCategory(String category) throws FinderException {
		return idoFindAllIDsByColumnBySQL(COLUMN_TOUR_CATEGORY, category);
	}
	
	public Collection ejbFindAll() throws FinderException {
		return idoFindAllIDsBySQL();
	}
	
}
