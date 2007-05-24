package com.idega.block.building.business;


import com.idega.block.building.data.ComplexSubcategoryViewHome;
import com.idega.block.building.data.RoomHome;
import com.idega.block.building.data.ComplexHome;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentCategoryHome;
import java.rmi.RemoteException;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Room;
import com.idega.block.building.data.ApartmentSubcategoryHome;
import com.idega.block.building.data.ApartmentSubcategory;
import com.idega.block.building.data.Building;
import com.idega.business.IBOService;
import com.idega.core.builder.data.ICPage;
import com.idega.block.building.data.ApartmentViewHome;
import com.idega.block.building.data.ApartmentTypeHome;
import com.idega.block.building.data.FloorHome;
import com.idega.block.building.data.BuildingHome;
import com.idega.block.building.data.Floor;
import com.idega.block.building.data.ApartmentHome;

public interface BuildingService extends IBOService {
	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#storeComplex
	 */
	public Complex storeComplex(Integer complexID, String name, String info,
			Integer imageID, Integer textID, String flashPageID, Boolean locked)
			throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#storeBuilding
	 */
	public Building storeBuilding(Integer buildingID, String name,
			String address, String info, Integer imageID, Integer complexID,
			Integer textID, Boolean locked) throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#storeFloor
	 */
	public Floor storeFloor(Integer floorID, String name, Integer buildingID,
			String info, Integer imageID, Integer textID)
			throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#storeApartmentCategory
	 */
	public ApartmentCategory storeApartmentCategory(Integer categoryID,
			String name, String info, Integer imageID, Integer textID)
			throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#storeSubcategory
	 */
	public ApartmentSubcategory storeSubcategory(Integer categoryID,
			Integer subcategoryID, String name, String info, Integer imageID,
			Integer textID) throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#storeApartmentType
	 */
	public ApartmentType storeApartmentType(Integer typeID, String name,
			String info, String abbrev, String extraInfo, Integer planID,
			Integer imageID, Integer subcategoryID, Integer textID,
			Double area, Integer roomcount, Integer rent, Boolean balcony,
			Boolean bath, Boolean kitchen, Boolean storage, Boolean study,
			Boolean furniture, Boolean loft, Boolean locked)
			throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#storeApartment
	 */
	public Apartment storeApartment(Integer apartmentID, String name,
			String info, Integer floorID, Integer typeID, Boolean rentable,
			Integer imageID, Integer textID, String apartmentSerialNumber)
			throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#storeRoom
	 */
	public Room storeRoom(Integer roomID) throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#removeComplex
	 */
	public void removeComplex(Integer complexID) throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#removeBuilding
	 */
	public void removeBuilding(Integer buildingID) throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#removeFloor
	 */
	public void removeFloor(Integer floorID) throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#removeApartment
	 */
	public void removeApartment(Integer apartmentID) throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#removeApartmentCategory
	 */
	public void removeApartmentCategory(Integer categoryID)
			throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#removeApartmentType
	 */
	public void removeApartmentType(Integer categoryID) throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#removeSubcategory
	 */
	public void removeSubcategory(Integer subcategoryID) throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#removeRoom
	 */
	public void removeRoom(Integer roomID) throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#getPage
	 */
	public ICPage getPage(int pageID) throws RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#getComplexHome
	 */
	public ComplexHome getComplexHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#getBuildingHome
	 */
	public BuildingHome getBuildingHome() throws RemoteException,
			RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#getFloorHome
	 */
	public FloorHome getFloorHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#getApartmentHome
	 */
	public ApartmentHome getApartmentHome() throws RemoteException,
			RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#getApartmentTypeHome
	 */
	public ApartmentTypeHome getApartmentTypeHome() throws RemoteException,
			RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#getApartmentCategoryHome
	 */
	public ApartmentCategoryHome getApartmentCategoryHome()
			throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#getApartmentSubcategoryHome
	 */
	public ApartmentSubcategoryHome getApartmentSubcategoryHome()
			throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#getRoomHome
	 */
	public RoomHome getRoomHome() throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#getApartmentViewHome
	 */
	public ApartmentViewHome getApartmentViewHome() throws RemoteException,
			RemoteException;

	/**
	 * @see com.idega.block.building.business.BuildingServiceBean#getComplexSubcategoryViewHome
	 */
	public ComplexSubcategoryViewHome getComplexSubcategoryViewHome()
			throws RemoteException, RemoteException;
}