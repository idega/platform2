package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class ApartmentTypeBMPBean
	extends com.idega.block.text.data.TextEntityBMPBean 
	implements ApartmentType {
	
	protected static final String FURNITURE = "furniture";
	protected static final String RENT = "rent";
	protected static final String LOFT = "loft";
	protected static final String STUDY = "study";
	protected static final String BALCONY = "balcony";
	protected static final String STORAGE = "storage";
	protected static final String BATHROOM = "bathroom";
	protected static final String KITCHEN = "kitchen";
	protected static final String AREA = "area";
	protected static final String ROOM_COUNT = "room_count";
	protected static final String PLAN_ID = "plan_id";
	protected static final String IC_IMAGE_ID = "ic_image_id";
	protected static final String EXTRA_INFO = "extra_info";
	protected static final String INFO = "info";
	protected static final String NAME = "name";
	protected static final String ABBREVIATION = "ABBREV";
	protected static final String BU_APRT_CAT_ID = "BU_APRT_CAT_ID";
	protected static final String BU_APRT_TYPE = "BU_APRT_TYPE";
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(
			BU_APRT_CAT_ID,
			"Category",
			true,
			true,
			java.lang.Integer.class,
			"many-to-one",
			ApartmentCategory.class);
		addAttribute(NAME, "Name", true, true, java.lang.String.class);
		addAttribute(INFO, "Info", true, true, java.lang.String.class, 4000);
		addAttribute(ABBREVIATION, "Abbreviation", true, true, java.lang.String.class, 10);
		addAttribute(EXTRA_INFO, "Extra_Info", true, true, java.lang.String.class, 4000);
		addAttribute(IC_IMAGE_ID, "Photo", true, true, java.lang.Integer.class);
		addAttribute(PLAN_ID, "Plan", true, true, java.lang.Integer.class);
		addAttribute(ROOM_COUNT, "Room Count", true, true, java.lang.Integer.class);
		addAttribute(AREA, "Area", true, true, java.lang.Float.class);
		addAttribute(KITCHEN, "Kitchen", true, true, java.lang.Boolean.class);
		addAttribute(BATHROOM, "Bath", true, true, java.lang.Boolean.class);
		addAttribute(STORAGE, "Storage", true, true, java.lang.Boolean.class);
		addAttribute(BALCONY, "Balcony", true, true, java.lang.Boolean.class);
		addAttribute(STUDY, "Study", true, true, java.lang.Boolean.class);
		addAttribute(LOFT, "Loft", true, true, java.lang.Boolean.class);
		addAttribute(RENT, "Rent", true, true, java.lang.Integer.class);
		addAttribute(FURNITURE, "Furniture", true, true, java.lang.Boolean.class);
		super.setMaxLength(INFO, 4000);
	}
	public String getEntityName() {
		return BU_APRT_TYPE;
	}
	
	public String getName() {
		return getStringColumnValue(NAME);
	}
	public void setName(String name) {
		setColumn(NAME, name);
	}
	public int getApartmentCategoryId() {
		return getIntColumnValue(BU_APRT_CAT_ID);
	}
	public ApartmentCategory getApartmentCategory(){
		return( ApartmentCategory ) getColumnValue(BU_APRT_CAT_ID);
	}
	public void setApartmentCategoryId(int apartment_category_id) {
		setColumn(BU_APRT_CAT_ID, apartment_category_id);
	}
	public void setApartmentCategoryId(Integer apartment_category_id) {
		setColumn(BU_APRT_CAT_ID, apartment_category_id);
	}
	public String getInfo() {
		return getStringColumnValue(INFO);
	}
	public void setInfo(String info) {
		setColumn(INFO, info);
	}
	public String getAbbreviation() {
		return getStringColumnValue(ABBREVIATION);
	}
	public void setAbbreviation(String abbreviation) {
		setColumn(ABBREVIATION, abbreviation);
	}
	public String getExtraInfo() {
		return getStringColumnValue(EXTRA_INFO);
	}
	public void setExtraInfo(String info) {
		setColumn(EXTRA_INFO, info);
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
	public int getFloorPlanId() {
		return getIntColumnValue(PLAN_ID);
	}
	public void setFloorPlanId(int floorplan_id) {
		setColumn(PLAN_ID, floorplan_id);
	}
	public void setFloorPlanId(Integer floorplan_id) {
		setColumn(PLAN_ID, floorplan_id);
	}
	public int getRoomCount() {
		return getIntColumnValue(ROOM_COUNT);
	}
	public void setRoomCount(int room_count) {
		setColumn(ROOM_COUNT, room_count);
	}
	public void setRoomCount(Integer room_count) {
		setColumn(ROOM_COUNT, room_count);
	}
	public float getArea() {
		return getFloatColumnValue(AREA);
	}
	public void setArea(float area) {
		setColumn(AREA, area);
	}
	public void setArea(Float area) {
		setColumn(AREA, area);
	}
	public int getRent() {
		return getIntColumnValue(RENT);
	}
	public void setRent(int rent) {
		setColumn(RENT, rent);
	}
	public void setRent(Integer rent) {
		setColumn(RENT, rent);
	}
	public void setKitchen(boolean kitchen) {
		setColumn(KITCHEN, kitchen);
	}
	public boolean getKitchen() {
		return getBooleanColumnValue(KITCHEN);
	}
	public void setBathRoom(boolean bathroom) {
		setColumn(BATHROOM, bathroom);
	}
	public boolean getBathRoom() {
		return getBooleanColumnValue(BATHROOM);
	}
	public void setStorage(boolean storage) {
		setColumn(STORAGE, storage);
	}
	public boolean getStorage() {
		return getBooleanColumnValue(STORAGE);
	}
	public void setBalcony(boolean balcony) {
		setColumn(BALCONY, balcony);
	}
	public boolean getBalcony() {
		return getBooleanColumnValue(BALCONY);
	}
	public void setStudy(boolean study) {
		setColumn(STUDY, study);
	}
	public boolean getStudy() {
		return getBooleanColumnValue(STUDY);
	}
	public void setLoft(boolean loft) {
		setColumn(LOFT, loft);
	}
	public boolean getLoft() {
		return getBooleanColumnValue(LOFT);
	}
	public boolean getFurniture() {
		return getBooleanColumnValue(FURNITURE);
	}
	public void setFurniture(boolean furniture) {
		setColumn(FURNITURE, furniture);
	}
	
	public Collection ejbFindAll()throws FinderException{
		return super.idoFindAllIDsBySQL();
	}
	
	public Collection ejbFindByBuilding(Integer buildingID) throws FinderException{
		/*
		 select distinct bu_aprt_type.*
		 from bu_aprt_type p,bu_apartment a,bu_floor f
		 where p.bu_aprt_type_id = a.bu_aprt_type_id
		 and a.bu_floor_id = f.bu_floor_id
		 and f.bu_building_id = 2

		 StringBuffer sql = new StringBuffer(" select distinct bu_aprt_type.* ");
		 sql.append(" from bu_aprt_type p,bu_apartment a,bu_floor f ");
		 sql.append(" where p.bu_aprt_type_id = a.bu_aprt_type_id");
		 sql.append(" and a.bu_floor_id = f.bu_floor_id");
		 sql.append(" and f.bu_building_id = ");
		 sql.append(iBuildingId);
		 */
		
		try {
			Table type = new Table(this,"p");
			Table apartment =new Table(Apartment.class,"a");
			Table floor =new Table(Floor.class,"f");
			SelectQuery query = new SelectQuery(type);
			query.setAsDistinct(true);
			query.addColumn(new WildCardColumn(type));
			query.addJoin(type,apartment);
			query.addJoin(floor,apartment);
			query.addCriteria(new MatchCriteria(new Column(floor,FloorBMPBean.BU_BUILDING_ID),MatchCriteria.EQUALS,buildingID.intValue() ));
			return idoFindPKsBySQL(query.toString());
		}
		catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}
		
	}
	
	public Collection ejbFindByCategory(Integer categoryID)throws FinderException{
		Table type =new Table(this);
		SelectQuery query =new SelectQuery(type);
		query.addColumn(new WildCardColumn(type));
		query.addCriteria(new MatchCriteria(type,BU_APRT_CAT_ID,MatchCriteria.EQUALS,categoryID.intValue()));
		return idoFindPKsBySQL(query.toString());
	}
	
	public Collection ejbFindByComplex(Integer complexID)throws FinderException{
		/*
		select distinct bu_aprt_type.*
		from bu_aprt_type p,bu_apartment a,bu_floor f, bu_building b
		where p.bu_aprt_type_id = a.bu_aprt_type_id
		and a.bu_floor_id = f.bu_floor_id
		and f.bu_building_id = b.building_id
		and b.bu_complex_id = 2
		*/
		Table type = new Table(this,"p");
		Table apartment =new Table(Apartment.class,"a");
		Table floor =new Table(Floor.class,"f");
		Table building =new Table(Building.class,"b");
		SelectQuery query = new SelectQuery(type);
		query.setAsDistinct(true);
		query.addColumn(new WildCardColumn(type));
		try {
			query.addJoin(apartment,type);
			query.addJoin(apartment,floor);
			query.addJoin(floor,building);
		}
		catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}
		query.addCriteria(new MatchCriteria(new Column(building,BuildingBMPBean.BU_COMPLEX_ID),MatchCriteria.EQUALS,complexID.intValue() ));
		return idoFindPKsBySQL(query.toString());
		
	}
	
	public Collection ejbFindFromSameComplex(ApartmentType thetype)throws FinderException{
		Table type =new Table(this);
		Table complextype = new Table(ComplexTypeView.class);
		SelectQuery query = new SelectQuery(type);
		query.addColumn(new WildCardColumn(type));
		query.addJoin(type,getIDColumnName(),complextype,ComplexTypeViewBMPBean.BU_APRT_TYPE_ID);
		query.addCriteria(new MatchCriteria(type,getIDColumnName(),MatchCriteria.EQUALS,((Integer)thetype.getPrimaryKey()).intValue()));
		return idoFindPKsBySQL(query.toString());
	}

	
	public Collection getApartments(){
		try {
			return super.idoGetRelatedEntities(Apartment.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getApartments() : " + e.getMessage());
		}
	}
}

