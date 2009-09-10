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

	public static final String ENTITY_NAME = "SCH_MANAGEMENT_TYPE";

	public static final String COLUMN_MANAGEMENT_TYPE = "MANAGEMENT_TYPE";
	public static final String COLUMN_NAME = "MANAGEMENT_TYPE_NAME";
	public static final String COLUMN_LOCALIZED_KEY = "LOCALIZED_KEY";
	
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

	public Collection ejbFindManagementTypesByCategories(String[] categories) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect();
		query.append("distinct mt.*").appendFrom();
		query.append(getEntityName() + " mt, ");
		query.append("sch_school s, ");
		query.append("sch_school_sch_school_type sst, ");
		query.append("sch_school_type st, ");
		query.append("sch_school_category c");
		query.appendWhere();
		query.appendEquals("sst.sch_school_type_id", "st.sch_school_type_id");
		query.appendAnd().appendEquals("sst.sch_school_id", "s.sch_school_id");
		query.appendAnd().appendEquals("st.school_category", "c.category");
		query.appendAnd().appendEquals("s.management_type", "mt.management_type");
		query.appendAnd().append("c.category in ").appendLeftParenthesis();
		query.appendCommaDelimitedWithinSingleQuotes(categories).appendRightParenthesis();
		
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