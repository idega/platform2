package com.idega.block.building.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega multimedia
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 * 
 */
public class ApartmentBMPBean extends
		com.idega.block.text.data.TextEntityBMPBean implements
		com.idega.block.building.data.Apartment {

	private static final String ENTITY_NAME = "bu_apartment";

	private static final String COLUMN_STATUS = "status";

	private static final String COLUMN_SERIE = "serie";

	private static final String COLUMN_UNAVAILABLE_UNTIL = "unavailable_until";

	private static final String COLUMN_RENTABLE = "rentable";

	private static final String COLUMN_APARTMENT_TYPE = "BU_APRT_TYPE_ID";

	private static final String COLUMN_FLOOR = "bu_floor_id";

	private static final String COLUMN_INFO = "info";

	private static final String COLUMN_IMAGE = "ic_image_id";

	private static final String COLUMN_NAME = "name";

	private static final String COLUMN_SERIAL_NUMBER = "snr";
	
	private static final String COLUMN_MARKED = "marked";

	public final static String STATUS_AVAILABLE = "A";

	public final static String STATUS_RENTED = "R";

	public final static String STATUS_FROZEN = "F";
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Name", String.class);
		addAttribute(COLUMN_INFO, "Info", String.class, 5000);
		addManyToOneRelationship(COLUMN_FLOOR, Floor.class);
		addManyToOneRelationship(COLUMN_APARTMENT_TYPE, ApartmentType.class);
		addAttribute(COLUMN_RENTABLE, "Leigjanleg", Boolean.class);
		addAttribute(COLUMN_IMAGE, "Mynd", Integer.class);
		addAttribute(COLUMN_UNAVAILABLE_UNTIL, "Frosin", Date.class);
		addAttribute(COLUMN_SERIE, "Serie", String.class, 2);
		addAttribute(COLUMN_STATUS, "Status", String.class, 1);
		addAttribute(COLUMN_SERIAL_NUMBER, "Serial number", String.class, 255);
		addAttribute(COLUMN_MARKED, "marked", Boolean.class);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	//getters
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public String getInfo() {
		return getStringColumnValue(COLUMN_INFO);
	}

	public int getFloorId() {
		return getIntColumnValue(COLUMN_FLOOR);
	}

	public Floor getFloor() {
		return (Floor) getColumnValue(COLUMN_FLOOR);
	}

	public int getApartmentTypeId() {
		return getIntColumnValue(COLUMN_APARTMENT_TYPE);
	}

	public ApartmentType getApartmentType() {
		return (ApartmentType) getColumnValue(COLUMN_APARTMENT_TYPE);
	}

	public int getImageId() {
		return getIntColumnValue(COLUMN_IMAGE);
	}

	public boolean getRentable() {
		return getBooleanColumnValue(COLUMN_RENTABLE);
	}

	public Date getUnavailableUntil() {
		return ((Date) getColumnValue(COLUMN_UNAVAILABLE_UNTIL));
	}

	public String getSerie() {
		return getStringColumnValue(COLUMN_SERIE);
	}

	public String getStatus() {
		return (getStringColumnValue(COLUMN_STATUS));
	}

	public boolean getMarked() {
		return getBooleanColumnValue(COLUMN_MARKED, false);
	}

	public String getSerialNumber() {
		return getStringColumnValue(COLUMN_SERIAL_NUMBER);
	}

	//setters
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setInfo(String info) {
		setColumn(COLUMN_INFO, info);
	}

	public void setFloorId(int floor_id) {
		setColumn(COLUMN_FLOOR, floor_id);
	}

	public void setFloorId(Integer floor_id) {
		setColumn(COLUMN_FLOOR, floor_id);
	}
	
	public void setFloor(Floor floor) {
		setColumn(COLUMN_FLOOR, floor);
	}

	public void setApartmentTypeId(int apartment_type_id) {
		setColumn(COLUMN_APARTMENT_TYPE, apartment_type_id);
	}

	public void setApartmentTypeId(Integer apartment_type_id) {
		setColumn(COLUMN_APARTMENT_TYPE, apartment_type_id);
	}

	public void setApartmentType(ApartmentType type) {
		setColumn(COLUMN_APARTMENT_TYPE, type);
	}
	
	public void setImageId(int room_type_id) {
		setColumn(COLUMN_IMAGE, room_type_id);
	}

	public void setImageId(Integer room_type_id) {
		setColumn(COLUMN_IMAGE, room_type_id);
	}

	public void setRentable(boolean rentable) {
		setColumn(COLUMN_RENTABLE, rentable);
	}

	public void setUnavailableUntil(Date date) {
		setColumn(COLUMN_UNAVAILABLE_UNTIL, date);
	}

	public void setSerie(String serie) {
		setColumn(COLUMN_SERIE, serie);
	}

	public void setStatus(String status) {
		if (status.equalsIgnoreCase(STATUS_FROZEN) || status.equalsIgnoreCase(STATUS_RENTED)
				|| status.equalsIgnoreCase(STATUS_AVAILABLE)) {
			setColumn(COLUMN_STATUS, status);
		}
		else {
			System.err.println("Undefined status :" + status);
		}
	}

	public void setStatusFrozen() {
		setStatus(STATUS_FROZEN);
	}

	public void setStatusAvailable() {
		setStatus(STATUS_AVAILABLE);
	}

	public void setStatusRented() {
		setStatus(STATUS_RENTED);
	}

	public void setSerialNumber(String number) {
		setColumn(COLUMN_SERIAL_NUMBER, number);
	}

	public void setMarked(boolean marked) {
		setColumn(COLUMN_MARKED, marked);
	}
	
	//ejb
	public Collection ejbFindBySQL(String sql) throws FinderException {
		return super.idoFindPKsBySQL(sql);
	}

	public Collection ejbFindByType(Integer typeID) throws FinderException {
		Table apartment = new Table(this);
		SelectQuery query = new SelectQuery(apartment);
		query.addCriteria(new MatchCriteria(apartment, COLUMN_APARTMENT_TYPE,
				MatchCriteria.EQUALS, typeID.intValue()));
		return idoFindPKsBySQL(query.toString());
	}

	public Collection ejbFindeByTypeAndComplex(Integer typeID, Integer complexID)
			throws FinderException {

		try {
			SelectQuery query = getTypeAndComplexQuery(typeID, complexID);
			query.addOrder(new Order(new Column(COLUMN_FLOOR), true));
			return idoFindPKsBySQL(query.toString());
		} catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}

	}

	public Collection ejbFindByName(String name) throws FinderException {
		Table apartment = new Table(this);
		SelectQuery query = new SelectQuery(apartment);
		query.addCriteria(new MatchCriteria(apartment, COLUMN_NAME,
				MatchCriteria.EQUALS, name));
		return idoFindPKsBySQL(query.toString());
	}

	public int ejbHomeGetRentableCount() throws IDOException {
		Table apartment = new Table(this);

		SelectQuery query = new SelectQuery(apartment);
		query.setAsCountQuery(true);
		query.addColumn(new WildCardColumn());

		query.addCriteria(new MatchCriteria(apartment, COLUMN_RENTABLE,
				MatchCriteria.EQUALS, true));
		return idoGetNumberOfRecords(query.toString());
	}

	public int ejbHomeGetTypeAndComplexCount(Integer typeID, Integer complexID)
			throws IDOException {
		SelectQuery query = getTypeAndComplexQuery(typeID, complexID);
		return idoGetNumberOfRecords(query.toString());
	}

	private SelectQuery getTypeAndComplexQuery(Integer typeID, Integer complexID)
			throws IDORelationshipException {
		Table apartment = new Table(this);
		Table type = new Table(ApartmentType.class);
		Table floor = new Table(Floor.class);
		Table building = new Table(Building.class);
		SelectQuery query = new SelectQuery(apartment);
		query.addColumn(new WildCardColumn(apartment));

		query.addJoin(type, apartment);
		query.addJoin(floor, apartment);
		query.addJoin(building, floor);

		query.addCriteria(new MatchCriteria(apartment, COLUMN_APARTMENT_TYPE,
				MatchCriteria.EQUALS, typeID.intValue()));
		query.addCriteria(new MatchCriteria(new Column(building,
				BuildingBMPBean.COLUMN_COMPLEX), MatchCriteria.EQUALS,
				complexID.intValue()));
		return query;
	}

	public Collection ejbFindBySearch(Integer complexID, Integer buildingID,
			Integer floorID, Integer typeID, Integer subcategoryID, boolean order)
			throws FinderException {

		Table apartment = new Table(this, "a");
		Table floor = new Table(Floor.class, "f");
		Table building = new Table(Building.class, "b");
		Table complex = new Table(Complex.class, "c");
		Table category = new Table(ApartmentCategory.class, "y");
		Table subcategory = new Table(ApartmentSubcategory.class, "y");
		Table type = new Table(ApartmentType.class, "t");

		SelectQuery query = new SelectQuery(apartment);
		query.addColumn(new WildCardColumn(apartment));
		query.addJoin(building, BuildingBMPBean.COLUMN_COMPLEX, complex,
				BuildingBMPBean.COLUMN_COMPLEX);
		query.addJoin(floor, FloorBMPBean.COLUMN_BUILDING, building,
				FloorBMPBean.COLUMN_BUILDING);
		query.addJoin(apartment, COLUMN_FLOOR, floor, COLUMN_FLOOR);
		query.addJoin(apartment, COLUMN_APARTMENT_TYPE, type, COLUMN_APARTMENT_TYPE);
		query.addJoin(type, ApartmentTypeBMPBean.COLUMN_APARTMENT_SUBCATEGORY, subcategory,
				ApartmentTypeBMPBean.COLUMN_APARTMENT_SUBCATEGORY);

		if (complexID != null && complexID.intValue() > 0) {
			query.addCriteria(new MatchCriteria(building,
					BuildingBMPBean.COLUMN_COMPLEX, MatchCriteria.EQUALS,
					complexID.intValue()));
			if (order) {
				query.addOrder(complex, ComplexBMPBean.COLUMN_NAME, true);
			}

		}
		if (buildingID != null && buildingID.intValue() > 0) {
			query.addCriteria(new MatchCriteria(floor,
					FloorBMPBean.COLUMN_BUILDING, MatchCriteria.EQUALS,
					buildingID.intValue()));
			if (order) {
				query.addOrder(building, BuildingBMPBean.COLUMN_NAME, true);
			}
		}
		if (floorID != null && floorID.intValue() > 0) {
			query.addCriteria(new MatchCriteria(apartment,
					ApartmentBMPBean.COLUMN_FLOOR, MatchCriteria.EQUALS, floorID
							.intValue()));
			if (order) {
				query.addOrder(floor, FloorBMPBean.COLUMN_NAME, true);
			}
		}
		if (typeID != null && typeID.intValue() > 0) {
			query.addCriteria(new MatchCriteria(apartment,
					ApartmentBMPBean.COLUMN_APARTMENT_TYPE, MatchCriteria.EQUALS,
					typeID.intValue()));
			if (order) {
				query.addOrder(type, ApartmentTypeBMPBean.COLUMN_NAME, true);
			}
		}
		if (subcategoryID != null && subcategoryID.intValue() > 0) {
			query.addCriteria(new MatchCriteria(type,
					ApartmentTypeBMPBean.COLUMN_APARTMENT_SUBCATEGORY, MatchCriteria.EQUALS,
					subcategoryID.intValue()));
			if (order) {
				query.addOrder(category, ApartmentCategoryBMPBean.COLUMN_NAME, true);
			}
		}
		return idoFindPKsBySQL(query.toString());
	}

	public Collection ejbFindByFloor(Integer floorID) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_FLOOR, floorID);
		query.appendOrderBy(COLUMN_NAME);

		return idoFindPKsByQuery(query);
	}

	public Object ejbFindByFloorAndTypeAndName(String name, Floor floor, ApartmentType type) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_FLOOR, floor);
		query.appendAndEquals(COLUMN_APARTMENT_TYPE, type);
		query.appendAndEqualsQuoted(COLUMN_NAME, name);
		
		return idoFindOnePKByQuery(query);
	}
	
	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindByFloor(Floor floor) throws FinderException {
		return ejbFindByFloor((Integer) floor.getPrimaryKey());
	}

}
