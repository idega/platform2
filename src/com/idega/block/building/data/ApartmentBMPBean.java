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

	private static final String STATUS = "status";

	private static final String SERIE = "serie";

	private static final String UNAVAILABLE_UNTIL = "unavailable_until";

	private static final String RENTABLE = "rentable";

	private static final String BU_APRT_TYPE_ID = "BU_APRT_TYPE_ID";

	private static final String BU_FLOOR_ID = "bu_floor_id";

	private static final String INFO = "info";

	private static final String IC_IMAGE_ID = "ic_image_id";

	private static final String NAME = "name";

	private static final String BU_APARTMENT = "bu_apartment";

	private static final String APARTMENT_SERIAL_NUMBER = "snr";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(NAME, "Name", true, true, java.lang.String.class);
		addAttribute(INFO, "Info", true, true, java.lang.String.class);
		addAttribute(BU_FLOOR_ID, "Floor", true, true, java.lang.Integer.class,
				"many-to-one", Floor.class);
		addAttribute(BU_APRT_TYPE_ID, "ApartmentType", true, true,
				java.lang.Integer.class, "many-to-one", ApartmentType.class);
		addAttribute(RENTABLE, "Leigjanleg", true, true,
				java.lang.Boolean.class);
		addAttribute(IC_IMAGE_ID, "Mynd", true, true, java.lang.Integer.class);
		addAttribute(UNAVAILABLE_UNTIL, "Frosin", true, true,
				java.sql.Date.class);
		addAttribute(SERIE, "Serie", true, true, java.lang.String.class, 2);
		addAttribute(STATUS, "Status", true, true, java.lang.String.class);
		setMaxLength(INFO, 5000);
		setMaxLength(STATUS, 1);
		addAttribute(APARTMENT_SERIAL_NUMBER, "Serial number", String.class, 255);
	}

	public String getEntityName() {
		return BU_APARTMENT;
	}

	public final static String AVAILABLE = "A";

	public final static String RENTED = "R";

	public final static String FROZEN = "F";

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

	public int getFloorId() {
		return getIntColumnValue(BU_FLOOR_ID);
	}

	public Floor getFloor() {
		return (Floor) getColumnValue(BU_FLOOR_ID);
	}

	public void setFloorId(int floor_id) {
		setColumn(BU_FLOOR_ID, floor_id);
	}

	public void setFloorId(Integer floor_id) {
		setColumn(BU_FLOOR_ID, floor_id);
	}

	public int getApartmentTypeId() {
		return getIntColumnValue(BU_APRT_TYPE_ID);
	}

	public ApartmentType getApartmentType() {
		return (ApartmentType) getColumnValue(BU_APRT_TYPE_ID);
	}

	public void setApartmentTypeId(int apartment_type_id) {
		setColumn(BU_APRT_TYPE_ID, apartment_type_id);
	}

	public void setApartmentTypeId(Integer apartment_type_id) {
		setColumn(BU_APRT_TYPE_ID, apartment_type_id);
	}

	public int getImageId() {
		return getIntColumnValue(IC_IMAGE_ID);
	}

	public void setImageId(int room_type_id) {
		setColumn(IC_IMAGE_ID, room_type_id);
	}

	public void setImageId(Integer room_type_id) {
		setColumn(IC_IMAGE_ID, room_type_id);
	}

	public boolean getRentable() {
		return getBooleanColumnValue(RENTABLE);
	}

	public Date getUnavailableUntil() {
		return ((Date) getColumnValue(UNAVAILABLE_UNTIL));
	}

	public void setRentable(boolean rentable) {
		setColumn(RENTABLE, rentable);
	}

	public void setUnavailableUntil(Date date) {
		setColumn(UNAVAILABLE_UNTIL, date);
	}

	public String getSerie() {
		return getStringColumnValue(SERIE);
	}

	public void setSerie(String serie) {
		setColumn(SERIE, serie);
	}

	public String getStatus() {
		return (getStringColumnValue(STATUS));
	}

	public void setStatus(String status) {
		if (status.equalsIgnoreCase(FROZEN) || status.equalsIgnoreCase(RENTED)
				|| status.equalsIgnoreCase(AVAILABLE)) {
			setColumn(STATUS, status);
		}
		else {
			System.err.println("Undefined status :" + status);
		}
	}

	public void setStatusFrozen() {
		setStatus(FROZEN);
	}

	public void setStatusAvailable() {
		setStatus(AVAILABLE);
	}

	public void setStatusRented() {
		setStatus(RENTED);
	}

	public void setSerialNumber(String number) {
		setColumn(APARTMENT_SERIAL_NUMBER, number);
	}

	public String getSerialNumber() {
		return getStringColumnValue(APARTMENT_SERIAL_NUMBER);
	}

	public Collection ejbFindBySQL(String sql) throws FinderException {
		return super.idoFindPKsBySQL(sql);
	}

	public Collection ejbFindByType(Integer typeID) throws FinderException {
		Table apartment = new Table(this);
		SelectQuery query = new SelectQuery(apartment);
		query.addCriteria(new MatchCriteria(apartment, BU_APRT_TYPE_ID,
				MatchCriteria.EQUALS, typeID.intValue()));
		return idoFindPKsBySQL(query.toString());
	}

	public Collection ejbFindeByTypeAndComplex(Integer typeID, Integer complexID)
			throws FinderException {

		try {
			SelectQuery query = getTypeAndComplexQuery(typeID, complexID);
			query.addOrder(new Order(new Column(BU_FLOOR_ID), true));
			return idoFindPKsBySQL(query.toString());
		} catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}

	}

	public Collection ejbFindByName(String name) throws FinderException {
		Table apartment = new Table(this);
		SelectQuery query = new SelectQuery(apartment);
		query.addCriteria(new MatchCriteria(apartment, NAME,
				MatchCriteria.EQUALS, name));
		return idoFindPKsBySQL(query.toString());
	}

	public int ejbHomeGetRentableCount() throws IDOException {
		Table apartment = new Table(this);

		SelectQuery query = new SelectQuery(apartment);
		query.setAsCountQuery(true);
		query.addColumn(new WildCardColumn());

		query.addCriteria(new MatchCriteria(apartment, RENTABLE,
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

		query.addCriteria(new MatchCriteria(apartment, BU_APRT_TYPE_ID,
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
		query.addJoin(floor, FloorBMPBean.BU_BUILDING_ID, building,
				FloorBMPBean.BU_BUILDING_ID);
		query.addJoin(apartment, BU_FLOOR_ID, floor, BU_FLOOR_ID);
		query.addJoin(apartment, BU_APRT_TYPE_ID, type, BU_APRT_TYPE_ID);
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
					FloorBMPBean.BU_BUILDING_ID, MatchCriteria.EQUALS,
					buildingID.intValue()));
			if (order) {
				query.addOrder(building, BuildingBMPBean.COLUMN_NAME, true);
			}
		}
		if (floorID != null && floorID.intValue() > 0) {
			query.addCriteria(new MatchCriteria(apartment,
					ApartmentBMPBean.BU_FLOOR_ID, MatchCriteria.EQUALS, floorID
							.intValue()));
			if (order) {
				query.addOrder(floor, FloorBMPBean.NAME, true);
			}
		}
		if (typeID != null && typeID.intValue() > 0) {
			query.addCriteria(new MatchCriteria(apartment,
					ApartmentBMPBean.BU_APRT_TYPE_ID, MatchCriteria.EQUALS,
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
		query.appendWhereEquals(BU_FLOOR_ID, floorID);
		query.appendOrderBy(NAME);

		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindByFloor(Floor floor) throws FinderException {
		return ejbFindByFloor((Integer) floor.getPrimaryKey());
	}

}
