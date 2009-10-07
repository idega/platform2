package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;

/**
 * 
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega multimedia
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class FloorBMPBean extends com.idega.block.text.data.TextEntityBMPBean
		implements com.idega.block.building.data.Floor {

	protected static final String ENTITY_NAME = "bu_floor";

	protected static final String COLUMN_IMAGE = "ic_image_id";

	protected static final String COLUMN_NAME = "name";

	protected static final String COLUMN_INFO = "info";

	protected static final String COLUMN_BUILDING = "bu_building_id";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Name", true, true, java.lang.String.class);
		addAttribute(COLUMN_INFO, "Info", true, true, java.lang.String.class, 4000);
		addManyToOneRelationship(COLUMN_BUILDING, Building.class);
		addAttribute(COLUMN_IMAGE, "Plan", true, true, java.lang.Integer.class);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public String getInfo() {
		return getStringColumnValue(COLUMN_INFO);
	}

	public void setInfo(String info) {
		setColumn(COLUMN_INFO, info);
	}

	public int getBuildingId() {
		return getIntColumnValue(COLUMN_BUILDING);
	}

	public Building getBuilding() {
		return (Building) getColumnValue(COLUMN_BUILDING);
	}

	public void setBuildingId(int building_id) {
		setColumn(COLUMN_BUILDING, building_id);
	}

	public void setBuildingId(Integer building_id) {
		setColumn(COLUMN_BUILDING, building_id);
	}
	
	public void setBuilding(Building building) {
		setColumn(COLUMN_BUILDING, building);
	}

	public int getImageId() {
		return getIntColumnValue(COLUMN_IMAGE);
	}

	public void setImageId(int image_id) {
		setColumn(COLUMN_IMAGE, image_id);
	}

	public void setImageId(Integer image_id) {
		setColumn(COLUMN_IMAGE, image_id);
	}

	public Collection ejbFindAll() throws FinderException {
		return idoFindAllIDsBySQL();
	}

	public Collection ejbFindByBuilding(Integer buildingID)
			throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_BUILDING, buildingID);
		query.appendOrderBy(COLUMN_NAME);

		return idoFindPKsByQuery(query);
	}

	public Object ejbFindByBuildingAndName(String name, Building building)
			throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_BUILDING, building);
		query.appendAndEqualsQuoted(COLUMN_NAME, name);

		return this.idoFindOnePKByQuery(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.block.building.data.Floor#getApartments()
	 */
	public Collection getApartments() {
		try {
			return idoGetRelatedEntities(Apartment.class);
		} catch (IDORelationshipException e) {

		}
		return null;
	}
}
