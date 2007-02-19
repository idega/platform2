package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega multimedia
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class ApartmentTypeBMPBean extends
		com.idega.block.text.data.TextEntityBMPBean implements ApartmentType {

	protected static final String ENTITY_NAME = "BU_APRT_TYPE";

	protected static final String COLUMN_FURNITURE = "furniture";

	protected static final String COLUMN_RENT = "rent";

	protected static final String COLUMN_LOFT = "loft";

	protected static final String COLUMN_STUDY = "study";

	protected static final String COLUMN_BALCONY = "balcony";

	protected static final String COLUMN_STORAGE = "storage";

	protected static final String COLUMN_BATHROOM = "bathroom";

	protected static final String COLUMN_KITCHEN = "kitchen";

	protected static final String COLUMN_AREA = "area";

	protected static final String COLUMN_ROOM_COUNT = "room_count";

	protected static final String COLUMN_PLAN = "plan_id";

	protected static final String COLUMN_IMAGE = "ic_image_id";

	protected static final String COLUMN_EXTRA_INFO = "extra_info";

	protected static final String COLUMN_INFO = "info";

	protected static final String COLUMN_NAME = "name";

	protected static final String COLUMN_ABBREVIATION = "ABBREV";

	protected static final String COLUMN_APARTMENT_CATEGORY = "BU_APRT_CAT_ID";

	protected static final String COLUMN_LOCKED = "locked";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_APARTMENT_CATEGORY,
				ApartmentCategory.class);
		addAttribute(COLUMN_NAME, "Name", String.class);
		addAttribute(COLUMN_INFO, "Info", String.class, 4000);
		addAttribute(COLUMN_ABBREVIATION, "Abbreviation", String.class, 10);
		addAttribute(COLUMN_EXTRA_INFO, "Extra_Info", String.class, 4000);
		addAttribute(COLUMN_IMAGE, "Photo", Integer.class);
		addAttribute(COLUMN_PLAN, "Plan", Integer.class);
		addAttribute(COLUMN_ROOM_COUNT, "Room Count", Integer.class);
		addAttribute(COLUMN_AREA, "Area", Double.class);
		addAttribute(COLUMN_KITCHEN, "Kitchen", Boolean.class);
		addAttribute(COLUMN_BATHROOM, "Bath", Boolean.class);
		addAttribute(COLUMN_STORAGE, "Storage", Boolean.class);
		addAttribute(COLUMN_BALCONY, "Balcony", Boolean.class);
		addAttribute(COLUMN_STUDY, "Study", Boolean.class);
		addAttribute(COLUMN_LOFT, "Loft", Boolean.class);
		addAttribute(COLUMN_RENT, "Rent", Integer.class);
		addAttribute(COLUMN_FURNITURE, "Furniture", Boolean.class);
		addAttribute(COLUMN_LOCKED, "Locked", Boolean.class);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	// getters
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public int getApartmentCategoryId() {
		return getIntColumnValue(COLUMN_APARTMENT_CATEGORY);
	}

	public ApartmentCategory getApartmentCategory() {
		return (ApartmentCategory) getColumnValue(COLUMN_APARTMENT_CATEGORY);
	}

	public String getInfo() {
		return getStringColumnValue(COLUMN_INFO);
	}

	public String getAbbreviation() {
		return getStringColumnValue(COLUMN_ABBREVIATION);
	}

	public String getExtraInfo() {
		return getStringColumnValue(COLUMN_EXTRA_INFO);
	}

	public int getImageId() {
		return getIntColumnValue(COLUMN_IMAGE);
	}

	public int getFloorPlanId() {
		return getIntColumnValue(COLUMN_PLAN);
	}

	public int getRoomCount() {
		return getIntColumnValue(COLUMN_ROOM_COUNT);
	}

	public double getArea() {
		return getDoubleColumnValue(COLUMN_AREA);
	}

	public int getRent() {
		return getIntColumnValue(COLUMN_RENT);
	}

	public boolean getKitchen() {
		return getBooleanColumnValue(COLUMN_KITCHEN);
	}

	public boolean getBathRoom() {
		return getBooleanColumnValue(COLUMN_BATHROOM);
	}

	public boolean getStorage() {
		return getBooleanColumnValue(COLUMN_STORAGE);
	}

	public boolean getBalcony() {
		return getBooleanColumnValue(COLUMN_BALCONY);
	}

	public boolean getStudy() {
		return getBooleanColumnValue(COLUMN_STUDY);
	}

	public boolean getLoft() {
		return getBooleanColumnValue(COLUMN_LOFT);
	}

	public boolean getFurniture() {
		return getBooleanColumnValue(COLUMN_FURNITURE);
	}

	public Collection getApartments() {
		try {
			return super.idoGetRelatedEntities(Apartment.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getApartments() : "
					+ e.getMessage());
		}
	}

	public boolean getLocked() {
		return getBooleanColumnValue(COLUMN_LOCKED, false);
	}

	// setters
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setApartmentCategoryId(int apartment_category_id) {
		setColumn(COLUMN_APARTMENT_CATEGORY, apartment_category_id);
	}

	public void setApartmentCategoryId(Integer apartment_category_id) {
		setColumn(COLUMN_APARTMENT_CATEGORY, apartment_category_id);
	}

	public void setInfo(String info) {
		setColumn(COLUMN_INFO, info);
	}

	public void setAbbreviation(String abbreviation) {
		setColumn(COLUMN_ABBREVIATION, abbreviation);
	}

	public void setExtraInfo(String info) {
		setColumn(COLUMN_EXTRA_INFO, info);
	}

	public void setImageId(int image_id) {
		setColumn(COLUMN_IMAGE, image_id);
	}

	public void setImageId(Integer image_id) {
		setColumn(COLUMN_IMAGE, image_id);
	}

	public void setFloorPlanId(int floorplan_id) {
		setColumn(COLUMN_PLAN, floorplan_id);
	}

	public void setFloorPlanId(Integer floorplan_id) {
		setColumn(COLUMN_PLAN, floorplan_id);
	}

	public void setRoomCount(int room_count) {
		setColumn(COLUMN_ROOM_COUNT, room_count);
	}

	public void setRoomCount(Integer room_count) {
		setColumn(COLUMN_ROOM_COUNT, room_count);
	}

	public void setArea(double area) {
		setColumn(COLUMN_AREA, area);
	}

	public void setArea(Double area) {
		setColumn(COLUMN_AREA, area);
	}

	public void setRent(int rent) {
		setColumn(COLUMN_RENT, rent);
	}

	public void setRent(Integer rent) {
		setColumn(COLUMN_RENT, rent);
	}

	public void setKitchen(boolean kitchen) {
		setColumn(COLUMN_KITCHEN, kitchen);
	}

	public void setBathRoom(boolean bathroom) {
		setColumn(COLUMN_BATHROOM, bathroom);
	}

	public void setStorage(boolean storage) {
		setColumn(COLUMN_STORAGE, storage);
	}

	public void setBalcony(boolean balcony) {
		setColumn(COLUMN_BALCONY, balcony);
	}

	public void setStudy(boolean study) {
		setColumn(COLUMN_STUDY, study);
	}

	public void setLoft(boolean loft) {
		setColumn(COLUMN_LOFT, loft);
	}

	public void setFurniture(boolean furniture) {
		setColumn(COLUMN_FURNITURE, furniture);
	}

	public void setLocked(boolean locked) {
		setColumn(COLUMN_LOCKED, locked);
	}

	// ejb
	public Collection ejbFindAll() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhere();
		query.appendLeftParenthesis();
		query.append(COLUMN_LOCKED);
		query.append(" is null");
		query.appendOr();
		query.appendEquals(COLUMN_LOCKED, false);
		query.appendRightParenthesis();
		query.appendOrderBy(COLUMN_NAME);

		return this.idoFindPKsByQuery(query);
	}

	public Collection ejbFindAllIncludingLocked() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindByBuilding(Integer buildingID)
			throws FinderException {
		try {
			Table type = new Table(this, "p");
			Table apartment = new Table(Apartment.class, "a");
			Table floor = new Table(Floor.class, "f");
			SelectQuery query = new SelectQuery(type);
			query.setAsDistinct(true);
			query.addColumn(new WildCardColumn(type));
			query.addJoin(type, apartment);
			query.addJoin(floor, apartment);
			query.addCriteria(new MatchCriteria(new Column(floor,
					FloorBMPBean.BU_BUILDING_ID), MatchCriteria.EQUALS,
					buildingID.intValue()));
			// query.addCriteria(new MatchCriteria(new Column(type,
			// COLUMN_LOCKED), MatchCriteria.NOTEQUALS,
			// true));
			query.addCriteria(new OR(new MatchCriteria(new Column(type,
					COLUMN_LOCKED), MatchCriteria.EQUALS, false),
					new MatchCriteria(type.getColumn(COLUMN_LOCKED))));
			query.addOrder(new Order(new Column(type, COLUMN_NAME), true));

			return idoFindPKsBySQL(query.toString());
		} catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}
	}

	public Collection ejbFindByCategory(Integer categoryID)
			throws FinderException {
		Table type = new Table(this);
		SelectQuery query = new SelectQuery(type);
		query.addColumn(new WildCardColumn(type));
		query.addCriteria(new MatchCriteria(type, COLUMN_APARTMENT_CATEGORY,
				MatchCriteria.EQUALS, categoryID.intValue()));
		// query.addCriteria(new MatchCriteria(new Column(type,
		// COLUMN_LOCKED), MatchCriteria.NOTEQUALS,
		// true));
		query.addCriteria(new OR(new MatchCriteria(new Column(type,
				COLUMN_LOCKED), MatchCriteria.EQUALS, false),
				new MatchCriteria(type.getColumn(COLUMN_LOCKED))));
		query.addOrder(new Order(new Column(type, COLUMN_NAME), true));

		return idoFindPKsBySQL(query.toString());
	}

	public Collection ejbFindByComplex(Integer complexID)
			throws FinderException {
		Table type = new Table(this, "p");
		Table apartment = new Table(Apartment.class, "a");
		Table floor = new Table(Floor.class, "f");
		Table building = new Table(Building.class, "b");
		SelectQuery query = new SelectQuery(type);
		query.setAsDistinct(true);
		query.addColumn(new WildCardColumn(type));
		try {
			query.addJoin(apartment, type);
			query.addJoin(apartment, floor);
			query.addJoin(floor, building);
		} catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}
		query.addCriteria(new MatchCriteria(new Column(building,
				BuildingBMPBean.COLUMN_COMPLEX), MatchCriteria.EQUALS,
				complexID.intValue()));
		// query.addCriteria(new MatchCriteria(new Column(type,
		// COLUMN_LOCKED), MatchCriteria.NOTEQUALS,
		// true));
		query.addCriteria(new OR(new MatchCriteria(new Column(type,
				COLUMN_LOCKED), MatchCriteria.EQUALS, false),
				new MatchCriteria(type.getColumn(COLUMN_LOCKED))));
		query.addOrder(new Order(new Column(type, COLUMN_NAME), true));

		System.out.println("sql = " + query.toString());

		return idoFindPKsBySQL(query.toString());
	}

	public Collection ejbFindFromSameComplex(ApartmentType thetype)
			throws FinderException {
		Table type = new Table(this);
		Table complextype = new Table(ComplexTypeView.class);
		SelectQuery query = new SelectQuery(type);
		query.addColumn(new WildCardColumn(type));
		query.addJoin(type, getIDColumnName(), complextype,
				ComplexTypeViewBMPBean.BU_APRT_TYPE_ID);
		query.addCriteria(new MatchCriteria(type, getIDColumnName(),
				MatchCriteria.EQUALS, ((Integer) thetype.getPrimaryKey())
						.intValue()));
		// query.addCriteria(new MatchCriteria(new Column(type,
		// COLUMN_LOCKED), MatchCriteria.NOTEQUALS,
		// true));
		query.addCriteria(new OR(new MatchCriteria(new Column(type,
				COLUMN_LOCKED), MatchCriteria.EQUALS, false),
				new MatchCriteria(type.getColumn(COLUMN_LOCKED))));
		query.addOrder(new Order(new Column(type, COLUMN_NAME), true));

		return idoFindPKsBySQL(query.toString());
	}
}