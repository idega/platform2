/*
 * $Id: StaffUserPluginBusinessHome.java,v 1.1 2005/02/01 13:40:21 laddi Exp $
 * Created on 16.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.staff.business.plugin;



import com.idega.business.IBOHome;


/**
 * Last modified: 16.11.2004 15:11:18 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface StaffUserPluginBusinessHome extends IBOHome {

	public StaffUserPluginBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
