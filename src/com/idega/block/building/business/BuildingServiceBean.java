/*
 * Created on Jan 22, 2004
 *
 */
package com.idega.block.building.business;

import java.rmi.RemoteException;

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
import com.idega.block.building.data.Room;
import com.idega.block.building.data.RoomHome;
import com.idega.business.IBOServiceBean;

/**
 * BuildingServiceBean
 * @author aron 
 * @version 1.0
 */
public class BuildingServiceBean extends IBOServiceBean implements BuildingService {
	
	public Complex storeComplex(Integer complexID, String name, String info, Integer imageID, Integer textID) {
		return null;
	}
	
	public Building storeBuilding(Integer buildingID, String name, String address, String info, Integer imageID, Integer complexid, Integer textID){
		return null;
	}
	
	public Floor storeFloor(Integer floorID, String name, Integer buildingID, String info, Integer imageID, Integer textID) {
		return null;
	}
	
	public ApartmentCategory saveApartmentCategory(Integer categoryID, String name, String info, Integer imageID, Integer textID) {
		return null;
	}
	
	public ApartmentType storeApartmentType(Integer typeID, String name, String info, String extraInfo, Integer planID, Integer imageID, Integer categoryID, Integer textID, Float area, Integer roomcount, Integer rent, Boolean balcony, Boolean bath, Boolean kitchen, Boolean storage, Boolean study, Boolean furniture, Boolean loft) {
		return null;
	}
	
	public Apartment storeApartment(Integer apartmentID, String name, String info, Integer floorID, Integer typeID, Boolean rentable, Integer imageID, Integer textID) {
		return null;
	}
	
	public Room storeRoom(Integer roomID){
		return null;
	}
	
	public void removeComplex(Integer complexID) {
		
	}
	
	public void removeBuilding(Integer buildingID) {
		
	}
	
	public void removeFloor(Integer floorID) {
		
	}
	
	public void removeApartment(Integer apartmentID) {
		
	}
	
	public void removeApartmentCategory(Integer categoryID) {
		
	}
	
	public void removeApartmentType(Integer categoryID) {
		
	}
	
	public void removeRoom(Integer roomID) {
		
	}
	
	public ComplexHome getComplexHome()throws RemoteException{
		return (ComplexHome) getIDOHome(Complex.class);
	}
	public BuildingHome getBuildingHome()throws RemoteException{
		return (BuildingHome) getIDOHome(Building.class);
	}
	public FloorHome getFloorHome()throws RemoteException{
		return (FloorHome) getIDOHome(Floor.class);
	}
	public ApartmentHome getApartmentHome()throws RemoteException{
		return (ApartmentHome) getIDOHome(Apartment.class);
	}
	public ApartmentTypeHome getApartmentTypeHome()throws RemoteException{
		return (ApartmentTypeHome) getIDOHome(ApartmentType.class);
	}
	public ApartmentCategoryHome getApartmentCategoryHome()throws RemoteException{
		return (ApartmentCategoryHome) getIDOHome(ApartmentCategory.class);
	}
	public RoomHome getRoomHome()throws RemoteException{
		return (RoomHome) getIDOHome(Room.class);
	}
/*	public ApartmentViewHome getApartmentViewHome()throws RemoteException{
		return (ApartmentViewHome) getIDOHome(ApartmentView.class);
	}*/
	/*
	public ComplexTypeViewHome getComplexTypeViewHome() throws RemoteException{
		return (ComplexTypeViewHome) getIDOHome(ComplexTypeView.class);
	}
	
	public void switchNameAndInfo(BuildingEntity entity, java.util.Locale locale) {
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
	}*/
	
	
}
