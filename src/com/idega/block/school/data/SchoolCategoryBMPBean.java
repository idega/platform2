/*
 * Created on 20.8.2003
 */
package com.idega.block.school.data;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;

/**
 * @author laddi
 */
public class SchoolCategoryBMPBean extends GenericEntity implements SchoolCategory {

	public static final String ENTITY_NAME = "sch_category";

	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_NAME = "category_name";
	public static final String COLUMN_LOCALIZED_KEY = "localized_key";
	
	/* (non-Javadoc)
	 * @see com.idega.data.IDOEntityBean#getPrimaryKeyClass()
	 */
	public Class getPrimaryKeyClass() {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#insertStartData()
	 */
	public void insertStartData() throws Exception {
		String[] categories = { "CHILD_CARE", "PRIMARY_SCHOOL", "HIGH_SCHOOL", "COLLEGE", "UNIVERSITY" };
		String[] names = { "Child care", "Primary school", "High school", "College", "University" };
		
		SchoolCategoryHome categoryHome = (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
		SchoolCategory category;
		
		for (int i = 0; i < categories.length; i++) {
			category = categoryHome.create();
			category.setCategory(categories[i]);
			category.setName(names[i]);
			category.setLocalizedKey("sch."+categories[i]);
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
		setColumn(COLUMN_CATEGORY, category);
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
}