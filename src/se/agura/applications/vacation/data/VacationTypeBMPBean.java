/*
 * Created on Nov 3, 2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package se.agura.applications.vacation.data;

import java.util.Collection;
import java.util.Map;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * @author Anna
 */
public class VacationTypeBMPBean extends GenericEntity implements VacationType {

	public static final String ENTITY_NAME = "vac_vacation_type";

	public static final String COLUMN_VACATION_TYPE_ID = "vacation_type_id";

	public static final String COLUMN_TYPE_NAME = "type_name";

	public static final String COLUMN_LOCALIZED_KEY = "localized_key";

	public static final String COLUMN_ALLOWS_FORWARDING = "allows_forwarding";

	public static final String COLUMN_MAX_DAYS = "max_days";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(COLUMN_VACATION_TYPE_ID);
		setAsPrimaryKey(COLUMN_VACATION_TYPE_ID, true);
		addAttribute(COLUMN_TYPE_NAME, "Type name", String.class);
		addAttribute(COLUMN_LOCALIZED_KEY, "Localized key", String.class);
		addAttribute(COLUMN_ALLOWS_FORWARDING, "Allows forwarding", Boolean.class);
		addAttribute(COLUMN_MAX_DAYS, "Max days", Integer.class);

		addMetaDataRelationship();
	}

	public boolean getAllowsForwarding() {
		return getBooleanColumnValue(COLUMN_ALLOWS_FORWARDING, false);
	}

	public void setAllowsForwarding(boolean allowes) {
		setColumn(COLUMN_ALLOWS_FORWARDING, allowes);
	}

	public int getMaxDays() {
		return getIntColumnValue(COLUMN_MAX_DAYS);
	}

	public void setMaxDays(int maxDays) {
		setColumn(COLUMN_ALLOWS_FORWARDING, maxDays);
	}

	public String getTypeName() {
		return getStringColumnValue(COLUMN_TYPE_NAME);
	}

	public void setTypeName(String typeName) {
		setColumn(COLUMN_TYPE_NAME, typeName);
	}

	public String getLocalizedKey() {
		return getStringColumnValue(COLUMN_LOCALIZED_KEY);
	}

	public void setLocalizedKey(String localizedKey) {
		setColumn(COLUMN_LOCALIZED_KEY, localizedKey);
	}

	// Metadata methods
	public void setExtraTypeInformation(String key, String value, String type) {
		addMetaData(key, value, type);
	}

	public String getExtraTypeInformation(String key) {
		return getMetaData(key);
	}

	public String getExtraTypeInformationType(String key) {
		Map map = this.getMetaDataTypes();
		if (map != null) {
			return (String) map.get(key);
		}
		return null;
	}

	public void removeExtraTypeInformation(String key) {
		removeMetaData(key);
	}

	public Collection ejbFindAll() throws FinderException {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		return idoFindPKsByQuery(query);
	}
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#insertStartData()
	 */
	public void insertStartData() throws Exception {
		VacationTypeHome home = (VacationTypeHome) IDOLookup.getHome(VacationType.class);
		
		VacationType type = home.create();
		type.setAllowsForwarding(false);
		type.setTypeName("Compensation vacation");
		type.setLocalizedKey("vacation_type.compensation");
		type.store();

		type = home.create();
		type.setAllowsForwarding(false);
		type.setTypeName("Flextime");
		type.setLocalizedKey("vacation_type.flextime");
		type.store();

		type = home.create();
		type.setAllowsForwarding(false);
		type.setTypeName("Preacher leave");
		type.setLocalizedKey("vacation_type.preacher_leave");
		type.store();

		type = home.create();
		type.setAllowsForwarding(false);
		type.setTypeName("General");
		type.setLocalizedKey("vacation_type.general");
		type.store();

		type = home.create();
		type.setAllowsForwarding(false);
		type.setTypeName("Parental leave");
		type.setLocalizedKey("vacation_type.parental_leave");
		type.setMetaData("parental_leave_type", "over_lowest_level,lowest_level,fathers_days", "com.idega.presentation.ui.RadioButton");
		type.setMetaData("child_personal_id", "", "com.idega.presentation.ui.TextInput");
		type.store();

		type = home.create();
		type.setAllowsForwarding(false);
		type.setTypeName("Study break");
		type.setLocalizedKey("vacation_type.study_break");
		type.setMetaData("with_payment", "with,without", "com.idega.presentation.ui.RadioButton");
		type.store();

		type = home.create();
		type.setAllowsForwarding(false);
		type.setTypeName("Official assignment");
		type.setLocalizedKey("vacation_type.official_assignment");
		type.setMetaData("explanation", "", "com.idega.presentation.ui.TextArea");
		type.store();

		type = home.create();
		type.setAllowsForwarding(false);
		type.setTypeName("Private matter");
		type.setLocalizedKey("vacation_type.private_matter");
		type.setMetaData("explanation", "", "com.idega.presentation.ui.TextArea");
		type.setMetaData("with_payment", "with,without", "com.idega.presentation.ui.RadioButton");
		type.store();

		type = home.create();
		type.setAllowsForwarding(true);
		type.setTypeName("Union assignment");
		type.setLocalizedKey("vacation_type.union_assignment");
		type.setMetaData("explanation", "", "com.idega.presentation.ui.TextArea");
		type.setMetaData("file", "", "com.idega.block.media.presentation.FileChooser");
		type.setMetaData("with_payment", "with,without", "com.idega.presentation.ui.RadioButton");
		type.store();

		type = home.create();
		type.setAllowsForwarding(false);
		type.setTypeName("Other");
		type.setLocalizedKey("vacation_type.other");
		type.setMetaData("explanation", "", "com.idega.presentation.ui.TextArea");
		type.setMetaData("with_payment", "with,without", "com.idega.presentation.ui.RadioButton");
		type.store();
	}
}