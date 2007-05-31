package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.text.data.TextEntityBMPBean;
import com.idega.data.query.Column;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

public class ApartmentSubcategoryBMPBean extends TextEntityBMPBean implements
		ApartmentSubcategory {

	protected static final String ENTITY_NAME = "bu_aprt_sub_cat";

	protected static final String COLUMN_NAME = "name";

	protected static final String COLUMN_IMAGE = "image";

	protected static final String COLUMN_INFO = "info";

	protected static final String COLUMN_APARTMENT_CATEGORY = "aprt_cat";

	protected static final String COLUMN_LOCKED = "locked";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Name", String.class);
		addAttribute(COLUMN_INFO, "Info", String.class, 4000);
		addAttribute(COLUMN_IMAGE, "Icon", Integer.class);
		addManyToOneRelationship(COLUMN_APARTMENT_CATEGORY,
				ApartmentCategory.class);
		addAttribute(COLUMN_LOCKED, "locked", Boolean.class);
	}

	// getters
	public String getInfo() {
		return getStringColumnValue(COLUMN_INFO);
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public int getImage() {
		return getIntColumnValue(COLUMN_IMAGE);
	}

	public ApartmentCategory getApartmentCategory() {
		return (ApartmentCategory) getColumnValue(COLUMN_APARTMENT_CATEGORY);
	}

	// setters
	public void setInfo(String info) {
		setColumn(COLUMN_INFO, info);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setImage(int imageID) {
		setColumn(COLUMN_IMAGE, imageID);
	}

	public void setImage(Integer imageID) {
		setColumn(COLUMN_IMAGE, imageID);
	}

	public void setApartmentCategory(ApartmentCategory aprtSubcat) {
		setColumn(COLUMN_APARTMENT_CATEGORY, aprtSubcat);
	}

	public void setApartmentCategoryID(Integer aprtSubcat) {
		setColumn(COLUMN_APARTMENT_CATEGORY, aprtSubcat);
	}

	// sql
	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindByCategory(Integer categoryID)
			throws FinderException {
		Table subcategory = new Table(this);
		SelectQuery query = new SelectQuery(subcategory);
		query.addColumn(new WildCardColumn(subcategory));
		query.addCriteria(new MatchCriteria(new Column(subcategory,
				COLUMN_APARTMENT_CATEGORY), MatchCriteria.EQUALS, categoryID
				.intValue()));

		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindByCategory(Integer categoryID[])
			throws FinderException {
		Table subcategory = new Table(this);
		SelectQuery query = new SelectQuery(subcategory);
		query.addColumn(new WildCardColumn(subcategory));
		query.addCriteria(new InCriteria(new Column(subcategory,
				COLUMN_APARTMENT_CATEGORY), categoryID));

		System.out.println("sql = " + query.toString());
		
		return idoFindPKsByQuery(query);
	}

}