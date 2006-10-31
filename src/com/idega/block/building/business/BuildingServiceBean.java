/*
 * Created on Jan 22, 2004
 *
 */
package com.idega.block.building.business;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.block.building.data.ApartmentCategoryHome;
import com.idega.block.building.data.ApartmentHome;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.ApartmentTypeHome;
import com.idega.block.building.data.ApartmentView;
import com.idega.block.building.data.ApartmentViewHome;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.BuildingHome;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.ComplexHome;
import com.idega.block.building.data.ComplexTypeView;
import com.idega.block.building.data.ComplexTypeViewHome;
import com.idega.block.building.data.Floor;
import com.idega.block.building.data.FloorHome;
import com.idega.block.building.data.Room;
import com.idega.block.building.data.RoomHome;
import com.idega.business.IBOServiceBean;
import com.idega.core.builder.data.ICPage;
import com.idega.core.builder.data.ICPageHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;

/**
 * BuildingServiceBean
 * 
 * @author aron
 * @version 1.0
 */
public class BuildingServiceBean extends IBOServiceBean implements
		BuildingService {

	public Complex storeComplex(Integer complexID, String name, String info,
			Integer imageID, Integer textID, String flashPageID, Boolean locked) {

		try {
			Complex complex = null;
			if (complexID != null && complexID.intValue() > 0) {
				complex = getComplexHome().findByPrimaryKey(complexID);
			} else {
				complex = getComplexHome().create();
			}
			complex.setName(name);
			complex.setInfo(info);
			if (imageID != null && imageID.intValue() > 0)
				complex.setImageId(imageID);
			if (textID != null && textID.intValue() > 0)
				complex.setTextId(textID.intValue());
			if (flashPageID != null && !"".equals(flashPageID)) {
				complex.setFlashPageID(Integer.valueOf(flashPageID).intValue());
			}
			
			complex.setLocked(locked.booleanValue());
			complex.store();
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Building storeBuilding(Integer buildingID, String name,
			String address, String info, Integer imageID, Integer complexID,
			Integer textID, Boolean locked) {
		try {
			Building building = null;

			if (buildingID != null && buildingID.intValue() > 0) {
				building = getBuildingHome().findByPrimaryKey(buildingID);
			} else {
				building = getBuildingHome().create();
			}
			building.setName(name);
			building.setStreet(address);
			building.setInfo(info);
			if (imageID != null && imageID.intValue() > 0)
				building.setImageId(imageID);
			if (complexID != null && complexID.intValue() > 0)
				building.setComplexId(complexID.intValue());

			if (textID != null && textID.intValue() > 0)
				building.setTextId(textID.intValue());
			
			building.setLocked(locked.booleanValue());
			building.store();
			
			return building;
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;

	}

	public Floor storeFloor(Integer floorID, String name, Integer buildingID,
			String info, Integer imageID, Integer textID) {
		try {
			Floor floor = null;
			if (floorID != null && floorID.intValue() > 0) {
				floor = getFloorHome().findByPrimaryKey(floorID);
			} else {
				floor = getFloorHome().create();
			}
			floor.setName(name);
			floor.setBuildingId(buildingID);
			floor.setInfo(info);
			if (imageID != null && imageID.intValue() > 0)
				floor.setImageId(imageID);
			if (textID != null && textID.intValue() > 0)
				floor.setTextId(textID.intValue());
			floor.store();
			return floor;
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ApartmentCategory storeApartmentCategory(Integer categoryID,
			String name, String info, Integer imageID, Integer textID) {
		try {
			ApartmentCategory category = null;
			if (categoryID != null && categoryID.intValue() > 0) {
				category = getApartmentCategoryHome().findByPrimaryKey(
						categoryID);
			} else {
				category = getApartmentCategoryHome().create();
			}
			category.setName(name);
			category.setInfo(info);
			if (imageID != null && imageID.intValue() > 0)
				category.setImageId(imageID);
			if (textID != null && textID.intValue() > 0)
				category.setTextId(textID.intValue());
			category.store();
			return category;
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ApartmentType storeApartmentType(Integer typeID, String name,
			String info, String abbrev, String extraInfo, Integer planID,
			Integer imageID, Integer categoryID, Integer textID, Float area,
			Integer roomcount, Integer rent, Boolean balcony, Boolean bath,
			Boolean kitchen, Boolean storage, Boolean study, Boolean furniture,
			Boolean loft, Boolean locked) {

		try {
			ApartmentType type = null;
			if (typeID != null && typeID.intValue() > 0) {
				type = getApartmentTypeHome().findByPrimaryKey(typeID);
			} else {
				type = getApartmentTypeHome().create();
			}

			type.setName(name);
			type.setInfo(info);
			type.setAbbreviation(abbrev);
			type.setExtraInfo(extraInfo);
			if (planID != null && planID.intValue() > 0)
				type.setFloorPlanId(planID);
			if (imageID != null && imageID.intValue() > 0)
				type.setImageId(imageID);
			if (textID != null && textID.intValue() > 0)
				type.setTextId(textID.intValue());
			type.setApartmentCategoryId(categoryID);
			type.setArea(area);
			type.setRoomCount(roomcount);
			type.setRent(rent);

			type.setBalcony(balcony.booleanValue());
			type.setBathRoom(bath.booleanValue());
			type.setKitchen(kitchen.booleanValue());
			type.setLoft(loft.booleanValue());
			type.setStorage(storage.booleanValue());
			type.setStudy(study.booleanValue());
			type.setFurniture(furniture.booleanValue());
			type.setLocked(locked.booleanValue());

			type.store();
			return type;
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;

	}

	public Apartment storeApartment(Integer apartmentID, String name,
			String info, Integer floorID, Integer typeID, Boolean rentable,
			Integer imageID, Integer textID, String apartmentSerialNumber) {

		try {
			Apartment apartment = null;
			if (apartmentID != null && apartmentID.intValue() > 0) {
				apartment = getApartmentHome().findByPrimaryKey(apartmentID);
			} else {
				apartment = getApartmentHome().create();
			}
			apartment.setName(name);
			apartment.setFloorId(floorID);
			apartment.setApartmentTypeId(typeID);
			apartment.setInfo(info);
			apartment.setRentable(rentable.booleanValue());
			if (imageID != null && imageID.intValue() > 0)
				apartment.setImageId(imageID);

			if (textID != null && textID.intValue() > 0)
				apartment.setTextId(textID.intValue());

			apartment.setSerialNumber(apartmentSerialNumber);
			
			apartment.store();
			return apartment;
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Room storeRoom(Integer roomID) {
		return null;
	}

	public void removeComplex(Integer complexID) {

		try {
			Complex complex = getComplexHome().findByPrimaryKey(complexID);
			complex.remove();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		} catch (RemoveException e) {
			e.printStackTrace();
		}
	}

	public void removeBuilding(Integer buildingID) {
		try {
			Building building = getBuildingHome().findByPrimaryKey(buildingID);
			building.remove();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (RemoveException e) {
			e.printStackTrace();
		}
	}

	public void removeFloor(Integer floorID) {
		try {
			Floor floor = getFloorHome().findByPrimaryKey(floorID);
			floor.remove();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (RemoveException e) {
			e.printStackTrace();
		}

	}

	public void removeApartment(Integer apartmentID) {
		try {
			Apartment apartment = getApartmentHome().findByPrimaryKey(
					apartmentID);
			apartment.remove();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (RemoveException e) {
			e.printStackTrace();
		}
	}

	public void removeApartmentCategory(Integer categoryID) {
		try {
			ApartmentCategory apCat = getApartmentCategoryHome()
					.findByPrimaryKey(categoryID);
			apCat.remove();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (RemoveException e) {
			e.printStackTrace();
		}
	}

	public void removeApartmentType(Integer categoryID) {
		try {
			ApartmentType apType = getApartmentTypeHome().findByPrimaryKey(
					categoryID);
			apType.remove();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (RemoveException e) {
			e.printStackTrace();
		}
	}

	public void removeRoom(Integer roomID) {

	}

	public ICPage getPage(int pageID) {
		try {
			return ((ICPageHome) IDOLookup.getHome(ICPage.class))
					.findByPrimaryKey(pageID);
		} catch (IDOLookupException e) {
		} catch (FinderException e) {
		}

		return null;
	}

	public ComplexHome getComplexHome() throws RemoteException {
		return (ComplexHome) getIDOHome(Complex.class);
	}

	public BuildingHome getBuildingHome() throws RemoteException {
		return (BuildingHome) getIDOHome(Building.class);
	}

	public FloorHome getFloorHome() throws RemoteException {
		return (FloorHome) getIDOHome(Floor.class);
	}

	public ApartmentHome getApartmentHome() throws RemoteException {
		return (ApartmentHome) getIDOHome(Apartment.class);
	}

	public ApartmentTypeHome getApartmentTypeHome() throws RemoteException {
		return (ApartmentTypeHome) getIDOHome(ApartmentType.class);
	}

	public ApartmentCategoryHome getApartmentCategoryHome()
			throws RemoteException {
		return (ApartmentCategoryHome) getIDOHome(ApartmentCategory.class);
	}

	public RoomHome getRoomHome() throws RemoteException {
		return (RoomHome) getIDOHome(Room.class);
	}

	public ApartmentViewHome getApartmentViewHome() throws RemoteException {
		return (ApartmentViewHome) getIDOHome(ApartmentView.class);
	}

	public ComplexTypeViewHome getComplexTypeViewHome() throws RemoteException {
		return (ComplexTypeViewHome) getIDOHome(ComplexTypeView.class);
	}
}