/*
 * Created on 3.9.2003
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;

/**
 * @author laddi
 */
public class SchoolManagementTypeBMPBean extends GenericEntity implements SchoolManagementType {

	public static final String ENTITY_NAME = "sch_management_type";

	public static final String COLUMN_MANAGEMENT_TYPE = "management_type";
	public static final String COLUMN_NAME = "management_type_name";
	public static final String COLUMN_LOCALIZED_KEY = "localized_key";
	
	public static final String TYPE_COMMUNE = "COMMUNE";
	public static final String TYPE_PRIVATE = "PRIVATE";
	public static final String TYPE_COMPANY = "COMPANY";
	public static final String TYPE_GOVERNMENT = "GOVERNMENT";
	public static final String TYPE_FOUNDATION = "FOUNDATION";
	public static final String TYPE_COUNTY_COUNCIL = "COUNTY_COUNCIL";
	public static final String TYPE_COOPERATIVE_COMMUNE_LIABILITY = "COOPERATIVE_COMMUNE_LIABILITY";
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOEntityBean#getPrimaryKeyClass()
	 */
	public Class getPrimaryKeyClass() {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getIDColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_MANAGEMENT_TYPE;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#insertStartData()
	 */
	public void insertStartData() throws Exception {

		String[] types = {
				TYPE_COMMUNE,
				TYPE_PRIVATE,
				TYPE_COMPANY,
				TYPE_GOVERNMENT,
				TYPE_FOUNDATION,
				TYPE_COUNTY_COUNCIL,
				TYPE_COOPERATIVE_COMMUNE_LIABILITY
		};
		
		String[] names = {
				"Commune",
				"Private",
				"Company",
				"Government",
				"Foundation",
				"County council",
				"Cooperative with commune employer liability"
		};
		
		SchoolManagementTypeHome typeHome = (SchoolManagementTypeHome) IDOLookup.getHome(SchoolManagementType.class);
		SchoolManagementType type;
		
		for (int i = 0; i < types.length; i++) {
			type = typeHome.create();
			type.setType(types[i]);
			type.setName(names[i]);
			type.setLocalizedKey("school_management_type."+types[i]);
			type.store();
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(COLUMN_MANAGEMENT_TYPE,"Type",String.class, 30);
		setAsPrimaryKey(COLUMN_MANAGEMENT_TYPE, true);

		addAttribute(COLUMN_NAME, "Name of type", String.class, 255);
		addAttribute(COLUMN_LOCALIZED_KEY, "Localized key for type", String.class, 255);
	}

	//Setters
	public void setType(String type) {
		setColumn(COLUMN_MANAGEMENT_TYPE, type.toUpperCase());
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setLocalizedKey(String key) {
		setColumn(COLUMN_LOCALIZED_KEY, key);
	}

	//Getters
	public String getType() {
		return getStringColumnValue(COLUMN_MANAGEMENT_TYPE);
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public String getLocalizedKey() {
		return getStringColumnValue(COLUMN_LOCALIZED_KEY);
	}
	
	//Find methods
	public Collection ejbFindAllManagementTypes() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		
		return idoFindPKsByQuery(query);
	}

	public String ejbFindManagementType(String name) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_NAME, name);
		
		return (String) idoFindOnePKByQuery(query);
	}

	public String ejbFindCommuneManagementType() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_MANAGEMENT_TYPE, TYPE_COMMUNE);
		return (String) idoFindOnePKByQuery(query);
	}

	public String ejbFindPrivateManagementType() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_MANAGEMENT_TYPE, TYPE_PRIVATE);
		return (String) idoFindOnePKByQuery(query);
	}
	
	public String ejbFindCompanyManagementType() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_NAME, TYPE_COMPANY);
		return (String) idoFindOnePKByQuery(query);
	}
	
	public String ejbFindGovernmentManagementType() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_NAME, TYPE_GOVERNMENT);
		return (String) idoFindOnePKByQuery(query);
	}
	
	public String ejbFindFoundationManagementType() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_NAME, TYPE_FOUNDATION);
		return (String) idoFindOnePKByQuery(query);
	}
	
	public String ejbFindCountyCouncilManagementType() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_NAME, TYPE_COUNTY_COUNCIL);
		return (String) idoFindOnePKByQuery(query);
	}
	
	public String ejbFindCooperativeCommuneLiabilityManagementType() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_NAME, TYPE_COOPERATIVE_COMMUNE_LIABILITY);
		return (String) idoFindOnePKByQuery(query);
	}
}