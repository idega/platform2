package com.idega.block.building.business;

import java.rmi.RemoteException;

import java.util.Locale;

import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.block.building.data.ApartmentCategoryHome;
import com.idega.block.building.data.ApartmentHome;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.ApartmentTypeHome;
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
import com.idega.data.IDOLookupException;
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
		
		try {
			Complex eComplex = getComplexHome().create();
			if (id > 0) {
				eComplex = getComplexHome().findByPrimaryKey(new Integer(id));
			}
			eComplex.setName(sName);
			eComplex.setInfo(sInfo);
			if (imageid > 0)
				eComplex.setImageId(imageid);
			if(textid>0)
				eComplex.setTextId(textid);
			eComplex.store();
			BuildingCacher.reload();
			return true;

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean saveBuilding(int id, String sName, String sAddress, String sInfo, int imageid, int complexid, String sSerie, int textId) {
		
		try {
			Building ebuilding = getBuildingHome().create();
			if (complexid > 0) {
				if (id > 0) {
					ebuilding = getBuildingHome().findByPrimaryKey(new Integer(id));
				}
				ebuilding.setName(sName);
				ebuilding.setStreet(sAddress);
				ebuilding.setInfo(sInfo);
				if (imageid > 0)
					ebuilding.setImageId(imageid);
				ebuilding.setComplexId(complexid);
				ebuilding.setSerie(sSerie);
				if(textId>0)
				ebuilding.setTextId(textId);
				ebuilding.store();
				BuildingCacher.reload();
				return true;
			}
		}
		catch (Exception e) {
		}
		return false;
	}
	public boolean saveFloor(int id, String sName, int buildingid, String sInfo, int imageid, int textid) {
		
		try {
			Floor efloor = getFloorHome().create();
			if (id > 0) {
				efloor = getFloorHome().findByPrimaryKey(new Integer(id));
			}
			efloor.setName(sName);
			efloor.setBuildingId(buildingid);
			efloor.setInfo(sInfo);
			if (imageid > 0)
				efloor.setImageId(imageid);
			if(textid>0)
				efloor.setTextId(textid);
			efloor.store();
			BuildingCacher.reload();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public boolean saveApartmentCategory(int id, String sName, String sInfo, int imageid, int textid) {
		try {
			ApartmentCategory eACategory = getApartmentCategoryHome().create();
			if (id > 0) {
				eACategory = getApartmentCategoryHome().findByPrimaryKey(new Integer(id));
			}
			eACategory.setName(sName);
			eACategory.setInfo(sInfo);
			if (imageid > 0)
				eACategory.setImageId(imageid);
			if(textid>0)
			eACategory.setTextId(textid);
			eACategory.store();
			BuildingCacher.reload();
			return true;
		}
		catch (Exception e) {
		}
		return false;
	}

	public boolean saveApartmentType(int id, String sName, String sInfo, String sExtraInfo, int planid, int imageid, int categoryid, int textid, float area, int roomcount, int rent, boolean balcony, boolean bath, boolean kitchen, boolean storage, boolean study, boolean furniture, boolean loft) {
		try {
			if (categoryid > 0) {
				ApartmentType etype = (getApartmentTypeHome()).create();
				if (id > 0) {
					etype = (getApartmentTypeHome()).findByPrimaryKey(new Integer(id));
				}
				etype.setName(sName);
				etype.setInfo(sInfo);
				etype.setExtraInfo(sExtraInfo);
				if (planid > 0)
					etype.setFloorPlanId(planid);
				if (imageid > 0)
					etype.setImageId(imageid);
				if(textid>0)
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
		catch (Exception e) {
			e.printStackTrace();
		}

	
		return false;
	}
	public boolean saveApartment(int id, String sName, String sInfo, int floorid, int typeid, boolean rentable, int imageid, String sSerie, int textid) {
		try {
			Apartment apartment =getApartmentHome().create();
			if (id > 0) {
				apartment =getApartmentHome().findByPrimaryKey(new Integer(id));
			}
			apartment.setName(sName);
			apartment.setFloorId(floorid);
			apartment.setApartmentTypeId(typeid);
			apartment.setInfo(sInfo);
			apartment.setRentable(rentable);
			if (imageid > 0)
				apartment.setImageId(imageid);
			apartment.setSerie(sSerie);
			if(textid>0)
			apartment.setTextId(textid);

			apartment.store();
			BuildingCacher.reload();
			return true;
		}
		catch (Exception e) {
		}
		return false;
	}

	public void deleteComplex(int id) {
		try {
			((ComplexHome) IDOLookup.getHome(Complex.class)).findByPrimaryKey(new Integer(id)).remove();
			BuildingCacher.reload();
		}
		catch (Exception sql) {
		}
	}
	public void deleteBuilding(int id) {
		try {
			(getBuildingHome()).findByPrimaryKey(new Integer(id)).remove();
			BuildingCacher.reload();
		}
		catch (Exception ex) {
		}
	}
	public BuildingHome getBuildingHome() throws IDOLookupException {
		return (BuildingHome) IDOLookup.getHome(Building.class);
	}
	
	public ComplexHome getComplexHome() throws IDOLookupException {
		return (ComplexHome) IDOLookup.getHome(Complex.class);
	}

	public void deleteFloor(int id) {
		try {
			(getFloorHome()).findByPrimaryKey(new Integer(id)).remove();
			BuildingCacher.reload();
		}
		catch (Exception ex) {

		}
	}
	public FloorHome getFloorHome() throws RemoteException{
		return (FloorHome) IDOLookup.getHome(Floor.class);
	}

	public void deleteApartment(int id) {
		try {
			(getApartmentHome()).findByPrimaryKey(new Integer(id)).remove();
			BuildingCacher.reload();
		}
		catch (Exception ex) {
		}
	}
	public ApartmentHome getApartmentHome()throws RemoteException {
		return (ApartmentHome) IDOLookup.getHome(Apartment.class);
	}

	public void deleteApartmentCategory(int id) {
		try {
			(getApartmentCategoryHome()).findByPrimaryKey(new Integer(id)).remove();
			BuildingCacher.reload();
		}
		catch (Exception ex) {
		}
	}
	public ApartmentCategoryHome getApartmentCategoryHome() throws IDOLookupException {
		return (ApartmentCategoryHome) IDOLookup.getHome(ApartmentCategory.class);
	}

	public void deleteApartmentType(int id) {
		try {
			getApartmentTypeHome().findByPrimaryKey(new Integer(id)).remove();
			BuildingCacher.reload();
		}
		catch (Exception ex) {

		}
	}

	public ApartmentTypeHome getApartmentTypeHome() throws RemoteException{
		return (ApartmentTypeHome) IDOLookup.getHome(ApartmentType.class);
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
