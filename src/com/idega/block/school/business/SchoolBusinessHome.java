/**
 * 
 */
package com.idega.block.school.business;



import com.idega.business.IBOHome;

/**
 * @author bluebottle
 *
 */
public interface SchoolBusinessHome extends IBOHome {
	public SchoolBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
