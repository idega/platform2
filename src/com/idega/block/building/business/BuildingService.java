package com.idega.block.building.business;


public interface BuildingService extends com.idega.business.IBOService
{
 public com.idega.block.building.data.ApartmentCategoryHome getApartmentCategoryHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.building.data.ApartmentHome getApartmentHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.building.data.ApartmentTypeHome getApartmentTypeHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.building.data.BuildingHome getBuildingHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.building.data.ComplexHome getComplexHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.building.data.FloorHome getFloorHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.building.data.RoomHome getRoomHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeApartment(java.lang.Integer p0) throws java.rmi.RemoteException;
 public void removeApartmentCategory(java.lang.Integer p0) throws java.rmi.RemoteException;
 public void removeApartmentType(java.lang.Integer p0) throws java.rmi.RemoteException;
 public void removeBuilding(java.lang.Integer p0) throws java.rmi.RemoteException;
 public void removeComplex(java.lang.Integer p0) throws java.rmi.RemoteException;
 public void removeFloor(java.lang.Integer p0) throws java.rmi.RemoteException;
 public void removeRoom(java.lang.Integer p0) throws java.rmi.RemoteException;
 public com.idega.block.building.data.ApartmentCategory saveApartmentCategory(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.Integer p3,java.lang.Integer p4) throws java.rmi.RemoteException;
 public com.idega.block.building.data.Apartment storeApartment(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.Integer p3,java.lang.Integer p4,java.lang.Boolean p5,java.lang.Integer p6,java.lang.Integer p7) throws java.rmi.RemoteException;
 public com.idega.block.building.data.ApartmentType storeApartmentType(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.Integer p4,java.lang.Integer p5,java.lang.Integer p6,java.lang.Integer p7,java.lang.Float p8,java.lang.Integer p9,java.lang.Integer p10,java.lang.Boolean p11,java.lang.Boolean p12,java.lang.Boolean p13,java.lang.Boolean p14,java.lang.Boolean p15,java.lang.Boolean p16,java.lang.Boolean p17) throws java.rmi.RemoteException;
 public com.idega.block.building.data.Building storeBuilding(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.Integer p4,java.lang.Integer p5,java.lang.Integer p6) throws java.rmi.RemoteException;
 public com.idega.block.building.data.Complex storeComplex(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.Integer p3,java.lang.Integer p4) throws java.rmi.RemoteException;
 public com.idega.block.building.data.Floor storeFloor(java.lang.Integer p0,java.lang.String p1,java.lang.Integer p2,java.lang.String p3,java.lang.Integer p4,java.lang.Integer p5) throws java.rmi.RemoteException;
 public com.idega.block.building.data.Room storeRoom(java.lang.Integer p0) throws java.rmi.RemoteException;
}
