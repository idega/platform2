package com.idega.block.building.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface BuildingServiceHome extends IBOHome {
	public BuildingService create() throws CreateException, RemoteException;
}