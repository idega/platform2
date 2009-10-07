package com.idega.block.building.business;


import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.business.IBOHome;

public interface BuildingServiceHome extends IBOHome {
	public BuildingService create() throws CreateException, RemoteException;
}