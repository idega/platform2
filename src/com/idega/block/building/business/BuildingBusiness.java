package com.idega.block.building.business;

import java.sql.SQLException;
import java.util.Locale;

import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.block.building.data.ApartmentCategoryHome;
import com.idega.block.building.data.ApartmentHome;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.BuildingHome;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.ComplexHome;
import com.idega.block.building.data.Floor;
import com.idega.block.building.data.FloorHome;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.TextEntity;
import com.idega.data.IDOLookup;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class BuildingBusiness {

	private BuildingBusiness() {
	}
	public static BuildingBusiness bb = null;

	public static BuildingBusiness getStaticInstance() {

		if (bb == null)
			bb = new BuildingBusiness();
		return bb;
	}

	public boolean saveComplex(int id, String sName, String sInfo, int imageid, int textid) {
		Complex eComplex = ((ComplexHome) IDOLookup.getHomeLegacy(Complex.class)).createLegacy();
		try {
			if (id > 0) {
				eComplex = ((ComplexHome) IDOLookup.getHomeLegacy(Complex.class)).findByPrimaryKeyLegacy(id);
			}
			eComplex.setName(sName);
			eComplex.setInfo(sInfo);
			if (imageid > 0)
				eComplex.setImageId(imageid);
			eComplex.setTextId(textid);
			eComplex.store();
			BuildingCacher.reload();
			return true;

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean saveBuilding(int id, String sName, String sAddress, String sInfo, int imageid, int complexid, String sSerie, int textId) {
		Building ebuilding = ((BuildingHome) IDOLookup.getHomeLegacy(Building.class)).createLegacy();
		try {
			if (complexid > 0) {
				if (id > 0) {
					ebuilding = ((BuildingHome) IDOLookup.getHomeLegacy(Building.class)).findByPrimaryKeyLegacy(id);
				}
				ebuilding.setName(sName);
				ebuilding.setStreet(sAddress);
				ebuilding.setInfo(sInfo);
				if (imageid > 0)
					ebuilding.setImageId(imageid);
				ebuilding.setComplexId(complexid);
				ebuilding.setSerie(sSerie);
				ebuilding.setTextId(textId);
				ebuilding.store();
				BuildingCacher.reload();
				return true;
			}
		}
		catch (SQLException e) {
		}
		return false;
	}
	public boolean saveFloor(int id, String sName, int buildingid, String sInfo, int imageid, int textid) {
		Floor efloor = ((FloorHome) IDOLookup.getHomeLegacy(Floor.class)).createLegacy();
		try {
			if (id > 0) {
				efloor = ((FloorHome) IDOLookup.getHomeLegacy(Floor.class)).findByPrimaryKeyLegacy(id);
			}
			efloor.setName(sName);
			efloor.setBuildingId(buildingid);
			efloor.setInfo(sInfo);
			if (imageid > 0)
				efloor.setImageId(imageid);
			efloor.setTextId(textid);
			efloor.store();
			BuildingCacher.reload();
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean saveApartmentCategory(int id, String sName, String sInfo, int imageid, int textid) {
		try {
			ApartmentCategory eACategory = ((ApartmentCategoryHome) IDOLookup.getHomeLegacy(ApartmentCategory.class)).createLegacy();
			if (id > 0) {
				eACategory = ((ApartmentCategoryHome) IDOLookup.getHomeLegacy(ApartmentCategory.class)).findByPrimaryKeyLegacy(id);
			}
			eACategory.setName(sName);
			eACategory.setInfo(sInfo);
			if (imageid > 0)
				eACategory.setImageId(imageid);
			eACategory.setTextId(textid);
			eACategory.store();
			BuildingCacher.reload();
			return true;
		}
		catch (SQLException e) {
		}
		return false;
	}

	public boolean saveApartmentType(int id, String sName, String sInfo, String sExtraInfo, int planid, int imageid, int categoryid, int textid, float area, int roomcount, int rent, boolean balcony, boolean bath, boolean kitchen, boolean storage, boolean study, boolean furniture, boolean loft) {
		try {
			if (categoryid > 0) {
				ApartmentType etype = ((com.idega.block.building.data.ApartmentTypeHome) com.idega.data.IDOLookup.getHomeLegacy(ApartmentType.class)).createLegacy();
				if (id > 0) {
					etype = ((com.idega.block.building.data.ApartmentTypeHome) com.idega.data.IDOLookup.getHomeLegacy(ApartmentType.class)).findByPrimaryKeyLegacy(id);
				}
				etype.setName(sName);
				etype.setInfo(sInfo);
				etype.setExtraInfo(sExtraInfo);
				if (planid > 0)
					etype.setFloorPlanId(planid);
				if (imageid > 0)
					etype.setImageId(imageid);
				etype.setTextId(textid);
				etype.setApartmentCategoryId(categoryid);
				etype.setArea(area);
				etype.setRoomCount(roomcount);
				etype.setRent(rent);

				etype.setBalcony(balcony);
				etype.setBathRoom(bath);
				etype.setKitchen(kitchen);
				etype.setLoft(loft);
				etype.setStorage(storage);
				etype.setStudy(study);
				etype.setFurniture(furniture);

				etype.store();

				BuildingCacher.reload();
				return true;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean saveApartment(int id, String sName, String sInfo, int floorid, int typeid, boolean rentable, int imageid, String sSerie, int textid) {
		try {
			Apartment apartment = ((ApartmentHome) IDOLookup.getHomeLegacy(Apartment.class)).createLegacy();
			if (id > 0) {
				apartment = ((ApartmentHome) IDOLookup.getHomeLegacy(Apartment.class)).findByPrimaryKeyLegacy(id);
			}
			apartment.setName(sName);
			apartment.setFloorId(floorid);
			apartment.setApartmentTypeId(typeid);
			apartment.setInfo(sInfo);
			apartment.setRentable(rentable);
			if (imageid > 0)
				apartment.setImageId(imageid);
			apartment.setSerie(sSerie);
			apartment.setTextId(textid);

			apartment.store();
			BuildingCacher.reload();
			return true;
		}
		catch (SQLException e) {
		}
		return false;
	}

	public void deleteComplex(int id) {
		try {
			((com.idega.block.building.data.ComplexHome) com.idega.data.IDOLookup.getHomeLegacy(Complex.class)).findByPrimaryKeyLegacy(id).delete();
			BuildingCacher.reload();
		}
		catch (SQLException sql) {
		}
	}
	public void deleteBuilding(int id) {
		try {
			((com.idega.block.building.data.BuildingHome) com.idega.data.IDOLookup.getHomeLegacy(Building.class)).findByPrimaryKeyLegacy(id).delete();
			BuildingCacher.reload();
		}
		catch (SQLException ex) {
		}
	}
	public void deleteFloor(int id) {
		try {
			((com.idega.block.building.data.FloorHome) com.idega.data.IDOLookup.getHomeLegacy(Floor.class)).findByPrimaryKeyLegacy(id).delete();
			BuildingCacher.reload();
		}
		catch (SQLException ex) {

		}
	}
	public void deleteApartment(int id) {
		try {
			((com.idega.block.building.data.ApartmentHome) com.idega.data.IDOLookup.getHomeLegacy(Apartment.class)).findByPrimaryKeyLegacy(id).delete();
			BuildingCacher.reload();
		}
		catch (SQLException ex) {
		}
	}
	public void deleteApartmentCategory(int id) {
		try {
			((com.idega.block.building.data.ApartmentCategoryHome) com.idega.data.IDOLookup.getHomeLegacy(ApartmentCategory.class)).findByPrimaryKeyLegacy(id).delete();
			BuildingCacher.reload();
		}
		catch (SQLException ex) {
		}
	}
	public void deleteApartmentType(int id) {
		try {
			((com.idega.block.building.data.ApartmentTypeHome) com.idega.data.IDOLookup.getHomeLegacy(ApartmentType.class)).findByPrimaryKeyLegacy(id).delete();
			BuildingCacher.reload();
		}
		catch (SQLException ex) {

		}
	}

	public void changeNameAndInfo(TextEntity entity, Locale locale) {
		String infoText = "", nameText = "";
		if (entity.getTextId() > 0) {
			ContentHelper helper = TextFinder.getContentHelper(entity.getTextId(), locale);
			infoText = helper.getLocalizedText() != null ? helper.getLocalizedText().getBody() : "";
			nameText = helper.getLocalizedText() != null ? helper.getLocalizedText().getHeadline() : "";
		}
		if (infoText.length() > 0) {
			entity.setInfo(infoText);
		}
		if (nameText.length() > 0) {
			entity.setName(nameText);
		}
	}

}
