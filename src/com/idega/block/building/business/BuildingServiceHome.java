/**
 * 
 */
package com.idega.block.building.business;



import com.idega.business.IBOHome;

/**
 * @author bluebottle
 *
 */
public interface BuildingServiceHome extends IBOHome {
	public BuildingService create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
