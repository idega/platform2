/*
 * Created on 14.7.2003
 */
package is.idega.idegaweb.golf.course.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;

/**
 * @author laddi
 */
public class TeeColorBMPBean extends GenericEntity implements TeeColor {

	public static final String ENTITY_NAME = "golf_tee_color";

	public static final String COLUMN_TEE_COLOR_ID = "tee_color_id";
	public static final String COLUMN_NAME = "tee_color_name";
	public static final String COLUMN_COLOR = "tee_color";
	public static final String COLUMN_LOCALIZED_KEY = "localized_key";
	
	public static final String TEE_COLOR_WHITE = "WHITE";
	public static final String TEE_COLOR_YELLOW = "YELLOW";
	public static final String TEE_COLOR_BLUE = "BLUE";
	public static final String TEE_COLOR_RED = "RED";

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getIDColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_TEE_COLOR_ID;
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
		addAttribute(COLUMN_TEE_COLOR_ID);
		setAsPrimaryKey(COLUMN_TEE_COLOR_ID, true);
		
		addAttribute(COLUMN_NAME, "The tee color name", true, true, String.class, 255);
		addAttribute(COLUMN_COLOR, "The color", true, true, String.class, 255);
		addAttribute(COLUMN_LOCALIZED_KEY, "The localized key", true, true, String.class, 255);
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#insertStartData()
	 */
	public void insertStartData() throws Exception {
		TeeColorHome teeColorHome = (TeeColorHome) IDOLookup.getHome(TeeColor.class);

		final String [][] startData = {
			{ "WHITE", "#FFFFFF", "tee_color.white" },
			{ "YELLOW", "#FFFF00", "tee_color.yellow" },
			{ "BLUE", "#0000FF", "tee_color.blue" },
			{ "RED", "#FF0000", "tee_color.red" }
		};
		
		for (int i = 0; i < startData.length; i++) {
			String name = startData[i][0];
			String color = startData[i][1];
			String localizedKey = startData[i][2];
		
			TeeColor teeColor = teeColorHome.create();
			teeColor.setTeeColorName(name);
			teeColor.setTeeColor(color);
			teeColor.setTeeColorLocalizedKey(localizedKey);
			teeColor.store();
		}
	}
	
	//Getters
	public String getTeeColorName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public String getTeeColor() {
		return getStringColumnValue(COLUMN_COLOR);
	}

	public String getTeeColorLocalizedKey() {
		return getStringColumnValue(COLUMN_LOCALIZED_KEY);
	}
	
	//Setters
	public void setTeeColorName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setTeeColor(String color) {
		setColumn(COLUMN_COLOR, color);
	}

	public void setTeeColorLocalizedKey(String key) {
		setColumn(COLUMN_LOCALIZED_KEY, key);
	}

	//Find methods
	public Collection ejbFindAll() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		return idoFindPKsByQuery(query);
	}
}