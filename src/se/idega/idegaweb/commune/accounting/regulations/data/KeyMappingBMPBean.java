package se.idega.idegaweb.commune.accounting.regulations.data;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;

/**
 * @author Joakim
 * This is a table used to allow the system to know what the primary key for certain objects in 
 * tables realted to the regulation ("regelverk"). Since the data for the regulation is entered 
 * when the system is installed, we need this mapping to be able to reffer to specifics like 
 * "check" or "Childcare". This table needs to be updated at installation to reflect this. 
 * 
 * Probably depricated (joakim@idega.com)
 */
public class KeyMappingBMPBean extends GenericEntity implements KeyMapping, IDOLegacyEntity {
	private static final String ENTITY_NAME = "cacc_key_mapping";

	public static final String CAT_ACTIVITY ="activity";
	public static final String CAT_REG_SPEC ="reg_spec";
//	public static final String CAT_COMPANY_TYPE ="companyt_type";
	public static final String CAT_COMMUNE ="commune";

	public static final String KEY_CHILDCARE ="childcare";
	public static final String KEY_CHECK ="check";
	public static final String KEY_IN_COMMUNE ="in_commune";
			 
	private static final String COLUMN_CATEGORY = "category";
	private static final String COLUMN_KEY = "key";
	private static final String COLUMN_VALUE = "value";
	/**
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}
	/**
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_CATEGORY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_KEY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_VALUE, "", true, true, java.lang.Integer.class);
	}
	
	/**
	 * insertStartData inserts the data the the system needs to have to values for.
	 * This is done just to make it easier for the person configuring the system, and 
	 * reduce risk of forgetting setting a value
	 */
	public void insertStartData () throws Exception {
		super.insertStartData ();
		init(CAT_ACTIVITY, KEY_CHILDCARE);
		init(CAT_REG_SPEC, KEY_CHECK);
		init(CAT_COMMUNE, KEY_IN_COMMUNE);
	}

	/**
	 * Convenience function for the insertStartData() to reduce the clutter and make it easier
	 * to add more required cat - key pairs, needed for initialization
	 * @param cat Category
	 * @param key
	 * @throws IDOLookupException
	 * @throws CreateException
	 */
	private void init(String cat, String key) throws IDOLookupException, CreateException {
		KeyMappingHome home = (KeyMappingHome) IDOLookup.getHome(KeyMapping.class);
		KeyMapping map = home.create();
		map.setCategory(cat);
		map.setKey(key);
		map.store();
	}

	
	public String getCategory() {
		return getStringColumnValue(COLUMN_CATEGORY);
	}
	public String getKey() {
		return getStringColumnValue(COLUMN_KEY);
	}
	public int getValue() {
		return getIntColumnValue(COLUMN_VALUE);
	}

	public void set(String cat, String key, int val) {
		setColumn(COLUMN_CATEGORY, cat);
		setColumn(COLUMN_KEY, key);
		setColumn(COLUMN_VALUE, val);
	}
	
	public void setCategory(String s) {
		setColumn(COLUMN_CATEGORY, s);
	}
	public void setKey(String s) {
		setColumn(COLUMN_KEY, s);
	}
	public void setValue(int s) {
		setColumn(COLUMN_VALUE, s);
	}
	
	public Object ejbFindValueByCategoryAndKey(String category, String key) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CATEGORY, category);
		sql.appendAndEquals(COLUMN_KEY, key);
		return idoFindOnePKByQuery(sql);
	}
}
