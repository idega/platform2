package is.idega.idegaweb.travel.service.tour.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;

/**
 * @author gimmi
 */
public class TourCategoryBMPBean extends GenericEntity implements TourCategory {
	
	private static String ENTITY_NAME = "TB_TOUR_CATEGORY";
	private static String COLUMN_PRIMARY_KEY = "TB_TOUR_CATEGORY_NAME";
	private static String COLUMN_LOCALIZATION_KEY = "LOCALIZATION_KEY";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		this.addAttribute(COLUMN_PRIMARY_KEY, "primKey", true, true, String.class, 30);
		this.addAttribute(COLUMN_LOCALIZATION_KEY, "locKEy", true, true, String.class);
		
		this.setAsPrimaryKey(COLUMN_PRIMARY_KEY, true);
	}
	
	public void insertStartData() {
		try {
			TourCategoryHome home = (TourCategoryHome) IDOLookup.getHome(TourCategory.class);
			
			TourCategory transportation = home.create();
			transportation.setName(CATEGORY_TRANSPORTATION);
			transportation.setLocalizationKey("iwt_tranportation");
			transportation.store();
			
			TourCategory excursion = home.create();
			excursion.setName(CATEGORY_EXCURSION);
			excursion.setLocalizationKey("iwt_excursion");
			excursion.store();
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	public Class getPrimaryKeyClass() {
		return String.class;
	}
	
	public String getName() {
		return super.getName();
	}
	
	public void setName(String name) {
		setColumn(COLUMN_PRIMARY_KEY, name);
	}
	
	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZATION_KEY);
	}
	
	public void setLocalizationKey(String key) {
		setColumn(COLUMN_LOCALIZATION_KEY, key);
	}
	
	public Collection ejbFindAll() throws FinderException {
		return idoFindAllIDsBySQL();
	}
	
	
}
