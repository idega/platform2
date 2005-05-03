/*
 * Created on 2005-maj-02
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.school.business;



import com.idega.business.IBOHome;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface SchoolBusinessHome extends IBOHome {
	public SchoolBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
