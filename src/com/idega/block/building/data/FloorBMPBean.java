package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;

/**

 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class FloorBMPBean extends com.idega.block.text.data.TextEntityBMPBean  implements com.idega.block.building.data.Floor {
	
	protected static final String IC_IMAGE_ID = "ic_image_id";
	protected static final String BU_FLOOR = "bu_floor";
	protected static final String NAME = "name";
	protected static final String INFO = "info";
	protected static final String BU_BUILDING_ID = "bu_building_id";
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(NAME, "Name", true, true, java.lang.String.class);
		addAttribute(INFO, "Info", true, true, java.lang.String.class, 4000);
		addAttribute(	BU_BUILDING_ID,	"Building",	true,true,	java.lang.Integer.class,	"many-to-one",Building.class);
		addAttribute(IC_IMAGE_ID, "Plan", true, true, java.lang.Integer.class);
	}
	public String getEntityName() {
		return BU_FLOOR;
	}
	
	public String getName() {
		return getStringColumnValue(NAME);
	}
	public void setName(String name) {
		setColumn(NAME, name);
	}
	public String getInfo() {
		return getStringColumnValue(INFO);
	}
	public void setInfo(String info) {
		setColumn(INFO, info);
	}
	public int getBuildingId() {
		return getIntColumnValue(BU_BUILDING_ID);
	}
	public Building getBuilding(){
		return (Building) getColumnValue(BU_BUILDING_ID);
	}
	public void setBuildingId(int building_id) {
		setColumn(BU_BUILDING_ID, building_id);
	}
	public void setBuildingId(Integer building_id) {
		setColumn(BU_BUILDING_ID, building_id);
	}
	public int getImageId() {
		return getIntColumnValue(IC_IMAGE_ID);
	}
	public void setImageId(int image_id) {
		setColumn(IC_IMAGE_ID, image_id);
	}
	public void setImageId(Integer image_id) {
		setColumn(IC_IMAGE_ID, image_id);
	}
	
	public Collection ejbFindAll()throws FinderException{
		return idoFindAllIDsBySQL();
	}
	
	public Collection ejbFindByBuilding(Integer buildingID)throws FinderException{
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(BU_BUILDING_ID, buildingID);
		query.appendOrderBy(NAME);
		
		return idoFindPKsByQuery(query);	
	}
	
	
	/* (non-Javadoc)
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
