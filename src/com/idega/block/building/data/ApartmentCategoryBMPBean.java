/*
 * $Id: ApartmentCategoryBMPBean.java,v 1.8.2.4 2008/02/13 17:00:20 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.text.data.TextEntityBMPBean;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class ApartmentCategoryBMPBean extends TextEntityBMPBean implements
		ApartmentCategory {

	protected static final String ENTITY_NAME = "bu_aprt_cat";

	protected static final String COLUMN_IMAGE = "ic_image_id";
	
	protected static final String COLUMN_INFO = "info";
	
	protected static final String COLUMN_NAME = "name";
	
	protected static final String COLUMN_SHOW_SPOUSE = "show_spouse";

	protected static final String COLUMN_SPOUSE_MANDATORY = "spouse_mandatory";

	protected static final String COLUMN_SHOW_CHILDREN = "show_children";

	protected static final String COLUMN_CHILDREN_MANDATORY = "children_mandatory";
	
	protected static final String COLUMN_MAX_NUMBER_OF_CHOICES = "number_of_choices";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Name", String.class);
		addAttribute(COLUMN_INFO, "Info", String.class);
		addAttribute(COLUMN_IMAGE, "Icon", Integer.class);
		super.setMaxLength(COLUMN_INFO, 4000);
		addAttribute(COLUMN_SHOW_SPOUSE, "Show spouse", Boolean.class);
		addAttribute(COLUMN_SPOUSE_MANDATORY, "Spouse mandatory", Boolean.class);
		addAttribute(COLUMN_SHOW_CHILDREN, "Show children", Boolean.class);
		addAttribute(COLUMN_CHILDREN_MANDATORY, "Children mandatory", Boolean.class);
		addAttribute(COLUMN_MAX_NUMBER_OF_CHOICES, "Max number of choices", Integer.class);
	}

	//getters
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public String getInfo() {
		return getStringColumnValue(COLUMN_INFO);
	}

	public int getImageId() {
		return getIntColumnValue(COLUMN_IMAGE);
	}

	public boolean getShowSpouse() {
		return getBooleanColumnValue(COLUMN_SHOW_SPOUSE, true);
	}
	
	public boolean getSpouseMandatory() {
		return getBooleanColumnValue(COLUMN_SPOUSE_MANDATORY, false);
	}
	
	public boolean getShowChildren() {
		return getBooleanColumnValue(COLUMN_SHOW_CHILDREN, true);
	}
	
	public boolean getChildrenMandatory() {
		return getBooleanColumnValue(COLUMN_CHILDREN_MANDATORY, false);
	}
	
	public int getMaxNumberOfChoices() {
		return getIntColumnValue(COLUMN_MAX_NUMBER_OF_CHOICES, 3);
	}
	
	//setters	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setInfo(String info) {
		setColumn(COLUMN_INFO, info);
	}

	public void setImageId(int image_id) {
		setColumn(COLUMN_IMAGE, image_id);
	}

	public void setImageId(Integer image_id) {
		setColumn(COLUMN_IMAGE, image_id);
	}

	public void setShowSpouse(boolean showSpouse) {
		setColumn(COLUMN_SHOW_SPOUSE, showSpouse);
	}
	
	public void setSpouseMandatory(boolean spouseMandatory) {
		setColumn(COLUMN_SPOUSE_MANDATORY, spouseMandatory);
	}
	
	public void setShowChildren(boolean showChildren) {
		setColumn(COLUMN_SHOW_CHILDREN, showChildren);
	}
	
	public void setChildrenMandatory(boolean childrenMandatory) {
		setColumn(COLUMN_CHILDREN_MANDATORY, childrenMandatory);
	}
	
	public void setMaxNumberOfChoices(int maxNumberOfChoices) {
		setColumn(COLUMN_MAX_NUMBER_OF_CHOICES, maxNumberOfChoices);
	}
	
	//sql
	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection getApartmentTypes() {
		try {
			return super.idoGetRelatedEntities(ApartmentType.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getApartmentTypes() : "
					+ e.getMessage());
		}
	}

	public Collection ejbFindByComplex(Integer complexID)
			throws FinderException {
		Table category = new Table(this, "c");
		Table subcategory = new Table(ApartmentSubcategory.class, "s");
		Table type = new Table(ApartmentType.class, "t");
		Table apartment = new Table(Apartment.class, "a");
		Table floor = new Table(Floor.class, "f");
		Table building = new Table(Building.class, "b");
		SelectQuery query = new SelectQuery(category);
		query.setAsDistinct(true);
		query.addColumn(new WildCardColumn(category));
		try {
			query.addJoin(subcategory, category);
			query.addJoin(type, subcategory);
			query.addJoin(apartment, type);
			query.addJoin(apartment, floor);
			query.addJoin(floor, building);
		} catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}
		query.addCriteria(new MatchCriteria(new Column(building,
				BuildingBMPBean.COLUMN_COMPLEX), MatchCriteria.EQUALS,
				complexID.intValue()));
		query.addOrder(category, this.getIDColumnName(), true);
		return idoFindPKsBySQL(query.toString());
	}
}