/*
 * Created on 1.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.staff.business;

import com.idega.business.IBOHome;

/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface StaffUserBusinessHome extends IBOHome {
	public StaffUserBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
