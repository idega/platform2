/*
 * Created on 20.8.2003
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
public class SchoolCategoryBMPBean extends GenericEntity implements SchoolCategory {

	public static final String ENTITY_NAME = "SCH_SCHOOL_CATEGORY";

	public static final String COLUMN_CATEGORY = "CATEGORY";
	public static final String COLUMN_NAME = "CATEGORY_NAME";
	public static final String COLUMN_LOCALIZED_KEY = "localized_key";
	
	public static final String CATEGORY_MUSIC_SCHOOL = "MUSIC_SCHOOL";
	public static final String CATEGORY_CHILD_CARE = "CHILD_CARE";
	public static final String CATEGORY_ELEMENTARY_SCHOOL = "ELEMENTARY_SCHOOL";
	public static final String CATEGORY_HIGH_SCHOOL = "HIGH_SCHOOL";
	public static final String CATEGORY_COLLEGE = "COLLEGE";
	public static final String CATEGORY_UNIVERSITY = "UNIVERSITY";
	public static final String CATEGORY_PREFIX = "school_category.";
	
	/* (non-Javadoc)
	 * @see com.idega.data.IDOEntityBean#getPrimaryKeyClass()
	 */
	public Class getPrimaryKeyClass() {
		return String.class;
	}

	public String getIDColumnName(){
		return COLUMN_CATEGORY;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#insertStartData()
	 */
	public void insertStartData() throws Exception {
		String[] categories = { CATEGORY_CHILD_CARE, CATEGORY_ELEMENTARY_SCHOOL, CATEGORY_HIGH_SCHOOL, CATEGORY_COLLEGE, CATEGORY_UNIVERSITY };
		String[] names = { "Child care", "Elementary school", "High school", "College", "University" };
		
		SchoolCategoryHome categoryHome = (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
		SchoolCategory category;
		
		for (int i = 0; i < categories.length; i++) {
			category = categoryHome.create();
			category.setCategory(categories[i]);
			category.setName(names[i]);
			category.setLocalizedKey(CATEGORY_PREFIX + categories[i]);
			category.store();
		}
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
		addAttribute(COLUMN_CATEGORY,"Category",String.class,30);
		setAsPrimaryKey(COLUMN_CATEGORY, true);

		addAttribute(COLUMN_NAME, "Name of category", String.class, 255);
		addAttribute(COLUMN_LOCALIZED_KEY, "Localized key for category", String.class, 255);
	}
	
	//Setters
	public void setCategory(String category) {
		setColumn(COLUMN_CATEGORY, category.toUpperCase());
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setLocalizedKey(String key) {
		setColumn(COLUMN_LOCALIZED_KEY, key);
	}


	//Getters
	public String getCategory() {
		return getStringColumnValue(COLUMN_CATEGORY);
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public String getLocalizedKey() {
		return getStringColumnValue(COLUMN_LOCALIZED_KEY);
	}
	
	
	//Find methods
	public Collection ejbFindAllCategories() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		
		return idoFindPKsByQuery(query);
	}
	
	public String ejbFindByLocalizedKey(String key) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_LOCALIZED_KEY, key);
		
		return (String) idoFindOnePKByQuery(query);
	}

	public String ejbFindChildcareCategory() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_CATEGORY, CATEGORY_CHILD_CARE);
		
		return (String) idoFindOnePKByQuery(query);
	}

	public String ejbFindElementarySchoolCategory() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_CATEGORY, CATEGORY_ELEMENTARY_SCHOOL);
		
		return (String) idoFindOnePKByQuery(query);
	}
	
	public String ejbFindHighSchoolCategory() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_CATEGORY, CATEGORY_HIGH_SCHOOL);
		
		return (String) idoFindOnePKByQuery(query);
	}
	
	public String ejbFindCollegeCategory() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_CATEGORY, CATEGORY_COLLEGE);
		
		return (String) idoFindOnePKByQuery(query);
	}
	
	public String ejbFindUniversityCategory() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_CATEGORY, CATEGORY_UNIVERSITY);
		
		return (String) idoFindOnePKByQuery(query);
	}

	public String ejbFindMusicSchoolCategory() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_CATEGORY, CATEGORY_MUSIC_SCHOOL);
		
		return (String) idoFindOnePKByQuery(query);
	}
}