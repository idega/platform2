package com.idega.block.building.business;


public interface BuildingServiceHome extends com.idega.business.IBOHome
{
 public BuildingService create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}